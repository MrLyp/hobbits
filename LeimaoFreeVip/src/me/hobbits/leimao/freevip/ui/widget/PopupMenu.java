package me.hobbits.leimao.freevip.ui.widget;

import cn.gandalf.util.DefaultProperties;
import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.util.UnreadImageUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupMenu extends PopupWindow {

	public static final String KEY_NOT_SHARE = "key_not_share_success";
	public static final int INDEX_MAIN = 0;
	public static final int INDEX_RECORD = 1;
	public static final int INDEX_TASK = 2;
	public static final int INDEX_HELP = 3;
	public static final int INDEX_ABOUT = 4;
	public static final int INDEX_RECOMMEND = 5;

	private TextView tvId;

	private View tvMain;
	private View tvRecord;
	private View tvTask;
	private View tvRecommend;
	private View tvHelp;
	private View tvAbout;
	private ImageView mainIndex, recordIndex, taskIndex, helpIndex, aboutIndex,
			recommendIndex;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
			if (mListener == null) {
				return;
			}
			int idx = -1;
			if (v == tvMain) {
				mListener.onPopupMenuClick(INDEX_MAIN);
				idx = INDEX_MAIN;
			} else if (v == tvRecord) {
				mListener.onPopupMenuClick(INDEX_RECORD);
				idx = INDEX_RECORD;
			} else if (v == tvTask) {
				mListener.onPopupMenuClick(INDEX_TASK);
				idx = INDEX_TASK;
			} else if (v == tvRecommend) {
				mListener.onPopupMenuClick(INDEX_RECOMMEND);
			} else if (v == tvHelp) {
				mListener.onPopupMenuClick(INDEX_HELP);
				idx = INDEX_HELP;
			} else if (v == tvAbout) {
				mListener.onPopupMenuClick(INDEX_ABOUT);
				idx = INDEX_ABOUT;
			}
			setIndexCircle(idx);
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

	private View getView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.widget_popupmenu, null);

		tvId = (TextView) view.findViewById(R.id.tv_id);

		tvMain = view.findViewById(R.id.rl_main);
		tvRecord = view.findViewById(R.id.rl_record);
		tvTask = view.findViewById(R.id.rl_task);
		tvRecommend = view.findViewById(R.id.rl_recommend);
		tvHelp = view.findViewById(R.id.rl_help);
		tvAbout = view.findViewById(R.id.rl_about);
		mainIndex = (ImageView) view.findViewById(R.id.index_main);
		recordIndex = (ImageView) view.findViewById(R.id.index_record);
		taskIndex = (ImageView) view.findViewById(R.id.index_task);
		helpIndex = (ImageView) view.findViewById(R.id.index_help);
		aboutIndex = (ImageView) view.findViewById(R.id.index_about);
		recommendIndex = (ImageView) view.findViewById(R.id.index_recommend);
		boolean isVisible = DefaultProperties.getBoolPref(context,
				KEY_NOT_SHARE, true);
		recommendIndex.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);

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

	private void setIndexCircle(int idx) {
		View[] views = new View[] { mainIndex, recordIndex, taskIndex,
				helpIndex, aboutIndex };
		for (int i = 0; i < 5; i++) {
			if (idx < 0 || idx > 4)
				continue;
			if (i == idx)
				views[i].setVisibility(View.VISIBLE);
			else
				views[i].setVisibility(View.INVISIBLE);
		}
	}

	private OnPopupMenuClickListener mListener;

	public void setOnPopupMenuClickListener(OnPopupMenuClickListener l) {
		mListener = l;
	}

	public interface OnPopupMenuClickListener {
		public void onPopupMenuClick(int index);
	}

}
