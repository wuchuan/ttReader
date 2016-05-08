package com.zqgame.yyreader.util;

import android.util.Log;


public class LogUtil {
	private static final String TAG = "yyread";
	// 日志文件总开关
	public static Boolean DEBUG = true;

	public enum LOG_ENUM {
		INFO, WARN, ERROR
	}

	public static void log(String msg) {
		log(LOG_ENUM.INFO,TAG,msg);
	}
	public static void i(String tag,String msg) {
		log(LOG_ENUM.INFO,tag,msg);
	}
	public static void log(LOG_ENUM level,String msg){
		log(level,TAG,msg);
	}
	/**
	 * 根据log级别打印log信息
	 * 
	 * @param level
	 *            log级别
	 * @param msg
	 *            log信息
	 */
	public static void log(LOG_ENUM level, String tag, String msg) {
		if (DEBUG) {
			//android 默认log有字数限制，如果过多，则分段打印出来
		    if (msg == null) {
                return;
            }
			int maxLogSize=1000;
			int divide=msg.length()/maxLogSize;
			for(int i=0;i<=divide;i++){
				int start = i*maxLogSize;
				int end=(i+1)*maxLogSize;
				end=end>msg.length()?msg.length():end;
				String sectionLogMsg=msg.substring(start, end);
				switch (level) {
				case INFO:
					Log.i(tag, sectionLogMsg);
					break;
				case WARN:
					Log.w(tag, sectionLogMsg);
					break;
				case ERROR:
					Log.e(tag, sectionLogMsg);
					break;
				default:
					break;
				}
			}
			
		}
	}
}
