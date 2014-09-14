package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class IncomeList extends ArrayList<Income> implements JsonList<Income> {

	private static final long serialVersionUID = 7306045617919559474L;

	@Override
	public Class<Income> getGenericClass() {
		return Income.class;
	}

}
