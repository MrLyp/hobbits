package me.hobbits.leimao.freevip.ui.activity;

import java.util.List;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.db.MessageHandler;
import me.hobbits.leimao.freevip.model.Message;
import me.hobbits.leimao.freevip.ui.widget.TitlebarView;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.gandalf.widget.AsyncImageView;

public class MessageActivity extends BaseActivity {

	private TitlebarView mTitlebarView;

	private ListView lvMessage;
	private MessageAdapter adapterMessage;
	private MessageHandler mMessageHandler;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mTitlebarView.getLeftButton()) {
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMessageHandler = new MessageHandler(this);
		List<Message> list = mMessageHandler.queryAllMessage();
		adapterMessage.setData(list);
		adapterMessage.notifyDataSetChanged();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mTitlebarView = (TitlebarView) findViewById(R.id.titlebar);
		mTitlebarView.setOnLeftButtonClickListener(mOnClickListener);
		mTitlebarView.setDotVisibility(View.INVISIBLE);

		View emptyView = getLayoutInflater().inflate(
				R.layout.list_item_message_empty, null);

		adapterMessage = new MessageAdapter(this);
		lvMessage = (ListView) findViewById(R.id.lv_message);
		((ViewGroup) lvMessage.getParent()).addView(emptyView);
		lvMessage.setEmptyView(emptyView);
		lvMessage.setAdapter(adapterMessage);
	}

	private class MessageAdapter extends BaseAdapter {

		private Context mContext;
		private List<Message> mData;
		private View mSelectedView;
		private int mSelectedItem;

		public MessageAdapter(Context context) {
			mContext = context;
		}

		public void setData(List<Message> l) {
			mData = l;
		}

		@Override
		public int getCount() {
			if (mData == null)
				return 0;
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			if (mData == null || position > mData.size())
				return null;
			else
				return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = getLayoutInflater().inflate(
						R.layout.list_item_message, null);
			Object obj = getItem(position);
			if (obj == null)
				return convertView;
			final Message msg = (Message) obj;
			AsyncImageView image = (AsyncImageView) convertView
					.findViewById(R.id.image);
			image.setImageUrlAndLoad(msg.getImg());
			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText(msg.getTitle());
			TextView detail = (TextView) convertView.findViewById(R.id.summary);
			detail.setText(msg.getContent());
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					showDeleteDialog(msg.getMsg_id());
					return false;
				}
			});
			return convertView;
		}
		
		private void showDeleteDialog(final int id) {
			AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("提示").setMessage("您确定删除这条消息么").setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					mMessageHandler.deleteMessage(id);
					setData(mMessageHandler.queryAllMessage());
					notifyDataSetChanged();
				}
			}).setNegativeButton("取消", null).create();
			dialog.show();
		}
	}
}
