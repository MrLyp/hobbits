package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class Task implements JsonItem {

	private static final long serialVersionUID = -6189933734316772794L;
	private String name;
	private String show_name;
	private String img;
	private String detail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShow_name() {
		return show_name;
	}

	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
