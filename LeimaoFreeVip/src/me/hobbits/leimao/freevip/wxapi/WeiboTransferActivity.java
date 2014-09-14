package me.hobbits.leimao.freevip.wxapi;

import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import me.hobbits.leimao.freevip.util.WeiboManager;
import android.app.Activity;
import android.content.Intent;

import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboHandler;

public class WeiboTransferActivity extends Activity implements
		IWeiboHandler.Response {

	public static final String ACTION_SEND_MSG = "action.send.weibo.msg";
	public static final String ACTION_MSG_CBK = "com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY";
	public static final String EXTRA_DATA = "extra.weibo.data";

	private ShareContent mContent;
	private WeiboManager mWeibo;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mWeibo = WeiboManager.getIns(this);

		if (ACTION_SEND_MSG.equals(intent.getAction())) {

			Object objData = intent.getSerializableExtra(EXTRA_DATA);
			if (!(objData instanceof ShareContent)) {
				finish();
				return;
			}
			mContent = (ShareContent) objData;
			mWeibo.sendMessage(this, mContent);
		} else if (ACTION_MSG_CBK.equals(intent.getAction())) {
			mWeibo.getApi().responseListener(intent, this);
		}
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeibo = WeiboManager.getIns(this);
		mWeibo.getApi().responseListener(intent, this);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			break;
		}
	};
}
