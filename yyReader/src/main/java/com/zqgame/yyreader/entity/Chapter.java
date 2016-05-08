package com.zqgame.yyreader.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zqgame.yyreader.Config;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.util.JsonUtil;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.ReaderUtil;
import com.zqgame.yyreader.util.StringUtil;

public class Chapter extends Catalog{
	private String content;



	
	
	private int ChapterId;
	/**
	 * 章节在文件中的开始位置
	 */
	private int mChapterStartPosition;

	/**
	 * 章节在文件中的结束位置
	 */
	private int mChapterEndPosition;
	
	
	private int mChapterTestPosition;
	
	private int currentPagerPosition=0;
	
//	private Pager currentPager;
//	
//	private Pager mPrePager;
//	
//	private Pager mNextPager;
//	private int nomalLine;
	
	private String title;
	
	private ArrayList<Pager> PagerList=new ArrayList<Pager>();
	
	private String[] strChapterList;
	
	private int  strChapterListPosition=0;
	private Book mBook;
	
	
	
	

	
	public Book getmBook() {
		return mBook;
	}

	public void setmBook(Book mBook) {
		this.mBook = mBook;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public int  getBookId(){
		return mBook.getId();
	}
	public String getBookDir(){
		return mBook.getBookDir();
	}
	

	@Override
	public void setId(int id) {
		setChapterId(id-1);//我使用的是从0开始，服务器从1开始
		super.setId(id);
	}
	public Chapter(){
		
	}
	public Chapter(Book mBook){
		this.mBook=mBook;
	}
	
	public long getBookLenght(){
		return mBook.getWordCount();
	}

	public int getChapterId() {
		return ChapterId;
	}

	public void setChapterId(int chapterId) {
		ChapterId = chapterId;
	}
	
	

	public int getCurrentPagerPosition() {
		return currentPagerPosition;
	}

	public void setCurrentPagerPosition(int currentPagerPosition) {
		this.currentPagerPosition = currentPagerPosition;
	}

	public long getmChapterStartPosition() {
		return mChapterStartPosition;
	}

	public void setmChapterStartPosition(int mChapterStartPosition) {
		this.mChapterStartPosition = mChapterStartPosition;
	}

	public long getmChapterEndPosition() {
		return mChapterEndPosition;
	}

	public void setmChapterEndPosition(int mChapterEndPosition) {
		this.mChapterEndPosition = mChapterEndPosition;
	}
	public Pager getCurrentPager() {
		Pager mPager=null;
		if(PagerList.size()==0){
			getPagers();
		}
		if(currentPagerPosition<PagerList.size()){
			mPager=PagerList.get(currentPagerPosition);
		}
		else if(PagerList.size()!=0){
			mPager=PagerList.get(0);
		}
		return mPager;
	}
	public boolean isLastPager(){
		if(PagerList.size()==0){
			getPagers();
		}
		if(currentPagerPosition<PagerList.size()-1){
			return false;
		}
			return true;
	}
	public boolean isFristPager(){
	
		if(currentPagerPosition<1){
			return true;
		}
		return false;
		
	}
	
	

//	public Pager getCurrentPager() {
//		return currentPager;
//	}
//
//	public void setCurrentPager(Pager currentPager) {
//		this.currentPager = currentPager;
//	}
//
//	public Pager getmPrePager() {
//		return mPrePager;
//	}
//
//	public void setmPrePager(Pager mPrePager) {
//		this.mPrePager = mPrePager;
//	}
//
//	public Pager getmNextPager() {
//		return mNextPager;
//	}
//
//	public void setmNextPager(Pager mNextPager) {
//		this.mNextPager = mNextPager;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public ArrayList<Pager> getPagerList() {
		return PagerList;
	}

	public void setPagerList(ArrayList<Pager> pagerList) {
		PagerList = pagerList;
	}
	public String getmStrCharsetName(){
		return mBook.getmStrCharsetName();
	}

