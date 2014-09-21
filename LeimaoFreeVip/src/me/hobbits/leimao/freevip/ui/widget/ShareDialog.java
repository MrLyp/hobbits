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

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	private TextView mWechat;
	private TextView mFriendCircle;
	private TextView mTencent;
	private TextView mWeibo;
	private ImageView mExit;
	private Context mContext;
	private QQManager mQQManager;
	private WeiboManager mWeiboManager;
	private WeixinManager mWeiXinManager;

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
		mQQManager = new QQManager(mContext);
		mWeiboManager = new WeiboManager(mContext);
		mWeiXinManager = new WeixinManager(mContext);
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
			onShare(ShareChannel.WECHAT);
			break;
		case R.id.tv_friend_circle:
			onShare(ShareChannel.FRIEND_CIRCLE);
			break;
		case R.id.tv_qq:
			onShare(ShareChannel.QQ);
			break;
		case R.id.tv_weibo:
			onShare(ShareChannel.WEIBO);
			break;
		case R.id.iv_close:
			dismiss();
			break;
		default:
			break;
		}
	}

	private void onShare(ShareChannel channel) {
		ShareContent sc;
		switch (channel) {
		case WECHAT:
			sc = new ShareContent();
			sc.title = "迅雷白金会员免费送啦！";
			sc.content = "海量高清视频免费看，种子资源随便下~你懂的！";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, true);
			sc.url = ShareUtils.URL_SHARE;
			mWeiXinManager.sendMessage(sc, false);
			break;
		case FRIEND_CIRCLE:
			sc = new ShareContent();
			sc.title = "迅雷白金会员免费送啦！用来干嘛？你懂的!";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, true);
			sc.url = ShareUtils.URL_SHARE;
			mWeiXinManager.sendMessage(sc, true);
			break;
		case QQ:
			sc = new ShareContent();
			sc.title = "迅雷白金会员免费送啦！";
			sc.content = "海量高清视频免费看，种子资源随便下~你懂的！";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, true);
			sc.url = ShareUtils.URL_SHARE;
			mQQManager.sendMessage(sc);
			break;
		case WEIBO:
			sc = new ShareContent();
			sc.title = "【免费高清视频】这个APP太给力了，可以免费用迅雷白金会员！同时还有种子资源哦！";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, false);
			sc.url = ShareUtils.URL_SHARE;
			Intent intent = new Intent(mContext,
					WeiboTransferActivity.class);
			intent.setAction(WeiboTransferActivity.ACTION_SEND_MSG);
			intent.putExtra(WeiboTransferActivity.EXTRA_DATA, sc);
			mContext.startActivity(intent);
			break;
		default:
			return;
		}
		dismiss();
	}

}
