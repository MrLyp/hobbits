package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class ExchangeList extends ArrayList<Exchange> implements
		JsonList<Exchange> {
	private static final long serialVersionUID = -8757272817287658997L;

	@Override
	public Class<Exchange> getGenericClass() {
		return Exchange.class;
	}

}
