package com.zqgame.yyreader.http;

import com.zqgame.yyreader.util.LogUtil;

public class Url {
	// http://58.67.194.119:8093/reader-api /
	public static final String IP = "http://58.67.194.119";
	public static final int PORT = 8093;

	public final static String POST_BOOK_DETAIL = "/reader-api/book/detail";

	public final static String POST_BOOK_CATALOG = "/reader-api/book/catalog";

	public final static String POST_BOOK_CHAPTER = "/reader-api/book/chapter";
	
	public final static String POST_LOGIN = 		"/reader-api/user/login";
	
	public final static String POST_GET_SMS = 	    "/reader-api/user/getValidCode";
	
	public final static String POST_REGISTER =    "/reader-api/user/register";
	
	public final static String POST_LOGIN_CHANNEL =    "/reader-api/user/loginChannel";
	
	
	public final static String POST_RESETPWD =    "/reader-api/user/resetPwd";
	
	
	
	
	
	
	
	public static String getUri(String uriType) {
		return getUri(uriType, PORT);
	}
	public static String getUri(String uriType, int port) {
		LogUtil.log("getUri:" + IP + ":" + port + uriType);
		return IP + ":" + PORT + uriType;
	}
}
