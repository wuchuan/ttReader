package com.zqgame.yyreader.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;
import com.zqgame.yyreader.GloableParams;
import com.zqgame.yyreader.R;
import com.zqgame.yyreader.activity.ReadActivity;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.widget.DraggableGridView;
import com.zqgame.yyreader.widget.FloatCircleView;
import com.zqgame.yyreader.widget.OnRearrangeListener;

public class MainFragment extends Fragment implements OnClickListener {
	private FloatCircleView mLayout;
	private WindowManager mWindowManager;
	private LayoutParams param;
	private LinearLayout bookshelves_set;
	private DraggableGridView bookshelves;
	private Button bookshelves_sign_btn;
	private BitmapUtils bitmapUtils;
	static Random random = new Random();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 bitmapUtils = new BitmapUtils(getActivity().getApplication());
		View view = inflater.inflate(R.layout.main_fragment, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		bookshelves_set = (LinearLayout) view
				.findViewById(R.id.bookshelves_set);
		bookshelves = (DraggableGridView) view.findViewById(R.id.bookshelves);
		bookshelves.setBackGroundDraw(R.drawable.k);
		bookshelves_sign_btn = (Button) view
				.findViewById(R.id.bookshelves_sign_btn);
		bookshelves_sign_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView view = new ImageView(getActivity());
				// 加载网络图片
				bitmapUtils.display(view, "http://pic.mmfile.net/2013/07/72293400gw1e2s2c81mu0j.jpg");
//				view.setImageBitmap(getThumb("天龙八部", "http://pic.mmfile.net/2013/07/72293400gw1e2s2c81mu0j.jpg"));
				bookshelves.addView(view);
			}
		});

		bookshelves.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				// String word = poem.remove(oldIndex);
				// if (oldIndex < newIndex)
				// poem.add(newIndex, word);
				// else
				// poem.add(newIndex, word);
			}
		});
		bookshelves.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent mIntent = new Intent(getActivity(), ReadActivity.class);
				mIntent.putExtra("isFormNet", true);
				mIntent.putExtra("bookid", 1);
				getActivity().startActivity(mIntent);

				// bookshelves.removeViewAt(arg2);
				// poem.remove(arg2);
			}
		});
		initFloatView();
		getdata();
	}

	private Bitmap getThumb(String s, String url) {
		Bitmap bmp;
		 
//		if (null != url) {
//			bmp = Bitmap.createBitmap(FileUtil.pictureCompress2Drawable(url),
//					0, 0, 300, 450);
//		} else {
//			bmp = Bitmap.createBitmap(300, 450, Bitmap.Config.RGB_565);
//		}
		bmp = Bitmap.createBitmap(300, 450, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();

		paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128),
				random.nextInt(128)));
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 300, 450), paint);
		paint.setColor(Color.GREEN);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);

		return bmp;
	}

	@SuppressLint("NewApi")
	private void getdata() {
		String url = Url.getUri(Url.POST_BOOK_DETAIL);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("bookId", 1 + ""));
		parameters.add(new BasicNameValuePair("token", ""));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {
					}

					@Override
					public void handleNetWorkData(String result, String flagStr) {
						LogUtil.log("result is :" + result);
					}
				});

		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

	}

	@Override
	public void onPause() {
		super.onPause();
		if ((mLayout.getParent() != null)) {
			mWindowManager.removeView(mLayout);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if ((mLayout.getParent() == null)) {
			mWindowManager.addView(mLayout, param);
		}
	}

	private void initFloatView() {
		mLayout = new FloatCircleView(getActivity());
		mLayout.setBackgroundResource(R.drawable.ic_launcher);
		// mLayout.setScaleType(ImageView.ScaleType.CENTER);
		// mLayout.setImageResource(R.drawable.live_gif_anim);
		// AnimationDrawable AniDraw = (AnimationDrawable)mLayout.getDrawable();
		// AniDraw.start();
		mLayout.setId(R.id.floatcircleview);
		// 获取WindowManager
		mWindowManager = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		param = GloableParams.getMywmParams();

		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // 系统提示类型,重要
		param.format = 1;
		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
		// param.flags = param.flags |
		// WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		param.flags = param.flags
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		// param.flags = param.flags
		// | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

		param.alpha = 1.0f;

		param.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// param.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		// 以屏幕左上角为原点，设置x、y初始值
		// param.x = 0;
		// param.y = 0;

		// param.x = getResources().getDisplayMetrics().widthPixels-(int)
		// getResources().getDimension(R.dimen.width_120dp)/2;
		// param.y = getResources().getDisplayMetrics().heightPixels-(int)
		// getResources().getDimension(R.dimen.height_265dp)/2;

		param.x = getResources().getDisplayMetrics().widthPixels
				- (int) getResources().getDimension(R.dimen.width_120dp) / 2;
		param.y = getResources().getDisplayMetrics().heightPixels
				- (int) getResources().getDimension(R.dimen.height_290dp) / 2;
		// 设置悬浮窗口长宽数据
		// param.width = 140;
		// param.height = 140;

		// param.width = (int)
		// getResources().getDimension(R.dimen.width_100dp)/2;
		// param.height = (int)
		// getResources().getDimension(R.dimen.height_100dp)/2;
		param.width = (int) getResources().getDimension(R.dimen.width_100dp) / 2;
		param.height = (int) getResources().getDimension(R.dimen.height_100dp) / 2;
		// 显示myFloatView图像
		mWindowManager.addView(mLayout, param);
		mLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (bookshelves_set.getVisibility() == View.GONE) {
			bookshelves_set.setVisibility(View.VISIBLE);
		} else {
			bookshelves_set.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if ((mLayout.getParent() != null)) {
			mWindowManager.removeView(mLayout);
		}
	}

}
