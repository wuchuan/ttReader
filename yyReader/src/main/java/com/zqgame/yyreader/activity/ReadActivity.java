package com.zqgame.yyreader.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zqgame.yyreader.Config;
import com.zqgame.yyreader.R;
import com.zqgame.yyreader.adapter.CatalogueAdapter;
import com.zqgame.yyreader.callback.CatalogueItemCallBack;
import com.zqgame.yyreader.encryption.Cryptor;
import com.zqgame.yyreader.entity.Book;
import com.zqgame.yyreader.entity.Chapter;
import com.zqgame.yyreader.entity.Maker;
import com.zqgame.yyreader.entity.Pager;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.util.FileUtil;
import com.zqgame.yyreader.util.JsonUtil;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.ReaderUtil;
import com.zqgame.yyreader.util.StringUtil;
import com.zqgame.yyreader.widget.CatalogueViewPager;
import com.zqgame.yyreader.widget.PageWidget;

public class ReadActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout reader_change_set;
	private LinearLayout reader_title_use;
	private ImageView reader_change;
	private TextView reader_pre_chapter;
	private TextView reader_next_chapter;
	private SeekBar reader_seekbar;
	private TextView reader_timer;
	private CatalogueViewPager catalogue_viewpager;
	private TextView reader_catalogue_maker_title;
	private ImageView reader_catalogue;
	private ImageView reader_marker;

	private List<Chapter> chapterList;
	private List<Maker> MakerList;
	private CatalogueItemCallBack mCatalogueItemCallBack;
	private Book mBook;
	private LinearLayout reader_open_catalogue;
	private LinearLayout reader_catalogue_maker;
	private GestureDetectorCompat mGesture;
	private RelativeLayout read_content;
	private PageWidget mPageWidget;
	private Bitmap mCurPageBitmap;
	private Bitmap mNextPageBitmap;
	private Canvas mCurPageCanvas;
	private Canvas mNextPageCanvas;
	private TextView read_title_chapter;
	private TextView read_rate;

	private LinearLayout reader_font_set;
	private LinearLayout reader_font_bg;
	private ImageView reader_font_set_add;
	private TextView reader_font_set_size;
	private ImageView reader_font_set_remove;
	private LinearLayout reader_protect_eye;
	private LinearLayout reader_open_protect_eye;

	private LinearLayout reader_protect_eye_mode;
	private TextView reader_protect_eye_mode_txt;
	private View reader_protect_eye_mode_view;

	private LinearLayout reader_night_mode;
	private TextView reader_night_mode_txt;
	private View reader_night_mode_view;

	private SeekBar reader_light_bar;

	private LinearLayout reader_open_flip_mode;
	private LinearLayout reader_flip_mode;

	private LinearLayout reader_open_other_set;
	private LinearLayout reader_other_set;
	private LinearLayout reader_chapter_change;

	private int netBookId = -1;
	private String localBookDir = "";

	private ImageView reader_font_set_color1;
	private ImageView reader_font_set_color2;
	private ImageView reader_font_set_color3;
	private ImageView reader_font_set_color4;

	private ImageView reader_font_set_bg1;
	private ImageView reader_font_set_bg2;
	private ImageView reader_font_set_bg3;
	private ImageView reader_font_set_bg4;

	private LinearLayout reader_other_set_addmk;
	private LinearLayout reader_other_set_award;
	private LinearLayout reader_other_set_share;
	private LinearLayout reader_other_set_prepurchase;
	private LinearLayout reader_other_set_adaptation;

	private LinearLayout reader_mode_real;
	private LinearLayout reader_mode_rl;
	private LinearLayout reader_mode_tb;
	private LinearLayout reader_mode_no;

	private ImageView reader_mode_real_img;
	private ImageView reader_mode_rl_img;
	private ImageView reader_mode_tb_img;
	private ImageView reader_mode_no_img;

	private TimerBroadCast myReceiver;
	private boolean isStBoveReader = false;
	private LinearLayout reader_set_list;
	private ImageView reader_back;

	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case 1:
	// mBook.displayCureentPager(mCurPageCanvas);
	// break;
	// case 2:
	// break;
	// }
	//
	// }
	//
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reader_layout);
		initView();
		initOpt();

	}

	private void initOpt() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		changeTextSizes(Config.CONTENT_SIZE);
		if (getIntent().getBooleanExtra("isFormNet", false)) {
			netBookId = getIntent().getIntExtra("bookid", -1);
			// TODO 先从数据库拿基本信息，再根据基本信息，去拿缓存文件，如果没有没有缓存文件，从网络取
			// getBookDetailByDb(netBookId);
			//

			getBookDetailByNet(netBookId);
		} else {
			localBookDir = getIntent().getStringExtra("bookdir");
			mBook = new Book();
			// mBook.openBook("/storage/emulated/0/xxx.txt");
			mBook.openBook(localBookDir);
			CatalogueAdapter mCatalogueAdapter = new CatalogueAdapter(
					getSupportFragmentManager(), mBook.getChapterList(),
					MakerList, mCatalogueItemCallBack);
			catalogue_viewpager.setAdapter(mCatalogueAdapter);
			mBook.displayCureentPager(mCurPageCanvas);
		}
		initSet();
		initBroundCast();

	}

	private void getBookDetailByDb(int id) {

	}

	/** 读取以前的配置，并设置 */
	private void initSet() {
		if (ReaderUtil.getSetBoolean(this, "isNightMode", false)) {
			reader_night_mode_txt.setTextColor(Color.parseColor("#6ED3C4"));
			reader_protect_eye_mode_txt.setTextColor(Color
					.parseColor("#ffffff"));
			reader_protect_eye_mode_view.setVisibility(View.GONE);
			reader_night_mode_view.setVisibility(View.VISIBLE);
		} else {
			reader_night_mode_txt.setTextColor(Color.parseColor("#ffffff"));
			reader_protect_eye_mode_txt.setTextColor(Color
					.parseColor("#6ED3C4"));
			reader_protect_eye_mode_view.setVisibility(View.VISIBLE);
			reader_night_mode_view.setVisibility(View.GONE);
		}

		switch (ReaderUtil.getSetInt(this, "bg", 1)) {
		case 1:
			reader_font_set_bg1.setImageResource(R.drawable.font_sel);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg1);
			break;
		case 2:
			reader_font_set_bg2.setImageResource(R.drawable.font_sel);
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg2);
			break;
		case 3:
			reader_font_set_bg3.setImageResource(R.drawable.font_sel);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg3);
			break;
		case 4:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageResource(R.drawable.font_sel);
			
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg4);
			break;
		case 11:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color1.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = null;
			Config.BG_COLOR=0xffF1E2CD;
			break;
		case 12:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color2.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = null;
			Config.BG_COLOR=0xffCBDDF3;
			break;
		case 13:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageResource(R.drawable.font_sel);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = null;
			Config.BG_COLOR=0xffFFDCFD;
			break;
		case 14:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			
			reader_font_set_color4.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = null;
			Config.BG_COLOR=0xffFEB6B7;
			break;
		}

	}

	private void initBroundCast() {

		myReceiver = new TimerBroadCast();
		IntentFilter filter = new IntentFilter();
		// 向过滤器中添加action
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);

		// 注册广播
		registerReceiver(myReceiver, filter);

	}

	public void initView() {
		mCurPageBitmap = Bitmap.createBitmap(Config.SCREEN_WIDTH,
				Config.SCREEN_HEIGHT, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(Config.SCREEN_WIDTH,
				Config.SCREEN_HEIGHT, Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);

		// mCurPageCanvas.drawBitmap(
		// BitmapFactory.decodeResource(getResources(), R.drawable.bg), 0,
		// 0, null);
		mCurPageCanvas.drawColor(Config.BG_COLOR);

		// mNextPageCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher), 0, 0, null);
		mNextPageCanvas.drawColor(Config.BG_COLOR);

		reader_back = (ImageView) findViewById(R.id.reader_back);
		read_title_chapter = (TextView) findViewById(R.id.read_title_chapter);
		read_rate = (TextView) findViewById(R.id.read_rate);
		reader_timer = (TextView) findViewById(R.id.reader_timer);
		reader_timer.setText(new java.text.SimpleDateFormat("hh:mm a",
				Locale.US).format(new java.util.Date()));
		read_content = (RelativeLayout) findViewById(R.id.read_content);
		reader_font_set = (LinearLayout) findViewById(R.id.reader_font_set);
		reader_chapter_change = (LinearLayout) findViewById(R.id.reader_chapter_change);
		reader_set_list = (LinearLayout) findViewById(R.id.reader_set_list);

		mPageWidget = new PageWidget(this, Config.SCREEN_WIDTH,
				Config.SCREEN_HEIGHT - Config.SCREEN_WIDTH / 320);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		read_content.addView(mPageWidget);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			int index;

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret = false;
				if (v == mPageWidget) {
					if (!isStBoveReader) {
						if (e.getAction() == MotionEvent.ACTION_DOWN) {
							mPageWidget.abortAnimation();
							mPageWidget.calcCornerXY(e.getX(), e.getY());
							// mCurPageCanvas.drawBitmap(BitmapFactory.decodeResource(
							// getResources(), R.drawable.bg), 0, 0, null);
							mCurPageCanvas.drawColor(Config.BG_COLOR);

							mBook.displayCureentPager(mCurPageCanvas);
							read_title_chapter.setText(mBook.getChapterList()
									.get(mBook.getCurrentChapterNum())
									.getName());
							if (StringUtil.isEmptyString(mBook.getBookDir())
									&& mBook.getId() != -1) {
								refreshPosition();
							} else {
								refreshPosition();
							}
							// mNextPageCanvas.drawBitmap(BitmapFactory
							// .decodeResource(getResources(), R.drawable.bg),
							// 0, 0, null);
							mNextPageCanvas.drawColor(Config.BG_COLOR);

							if (mPageWidget.DragToRight()) {// 左翻
								// Log.e(TAG, "onTouch->DragToRight");
								// mCurPageCanvas.drawBitmap(
								// BitmapFactory.decodeResource(getResources(),
								// R.drawable.bg), 0,
								// 0, null);
								index = mBook.displayPrePager(mNextPageCanvas);
								if (index > -1) {
									// 1.取缓存 2.网络提取
									String s = getChapter(mBook.getId(),
											index + 1);
									if (!StringUtil.isEmptyString(s)) {
										mBook.getChapterList().get(index)
												.setContent(s);
										mBook.displayPrePager(mNextPageCanvas);

									} else {
										// 直接请求，还是出界面提示下载？？
										getBookChapter(mBook.getId(), index + 1);
									}
								} else if (index == -2) {
									// 到了最前面
									// Toast.makeText(this, "已经是最前面一页！",
									// Toast.LENGTH_SHORT).show();
								}

								read_title_chapter.setText(mBook
										.getChapterList()
										.get(mBook.getCurrentChapterNum())
										.getName());
							} else {// 右翻
								// Log.e(TAG, "onTouch->DragToLeft");
								index = mBook.displayNextPager(mNextPageCanvas);
								if (index > -1) {
									// 1.取缓存 2.网络提取
									String s = getChapter(mBook.getId(),
											index + 1);
									if (!StringUtil.isEmptyString(s)) {
										mBook.getChapterList().get(index)
												.setContent(s);
										mBook.displayNextPager(mNextPageCanvas);

									} else {
										// 直接请求，还是出界面提示下载？？
										getBookChapter(mBook.getId(), index + 1);
									}
								} else if (index == -2) {
									// 到了最后
									// Toast.makeText(this, "已经是最后一页！",
									// Toast.LENGTH_SHORT).show();
								}
								read_title_chapter.setText(mBook
										.getChapterList()
										.get(mBook.getCurrentChapterNum())
										.getName());

							}
							mPageWidget.setBitmaps(mCurPageBitmap,
									mNextPageBitmap);
						}
						if (Config.TouchMode != Config.TouchMode.NONE) {
							ret = mPageWidget.doTouchEvent(e);
						}
						return ret;
					} else {
						isStBoveReader = false;
						reader_font_set.setVisibility(View.GONE);
						reader_change_set.setVisibility(View.GONE);
						reader_catalogue_maker.setVisibility(View.GONE);
						reader_title_use.setVisibility(View.GONE);
						reader_protect_eye.setVisibility(View.GONE);
						reader_flip_mode.setVisibility(View.GONE);
						reader_other_set.setVisibility(View.GONE);
					}
				}
				return false;
			}
		});

		// 阅读器设置
		reader_font_bg = (LinearLayout) findViewById(R.id.reader_font_bg);
		reader_change_set = (LinearLayout) findViewById(R.id.reader_change_set);
		reader_title_use = (LinearLayout) findViewById(R.id.reader_title_use);

		reader_change = (ImageView) findViewById(R.id.reader_change);
		reader_pre_chapter = (TextView) findViewById(R.id.reader_pre_chapter);
		reader_next_chapter = (TextView) findViewById(R.id.reader_next_chapter);
		reader_seekbar = (SeekBar) findViewById(R.id.reader_seekbar);

		reader_open_protect_eye = (LinearLayout) findViewById(R.id.reader_open_protect_eye);
		reader_protect_eye = (LinearLayout) findViewById(R.id.reader_protect_eye);
		reader_light_bar = (SeekBar) findViewById(R.id.reader_light_bar);
		reader_light_bar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
					}

					@Override
					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean arg2) {
						WindowManager.LayoutParams lp = getWindow()
								.getAttributes();
						lp.screenBrightness = (float) ((float) progress / (100.0));
						getWindow().setAttributes(lp);
					}
				});
		reader_open_flip_mode = (LinearLayout) findViewById(R.id.reader_open_flip_mode);
		reader_flip_mode = (LinearLayout) findViewById(R.id.reader_flip_mode);

		reader_protect_eye_mode = (LinearLayout) findViewById(R.id.reader_protect_eye_mode);
		reader_protect_eye_mode_txt = (TextView) findViewById(R.id.reader_protect_eye_mode_txt);
		reader_protect_eye_mode_view = (View) findViewById(R.id.reader_protect_eye_mode_view);

		reader_night_mode = (LinearLayout) findViewById(R.id.reader_night_mode);
		reader_night_mode_txt = (TextView) findViewById(R.id.reader_night_mode_txt);
		reader_night_mode_view = (View) findViewById(R.id.reader_night_mode_view);

		reader_open_other_set = (LinearLayout) findViewById(R.id.reader_open_other_set);
		reader_other_set = (LinearLayout) findViewById(R.id.reader_other_set);
		reader_other_set_addmk = (LinearLayout) findViewById(R.id.reader_other_set_addmk);
		reader_other_set_award = (LinearLayout) findViewById(R.id.reader_other_set_award);
		reader_other_set_share = (LinearLayout) findViewById(R.id.reader_other_set_share);
		reader_other_set_prepurchase = (LinearLayout) findViewById(R.id.reader_other_set_prepurchase);
		reader_other_set_adaptation = (LinearLayout) findViewById(R.id.reader_other_set_adaptation);

		reader_open_catalogue = (LinearLayout) findViewById(R.id.reader_open_catalogue);

		catalogue_viewpager = (CatalogueViewPager) findViewById(R.id.catalogue_viewpager);
		catalogue_viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				return mGesture.onTouchEvent(event);
			}
		});

		mGesture = new GestureDetectorCompat(this, new OnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {

			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {

			}

			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {

				int num = catalogue_viewpager.getCurrentItem();
				if (num == 0) {
					catalogue_viewpager.setCurrentItem(1, true);
					reader_catalogue.setSelected(false);
					reader_marker.setSelected(true);
					// mGiftSelectButton.setImageResource(R.drawable.btn_gift_grid);
				}

				else if (num == 1) {
					catalogue_viewpager.setCurrentItem(0, true);
					reader_catalogue.setSelected(true);
					reader_marker.setSelected(false);
					// mGiftSelectButton.setImageResource(R.drawable.btn_gift_list);
				}
				return false;
			}

			@Override
			public boolean onDown(MotionEvent arg0) {
				return false;
			}
		});
		reader_catalogue_maker_title = (TextView) findViewById(R.id.reader_catalogue_maker_title);
		reader_catalogue = (ImageView) findViewById(R.id.reader_catalogue);
		reader_marker = (ImageView) findViewById(R.id.reader_marker);

		reader_catalogue_maker = (LinearLayout) findViewById(R.id.reader_catalogue_maker);

		reader_back.setOnClickListener(this);

		reader_open_catalogue.setOnClickListener(this);
		reader_font_bg.setOnClickListener(this);
		reader_open_protect_eye.setOnClickListener(this);
		reader_open_flip_mode.setOnClickListener(this);

		reader_open_other_set.setOnClickListener(this);
		reader_other_set_addmk.setOnClickListener(this);
		reader_other_set_award.setOnClickListener(this);
		reader_other_set_share.setOnClickListener(this);
		reader_other_set_prepurchase.setOnClickListener(this);
		reader_other_set_adaptation.setOnClickListener(this);

		reader_protect_eye_mode.setOnClickListener(this);
		reader_night_mode.setOnClickListener(this);

		reader_title_use.setOnClickListener(this);

		reader_chapter_change.setOnClickListener(this);
		reader_set_list.setOnClickListener(this);
		reader_font_set.setOnClickListener(this);// 屏蔽点击布局的空白区域，退出界面

		reader_mode_real = (LinearLayout) findViewById(R.id.reader_mode_real);
		reader_mode_rl = (LinearLayout) findViewById(R.id.reader_mode_rl);
		reader_mode_tb = (LinearLayout) findViewById(R.id.reader_mode_tb);
		reader_mode_no = (LinearLayout) findViewById(R.id.reader_mode_no);

		reader_mode_real_img = (ImageView) findViewById(R.id.reader_mode_real_img);
		reader_mode_rl_img = (ImageView) findViewById(R.id.reader_mode_rl_img);
		reader_mode_tb_img = (ImageView) findViewById(R.id.reader_mode_tb_img);
		reader_mode_no_img = (ImageView) findViewById(R.id.reader_mode_no_img);

		reader_font_set_color1 = (ImageView) findViewById(R.id.reader_font_set_color1);
		reader_font_set_color2 = (ImageView) findViewById(R.id.reader_font_set_color2);
		reader_font_set_color3 = (ImageView) findViewById(R.id.reader_font_set_color3);
		reader_font_set_color4 = (ImageView) findViewById(R.id.reader_font_set_color4);

		reader_font_set_bg1 = (ImageView) findViewById(R.id.reader_font_set_bg1);
		reader_font_set_bg2 = (ImageView) findViewById(R.id.reader_font_set_bg2);
		reader_font_set_bg3 = (ImageView) findViewById(R.id.reader_font_set_bg3);
		reader_font_set_bg4 = (ImageView) findViewById(R.id.reader_font_set_bg4);

		reader_font_set_add = (ImageView) findViewById(R.id.reader_font_set_add);
		reader_font_set_size = (TextView) findViewById(R.id.reader_font_set_size);
		reader_font_set_remove = (ImageView) findViewById(R.id.reader_font_set_remove);

		reader_font_set_color1.setOnClickListener(this);
		reader_font_set_color2.setOnClickListener(this);
		reader_font_set_color3.setOnClickListener(this);
		reader_font_set_color4.setOnClickListener(this);

		reader_font_set_bg1.setOnClickListener(this);
		reader_font_set_bg2.setOnClickListener(this);
		reader_font_set_bg3.setOnClickListener(this);
		reader_font_set_bg4.setOnClickListener(this);

		reader_font_set_add.setOnClickListener(this);
		reader_font_set_size.setOnClickListener(this);
		reader_font_set_remove.setOnClickListener(this);

		reader_mode_real.setOnClickListener(this);
		reader_mode_rl.setOnClickListener(this);
		reader_mode_tb.setOnClickListener(this);
		reader_mode_no.setOnClickListener(this);

		reader_catalogue.setOnClickListener(this);
		reader_marker.setOnClickListener(this);
		reader_change.setOnClickListener(this);
		reader_pre_chapter.setOnClickListener(this);
		reader_next_chapter.setOnClickListener(this);

		mCatalogueItemCallBack = new CatalogueItemCallBack() {
			@Override
			public void jumpToCatalogue(int position) {
				mBook.setCurrentChapterNum(position);
				mBook.getChapterList().get(mBook.getCurrentChapterNum())
						.setCurrentPagerPosition(0);
				// mCurPageCanvas.drawBitmap(BitmapFactory.decodeResource(
				// getResources(), R.drawable.bg), 0, 0, null);
				mCurPageCanvas.drawColor(Config.BG_COLOR);
				int index = mBook.displayCureentPager(mCurPageCanvas);
				if (index > -1) {
					// 1.取缓存 2.网络提取
					String s = getChapter(mBook.getId(), index + 1);
					if (!StringUtil.isEmptyString(s)) {
						mBook.getChapterList().get(index).setContent(s);
						mBook.displayCureentPager(mCurPageCanvas);

					} else {
						// 直接请求，还是出界面提示下载？？
						getBookChapter(mBook.getId(), index + 1);
					}
				} else if (index == -2) {
					// 到了最前面
					// Toast.makeText(this, "已经是最前面一页！",
					// Toast.LENGTH_SHORT).show();
				}
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
				read_title_chapter.setText(mBook.getChapterList()
						.get(mBook.getCurrentChapterNum()).getName());
				refreshPosition();
				reader_catalogue_maker.setVisibility(View.GONE);

			}
		};

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (reader_change_set.getVisibility() == View.GONE) {
				isStBoveReader = true;
				reader_change_set.setVisibility(View.VISIBLE);
				reader_title_use.setVisibility(View.VISIBLE);
			} else {
				reader_change_set.setVisibility(View.GONE);
				reader_title_use.setVisibility(View.GONE);
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mBook != null) {
				mBook.setReadPos(mBook.getCurrentChapterNum()
						+ ","
						+ mBook.getChapterList()
								.get(mBook.getCurrentChapterNum())
								.getCurrentPager().getmStartPosition());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		int index;
		switch (view.getId()) {
		case R.id.reader_back:
			if (mBook != null) {
				mBook.setReadPos(mBook.getCurrentChapterNum()
						+ ","
						+ mBook.getChapterList()
								.get(mBook.getCurrentChapterNum())
								.getCurrentPager().getmStartPosition());
			}
			finish();
			break;
		case R.id.reader_change: // 切换日夜模式

			break;
		case R.id.reader_pre_chapter:
			mCurPageCanvas.drawColor(Config.BG_COLOR);
			index = mBook.displayPreChapter(mCurPageCanvas);
			if (index > -1) {
				// 1.取缓存 2.网络提取
				String s = getChapter(mBook.getId(), index + 1);
				if (!StringUtil.isEmptyString(s)) {
					mBook.getChapterList().get(index).setContent(s);
					mBook.displayPreChapter(mCurPageCanvas);

				} else {
					// 直接请求，还是出界面提示下载？？
					getBookChapter(mBook.getId(), index + 1);
				}
			} else if (index == -2) {
				// 到了最前面
				// Toast.makeText(this, "已经是最前面一页！", Toast.LENGTH_SHORT).show();
			}
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.invalidate();
			break;
		case R.id.reader_next_chapter:
			mCurPageCanvas.drawColor(Config.BG_COLOR);
			index = mBook.displayNextChapter(mCurPageCanvas);
			if (index > -1) {
				// 1.取缓存 2.网络提取
				String s = getChapter(mBook.getId(), index + 1);
				if (!StringUtil.isEmptyString(s)) {
					mBook.getChapterList().get(index).setContent(s);
					mBook.displayNextChapter(mCurPageCanvas);

				} else {
					// 直接请求，还是出界面提示下载？？
					getBookChapter(mBook.getId(), index + 1);
				}
			} else if (index == -2) {

				refreshPosition();

				// 到了最前面
				// Toast.makeText(this, "已经是最前面一页！", Toast.LENGTH_SHORT).show();
			}
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.invalidate();
			break;
		case R.id.reader_marker:
			catalogue_viewpager.setCurrentItem(1, true);
			reader_catalogue.setSelected(false);
			reader_marker.setSelected(true);
			break;
		case R.id.reader_catalogue:
			catalogue_viewpager.setCurrentItem(0, true);
			reader_catalogue.setSelected(true);
			reader_marker.setSelected(false);
			break;
		case R.id.reader_open_catalogue:
			reader_catalogue_maker.setVisibility(View.VISIBLE);
			reader_change_set.setVisibility(View.GONE);
			reader_title_use.setVisibility(View.GONE);
			reader_catalogue_maker_title.setText(mBook.getChapterList()
					.get(mBook.getCurrentChapterNum()).getName());
			break;
		case R.id.reader_open_protect_eye:
			isStBoveReader = true;
			reader_protect_eye.setVisibility(View.VISIBLE);
			reader_change_set.setVisibility(View.GONE);
			reader_title_use.setVisibility(View.GONE);
			break;
		case R.id.reader_open_flip_mode:
			isStBoveReader = true;
			reader_flip_mode.setVisibility(View.VISIBLE);
			reader_change_set.setVisibility(View.GONE);
			reader_title_use.setVisibility(View.GONE);
			break;
		case R.id.reader_open_other_set:
			isStBoveReader = true;
			reader_other_set.setVisibility(View.VISIBLE);
			reader_change_set.setVisibility(View.GONE);
			reader_title_use.setVisibility(View.GONE);
			break;
		case R.id.reader_font_bg:
			isStBoveReader = true;
			reader_font_set.setVisibility(View.VISIBLE);
			reader_change_set.setVisibility(View.GONE);
			reader_catalogue_maker.setVisibility(View.GONE);
			reader_title_use.setVisibility(View.GONE);
			break;
		case R.id.reader_font_set_add:
			if (Config.CONTENT_SIZE < 71) {
				Config.CONTENT_SIZE = Config.CONTENT_SIZE + 1;
				changeTextSizes(Config.CONTENT_SIZE);
				reader_font_set_size.setText(Config.CONTENT_SIZE + "");
				mBook.refreshFontSize();
				// mCurPageCanvas.drawBitmap(BitmapFactory.decodeResource(
				// getResources(), R.drawable.bg), 0, 0, null);
				mCurPageCanvas.drawColor(Config.BG_COLOR);

				mBook.displayCureentPager(mCurPageCanvas);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
			} else {
				Config.CONTENT_SIZE = 71;
				reader_font_set_size.setText(Config.CONTENT_SIZE + "");
			}
			break;
		case R.id.reader_font_set_remove:
			if (Config.CONTENT_SIZE < 22) {
				Config.CONTENT_SIZE = 21;
				reader_font_set_size.setText(Config.CONTENT_SIZE + "");
			} else {
				Config.CONTENT_SIZE = Config.CONTENT_SIZE - 1;
				changeTextSizes(Config.CONTENT_SIZE);
				reader_font_set_size.setText(Config.CONTENT_SIZE + "");
				mBook.refreshFontSize();
				// mCurPageCanvas.drawBitmap(BitmapFactory.decodeResource(
				// getResources(), R.drawable.bg), 0, 0, null);

				mCurPageCanvas.drawColor(Config.BG_COLOR);
				mBook.displayCureentPager(mCurPageCanvas);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
			}
			break;

		case R.id.reader_font_set_bg1:
			reader_font_set_bg1.setImageResource(R.drawable.font_sel);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			// TODO 加载背景图片的方式需要优化
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg1);
			ReaderUtil.saveSetInt(this, "bg", 1);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_font_set_bg2:
			reader_font_set_bg2.setImageResource(R.drawable.font_sel);
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg2);
			ReaderUtil.saveSetInt(this, "bg", 2);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_font_set_bg3:
			reader_font_set_bg3.setImageResource(R.drawable.font_sel);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg3);
			ReaderUtil.saveSetInt(this, "bg", 3);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_font_set_bg4:
			reader_font_set_bg4.setImageResource(R.drawable.font_sel);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			Config.BITMAP_BG = BitmapFactory.decodeResource(getResources(),
					R.drawable.reader_bg4);
			ReaderUtil.saveSetInt(this, "bg", 4);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;

		case R.id.reader_font_set_color1:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color1.setImageResource(R.drawable.font_sel);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			Config.BITMAP_BG=null;
			Config.BG_COLOR=0xffF1E2CD;
			ReaderUtil.saveSetInt(this, "bg", 11);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();

			break;
		case R.id.reader_font_set_color2:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color2.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			Config.BITMAP_BG=null;
			Config.BG_COLOR=0xffCBDDF3;
			ReaderUtil.saveSetInt(this, "bg", 12);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_font_set_color3:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color3.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color4.setImageDrawable(null);
			Config.BITMAP_BG=null;
			Config.BG_COLOR=0xffFFDCFD;
			ReaderUtil.saveSetInt(this, "bg", 13);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_font_set_color4:
			reader_font_set_bg1.setImageDrawable(null);
			reader_font_set_bg2.setImageDrawable(null);
			reader_font_set_bg3.setImageDrawable(null);
			reader_font_set_bg4.setImageDrawable(null);
			reader_font_set_color4.setImageResource(R.drawable.font_sel);
			reader_font_set_color1.setImageDrawable(null);
			reader_font_set_color2.setImageDrawable(null);
			reader_font_set_color3.setImageDrawable(null);
			Config.BITMAP_BG=null;
			Config.BG_COLOR=0xffFEB6B7;
			ReaderUtil.saveSetInt(this, "bg", 14);
			mBook.displayCureentPager(mCurPageCanvas);
			mPageWidget.invalidate();
			break;
		case R.id.reader_other_set_addmk:
			break;
		case R.id.reader_other_set_award:
			break;
		case R.id.reader_other_set_share:
			break;
		case R.id.reader_other_set_prepurchase:
			break;
		case R.id.reader_other_set_adaptation:
			break;

		case R.id.reader_mode_real:
			reader_mode_real_img.setSelected(true);
			reader_mode_rl_img.setSelected(false);
			reader_mode_tb_img.setSelected(false);
			reader_mode_no_img.setSelected(false);
			Config.TouchMode = Config.TouchMode.REAL;
			break;
		case R.id.reader_mode_rl:
			reader_mode_real_img.setSelected(false);
			reader_mode_rl_img.setSelected(true);
			reader_mode_tb_img.setSelected(false);
			reader_mode_no_img.setSelected(false);
			Config.TouchMode = Config.TouchMode.LR;
			break;
		case R.id.reader_mode_tb:
			reader_mode_real_img.setSelected(false);
			reader_mode_rl_img.setSelected(false);
			reader_mode_tb_img.setSelected(true);
			reader_mode_no_img.setSelected(false);
			Config.TouchMode = Config.TouchMode.TB;
			break;
		case R.id.reader_mode_no:
			reader_mode_real_img.setSelected(false);
			reader_mode_rl_img.setSelected(false);
			reader_mode_tb_img.setSelected(false);
			reader_mode_no_img.setSelected(true);
			Config.TouchMode = Config.TouchMode.NONE;
			break;
		case R.id.reader_protect_eye_mode:
			reader_protect_eye_mode_txt.setTextColor(Color
					.parseColor("#6ED3C4"));
			reader_night_mode_txt.setTextColor(Color.parseColor("#ffffff"));
			reader_protect_eye_mode_view.setVisibility(View.VISIBLE);
			reader_night_mode_view.setVisibility(View.GONE);
			ReaderUtil.saveSetBoolean(this, "isNightMode", false);
			break;
		case R.id.reader_night_mode:
			reader_night_mode_txt.setTextColor(Color.parseColor("#6ED3C4"));
			reader_protect_eye_mode_txt.setTextColor(Color
					.parseColor("#ffffff"));
			reader_protect_eye_mode_view.setVisibility(View.GONE);
			reader_night_mode_view.setVisibility(View.VISIBLE);
			ReaderUtil.saveSetBoolean(this, "isNightMode", true);
			break;

		}
	}

	private void refreshPosition() {
		int nowPosition = 0;
		for (int i = 0; i < mBook.getCurrentChapterNum(); i++) {
			nowPosition = +mBook.getChapterList().get(i).getWordCount();
			LogUtil.log("mBook.getChapterList().get(i).nowPosition is " + i
					+ "    " + nowPosition);
		}
		Pager mPager = mBook.getChapterList().get(mBook.getCurrentChapterNum())
				.getCurrentPager();
		if (mPager != null) {
			nowPosition = +(int) mPager.getmEndPosition();
		}
		LogUtil.log("mBook.getChapterList().get(i).nowPosition is mBook.getWordCount() "
				+ mBook.getWordCount());
		reader_seekbar.setProgress(nowPosition * 100 / mBook.getWordCount());
		read_rate.setText(nowPosition * 100 / mBook.getWordCount() + "%");
	}

	/**
	 * 获取书的详细信息
	 * 
	 * @param id
	 *            书籍的ID
	 */
	@SuppressLint("NewApi")
	public void getBookDetailByNet(int id) {
		String url = Url.getUri(Url.POST_BOOK_DETAIL);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("bookId", id + ""));
		parameters.add(new BasicNameValuePair("token", ""));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {
					}

					@Override
					public void handleNetWorkData(String result, String flagStr) {
						if (StringUtil.isEmptyString(result)) {
							// 网络连接超时处理
						}
						if (JsonUtil.getCode(result) == 200) {
							if (JsonUtil.getObject(result, "book") != null) {
								mBook = (Book) JsonUtil.json2bean(JsonUtil
										.getObject(result, "book").toString(),
										Book.class);
								// getBookCatalog(mBook.getId());
								getBookCatalog(1);
							}
						} else {

						}
					}
				});

		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}

	/**
	 * 书籍的目录
	 * 
	 * @param id
	 */
	@SuppressLint("NewApi")
	public void getBookCatalog(int id) {
		String url = Url.getUri(Url.POST_BOOK_CATALOG);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("bookId", id + ""));
		parameters.add(new BasicNameValuePair("token", ""));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {
					}

					@Override
					public void handleNetWorkData(String result, String flagStr) {
						if (JsonUtil.getCode(result) == 200) {
							if (JsonUtil.getObject(result, "catalog") != null) {
								chapterList = (List<Chapter>) JsonUtil
										.json2List(
												JsonUtil.getObject(result,
														"catalog").toString(),
												Chapter.class);
								if (chapterList != null) {
									mBook.setChapterList(chapterList);
									CatalogueAdapter mCatalogueAdapter = new CatalogueAdapter(
											getSupportFragmentManager(), mBook
													.getChapterList(),
											MakerList, mCatalogueItemCallBack);
									catalogue_viewpager
											.setAdapter(mCatalogueAdapter);
									int index = mBook
											.displayCureentPager(mCurPageCanvas);
									if (index > -1) {
										// 1.取缓存 2.网络提取
										String s = getChapter(mBook.getId(),
												index + 1);
										if (!StringUtil.isEmptyString(s)) {
											mBook.getChapterList().get(index)
													.setContent(s);
											mBook.displayCureentPager(mCurPageCanvas);
											mPageWidget.setBitmaps(
													mCurPageBitmap,
													mNextPageBitmap);
											mPageWidget.invalidate();
											// LogUtil.log("index is displayCureentPager :"
											// +
											// mBook.displayCureentPager(mCurPageCanvas));
										} else {

											getBookChapter(mBook.getId(),
													index + 1);
										}
									}
								}
							}
						} else {
						}
					}
				});

		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}

	public void changeTextSizes(int size) {
		Config.TITLE_SIZE = 30 + (size - 20);
		Log.e("", "Config.TITLE_SIZE is " + Config.TITLE_SIZE
				+ " Config.CONTENT_SIZE" + Config.CONTENT_SIZE);
		Config.CONTENT_SIZE = size;
		Config.NOMAL_LINES = ReaderUtil.getLineNumByNomal();
	}

	/**
	 * 
	 * @param id
	 * @param chapterId
	 *            从零开始的章节
	 */
	@SuppressLint("NewApi")
	public void getBookChapter(int id, final int chapterId) {
		String url = Url.getUri(Url.POST_BOOK_CHAPTER);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("bookId", 1 + ""));
		parameters.add(new BasicNameValuePair("chapterId", chapterId + ""));
		parameters.add(new BasicNameValuePair("token", ""));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {
					}

					@Override
					public void handleNetWorkData(String result, String flagStr) {
						if (JsonUtil.getCode(result) == 200) {
							if (JsonUtil.getObject(result, "chapter") != null) {
								Chapter mChapter = (Chapter) JsonUtil
										.json2bean(
												JsonUtil.getObject(result,
														"chapter").toString(),
												Chapter.class);
								if (mChapter != null) {
									mBook.getChapterList().get(chapterId - 1)
											.setContent(mChapter.getContent());
									saveChapter(0, chapterId,
											mChapter.getContent());
									mBook.displayCureentPager(mCurPageCanvas);
									mPageWidget.setBitmaps(mCurPageBitmap,
											mNextPageBitmap);
									mPageWidget.invalidate();
								}
							}
						} else {
						}
					}
				});
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}

	/**
	 * @param type
	 *            0:缓存 1：文件
	 * @param chapterId
	 * @param content
	 */
	public void saveChapter(int type, int chapterId, String content) {
		String rootDir = null;
		if (type == 0) {
			rootDir = FileUtil.getValidCachePath(getApplicationContext());
		} else {
			rootDir = FileUtil.getValidFilePath(getApplicationContext());
		}
		String chapterFile = rootDir + "/" + mBook.getId() + mBook.getName()
				+ File.separator;
		File f1 = new File(chapterFile);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		chapterFile = chapterFile + "chapter" + chapterId + ".txt";
		File f2 = new File(chapterFile);
		if (!f2.exists()) {
			try {
				f2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(f2);
//			String tmp=Cryptor.getCryptor().encrypt(content);
//			 byte[] buffer = tmp.getBytes();
			byte[] buffer = content.getBytes();
			out.write(buffer);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param type
	 *            0:缓存 1：文件
	 * @param chapterId
	 *            从1开始
	 * @return
	 */
	public String getChapter(int type, int chapterId) {

		String rootDir = null;
		if (type == 0) {
			rootDir = FileUtil.getValidCachePath(getApplicationContext());
		} else {
			rootDir = FileUtil.getValidFilePath(getApplicationContext());
		}
		String chapterFile = rootDir + "/" + mBook.getId() + mBook.getName()
				+ File.separator;
		File f1 = new File(chapterFile);
		if (!f1.exists()) {
			return null;
		}
		chapterFile = chapterFile + "chapter" + chapterId + ".txt";
		File f2 = new File(chapterFile);
		if (!f2.exists()) {
			return null;
		}
		String s = null;
		try {
			FileInputStream in = new FileInputStream(f2);
//			 String tmp=Cryptor.getCryptor().encrypt(content);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			s = new String(buffer);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;

	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	class TimerBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				reader_timer.setText(new java.text.SimpleDateFormat("hh:mm a",
						Locale.US).format(new java.util.Date()));
			} else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				// TODO 电量显示的实现
				int level = intent.getIntExtra("level", 0); // 电池电量等级
				int scale = intent.getIntExtra("scale", 100); // 电池满时百分比
				int status = intent.getIntExtra("status", 0); // 电池状态
			}
		}

	}
}
