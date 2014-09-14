package me.hobbits.leimao.freevip.model;

import java.util.ArrayList;

import cn.gandalf.json.JsonList;

public class MessageList extends ArrayList<Message> implements
		JsonList<Message> {

	private static final long serialVersionUID = -913700776682896608L;

	@Override
	public Class<Message> getGenericClass() {
		return Message.class;
	}

}
