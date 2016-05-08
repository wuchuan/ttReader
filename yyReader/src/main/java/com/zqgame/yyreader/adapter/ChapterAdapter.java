package com.zqgame.yyreader.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zqgame.yyreader.R;
import com.zqgame.yyreader.callback.CatalogueItemCallBack;
import com.zqgame.yyreader.entity.Chapter;

public class ChapterAdapter extends BaseAdapter {
	
	private List<Chapter> mCharacterList;
	private Context ctx;
	private LayoutInflater li;
	private CatalogueItemCallBack callBack;

	public ChapterAdapter(Context ctx,List<Chapter> l,CatalogueItemCallBack callBack){
		
		this.ctx=ctx;
		mCharacterList=l;
		this.callBack=callBack;
		li=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	@Override
	public int getCount() {
		return mCharacterList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView catalogue_textview = null;
		if(convertView==null){
			convertView=li.inflate(R.layout.catalogue_item, null);
		}
		catalogue_textview = (TextView) convertView.findViewById(R.id.catalogue_textview);
//		if(mCharacterList.get(position).getBookId()!=-1){
			catalogue_textview.setText(mCharacterList.get(position).getName());
//		}
//		catalogue_textview.setText(mCharacterList.get(position).getTitle());
			convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.jumpToCatalogue(position);
			}
		});
		return convertView;
	}

}
