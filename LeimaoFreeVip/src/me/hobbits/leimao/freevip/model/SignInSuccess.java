package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class SignInSuccess implements JsonItem {
	private static final long serialVersionUID = 9026603998979272051L;
	private String user_id;
	private String pwd;
	private int result;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
