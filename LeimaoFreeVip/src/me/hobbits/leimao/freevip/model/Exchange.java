package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class Exchange implements JsonItem {
	private static final long serialVersionUID = 4614218324080592402L;
	private int trade_id;
	private String name;
	private String status;
	private String time;
	private String card_no;
	private String card_pwd;
	private String card_expire_time;

	public int getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
