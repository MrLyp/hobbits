package cn.gandalf.task;

import java.util.concurrent.Executor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;

/**
 * a base class for all task.
 */
public abstract class BaseTask<A, B, C> extends AsyncTask<A, B, C> {
	private static final String TAG = "BaseTask";
	protected Context mContext;
	protected Resources mRes;
	/** whether to finish current activity when the ProgressDialog is canceled. */
	private boolean mFinishActivityOnCancel = false;
	/** whether show the progress dialog when task is running in background */
	private boolean mShowProgressDialog = false;
	/** whether to cancel this task when this Dialog is canceled */
	private boolean mFinshTaskOnCancel = true;
	/** whether the task is cancelable **/
	private boolean mTaskCancelAble = true;
	private String mMessage;
	private String mTitle;
	private Dialog mProgressDialog;
	private Executor mCustomExecutor;

	public BaseTask(Context context) {
		this(context, false);
	}

	public BaseTask(Context context, boolean finishActivityOnCancel) {
		this.mFinishActivityOnCancel = finishActivityOnCancel;
		this.mContext = context;
		mRes = context.getResources();
		mTitle = "消息处理";
		mMessage = "连接服务器";
		mCustomExecutor = getCustomExecutor();
	}

	public void setShowProgessDialog(boolean f) {
		mShowProgressDialog = f;
	}

	public void setFinishActivityOnCancel(boolean f) {
		mFinishActivityOnCancel = f;
	}

	public void setFinshTaskOnCancel(boolean f) {
		mFinshTaskOnCancel = f;
	}

	public void setTaskCancelAble(boolean b) {
		mTaskCancelAble = b;
	}

	public void setCustomExecutor(Executor executor) {
		mCustomExecutor = executor;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mShowProgressDialog) {
			showDialog();
		}
	}

	@Override
	protected void onCancelled() {
		dismissDialog();
	}

	protected void onPostExecute(C result) {
		dismissDialog();
	}

	public void dismissDialog() {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		} catch (Exception e) {
		}
	}

	private void showDialog() {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = createLoadingDialog();
				mProgressDialog.setCancelable(mTaskCancelAble);
				mProgressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (mFinishActivityOnCancel) {
							finishContext();
						}
						if (mFinshTaskOnCancel) {
							cancel(true);
							if (mOnCancelListener != null) {
								mOnCancelListener.onCancel(BaseTask.this);
							}
						}
					}
				});
			}
			if (!mProgressDialog.isShowing())
				mProgressDialog.show();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
	}

	protected Dialog createLoadingDialog() {
		// use top parent context to create the dialog. since the dialog should
		// not appear if the activity is in a
		// TabHost.
		ProgressDialog progressDialog = new ProgressDialog(
				getTopParent((Activity) mContext));
		if (mTitle != null)
			progressDialog.setTitle(mTitle);
		if (mMessage != null)
			progressDialog.setMessage(mMessage);
		return progressDialog;
	}

	private Context getTopParent(Activity context) {
		Activity parent = context.getParent();
		while (parent != null) {
			context = parent;
			parent = context.getParent();
		}
		return context;
	}

	public void finishContext() {
		if (mContext instanceof Activity) {
			((Activity) mContext).onBackPressed();
		}
	}

	public void setLoadingTitle(String title) {
		mTitle = title;
		if (mProgressDialog instanceof ProgressDialog) {
			((ProgressDialog) mProgressDialog).setTitle(mTitle);
		}
	}

	public void setLoadingMessage(String message) {
		mMessage = message;
		if (mProgressDialog instanceof ProgressDialog) {
			((ProgressDialog) mProgressDialog).setMessage(message);
		}
	}

	private boolean mIsCanceled = false;

	public void cancel() {
		super.cancel(true);
		mIsCanceled = true;
	}

	public boolean isCancelRequested() {
		return mIsCanceled;
	}

	private OnTaskCancelListener mOnCancelListener;

	public void setOnCancelListener(OnTaskCancelListener l) {
		mOnCancelListener = l;
	}

	public static interface OnTaskCancelListener {
		public void onCancel(BaseTask task);
	}

	@SuppressLint("NewApi")
	public AsyncTask<A, B, C> execute(A params) {
		if (mCustomExecutor != null
				&& VERSION.SDK_INT > VERSION_CODES.HONEYCOMB)
			return super.executeOnExecutor(mCustomExecutor, params);
		else
			return super.execute(params);
	}

	protected Executor getCustomExecutor() {
		return null;
	}
}
