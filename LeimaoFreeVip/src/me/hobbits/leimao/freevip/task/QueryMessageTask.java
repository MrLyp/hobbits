package me.hobbits.leimao.freevip.task;

import me.hobbits.leimao.freevip.model.MessageList;
import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.GlobalValue;
import android.content.Context;
import cn.gandalf.exception.ECode;
import cn.gandalf.task.BaseTask;

public class QueryMessageTask extends BaseTask {

	private MessageList mList;
	private int newCount;

	public QueryMessageTask(Context context) {
		super(context);
	}

	@Override
	protected Object doInBackground(Void... arg0) {
		try {
			mList = HttpManager.processApi(HttpManager.getMsgParam());
			newCount = GlobalValue.getIns(mContext).updateMessages(mList);
			return ECode.SUCCESS;
		} catch (Exception e) {

		}
		return ECode.FAIL;
	}

	public int getNewCount() {
		return newCount;
	}

}
