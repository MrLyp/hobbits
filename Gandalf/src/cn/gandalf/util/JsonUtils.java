package cn.gandalf.util;

import java.util.List;

import org.json.JSONArray;

import cn.gandalf.json.JsonList;
import cn.gandalf.json.JsonItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {
	public static <T extends Object> T parseJSONToObject(Class<T> tclass,
			String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, tclass);
	}

	public static <T extends JsonList> T parseJSONToObject(Class<T> tlass,
			JSONArray jsArray) {
		Gson gson = new Gson();
		return gson.fromJson(gson.toJson(tlass), new TypeToken<List<T>>() {
		}.getType());
	}
}
