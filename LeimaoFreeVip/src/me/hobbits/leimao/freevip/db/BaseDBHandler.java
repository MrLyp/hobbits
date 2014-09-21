package me.hobbits.leimao.freevip.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.gandalf.json.JsonItem;
import cn.gandalf.util.Base64;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public abstract class BaseDBHandler {
	private static final String TAG = "BaseDBHandler";
	protected Context mContext;

	public BaseDBHandler(Context context) {
		mContext = context.getApplicationContext();
	}

	protected abstract String getTableName();

	public void deleteAll() {
		try {
			SQLiteStatement statement = getDatabase().compileStatement(
					"delete from " + getTableName());
			statement.execute();
		} catch (Exception e) {
		}
	}

	public int getCount() {
		int res = 0;
		try {
			SQLiteStatement statement = getDatabase().compileStatement(
					"select count(*) from " + getTableName());
			res = (int) statement.simpleQueryForLong();
			statement.close();
		} catch (Exception e) {
		}
		return res;
	}

	protected SQLiteDatabase getDatabase() {
		DBHelper helper = DBHelper.getIns(mContext);
		return helper == null ? null : helper.getWritableDatabase();
	}

	protected String convertJsonToBase64(JsonItem object) {
		ByteArrayOutputStream toByte = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(toByte);
			oos.writeObject(object);
		} catch (Exception e) {

		}
		return Base64.encodeBase64String(toByte.toByteArray());
	}

	protected JsonItem convertBase64ToJson(Class<? extends JsonItem> resClass,
			String data) {
		if (data == null || resClass == null)
			return null;
		byte[] base64Bytes = Base64.decodeBase64(data);
		try {

			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			JsonItem map = (JsonItem) ois.readObject();
			return map;
		} catch (Exception e) {
			return null;
		}
	}
}
