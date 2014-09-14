package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class BannerList extends ArrayList<Banner> implements JsonList<Banner> {

	private static final long serialVersionUID = -5040468415419144366L;

	@Override
	public Class<Banner> getGenericClass() {
		return Banner.class;
	}

}
