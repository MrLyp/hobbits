package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.BannerList;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.GoodsList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.task.QueryMainPageInfoTask;
import me.hobbits.leimao.freevip.ui.activity.CreditActivity;
import me.hobbits.leimao.freevip.ui.activity.ExchangeActivity;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.gandalf.task.HandlerMessageTask;
import cn.gandalf.task.HandlerMessageTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.widget.AsyncImageView;

public class MainFragment extends BaseFragment {

	private GoodsList mGoodsList;
	private BannerList mBannerList;
	private Balance mBalance;

	private FrameLayout flCredit;
	private ListView lvVip;
	private VipAdapter mGoodsAdapter;
	private TextView mAccount;
	private TextView mTotalBalance;

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
			Intent intent = new Intent(getActivity(), ExchangeActivity.class);
			intent.putExtra(ExchangeActivity.EXTRA_GOODS_ID, arg2);
			startActivity(intent);
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	protected void initViews() {
		super.initViews();
		flCredit = (FrameLayout) findViewById(R.id.fl_credit);
		flCredit.setOnClickListener(mOnClickListener);

		mGoodsAdapter = new VipAdapter(getActivity());
		lvVip = (ListView) findViewById(R.id.lv_vip);
		lvVip.setAdapter(mGoodsAdapter);
		lvVip.setOnItemClickListener(mOnItemClickListener);
		mAccount = (TextView) findViewById(R.id.balance);
		mTotalBalance = (TextView) findViewById(R.id.total);
	}

	private void initData() {
		initContent();
		QueryMainPageInfoTask mTask = new QueryMainPageInfoTask(mContext);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(HandlerMessageTask task, Object t) {
				initContent();
			}

			@Override
			public void onFail(HandlerMessageTask task, Object t) {

			}
		});
		mTask.execute();
	}

	private void initContent() {
		mGoodsList = GlobalValue.getIns(mContext).getGoodsList();
		if (mGoodsList != null) {
			mGoodsAdapter.setData(mGoodsList);
			mGoodsAdapter.notifyDataSetChanged();
		}
		mBalance = GlobalValue.getIns(mContext).getBalance();
		if (mBalance != null) {
			mAccount.setText("我的点数" + mBalance.getBalance());
			mTotalBalance.setText("今日总计发放点数\n" + mBalance.getToday_gold());
		}
	}

	private class VipAdapter extends BaseAdapter {

		private Context mContext;
		private GoodsList mData;

		public VipAdapter(Context context) {
			mContext = context;
		}

		public void setData(GoodsList list) {
			mData = list;
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
			if (mData != null)
				return mData.get(position);
			else
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

			Object obj = getItem(position);
			if (obj != null) {
				Goods goods = (Goods) obj;
				viewHolder.ivLogo.setImageUrlAndLoad("" + goods.getImg());
				viewHolder.tvTitle.setText("" + goods.getName());
				viewHolder.tvLeft.setText("余" + goods.getQuantity() + "名额");
				viewHolder.tvPoint.setText("需" + goods.getPrice() + "点数");
				viewHolder.ivTag
						.setVisibility(goods.getFlag() == 0 ? View.INVISIBLE
								: View.VISIBLE);
			}

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
