package com.zqgame.yyreader.util;

import com.zqgame.yyreader.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;

public class ReaderUtil {
	public static float getFontHeight(int size){
		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
		mTestPaint.setTextAlign(Align.LEFT);// 左对其
		mTestPaint.setTextSize(size);// 字体大小
		FontMetrics fm = mTestPaint.getFontMetrics();  
		return (float) Math.ceil(fm.descent - fm.ascent);
//		return (float) Math.ceil(fm.descent - fm.top);
//		return (float) Math.ceil(fm.descent - fm.top)+4;
	}
	
	public static int getLineNumByNomal(){
		return (int) ((Config.SCREEN_HEIGHT-Config.MARGIN_HEIGHT*2)/(getFontHeight(Config.CONTENT_SIZE)*1.6));
		
	}
	
	public static void saveSetBoolean(Context context,String param,Boolean Value){
		SharedPreferences sp = context.getSharedPreferences(Config.SP_SET, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(param, Value);
		edit.commit();
	}
	
	public static boolean getSetBoolean(Context context,String param,Boolean defultValue){
		SharedPreferences sp = context.getSharedPreferences(Config.SP_SET, Context.MODE_PRIVATE);
		return sp.getBoolean(param, defultValue);
	}
	public static void saveSetInt(Context context,String param,int Value){
		SharedPreferences sp = context.getSharedPreferences(Config.SP_SET, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(param, Value);
		edit.commit();
	}
	
	public static int getSetInt(Context context,String param,int defultValue){
		SharedPreferences sp = context.getSharedPreferences(Config.SP_SET, Context.MODE_PRIVATE);
		return sp.getInt(param, defultValue);
	}
}
