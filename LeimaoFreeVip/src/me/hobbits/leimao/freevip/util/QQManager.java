/**
 * 
 */
package me.hobbits.leimao.freevip.util;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.gandalf.util.StringUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQManager {
	private static final String APP_ID = "1102291442";
	private static final String APP_KEY = "4PxTzHngEC3A6xx0";

	private static int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;

	private Context mContext;
	public Tencent mTencent;
	public QQShare mQQShare;

	public QQManager(Context context) {
		mContext = context;
		init();
	}

	private void init() {
		try {
			mTencent = Tencent.createInstance(APP_ID,
					mContext.getApplicationContext());
			if (mTencent == null)
				return;
			mQQShare = new QQShare(mContext, mTencent.getQQToken());
		} catch (Exception e) {
			Log.e("QQManager", "Init Failed", e);
		}
	}

	public void sendMessage(ShareContent sc) {
		final Bundle params = new Bundle();
		if (!StringUtils.isEmpty(sc.title))
			params.putString(QQShare.SHARE_TO_QQ_TITLE, sc.title);
		if (!StringUtils.isEmpty(sc.content))
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, sc.content);
		if (!StringUtils.isEmpty(sc.url))
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, sc.url);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, sc.imagePath);
		String appName = mContext.getResources().getString(R.string.app_name);
		if (StringUtils.isEmpty(appName))
			appName = "免费高清视频";
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);

		if (mQQShare == null) {
			init();
			Toast.makeText(mContext, "分享失败，请稍后再试!", Toast.LENGTH_SHORT).show();
			return;
		}

		mQQShare.shareToQQ((Activity)mContext, params, new IUiListener() {

			@Override
			public void onCancel() {
			}

			@Override
			public void onComplete(Object response) {
			}

			@Override
			public void onError(UiError e) {
			}
		});
	}
}