	public ArrayList<Pager> getPagers(){
		
//		mChapterTestPosition=mChapterStartPosition;
//		PagerList.clear();
//		String strChapter = null;
//		
//		if(mBook.getId()!=-1&&StringUtil.isEmptyString(mBook.getBookDir())){
//			//TODO 从内存找
//			//TODO 从缓存找
//			//TODO 从文件找
//			if(!StringUtil.isEmptyString(getContent())){
//				strChapter=getContent();
//			}
//			else{
//				return null;
//			}
//		}
//		else{
//			byte[] Buf=mBook.readInPosition(mChapterStartPosition, mChapterEndPosition);
//			try {
//				 strChapter = new String(Buf, mBook.getmStrCharsetName());
//			} catch (UnsupportedEncodingException e) {
//				Log.e("", "pageUp->转换编码失败", e);
//			}
//		}
//		
//		Pager mPager=new Pager(this);
//		mPager.setmHaveTitle(true);
//		mPager.setmStartPosition(mChapterStartPosition);
//		PagerList.add(mPager);
//		
//	
//		strChapterList=strChapter.split("\r\n|\n");
//		
////		NOMAL_LINES=getLineNumByNomal();
//		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
//		mTestPaint.setTextAlign(Align.LEFT);// 左对齐
//		mTestPaint.setTextSize(Config.CONTENT_SIZE);// 字体大小
//		int contentLine=getLineNumByHaveTitle(strChapterList[0]);
//		strChapterListPosition=1;
//		try {
//			mChapterTestPosition=mChapterTestPosition+strChapterList[0].getBytes(mBook.getmStrCharsetName()).length+2;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		calcString(mTestPaint,strChapterList[strChapterListPosition],contentLine);
//		PagerList.get(PagerList.size()-1).setmEndPosition(mChapterEndPosition);
//		return PagerList;	
		if(mBook.getId()!=-1&&StringUtil.isEmptyString(mBook.getBookDir())){
			return getPagersByNet();
		}
		else{
			return getPagersByLocal();
		}
	}
	
