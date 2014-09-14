package me.hobbits.leimao.freevip.util;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import cn.gandalf.util.BitmapUtils;

import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Util;

public class WeiboManager {
	private static final String APP_KEY = "1633550764"; // test
	private static final int THUMB_SIZE = 300;
	private static WeiboManager mIns;
	private IWeiboAPI mApi;

	public static WeiboManager getIns(Context context) {
		if (mIns == null)
			mIns = new WeiboManager(context);
		return mIns;
	}

	public WeiboManager(Context context) {
		mApi = WeiboSDK.createWeiboAPI(context, APP_KEY);
		regWeibo();
	}

	public IWeiboAPI getApi() {
		return mApi;
	}

	public void sendMessage(Activity activity, ShareContent content) {
		regWeibo();
		WeiboMultiMessage msg = new WeiboMultiMessage();
		msg.textObject = wrapTextObj(content.mergeAllText());
		ImageObject imgObj = wrapImageObject(content.imagePath);
		msg.imageObject = imgObj;
		msg.mediaObject = wrapWebpageObj(content);

		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		req.transaction = buildTransaction("weibo_");
		req.multiMessage = msg;
		mApi.sendRequest(activity, req);
	}

	public void sendMessage2(Activity activity, ShareContent content) {
		regWeibo();
		WeiboMessage msg = new WeiboMessage();
		msg.mediaObject = wrapWebpageObj(content);
		SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
		req.transaction = buildTransaction("weibo_");
		req.message = msg;
		mApi.sendRequest(activity, req);
	}

	private void regWeibo() {
		mApi.registerApp();
	}

	public boolean isSupportSdk() {
		return mApi.isWeiboAppSupportAPI();
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private ImageObject wrapImageObject(String path) {
		Bitmap bmp = BitmapUtils.createImageThumbWithLim(path, THUMB_SIZE,
				THUMB_SIZE);
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
		mediaObject.identify = Util.generateId();
		mediaObject.title = "" + sc.title;
		mediaObject.description = "" + sc.content;
		mediaObject.defaultText = "webpage";

		Bitmap bmp = BitmapUtils.createImageThumbWithLim(sc.imagePath, 200, 200);
		if (bmp != null)
			mediaObject.setThumbImage(bmp);
		if (sc.url != null)
			mediaObject.actionUrl = sc.url;
		return mediaObject;
	}
}
