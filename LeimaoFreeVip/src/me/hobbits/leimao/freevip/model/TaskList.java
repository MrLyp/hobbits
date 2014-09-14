package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class TaskList extends ArrayList<Task> implements JsonList<Task>{

	private static final long serialVersionUID = 9155487020958625640L;

	@Override
	public Class<Task> getGenericClass() {
		return Task.class;
	}

}
