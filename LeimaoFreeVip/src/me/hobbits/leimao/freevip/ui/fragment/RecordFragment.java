package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.ui.activity.RecordDetailActivity;
import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecordFragment extends BaseFragment {

	private ListView lvRecord;
	private BaseAdapter adapterRecord;

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			startActivity(new Intent(getActivity(), RecordDetailActivity.class));
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
	}

	private class RecordAdapter extends BaseAdapter {

		private Context mContext;

		public RecordAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return new Object();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_record, null);

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

			viewHolder.tvTitle.setText("优酷会员1个月");
			viewHolder.tvCost.setText("花费：10点数");
			viewHolder.tvTime.setText("2014-09-04 19:32:44");
			viewHolder.tvStatus.setText("处理中");

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
