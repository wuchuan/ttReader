package com.zqgame.yyreader.fragment;

import java.util.List;

import com.zqgame.yyreader.R;
import com.zqgame.yyreader.adapter.ChapterAdapter;
import com.zqgame.yyreader.callback.CatalogueItemCallBack;
import com.zqgame.yyreader.entity.Maker;
import com.zqgame.yyreader.list.PullToRefreshBase;
import com.zqgame.yyreader.list.PullToRefreshListView;
import com.zqgame.yyreader.list.PullToRefreshBase.Mode;
import com.zqgame.yyreader.list.PullToRefreshBase.OnRefreshListener2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Maker extends Fragment {

	private PullToRefreshListView mine_marker_list;
	private List<Maker> makerList;
	private CatalogueItemCallBack mCatalogueItemCallBack;

	public Fragment_Maker(List<Maker> makerList,
			CatalogueItemCallBack mCatalogueItemCallBack) {
		this.makerList=makerList;
		this.mCatalogueItemCallBack=mCatalogueItemCallBack;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.catalogue_list_layout, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		
		mine_marker_list=(PullToRefreshListView)view.findViewById(R.id.mine_catalogue_list);
		mine_marker_list.setMode(Mode.PULL_FROM_END);
		mine_marker_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				
			}
		});
		//ChapterAdapter mChapterAdapter =new ChapterAdapter(getActivity(), makerList,mCatalogueItemCallBack);
		//mine_marker_list.setAdapter(mChapterAdapter);
		
	}
	
	
	

}
