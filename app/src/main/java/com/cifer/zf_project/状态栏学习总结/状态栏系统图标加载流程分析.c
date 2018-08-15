PhoneStatusBar.java
---->super.start()即BaseStatusBar.start
---->createAndAddWindows()
---->PhoneStatusBar中复写了此方法-->@Override
					public void createAndAddWindows() {
						addStatusBarWindow();
					}

-->addStatusBarWindow()中:(a)makeStatusBarView()方法即之前提到的加载状态栏布局(b)其后的mWindowManager.addView(mStatusBarWindow, lp)方法即将状态栏添加到window显示，总在最前.(so,主要看makeStatusBarView()加在布局流程，现在先分析状态栏未展开时系统图标的加载)

--->makeStatusBarView()方法中:

--->mIconPolicy = new PhoneStatusBarPolicy(mContext);一步

调用构造方法public PhoneStatusBarPolicy(Context context) {
        mContext = context;
        mService = (StatusBarManager)context.getSystemService(Context.STATUS_BAR_SERVICE);
		
        mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
		.....
		.....
        // TTY status
        mService.setIcon("tty",  R.drawable.stat_sys_tty_mode, 0, null);
        mService.setIconVisibility("tty", false);

        // Cdma Roaming Indicator, ERI
        mService.setIcon("cdma_eri", R.drawable.stat_sys_roaming_cdma_0, 0, null);
        mService.setIconVisibility("cdma_eri", false);

        // bluetooth status
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        int bluetoothIcon = R.drawable.stat_sys_data_bluetooth;
        if (adapter != null) {
            mBluetoothEnabled = (adapter.getState() == BluetoothAdapter.STATE_ON);
            if (adapter.getConnectionState() == BluetoothAdapter.STATE_CONNECTED) {
                bluetoothIcon = R.drawable.stat_sys_data_bluetooth_connected;
            }
        }
        Log.v(TAG, "updateBluetooth, init: mBluetoothEnabled = " + mBluetoothEnabled);
        mService.setIcon("bluetooth", bluetoothIcon, 0, null);
		
			Log.v(TAG, "smart_eye show");
        mService.setIconVisibility("bluetooth", mBluetoothEnabled);

        // Alarm clock
        mService.setIcon("alarm_clock", R.drawable.stat_sys_alarm1, 0, null);
        mService.setIconVisibility("alarm_clock", false);

        // Sync state
        mService.setIcon("sync_active", R.drawable.stat_sys_sync, 0, null);
        mService.setIconVisibility("sync_active", false);
        // "sync_failing" is obsolete: b/1297963

        // volume
        mService.setIcon("volume", R.drawable.stat_sys_ringer_silent, 0, null);
        mService.setIconVisibility("volume", false);
        updateVolume();
		.....	
		.....

    }(mService.setIcon()即为入口点,通过Log得知mService)

--->mService为StatusBarManager
--->StatusBarManager中：public void setIcon(String slot, int iconId, int iconLevel, String contentDescription) 
        try {
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIcon(slot, mContext.getPackageName(), iconId, iconLevel,
                    contentDescription);
       ......
    }

--->svc为 mService = IStatusBarService.Stub.asInterface(
                    ServiceManager.getService(Context.STATUS_BAR_SERVICE));

	即StatusBarManagerService类(extends IStatusBarService.Stub)

--->setIcon(String slot, String iconPackage, int iconId, int iconLevel,String contentDescription)方法中：
--->mBar.setIcon(index, icon);//
	***icon: StatusBarIcon icon = new StatusBarIcon(iconPackage, UserHandle.OWNER, iconId,
                    iconLevel, 0,
                    contentDescription);

--->mBar的赋值是在StatusBarManagerService类中的registerStatusBar()回调方法中操作的
--->registerStatusBar(IStatusBar bar, StatusBarIconList iconList,....)是在BaseStatusBar.start()时调用

--->public void start() {
        ...
        try {
            mBarService.registerStatusBar(mCommandQueue, iconList, notificationKeys, notifications,
                    switches, binders);
        } catch (RemoteException ex) {
            // If the system process isn't there we're doomed anyway.
        }

        createAndAddWindows();

        ....
    }
	参数
	a)mCommandQueue: mCommandQueue = new CommandQueue(this, iconList);
	***CommandQueue extends IStatusBar.Stub
	b)iconList:  StatusBarIconList iconList = new StatusBarIconList();

--->mBar=mCommandQueue
--->进入到CommandQueue.setIcon()
--->public void setIcon(int index, StatusBarIcon icon) {
        synchronized (mList) {
            ...
            mHandler.obtainMessage(what, OP_SET_ICON, 0, icon.clone()).sendToTarget();//发送消息
        }
    }
--->处理此消息:case OP_SET_ICON: {
                            StatusBarIcon icon = (StatusBarIcon)msg.obj;
                            StatusBarIcon old = mList.getIcon(index);
                            if (old == null) {//第一次加载
                                mList.setIcon(index, icon);
                                mCallbacks.addIcon(mList.getSlot(index), index, viewIndex, icon);
								StackTraceElement st[]= Thread.currentThread().getStackTrace();  
                            } else {//此后不再add而仅仅是updateIcon
                                mList.setIcon(index, icon);
                                mCallbacks.updateIcon(mList.getSlot(index), index, viewIndex,
                                        old, icon);
                            }
                            break;
                        }

--->这里先分析第一次加载：即mCallbacks.addIcon(mList.getSlot(index), index, viewIndex, icon);

--->mCallbacks:在注册StatusBarManagerService时传入的参数mCommandQueue = new CommandQueue(this, iconList);
	对比CommandQueue的构造函数
	public CommandQueue(Callbacks callbacks, StatusBarIconList list) {
        mCallbacks = callbacks;
        mList = list;
    }

--->mCallbacks即 类BaseStatusBar implements CommandQueue.Callbacks;但是BaseStatusBar中并没有实现addIcon方法,其子类PhoneStatusBar实现了此方法:
	public void addIcon(String slot, int index, int viewIndex, StatusBarIcon icon) {
					if (SPEW) Log.d(TAG, "addIcon slot=" + slot + " index=" + index + " viewIndex=" + viewIndex
							+ " icon=" + icon);
					StatusBarIconView view = new StatusBarIconView(mContext, slot, null);
					view.set(icon);
					mStatusIcons.addView(view, viewIndex, new LinearLayout.LayoutParams(mIconSize, mIconSize));
				}

--->view.set(icon)中:
	调用updateDrawable:
	private boolean updateDrawable(boolean withClear) {
        Drawable drawable = getIcon(mIcon);
        if (drawable == null) {
            Log.w(TAG, "No icon for slot " + mSlot);
            return false;
        }
        if (withClear) {
            setImageDrawable(null);
        }
        setImageDrawable(drawable);
        return true;
    }
--->设置图标	
--->mStatusIcons即布局中的系统图标;











