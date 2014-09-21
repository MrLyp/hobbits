package me.hobbits.leimao.freevip.ui.activity;

import cn.gandalf.json.ErrorResp;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends BaseActivity implements Runnable {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		Handler mHandler = new Handler();
		mHandler.postDelayed(this, 2000);
		registerToServer();
	}

	private void registerToServer() {
		final HttpConnectTask mTask = new HttpConnectTask(mContext,
				HttpManager.getSignInParam());
		mTask.setShowCodeMsg(false);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				SignInSuccess signIn = (SignInSuccess) mTask.getResult();
				GlobalValue.getIns(mContext).updateUserInfo(signIn);
			}

			@Override
			public void onFail(BaseTask task, Object t) {
				ErrorResp error = mTask.getError();
				if (error != null) {
					Toast.makeText(mContext, error.getError_message(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		mTask.execute();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_splash;
	}

	@Override
	public void run() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

}
