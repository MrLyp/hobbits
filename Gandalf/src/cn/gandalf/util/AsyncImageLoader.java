package cn.gandalf.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AsyncImageLoader {

	public static void init(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	public static void displayImage(ImageView view, String url) {
		try {
			ImageLoader.getInstance().displayImage(url, view);
		} catch (Exception e) {

		} catch (OutOfMemoryError er) {

		}
	}

	public static void displayImage(ImageView view, String url,
			DisplayImageOptions options, ImageLoadingListener listener) {
		try {
			ImageLoader.getInstance()
					.displayImage(url, view, options, listener);
		} catch (Exception e) {

		} catch (OutOfMemoryError er) {

		}
	}
}