	public ArrayList<Pager> getPagersByNet(){

		mChapterTestPosition=0;
		PagerList.clear();
		String strChapter = null;
//		//TODO 从内存找
//		//TODO 从缓存找
//		//TODO 从文件找
		if(!StringUtil.isEmptyString(getContent())){
			strChapter=getContent();
		}
		else{
			return null;
		}
			
		Pager mPager=new Pager(this);
		mPager.setmHaveTitle(true);
		mPager.setmStartPosition(mChapterStartPosition);
		PagerList.add(mPager);
		
	
		strChapterList=strChapter.split("\r\n|\n");
		
//		NOMAL_LINES=getLineNumByNomal();
		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
		mTestPaint.setTextAlign(Align.LEFT);// 左对齐
		mTestPaint.setTextSize(Config.CONTENT_SIZE);// 字体大小
		int contentLine=getLineNumByHaveTitle(getName());
		strChapterListPosition=0;
//		try {
//			mChapterTestPosition=mChapterTestPosition+strChapterList[0].getBytes(mBook.getmStrCharsetName()).length+2;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		calcString(mTestPaint,strChapterList[strChapterListPosition],contentLine);
		try {
			PagerList.get(PagerList.size()-1).setmEndPosition(strChapter.getBytes(mBook.getmStrCharsetName()).length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return PagerList;		
	}
	public ArrayList<Pager> getPagersByLocal(){
		mChapterTestPosition=mChapterStartPosition;
		PagerList.clear();
		String strChapter = null;
			byte[] Buf=mBook.readInPosition(mChapterStartPosition, mChapterEndPosition);
			try {
				 strChapter = new String(Buf, mBook.getmStrCharsetName());
			} catch (UnsupportedEncodingException e) {
				Log.e("", "pageUp->转换编码失败", e);
			}
		Pager mPager=new Pager(this);
		mPager.setmHaveTitle(true);
		mPager.setmStartPosition(mChapterStartPosition);
		PagerList.add(mPager);
		
	
		strChapterList=strChapter.split("\r\n|\n");
		
//		NOMAL_LINES=getLineNumByNomal();
		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
		mTestPaint.setTextAlign(Align.LEFT);// 左对齐
		mTestPaint.setTextSize(Config.CONTENT_SIZE);// 字体大小
		int contentLine=getLineNumByHaveTitle(strChapterList[0]);
		strChapterListPosition=1;
		try {
			mChapterTestPosition=mChapterTestPosition+strChapterList[0].getBytes(mBook.getmStrCharsetName()).length+2;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		calcString(mTestPaint,strChapterList[strChapterListPosition],contentLine);
		PagerList.get(PagerList.size()-1).setmEndPosition(mChapterEndPosition);
		return PagerList;	
		
	}
	

	
	
	/**
	 * 耦合太深不能单独使用
	 * 三种情况
	 * 1.第一种字符串刚好填满这一页，位置+字符串大小+2，下一次重新拿下一组字符串，不存在下一组字符串，停止
	 * 2.第二种字符串用尽了，还没填满这一页，位置+字符串大小+2，重新拿下一组字符串..重复这三种情况的选择,
	 * 直到不存在下一组字符串，停止
	 * 3.第三种字符串没用尽，取出剩余字符串，位置+字符串大小,字符串去到下一个页面...重复这三种情况的选择，
	 * 直到不存在下一组字符串，停止
	 * @param lineNum 还需要填多少行
	 * @return 0正常结束
	 */
	public void calcString(Paint mTestPaint,String s,int lineNum){
//		Log.e("calcString 1", "strChapterListPosition is "+strChapterListPosition+" sting is :"+s);
		
		int i=0;//记录这一页已经填的行数
		int stringLenght=0;
		while (s.length() == 0) {
			i++;
			if(i==lineNum){
					if(strChapterListPosition+1<strChapterList.length){
						mChapterTestPosition=mChapterTestPosition+stringLenght+2;
						Pager mPager=new Pager(this);
						mPager.setmStartPosition(mChapterTestPosition);
						PagerList.get(PagerList.size()-1).setmEndPosition(mChapterTestPosition);
						Log.e("calcString", "s.substring 1 EndPosition IS  "+mChapterTestPosition);
						PagerList.add(mPager);
						strChapterListPosition++;
						s=strChapterList[strChapterListPosition];
						i=0;
						lineNum=Config.NOMAL_LINES;
//						Log.e("calcString 2", "strChapterListPosition is "+strChapterListPosition+" sting is :"+s);
					}
			}
			else{
				mChapterTestPosition=mChapterTestPosition+stringLenght+2;
				if(strChapterListPosition+1<strChapterList.length){
					strChapterListPosition++;
					s=strChapterList[strChapterListPosition];
					Log.e("calcString 3", "s.substring 4 strChapterListPosition is "+strChapterListPosition+" sting is :"+s);
				}
			}
			
		}
		while (s.length() > 0) {
			// 测量一行能显示多少个字，并把文字分行
			int nSize = mTestPaint.breakText(s, true, Config.SCREEN_WIDTH-2*Config.MARGIN_WIDTH,null);
			try {
				stringLenght=stringLenght+s.substring(0, nSize).getBytes(mBook.getmStrCharsetName()).length;
				Log.e("calcString", "s.substring IS  "+s.substring(0, nSize));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			s = s.substring(nSize);
			i=i+1;
			if(i==lineNum){
				if(s.length()== 0){ //1.第一种字符串刚好填满这一页,而且还有下一组字符串
					if(strChapterListPosition+1<strChapterList.length){
						mChapterTestPosition=mChapterTestPosition+stringLenght+2;
						Pager mPager=new Pager(this);
						mPager.setmStartPosition(mChapterTestPosition);
						PagerList.get(PagerList.size()-1).setmEndPosition(mChapterTestPosition);
						Log.e("calcString", "s.substring EndPosition 2 IS  "+mChapterTestPosition+
								"lineNum is "+lineNum+"i is "+i);
						PagerList.add(mPager);
						strChapterListPosition++;
						calcString(mTestPaint,strChapterList[strChapterListPosition],Config.NOMAL_LINES);
					}
				}
				else{ //第三种字符串没用尽，取出剩余字符串
					mChapterTestPosition=mChapterTestPosition+stringLenght;
					Pager mPager=new Pager(this);
					mPager.setmStartPosition(mChapterTestPosition);
					PagerList.get(PagerList.size()-1).setmEndPosition(mChapterTestPosition);
					Log.e("calcString", "s.substring EndPosition 3 IS  "+mChapterTestPosition);
					PagerList.add(mPager);
					Log.e("calcString", "s.substring EndPosition 3 IS Config.NOMAL_LINES "+Config.NOMAL_LINES);
					calcString(mTestPaint,s,Config.NOMAL_LINES);
				}
				
			}
			if((s.length()== 0)&&(i<lineNum)){
				//第二种字符串用尽了，还没填满这一页，而且还有下一组字符串
				Log.e("calcString", "s.substring EndPosition 4 IS  "+mChapterTestPosition+
						"lineNum is "+lineNum+"i is "+i);
				mChapterTestPosition=mChapterTestPosition+stringLenght+2;
				if(strChapterListPosition+1<strChapterList.length){
					strChapterListPosition++;
					calcString(mTestPaint,strChapterList[strChapterListPosition],lineNum-i);
				}
			}
		}
		
		

	
//		s = s.replaceAll("\r\n", "");
//		s = s.replaceAll("\n", "");
//		// 如果是空白行，直接添加
//		
//		if (s.length() == 0) {
//			i++;
//		}
//		while (s.length() > 0) {
//			// 测量一行能显示多少个字，并把文字分行
//			int nSize = mTestPaint.breakText(s, true, Config.SCREEN_WIDTH-Config.MARGIN_WIDTH*2,null);
//			s = s.substring(nSize);
//			i++;
//		}
//		return i;
	}
	
	public  byte[] readInPosition(int start,int end) {
		return mBook.readInPosition(start, end);
	}


	/**
	 * 使用尺寸去测量这些文字会占用多少行
	 * @param s
	 * @param size
	 * @return
	 */
	public int getLineNumBySize(String s,int Size){
		
		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
		mTestPaint.setTextAlign(Align.LEFT);// 左对齐
		mTestPaint.setTextSize(Size);// 字体大小
		int i=0;
		s = s.replaceAll("\r\n", "");
		s = s.replaceAll("\n", "");
		// 如果是空白行，直接添加
		
		if (s.length() == 0) {
			i++;
		}
		while (s.length() > 0) {
			// 测量一行能显示多少个字，并把文字分行
			int nSize = mTestPaint.breakText(s, true, Config.SCREEN_WIDTH-2*Config.MARGIN_WIDTH,null);
			s = s.substring(nSize);
			i++;
		}
		return i;
	}
	
	public Vector<String> getLineListBySize(String s,int Size){
		Vector<String> Lines = new Vector<String>();
		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
		mTestPaint.setTextAlign(Align.LEFT);// 左对齐
		mTestPaint.setTextSize(Size);// 字体大小
		int i=0;
		s = s.replaceAll("\r\n", "");
		s = s.replaceAll("\n", "");
		// 如果是空白行，直接添加
		
		if (s.length() == 0) {
			Lines.add(s);
		}
		while (s.length() > 0) {
			// 测量一行能显示多少个字，并把文字分行
			int nSize = mTestPaint.breakText(s, true, Config.SCREEN_WIDTH-Config.MARGIN_WIDTH*2,null);
			Lines.add(s.substring(0, nSize));
			s = s.substring(nSize);
		}
		return Lines;
	}
	
	
//	public float getFontHeight(int size){
//		Paint mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
//		mTestPaint.setTextAlign(Align.LEFT);// 左对其
//		mTestPaint.setTextSize(size);// 字体大小
//		FontMetrics fm = mTestPaint.getFontMetrics();  
//		return (float) Math.ceil(fm.descent - fm.ascent);
//	}

	
	
//	public int getLineNumByNomal(){
//		return (int) ((Config.SCREEN_HEIGHT-Config.MARGIN_HEIGHT*2)/ getFontHeight(Config.CONTENT_SIZE)*1.6);
//		
//	}
	/**
	 * 
	 * @param s 标题文字
	 * @return 带标题的页面，正文会占用多少行
	 */
	public int getLineNumByHaveTitle(String s){
		
		int mTitleLine=getLineNumBySize(s, Config.TITLE_SIZE);
	 return (int) (((Config.SCREEN_HEIGHT-Config.MARGIN_HEIGHT*2)- ReaderUtil.getFontHeight(Config.TITLE_SIZE)*1.6*mTitleLine)/(ReaderUtil.getFontHeight(Config.CONTENT_SIZE)*1.6));
	}

	
}
