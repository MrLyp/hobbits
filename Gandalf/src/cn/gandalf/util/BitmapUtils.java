package cn.gandalf.util;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtils {

	public static Bitmap createImageThumbWithLim(String path, int maxWidth,
			int maxHeight) {
		if (path == null)
			return null;
		File file = new File(path);
		if (!file.exists() || !file.isFile())
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		setSampleRateWithLim(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(path, options);
		Bitmap sbm = createScaledBitmapWithLim(bm, maxWidth, maxHeight);
		if (sbm != bm)
			bm.recycle();
		return sbm;
	}

	private static void setSampleRateWithLim(BitmapFactory.Options ops,
			int maxWidth, int maxHeight) {
		if (ops == null)
			return;

		float xr = 1.0f * ops.outWidth / maxWidth, yr = 1.0f * ops.outHeight
				/ maxHeight;
		ops.inSampleSize = (int) Math.max(xr, yr);
	}

	private static Bitmap createScaledBitmapWithLim(Bitmap bitmap,
			int maxWidth, int maxHeight) {
		float hr = bitmap.getHeight() / maxHeight;
		float wr = bitmap.getWidth() / maxWidth;
		float sr = Math.max(Math.max(hr, wr), 1.0f);

		Matrix matrix = new Matrix();
		matrix.postScale(sr, sr);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
				(int) (bitmap.getWidth() / sr),
				(int) (bitmap.getHeight() / sr), matrix, true);
		return resizeBmp;
	}
}
