以DeskClock为例:
public static void showAlarmNotification(Context context, AlarmInstance instance) {
        ...
        Notification.Builder notification = new Notification.Builder(context)//构建Notification
                .setContentTitle(resources.getText(R.string.menu_alarm)/*instance.getLabelOrDefault(context)*/)
                .setContentText(AlarmUtils.getFormattedTime(context, instance.getAlarmTime()))
                .setSmallIcon(R.drawable.stat_notify_alarm)
				.setLargeIcon(BitmapFactory.decodeResource(resources,  R.drawable.alarm_icon_notification))
                .setOngoing(true)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setWhen(0);

        // Setup Snooze Action
        Intent snoozeIntent = AlarmStateManager.createStateChangeIntent(context, "SNOOZE_TAG",
                instance, AlarmInstance.SNOOZE_STATE);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, instance.hashCode(),
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.addAction(0/*R.drawable.stat_notify_alarm*/,
                resources.getString(R.string.alarm_alert_snooze_text), snoozePendingIntent);
		...
        nm.cancel(instance.hashCode());
        nm.notify(instance.hashCode(), notification.build());//发送此通知
    }

--->nm为NotificationManager对象,调用NotificationManager.notify

--->public void notify(String tag, int id, Notification notification)
    {
        ...
        try {
			service.enqueueNotificationWithTag(pkg, mContext.getOpPackageName(), tag, id,
                    notification, idOut, UserHandle.myUserId());
            if (id != idOut[0]) {
                Log.w(TAG, "notify: id corrupted: sent " + id + ", got back " + idOut[0]);
            }
        } catch (RemoteException e) {
        }
    }

--->service为NotificationManagerService对象

--->public void enqueueNotificationWithTag(String pkg, String basePkg, String tag, int id,
            Notification notification, int[] idOut, int userId)
    {
        enqueueNotificationInternal(pkg, basePkg, Binder.getCallingUid(), Binder.getCallingPid(),
                tag, id, notification, idOut, userId);
    }

