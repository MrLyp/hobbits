package me.hobbits.leimao.freevip.ui.activity;

import com.mfgqsipin.Bxhtod;

import cn.dm.android.DMOfferWall;
import cn.waps.AppConnect;
import me.hobbits.leimao.freevip.model.Task;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initOfferWalls();
	}

	private void initOfferWalls() {
		DMOfferWall.init(this, "96ZJ2vzgzeBz/wTBCV");
		AppConnect
				.getInstance("fae474acea708f29b58ca549d9c6514e", "waps", this);
		Bxhtod.initGoogleContext(this, "8551bfb5d76985621430994362ab75c5");
		Bxhtod.setCurrentUserID(this, "1234");
		Bxhtod.setCustomActivity("com.mfgqsipin.BxhtodGoogleActivity");
		Bxhtod.setCustomService("com.mfgqsipin.BxhtodGoogleService");
	}

	private void onTaskClick(Task task) {
		if (task == null)
			return;
		if (task.getName().equals("domob")) {
			DMOfferWall.getInstance(this).showOfferWall(this);
		} else if (task.getName().equals("waps")) {
			AppConnect.getInstance(this).showOffers(this);
		} else if (task.getName().equals("adwo")) {
			Bxhtod.showOffers(this);
		} else if (task.getName().equals("adview")) {
			startActivity(new Intent(this, LimeiWallActivity.class));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(this).close();
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
