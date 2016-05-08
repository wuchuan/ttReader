package com.zqgame.yyreader.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CatalogueViewPager extends ViewPager {
	
	private View mView;
	private boolean mEnableSlip=false;
	
	public CatalogueViewPager(Context context) {
		super(context);
		InitView(context);
	}
	public CatalogueViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView(context);
	}
	private void InitView(Context context) {
		
		mView=new View(context);
		
	}
	
    public void setEnableSlip(boolean enable){
    	mEnableSlip = enable;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableSlip) {
            return mView.onTouchEvent(event);
        }
        return false;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnableSlip) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
    
   
}
