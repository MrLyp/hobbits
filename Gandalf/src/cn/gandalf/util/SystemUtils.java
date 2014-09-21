package cn.gandalf.util;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SystemUtils {
	public static String getVersionName(Context ctx) {
		try {
			ComponentName comp = new ComponentName(ctx, "");
			PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			return pinfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return null;
	}

	public static int getVersionCode(Context ctx) {
		try {
			ComponentName comp = new ComponentName(ctx, "");
			PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return 0;
	}

	/** get meta data in manifest.xml */
	public static String getMetaData(Context ctx, String name) {
		String data = null;
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
					ctx.getPackageName(), PackageManager.GET_META_DATA);
			data = ai.metaData.getString(name);
		} catch (Exception e) {
		}
		return data;

	}

	public static String getChannel(Context ctx) {
		return getMetaData(ctx, "UMENG_CHANNEL");
	}

	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	public static String getPhoneNo(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	public static String getPackageName(Context context) {
		try {
			ComponentName comp = new ComponentName(context, "");
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			return pinfo.packageName;
		} catch (NameNotFoundException e) {
		}
		return null;
	}

	public static String getMacAddress(Context context) {
		String macAddress = "";
		WifiManager wifimanager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifimanager.getConnectionInfo();
		if (wifiinfo != null && wifiinfo.getMacAddress() != null
				&& wifiinfo.getMacAddress().length() > 0) {
			macAddress = wifiinfo.getMacAddress();
		}
		return macAddress;
	}

	public static String getSystemVersion() {
		try {
			String systemVersion = android.os.Build.VERSION.RELEASE;
			return systemVersion;
		} catch (Exception e) {
			return null;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void copyText(Context context, String content) {
		if (Build.VERSION.SDK_INT >= 11) {
			android.content.ClipboardManager cmb = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(content.trim());
		} else {
			android.text.ClipboardManager cmb = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(content.trim());
		}
	}
}
