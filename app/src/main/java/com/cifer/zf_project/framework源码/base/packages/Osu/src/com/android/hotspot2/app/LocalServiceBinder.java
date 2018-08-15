package com.cifer.zf_project.framework源码.base.packages.Osu.src.com.android.hotspot2.app;

import android.os.Binder;

public class LocalServiceBinder extends Binder {
    private final OSUService mDelegate;

    public LocalServiceBinder(OSUService delegate) {
        mDelegate = delegate;
    }

    public OSUService getService() {
        return mDelegate;
    }
}
