package com.cifer.zf_project.ZF_SalesTracker;

import android.os.SystemProperties;

public final class FeatureOption
{
	//public static final boolean TL_SMS_SALE_TRACKER = "1".equals(SystemProperties.get("ro.sales_tracker_support"));
    public static final boolean TL_SMS_SALE_TRACKER = true;	
	public static final boolean GEMINI_SUPPORT = "1".equals(SystemProperties.get("ro.mtk_gemini_support"));
}
