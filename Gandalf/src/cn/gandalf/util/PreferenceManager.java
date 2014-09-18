package cn.gandalf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {

	public static final long TIME_FOREVER = -1;
	public static final long TIME_INVALID = -2;
	private static final String SUFFIX_TIME = "_time";
	private static final String SUFFIX_COUNT = "_count";
	private static PreferenceManager instance = null;
	private Context mContext;
	private SharedPreferences mPreferences;

	private PreferenceManager(Context context) {
		mContext = context.getApplicationContext();
		String name = mContext.getPackageName();
		mPreferences = mContext
				.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static synchronized PreferenceManager getInstance(Context context) {
		if (instance == null) {
			instance = new PreferenceManager(context);
		}
		return instance;
	}

	public void putBoolean(String key, boolean value) {
		putBoolean(key, value, TIME_FOREVER);
	}

	public void putBoolean(String key, boolean value, long period) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
		putPeriod(key, period);
	}

	public boolean getBoolean(String key, boolean defValue) {
		if (checkPeriod(key)) {
			return mPreferences.getBoolean(key, defValue);
		}
		return defValue;
	}

	public void putFloat(String key, float value) {
		putFloat(key, value, TIME_FOREVER);
	}

	public void putFloat(String key, float value, long period) {
		Editor editor = mPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
		putPeriod(key, period);
	}

	public float getFloat(String key, float defValue) {
		if (checkPeriod(key)) {
			return mPreferences.getFloat(key, defValue);
		}
		return defValue;
	}

	public void putInt(String key, int value) {
		putInt(key, value, TIME_FOREVER);
	}

	public void putInt(String key, int value, long period) {
		Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
		putPeriod(key, period);
	}

	public int getInt(String key, int defValue) {
		if (checkPeriod(key)) {
			return mPreferences.getInt(key, defValue);
		}
		return defValue;
	}

	public void putLong(String key, long value) {
		putLong(key, value, TIME_FOREVER);
	}

	public void putLong(String key, long value, long period) {
		Editor editor = mPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
		putPeriod(key, period);
	}

	public long getLong(String key, long defValue) {
		if (checkPeriod(key)) {
			return mPreferences.getLong(key, defValue);
		}
		return defValue;
	}

	public void putString(String key, String value) {
		putString(key, value, TIME_FOREVER);
	}

	public void putString(String key, String value, long period) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
		putPeriod(key, period);
	}

	public String getString(String key, String defValue) {
		if (checkPeriod(key)) {
			return mPreferences.getString(key, defValue);
		}
		return defValue;
	}

	public void putPeriod(String key, long period) {
		Editor editor = mPreferences.edit();
		editor.putLong(key + SUFFIX_TIME, getLimitedTime(period));
		editor.commit();
	}

	private long getLimitedTime(long period) {
		if (TIME_INVALID == period) {
			return TIME_INVALID;
		} else if (TIME_FOREVER == period) {
			return TIME_FOREVER;
		}
		return System.currentTimeMillis() + period;
	}

	public boolean checkPeriod(String key) {
		long period = mPreferences.getLong(key + SUFFIX_TIME, TIME_INVALID);
		return checkPeriod(period);
	}

	private boolean checkPeriod(long period) {
		if (period == TIME_INVALID) {
			return false;
		} else if (period == TIME_FOREVER) {
			return true;
		} else if (System.currentTimeMillis() < period) {
			return true;
		}
		return false;
	}

	public void putCount(String key, int count) {
		Editor editor = mPreferences.edit();
		editor.putInt(key + SUFFIX_COUNT, count);
		editor.commit();
	}

	public boolean checkCount(String key) {
		int count = mPreferences.getInt(key + SUFFIX_COUNT, 0);
		return count > 0;
	}

	public void subCount(String key) {
		int count = mPreferences.getInt(key + SUFFIX_COUNT, 0);
		putCount(key, count - 1);
	}
}
