package me.hobbits.leimao.freevip.util;

import java.io.ByteArrayOutputStream;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import cn.gandalf.util.BitmapUtils;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WeixinManager {
	private static final String APP_ID = "wx4f48dc6cc12b7bf3";
	private static final int THUMB_SIZE = 300;
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	private IWXAPI mApi;

	public WeixinManager(Context context) {
		mApi = WXAPIFactory.createWXAPI(context, APP_ID);
		registWeixin();
	}

	public IWXAPI getApi() {
		return mApi;
	}

	public boolean isSupportTimeline() {
		int ver = mApi.getWXAppSupportAPI();
		return (ver >= TIMELINE_SUPPORTED_VERSION);
	}

	public boolean isWXAppInstalled() {
		return mApi.isWXAppInstalled();
	}

	public boolean registWeixin() {
		return mApi.registerApp(APP_ID);
	}

	public void sendMessage(Context context, ShareContent content) {
		sendMessage(context, content, false);
	}

	public void sendMessage(Context context, ShareContent content,
			boolean timelineScene) {
		boolean success = registWeixin();
		WXMediaMessage msg = new WXMediaMessage();
		String url = content.url;
		boolean isImageShare = false;
		if (url == null || url.length() == 0)
			isImageShare = true;
		msg.mediaObject = !isImageShare ? getWXWebpageObject(context, content)
				: getWXImageObject(context, content);
		if (!isImageShare) {
			msg.title = "" + content.title;
			msg.description = "" + content.content;
		}
		Bitmap thumbBmp = BitmapUtils.createImageThumbWithLim(content.imagePath,
				THUMB_SIZE, THUMB_SIZE);
		if (thumbBmp != null) {
			msg.setThumbImage(thumbBmp);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("weixin_");
		req.message = msg;

		if (isSupportTimeline() && timelineScene)
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		else
			req.scene = SendMessageToWX.Req.WXSceneSession;
		mApi.sendReq(req);
		mApi.unregisterApp();
	}

	private WXWebpageObject getWXWebpageObject(Context context,
			ShareContent content) {
		WXWebpageObject webObj = new WXWebpageObject();
		webObj.webpageUrl = "http://www.weiche.me";
		if (content.url != null && content.url.length() > 0)
			webObj.webpageUrl = content.url;
		return webObj;
	}

	private WXImageObject getWXImageObject(Context context, ShareContent content) {
		WXImageObject obj = new WXImageObject();
		obj.setImagePath(content.imagePath);
		return obj;
	}

	private static byte[] bmpToByteArray(final Bitmap bmp,
			CompressFormat format, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(format, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private CompressFormat getFormat(String path) {
		if (path.toLowerCase().endsWith(".png"))
			return CompressFormat.JPEG;
		else if (path.toLowerCase().endsWith(".png"))
			return CompressFormat.PNG;
		return CompressFormat.JPEG;
	}

}
