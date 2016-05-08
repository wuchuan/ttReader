package com.zqgame.yyreader.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zqgame.yyreader.callback.CatalogueItemCallBack;
import com.zqgame.yyreader.entity.Chapter;
import com.zqgame.yyreader.entity.Maker;
import com.zqgame.yyreader.fragment.Fragment_Catalogue;
import com.zqgame.yyreader.fragment.Fragment_Maker;

public class CatalogueAdapter extends FragmentStatePagerAdapter {
	private List<Chapter> chapterList;
	private List<Maker> makerList;
	private CatalogueItemCallBack mCatalogueItemCallBack;

//	public CatalogueAdapter(FragmentManager fragmentManager,String TargetUserId) {
//		super(fragmentManager);
//		this.TargetUserId=TargetUserId;
//	
//	}
	public CatalogueAdapter(FragmentManager supportFragmentManager,
			List<Chapter> chapterList, List<Maker> makerList,
			CatalogueItemCallBack mCatalogueItemCallBack) {
		super(supportFragmentManager);
		this.chapterList=chapterList;
		this.makerList=makerList;
		this.mCatalogueItemCallBack=mCatalogueItemCallBack;
		
		
	}
	@Override
	public int getCount() {
		return 2;
	}
	@Override
	public Fragment getItem(int position) {
		Fragment mFragment=null;
		switch(position){
		case 0:
			mFragment=new Fragment_Catalogue(chapterList,mCatalogueItemCallBack);
			break;
		case 1:
			mFragment=new Fragment_Maker(makerList,mCatalogueItemCallBack);
			break;
		}
		return mFragment;
	}

}
