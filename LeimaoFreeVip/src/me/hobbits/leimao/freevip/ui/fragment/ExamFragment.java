package me.hobbits.leimao.freevip.ui.fragment;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.ui.activity.WebViewActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExamFragment extends BaseFragment implements OnItemClickListener {

	private final int[] icons = new int[] { R.drawable.ic_ask01,
			R.drawable.ic_ask02, R.drawable.ic_ask03, R.drawable.ic_ask04,
			R.drawable.ic_ask05 };
	private final String[] urls = new String[] {
			"http://tcvideo.bitclock.cn/index.php/Web/help?id=1",
			"http://tcvideo.bitclock.cn/index.php/Web/help?id=2",
			"http://tcvideo.bitclock.cn/index.php/Web/help?id=3",
			"http://tcvideo.bitclock.cn/index.php/Web/help?id=4",
			"http://tcvideo.bitclock.cn/index.php/Web/help?id=5" };

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_exam;
	}

	@Override
	protected void initViews() {
		super.initViews();
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ExamAdapter());
		list.setOnItemClickListener(this);
	}

	private class ExamAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return new Object();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_exam, null);
			TextView title = (TextView) convertView.findViewById(R.id.tv_title);
			title.setText("调查问卷" + (position + 1));
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			image.setImageResource(icons[position]);
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra(WebViewActivity.EXTRA_TITLE, "调查问卷");
		intent.putExtra(WebViewActivity.EXTRA_URL, urls[arg2]);
		startActivity(intent);
	}

}
