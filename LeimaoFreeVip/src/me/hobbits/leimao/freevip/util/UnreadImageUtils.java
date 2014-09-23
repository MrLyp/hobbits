package me.hobbits.leimao.freevip.util;

import cn.gandalf.util.DefaultProperties;
import android.content.Context;
import android.view.View;

public class UnreadImageUtils {
	private static UnreadImageUtils instance;
	private Context mContext;

	public static UnreadImageUtils getInstance(Context context) {
		if (instance == null) {
			instance = new UnreadImageUtils(context);
		}
		return instance;
	}

	public UnreadImageUtils(Context context) {
		mContext = context.getApplicationContext();
	}

	public void setUnreadImageVisibility(View unreadImage, String prefs,
			boolean isVisible) {
		if (unreadImage == null)
			return;
		unreadImage.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		DefaultProperties.setBoolPref(mContext, prefs, !isVisible);
	}

	public void initUnreadImageVisibility(View unreadImage, String prefs) {
		boolean isVisible = !DefaultProperties.getBoolPref(mContext, prefs,
				true);
		if (unreadImage == null)
			return;
		unreadImage.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	}

}
