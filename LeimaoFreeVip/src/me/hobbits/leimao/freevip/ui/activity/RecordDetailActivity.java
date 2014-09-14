package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.view.View;

public class RecordDetailActivity extends BaseActivity {

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
	protected int getLayoutId() {
		return R.layout.activity_record_detail;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);

	}
}
