package com.bosseditor;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Global {

	public static final String PREF_BRIGHTNESS="pref_bright";
	public static final String PREF_CONTRAST="pref_contrast";
	
	public static float getPreferenceFloat(Context c,String pref, Float def_val){
		return PreferenceManager.getDefaultSharedPreferences(c).getFloat(pref, def_val);
	}
	public static void setPreferenceFloat(Context c,String pref, Float val){
		Editor e=PreferenceManager.getDefaultSharedPreferences(c).edit();
		e.putFloat(pref, val).commit();
	}
	
}
