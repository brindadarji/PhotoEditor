package com.bosseditor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqLiteDb;

	private Context HCtx = null;

	private static final String DATABASE_NAME = "my_images";
	private static final int DATABASE_VERSION = 1;

	static final String my_images_table = "tbl_images";
	static final String my_shared_table = "tbl_shared";

	static final int new_images_Int = 0;
	static final int new_shared_Int=1;

	static String[][] tables = new String[][] { 
		{ "sr_no","images","e_date" },
		{"sr_no","img","s_date"}
	};

	private static final String TABLE_1_CREATE = "create table "+ my_images_table
			+" (sr_no integer primary key autoincrement,images String,e_date int);";

	private static final String TABLE_2_CREATE = "create table "+ my_shared_table
			+"(sr_no integer primary key autoincrement,img String,s_date int);";
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_1_CREATE);
			db.execSQL(TABLE_2_CREATE);
			}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + my_images_table);
			db.execSQL("DROP TABLE IF EXISTS " + my_shared_table);
			onCreate(db);
		}
	}

	/** Constructor */
	public AppDatabase(Context ctx) {
		HCtx = ctx;
	}

	public AppDatabase open() throws SQLException {
		dbHelper = new DatabaseHelper(HCtx);
		sqLiteDb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	
	public void cleanTable(int tableNo) {
		switch (tableNo) {
		case new_images_Int:
			sqLiteDb.delete(my_images_table, null, null);
			break;
		case new_shared_Int:
			sqLiteDb.delete(my_shared_table, null, null);
			break;
		default:
			break;
		}
	}

	
	public synchronized long createAlert(String DATABASE_TABLE, int tableNo,
			String[] values) {
		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(tables[tableNo][i + 1], values[i]);
		}
		return sqLiteDb.insert(DATABASE_TABLE, null, vals);
	}

	public synchronized boolean deleteAlert(String DATABASE_TABLE,String whereCause) {
		return sqLiteDb.delete(DATABASE_TABLE, whereCause, null) > 0;
	}

	public boolean deleteAlert(String DATABASE_TABLE, int tableNo, long rowId) {
		return sqLiteDb.delete(DATABASE_TABLE,tables[tableNo][0] + "=" + rowId, null) > 0;
	}

	public synchronized Cursor fetchAllAlerts(String DATABASE_TABLE, int tableNo) {
		try {
			return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], null, null,null, null, "sr_no");

		} catch (Exception e) {
			Log.d("yo", e.getMessage());
			return null;
		}
	}

	public synchronized Cursor fetchAlert(String DATABASE_TABLE, int tableNo,
			long rowId) throws SQLException {
		Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
				tables[tableNo][0] + "=" + rowId, null, null, null, null);
		if (ret != null) {
			ret.moveToFirst();
		}
		return ret;
	}

	public synchronized Cursor fetchAlertWhere(String DATABASE_TABLE,
			int tableNo, String where, String order) throws SQLException {
		Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where,
				null, null, null, order);
		if (ret != null) {
			ret.moveToFirst();
		}
		return ret;
	}

	public synchronized Cursor fetchAlertWhereLimit(String DATABASE_TABLE,
			int tableNo, String where, String order, String limit)
			throws SQLException {
		Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where,
				null, null, null, order, limit);
		if (ret != null) {
			ret.moveToFirst();
		}
		return ret;
	}

	public boolean updateAlert(String DATABASE_TABLE, int tableNo, long rowId,
			String[] values) {
		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(tables[tableNo][i + 1], values[i]);
		}
		return sqLiteDb.update(DATABASE_TABLE, vals, tables[tableNo][0] + "="
				+ rowId, null) > 0;
	}

	public boolean updateAlerts(String DATABASE_TABLE, int tableNo,
			ContentValues cv, int id) {
		return sqLiteDb.update(DATABASE_TABLE, cv, "sr_no=" + id, null) > 0;
	}

	public boolean updateAlertWhere(String DATABASE_TABLE, int tableNo,
			ContentValues cv, String where) {
		try {
			return sqLiteDb.update(DATABASE_TABLE, cv, where, null) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
