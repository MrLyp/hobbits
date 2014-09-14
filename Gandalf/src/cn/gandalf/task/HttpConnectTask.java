package cn.gandalf.task;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;
import cn.gandalf.exception.CustomException;
import cn.gandalf.exception.ECode;
import cn.gandalf.json.ErrorResp;
import cn.gandalf.json.JsonItem;
import cn.gandalf.net.BaseHttpManager;
import cn.gandalf.net.BaseHttpManager.HttpResp;
import cn.gandalf.net.BaseHttpManager.RequestParam;
import cn.gandalf.net.BaseHttpManager.ServerApi;
import cn.gandalf.util.JsonUtils;

public class HttpConnectTask extends HandlerMessageTask {
	private static final String TAG = "HttpConnectTask";
	private JsonItem mData = null;
	private ErrorResp mError;
	private RequestParam mParams;
	private Context mContext;

	private boolean mHasCache = false;
	private boolean mSkipCache = false;

	public HttpConnectTask(Context context, RequestParam params) {
		super(context);
		this.mParams = params;
		this.mContext = context.getApplicationContext();
	}

	public JsonItem getResult() {
		return mData;
	}

	public ErrorResp getError() {
		return mError;
	}

	@Override
	protected Object doInBackground(Void... params) {
		return process();
	}

	public Object process() {
		if (mParams == null)
			return ECode.FAIL;
		HttpResp resp = null;
		try {
			resp = BaseHttpManager.executeRequest(mParams);
		} catch (CustomException e1) {
			Log.e("lyp", "", e1);
		}
		if (resp == null || resp.mStatusCode <= 0) {
			Log.e("lyp","resp is null");
			return ECode.FAIL;
		}

		String body = resp.mBodyContent;
		Log.e("lyp", "body = " + body);
		if (body == null) {
			Log.e("lyp", "body is null");
			return ECode.FAIL;
		}

		int statusCode = resp.mStatusCode;
		try {
			if (statusCode == HttpStatus.SC_OK) {
				ServerApi api = mParams.getServerApi();

				mData = JsonUtils.parseJSONToObject(api.getResponse(), body);
				if (mData == null)
					mError = JsonUtils.parseJSONToObject(ErrorResp.class, body);
			}

		} catch (Exception e) {
			Log.e("lyp", "", e);
			return ECode.FAIL;
		}

		return ECode.FAIL;
	}
}
