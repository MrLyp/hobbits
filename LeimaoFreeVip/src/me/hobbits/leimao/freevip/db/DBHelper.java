package me.hobbits.leimao.freevip.db;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = "DBHelper";
	private static final int mVersion = 1;
	private static final String mDBName = "freevideo";
	private static List<TableInfo> mInfos = new ArrayList<TableInfo>();
	private static DBHelper mIns;

	static {
		registerTable(MessageHandler.TABLE_INFO);
		registerTable(ExchangeHandler.TABLE_INFO);
		registerTable(IncomeHandler.TABLE_INFO);
	}

	private static void registerTable(TableInfo info) {
		mInfos.add(info);
	}

	private DBHelper(Context context) {
		super(context, mDBName, null, mVersion);
	}

	public static class TableInfo {
		public String tableName;
		public String createSql;

		public TableInfo(String table, String sql) {
			tableName = table;
			createSql = sql;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof TableInfo) {
				return tableName != null
						&& tableName.equals(((TableInfo) o).tableName);
			} else
				return super.equals(o);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (TableInfo info : mInfos) {
			try {
				db.execSQL(info.createSql);
			} catch (SQLException e) {
				Log.w(TAG, "", e);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (TableInfo info : mInfos) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + info.tableName);
			} catch (SQLException e) {
				Log.w(TAG, "", e);
			}
		}
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	@Override
	public SQLiteDatabase getReadableDatabase() {
		try {
			return super.getReadableDatabase();
		} catch (Throwable e) {
			return null;
		}
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		try {
			return super.getWritableDatabase();
		} catch (Throwable e) {
			return null;
		}
	}

	@Override
	public synchronized void close() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static synchronized DBHelper getIns(Context context) {
		if (mIns == null) {
			mIns = new DBHelper(context.getApplicationContext());
			if (Build.VERSION.SDK_INT >= 11) {
				SQLiteDatabase db = mIns.getWritableDatabase();
				if (db != null)
					db.enableWriteAheadLogging();
			}
		}
		return mIns;
	}
}
