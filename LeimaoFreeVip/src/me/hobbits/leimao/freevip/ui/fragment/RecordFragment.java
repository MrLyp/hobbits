package me.hobbits.leimao.freevip.ui.fragment;

import java.util.List;

import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Exchange;
import me.hobbits.leimao.freevip.task.QueryExchangeRecordTask;
import me.hobbits.leimao.freevip.ui.activity.MainActivity;
import me.hobbits.leimao.freevip.ui.activity.RecordDetailActivity;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecordFragment extends BaseFragment {

	private ListView lvRecord;
	private RecordAdapter adapterRecord;
	private List<Exchange> mExchangeList;
	private View mRefreshButton;
	private Animation mAnimRefresh;

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (mExchangeList == null || position > mExchangeList.size())
				return;
			Exchange e = mExchangeList.get(position);
			if (e != null && e.getStatus().equals("兑换成功")) {
				Intent intent = new Intent(getActivity(),
						RecordDetailActivity.class);
				intent.putExtra(RecordDetailActivity.EXTRA_EXCHANGE, e);
				startActivity(intent);
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_record;
	}

	@Override
	protected void initViews() {
		super.initViews();
		adapterRecord = new RecordAdapter(getActivity());
		lvRecord = (ListView) findViewById(R.id.lv_record);
		lvRecord.setAdapter(adapterRecord);
		lvRecord.setOnItemClickListener(mOnItemClickListener);
		LayoutInflater inflater = mContext.getLayoutInflater();
		View emptyView = inflater.inflate(R.layout.list_item_message_empty,
				null);
		TextView text = (TextView) emptyView.findViewById(R.id.empty_text);
		text.setText("没有更多记录");
		((ViewGroup) lvRecord.getParent()).addView(emptyView);
		lvRecord.setEmptyView(emptyView);
		TitlebarView title = ((MainActivity) getActivity()).getTitleBar();
		if (title != null)
			mRefreshButton = title.getRightButton();
		mAnimRefresh = AnimationUtils.loadAnimation(mContext,
				R.anim.anticlockwise_rotation);
	}

	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh() {
		mRefreshButton.startAnimation(mAnimRefresh);
		mExchangeList = GlobalValue.getIns(mContext).getExchangeList();
		initListContent();
		QueryExchangeRecordTask mTask = new QueryExchangeRecordTask(mContext);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				mRefreshButton.clearAnimation();
				mExchangeList = GlobalValue.getIns(mContext).getExchangeList();
				initListContent();
			}

			@Override
			public void onFail(BaseTask task, Object t) {
				mRefreshButton.clearAnimation();
			}
		});
		mTask.execute();
	}

	private void initListContent() {
		adapterRecord.setData(mExchangeList);
		adapterRecord.notifyDataSetChanged();
	}

	private class RecordAdapter extends BaseAdapter {

		private Context mContext;
		private List<Exchange> mData;

		public RecordAdapter(Context context) {
			mContext = context;
		}

		public void setData(List<Exchange> list) {
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
						R.layout.list_item_record, null);

				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tvCost = (TextView) convertView
						.findViewById(R.id.tv_cost);
				viewHolder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				viewHolder.tvStatus = (TextView) convertView
						.findViewById(R.id.tv_status);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Object obj = getItem(position);
			if (obj == null)
				return convertView;
			Exchange exchange = (Exchange) obj;
			viewHolder.tvTitle.setText(exchange.getName());
			viewHolder.tvCost.setText("花费：" + exchange.getPrice() + "点数");
			viewHolder.tvTime.setText(exchange.getTime());
			viewHolder.tvStatus.setText(exchange.getStatus());
			return convertView;
		}
	}

	private static class ViewHolder {
		TextView tvTitle;
		TextView tvCost;
		TextView tvTime;
		TextView tvStatus;
	}
}
