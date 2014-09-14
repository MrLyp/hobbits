package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.view.View;
import android.widget.Toast;

public class ExchangeActivity extends BaseFragmentActivity {

	private TitlebarView mTitlebarView;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mTitlebarView.getLeftButton()) {
				finish();
			} else if (v == mTitlebarView.getRightButton()) {
				Toast.makeText(ExchangeActivity.this, "why???",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.activity_exchange;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);
		mTitlebarView.setOnRightButtonClickListener(mOnClickListener);
	}

}
