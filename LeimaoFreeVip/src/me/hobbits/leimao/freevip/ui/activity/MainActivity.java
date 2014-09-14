package me.hobbits.leimao.freevip.ui.activity;

import cn.gandalf.json.ErrorResp;
import cn.gandalf.task.HandlerMessageTask;
import cn.gandalf.task.HandlerMessageTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.ui.fragment.AboutFragment;
import me.hobbits.leimao.freevip.ui.fragment.HelpFragment;
import me.hobbits.leimao.freevip.ui.fragment.MainFragment;
import me.hobbits.leimao.freevip.ui.fragment.RecordFragment;
import me.hobbits.leimao.freevip.ui.fragment.TaskFragment;
import me.hobbits.leimao.freevip.ui.widget.PopupMenu;
import me.hobbits.leimao.freevip.ui.widget.ShareDialog;
import me.hobbits.leimao.freevip.ui.widget.PopupMenu.OnPopupMenuClickListener;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseFragmentActivity {

	private TitlebarView mTitlebarView;

	private PopupMenu mPopupMenu;

	private Fragment mCurrentFragment;

	private Fragment mMainFragment;
	private Fragment mRecordFragment;
	private Fragment mTaskFragment;
	private Fragment mHelpFragment;
	private Fragment mAboutFragment;

	private Context mContext;

	private OnPopupMenuClickListener mOnPopupMenuClickListener = new OnPopupMenuClickListener() {

		@Override
		public void onPopupMenuClick(int index) {
			mTitlebarView.setDotVisibility(View.GONE);
			if (index == PopupMenu.INDEX_MAIN) {
				switchFragment(mMainFragment);
				mTitlebarView.setTitleText("");
				mTitlebarView.setDotVisibility(View.VISIBLE);
				mTitlebarView.setTitleImageResource(R.drawable.text_title);
				mTitlebarView.setRightImageResource(R.drawable.ic_message);
			} else if (index == PopupMenu.INDEX_RECORD) {
				switchFragment(mRecordFragment);
				mTitlebarView.setTitleTextResource(R.string.title_record);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_refresh);
			} else if (index == PopupMenu.INDEX_TASK) {
				switchFragment(mTaskFragment);
				mTitlebarView.setTitleTextResource(R.string.title_task);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_refresh);
			} else if (index == PopupMenu.INDEX_RECOMMEND) {
				ShareDialog dialog = new ShareDialog(mContext);
				dialog.show();
			} else if (index == PopupMenu.INDEX_HELP) {
				switchFragment(mHelpFragment);
				mTitlebarView.setTitleTextResource(R.string.title_help);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_message);
			} else if (index == PopupMenu.INDEX_ABOUT) {
				switchFragment(mAboutFragment);
				mTitlebarView.setTitleTextResource(R.string.title_about);
				mTitlebarView.setTitleImageDrawable(null);
				mTitlebarView.setRightImageResource(R.drawable.ic_message);
			}

		}
	};

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mTitlebarView.getLeftButton()) {
				mPopupMenu.showAsDropDown(v);
			} else if (v == mTitlebarView.getRightButton()) {
				if (mCurrentFragment == mRecordFragment
						|| mCurrentFragment == mTaskFragment) {
					Toast.makeText(MainActivity.this, "Refresh",
							Toast.LENGTH_SHORT).show();
				} else {
					startActivity(new Intent(MainActivity.this,
							MessageActivity.class));
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		HttpManager.init(mContext);
		mMainFragment = new MainFragment();
		mRecordFragment = new RecordFragment();
		mTaskFragment = new TaskFragment();
		mHelpFragment = new HelpFragment();
		mAboutFragment = new AboutFragment();

		switchFragment(mMainFragment);
		mTitlebarView.setTitleText("");
		mTitlebarView.setDotVisibility(View.VISIBLE);
		mTitlebarView.setTitleImageResource(R.drawable.text_title);
		mTitlebarView.setRightImageResource(R.drawable.ic_message);

		mPopupMenu = new PopupMenu(this);
		mPopupMenu.setIdText("18701285225");
		mPopupMenu.setOnPopupMenuClickListener(mOnPopupMenuClickListener);
		final HttpConnectTask mTask = new HttpConnectTask(mContext,
				HttpManager.getSignIn());
		mTask.setShowCodeMsg(false);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(HandlerMessageTask task, Object t) {
				SignInSuccess signIn = (SignInSuccess) mTask.getResult();
				Log.d("lyp", "iddddddddddddddd = " + signIn.getUser_id());
			}

			@Override
			public void onFail(HandlerMessageTask task, Object t) {
				ErrorResp error = mTask.getError();
				if (error != null) {
					Log.d("lyp", error.getError_message());
					Toast.makeText(mContext, error.getError_message(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		mTask.execute();
	};

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);
		mTitlebarView.setOnRightButtonClickListener(mOnClickListener);
	}

	@Override
	public void onBackPressed() {
		if (mPopupMenu != null && mPopupMenu.isShowing()) {
			mPopupMenu.dismiss();
			return;
		}
		super.onBackPressed();
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
}
