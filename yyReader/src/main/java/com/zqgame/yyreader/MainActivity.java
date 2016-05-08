package com.zqgame.yyreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zqgame.yyreader.fragment.AboutFragment;
import com.zqgame.yyreader.fragment.MainFragment;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        // 设置标题
        setTitle("Attach");
        // 初始化滑动菜�?
        initSlidingMenu();
        getPhonePrama();
        
		
	}

    private void initSlidingMenu() {
        // 设置主界面视�?
        //setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
               .replace(R.id.content_frame, new MainFragment()).commit();
        
        Fragment leftMenuFragment = new AboutFragment();  
        setBehindContentView(R.layout.left_menu_frame);  
        getSupportFragmentManager().beginTransaction()  
                .replace(R.id.left_menu_frame_layout, leftMenuFragment).commit(); 
        
        
        SlidingMenu menu = getSlidingMenu();  
        menu.setMode(SlidingMenu.LEFT_RIGHT);  
        // 设置触摸屏幕的模式  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);  
        
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadowwidth);  
//        menu.setShadowDrawable(R.drawable.shadow);  
        // 设置滑动菜单视图的宽度  
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        // 设置渐入渐出效果的值  
        menu.setFadeDegree(0.35f);  
        // menu.setBehindScrollScale(1.0f);  
//        menu.setSecondaryShadowDrawable(R.drawable.shadow);  
        //设置右边（二级）侧滑菜单  
        menu.setSecondaryMenu(R.layout.right_menu_frame);  
        Fragment rightMenuFragment = new MenuRightFragment();  
        getSupportFragmentManager().beginTransaction()  
                .replace(R.id.right_menu_frame_layout, rightMenuFragment).commit();  
        

//        // 设置滑动菜单的属性�?
//        menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT_RIGHT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.slidingmenu_shadowwidth);
//       // menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        // 设置滑动菜单的视图界�?
//        menu.setMenu(R.layout.menu_frame);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.menu_frame, new AboutFragment()).commit();
    }
    
	private void getPhonePrama() {
		DisplayMetrics metric = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(metric); 
		Config.SCREEN_WIDTH = metric.widthPixels;   
		Config.SCREEN_HEIGHT = metric.heightPixels; 
	}
}
