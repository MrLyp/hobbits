package me.hobbits.leimao.freevip.db;

import java.util.ArrayList;
import java.util.List;

import me.hobbits.leimao.freevip.db.DBHelper.TableInfo;
import me.hobbits.leimao.freevip.model.Exchange;
import me.hobbits.leimao.freevip.model.Message;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageHandler extends BaseDBHandler {

	private static final String TAG = "MessageHandler";
	private static final String TABLE_NAME = "exchange";
	private static final String _ID = "_id";
	private static final String _DATA = "data";
	public static final String CREATE_SQL = "CREATE TABLE "
			+ TABLE_NAME
			+ " ("
			+ _ID
			+ " INTEGER PRIMARY KEY, "
			+ _DATA
			+ " TEXT "
			+ ")                                                                     ";

	public static TableInfo TABLE_INFO = new TableInfo(TABLE_NAME, CREATE_SQL);

	public MessageHandler(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	public void insertMessage(Message m) {
		try {
			ContentValues values = new ContentValues();
			values.put(_ID, m.getMsg_id());
			values.put(_DATA, convertJsonToBase64(m));
			getDatabase().insertWithOnConflict(getTableName(), null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		} catch (Exception ex) {
		}
	}

	public List<Message> queryAllExchange() {
		String[] columns = new String[] { _ID, _DATA };
		String orderBy = _ID + " desc";
		Cursor c = getDatabase().query(TABLE_NAME, columns, null, null, null,
				null, orderBy);
		List<Message> res = new ArrayList<Message>();
		try {
			while (c.moveToNext()) {
				String data = c.getString(c.getColumnIndex(_DATA));
				res.add((Message) convertBase64ToJson(Message.class, data));
			}
		} finally {
			c.close();
		}
		return res;
	}

}
