点击状态栏弹出一部分

a.)Activity——>dispatchTouchEvent(){
	if (getWindow().superDispatchTouchEvent(ev)) {——>mWindow=PolicyManager.makeNewWindow()——>PhoneWindow.
            return true;							superDispatchTouchEvent()——>DecorView(FrameLayout).
    }											    superDispatchTouchEvent()——>ViewGroup.
    return onTouchEvent(ev);						DispatchTouchEvent()
}
b.)ViewGroup.DispatchTouchEvent()——>false:Activity.onTouchEvent(ev)
								 ——>true:表示此事件被子view消费掉，此事件的传递也就到此为止

c.)ViewGroup.DispatchTouchEvent()中调用intercepted = onInterceptTouchEvent(ev);
——>如果设置了阻断：DispatchTouchEvent()返回false；那么这个事件不会再继续往下传递，false:Activity.onTouchEvent(ev)

d.)<com.android.systemui.statusbar.phone.StatusBarWindowView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:systemui="http://schemas.android.com/apk/res/com.android.systemui"
    android:focusable="true"
    android:descendantFocusability="afterDescendants"
    android:fitsSystemWindows="true"
    android:background="@android:color/transparent"
    >

    <include layout="@layout/status_bar"
        android:layout_width="match_parent"
        android:layout_height="@*android:dimen/status_bar_height"
        />

    <com.android.systemui.statusbar.phone.PanelHolder
        android:id="@+id/panel_holder"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
        <include layout="@layout/status_bar_expanded"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />
        <ViewStub android:id="@+id/quick_settings_stub"
            android:layout="@layout/quick_settings"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />
    </com.android.systemui.statusbar.phone.PanelHolder>

</com.android.systemui.statusbar.phone.StatusBarWindowView>

e.)StatusBarWindowView:(extends FrameLayout)
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        if (mNotificationPanel.isFullyExpanded() && mScrollView.getVisibility() == View.VISIBLE) {
            intercept = mExpandHelper.onInterceptTouchEvent(ev);
        }
        if (!intercept) {
            super.onInterceptTouchEvent(ev);//默认false；不拦截事件的传递
        }
        if (intercept) {
            MotionEvent cancellation = MotionEvent.obtain(ev);
            cancellation.setAction(MotionEvent.ACTION_CANCEL);
            latestItems.onInterceptTouchEvent(cancellation);
            cancellation.recycle();
        }
        return intercept;
    }
f.)ViewGroup中:
--->						if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                mLastTouchDownIndex = childIndex;
                                mLastTouchDownX = ev.getX();
                                mLastTouchDownY = ev.getY();
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }

--->if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            if (DBG_MOTION) {
                Xlog.d(TAG, "Dispatch cancel action end: handled = " + handled + ",oldAction = "
                        + oldAction + ",child = " + child + ",this = " + this);
            }
            return handled;
        }
--->child.dispatchTouchEvent(event);

---><include layout="@layout/status_bar"//com.android.systemui.statusbar.phone.PhoneStatusBarView(没做事件处理)
        android:layout_width="match_parent"
        android:layout_height="@*android:dimen/status_bar_height"
        />

--->...

--->com.android.systemui.statusbar.phone.NotificationPanelView(extends PanelView)

--->View:
	public boolean dispatchTouchEvent(MotionEvent event) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(event, 0);
       ...
        if (onFilterTouchEventForSecurity(event)) {
            //noinspection SimplifiableIfStatement
            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                /// M : add log to help debugging
                if (DBG_TOUCH) {
                    Xlog.d(VIEW_LOG_TAG, "handle Touch event by listerner, listener = " + li + ", event = "
                        + event + ", this = " + this);
                }
                return true;
            }

            if (onTouchEvent(event)) {
                /// M : add log to help debugging
                if (DBG_TOUCH) {
                    Xlog.d(VIEW_LOG_TAG, "handle Touch event by onTouchEvent, event = "
                        + event + ", this = " + this);
                }
                return true;
            }
        }

    }

--->如果view实现了OnTouchListener接口，则优先执行此方法;如果没有，则执行onTouchEvent(event)方法

--->PanelView:
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        return mHandleView.dispatchTouchEvent(event);
    }

--->mHandleView.setOnTouchListener(new View.OnTouchListener() {
		....
		
		switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            mTracking = true;
                            mHandleView.setPressed(true);
                            postInvalidate(); // catch the press state change
                            mInitialTouchY = y;
                            mVelocityTracker = FlingTracker.obtain();
                            trackMovement(event);
                            mTimeAnimator.cancel(); // end any outstanding animations
                            mBar.onTrackingStarted(PanelView.this);
                            mTouchOffset = (rawY - mAbsPos[1]) - mExpandedHeight;
                            if (mExpandedHeight == 0) {
                                mJustPeeked = true;
                                runPeekAnimation();
                            }
                            break;

		....

	}

--->runPeekAnimation();


--->private void runPeekAnimation() {
        if (DEBUG) logf("peek to height=%.1f", mPeekHeight);
        if (mTimeAnimator.isStarted()) {
            return;
        }
        if (mPeekAnimator == null) {
            mPeekAnimator = ObjectAnimator.ofFloat(this,
                    "expandedHeight", mPeekHeight)
                .setDuration(250);
        }
        mPeekAnimator.start();
    }



	

