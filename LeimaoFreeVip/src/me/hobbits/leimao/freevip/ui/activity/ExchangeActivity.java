package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.SubmitSuccess;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.ui.fragment.HelpFragment;
import me.hobbits.leimao.freevip.ui.widget.AlertDialog;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.gandalf.json.ErrorResp;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.widget.AsyncImageView;

public class ExchangeActivity extends BaseFragmentActivity implements
		OnClickListener {

	public static final String EXTRA_GOODS_INDEX = "extra_goods_idx";

	private TitlebarView mTitlebarView;
	private AsyncImageView mLogo;
	private TextView mRemain;
	private TextView mName;
	private TextView mPrice;
	private TextView mAccount;
	private TextView mConfirm;
	private TextView mDetail;

	private Goods mGoods;
	private Balance mBalance;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initContent();
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
	}

	private void initContent() {
		int idx = getIntent().getIntExtra(EXTRA_GOODS_INDEX, -1);
		if (idx == -1)
			return;
		mGoods = GlobalValue.getIns(this).getGoodsItem(idx);
		if (mGoods == null)
			return;
		mLogo.setImageUrlAndLoad(mGoods.getImg());
		mRemain.setText("余" + (int) mGoods.getQuantity() + "名额");
		mPrice.setText("需" + (int) mGoods.getPrice() + "点数");
		mName.setText(mGoods.getName());
		mDetail.setText(mGoods.getDetail());
		mBalance = GlobalValue.getIns(this).getBalance();
		if (mBalance != null) {
			mAccount.setText("我的点数：" + mBalance.getBalance() + "点数");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mTitlebarView.getLeftButton()) {
			finish();
		} else if (v == mTitlebarView.getRightButton()) {
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(WebViewActivity.EXTRA_TITLE, "帮助");
			intent.putExtra(WebViewActivity.EXTRA_URL, HelpFragment.URL_HELP);
			startActivity(intent);
		} else if (v == mConfirm) {
			if (mBalance.getBalance() < mGoods.getPrice())
				showCreditLowDialog();
			else
				showExchangeConfirmDialog();
		}
	}

	private void onSubmit() {
		if (mGoods == null)
			return;
		final HttpConnectTask mTask = new HttpConnectTask(this,
				HttpManager.getSubmitParam(mGoods.getGoods_id()));
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				SubmitSuccess result = (SubmitSuccess) mTask.getResult();
				if (result != null && result.getResult() == 1) {
					Intent intent = new Intent(mContext,
							ExchangeSuccessActivity.class);
					intent.putExtra(
							ExchangeSuccessActivity.EXTRA_SUBMIT_SUCCESS,
							result);
					intent.putExtra(ExchangeSuccessActivity.EXTRA_GOODS, mGoods);
					startActivity(intent);
				} else {
					Toast.makeText(mContext, "兑换失败，请稍后再试", Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onFail(BaseTask task, Object t) {
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

	private void showExchangeConfirmDialog() {
		final AlertDialog dialog = new AlertDialog(this);
		String content = "兑换 : " + mGoods.getName() + "\n花费 ： "
				+ mGoods.getPrice() + "点数";
		dialog.setTitle("请确认以下信息").setContent(content)
				.setContentGravity(Gravity.LEFT)
				.setPositiveButton("马上兑换", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						onSubmit();
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	private void showCreditLowDialog() {
		final AlertDialog dialog = new AlertDialog(this);
		String content = "你的点数不够，还不能马上兑换呢\n赶快去赚些点数吧！";
		dialog.setTitle("提示").setContent(content)
				.setContentGravity(Gravity.CENTER)
				.setPositiveButton("马上去赚", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(mContext, CreditActivity.class));
						dialog.dismiss();
					}
				});
		dialog.show();
	}
}
