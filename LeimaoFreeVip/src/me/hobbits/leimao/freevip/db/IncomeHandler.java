package me.hobbits.leimao.freevip.db;

import java.util.ArrayList;
import java.util.List;

import me.hobbits.leimao.freevip.db.DBHelper.TableInfo;
import me.hobbits.leimao.freevip.model.Income;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IncomeHandler extends BaseDBHandler{

	private static final String TAG = "IncomeHandler";
	private static final String TABLE_NAME = "income";
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
	
	public IncomeHandler(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}
	
	public void insertIncome(Income i) {
		try {
			ContentValues values = new ContentValues();
			values.put(_ID, i.getLog_id());
			values.put(_DATA, convertJsonToBase64(i));
			getDatabase().insertWithOnConflict(getTableName(), null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		} catch (Exception ex) {
		}
	}

	public List<Income> queryAllIncome() {
		String[] columns = new String[] { _ID, _DATA };
		String orderBy = _ID + " desc";
		Cursor c = getDatabase().query(TABLE_NAME, columns, null, null, null,
				null, orderBy);
		List<Income> res = new ArrayList<Income>();
		try {
			while (c.moveToNext()) {
				String data = c.getString(c.getColumnIndex(_DATA));
				res.add((Income) convertBase64ToJson(Income.class, data));
			}
		} finally {
			c.close();
		}
		return res;
	}

}
