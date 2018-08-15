package com.cifer.zf_project.framework源码.base.packages.Osu.src.com.android.hotspot2.omadm;

public class NodeAttribute {
    private final String mName;
    private final String mType;
    private final String mValue;

    public NodeAttribute(String name, String type, String value) {
        mName = name;
        mType = type;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public String getType() {
        return mType;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) = '%s'", mName, mType, mValue);
    }
}
