package com.cifer.zf_project.framework源码.base.packages.Osu.src.com.android.hotspot2.asn1;

import java.io.IOException;

public class DecodeException extends IOException {
    private final int mOffset;

    public DecodeException(String message, int offset) {
        super(message);
        mOffset = offset;
    }

    @Override
    public String toString() {
        return super.toString() + " at " + mOffset;
    }
}
