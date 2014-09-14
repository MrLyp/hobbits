package cn.gandalf.json;

import cn.gandalf.json.JsonItem;

public class ErrorResp implements JsonItem {
	private static final long serialVersionUID = 1833495435106030849L;
	private int result;
	private String error_message;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

}
