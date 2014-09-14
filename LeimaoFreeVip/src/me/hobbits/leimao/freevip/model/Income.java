package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class Income implements JsonItem {
	private static final long serialVersionUID = -2994071109919855272L;
	private int log_id;
	private String info;
	private double amount;
	private double balance;
	private String time;

	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
