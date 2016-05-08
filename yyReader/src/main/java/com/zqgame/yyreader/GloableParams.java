package com.zqgame.yyreader;

import android.view.WindowManager;

public class GloableParams {
	private static WindowManager.LayoutParams wmParams;
	public static WindowManager.LayoutParams getMywmParams(){
		if(wmParams instanceof WindowManager.LayoutParams){
		}else{
			wmParams=new WindowManager.LayoutParams();
		}
		return wmParams;
	}

}
