package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.BasicConfig;
import me.hobbits.leimao.freevip.model.BasicConfig.Version;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.task.QueryMessageTask;
import me.hobbits.leimao.freevip.ui.fragment.AboutFragment;
import me.hobbits.leimao.freevip.ui.fragment.HelpFragment;
import me.hobbits.leimao.freevip.ui.fragment.MainFragment;
import me.hobbits.leimao.freevip.ui.fragment.RecordFragment;
import me.hobbits.leimao.freevip.ui.fragment.TaskFragment;
import me.hobbits.leimao.freevip.ui.widget.AlertDialog;
import me.hobbits.leimao.freevip.ui.widget.PopupMenu;
import me.hobbits.leimao.freevip.ui.widget.PopupMenu.OnPopupMenuClickListener;
import me.hobbits.leimao.freevip.ui.widget.ShareDialog;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import me.hobbits.leimao.freevip.util.QQManager;
import me.hobbits.leimao.freevip.util.ShareUtils;
import me.hobbits.leimao.freevip.util.ShareUtils.ShareChannel;
import me.hobbits.leimao.freevip.util.ShareUtils.ShareContent;
import me.hobbits.leimao.freevip.util.WeixinManager;
import me.hobbits.leimao.freevip.wxapi.WeiboTransferActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.util.DefaultProperties;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {

	public static final String EXTRA_FRAGMENT_INDEX = "extra_fragment_index";

	private TitlebarView mTitlebarView;

	private PopupMenu mPopupMenu;

	private Fragment mCurrentFragment;

	private Fragment mMainFragment;
	private Fragment mRecordFragment;
	private Fragment mTaskFragment;
	private Fragment mHelpFragment;
	private Fragment mAboutFragment;

	private Context mContext;
	private QQManager mQQManager;
	private WeixinManager mWeiXinManager;
	private ShareDialog mShareDialog;

	private OnPopupMenuClickListener mOnPopupMenuClickListener = new OnPopupMenuClickListener() {

		@Override
		public void onPopupMenuClick(int index) {
			if (index == PopupMenu.INDEX_MAIN) {
				switchFragment(mMainFragment);
				mTitlebarView.setTitleText("");
				mTitlebarView.setTitleImageResource(R.drawable.img_title);
				mTitlebarView.setRightImageResource(R.drawable.ic_message);
				mTitlebarView.getRightButton().setVisibility(View.VISIBLE);
				mTitlebarView.initDotVisibility();
			} else if (index == PopupMenu.INDEX_RECORD) {
				switchFragment(mRecordFragment);
				mTitlebarView.setTitleTextResource(R.string.title_record);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_refresh);
				mTitlebarView.getRightButton().setVisibility(View.VISIBLE);
				mTitlebarView.setDotVisibility(View.INVISIBLE);
			} else if (index == PopupMenu.INDEX_TASK) {
				switchFragment(mTaskFragment);
				mTitlebarView.setTitleTextResource(R.string.title_task);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_refresh);
				mTitlebarView.getRightButton().setVisibility(View.VISIBLE);
				mTitlebarView.setDotVisibility(View.INVISIBLE);
			} else if (index == PopupMenu.INDEX_RECOMMEND) {
				mShareDialog.show();
			} else if (index == PopupMenu.INDEX_HELP) {
				switchFragment(mHelpFragment);
				mTitlebarView.setTitleTextResource(R.string.title_help);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_message);
				mTitlebarView.initDotVisibility();
			} else if (index == PopupMenu.INDEX_ABOUT) {
				switchFragment(mAboutFragment);
				mTitlebarView.setTitleTextResource(R.string.title_about);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.getRightButton().setVisibility(View.INVISIBLE);
				mTitlebarView.setDotVisibility(View.INVISIBLE);
			}

		}
	};

	@Override
	public void onClick(View v) {
		if (v == mTitlebarView.getLeftButton()) {
			mPopupMenu.showAsDropDown(v, 10, 0);
		} else if (v == mTitlebarView.getRightButton()) {
			if (mCurrentFragment == mRecordFragment) {
				((RecordFragment) mCurrentFragment).refresh();
			} else if (mCurrentFragment == mTaskFragment) {
				((TaskFragment) mCurrentFragment).refresh();
			} else {
				DefaultProperties.setBoolPref(mContext,
						TitlebarView.KEY_NEW_MESSAGE_CLICKED, true);
				mTitlebarView.setDotVisibility(View.INVISIBLE);
				startActivity(new Intent(MainActivity.this,
						MessageActivity.class));
			}
		}
	}

	private OnClickListener mOnShareDialogClickListener = new OnClickListener() {

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
				mShareDialog.dismiss();
				break;
			default:
				break;
			}

		}
	};

	private void onShare(ShareChannel channel) {
		ShareContent sc;
		GlobalValue.getIns(mContext).setShareChannel(channel);
		switch (channel) {
		case WECHAT:
			sc = new ShareContent();
			sc.title = "迅雷白金会员免费送啦！";
			sc.content = "海量高清视频免费看，种子资源随便下~你懂的！";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, true);
			sc.url = ShareUtils.URL_SHARE;
			mWeiXinManager.sendMessage(mContext, sc, false);
			break;
		case FRIEND_CIRCLE:
			sc = new ShareContent();
			sc.title = "迅雷白金会员免费送啦！";
			sc.content = "用来干嘛？你懂的！小伙伴们速速领取吧！";
			sc.imagePath = ShareUtils.getShareImagePath(mContext, true);
			sc.url = ShareUtils.URL_SHARE;
			mWeiXinManager.sendMessage(mContext, sc, true);
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
			Intent intent = new Intent(mContext, WeiboTransferActivity.class);
			intent.setAction(WeiboTransferActivity.ACTION_SEND_MSG);
			intent.putExtra(WeiboTransferActivity.EXTRA_DATA, sc);
			mContext.startActivity(intent);
			break;
		default:
			return;
		}
		mShareDialog.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mMainFragment = new MainFragment();
		mRecordFragment = new RecordFragment();
		mTaskFragment = new TaskFragment();
		mHelpFragment = new HelpFragment();
		mAboutFragment = new AboutFragment();
		mQQManager = new QQManager(this);
		mWeiXinManager = new WeixinManager(this);
		initContent();
		initMessageTask();
		initUpdateTask();
	};
	
	protected void onResume() {
		super.onResume();
		mTitlebarView.initDotVisibility();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(this);
		mTitlebarView.setOnRightButtonClickListener(this);
		mTitlebarView.setTitleText("");
		mTitlebarView.setTitleImageResource(R.drawable.img_title);
		mTitlebarView.setRightImageResource(R.drawable.ic_message);
		mTitlebarView.initLeftDotVisibility();
		mPopupMenu = new PopupMenu(this);
		SignInSuccess info = GlobalValue.getIns(mContext).getUserInfo();
		String id = "";
		if (info != null)
			id = "ID:" + info.getUser_id();
		mPopupMenu.setIdText(id);
		mPopupMenu.setOnPopupMenuClickListener(mOnPopupMenuClickListener);
		mShareDialog = new ShareDialog(this);
		mShareDialog.setOnClickListener(mOnShareDialogClickListener);
	}

	private void initContent() {
		int idx = getIntent().getIntExtra(EXTRA_FRAGMENT_INDEX, -1);
		Fragment[] fragments = new Fragment[] { mMainFragment, mRecordFragment,
				mTaskFragment, mHelpFragment, mAboutFragment };
		if (idx < 0 || idx > 4)
			mCurrentFragment = mMainFragment;
		else
			mCurrentFragment = fragments[idx];
		switchFragment(mCurrentFragment);
	}

	private void initMessageTask() {
		final QueryMessageTask mTask = new QueryMessageTask(mContext);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				if (mTask.getNewCount() > 0) {
					DefaultProperties.setBoolPref(mContext,
							TitlebarView.KEY_NEW_MESSAGE_CLICKED, false);
					mTitlebarView.setDotVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFail(BaseTask task, Object t) {

			}
		});
		mTask.execute();
	}

	private void initUpdateTask() {
		final HttpConnectTask mTask = new HttpConnectTask(mContext,
				HttpManager.getBasicConfigParam());
		mTask.setShowCodeMsg(false);
		mTask.setShowProgessDialog(false);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				BasicConfig config = (BasicConfig) mTask.getResult();
				GlobalValue.getIns(mContext).updateBasicConfig(config);
				showUpdateDialog();
			}

			@Override
			public void onFail(BaseTask task, Object t) {
			}
		});
		mTask.execute();
	}

	private void showUpdateDialog() {
		BasicConfig config = GlobalValue.getIns(mContext).getBasicConfig();
		if (config == null)
			return;
		final Version version = config.getVersion();
		if (version.getNeed_update() == 0)
			return;
		AlertDialog dialog = new AlertDialog(mContext);
		dialog.setTitle("更新提示").setContent(version.getRelease_note())
				.setContentGravity(Gravity.LEFT)
				.setPositiveButton("点击下载", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(version.getDownload_url())));
					}
				});
		dialog.show();
	}

	private long mLastExitTime;

	@Override
	public void onBackPressed() {
		long now = System.currentTimeMillis();
		if (now - mLastExitTime < 2000)
			finish();
		else {
			mLastExitTime = now;
			Toast.makeText(mContext, "再次点击退出程序", Toast.LENGTH_SHORT).show();
		}
	}

	private void switchFragment(Fragment fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		if (mCurrentFragment != null) {
			transaction.detach(mCurrentFragment);
			transaction.replace(R.id.content, fragment);
			transaction.attach(fragment);
		} else {
			transaction.replace(R.id.content, fragment);
		}
		transaction.commit();
		mCurrentFragment = fragment;
	}

	public TitlebarView getTitleBar() {
		return mTitlebarView;
	}
}
