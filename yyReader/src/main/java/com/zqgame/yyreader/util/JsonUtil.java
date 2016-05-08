package com.zqgame.yyreader.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zqgame.yyreader.util.LogUtil.LOG_ENUM;

public class JsonUtil {

	public static int getCode(String jsonStr) {
		int result = -1;

		Object obj = getObject(jsonStr, "code");
		if (obj instanceof Integer) {
			result = (Integer) obj;
		}
		return result;
	}

	public static Object getObject(String jsonStr, String key) {
		Object resultObject = null;
		if (!StringUtil.isEmptyString(jsonStr)
				&& !StringUtil.isEmptyString(key)) {
			JSONObject json = null;
			try {
				json = JSON.parseObject(jsonStr);
			} catch (Exception e) {
				LogUtil.log(LOG_ENUM.ERROR, "不能转化为json对象：" + jsonStr + ",因为："
						+ e.getMessage());
			}
			if (null != json && json.containsKey(key)) {
				resultObject = json.get(key);
			}
		}
		return resultObject;
	}
	
	public static Object json2bean(String jsonStr, Class<?> clazz) {
		Object resultObject = null;
		if (!StringUtil.isEmptyString(jsonStr) && null != clazz) {
			try {
				resultObject = JSON.parseObject(jsonStr, clazz);
			} catch (Exception e) {
				LogUtil.log(LOG_ENUM.ERROR, "不能转化为bean对象：" + jsonStr + ",因为："
						+ e.getMessage());
			}
		}
		return resultObject;
	}
	
	/**
	 * 把一个json数组串转换成list
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @param clazz
	 *            list中存放的对象
	 * @return list集合
	 */
	public static List<?> json2List(String jsonStr, Class<?> clazz) {
		List<?> list = null;

		if (!StringUtil.isEmptyString(jsonStr) && null != clazz) {
			try {
				list = JSON.parseArray(jsonStr, clazz);
				
			} catch (Exception e) {
				LogUtil.log(LOG_ENUM.ERROR, "不能转化为json数组：" + ",因为："
						+ e.getMessage());
			}
		}
		return list;
	}
	
}
