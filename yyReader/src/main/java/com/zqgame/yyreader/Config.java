package com.zqgame.yyreader;

import java.util.HashMap;
import java.util.List;

import com.zqgame.yyreader.entity.Chapter;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Config {
	public  static int CONTENT_SIZE=20;
	public static int TITLE_SIZE=30;
	public static int  CONTENT_COLOR = Color.rgb(28, 28, 28);
	public  static int SCREEN_HEIGHT=1280;
	public static int SCREEN_WIDTH=720;
	public static int MARGIN_HEIGHT=50;
	public static int MARGIN_WIDTH=20;
	
	public static double BLANK_SCALE=0.6;
	
	public static int  NOMAL_LINES=0;
	
	public static int BG_COLOR = 0xffF1E2CD; // 背景颜色
	
	public static int FT_COLOR = 0xff000000; // 字体颜色
	
	public static Bitmap BITMAP_BG = null; // 背景图片
	public static TouchEnum TouchMode = TouchEnum.REAL; 
    public static enum TouchEnum {
        REAL, LR, TB, NONE;
    }
    public static String SP_SET="setSharedPreferences";
	public static HashMap<String, List<Chapter>> dirMap=new HashMap<>();
	
}
