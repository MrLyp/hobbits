package me.hobbits.leimao.freevip.ui.activity;

import cn.gandalf.util.StringUtils;
import cn.gandalf.util.SystemUtils;
import cn.gandalf.widget.AsyncImageView;
import me.hobbits.leimao.freevip.model.Exchange;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetailActivity extends BaseActivity implements
		OnClickListener {

	public static final String EXTRA_EXCHANGE = "extra_exchange";

	private TitlebarView mTitlebarView;
	private TextView mAccount;
	private TextView mPwd;
	private AsyncImageView mLogo;
	private TextView mDetail;
	private TextView mValidTime;
	private Exchange mExchange;

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
			if (mExchange != null) {
				SystemUtils.copyText(this, mExchange.getCard_no());
				Toast.makeText(this, "已成功复制到剪贴板", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.copy_pwd:
			if (mExchange != null) {
				SystemUtils.copyText(this, mExchange.getCard_pwd());
				Toast.makeText(this, "已成功复制到剪贴板", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.jump_out:
			startActivity(new Intent(this, CreditActivity.class));
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_record_detail;
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
		findViewById(R.id.jump_out).setOnClickListener(this);
	}

	private void initContent() {
		Object obj = getIntent().getSerializableExtra(EXTRA_EXCHANGE);
		if (obj == null)
			return;
		mExchange = (Exchange) obj;
		mLogo.setImageUrlAndLoad(mExchange.getImg());
		String detail = mExchange.getDetail();
		detail = detail.replaceAll("\\\\n", "");
		mDetail.setText(detail);
		if (StringUtils.isEmpty(mExchange.getCard_pwd())) {
			mAccount.setText("激活码 : " + mExchange.getCard_no());
			findViewById(R.id.ll_pwd).setVisibility(View.INVISIBLE);
			mValidTime.setText("有效期信息请查看如下注意事项");
		} else {
			mAccount.setText("卡号 : " + mExchange.getCard_no());
			mPwd.setText("密码 : " + mExchange.getCard_pwd());
			mValidTime.setText("有效期至" + mExchange.getCard_expire_time());
		}
	}
}
