package me.hobbits.leimao.freevip.util;

import java.io.File;
import java.io.Serializable;

import android.content.Intent;
import android.net.Uri;

public class ShareUtils {
	
	public static enum ShareChannel {
		WECHAT, FRIEND_CIRCLE, QQ, WEIBO
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
