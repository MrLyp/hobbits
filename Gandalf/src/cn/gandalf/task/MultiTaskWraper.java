package cn.gandalf.task;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import cn.gandalf.exception.ECode;

public class MultiTaskWraper extends BaseTask {
	private Map<BaseTask, TaskStatus> mTasks;
	private Map<BaseTask, Object> mErrors;
	private static final long MAX_WAIT_TIME = 20 * 1000;
	private static final long SLEEP_INTERVAL = 200;

	public MultiTaskWraper(Context context) {
		super(context);
		mTasks = new HashMap<BaseTask, TaskStatus>();
		mErrors = new HashMap<BaseTask, Object>();
	}

	public void addTask(BaseTask task) {
		mTasks.put(task, TaskStatus.PENDING);
		task.setShowCodeMsg(false);
		task.setShowProgessDialog(false);
	}

	@Override
	protected Object doInBackground(Void... params) {
		for (BaseTask task : mTasks.keySet()) {
			final BaseTask hTask = task;
			task.wrapCallback(new Callback() {

				@Override
				public void onSuccess(BaseTask task, Object t) {
					mTasks.put(task, TaskStatus.SUCCEED);
				}

				@Override
				public void onFail(BaseTask task, Object t) {
					mTasks.put(task, TaskStatus.FAILED);
					mErrors.put(task, t);
				}
			});
			hTask.setShowProgessDialog(false);
			hTask.setShowCodeMsg(false);
		}

		for (BaseTask task : mTasks.keySet()) {
			task.execute();
		}
		for (int i = 0; i < MAX_WAIT_TIME / SLEEP_INTERVAL; i++) {
			try {
				if (isAllFinished()) {
					break;
				}
				Thread.sleep(SLEEP_INTERVAL);
			} catch (InterruptedException e) {
			}
		}
		return isAllSucceed() ? ECode.SUCCESS : ECode.FAIL;
	}

	@Override
	protected void processResult(Object result) {
		if (result.equals(ECode.SUCCESS)) {
			super.processResult(result);
		} else {
			// If a sub task return a special error, try to show its special error message.
			String specialError = null;
			for (BaseTask task : mErrors.keySet()) {
				Object error = mErrors.get(task);
				if (error != null && !error.equals(ECode.FAIL)
						&& error instanceof Integer) {
					specialError = task.getCodeMsg((Integer) error);
				}
			}
			if (specialError != null) {
				showResultMessage(specialError);
			} else {
				super.processResult(result);
			}
		}
	}

	private boolean isAllFinished() {
		for (TaskStatus status : mTasks.values()) {
			if (status == TaskStatus.PENDING) {
				return false;
			}
		}
		return true;
	}

	private boolean isAllSucceed() {
		for (TaskStatus status : mTasks.values()) {
			if (status != TaskStatus.SUCCEED) {
				return false;
			}
		}
		return true;
	}

	public enum TaskStatus {
		PENDING, SUCCEED, FAILED, CANCELED;
	}
}
