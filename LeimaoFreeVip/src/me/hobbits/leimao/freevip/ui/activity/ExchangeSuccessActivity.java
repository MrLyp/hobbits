package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.SubmitSuccess;
import me.hobbits.leimao.freevip.ui.widget.PopupMenu;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.gandalf.util.StringUtils;
import cn.gandalf.util.SystemUtils;
import cn.gandalf.widget.AsyncImageView;

public class ExchangeSuccessActivity extends BaseActivity implements
		OnClickListener {

	public static final String EXTRA_SUBMIT_SUCCESS = "extra_submit_success";
	public static final String EXTRA_GOODS = "extra_goods";

	private TitlebarView mTitlebarView;
	private TextView mAccount;
	private TextView mPwd;
	private AsyncImageView mLogo;
	private TextView mDetail;
	private TextView mValidTime;
	private SubmitSuccess mCardInfo;
	private Goods mGoods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_left:
			finish();
			break;
		case R.id.copy_account:
			if (mCardInfo != null) {
				SystemUtils.copyText(this, mCardInfo.getCard_no());
				Toast.makeText(this, "已成功复制到剪贴板", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.copy_pwd:
			if (mCardInfo != null) {
				SystemUtils.copyText(this, mCardInfo.getCard_pwd());
				Toast.makeText(this, "已成功复制到剪贴板", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.jump_to_record:
			toMainActivity(PopupMenu.INDEX_RECORD);
			break;
		case R.id.jump_to_main:
			toMainActivity(PopupMenu.INDEX_MAIN);
			break;
		}
	}

	private void toMainActivity(int idx) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_FRAGMENT_INDEX, idx);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		finish();
		startActivity(intent);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_exchange_success;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(this);
		mLogo = (AsyncImageView) findViewById(R.id.logo);
		mAccount = (TextView) findViewById(R.id.account);
		mPwd = (TextView) findViewById(R.id.password);
		mValidTime = (TextView) findViewById(R.id.tv_valid);
		mDetail = (TextView) findViewById(R.id.detail);
		findViewById(R.id.copy_account).setOnClickListener(this);
		findViewById(R.id.copy_pwd).setOnClickListener(this);
		findViewById(R.id.jump_to_main).setOnClickListener(this);
		findViewById(R.id.jump_to_record).setOnClickListener(this);
	}

	private void initContent() {
		Object obj = getIntent().getSerializableExtra(EXTRA_SUBMIT_SUCCESS);
		if (obj == null)
			return;
		mCardInfo = (SubmitSuccess) obj;
		obj = getIntent().getSerializableExtra(EXTRA_GOODS);
		if (obj == null)
			return;
		mGoods = (Goods) obj;
		mLogo.setImageUrlAndLoad(mGoods.getImg());
		mAccount.setText("会员账号 : " + mCardInfo.getCard_no());
		mDetail.setText(mGoods.getDetail());
		if (StringUtils.isEmpty(mCardInfo.getCard_pwd())) {
			findViewById(R.id.ll_pwd).setVisibility(View.INVISIBLE);
			mValidTime.setText("有效期信息请查看如下注意事项");
		} else {
			mPwd.setText("密码 : " + mCardInfo.getCard_pwd());
			mValidTime.setText("有效期至" + mCardInfo.getCard_expire_time());
		}
	}

}
