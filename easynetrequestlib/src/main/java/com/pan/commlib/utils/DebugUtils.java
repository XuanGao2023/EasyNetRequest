package com.pan.commlib.utils;


import android.util.Log;

import com.pan.commlib.core.Config;


public class DebugUtils {

	public static void println(String tag, String strmsg) {
		if (tag == null) tag = "";
		if (Config.TESTING) {
			Long id = Thread.currentThread().getId();
			System.out.println("" + id.intValue() + "\t" + tag + "\t" + strmsg);
		} else {
			Log.d(tag, strmsg);
		}
	}

	public static void println(String strmsg) {
		println(null, strmsg);
	}
}
