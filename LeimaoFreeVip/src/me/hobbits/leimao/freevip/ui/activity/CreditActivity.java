package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CreditActivity extends BaseActivity {

	private TitlebarView mTitlebarView;

	private ListView lvCredit;
	private BaseAdapter adapterCredit;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mTitlebarView.getLeftButton()) {
				finish();
			}
		}
	};

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
	}

	private class CreditAdapter extends BaseAdapter {

		private Context mContext;

		public CreditAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object getItem(int position) {
			return null;
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
						R.layout.item_credit, null);

				viewHolder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tvSummary = (TextView) convertView
						.findViewById(R.id.tv_summary);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.ivIcon.setVisibility(View.VISIBLE);
			viewHolder.tvTitle.setText("任务多");
			viewHolder.tvSummary.setText("新增任务数量较多");

			return convertView;
		}

	}

	private static class ViewHolder {
		ImageView ivIcon;
		TextView tvTitle;
		TextView tvSummary;
	}
}
