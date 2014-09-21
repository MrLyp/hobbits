package me.hobbits.leimao.freevip.task;

import me.hobbits.leimao.freevip.model.IncomeList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import cn.gandalf.exception.ECode;
import cn.gandalf.task.BaseTask;

public class QueryIncomeTask extends BaseTask {

	private Context mContext;

	public QueryIncomeTask(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected Object doInBackground(Void... arg0) {
		try {
			IncomeList list = HttpManager.processApi(HttpManager
					.getIncomeParam());
			GlobalValue.getIns(mContext).updateIncomes(list);
			return ECode.SUCCESS;
		} catch (Exception e) {

		}
		return ECode.FAIL;
	}

}
