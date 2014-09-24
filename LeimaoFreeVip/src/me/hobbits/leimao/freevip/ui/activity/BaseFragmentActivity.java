package me.hobbits.leimao.freevip.ui.activity;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initViews();
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	abstract protected int getLayoutId();

	protected void initViews() {

	}
}