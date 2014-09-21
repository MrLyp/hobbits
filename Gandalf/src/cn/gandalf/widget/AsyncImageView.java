package cn.gandalf.widget;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.gandalf.R;
import cn.gandalf.util.AsyncImageLoader;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

	private Drawable mLoadingImageResource;
	private Drawable mLoadFailedImageResource;
	private Context mContext;

	public AsyncImageView(Context context) {
		this(context, null);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.AsyncImageView);

		Drawable loadingDrawable = ta
				.getDrawable(R.styleable.AsyncImageView_loadingImageResource);
		if (loadingDrawable != null)
			setLoadingImageResouce(loadingDrawable);
		Drawable loadFailedDrawable = ta
				.getDrawable(R.styleable.AsyncImageView_loadFailedImageResource);
		if (loadFailedDrawable != null)
			setLoadFailedImageResource(loadFailedDrawable);
		ta.recycle();
	}

	public void setLoadingImageResouce(Drawable drawable) {
		mLoadingImageResource = drawable;
		if (mLoadingImageResource != null)
			setImageDrawable(mLoadingImageResource);
	}

	public void setLoadFailedImageResource(Drawable drawable) {
		mLoadFailedImageResource = drawable;
	}

	public void setImageUrlAndLoad(String url) {
		if (url == null)
			return;
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
		ImageLoadingListener listener = new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				if (mLoadingImageResource != null)
					setImageDrawable(mLoadingImageResource);
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				if (mLoadFailedImageResource != null)
					setImageDrawable(mLoadFailedImageResource);
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				setImageBitmap(arg2);
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				if (mLoadFailedImageResource != null)
					setImageDrawable(mLoadFailedImageResource);
			}
		};
		ImageLoader.getInstance().displayImage(url, this, options, listener);
	}
}
