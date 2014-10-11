package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.AppConnect;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.model.Task;
import me.hobbits.leimao.freevip.model.TaskList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.hobbits.Qjtfgh;

import e.r.t.ccafss;
import e.r.t.os.ccbjss;

public class OfferWallFragment extends BaseFragment{

	private ListView lvCredit;
	private CreditAdapter adapterCredit;
	private TaskList mTaskList;
	private Activity mContext;
	private String mUserId;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		initOfferWalls();
	}

	private void initOfferWalls() {
		SignInSuccess userInfo = GlobalValue.getIns(mContext).getUserInfo();
		if (userInfo != null)
			mUserId = userInfo.getUser_id();
		DMOfferWall.init(mContext, "96ZJ2vzgzeBz/wTBCV");
		DMOfferWall.getInstance(mContext).setUserId(mUserId);
		AppConnect.getInstance(mContext);
		Qjtfgh.initGoogleContext(mContext, "2da34d33b2329637105bf382d99fee75");
		Qjtfgh.setCurrentUserID(mContext, mUserId);
		Qjtfgh.setCustomActivity("com.hobbits.QjtfghNativeActivity");
		Qjtfgh.setCustomService("com.hobbits.QjtfghNativeService");
		ccafss.getInstance(mContext).init("9bb91a86a07d0de5", "e315708d2d846002",
				false);
		ccbjss.getInstance(mContext).mmendd();
		ccbjss.getInstance(mContext).mmfgdd(mUserId);
	}

	private void onTaskClick(Task task) {
		if (task == null)
			return;
		if (task.getName().equals("domob")) {
			DMOfferWall.getInstance(mContext).showOfferWall(mContext);
		} else if (task.getName().equals("waps")) {
			AppConnect.getInstance(mContext).showOffers(mContext, mUserId);
		} else if (task.getName().equals("dianle")) {
			Qjtfgh.showOffers(mContext);
		} else if (task.getName().equals("youmi")) {
			ccbjss.getInstance(mContext).mmfqdd();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(mContext).close();
		ccbjss.getInstance(mContext).mmemdd();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_offerwall;
	}

	@Override
	protected void initViews() {
		super.initViews();
		adapterCredit = new CreditAdapter(mContext);
		lvCredit = (ListView) findViewById(R.id.lv_credit);
		lvCredit.setAdapter(adapterCredit);
		lvCredit.setOnItemClickListener(mOnItemClickListener);
		initData();
	}

	private void initData() {
		initContent();
		final HttpConnectTask mTask = new HttpConnectTask(mContext,
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
