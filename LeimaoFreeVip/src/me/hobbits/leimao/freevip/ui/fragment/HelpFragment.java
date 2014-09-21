package me.hobbits.leimao.freevip.ui.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.ui.widget.MWebView;

public class HelpFragment extends BaseFragment {

	public static final String URL_HELP = "http://tcvideo.bitclock.cn/index.php/Web/help";

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
		return R.layout.fragment_help;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mWeb = (MWebView) findViewById(R.id.view_web);
		mWeb.setWebViewClient(mClient);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mClient.shouldOverrideUrlLoading(mWeb, URL_HELP);
	}
}