--->public void enqueueNotificationInternal(final String pkg, String basePkg, final int callingUid,
            final int callingPid, final String tag, final int id, final Notification notification,
            int[] idOut, int incomingUserId)
    {
		....
		mHandler.post(new Runnable() {
            @Override
            public void run() {
				....
                synchronized (mNotificationList) {//同步锁
                    final StatusBarNotification n = new StatusBarNotification(
                            pkg, id, tag, callingUid, callingPid, score, notification, user);
                    NotificationRecord r = new NotificationRecord(n);
                    NotificationRecord old = null;

                    int index = indexOfNotificationLocked(pkg, tag, id, userId);
                    if (index < 0) {//消息列表添加新发的消息
                        mNotificationList.add(r);
                    } else {//旧消息列表更新
                        old = mNotificationList.remove(index);
                        mNotificationList.add(index, r);
                        // Make sure we don't lose the foreground service state.
                        if (old != null) {
                            notification.flags |=
                                old.getNotification().flags & Notification.FLAG_FOREGROUND_SERVICE;

                            /// M: [ALPS01233835] Reuse old notification time to prevent notifications from jumping
                            notification.when = old.getNotification().when;
                        }
                    }

                    ........

					if (notification.icon != 0
                        /// M: Do not show notifications if FLAG_HIDE_NOTIFICATION is on
                        && (notification.flags & Notification.FLAG_HIDE_NOTIFICATION) == 0) {
                        if (old != null && old.statusBarKey != null) {
                            r.statusBarKey = old.statusBarKey;
                            long identity = Binder.clearCallingIdentity();
                            try {
                                mStatusBar.updateNotification(r.statusBarKey, n);//旧消息更新
                            }
                            finally {
                                Binder.restoreCallingIdentity(identity);
                            }
                        } else {
                            long identity = Binder.clearCallingIdentity();
                            try {
                                r.statusBarKey = mStatusBar.addNotification(n);//表示新发的消息
                                if ((n.getNotification().flags & Notification.FLAG_SHOW_LIGHTS) != 0
                                        && canInterrupt) {
                                    /*led patch start */
                                    //mAttentionLight.pulse();
                                    /*led patch end */
                                }
                            }
                            finally {
                                Binder.restoreCallingIdentity(identity);
                            }
                        }
                        // Send accessibility events only for the current user.
                        if (currentUser == userId) {
                            sendAccessibilityEvent(notification, pkg);
                        }

                        notifyPostedLocked(r);
                    } else {
                        Slog.e(TAG, "Not posting notification with icon==0: " + notification);
                        if (old != null && old.statusBarKey != null) {
                            long identity = Binder.clearCallingIdentity();
                            try {
                                mStatusBar.removeNotification(old.statusBarKey);
                            }
                            finally {
                                Binder.restoreCallingIdentity(identity);
                            }

                            notifyRemovedLocked(r);
                        }
                        // ATTENTION: in a future release we will bail out here
                        // so that we do not play sounds, show lights, etc. for invalid notifications
                        Slog.e(TAG, "WARNING: In a future release this will crash the app: "
                                + n.getPackageName());
                    }

					......
        
		});

	...
	}

--->先看新消息的添加mStatusBar.addNotification(n)
--->mStatusBar为StatusBarManagerService类型
---->public IBinder addNotification(StatusBarNotification notification) {
        synchronized (mNotifications) {
            IBinder key = new Binder();
            mNotifications.put(key, notification);
            if (mBar != null) {
                try {
                    mBar.addNotification(key, notification);
                } catch (RemoteException ex) {
                }
            }
            return key;
        }
    }

--->mBar(很熟悉，即在registerStatusBar方法时赋值mBar = bar；即BaseStatusBar中创建的CommandQueue对象)

--->跳至PhoneStatusBar中的
	public void addNotification(IBinder key, StatusBarNotification notification) {
					if (DEBUG) Log.d(TAG, "addNotification score=" + notification.getScore());
					Entry shadeEntry = createNotificationViews(key, notification);
					if (shadeEntry == null) {
						return;
					}
					/// M: [ALPS00512845] Handle SD Swap Condition.
					if (SUPPORT_SD_SWAP) {
						try {
							ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(notification.getPackageName(), 0);
							if ((applicationInfo.flags & applicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
								if (mAvoidSDAppAddNotification) {
									return;
								}
								if (!mNeedRemoveKeys.contains(key)) {
									mNeedRemoveKeys.add(key);
								}
								Log.d(TAG, "addNotification, applicationInfo pkg = " + notification.getPackageName() + " to remove notification key = " + key);
							}
						} catch (NameNotFoundException e1) {
							e1.printStackTrace();
						}
					}

					........

					addNotificationViews(shadeEntry);

					// Recalculate the position of the sliding windows and the titles.
					setAreThereNotifications();
					updateExpandedViewPos(EXPANDED_LEAVE_ALONE);
				}

--->首先调用父类BaseStatusBar中的createNotificationViews方法:
	protected NotificationData.Entry createNotificationViews(IBinder key,
            StatusBarNotification notification) {
        ...
        // Construct the expanded view.
        NotificationData.Entry entry = new NotificationData.Entry(key, notification, iconView);
        if (!inflateViews(entry, mPile)) {
            handleNotificationError(key, notification, "Couldn't expand RemoteViews for: "
                    + notification);
            return null;
        }
        return entry;
    }
--->判断条件中执行inflateViews(entry, mPile)

--->public boolean inflateViews(NotificationData.Entry entry, ViewGroup parent) {
        int minHeight =
                mContext.getResources().getDimensionPixelSize(R.dimen.notification_min_height);
        int maxHeight =
                mContext.getResources().getDimensionPixelSize(R.dimen.notification_max_height);
        StatusBarNotification sbn = entry.notification;
        RemoteViews contentView = sbn.getNotification().contentView;
        RemoteViews bigContentView = sbn.getNotification().bigContentView;
        if (contentView == null) {
            return false;
        }

        // create the row view
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        ExpandableNotificationRow row = (ExpandableNotificationRow) inflater.inflate(
                R.layout.status_bar_notification_row, parent, false);

        .....
        
        entry.row = row;
        entry.row.setRowHeight(mRowHeight);
        entry.content = content;
        entry.expanded = contentViewLocal;
        entry.setBigContentView(bigContentViewLocal);

        /// M: Support SIM Info Notification.
        updateNotificationSimInfo(entry);

        return true;
    }

--->entry.row = row;(ExpandableNotificationRow)

--->往下会调用BaseStatusBar中的addNotificationViews方法:

--->protected void addNotificationViews(NotificationData.Entry entry) {
        // Add the expanded view and icon.
        int pos = mNotificationData.add(entry);
        if (DEBUG) {
            Log.d(TAG, "addNotificationViews: added at " + pos);
        }
        updateExpansionStates();
        updateNotificationIcons();
    }

--->updateNotificationIcons()抽象方法又在其子类PhoneStatusBar中实现

--->@Override
	protected void updateNotificationIcons() {
		if (mNotificationIcons == null) return;
		Log.i("zyl break","PhoneStatusBar--updateNotificationIcons()--------------------->done");
		loadNotificationShade();

		....
	}

--->loadNotificationShade()方法：
--->private void loadNotificationShade() {
				...
				for (int i=0; i<N; i++) {
						Entry ent = mNotificationData.get(N-i-1);
						if (!(provisioned || showNotificationEvenIfUnprovisioned(ent.notification))) continue;
						if (!notificationIsForCurrentUser(ent.notification)) continue;
						toShow.add(ent.row);//expanded view
					}
					...
					for (int i=0; i<toShow.size(); i++) {
						View v = toShow.get(i);
						if (v.getParent() == null) {
							mPile.addView(v, i);
						}
					}

					...
				}

--->mPile = (NotificationRowLayout)mStatusBarWindow.findViewById(R.id.latestItems);即下拉状态栏布局中的消息通知





//04.19:
------>PhoneStatusBar:
updateNotificationShade(---->
		for (int i=0; i<toShow.size(); i++) {
            View v = toShow.get(i);
            if (v.getParent() == null) {
                mStackScroller.addView(v);
            }
        }










