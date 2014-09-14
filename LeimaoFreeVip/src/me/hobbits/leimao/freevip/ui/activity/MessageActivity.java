package me.hobbits.leimao.freevip.ui.activity;

import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import me.hobbits.leimao.freevip.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MessageActivity extends BaseActivity {

	private TitlebarView mTitlebarView;

	private ListView lvMessage;
	private BaseAdapter adapterMessage;

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
		return R.layout.activity_message;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);

		ImageView emptyView = new ImageView(this);
		emptyView.setImageResource(R.drawable.ic_message_board);

		adapterMessage = new MessageAdapter(this);
		lvMessage = (ListView) findViewById(R.id.lv_message);
		lvMessage.setEmptyView(emptyView);
		lvMessage.setAdapter(adapterMessage);
	}

	private class MessageAdapter extends BaseAdapter {

		private Context mContext;

		public MessageAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(mContext);
			return textView;
		}

	}

}
