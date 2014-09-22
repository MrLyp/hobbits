package me.hobbits.leimao.freevip.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.ui.fragment.HelpFragment;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;

public class WebViewActivity extends BaseFragmentActivity implements
		OnClickListener {

	private TitlebarView mTitlebarView;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_webview;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fragment help = new HelpFragment();
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.stub, help).commit();
	}

	@Override
	public void onClick(View v) {
		if (v == mTitlebarView.getLeftButton()) {
			finish();
		}
	}
}
