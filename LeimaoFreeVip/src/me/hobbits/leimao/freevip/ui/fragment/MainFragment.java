package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.Banner;
import me.hobbits.leimao.freevip.model.BannerList;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.GoodsList;
import me.hobbits.leimao.freevip.task.QueryMainPageInfoTask;
import me.hobbits.leimao.freevip.ui.activity.CreditActivity;
import me.hobbits.leimao.freevip.ui.activity.ExchangeActivity;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
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
	private ViewPager mViewPager;

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
			Goods goods = GlobalValue.getIns(mContext).getGoodsItem(arg2);
			if (goods != null && goods.getQuantity() <= 0) {
				Toast.makeText(mContext, "商品卖光了，去看看其他的吧！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Intent intent = new Intent(getActivity(), ExchangeActivity.class);
			intent.putExtra(ExchangeActivity.EXTRA_GOODS_INDEX, arg2);
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
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(3);
	}

	private void initData() {
		initContent();
		QueryMainPageInfoTask mTask = new QueryMainPageInfoTask(mContext);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				initContent();
			}

			@Override
			public void onFail(BaseTask task, Object t) {

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
			mAccount.setText("我的点数\n" + mBalance.getBalance());
			mTotalBalance.setText("今日总计发放点数\n" + mBalance.getToday_gold());
		}
		ViewPageAdapter pageAdapter = new ViewPageAdapter(mContext);
		mBannerList = GlobalValue.getIns(mContext).getBanners();
		mViewPager.setAdapter(pageAdapter);
		pageAdapter.setBanner(mBannerList);
		pageAdapter.notifyDataSetChanged();
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
						R.layout.list_item_vip, null);

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
				viewHolder.soldOut = convertView.findViewById(R.id.sold_out);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Object obj = getItem(position);
			if (obj != null) {
				Goods goods = (Goods) obj;
				viewHolder.ivLogo.setImageUrlAndLoad("" + goods.getImg());
				viewHolder.tvTitle.setText("" + goods.getName());
				viewHolder.tvLeft.setText("余" + (int) goods.getQuantity()
						+ "名额");
				viewHolder.tvPoint.setText("需" + (int)goods.getPrice() + "点数");
				viewHolder.ivTag
						.setVisibility(goods.getFlag() == 0 ? View.INVISIBLE
								: View.VISIBLE);
				viewHolder.soldOut
						.setVisibility(goods.getQuantity() <= 0 ? View.VISIBLE
								: View.GONE);
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
		View soldOut;
	}

	private class ViewPageAdapter extends PagerAdapter {

		private BannerList mData;
		private Context mContext;

		public ViewPageAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			if (mData == null)
				return 1;
			else
				return mData.size() + 1;
		}

		public void setBanner(BannerList list) {
			mData = list;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			if (position == 0) {
				View image = inflater.inflate(R.layout.simple_image, null);
				((ViewPager) container).addView(image);
				return image;
			} else {
				View v = inflater.inflate(R.layout.simple_asyncimageview, null);
				AsyncImageView image = (AsyncImageView) v
						.findViewById(R.id.image);
				if (mData == null || position > mData.size())
					return v;
				Banner banner = mData.get(position);
				image.setImageUrlAndLoad(banner.getImg());
				container.addView(v);
				return v;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
