package me.hobbits.leimao.freevip.util;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import android.content.Context;
import android.graphics.Bitmap;
import cn.gandalf.util.BitmapUtils;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WeixinManager {
	private static final String APP_ID = "wx4f48dc6cc12b7bf3";
	private static final int THUMB_SIZE = 300;
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private static WeixinManager mIns;
	private IWXAPI mApi;

	public static WeixinManager getIns(Context context) {
		if (mIns == null)
			mIns = new WeixinManager(context);
		return mIns;
	}

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

	public boolean registWeixin() {
		return mApi.registerApp(APP_ID);
	}

	public void sendMessage(ShareContent content,
			boolean timelineScene) {
		registWeixin();
		WXWebpageObject webObj = new WXWebpageObject();
		webObj.webpageUrl = "";
		if (content.url != null && content.url.length() > 0)
			webObj.webpageUrl = content.url;

		WXMediaMessage msg = new WXMediaMessage(webObj);
		msg.title = "" + content.title;
		msg.description = "" + content.content;
		Bitmap thumbBmp = BitmapUtils.createImageThumbWithLim(
				content.imagePath, THUMB_SIZE, THUMB_SIZE);
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
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}
