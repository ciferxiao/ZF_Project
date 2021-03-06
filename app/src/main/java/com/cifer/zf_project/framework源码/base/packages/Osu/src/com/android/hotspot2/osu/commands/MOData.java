package com.cifer.zf_project.framework源码.base.packages.Osu.src.com.android.hotspot2.osu.commands;

import android.net.wifi.PasspointManagementObjectDefinition;

import com.android.hotspot2.omadm.MOTree;
import com.android.hotspot2.omadm.OMAConstants;
import com.android.hotspot2.omadm.OMAParser;
import com.android.hotspot2.omadm.XMLNode;

import org.xml.sax.SAXException;

import java.io.IOException;

public class MOData implements OSUCommandData {
    private final String mBaseURI;
    private final String mURN;
    private final MOTree mMOTree;

    public MOData(XMLNode root) {
        mBaseURI = root.getAttributeValue("spp:managementTreeURI");
        mURN = root.getAttributeValue("spp:moURN");
        mMOTree = root.getMOTree();
    }

    public String getBaseURI() {
        return mBaseURI;
    }

    public String getURN() {
        return mURN;
    }

    public MOTree getMOTree() {
        return mMOTree;
    }

    @Override
    public String toString() {
        return "Base URI: " + mBaseURI + ", MO: " + mMOTree;
    }
}
