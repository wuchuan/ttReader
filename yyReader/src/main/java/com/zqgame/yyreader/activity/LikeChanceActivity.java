package com.zqgame.yyreader.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zqgame.yyreader.Config;
import com.zqgame.yyreader.R;
import com.zqgame.yyreader.widget.CircleImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class LikeChanceActivity extends Activity implements OnClickListener {

	private RelativeLayout like_one;
	private RelativeLayout like_two;
	private RelativeLayout like_tree;
	private RelativeLayout like_four;
	private RelativeLayout like_five;
	private RelativeLayout like_six;
	private RelativeLayout like_seven;
//	private int[] wArray=new int[7];
//	private int[] hArray=new int[7];
	private int[] colorArray=new int[]
			{R.color.black,R.color.blue,R.color.gray,R.color.green,
			R.color.red,R.color.yellow,R.color.cyan,R.color.darkgray,
			R.color.lightgreen,R.color.ltgray,R.color.magenta,R.color.yello
			};
	private List<View> mList=new ArrayList<View>();
	private List<View> mIvList=new ArrayList<View>();
	private CircleImageView like_one_iv;
	private CircleImageView like_two_iv;
	private CircleImageView like_tree_iv;
	private CircleImageView like_four_iv;
	private CircleImageView like_five_iv;
	private CircleImageView like_six_iv;
	private CircleImageView like_seven_iv;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.like_layout);
		initView();
		
		
		
	}

	private void initView() {
		like_one=(RelativeLayout)findViewById(R.id.like_one);
		like_two=(RelativeLayout)findViewById(R.id.like_two);
		like_tree=(RelativeLayout)findViewById(R.id.like_tree);
		like_four=(RelativeLayout)findViewById(R.id.like_four);
		like_five=(RelativeLayout)findViewById(R.id.like_five);
		like_six=(RelativeLayout)findViewById(R.id.like_six);
		like_seven=(RelativeLayout)findViewById(R.id.like_seven);	
		
		like_one_iv=(CircleImageView)findViewById(R.id.like_one_iv);
		like_two_iv=(CircleImageView)findViewById(R.id.like_two_iv);
		like_tree_iv=(CircleImageView)findViewById(R.id.like_tree_iv);
		like_four_iv=(CircleImageView)findViewById(R.id.like_four_iv);
		like_five_iv=(CircleImageView)findViewById(R.id.like_five_iv);
		like_six_iv=(CircleImageView)findViewById(R.id.like_six_iv);
		like_seven_iv=(CircleImageView)findViewById(R.id.like_seven_iv);
		
		mList.add(like_one);
		mList.add(like_two);
		mList.add(like_tree);
		mList.add(like_four);
		mList.add(like_five);
		mList.add(like_six);
		mList.add(like_seven);
		
		mIvList.add(like_one_iv);
		mIvList.add(like_two_iv);
		mIvList.add(like_tree_iv);
		mIvList.add(like_four_iv);
		mIvList.add(like_five_iv);
		mIvList.add(like_six_iv);
		mIvList.add(like_seven_iv);
		
		
		like_one.setOnClickListener(this);
		like_two.setOnClickListener(this);
		like_tree.setOnClickListener(this);
		like_four.setOnClickListener(this);
		like_five.setOnClickListener(this);
		like_six.setOnClickListener(this);
		like_seven.setOnClickListener(this);
		
		
		
		
	}

	@Override
	public void onClick(View v) {
		getPosition();
		switch(v.getId()){
		case R.id.like_one:
			
			break;
		case  R.id.like_two:
			
			break;
		case R.id.like_tree:
			
			break;
		case R.id.like_four:
			
			break;
		case R.id.like_five:
			
			break;
		case R.id.like_six:
			
			break;
		case R.id.like_seven:
			
			break;
		}
		
	}
	
	
	public void getPosition(){
		int h,w,n,add;
		Random r=new Random();
		for(int i=0;i<7;i++){
			 h = r.nextInt(Config.SCREEN_HEIGHT/2-60);
			 w = r.nextInt(Config.SCREEN_WIDTH-150-60);
			 add=r.nextInt(60);
			 n = r.nextInt(12);
			 
			 
			 
//			 wArray[i]=w;
//			 hArray[i]=h;
			 mIvList.get(i).setBackgroundColor(getResources().getColor(colorArray[n]));
			 mList.get(i).layout(w, h, w+150+add, h+150+add);
		}
//		like_one.layout(wArray[0], hArray[0], wArray[0]+like_one.getWidth(), hArray[0]+like_one.getWidth()); 
//		like_two.layout(wArray[1], hArray[1], wArray[1]+like_two.getWidth(), hArray[1]+like_two.getWidth());
//		like_tree.layout(wArray[2], hArray[2], wArray[2]+like_tree.getWidth(), hArray[2]+like_tree.getWidth());
//		like_four.layout(wArray[3], hArray[3], wArray[3]+like_four.getWidth(), hArray[3]+like_four.getWidth());
//		like_five.layout(wArray[4], hArray[4], wArray[4]+like_five.getWidth(), hArray[4]+like_five.getWidth());
//		like_six.layout(wArray[5], hArray[5], wArray[5]+like_six.getWidth(), hArray[5]+like_six.getWidth());
//		like_seven.layout(wArray[6], hArray[6], wArray[6]+like_seven.getWidth(), hArray[6]+like_seven.getWidth());
	
	}
	

}
