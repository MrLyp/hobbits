package me.hobbits.leimao.freevip.ui.widget;

import cn.gandalf.util.DefaultProperties;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.util.UnreadImageUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitlebarView extends RelativeLayout {

	public static final String KEY_NEW_MESSAGE_CLICKED = "key_new_message_clicked";
	private View mTitlebarView;

	private ImageButton ibLeft;
	private ImageButton ibRight;
	private TextView tvTitle;

	private ImageView ivDot;
	private ImageView ivLeftDot;

	private Context mContext;

	public TitlebarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		mTitlebarView = LayoutInflater.from(context).inflate(
				R.layout.widget_titlebar, this);

		ibLeft = (ImageButton) mTitlebarView.findViewById(R.id.ib_left);
		ibRight = (ImageButton) mTitlebarView.findViewById(R.id.ib_right);
		tvTitle = (TextView) mTitlebarView.findViewById(R.id.tv_title);
		ivDot = (ImageView) mTitlebarView.findViewById(R.id.iv_dot);
		ivLeftDot = (ImageView) mTitlebarView.findViewById(R.id.iv_dot_left);

		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs,
					R.styleable.Titlebar);
			int leftBtnSrc = ta.getResourceId(
					R.styleable.Titlebar_titleLeftBtnSrc, 0);
			if (leftBtnSrc > 0) {
				ibLeft.setImageResource(leftBtnSrc);
			}

			int rightBtnSrc = ta.getResourceId(
					R.styleable.Titlebar_titleRightBtnSrc, 0);
			if (rightBtnSrc > 0) {
				ibRight.setImageResource(rightBtnSrc);
			}

			String titleText = ta
					.getString(R.styleable.Titlebar_titleTitleText);
			if (!TextUtils.isEmpty(titleText)) {
				tvTitle.setText(titleText);
			}

			ta.recycle();
		}
	}

	public ImageButton getLeftButton() {
		return ibLeft;
	}

	public ImageButton getRightButton() {
		return ibRight;
	}

	public TextView getTitleText() {
		return tvTitle;
	}

	public void setLeftImageResource(int resId) {
		if (resId > 0) {
			ibLeft.setImageResource(resId);
		}
	}

	public void setRightImageResource(int resId) {
		if (resId > 0) {
			ibRight.setImageResource(resId);
		}
	}

	public void setOnLeftButtonClickListener(View.OnClickListener l) {
		ibLeft.setOnClickListener(l);
	}

	public void setOnRightButtonClickListener(View.OnClickListener l) {
		ibRight.setOnClickListener(l);
	}

	public void setTitleImageResource(int resid) {
		tvTitle.setBackgroundResource(resid);
	}

	@SuppressWarnings("deprecation")
	public void setTitleImageDrawable(Drawable drawable) {
		tvTitle.setBackgroundDrawable(drawable);
	}

	public void setTitleTextResource(int resid) {
		tvTitle.setText(getResources().getString(resid));
	}

	public void setTitleText(CharSequence text) {
		tvTitle.setText(text);
	}

	public void setDotVisibility(int visibility) {
		ivDot.setVisibility(visibility);
	}

	public void initDotVisibility() {
		UnreadImageUtils.getInstance(mContext).initUnreadImageVisibility(ivDot,
				KEY_NEW_MESSAGE_CLICKED);
		;
	}

	public void initLeftDotVisibility() {
		boolean isVisible = DefaultProperties.getBoolPref(mContext,
				PopupMenu.KEY_NOT_SHARE, true);
		ivLeftDot.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
	}

}
