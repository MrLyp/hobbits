package me.hobbits.leimao.freevip.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	protected FragmentActivity mContext;
	private View mView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(getLayoutId(), container, false);
		initViews();
		return mView;
	}

	abstract protected int getLayoutId();

	protected void initViews() {

	}

	protected View findViewById(int id) {
		return mView.findViewById(id);
	}
}