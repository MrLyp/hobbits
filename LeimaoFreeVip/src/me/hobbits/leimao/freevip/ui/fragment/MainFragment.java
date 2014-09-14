package me.hobbits.leimao.freevip.ui.fragment;

import cn.gandalf.util.AsyncImageLoader;
import cn.gandalf.widget.AsyncImageView;
import me.hobbits.leimao.freevip.ui.activity.CreditActivity;
import me.hobbits.leimao.freevip.ui.activity.ExchangeActivity;
import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainFragment extends BaseFragment {

	private FrameLayout flCredit;

	private ListView lvVip;
	private BaseAdapter adapterVip;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == flCredit) {
				startActivity(new Intent(getActivity(), CreditActivity.class));
			}
		}
	};

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// intent need put extra
			startActivity(new Intent(getActivity(), ExchangeActivity.class));
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main;
	}

	@Override
	protected void initViews() {
		super.initViews();
		flCredit = (FrameLayout) findViewById(R.id.fl_credit);
		flCredit.setOnClickListener(mOnClickListener);

		adapterVip = new VipAdapter(getActivity());
		lvVip = (ListView) findViewById(R.id.lv_vip);
		lvVip.setAdapter(adapterVip);
		lvVip.setOnItemClickListener(mOnItemClickListener);
	}

	private class VipAdapter extends BaseAdapter {

		private Context mContext;

		public VipAdapter(Context context) {
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
						R.layout.item_vip, null);

				viewHolder.ivLogo = (AsyncImageView) convertView
						.findViewById(R.id.iv_logo);
				viewHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tvLeft = (TextView) convertView
						.findViewById(R.id.tv_left);
				viewHolder.tvPoint = (TextView) convertView
						.findViewById(R.id.tv_point);
				viewHolder.ivTag = (ImageView) convertView
						.findViewById(R.id.iv_tag);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.ivLogo.setImageUrlAndLoad("http://www.baidu.com/img/bdlogo.png");
			viewHolder.tvTitle.setText("优酷会员一个月");
			viewHolder.tvLeft.setText("余319名额");
			viewHolder.tvPoint.setText("需10点数");
			viewHolder.ivTag.setVisibility(View.VISIBLE);

			return convertView;
		}
	}

	private static class ViewHolder {
		AsyncImageView ivLogo;
		TextView tvTitle;
		TextView tvLeft;
		TextView tvPoint;
		ImageView ivTag;
	}
}
