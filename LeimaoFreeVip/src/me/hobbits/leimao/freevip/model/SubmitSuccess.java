package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class SubmitSuccess implements JsonItem {
	private static final long serialVersionUID = -2340145218009766793L;
	private int result;
	private String card_no;
	private String card_pwd;
	private String card_expire_time;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_pwd() {
		return card_pwd;
	}

	public void setCard_pwd(String card_pwd) {
		this.card_pwd = card_pwd;
	}

	public String getCard_expire_time() {
		return card_expire_time;
	}

	public void setCard_expire_time(String card_expire_time) {
		this.card_expire_time = card_expire_time;
	}

}
