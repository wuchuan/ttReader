package com.zqgame.yyreader.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class PropertyUtil {
	public static String getImei(Context context) {
		String imei = ((TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE)).getDeviceId();
		if (StringUtil.isEmptyString(imei)) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			imei = wifiManager.getConnectionInfo().getMacAddress();
		}
		if (StringUtil.isEmptyString(imei)) {
			String sdcardPath = Environment.getExternalStorageDirectory()
					.toString();
			sdcardPath = sdcardPath + "/yyreader";
			String filePath = sdcardPath + "/.yyreaderimei.txt";

			File file = new File(sdcardPath);
			File filex = new File(filePath);
			if (!file.exists() || !filex.exists()) {
				imei = UUID.randomUUID().toString();
				saveImeiUUID(imei, sdcardPath, filePath);
			} else {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(filex));
					imei = br.readLine();
					br.close();
					if (imei == null || imei.equals("")) {
						imei = UUID.randomUUID().toString();
						saveImeiUUID(imei, sdcardPath, filePath);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return imei;
	}
	public static void saveImeiUUID(String uuid, String sdcardPath,
			String filePath) {
		File file = new File(sdcardPath);
		if (!file.exists()) {
			file.mkdir();
		}
		File filey = new File(filePath);
		if (!filey.exists()) {
			try {
				filey.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filey));
			bw.write(uuid);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

public static String getVersionName(Context ctx){
	try {
		PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
				ctx.getPackageName(), 0);
		String versionName = pInfo.versionName;
		String channel=getApplicationMetaValue(ctx, "id");
		if(StringUtil.isEmptyString(channel)){
			versionName=versionName+"."+channel;
		}
		return versionName;
	} catch (Exception e) {
		return "1.0";
	}
	
}

public static String getApplicationMetaValue(Context context,
		String metaName) {
	try {
		ApplicationInfo appInfo = context.getPackageManager()
				.getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
		// return appInfo.metaData.getString(metaName);
		return appInfo.metaData.get(metaName) + "";
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "";
}	


	
}
