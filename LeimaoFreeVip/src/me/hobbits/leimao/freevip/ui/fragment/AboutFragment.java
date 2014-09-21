package me.hobbits.leimao.freevip.ui.fragment;

import cn.gandalf.util.SystemUtils;
import android.widget.TextView;
import me.hobbits.leimao.freevip.R;

public class AboutFragment extends BaseFragment {

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_about;
	}

	@Override
	protected void initViews() {
		super.initViews();
		TextView version = (TextView) findViewById(R.id.version);
		version.setText("版本号 ： V" + SystemUtils.getVersionName(mContext));
	}

}
