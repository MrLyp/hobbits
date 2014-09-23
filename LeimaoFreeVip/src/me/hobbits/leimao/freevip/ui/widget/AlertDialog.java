package me.hobbits.leimao.freevip.ui.widget;

import cn.gandalf.util.ScreenUtils;
import me.hobbits.leimao.freevip.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AlertDialog extends Dialog {

	private TextView mTitle;
	private TextView mContent;
	private TextView mPositiveButton;
	private TextView mNegativeButton;

	public AlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_confirm);
		setCanceledOnTouchOutside(false);
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(context) * 0.8f);
		dialogWindow.setAttributes(lp);
		initViews();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.title);
		mContent = (TextView) findViewById(R.id.content);
		mPositiveButton = (TextView) findViewById(R.id.positive);
		mNegativeButton = (TextView) findViewById(R.id.negtive);
		mNegativeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	public AlertDialog setPositiveButton(String text,
			android.view.View.OnClickListener l) {
		mPositiveButton.setText(text);
		if (l != null)
			mPositiveButton.setOnClickListener(l);
		return this;
	}

	public AlertDialog setNegativeButton(String text,
			android.view.View.OnClickListener l) {
		mNegativeButton.setText(text);
		if (l != null)
			mNegativeButton.setOnClickListener(l);
		return this;
	}

	public AlertDialog setTitle(String text) {
		mTitle.setText(text);
		return this;
	}

	public AlertDialog setContent(String text) {
		mContent.setText(text);
		return this;
	}

	public AlertDialog setContentGravity(int gravity) {
		mContent.setGravity(gravity);
		return this;
	}

}
