package me.hobbits.leimao.freevip.wxapi;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import me.hobbits.leimao.freevip.util.WeiboManager;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

public class WeiboTransferActivity extends Activity implements
		IWeiboHandler.Response {
	private static final String TAG = "WeiboTransferActivity";

	public static final String ACTION_SEND_MSG = "action.send.weibo.msg";
	public static final String ACTION_MSG_CBK = "com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY";
	public static final String EXTRA_DATA = "extra.weibo.data";

	private ShareContent mContent;
	private WeiboManager mWeibo;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		mWeibo = new WeiboManager(this);

		if (ACTION_SEND_MSG.equals(intent.getAction())) {

			Object objData = intent.getSerializableExtra(EXTRA_DATA);
			if (!(objData instanceof ShareContent)) {
				finish();
				return;
			}
			mContent = (ShareContent) objData;
			mWeibo.sendMessage(mContent);
		} else if (ACTION_MSG_CBK.equals(intent.getAction())) {
			// mWeibo.getApi().responseListener(intent, this);
			mWeibo.mApi.handleWeiboResponse(intent, this);
		}
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeibo.mApi.handleWeiboResponse(intent, this);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Log.d(TAG,baseResp.errMsg + " "+ baseResp.toString());
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Log.d(TAG,baseResp.errMsg + " "+ baseResp.toString());
			break;
		}
	}
}
