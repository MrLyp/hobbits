package me.hobbits.leimao.freevip.ui.activity;

import java.util.Hashtable;

import me.hobbits.leimao.freevip.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.immob.sdk.AdType;
import cn.immob.sdk.ImmobView;
import cn.immob.sdk.LMAdListener;
public class LimeiWallActivity extends Activity implements LMAdListener {
	private String tag = "LimeiWallActivity";
	private ImmobView view = null;
	public static String adUnitID = "dda1517dd899f51460645391de7ffab1";
	private LinearLayout layout = null;
	private RelativeLayout rlReceivedFaild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adwall);
		layout = (LinearLayout) findViewById(R.id.layout);
		rlReceivedFaild = (RelativeLayout) findViewById(R.id.rl_received_failed);
		Context context = this;
		view = new ImmobView(context, adUnitID,AdType.WALL);
		Hashtable<String, String> userProperties = new Hashtable<String, String>();
		String usrId = "";
		userProperties.put("accountname", usrId);
		view.setUserInfo(userProperties);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		view.setLayoutParams(params);
		view.setAdListener(this);
		layout.addView(view);
		layout.setBackgroundColor(Color.WHITE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (view != null) {
			view.destroy();
		}
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		rlReceivedFaild.setVisibility(View.GONE);
		view.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * This method will be called when a single ad request performed
	 * successfully.
	 */

	@Override
	public void onAdReceived(ImmobView arg0) {
		if (view != null) {
			view.display();
		}
	}

	/**
	 * This method will be called when some error
	 * 
	 * @param eCode
	 */
	@Override
	public void onDismissScreen(ImmobView arg0) {
		finish();

	}

	@Override
	public void onFailedToReceiveAd(ImmobView arg0, int arg1) {
		rlReceivedFaild.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLeaveApplication(ImmobView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPresentScreen(ImmobView arg0) {
		// TODO Auto-generated method stub

	}
}
