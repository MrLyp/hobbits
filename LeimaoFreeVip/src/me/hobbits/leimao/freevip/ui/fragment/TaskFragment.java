package me.hobbits.leimao.freevip.ui.fragment;

import java.util.List;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Income;
import me.hobbits.leimao.freevip.task.QueryIncomeTask;
import me.hobbits.leimao.freevip.ui.activity.MainActivity;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;

public class TaskFragment extends BaseFragment {

	private ListView lvTask;
	private TaskAdapter adapterTask;
	private View mRefreshButton;
	private Animation mAnimRefresh;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_task;
	}

	@Override
	protected void initViews() {
		super.initViews();
		adapterTask = new TaskAdapter(getActivity());
		lvTask = (ListView) findViewById(R.id.lv_task);
		lvTask.setAdapter(adapterTask);
		LayoutInflater inflater = mContext.getLayoutInflater();
		View emptyView = inflater.inflate(R.layout.list_item_message_empty,
				null);
		TextView text = (TextView) emptyView.findViewById(R.id.empty_text);
		text.setText("没有更多记录");
		((ViewGroup) lvTask.getParent()).addView(emptyView);
		lvTask.setEmptyView(emptyView);
		TitlebarView title = ((MainActivity) getActivity()).getTitleBar();
		if (title != null)
			mRefreshButton = title.getRightButton();
		mAnimRefresh = AnimationUtils.loadAnimation(mContext,
				R.anim.anticlockwise_rotation);
		refresh();
	}

	public void refresh() {
		mRefreshButton.startAnimation(mAnimRefresh);
		initContent();
		final QueryIncomeTask mTask = new QueryIncomeTask(mContext);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				mRefreshButton.clearAnimation();
				initContent();
			}

			@Override
			public void onFail(BaseTask task, Object t) {
				mRefreshButton.clearAnimation();
			}
		});
		mTask.execute();
	}

	private void initContent() {
		List<Income> list = GlobalValue.getIns(mContext).getIncomeList();
		adapterTask.setData(list);
		adapterTask.notifyDataSetChanged();
	}

	private class TaskAdapter extends BaseAdapter {

		private Context mContext;
		private List<Income> mData;

		public TaskAdapter(Context context) {
			mContext = context;
		}

		private void setData(List<Income> list) {
			mData = list;
		}

		@Override
		public int getCount() {
			if (mData == null)
				return 0;
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			if (mData == null || position > mData.size())
				return null;
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_task, null);

				viewHolder.tvTask = (TextView) convertView
						.findViewById(R.id.tv_task);
				viewHolder.tvLeft = (TextView) convertView
						.findViewById(R.id.tv_left);
				viewHolder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				viewHolder.tvPrice = (TextView) convertView
						.findViewById(R.id.tv_price);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Object obj = getItem(position);
			if (obj == null)
				return convertView;
			Income income = (Income) obj;
			viewHolder.tvTask.setText(income.getInfo());
			viewHolder.tvLeft.setText("余额 : " + income.getBalance());
			viewHolder.tvTime.setText(income.getTime());
			viewHolder.tvPrice.setText("+" + income.getAmount());

			return convertView;
		}

	}

	private static class ViewHolder {
		TextView tvTask;
		TextView tvLeft;
		TextView tvTime;
		TextView tvPrice;
	}
}
