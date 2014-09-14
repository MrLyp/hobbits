package me.hobbits.leimao.freevip.ui.widget;

import cn.gandalf.util.ScreenUtils;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.util.WeixinManager;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	private TextView mWechat;
	private TextView mFriendCircle;
	private TextView mTencent;
	private TextView mWeibo;
	private ImageView mExit;
	private Context mContext;

	public ShareDialog(Context context) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_share);
		setCanceledOnTouchOutside(true);
		initViews();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(context) * 0.8f);
		dialogWindow.setAttributes(lp);
		
	}

	private void initViews() {
		mWechat = (TextView) findViewById(R.id.tv_wechat);
		mFriendCircle = (TextView) findViewById(R.id.tv_friend_circle);
		mTencent = (TextView) findViewById(R.id.tv_qq);
		mWeibo = (TextView) findViewById(R.id.tv_weibo);
		mExit = (ImageView) findViewById(R.id.iv_close);
		mWechat.setOnClickListener(this);
		mFriendCircle.setOnClickListener(this);
		mTencent.setOnClickListener(this);
		mWeibo.setOnClickListener(this);
		mExit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_wechat:
			Log.d("lyp","share to wechat");
			break;
		case R.id.tv_friend_circle:
			Log.d("lyp","share to friend_circle");
			break;
		case R.id.tv_qq:
			Log.d("lyp","share to qq");
			break;
		case R.id.tv_weibo:
			Log.d("lyp","share to weibo");
			break;
		case R.id.iv_close:
			dismiss();
			break;
		default:
			break;
		}
	}

}
