package me.hobbits.leimao.freevip.ui.activity;

import cn.gandalf.json.ErrorResp;
import cn.gandalf.task.HandlerMessageTask;
import cn.gandalf.task.HandlerMessageTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.widget.AsyncImageView;
import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.SubmitSuccess;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import me.hobbits.leimao.freevip.R;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ExchangeActivity extends BaseFragmentActivity implements
		OnClickListener {

	public static final String EXTRA_GOODS_ID = "extra_goods_id";

	private TitlebarView mTitlebarView;
	private AsyncImageView mLogo;
	private TextView mRemain;
	private TextView mName;
	private TextView mPrice;
	private TextView mAccount;
	private TextView mConfirm;
	private TextView mDetail;

	private Goods mGoods;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_exchange;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(this);
		mTitlebarView.setOnRightButtonClickListener(this);
		mLogo = (AsyncImageView) findViewById(R.id.logo);
		mRemain = (TextView) findViewById(R.id.quantity);
		mName = (TextView) findViewById(R.id.name);
		mPrice = (TextView) findViewById(R.id.price);
		mAccount = (TextView) findViewById(R.id.account);
		mDetail = (TextView) findViewById(R.id.detail);
		mConfirm = (TextView) findViewById(R.id.confirm);
		mConfirm.setOnClickListener(this);
		initContent();
	}

	private void initContent() {
		int idx = getIntent().getIntExtra(EXTRA_GOODS_ID, -1);
		if (idx == -1)
			return;
		mGoods = GlobalValue.getIns(this).getGoodsItem(idx);
		if (mGoods == null)
			return;
		mLogo.setImageUrlAndLoad(mGoods.getImg());
		mRemain.setText("余" + mGoods.getQuantity() + "名额");
		mPrice.setText("需" + mGoods.getPrice() + "点数");
		mName.setText(mGoods.getName());
		mDetail.setText(mGoods.getDetail());
		Balance balance = GlobalValue.getIns(this).getBalance();
		if (balance != null) {
			mAccount.setText("我的点数：" + balance.getBalance() + "点数");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mTitlebarView.getLeftButton()) {
			finish();
		} else if (v == mTitlebarView.getRightButton()) {
			Toast.makeText(ExchangeActivity.this, "why???", Toast.LENGTH_SHORT)
					.show();
		} else if (v == mConfirm) {
			onSubmit();
		}
	}

	private void onSubmit() {
		if (mGoods == null)
			return;
		final HttpConnectTask mTask = new HttpConnectTask(this,
				HttpManager.getSubmitParam(mGoods.getGoods_id()));
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(HandlerMessageTask task, Object t) {
				SubmitSuccess result = (SubmitSuccess) mTask.getResult();
			}

			@Override
			public void onFail(HandlerMessageTask task, Object t) {
				ErrorResp error = mTask.getError();
				if (error != null) {
					Toast.makeText(mContext, error.getError_message(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		mTask.setShowProgessDialog(true);
		mTask.setShowCodeMsg(false);
		mTask.execute();
	}
}
