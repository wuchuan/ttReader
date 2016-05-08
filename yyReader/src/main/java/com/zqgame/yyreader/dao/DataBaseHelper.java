package com.zqgame.yyreader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "yyread.db";
	public static final String BOOK_LIST_TABLE = "booklist";
	public static final String MARK_LIST_TABLE = "marklist";
	public static final String SHELF_TABLE = "shelflist";
	public static final String BOOK_TABLE = "book";
	
	// private static final String MARK_LIST_TABLE="marklist";

	private static final String CREATE_BOOKLIST_TABLE = "create table if not exists"
			+ BOOK_LIST_TABLE
			+ "id integer primary key,"
			+ "path varchar(120),"
			+ "name varchar(60),"
			+ "coverPath varchar(120),"
			+ "readPosition integer,"
			+ "readLastTime timestamp);";
	private static final String CREATE_SHELF_TABLE = "create table if not exists"
			+ SHELF_TABLE
			+ "bookId integer,"
			+ "level integer,"
			+ "bookOrder integer," + "orderInFolder integer);";
	private static final String CREATE_MARK_TABLE = "create table if not exists"
			+ MARK_LIST_TABLE
			+ "bookId integer,"
			+ "chapterId integer,"
			+ "position integer,"
			+ "time timestamp"
			+ "about varchar(120)"
			+ ");";
	private static final String CREATE_BOOK_TABLE = "create table if not exists"
			+ BOOK_TABLE
			+ "id integer,"
			+ "name varchar(60),"
			+ "coverImg varchar(120),"
			+ "doctype varchar(120),"
			+ "readpos varchar(120),"
			+ "wordCount integer);";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOKLIST_TABLE);
		db.execSQL(CREATE_SHELF_TABLE);
		db.execSQL(CREATE_MARK_TABLE);
		db.execSQL(CREATE_BOOK_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {

	}
}
