package cn.gandalf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Debug;
import android.util.Log;

public class BitmapUtils {
	
	public static final String TAG = "BitmapUtils";

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
	
	private static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	private static int getSampleSizeByScreen(Context context, BitmapFactory.Options ops) {
		if (ops == null)
			return 0;
		int screenHeight = ScreenUtils.getScreenHeight(context);
		int screenWidth = ScreenUtils.getScreenWidth(context);
		float xr = 1.0f * ops.outWidth / screenWidth, yr = 1.0f * ops.outHeight
				/ (screenHeight * 2);
		int sampleSize = (int) Math.max(xr, yr);
		return sampleSize;
	}

	private static int getSampleSizeByMem(BitmapFactory.Options ops) {
		Runtime rt = Runtime.getRuntime();
		long availMem = 0;
		if (rt != null) {
			availMem = rt.maxMemory() - Debug.getNativeHeapAllocatedSize();
		}
		availMem = (long) (0.5f * availMem);
		int bmpSize = 4 * ops.outHeight * ops.outWidth;
		int sampleSize = 1;
		if (bmpSize > availMem)
			sampleSize = (int) Math.ceil(log(bmpSize / availMem, 2.0));
		return sampleSize;
	}

	public static Bitmap getResizedBitmapFromFile(Context context, String path) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeFile(path, options);
			int sampleSize = Math.max(getSampleSizeByMem(options),
					getSampleSizeByScreen(context,options));
			if (sampleSize < 1)
				sampleSize = 1;
			options.inSampleSize = sampleSize;
			options.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(path, options);
		} catch (Throwable e) {
			Log.v(TAG, "", e);
		}
		return bmp;
	}
	
	public static String saveToFile(String filePath, Bitmap bmp) {
		if (filePath == null || bmp == null)
			return null;
		File file = new File(filePath);
		File dir = file.getParentFile();
		if (dir != null && !dir.exists())
			dir.mkdirs();

		FileOutputStream fos = null;
		try {
			if (!file.exists())
				file.createNewFile();
			fos = new FileOutputStream(file);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
			}
		} catch (IOException ie) {
			Log.d(TAG, "save bitmap to file failed. IOE: ", ie);
		} catch (Exception e) {
			Log.d(TAG, "save bitmap to file failed.", e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
		return file.getAbsolutePath();
	}
}
