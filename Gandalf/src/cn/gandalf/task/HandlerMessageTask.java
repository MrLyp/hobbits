package cn.gandalf.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.gandalf.exception.CustomException;
import cn.gandalf.exception.ECode;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

public abstract class HandlerMessageTask extends BaseTask<Void, Object, Object> {
	private static final String TAG = "HandlerMessageTask";
	private boolean mShowCodeMsg = true;
	protected String mCodeMsg = null;
	private Map<Integer, Object> mCodeMsgs = new HashMap<Integer, Object>();
	private static Map<Integer, Object> mDefaultCodeMsgs;

	protected Callback mCallback;

	static {
		mDefaultCodeMsgs = new HashMap<Integer, Object>();
		mDefaultCodeMsgs.put(ECode.SUCCESS, "");
		mDefaultCodeMsgs.put(ECode.FAIL, "网络连接失败，请稍后重试");
	}

	public HandlerMessageTask(Context context) {
		super(context);
	}

	public static void setDefaultCodeMsg(int code, Object value) {
		mDefaultCodeMsgs.put(code, value);
	}

	public Object runBackground() {
		return doInBackground();
	}

	public void postExecute(Object result) {
		onPostExecute(result);
	}

	@Override
	protected void onPostExecute(Object result) {
		processResult(result);
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

	public HandlerMessageTask setCallback(Callback callback) {
		mCallback = callback;
		return this;
	}

	public HandlerMessageTask wrapCallback(final Callback newCall) {
		final Callback oldCall = mCallback;
		mCallback = new Callback() {
			@Override
			public void onSuccess(HandlerMessageTask task, Object t) {
				if (oldCall != null)
					oldCall.onSuccess(task, t);
				if (newCall != null)
					newCall.onSuccess(task, t);

			}

			@Override
			public void onFail(HandlerMessageTask task, Object t) {
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
		public void onSuccess(HandlerMessageTask task, Object t);

		public void onFail(HandlerMessageTask task, Object t);
	}

}
