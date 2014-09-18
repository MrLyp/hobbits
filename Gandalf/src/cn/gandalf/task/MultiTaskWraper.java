package cn.gandalf.task;

import java.util.HashMap;
import java.util.Map;

import cn.gandalf.exception.ECode;

import android.app.Dialog;
import android.content.Context;

public class MultiTaskWraper extends HandlerMessageTask {
	private Map<HandlerMessageTask, TaskStatus> mTasks;
	private Map<HandlerMessageTask, Object> mErrors;
	private static final long MAX_WAIT_TIME = 20 * 1000;
	private static final long SLEEP_INTERVAL = 200;

	public MultiTaskWraper(Context context) {
		super(context);
		mTasks = new HashMap<HandlerMessageTask, TaskStatus>();
		mErrors = new HashMap<HandlerMessageTask, Object>();
	}

	public void addTask(HandlerMessageTask task) {
		mTasks.put(task, TaskStatus.PENDING);
		task.setShowCodeMsg(false);
		task.setShowProgessDialog(false);
	}

	@Override
	protected Object doInBackground(Void... params) {
		for (HandlerMessageTask task : mTasks.keySet()) {
			final HandlerMessageTask hTask = task;
			task.wrapCallback(new Callback() {

				@Override
				public void onSuccess(HandlerMessageTask task, Object t) {
					mTasks.put(task, TaskStatus.SUCCEED);
				}

				@Override
				public void onFail(HandlerMessageTask task, Object t) {
					mTasks.put(task, TaskStatus.FAILED);
					mErrors.put(task, t);
				}
			});
			task.setOnCancelListener(new OnTaskCancelListener() {
				@Override
				public void onCancel(BaseTask task) {
					mTasks.put((HandlerMessageTask) task, TaskStatus.CANCELED);
				}
			});
			hTask.setShowProgessDialog(false);
			hTask.setShowCodeMsg(false);
		}

		for (HandlerMessageTask task : mTasks.keySet()) {
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
			for (HandlerMessageTask task : mErrors.keySet()) {
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
