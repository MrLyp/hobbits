package me.hobbits.leimao.freevip.task;

import me.hobbits.leimao.freevip.model.ExchangeList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import cn.gandalf.exception.ECode;
import cn.gandalf.task.BaseTask;

public class QueryExchangeRecordTask extends BaseTask {
	private ExchangeList mList;

	public QueryExchangeRecordTask(Context context) {
		super(context);
	}

	@Override
	protected Object doInBackground(Void... arg0) {
		try {
			mList = HttpManager.processApi(HttpManager.getExchangeParam());
			GlobalValue.getIns(mContext).updateExchanges(mList);
			return ECode.SUCCESS;
		} catch (Exception e) {

		}
		return ECode.FAIL;
	}

	public int getNewCount() {
		if (mList == null)
			return 0;
		return mList.size();
	}
}
