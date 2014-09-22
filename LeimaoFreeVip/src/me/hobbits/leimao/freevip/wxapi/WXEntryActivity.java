package me.hobbits.leimao.freevip.wxapi;

import me.hobbits.leimao.freevip.util.ShareUtils;
import me.hobbits.leimao.freevip.util.WeixinManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private final String TAG = "WXEntryActivity";
	private WeixinManager mWeixinManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWeixinManager = new WeixinManager(this);
		mWeixinManager.getApi().handleIntent(getIntent(), this);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mWeixinManager.getApi().handleIntent(intent, this);
		finish();
	}

	@Override
	public void onReq(BaseReq arg0) {
		// do nothing
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			ShareUtils.updateShareResult(this);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Log.d(TAG, resp.errCode + " " + resp.errStr + " "
					+ resp.transaction);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Log.d(TAG, resp.errCode + " " + resp.errStr + " "
					+ resp.transaction);
			break;
		default:
			break;
		}
	}

}
