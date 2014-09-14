package me.hobbits.leimao.freevip.wxapi;

import me.hobbits.leimao.freevip.util.WeixinManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WeixinManager.getIns(this).getApi().handleIntent(getIntent(), this);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WeixinManager.getIns(this).getApi().handleIntent(intent, this);
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
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}
	}

}
