package com.zqgame.yyreader.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class FileUtil {
	/**
	 * 获取可用的文件目录，如果sd卡不可用，就使用应用下的文件
	 * @param context
	 * @return
	 */
	public static String getValidFilePath(Context context) {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		}
		return context.getFilesDir().getAbsolutePath();
	}
	/**
	 * 获取可用的文件目录，如果sd卡不可用，就使用应用下的缓存
	 * @param context
	 * @return
	 */
	public static String getValidCachePath(Context context) {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return context.getExternalCacheDir().getAbsolutePath();
		}
		return context.getCacheDir().getAbsolutePath();
	}

}
