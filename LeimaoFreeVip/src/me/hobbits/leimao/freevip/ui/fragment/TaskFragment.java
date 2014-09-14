package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TaskFragment extends BaseFragment {

	private ListView lvTask;
	private BaseAdapter adapterTask;

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO task detail
		}
	};

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
		lvTask.setOnItemClickListener(mOnItemClickListener);
	}

	private class TaskAdapter extends BaseAdapter {

		private Context mContext;

		public TaskAdapter(Context context) {
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
						R.layout.item_task, null);

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

			viewHolder.tvTask.setText("下载《大航海时代》");
			viewHolder.tvLeft.setText("余额：19.02");
			viewHolder.tvTime.setText("2014-09-04 19:32:44");
			viewHolder.tvPrice.setText("+2.30");

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
