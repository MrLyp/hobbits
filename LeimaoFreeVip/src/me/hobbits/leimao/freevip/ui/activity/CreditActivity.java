package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.AppConnect;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.model.Task;
import me.hobbits.leimao.freevip.model.TaskList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.dm.android.DMOfferWall;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.widget.AsyncImageView;

import com.mfgqsipin.Bxhtod;

import e.r.t.ccafss;
import e.r.t.os.ccbjss;

public class CreditActivity extends BaseActivity {

	private TitlebarView mTitlebarView;

	private ListView lvCredit;
	private CreditAdapter adapterCredit;
	private TaskList mTaskList;
	private Context mContext;
	private String mUserId;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mTitlebarView.getLeftButton()) {
				finish();
			}
		}
	};

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (mTaskList == null || position > mTaskList.size())
				return;
			Task task = mTaskList.get(position);
			onTaskClick(task);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initOfferWalls();
		initData();
	}

	private void initOfferWalls() {
		SignInSuccess userInfo = GlobalValue.getIns(this).getUserInfo();
		mUserId = userInfo.getUser_id();
		DMOfferWall.init(this, "96ZJ2vzgzeBz/wTBCV");
		DMOfferWall.getInstance(this).setUserId(mUserId);
		AppConnect.getInstance(this);
		Bxhtod.initGoogleContext(this, "2da34d33b2329637105bf382d99fee75");
		Bxhtod.setCurrentUserID(this, mUserId);
		Bxhtod.setCustomActivity("com.mfgqsipin.BxhtodNativeActivity");
		Bxhtod.setCustomService("com.mfgqsipin.BxhtodNativeService");
		ccafss.getInstance(this).init("9bb91a86a07d0de5", "e315708d2d846002",
				false);
		ccbjss.getInstance(this).mmendd();
		ccbjss.getInstance(this).mmfgdd(mUserId);
	}

	private void onTaskClick(Task task) {
		if (task == null)
			return;
		if (task.getName().equals("domob")) {
			DMOfferWall.getInstance(this).showOfferWall(this);
		} else if (task.getName().equals("waps")) {
			AppConnect.getInstance(this).showOffers(this, mUserId);
		} else if (task.getName().equals("dianle")) {
			Bxhtod.showOffers(this);
		} else if (task.getName().equals("youmi")) {
			ccbjss.getInstance(this).mmfqdd();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(this).close();
		ccbjss.getInstance(this).mmemdd();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_credit;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);

		adapterCredit = new CreditAdapter(this);
		lvCredit = (ListView) findViewById(R.id.lv_credit);
		lvCredit.setAdapter(adapterCredit);
		lvCredit.setOnItemClickListener(mOnItemClickListener);
	}

	private void initData() {
		initContent();
		final HttpConnectTask mTask = new HttpConnectTask(this,
				HttpManager.getTaskParam());
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				TaskList list = (TaskList) mTask.getResult();
				GlobalValue.getIns(mContext).updateTasks(list);
				initContent();
			}

			@Override
			public void onFail(BaseTask task, Object t) {

			}
		});
		mTask.execute();
	}

	private void initContent() {
		mTaskList = GlobalValue.getIns(mContext).getTaskList();
		adapterCredit.setData(mTaskList);
		adapterCredit.notifyDataSetChanged();
	}

	private class CreditAdapter extends BaseAdapter {

		private Context mContext;
		private TaskList mData;

		public CreditAdapter(Context context) {
			mContext = context;
		}

		public void setData(TaskList l) {
			mData = l;
		}

		@Override
		public int getCount() {
			if (mData != null)
				return mData.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mData != null && position < mData.size())
				return mData.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_credit, null);

				viewHolder.ivIcon = (AsyncImageView) convertView
						.findViewById(R.id.iv_icon);
				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tvSummary = (TextView) convertView
						.findViewById(R.id.tv_summary);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Object obj = getItem(position);
			if (obj == null)
				return convertView;
			Task task = (Task) obj;
			viewHolder.ivIcon.setImageUrlAndLoad(task.getImg());
			viewHolder.tvTitle.setText(task.getShow_name());
			viewHolder.tvSummary.setText(task.getDetail());

			return convertView;
		}

	}

	private static class ViewHolder {
		AsyncImageView ivIcon;
		TextView tvTitle;
		TextView tvSummary;
	}
}
