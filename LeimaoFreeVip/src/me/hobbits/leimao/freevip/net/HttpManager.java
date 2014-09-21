package me.hobbits.leimao.freevip.net;

import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.BannerList;
import me.hobbits.leimao.freevip.model.BasicConfig;
import me.hobbits.leimao.freevip.model.ErrorResp;
import me.hobbits.leimao.freevip.model.ExchangeList;
import me.hobbits.leimao.freevip.model.GoodsList;
import me.hobbits.leimao.freevip.model.IncomeList;
import me.hobbits.leimao.freevip.model.MessageList;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.model.SubmitSuccess;
import me.hobbits.leimao.freevip.model.TaskList;
import me.hobbits.leimao.freevip.util.GlobalValue;

import org.apache.http.NameValuePair;

import android.content.Context;
import cn.gandalf.exception.CustomException;
import cn.gandalf.json.JsonItem;
import cn.gandalf.net.BaseHttpManager;
import cn.gandalf.util.CodecUtils;
import cn.gandalf.util.SystemUtils;
import cn.gandalf.util.TimeUtils;

public class HttpManager extends BaseHttpManager {

	public static final String HOST_URL = "http://tcvideo.bitclock.cn/";
	private static final String SECRET_KEY = "5A8EAEE1249C26CF85563C364338BA35";

	private static RequestParam addSign(RequestParam param) {
		StringBuffer sb = new StringBuffer();
		for (NameValuePair p : param) {
			sb.append(p.getName() + "=" + p.getValue() + "&");
		}
		String res = sb.toString();
		if (res.length() > 0)
			res = res.substring(0, res.length() - 1);
		param.addHeader("AUTHORIZATION", CodecUtils.md5Hex(res + SECRET_KEY));
		return param;
	}

	public static CRequestParam getBasicConfigParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Config/basicconfig",
				BasicConfig.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getGoodsParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Config/goods",
				GoodsList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getBannerParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Config/banner",
				BannerList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getTaskParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Config/task",
				TaskList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getMsgParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Config/msg",
				MessageList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getBalanceParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/User/balance",
				Balance.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getSignInParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/User/signin",
				SignInSuccess.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getIncomeParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/User/income",
				IncomeList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getExchangeParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/User/exchange",
				ExchangeList.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static CRequestParam getSubmitParam(int id) {
		ServerApi api = new ServerApi(HOST_URL, "index.php/Trade/submit",
				SubmitSuccess.class);
		CRequestParam param = new CRequestParam(api);
		param.addNameValuePair("goods_id", id);
		addSign(param);
		return param;
	}

	public static CRequestParam getShareParam() {
		ServerApi api = new ServerApi(HOST_URL, "index.php/User/share",
				ErrorResp.class);
		CRequestParam param = new CRequestParam(api);
		addSign(param);
		return param;
	}

	public static class CRequestParam extends RequestParam {

		private static final long serialVersionUID = -3993974256810246101L;
		private Context mContext;

		public CRequestParam(ServerApi api) {
			this(BaseHttpManager.mContext, api);
			mContext = BaseHttpManager.mContext;
		}

		public CRequestParam(Context context, ServerApi api) {
			super(api);
			mContext = context.getApplicationContext();
			setHttpMethod(METHOD_POST);
			addNameValuePair("imei", SystemUtils.getIMEI(mContext));
			addNameValuePair("imsi", SystemUtils.getIMSI(mContext));
			addNameValuePair("mac_addr", SystemUtils.getMacAddress(mContext));
			addNameValuePair("platform", "Android");
			addNameValuePair("stamp",
					TimeUtils.getTime(System.currentTimeMillis()));
			addNameValuePair("version", SystemUtils.getVersionCode(mContext));
			addNameValuePair("system_version", SystemUtils.getSystemVersion());
			addNameValuePair("app_name", mContext.getPackageName());
			addNameValuePair("platform_model", android.os.Build.MODEL);
			setUsrInfo();
		}

		public void setUsrInfo() {
			String userId = null;
			String pwd = null;
			SignInSuccess info = GlobalValue.getIns(mContext).getUserInfo();
			if (info != null) {
				userId = info.getUser_id();
				pwd = info.getPwd();
			}
			addNameValuePair("user_id", userId);
			addNameValuePair("pwd", pwd);
		}
	}

}
