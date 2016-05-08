package com.zqgame.yyreader.entity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Vector;

import com.zqgame.yyreader.Config;
import com.zqgame.yyreader.util.ReaderUtil;
import com.zqgame.yyreader.util.StringUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.util.Log;

public class Pager {

	private int ChapterId = 0;

	private int PagerIdAtChapter = 0;

	/**
	 * 页面在文件中的开始位置
	 */
	private int mStartPosition;

	/**
	 * 页面在文件中的开始位置
	 */
	private int mEndPosition;

	private boolean mHaveTitle = false;
	/**
	 * 当前页面position为0 前一个页面position为1 后一个页面position为2
	 */
	private int position = 0;


	private Vector<String> content;
	private Chapter mChapter;

	public Pager(Chapter mChapter) {
		this.mChapter = mChapter;
	}

	public boolean ismHaveTitle() {
		return mHaveTitle;
	}

	public void setmHaveTitle(boolean mHaveTitle) {
		this.mHaveTitle = mHaveTitle;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Vector<String> getContent() {
		return content;
	}

	public void setContent(Vector<String> content) {
		this.content = content;
	}

	public long getmStartPosition() {
		return mStartPosition;
	}

	public void setmStartPosition(int mStartPosition) {
		this.mStartPosition = mStartPosition;
	}

	public long getmEndPosition() {
		return mEndPosition;
	}

	public void setmEndPosition(int mEndPosition) {
		this.mEndPosition = mEndPosition;
	}

	public int getChapterId() {
		return ChapterId;
	}

	public void setChapterId(int chapterId) {
		ChapterId = chapterId;
	}

	public int getPagerIdAtChapter() {
		return PagerIdAtChapter;
	}

	public void setPagerIdAtChapter(int pagerIdAtChapter) {
		PagerIdAtChapter = pagerIdAtChapter;
	}

	/**
	 * 把小说文字字画到画布上
	 * 本地文件是直接放进高速缓存里面读出来，记录的是byte的位置
	 * 网络文件是直接取出来使用字符串的形式，记录的是string的char的位置
	 * @param c
	 */
	public void onChangeDraw(Canvas c) {
		if(mChapter.getBookId()!=-1&&StringUtil.isEmptyString(mChapter.getBookDir())){
			 onChangeDrawByNet(c);
		}
		else{
			onChangeDrawByLocal(c);
		}
	}
	public void onChangeDrawByLocal(Canvas c) {
		String strChapter = null;
		int i = 0;
		if (Config.BITMAP_BG == null) {
			c.drawColor(Config.BG_COLOR);
		} else {
			c.drawBitmap(Config.BITMAP_BG, 0, 0, null);
		}
//		Log.e("", "pos is "+getPagerIdAtChapter()+" mStartPosition is:"+mStartPosition+" mEndPosition:"+mEndPosition);
		byte[] Buf = mChapter.readInPosition(mStartPosition, mEndPosition);
		try {
			strChapter = new String(Buf, mChapter.getmStrCharsetName());
//			Log.e("", "strChapter is "+strChapter);
		} catch (UnsupportedEncodingException e) {
			Log.e("", "pageUp->转换编码失败", e);
		}
		String[] strChapterList = strChapter.split("\r\n|\n");
		Paint mPaint = new Paint();
		//mPaint.setStrokeWidth((float) 1);
		mPaint.setColor(Config.FT_COLOR); 
		float y = Config.MARGIN_HEIGHT;
		if (mHaveTitle) {
			// 设置画笔颜色
	
			mPaint.setTextSize(Config.TITLE_SIZE);
			y = y - mPaint.getFontMetrics().ascent;
			content = mChapter.getLineListBySize(strChapterList[i],
					Config.TITLE_SIZE);
			if (content.size() > 0) {
				for (String strLine : content) {
					c.drawText(strLine, Config.MARGIN_WIDTH, y, mPaint);
					y = (float) (y + ReaderUtil.getFontHeight(Config.TITLE_SIZE) * 1.6);
				}
				i++;
			}
		}
		mPaint.setTextSize(Config.CONTENT_SIZE);
		y = y - mPaint.getFontMetrics().ascent;
		for (; i < strChapterList.length; i++) {
			content = mChapter.getLineListBySize(strChapterList[i],
					Config.CONTENT_SIZE);
			if (content.size() > 0) {
				for (String strLine : content) {
					c.drawText(strLine, Config.MARGIN_WIDTH, y, mPaint);
					y = (float) (y + ReaderUtil.getFontHeight(Config.CONTENT_SIZE) * 1.6);
				}
			}

		}
//		float fPercent = (float) (mStartPosition * 1.0 / mChapter
//				.getBookLenght());
//		DecimalFormat df = new DecimalFormat("#0.0");
//		String strPercent = df.format(fPercent * 100) + "%";
//		int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
//
//		c.drawText(strPercent, Config.SCREEN_WIDTH - nPercentWidth,
//				Config.SCREEN_HEIGHT - 5, mPaint);
	}	
	
	public void onChangeDrawByNet(Canvas c) {
		String strChapter = null;
		int i = 0;
		if (Config.BITMAP_BG == null) {
			c.drawColor(Color.TRANSPARENT, Mode.CLEAR); 
			c.drawColor(Config.BG_COLOR);
		} else {
			c.drawBitmap(Config.BITMAP_BG, 0, 0, null);
		}
//		Log.e("", "pos is "+getPagerIdAtChapter()+" mStartPosition is:"+mStartPosition+" mEndPosition:"+mEndPosition);
		byte[] Buf = null;
		byte[] mBuf = null;
		try {
			Buf = mChapter.getContent().getBytes(mChapter.getmStrCharsetName());
			mBuf = new byte[mEndPosition-mStartPosition];
			System.arraycopy(Buf, mStartPosition, mBuf, 0, mEndPosition-mStartPosition);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			strChapter = new String(mBuf, mChapter.getmStrCharsetName());
		} catch (UnsupportedEncodingException e) {
			Log.e("", "pageUp->转换编码失败", e);
		}
		String[] strChapterList = strChapter.split("\r\n|\n");
		Paint mPaint = new Paint();
		//mPaint.setStrokeWidth((float) 1);
		mPaint.setColor(Config.FT_COLOR);
		float y = Config.MARGIN_HEIGHT;
		if (mHaveTitle) {
			 // 设置画笔颜色
			Log.e("", "Config.TITLE_SIZE is "+Config.TITLE_SIZE+" Config.CONTENT_SIZE"+Config.CONTENT_SIZE);
			mPaint.setTextSize(Config.TITLE_SIZE);
			y = y - mPaint.getFontMetrics().ascent;
			content = mChapter.getLineListBySize(mChapter.getName(),
					Config.TITLE_SIZE);
			if (content.size() > 0) {
				for (String strLine : content) {
					c.drawText(strLine, Config.MARGIN_WIDTH, y, mPaint);
					y = (float) (y + ReaderUtil.getFontHeight(Config.TITLE_SIZE) * 1.6);
				}
//				i++;
			}
		}
		mPaint.setTextSize(Config.CONTENT_SIZE);
		y = y - mPaint.getFontMetrics().ascent;
		for (; i < strChapterList.length; i++) {
			content = mChapter.getLineListBySize(strChapterList[i],
					Config.CONTENT_SIZE);
			if (content.size() > 0) {
				for (String strLine : content) {
					c.drawText(strLine, Config.MARGIN_WIDTH, y, mPaint);
//					Log.e("", "drawText"+getPagerIdAtChapter()+" mStartPosition is:"+mStartPosition+" mEndPosition:"+mEndPosition);
					y = (float) (y + ReaderUtil.getFontHeight(Config.CONTENT_SIZE) * 1.6);
				}
			}
		}
		
//		float fPercent = (float) (mStartPosition * 1.0 / mChapter
//				.getBookLenght());
//		DecimalFormat df = new DecimalFormat("#0.0");
//		String strPercent = df.format(fPercent * 100) + "%";
//		int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
//
//		c.drawText(strPercent, Config.SCREEN_WIDTH - nPercentWidth,
//				Config.SCREEN_HEIGHT - 5, mPaint);
	}

}
