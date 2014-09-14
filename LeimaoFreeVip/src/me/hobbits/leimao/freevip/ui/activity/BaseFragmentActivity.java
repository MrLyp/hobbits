package me.hobbits.leimao.freevip.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragmentActivity extends FragmentActivity {

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