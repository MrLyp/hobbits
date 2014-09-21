package cn.gandalf.task;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import cn.gandalf.exception.CustomException;
import cn.gandalf.exception.ECode;

public abstract class BaseTask extends AsyncTask<Void, Object, Object> {
	private static final String TAG = "BaseTask";
	protected Context mContext;
	protected Resources mRes;
	private boolean mShowProgressDialog = false;
	private Dialog mProgressDialog;
	private boolean mShowCodeMsg = true;
	protected String mCodeMsg = null;
	private Map<Integer, Object> mCodeMsgs = new HashMap<Integer, Object>();
	private static Map<Integer, Object> mDefaultCodeMsgs;

	protected Callback mCallback;

	public BaseTask(Context context) {
		this.mContext = context;
		mRes = context.getResources();
	}

	public void setShowProgessDialog(boolean f) {
		mShowProgressDialog = f;
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
			}
			if (!mProgressDialog.isShowing())
				mProgressDialog.show();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
	}

	protected Dialog createLoadingDialog() {
		ProgressDialog progressDialog = new ProgressDialog(
				getTopParent((Activity) mContext));
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

	private boolean mIsCanceled = false;

	public void cancel() {
		super.cancel(true);
		mIsCanceled = true;
	}

	public boolean isCancelRequested() {
		return mIsCanceled;
	}
	
	static {
		mDefaultCodeMsgs = new HashMap<Integer, Object>();
		mDefaultCodeMsgs.put(ECode.SUCCESS, "");
		mDefaultCodeMsgs.put(ECode.FAIL, "网络连接失败，请稍后重试");
	}

	public static void setDefaultCodeMsg(int code, Object value) {
		mDefaultCodeMsgs.put(code, value);
	}

	@Override
	protected void onPostExecute(Object result) {
		processResult(result);
		dismissDialog();
		super.onPostExecute(result);
	}

	protected void processResult(Object result) {
		if (isCancelRequested()) {
			return;
		}
		int code = 0;
		if (result == null)
			result = ECode.FAIL;
		if (result.equals(ECode.CANCELED)) {
			return;
		} else if (result instanceof CustomException) {
			CustomException e = (CustomException) result;
			code = e.getCode();
		} else if (result instanceof Integer) {
			code = (Integer) result;
		} else {
			code = ECode.FAIL;
		}

		mCodeMsg = getCodeMsg(code);
		if (mCodeMsg == null)
			mCodeMsg = getCodeMsg(ECode.FAIL);
		if (mShowCodeMsg && mCodeMsg != null && mCodeMsg.length() > 0)
			showResultMessage(mCodeMsg);
		if (code == ECode.SUCCESS || code == ECode.SUCCESS_LAST_TIME) {
			if (mCallback != null)
				mCallback.onSuccess(this, code);
		} else {
			if (mCodeMsg == null)
				Log.e(TAG, "Error code message should not be null." + code
						+ " " + this.getClass().getSimpleName());
			if (mCallback != null)
				mCallback.onFail(this, code);
		}
	}

	protected void showResultMessage(String codeMsg) {
		Toast.makeText(mContext, codeMsg,Toast.LENGTH_SHORT).show();
	}

	private String getDefaultCodeMsg(int code) {
		Object o = mDefaultCodeMsgs.get(code);
		return convertCodeMsg(o);
	}

	public String getCodeMsg(int code) {
		Object o = mCodeMsgs.get(code);
		if (o != null)
			return convertCodeMsg(o);
		else
			return getDefaultCodeMsg(code);
	}

	private String convertCodeMsg(Object o) {
		if (o == null)
			return null;
		if (o instanceof String)
			return (String) o;
		if (o instanceof Integer)
			return mContext.getResources().getString((Integer) o);

		return null;
	}

	public void setShowCodeMsg(boolean b) {
		mShowCodeMsg = b;
	}

	public void setCodeMsg(Integer code, String msg) {
		mCodeMsgs.put(code, msg);
	}

	public void setCodeMsg(Integer code, int res) {
		mCodeMsgs.put(code, res);
	}

	public void disableCodeMsg(Integer code) {
		mCodeMsgs.put(code, "");
	}

	public BaseTask setCallback(Callback callback) {
		mCallback = callback;
		return this;
	}

	public BaseTask wrapCallback(final Callback newCall) {
		final Callback oldCall = mCallback;
		mCallback = new Callback() {
			@Override
			public void onSuccess(BaseTask task, Object t) {
				if (oldCall != null)
					oldCall.onSuccess(task, t);
				if (newCall != null)
					newCall.onSuccess(task, t);

			}

			@Override
			public void onFail(BaseTask task, Object t) {
				if (oldCall != null)
					oldCall.onFail(task, t);
				if (newCall != null)
					newCall.onFail(task, t);

			}
		};
		return this;
	}

	public Callback getCallback() {
		return mCallback;
	}

	public static interface Callback {
		public void onSuccess(BaseTask task, Object t);

		public void onFail(BaseTask task, Object t);
	}

}
