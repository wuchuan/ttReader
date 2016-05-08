package com.zqgame.yyreader;

import com.umeng.socialize.PlatformConfig;

import android.app.Application;

public class ReaderApplication extends Application {

	@Override
	public void onCreate() {
		initConfig();
		super.onCreate();
	}

	 public void initConfig(){
		PlatformConfig.setWeixin("wx967daebe835fbeac",
				"5bb696d9ccd75a38c8a0bfe0675559b3");
		// 微信 appid appsecret

		PlatformConfig.setSinaWeibo("3921700954",
				"04b48b094faeb16683c32669824ebdad");
		// 新浪微博 appkey appsecret

		PlatformConfig.setQQZone("100424468",
				"c7394704798a158208a74ab60104f0ba");
		// QQ和Qzone appid appkey
	}
}
