package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.model.BasicConfig;
import me.hobbits.leimao.freevip.ui.fragment.ExamFragment;
import me.hobbits.leimao.freevip.ui.fragment.OfferWallFragment;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class CreditActivity extends BaseFragmentActivity {

	private TitlebarView mTitlebarView;

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
		Fragment offerWallFragment = new OfferWallFragment();
		Fragment ExamFragment = new ExamFragment();
		int isHidden = 0;
		BasicConfig config = GlobalValue.getIns(this).getBasicConfig();
		if (config != null)
			isHidden = config.getIs_hidden();
		Fragment currentFragment = isHidden == 1 ? ExamFragment
				: offerWallFragment;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.stub, currentFragment).commit();
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
	}
}
