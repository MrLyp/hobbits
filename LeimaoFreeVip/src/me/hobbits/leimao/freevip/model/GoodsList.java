package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class GoodsList extends ArrayList<Goods> implements JsonList<Goods> {

	private static final long serialVersionUID = -2782923013367991122L;

	@Override
	public Class<Goods> getGenericClass() {
		return Goods.class;
	}

}
