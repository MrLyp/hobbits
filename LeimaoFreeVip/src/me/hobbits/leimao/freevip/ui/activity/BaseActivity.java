package me.hobbits.leimao.freevip.ui.activity;

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
}
