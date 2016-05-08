/*Copyright(c)2012,中青宝SNS社交游戏事业部
 *Allrights reserved
 *
 *文件名称：QinNiAsyncTask.java
 *摘要：亲昵异步任务基类
 *
 *当前版本：1.0
 *作者：陆俊（2399）
 *完成日期：2012年11月30日
 */
package com.zqgame.yyreader.http;

import org.apache.http.conn.ConnectTimeoutException;

import android.os.AsyncTask;

import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.LogUtil.LOG_ENUM;
import com.zqgame.yyreader.util.StringUtil;

public class ReadAsyncTask extends AsyncTask<String, Integer, String> {

	public static enum RESULT_TYPE {
		BEAN, MAP, LIST, STRING
	};
	// 请求的参数类型，用户在同一界面中几种请求时的区分
//	private int requestFlag;
	// HTTP请求方式
	private RequestMethod requestMothod;
	// 数据回调接口
	private ReadAsyncTaskCallBack callback;
	/** 标记字段 */
	private String flagStr = "";
	private String url;
	private Object parameters = null;

	// private String stringParameters;
	public ReadAsyncTask(RequestMethod requestMothod,
			ReadAsyncTaskCallBack cb, String flagStr) {
//		this.requestFlag = requestFlag;
		this.requestMothod = requestMothod;
		this.flagStr = flagStr;
		callback = cb;
	}

	/**
	 * 构造函数
	 * 
	 * @param requestFlag
	 *            请求的参数类型，用户在同一界面中几种请求时的区分
	 * @param requestMothod
	 *            HTTP请求方式
	 * @param cb
	 *            数据回调接口
	 */
	public ReadAsyncTask(RequestMethod requestMothod,
			ReadAsyncTaskCallBack cb) {
		this.requestMothod = requestMothod;
		callback = cb;
	}

	public ReadAsyncTask(RequestMethod requestMothod,
			Object parameters, ReadAsyncTaskCallBack cb) {
		this.requestMothod = requestMothod;
		this.parameters = parameters;
		callback = cb;
	}

	// public MbmAsyncTask(int requestFlag, RequestMethod requestMothod,String
	// stringParameters,
	// MbmAsyncTaskCb cb) {
	// this.requestFlag = requestFlag;
	// this.requestMothod = requestMothod;
	// this.stringParameters=stringParameters;
	// callback = cb;
	// }

	@Override
	protected String doInBackground(String... params) {
		// 返回结果
		String result = null;
		// 参数检查
		url = StringUtil.removeAllSpace(params[0]);
		// String url=params[0].trim();
		// url=URLEncoder.encode(url);
		if (StringUtil.isEmptyString(url)) {
			LogUtil.log(LOG_ENUM.ERROR, "请求失败因为URL不合法");
			return result;
		}
		// 正在登录时不能发送非登录请求,避免请求超时后自动登录造成重复登录
		// if (LoginTask.isLogining() && !url.contains("/mbm/v2/user/login") &&
		// !url.contains("/mbm/conf/activate/v1")) {
		// LogUtil.log(LOG_ENUM.ERROR, "请求" + url + "失败因为正在登陆");
		// return result;
		// }
		try {
			if (requestMothod == RequestMethod.GET) {
				result = RestfulHelper.get(url);
			} else {
				if (parameters != null) {
					result = RestfulHelper.post(url, parameters);
				} else {
					result = RestfulHelper.post(url);
				}
			}
		} catch (Exception e) {
			if (e != null && e.getCause() instanceof ConnectTimeoutException) {
				// 当连接超时，赋值“”，为了不提示网络问题
				LogUtil.log(LOG_ENUM.INFO, "请求" + url + "超时异常");
				result = "";
			}
		}
		LogUtil.log(LOG_ENUM.INFO, "请求" + url + "得到数据:" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// 回调处理
		if (result == null) {
			if (null != callback) {
				callback.handleNetWorkData(result, flagStr);
			}
		} else {

			if (null != callback) {
				callback.handleNetWorkData(result, flagStr);
			}
		}
	}

	@Override
	protected void onPreExecute() {
		if (null != callback) {
			callback.preRequest();
		}
	}
}