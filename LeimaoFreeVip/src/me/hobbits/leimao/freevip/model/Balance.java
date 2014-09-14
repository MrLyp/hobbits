package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class Balance implements JsonItem {
	private static final long serialVersionUID = -3542178114035955750L;
	private int user_id;
	private double balance;
	private int today_gold;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getToday_gold() {
		return today_gold;
	}

	public void setToday_gold(int today_gold) {
		this.today_gold = today_gold;
	}

}
