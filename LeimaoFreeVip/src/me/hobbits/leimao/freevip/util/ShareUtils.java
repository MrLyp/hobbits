package me.hobbits.leimao.freevip.util;

import java.io.File;
import java.io.Serializable;

import me.hobbits.leimao.freevip.R;
import me.hobbits.leimao.freevip.net.HttpManager;

import cn.gandalf.json.ErrorResp;
import cn.gandalf.task.BaseTask;
import cn.gandalf.task.BaseTask.Callback;
import cn.gandalf.task.HttpConnectTask;
import cn.gandalf.util.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class ShareUtils {

	public final static String PATH_SD = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public final static String PATH_TEMP = PATH_SD
			+ "/me.hobbits.leimao.freevip/temp";

	public static final String URL_SHARE = "http://tcvideo.bitclock.cn/index.php/Web/share";

	public static enum ShareChannel {
		WECHAT, FRIEND_CIRCLE, QQ, WEIBO
	}

	private static String mShareImagePath = null;

	public static void initShareFile(Context context, boolean isLogo) {
		String dirPath = PATH_TEMP;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File shareImage = new File(dir, isLogo ? "logo.png" : "share.jpg");
		if (!shareImage.exists()) {
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
					isLogo ? R.drawable.img_share_logo : R.drawable.img_share);
			if (bmp != null) {
				BitmapUtils.saveToFile(shareImage.getAbsolutePath(), bmp);
			}
		}
		shareImage = new File(dir, isLogo ? "logo.png" : "share.jpg");
		if (shareImage.exists())
			mShareImagePath = shareImage.getAbsolutePath();
	}

	public static String getShareImagePath(Context context, boolean isLogo) {
		if (mShareImagePath == null || !(new File(mShareImagePath).exists())) {
			try {
				initShareFile(context, isLogo);
			} catch (Exception e) {
			}
		}
		return mShareImagePath;
	}

	public static void updateShareResult(final Context context) {
		final HttpConnectTask mTask = new HttpConnectTask(context,
				HttpManager.getShareParam());
		mTask.setShowCodeMsg(false);
		mTask.setCallback(new Callback() {

			@Override
			public void onSuccess(BaseTask task, Object t) {
				ErrorResp res = (ErrorResp) mTask.getResult();
				if (res.getResult() == 1)
					Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT)
							.show();
			}

			@Override
			public void onFail(BaseTask task, Object t) {

			}
		});
		mTask.execute();
	}

	public static class ShareContent implements Serializable {
		private static final long serialVersionUID = -6992225722166865874L;
		private static final int MAX_TEXT_LENGTH = 137;
		public String title;
		public String content;
		public String url;
		public String imagePath;

		public ShareContent() {
		}

		public Uri getImageUri() {
			File imageFile = imagePath == null ? null : new File(imagePath);
			if (imageFile != null && imageFile.exists() && imageFile.isFile())
				return Uri.fromFile(imageFile);
			return null;
		}

		public String mergeAllText() {
			return mergeAllText(MAX_TEXT_LENGTH);
		}

		public String mergeAllText(int maxTextLen) {
			String suffix = "";
			String shareContent = "";
			int titleLen = title == null ? 0 : title.length();
			int contentLen = content == null ? 0 : content.length();
			int urlLen = url == null ? 0 : url.length();
			int limitContentLen = maxTextLen - titleLen - urlLen;

			if (title != null)
				shareContent += title + "\n";
			if (content != null && limitContentLen > 0) {
				if (contentLen > limitContentLen) {
					suffix = "...";
					limitContentLen = Math.max(0, limitContentLen - 3);
				}
				limitContentLen = Math.min(limitContentLen, contentLen);
				shareContent += content.substring(0, limitContentLen) + suffix
						+ "\n";
			}
			if (url != null)
				shareContent += url;

			return shareContent;
		}

		public Intent wrapIntent() {
			Intent intent = new Intent(Intent.ACTION_SEND);
			Uri imageUri = getImageUri();
			if (imageUri == null) {
				intent.setType("text/plain");
			} else {
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			}
			intent.putExtra(Intent.EXTRA_TEXT, mergeAllText());

			return intent;
		}
	}
}
