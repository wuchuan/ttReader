package com.zqgame.yyreader.dao;

import com.zqgame.yyreader.entity.Book;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ReaderDao {
	private DataBaseHelper mDataBaseHelper;

	public ReaderDao(Context context) {
		mDataBaseHelper = new DataBaseHelper(context);
	}

	public void addBook(Book mBook) {
		SQLiteDatabase db = null;
		try {
			db = mDataBaseHelper.getWritableDatabase();
			if (db.isOpen()) {
				db.beginTransaction();
				ContentValues values=new ContentValues();
				values.put("id", mBook.getId());
				values.put("name", mBook.getName());
				values.put("coverImg", mBook.getCoverImg());
				values.put("doctype", mBook.getDoctype());
				values.put("wordCount", mBook.getWordCount());
				values.put("bookDir", mBook.getBookDir());
				values.put("readpos", mBook.getReadPos());
				db.insert(DataBaseHelper.BOOK_TABLE, null, values);
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
			mDataBaseHelper.close();
		}

	}

	public void removeBook() {
		SQLiteDatabase db = null;
		try {
			db = mDataBaseHelper.getWritableDatabase();
			if (db.isOpen()) {
				db.beginTransaction();

				
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();

		}
	}

	public Book getBookByid() {

		SQLiteDatabase db = null;
		try {
			db = mDataBaseHelper.getReadableDatabase();
			if (db.isOpen()) {
				db.beginTransaction();

				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
			mDataBaseHelper.close();
		}
		return null;

	}

}
