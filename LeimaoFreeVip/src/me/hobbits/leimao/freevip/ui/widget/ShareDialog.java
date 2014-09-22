package me.hobbits.leimao.freevip.ui.widget;

import cn.gandalf.util.ScreenUtils;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.util.ShareUtils.ShareChannel;
import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import me.hobbits.leimao.freevip.util.QQManager;
import me.hobbits.leimao.freevip.util.ShareUtils;
import me.hobbits.leimao.freevip.util.WeiboManager;
import me.hobbits.leimao.freevip.util.WeixinManager;
import me.hobbits.leimao.freevip.wxapi.WeiboTransferActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareDialog extends Dialog {

	private TextView mWechat;
	private TextView mFriendCircle;
	private TextView mTencent;
	private TextView mWeibo;
	private ImageView mExit;
	private Context mContext;
	private android.view.View.OnClickListener mOnClickListener;

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
	}

	public void setOnClickListener(android.view.View.OnClickListener l) {
		mOnClickListener = l;
		mWechat.setOnClickListener(mOnClickListener);
		mFriendCircle.setOnClickListener(mOnClickListener);
		mTencent.setOnClickListener(mOnClickListener);
		mWeibo.setOnClickListener(mOnClickListener);
		mExit.setOnClickListener(mOnClickListener);
	}
}
