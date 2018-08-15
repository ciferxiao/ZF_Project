package com.cifer.zf_project.framework源码.base.packages.Osu.src.com.android.anqp;

/**
 * Base class for an IEEE802.11u ANQP element.
 */
public abstract class ANQPElement {
    private final Constants.ANQPElementType mID;

    protected ANQPElement(Constants.ANQPElementType id) {
        mID = id;
    }

    public Constants.ANQPElementType getID() {
        return mID;
    }
}
