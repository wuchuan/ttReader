package com.zqgame.yyreader.http;

public interface ReadAsyncTaskCallBack {
	/**
	 * 网络请求前的操作，运行在主线程中
	 */
	public void preRequest();

	/**
	 * 网络返回后的操作，运行在主线程中,如果此时getActivity()为空，则不能执行后续操作
	 * 
	 * @param result
	 *            结果json数据
	 * @param requestFlag
	 *            对应的请求区分类型
	 * @param flagStr
	 *            标记字段
	 */
	public void handleNetWorkData(String result,String flagStr);
}
