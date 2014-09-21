package me.hobbits.leimao.freevip.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;

public class MWebView extends WebView {
	public MWebView(Context context) {
		this(context, null);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public MWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setInitialScale(1);
		getSettings().setJavaScriptEnabled(true);
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setUseWideViewPort(true);
		getSettings().setBuiltInZoomControls(true);
		getSettings().setSupportZoom(true);
		getSettings().setUseWideViewPort(true);
		getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setWebChromeClient(new WebChromeClient());
		setDownloadListener(new MyDownLoadListener());
	}

	private class MyDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			getContext().startActivity(intent);
		}
	}

	public void invokeJs(String method, String... param) {
		StringBuffer paramSb = new StringBuffer();
		if (param != null) {
			for (String s : param) {
				paramSb.append("'" + s + "',");
			}
		}
		String paramStr = paramSb.toString();
		if (paramSb.length() > 0) {
			paramStr = paramStr.substring(0, paramStr.length() - 1);
		}
		loadUrl("javascript:" + method + "(" + paramStr + ")");
	}
}
