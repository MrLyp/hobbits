package me.hobbits.leimao.freevip.ui.activity;

import cn.gandalf.util.StringUtils;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.ui.fragment.HelpFragment;
import me.hobbits.leimao.freevip.ui.widget.MWebView;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;

public class WebViewActivity extends BaseFragmentActivity implements
		OnClickListener {

	public static final String EXTRA_TITLE = "extra_title";
	public static final String EXTRA_URL = "extra_url";

	private TitlebarView mTitlebarView;
	private MWebView mWeb;
	private ProgressBar mProgress;
	private WebViewClient mClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mWeb.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mProgress.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mProgress.setVisibility(View.GONE);
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.activity_webview;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(this);
		String title = getIntent().getStringExtra(EXTRA_TITLE);
		if (!StringUtils.isEmpty(title))
			mTitlebarView.setTitleText(title);
		mWeb = (MWebView) findViewById(R.id.view_web);
		mWeb.setWebViewClient(mClient);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		String url = getIntent().getStringExtra(EXTRA_URL);
		mClient.shouldOverrideUrlLoading(mWeb, url);
	}

	@Override
	public void onClick(View v) {
		if (v == mTitlebarView.getLeftButton()) {
			finish();
		}
	}
}
