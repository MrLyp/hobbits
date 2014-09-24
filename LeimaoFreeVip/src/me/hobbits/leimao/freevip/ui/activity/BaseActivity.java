package me.hobbits.leimao.freevip.ui.activity;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initViews();
	}

	abstract protected int getLayoutId();

	protected void initViews() {

	}
	
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
