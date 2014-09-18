package me.hobbits.leimao.freevip.ui.widget;

import me.hobbits.leimao.freevip.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupMenu extends PopupWindow {

	public static final int INDEX_MAIN = 0;
	public static final int INDEX_RECORD = 1;
	public static final int INDEX_TASK = 2;
	public static final int INDEX_RECOMMEND = 3;
	public static final int INDEX_HELP = 4;
	public static final int INDEX_ABOUT = 5;

	private TextView tvId;

	private TextView tvMain;
	private TextView tvRecord;
	private TextView tvTask;
	private TextView tvRecommend;
	private TextView tvHelp;
	private TextView tvAbout;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
			if (mListener == null) {
				return;
			}
			if (v == tvMain) {
				mListener.onPopupMenuClick(INDEX_MAIN);
			} else if (v == tvRecord) {
				mListener.onPopupMenuClick(INDEX_RECORD);
			} else if (v == tvTask) {
				mListener.onPopupMenuClick(INDEX_TASK);
			} else if (v == tvRecommend) {
				mListener.onPopupMenuClick(INDEX_RECOMMEND);
			} else if (v == tvHelp) {
				mListener.onPopupMenuClick(INDEX_HELP);
			} else if (v == tvAbout) {
				mListener.onPopupMenuClick(INDEX_ABOUT);
			}
		}
	};

	public PopupMenu(Context context) {
		setContentView(getView(context));
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable());
		setOutsideTouchable(true);
		setFocusable(true);
	}

	@SuppressLint("InflateParams")
	private View getView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.widget_popupmenu, null);

		tvId = (TextView) view.findViewById(R.id.tv_id);

		tvMain = (TextView) view.findViewById(R.id.tv_main);
		tvRecord = (TextView) view.findViewById(R.id.tv_record);
		tvTask = (TextView) view.findViewById(R.id.tv_task);
		tvRecommend = (TextView) view.findViewById(R.id.tv_recommend);
		tvHelp = (TextView) view.findViewById(R.id.tv_help);
		tvAbout = (TextView) view.findViewById(R.id.tv_about);

		tvMain.setOnClickListener(mOnClickListener);
		tvRecord.setOnClickListener(mOnClickListener);
		tvTask.setOnClickListener(mOnClickListener);
		tvRecommend.setOnClickListener(mOnClickListener);
		tvHelp.setOnClickListener(mOnClickListener);
		tvAbout.setOnClickListener(mOnClickListener);

		return view;
	}

	public void setIdText(String id) {
		tvId.setText(id);
	}

	private OnPopupMenuClickListener mListener;

	public void setOnPopupMenuClickListener(OnPopupMenuClickListener l) {
		mListener = l;
	}

	public interface OnPopupMenuClickListener {
		public void onPopupMenuClick(int index);
	}

}
