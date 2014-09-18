package me.hobbits.leimao.freevip.task;

import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.BannerList;
import me.hobbits.leimao.freevip.model.GoodsList;
import me.hobbits.leimao.freevip.model.MessageList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import cn.gandalf.exception.ECode;
import cn.gandalf.task.HandlerMessageTask;

public class QueryMainPageInfoTask extends HandlerMessageTask {

	private Context mContext;
	private GlobalValue mDataManager;

	public QueryMainPageInfoTask(Context context) {
		super(context);
		mContext = context;
		mDataManager = GlobalValue.getIns(mContext);
	}

	@Override
	protected Object doInBackground(Void... arg0) {
		try {
			GoodsList goods = HttpManager.processApi(HttpManager
					.getGoodsParam());
			BannerList banners = HttpManager.processApi(HttpManager
					.getBannerParam());
			Balance balance = HttpManager.processApi(HttpManager
					.getBalanceParam());
			MessageList messageList = HttpManager.processApi(HttpManager
					.getMsgParam());
			if (goods != null && banners != null && balance != null
					&& messageList != null) {
				mDataManager.updateBalance(balance);
				mDataManager.updateBanners(banners);
				mDataManager.updateGoodsList(goods);
				return ECode.SUCCESS;
			} else
				return ECode.FAIL;
		} catch (Exception e) {
		}
		return ECode.FAIL;
	}

}
