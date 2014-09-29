package cn.gandalf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class DefaultProperties {
	public static final String ROOT_PATH = "me.hobbits.leimao.freevip";

	public final static String PATH_SD = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public final static String PATH_TEMP = PATH_SD + "/"
			+ ROOT_PATH + "/temp";

	public static boolean getBoolPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, false);
	}

	public static boolean getBoolPref(Context context, String key,
			boolean defaultValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, defaultValue);
	}

	public static void setBoolPref(Context context, String key, boolean value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putBoolean(key, value).commit();
	}

	public static int getIntPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getInt(key, -1);
	}

	public static void setIntPref(Context context, String key, int value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putInt(key, value).commit();
	}

	public static float getFloatPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(key, -1f);
	}

	public static void setFloatPref(Context context, String key, float value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putFloat(key, value).commit();
	}

	public static long getLongPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getLong(key, -1);
	}

	public static void setStringPref(Context context, String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putString(key, value).commit();
	}

	public static String getStringPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(key, "");
	}

	public static void setLongPref(Context context, String key, long value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putLong(key, value).commit();
	}
}
