package cn.gandalf.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import cn.gandalf.exception.CustomException;
import cn.gandalf.json.JsonItem;
import cn.gandalf.net.BaseHttpManager.RequestParam;
import cn.gandalf.util.JsonUtils;
import cn.gandalf.util.StringUtils;

/**
 * contain the base methods to invoke api via http. only for android.
 */
public class BaseHttpManager {
	protected static final String TAG = "BaseHttpsManager";
	/** connection timeout duration. */
	private static int TIMEOUT_CONNECTION = 20000;
	/** socket timeout duration */
	private static int TIMEOUT_SOCKET = 20000;

	protected static final String TAG_PARAM = "param";
	protected static final String PARAM_SERVER_API = "api";
	protected static final String PARAM_COMPRESS = "compress";

	protected static Context mContext;

	private static HttpParams mHttpParams;

	protected static final long DURATION_3HOUR = 3 * 3600 * 1000;

	/**
	 * must be called before invoke the class.
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		mContext = context.getApplicationContext();
		initHttpParameters(context);
	}

	/**
	 * init the httpParam. add proxy to param if the network is cmwap.
	 */
	private static void initHttpParameters(Context context) {
		if (mHttpParams == null) {
			mHttpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(mHttpParams,
					TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(mHttpParams, TIMEOUT_SOCKET);
		}
	}

	public static HttpResp executeRequest(RequestParam params)
			throws CustomException {
		HttpResp resp = null;
		try {
			switch (params.getHttpMethod()) {
			case RequestParam.METHOD_GET:
				resp = executeGetRequest(params);
				break;
			case RequestParam.METHOD_POST:
				resp = executePostRequest(params);
				break;
			}

		} catch (Exception e) {
			Log.e(TAG, "Error in http request", e);
			throw new CustomException("Error in http request", e);
		}
		return resp;
	}

	public static HttpResp executePostRequest(RequestParam params)
			throws ParseException, IOException {
		String urlStr = params.getUrl();
		HttpResp resp = executePostRequest(urlStr, params.buildEntity(),
				params.getHeaders());
		return resp;
	}

	public static HttpResp executePostRequest(String urlStr,
			List<NameValuePair> params, List<NameValuePair> appendHeaders)
			throws ParseException, IOException {
		HttpEntity requestEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		return executePostRequest(urlStr, requestEntity, appendHeaders);
	}

	public static HttpResp executePostRequest(String urlStr,
			HttpEntity requestEntity) throws ParseException, IOException {
		return executePostRequest(urlStr, requestEntity, null);
	}

	public static HttpResp executePostRequest(String urlStr,
			HttpEntity requestEntity, List<NameValuePair> appendHeaders)
			throws ParseException, IOException {
		if (mContext == null)
			throw new RuntimeException("Must call init() first");
		if (urlStr == null || requestEntity == null)
			throw new RuntimeException("Param url or entity is null.");
		HttpPost httpRequest = new HttpPost(urlStr);
		httpRequest.setHeader("Accept-Encoding", "gzip, deflate");
		if (appendHeaders != null) {
			for (NameValuePair nv : appendHeaders) {
				httpRequest.addHeader(nv.getName(), nv.getValue());
			}
		}

		httpRequest.setEntity(requestEntity);
		HttpResponse httpResponse = new DefaultHttpClient(mHttpParams)
				.execute(httpRequest);
		HttpResp resp = new HttpResp(httpResponse);
		return resp;
	}

	public static HttpResp executeGetRequest(RequestParam param)
			throws ParseException, IOException {
		return executeGetRequest(param.toUrlWithParam(true), param.getHeaders());
	}

	public static HttpResp executeGetRequest(String url) throws ParseException,
			IOException {
		return executeGetRequest(url, null);
	}

	public static HttpResp executeGetRequest(String url,
			List<NameValuePair> appendHeaders) throws ParseException,
			IOException {
		return executeGetRequest(URI.create(url), appendHeaders);
	}

	public static HttpResp executeGetRequest(URI urlStr,
			List<NameValuePair> appendHeaders) throws ParseException,
			IOException {
		if (mContext == null)
			throw new RuntimeException("Must call init() first");
		HttpGet httpRequest = new HttpGet(urlStr);
		httpRequest.setHeader("Accept-Encoding", "gzip");
		if (appendHeaders != null) {
			for (NameValuePair nv : appendHeaders) {
				httpRequest.addHeader(nv.getName(), nv.getValue());
			}
		}
		HttpResponse httpResponse = new DefaultHttpClient(mHttpParams)
				.execute(httpRequest);
		HttpResp resp = new HttpResp(httpResponse);
		return resp;
	}

	private static String parseHttpResponse(HttpResponse httpResponse)
			throws ParseException, IOException {
		HttpEntity responseEntity = getEntity(httpResponse.getEntity());
		return EntityUtils.toString(responseEntity);
	}

	private static HttpEntity getEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		Header ceheader = entity.getContentEncoding();
		if (ceheader != null) {
			HeaderElement[] codecs = ceheader.getElements();
			for (HeaderElement codec : codecs) {
				String codecname = codec.getName().toLowerCase(Locale.US);
				if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
					return new GzipDecompressingEntity(entity);
				} else if ("deflate".equals(codecname)) {
					return new DeflateDecompressingEntity(entity);
				}
			}
		}
		return entity;
	}

	public static HttpResponse getHttpResponse(String fileUrl) {
		HttpGet httpRequest = new HttpGet(fileUrl);
		HttpResponse httpResponse = null;
		try {
			httpResponse = new DefaultHttpClient(mHttpParams)
					.execute(httpRequest);
		} catch (Exception e) {
			Log.e(TAG, "error in getting respponse for " + fileUrl, e);
		}
		return httpResponse;
	}

	public static <T extends JsonItem> T processApi(RequestParam param)
			throws CustomException {
		Class<? extends JsonItem> resClass = param.getServerApi().getResponse();
		HttpResp resp = executeRequest(param);
		if (resClass != null && resp != null && resp.mBodyContent != null)
			return (T) JsonUtils.parseJSONToObject(resClass, resp.mBodyContent);
		else
			return null;
	}

	public static class RequestParam extends ArrayList<NameValuePair> {
		private static final long serialVersionUID = 1L;
		public static final int METHOD_POST = 0;
		public static final int METHOD_GET = 1;
		protected String mUrl;
		protected int mHttpMethod;
		protected HttpEntity mEntity;

		protected ArrayList<NameValuePair> mCustomHeaders;
		protected ServerApi mApi;

		public RequestParam(ServerApi api) {
			mApi = api;
			mUrl = api.getUrl();
			mHttpMethod = METHOD_POST;
			mCustomHeaders = new ArrayList<NameValuePair>();
		}

		public ServerApi getServerApi() {
			return mApi;
		}

		public HttpEntity buildEntity() throws UnsupportedEncodingException {
			return buildEntity(false);
		}

		public HttpEntity buildEntity(boolean rebuild)
				throws UnsupportedEncodingException {
			if (mEntity != null && !rebuild)
				return mEntity;
			mEntity = new UrlEncodedFormEntity(this, HTTP.UTF_8);
			return mEntity;
		}

		public String getHttpMethodName() {
			return mHttpMethod == METHOD_GET ? "GET" : "POST";
		}

		public int getHttpMethod() {
			return mHttpMethod;
		}

		public void setHttpMethod(int method) {
			mHttpMethod = method;
		}

		public void addNameValuePair(String key, Object value) {
			if (value != null && key != null)
				add(new SingleNameValuePair(key, value.toString()));
		}

		public void addHeader(String key, String value) {
			if (key == null || value == null)
				return;
			NameValuePair reduplicateNV = null;
			for (NameValuePair nv : mCustomHeaders) {
				if (key.equalsIgnoreCase(nv.getName())) {
					reduplicateNV = nv;
					break;
				}
			}
			if (reduplicateNV != null) {
				mCustomHeaders.remove(reduplicateNV);
			}
			mCustomHeaders.add(new SingleNameValuePair(key, value));
		}

		public List<NameValuePair> getHeaders() {
			return mCustomHeaders;
		}

		public Object getParamValue(String key) {
			NameValuePair n = getNameValuePair(key);
			if (n != null)
				return n.getValue();
			return null;
		}

		public NameValuePair getNameValuePair(String key) {
			for (NameValuePair n : this)
				if (n.getName().equals(key))
					return n;
			return null;
		}

		@Override
		public boolean add(NameValuePair object) {
			remove(object);
			return super.add(object);
		}

		public String getUrl() {
			return mUrl;
		}

		/** combine url and params. */
		public String toUrlWithParam(boolean withOptional) {
			String url = mUrl;
			String param = getParamStr(withOptional);
			if (StringUtils.isEmpty(param))
				return url;
			if (url.indexOf('?') >= 0) {
				url += "&" + param;
			} else {
				url += "?" + param;
			}
			return url;
		}

		protected String getParamStr(boolean withOptional) {
			StringBuffer sb = new StringBuffer();
			for (NameValuePair p : this) {
				if (withOptional)
					sb.append(p.getName() + "=" + p.getValue() + "&");
			}
			String res = sb.toString();
			if (res.length() > 0)
				res = res.substring(0, res.length() - 1);
			return res;
		}

		@Override
		public String toString() {
			return toUrlWithParam(true);
		}
	}

	private static class SingleNameValuePair extends BasicNameValuePair {

		public SingleNameValuePair(String name, String value) {
			super(name, value);
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof NameValuePair) {
				String name = ((NameValuePair) object).getName();
				if (name != null && name.equals(getName()))
					return true;
			}
			return false;
		}
	}

	public static class HttpResp {
		public int mStatusCode;
		public Map<String, String> mHeaders = new HashMap<String, String>();
		public String mBodyContent;

		public HttpResp(HttpResponse response) {
			if (response == null)
				return;

			mStatusCode = response.getStatusLine().getStatusCode();

			Header[] headers = response.getAllHeaders();
			if (headers != null) {
				for (Header h : headers) {
					if (h == null || h.getName() == null
							|| h.getValue() == null)
						continue;
					if (mHeaders.containsKey(h.getName())
							&& "Set-Cookie".equals(h.getName())) {
						String newCookie = mHeaders.get(h.getName()) + " ,"
								+ h.getValue();
						mHeaders.put(h.getName(), newCookie);
					} else
						mHeaders.put(h.getName(), h.getValue());
				}
			}

			try {
				mBodyContent = parseHttpResponse(response);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public String getHeader(String name) {
			if (name == null || mHeaders == null)
				return null;
			return mHeaders.get(name);
		}

		@Override
		public String toString() {
			if (mBodyContent != null)
				return mBodyContent;
			return "";
		}
	}

	public static class ServerApi {
		protected String mHost;
		protected String mApi;
		protected Class<? extends JsonItem> mResponse;

		public ServerApi(String host, String api,
				Class<? extends JsonItem> response) {
			this(host, "", api, response);
		}

		public ServerApi(String host, String apiVersion, String api,
				Class<? extends JsonItem> response) {
			mHost = host;
			mApi = api;
			mResponse = response;
		}

		public String getHost() {
			return mHost;
		}

		public String getUrl() {
			return mHost + mApi;
		}

		public Class<? extends JsonItem> getResponse() {
			return mResponse;
		}
	}
}
