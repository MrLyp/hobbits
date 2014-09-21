package me.hobbits.leimao.freevip.util;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import android.content.Context;
import android.graphics.Bitmap;
import cn.gandalf.util.BitmapUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

public class WeiboManager {
	private static final String TAG = "WeiboManager";
	public static final String APP_KEY = "1680301707";
	private static final int THUMB_SIZE = 500;
	public IWeiboShareAPI mApi;
	private Context mContext;

	public WeiboManager(Context context) {
		mApi = WeiboShareSDK.createWeiboAPI(context, APP_KEY);
		mContext = context;
	}

	public void sendMessage(ShareContent content) {
		if (mApi == null || mContext == null)
			return;
		regWeibo();
		WeiboMultiMessage msg = new WeiboMultiMessage();
		msg.textObject = wrapTextObj(content.mergeAllText());
		ImageObject imgObj = wrapImageObject(content.imagePath);
		msg.imageObject = imgObj;

		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		req.transaction = buildTransaction("weibo_");
		req.multiMessage = msg;
		mApi.sendRequest(req);
	}

	public void sendMessage2(ShareContent content) {
		if (mApi == null || mContext == null)
			return;
		regWeibo();
		WeiboMessage msg = new WeiboMessage();
		msg.mediaObject = wrapWebpageObj(content);
		SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
		req.transaction = buildTransaction("weibo_");
		req.message = msg;
		mApi.sendRequest(req);
	}

	private void regWeibo() {
		if (mApi == null)
			return;
		mApi.registerApp();
	}

	public boolean isSupportSdk() {
		if (mApi == null)
			return false;
		return mApi.isWeiboAppSupportAPI();
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private ImageObject wrapImageObject(String path) {
		Bitmap bmp = BitmapUtils.getResizedBitmapFromFile(mContext, path);
		if (bmp == null)
			return null;
		ImageObject imageObject = new ImageObject();
		imageObject.setImageObject(bmp);
		return imageObject;
	}

	private TextObject wrapTextObj(String str) {
		TextObject textObject = new TextObject();
		textObject.text = str;
		return textObject;
	}

	private WebpageObject wrapWebpageObj(ShareContent sc) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = "" + sc.title;
		mediaObject.description = "" + sc.content;
		mediaObject.defaultText = "";

		Bitmap bmp = BitmapUtils.createImageThumbWithLim(sc.imagePath,
				THUMB_SIZE, THUMB_SIZE);
		if (bmp != null)
			mediaObject.setThumbImage(bmp);
		if (sc.url != null)
			mediaObject.actionUrl = sc.url;
		return mediaObject;
	}
}
