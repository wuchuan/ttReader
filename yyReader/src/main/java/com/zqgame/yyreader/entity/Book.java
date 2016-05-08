package com.zqgame.yyreader.entity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zqgame.yyreader.Config;
import com.zqgame.yyreader.adapter.CatalogueAdapter;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.util.FileUtil;
import com.zqgame.yyreader.util.JsonUtil;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.StringUtil;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.widget.Toast;

public class Book {
	//需要保存数据库的
	private int id=-1;
	private String name;
	private String coverImg	;
	private String doctype;//文件格式类型(txt、word、pdf)
	private int wordCount;
	
	
	
	private  int type;//书的类型(1、电子书；2、实体书；3、电子和实体)
	private String introduce;	
	private String cateIds;		//书的类别集合
	private String author;	
	private String label;
	//private Time publishTime	;
	private String publisher;
	private String fileUrl;
	private int wordType;		//字数类型(1、短篇;2、中篇;3、长篇)
	private int isOver;	
	private int isFirPublish;
	private int isVip	;	//是否VIP权限
	private int isVote;		//是否可以投票改编
	private int isPresell;	//是否可以预售出版
	private int isFreeRead;		//是否可以免费试读
	private String freeChapter;	//可以免费试读的章节
	private double eBookPrice;		//电子书价格
	private double eBookDiscount;	 //电子书折扣
	private double paperPrice;		//纸质书价格
	private int paperDiscount;	//纸质书折扣
	private int isSigning;		//是否签约
	private String singinCompany;		//签约公司名称
	private int saleCount;		//销量
	private int downloadCount;		//下载数
	private int commentCount;		//评论数
	private int browseCount;	//浏览数
	private int rewardCount;	//打赏总额
	private int shareCount;		//分享数
	private int state;		//发布状态(0:未发布;1:已发布)
	
	private String readPos;
	
	private String bookDir;
	private String mStrCharsetName = "GBK";
	
	

	/**
	 * 取目录的位置，从0开始
	 */
	private int currentChapterNum = 0;

//	private long bookLenght;
	private long bookCurrentPosition = 0;

	private List<Chapter> ChapterList;
	private List<Maker> MakerList;

//	private boolean isFormNet=true;
	
	private File bookFile;

	private MappedByteBuffer mBookBuf;

	
	


//	public boolean isFormNet() {
//		return isFormNet;
//	}
//
//	public void setFormNet(boolean isFormNet) {
//		this.isFormNet = isFormNet;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getCateIds() {
		return cateIds;
	}

	public void setCateIds(String cateIds) {
		this.cateIds = cateIds;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	

	public String getReadPos() {
		return readPos;
	}

	public void setReadPos(String readPos) {
		this.readPos = readPos;
	}

	public int getWordType() {
		return wordType;
	}

	public void setWordType(int wordType) {
		this.wordType = wordType;
	}

	public int getIsOver() {
		return isOver;
	}
	

	public void setIsOver(int isOver) {
		this.isOver = isOver;
	}

	public int getIsFirPublish() {
		return isFirPublish;
	}

	public void setIsFirPublish(int isFirPublish) {
		this.isFirPublish = isFirPublish;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

	public int getIsVote() {
		return isVote;
	}

	public void setIsVote(int isVote) {
		this.isVote = isVote;
	}

	public int getIsPresell() {
		return isPresell;
	}

	public void setIsPresell(int isPresell) {
		this.isPresell = isPresell;
	}

	public int getIsFreeRead() {
		return isFreeRead;
	}

	public void setIsFreeRead(int isFreeRead) {
		this.isFreeRead = isFreeRead;
	}

	public String getFreeChapter() {
		return freeChapter;
	}

	public void setFreeChapter(String freeChapter) {
		this.freeChapter = freeChapter;
	}

	public double geteBookPrice() {
		return eBookPrice;
	}

	public void seteBookPrice(double eBookPrice) {
		this.eBookPrice = eBookPrice;
	}

	public double geteBookDiscount() {
		return eBookDiscount;
	}

	public void seteBookDiscount(double eBookDiscount) {
		this.eBookDiscount = eBookDiscount;
	}

	public double getPaperPrice() {
		return paperPrice;
	}

	public void setPaperPrice(double paperPrice) {
		this.paperPrice = paperPrice;
	}

	public int getPaperDiscount() {
		return paperDiscount;
	}

	public void setPaperDiscount(int paperDiscount) {
		this.paperDiscount = paperDiscount;
	}

	public int getIsSigning() {
		return isSigning;
	}

	public void setIsSigning(int isSigning) {
		this.isSigning = isSigning;
	}

	public String getSinginCompany() {
		return singinCompany;
	}

	public void setSinginCompany(String singinCompany) {
		this.singinCompany = singinCompany;
	}

	public int getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getRewardCount() {
		return rewardCount;
	}

	public void setRewardCount(int rewardCount) {
		this.rewardCount = rewardCount;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getBookDir() {
		return bookDir;
	}

	public void setBookDir(String bookDir) {
		this.bookDir = bookDir;
	}

//	public long getBookLenght() {
//		return bookLenght;
//	}
//
//	public void setBookLenght(long bookLenght) {
//		this.bookLenght = bookLenght;
//	}

	public int getCurrentChapterNum() {
		return currentChapterNum;
	}

	public void setCurrentChapterNum(int currentChapterNum) {
		this.currentChapterNum = currentChapterNum;
	}

	public List<Chapter> getChapterList() {
		return ChapterList;
	}

	public void setChapterList(List<Chapter> chapterList) {
		ChapterList = chapterList;
		if(ChapterList.size()>1&&ChapterList.get(0).getmBook()==null){
			for(Chapter c:ChapterList){
				c.setmBook(this);
			}
			
		}
	}

	public List<Maker> getMakerList() {
		return MakerList;
	}

	public void setMakerList(List<Maker> makerList) {
		MakerList = makerList;
	}



	// 操作方法

	public long getBookCurrentPosition() {
		return bookCurrentPosition;
	}

	public void setBookCurrentPosition(long bookCurrentPosition) {
		this.bookCurrentPosition = bookCurrentPosition;
	}

	public String getmStrCharsetName() {
		return mStrCharsetName;
	}

	public void setmStrCharsetName(String mStrCharsetName) {
		this.mStrCharsetName = mStrCharsetName;
	}

	/**
	 * 1.打开文件 2. 2.提取目录，位置 3.提取书签（本地） 4.确定书籍的当前位置并显示
	 * 
	 * @param FlieDir
	 */
	public void openBook(String FlieDir) {
		setBookDir(FlieDir);
		bookFile = new File(FlieDir);
		long lLen = bookFile.length();
		wordCount = (int) lLen;
		try {
			mBookBuf = new RandomAccessFile(bookFile, "r").getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, lLen);
			mStrCharsetName = codeString(FlieDir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Config.dirMap.get(FlieDir) != null) {
			setChapterList(Config.dirMap.get(FlieDir));
		} else {
			setChapterList(getDir(FlieDir));
		}
		setMakerList(getMaker());

		assignmentIndex();

		// displayCureentPager();

	}
	private List<Maker> getMaker() {
		// 本地数据库提取书签
		// 网络提取书签
		// 对比书签，并上传不同的书签
		getLocalMaker();
		getNetMaker();

		return null;
	}

	private List<Maker> getNetMaker() {
		return null;

	}

	private List<Maker> getLocalMaker() {
		return null;

	}

	/**
	 * 读取以前读取到的位置， 并转化成那一章那一节
	 */
	private void assignmentIndex() {
		// TODO 读取位置已经读到的位置
		int index = -1;
		for (int i = 0; i < ChapterList.size(); i++) {
			if (ChapterList.get(i).getmChapterStartPosition() > bookCurrentPosition) {
				index = i;
				break;
			}
			if (index > 0) {
				currentChapterNum = index - 1;
			} else {
				currentChapterNum = ChapterList.size() - 1;
			}
		}
	}

	public void closeBook() {

	}

	/**
	 * 展示当前页
	 * @return -1：正常显示 -2：到了第一章第一页 
	 * else：从零开始计算的章节 即将要请求的章节
	 */
	public int  displayCureentPager(Canvas c) {
		
		if(StringUtil.isEmptyString(getChapterList().get(currentChapterNum).getContent())
				&&id!=-1&&StringUtil.isEmptyString(getBookDir())){
			//TODO 从内存找
			//TODO 从缓存找
			//TODO 从文件找
//			getBookChapter(id, currentChapterNum+1,1,c);
//			getBookChapter(1, currentChapterNum+1,1,c);
			return currentChapterNum;
		}
		getChapterList().get(currentChapterNum).getCurrentPager()
				.onChangeDraw(c);
		setBookCurrentPosition(getChapterList().get(currentChapterNum)
				.getCurrentPager().getmStartPosition());
		return -1;
	}

	/**
	 * @param c
	 * @return -1：正常显示 -2：到了最后一章最后一页 
	 * 		else：从零开始计算的章节 即将要请求的章节
	 */
	public int  displayNextPager(Canvas c) {
		
	
		// 首先判断是否是这章的最后一页,不是的话前进一页
		// 是的话，看是不是最后一章，最后一章的话，不动，不是的话加一章
		if (!getChapterList().get(currentChapterNum).isLastPager()) {
			
			int pos = getChapterList().get(currentChapterNum)
					.getCurrentPagerPosition();
			getChapterList().get(currentChapterNum).setCurrentPagerPosition(
					pos + 1);
		} else {
			if (!isLastChapter()) {
				getChapterList().get(currentChapterNum+1).setCurrentPagerPosition(0);
				if(StringUtil.isEmptyString(getChapterList().get(currentChapterNum+1).getContent())
				&&id!=-1&&StringUtil.isEmptyString(getBookDir())){
					//TODO 从内存找
					//TODO 从缓存找
					//TODO 从文件找
//					getBookChapter(id, currentChapterNum+1,2,c);
//					getBookChapter(1, currentChapterNum+1,2,c);
					return currentChapterNum+1;
				}
				else{
					currentChapterNum++;
				}
			} else {
				return -2;
			}
		}
		displayCureentPager(c);
		return -1;

	}

	/**
	 * 
	 * @param c
	 * @return -1：正常显示 -2：到了第一章第一页 else：从零开始计算的章节
	 */
	public int displayPrePager(Canvas c) {
		if (!getChapterList().get(currentChapterNum).isFristPager()) {
			int pos = getChapterList().get(currentChapterNum)
					.getCurrentPagerPosition();
			getChapterList().get(currentChapterNum).setCurrentPagerPosition(
					pos - 1);
		} else {
			if (!isFristChapter()) {
				currentChapterNum--;
				if(StringUtil.isEmptyString(getChapterList().get(currentChapterNum).getContent())
						&&id!=-1&&StringUtil.isEmptyString(getBookDir())){
					//TODO 从内存找
					//TODO 从缓存找
					//TODO 从文件找
//					getBookChapter(id, currentChapterNum+1,0,c);
//					getBookChapter(1, currentChapterNum+1,0,c);
					return currentChapterNum;
					
				}
				int pos = getChapterList().get(currentChapterNum)
						.getPagerList().size();
				getChapterList().get(currentChapterNum)
						.setCurrentPagerPosition(pos - 1);
			} else {
				return -2;
			}

		}
		displayCureentPager(c);
		return -1;

	}
	/**
	 * 
	 * @param c
	 * @return -1：正常显示 -2：到了第一章第一页 else：从零开始计算的章节
	 */
	public int displayPreChapter(Canvas c) {
			if (!isFristChapter()) {
				currentChapterNum--;
				if(StringUtil.isEmptyString(getChapterList().get(currentChapterNum).getContent())
						&&id!=-1&&StringUtil.isEmptyString(getBookDir())){
					//TODO 从内存找
					//TODO 从缓存找
					//TODO 从文件找
//					getBookChapter(id, currentChapterNum+1,0,c);
//					getBookChapter(1, currentChapterNum+1,0,c);
					return currentChapterNum;
				}
				int pos = getChapterList().get(currentChapterNum)
						.getPagerList().size();
				getChapterList().get(currentChapterNum)
						.setCurrentPagerPosition(0);
			} 
			else{
				return -2;
			}

		displayCureentPager(c);
		return -1;

	}
	public int displayNextChapter(Canvas c) {
			if (!isLastChapter()) {
				currentChapterNum++;
				if(StringUtil.isEmptyString(getChapterList().get(currentChapterNum).getContent())
						&&id!=-1&&StringUtil.isEmptyString(getBookDir())){
					//TODO 从内存找
					//TODO 从缓存找
					//TODO 从文件找
//					getBookChapter(id, currentChapterNum+1,0,c);
//					getBookChapter(1, currentChapterNum+1,0,c);
					return currentChapterNum;
					
				}
				int pos = getChapterList().get(currentChapterNum)
						.getPagerList().size();
				getChapterList().get(currentChapterNum)
						.setCurrentPagerPosition(0);
			} else {
				return -2;
			}
		displayCureentPager(c);
		return -1;

	}

	public boolean isFristChapter() {
		if (currentChapterNum == 0) {
			return true;
		}
		return false;
	}

	public boolean isLastChapter() {
		if (ChapterList == null) {
			setChapterList(getDir(getBookDir()));
		}
		if (currentChapterNum < ChapterList.size() - 1) {
			return false;
		}
		return true;
	}

	public void refreshFontSize() {
		ArrayList<Pager> pList = getChapterList().get(currentChapterNum)
				.getPagers();
		for (int i = 0; i < pList.size(); i++) {
			if (bookCurrentPosition < pList.get(i).getmStartPosition()) {
				getChapterList().get(currentChapterNum)
						.setCurrentPagerPosition(i - 1);
				break;
			}
		}

	}

	// 工具方法

	public List<Chapter> getDir(String filepath) {
		int position = 0;
		Pattern pattern = Pattern
				.compile("[\u4e00-\u9fa5]{0,}第[0-9,零,一,二,三,四,五,六,七,八,九,十,百,千]{1,}[章,节,卷,回][ ,?,!,(,),\u4e00-\u9fa5]{0,}");
		ArrayList<Chapter> dir = new ArrayList<Chapter>();
		// File f = new File(filepath);
		try {
			FileInputStream fInputStream = new FileInputStream(bookFile);
			// code为上面方法里返回的编码方式
			InputStreamReader inputStreamReader;

			inputStreamReader = new InputStreamReader(fInputStream,
					mStrCharsetName);

			BufferedReader in = new BufferedReader(inputStreamReader);

			String strTmp = "";
			// 按行读取

			while ((strTmp = in.readLine()) != null) {
				if ((strTmp.length() > 0) && strTmp.contains("第")) {
					Matcher matcher = pattern.matcher(strTmp);
					if (matcher.matches()) {
						Chapter c = new Chapter(this);
						c.setName(strTmp);
						c.setmChapterStartPosition(position);
						c.setChapterId(dir.size());
						dir.add(c);
						if (dir.size() > 1) {
							dir.get(dir.size() - 2).setmChapterEndPosition(
									position);
						}
					}
				}
				position = position + strTmp.getBytes(mStrCharsetName).length
						+ 2;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Config.dirMap.put(filepath, dir);
		return dir;

	}

	/**
	 * 获取文件的编码格式
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private String codeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
				fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			// code = "GBK";
			code = "GB18030";
		}

		return code;
	}

	/**
	 * 读取指定位置的byte
	 * 
	 * @param start
	 *            开始的位置
	 * @param end
	 *            结束的位置
	 * @return
	 */

	public byte[] readInPosition(int start, int end) {

		int nParaSize = end - start + 1;// 直接减会少一位
		byte[] buf = new byte[nParaSize];
		for (int i = 0; i < nParaSize; i++) {
			buf[i] = mBookBuf.get(start + i);
		}
		return buf;
	}

	/**
	 * 读取指定位置的下一个段落
	 * 
	 * @param nFromPos
	 * @return byte[]
	 */
	protected byte[] readParagraphForward(int nFromPos) {
		// Log.e(TAG, "pageDown() readParagraphForward");
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (mStrCharsetName.equals("UTF-16LE")) {
			while (i < wordCount - 1) {
				b0 = mBookBuf.get(i++);
				b1 = mBookBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (mStrCharsetName.equals("UTF-16BE")) {
			while (i < wordCount - 1) {
				b0 = mBookBuf.get(i++);
				b1 = mBookBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < wordCount) {
				b0 = mBookBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = mBookBuf.get(nFromPos + i);
		}
		return buf;
	}

	/**
	 * 读取指定位置的size个文字
	 * 
	 * @param fileName
	 * @param start
	 * @param size
	 * @return
	 */
	private String readString(String fileName, int start, int size) {
		String rueult = null;
		File f = new File(fileName);

		// FileInputStream in=new FileInputStream(f);

		FileInputStream fInputStream;
		try {
			fInputStream = new FileInputStream(f);
			InputStreamReader inputStreamReader;
			inputStreamReader = new InputStreamReader(fInputStream,
					codeString(fileName));
			BufferedReader in = new BufferedReader(inputStreamReader);
			char[] buffer = new char[size];
			int ret = in.read(buffer, start, size);
			if (ret == -1) {
				rueult = null;
			} else if (ret == size) {
				rueult = new String(buffer);
			} else {
				char[] tmp = new char[ret];
				for (int i = 0; i < ret; i++) {
					tmp[i] = buffer[i];
				}
				rueult = new String(tmp);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rueult;

	}
	/**
	 * 
	 * @param id
	 * @param chapterId
	 * @param type 0:前一章 1：当前章 2：后一章
	 */
//	@SuppressLint("NewApi")
//	public void  getBookChapter(int id,final int chapterId,final int type,final Canvas c){
//			String url = Url.getUri(Url.POST_BOOK_CHAPTER);
//			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//			parameters.add(new BasicNameValuePair("bookId", id + "")); 
//			parameters.add(new BasicNameValuePair("chapterId", chapterId + "")); 
//			parameters.add(new BasicNameValuePair("token",""));
//			ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
//					new ReadAsyncTaskCallBack() {
//						@Override
//						public void preRequest() {
//						}
//						@Override
//						public void handleNetWorkData(String result, String flagStr) {
//							if(JsonUtil.getCode(result)==200){
//							if(JsonUtil.getObject(result, "chapter")!=null){
//								Chapter mChapter = (Chapter) JsonUtil.json2bean(JsonUtil.getObject(result, "chapter").toString(), Chapter.class);
//								if(mChapter!=null){
//									getChapterList().get(chapterId-1).setContent(mChapter.getContent());
//									FileUtil.getValidCachePath()
//									//TODO 放置到缓存，还是放置到文件？？？
//								switch(type){
//								case 0:
//									int pos = getChapterList().get(currentChapterNum).getPagerList().size();
//									getChapterList().get(currentChapterNum).setCurrentPagerPosition(pos - 1);
//									break;
//								case 1:
//									
//									break;
//								case 2:
//									
//									break;
//								}
//								displayCureentPager(c);	
//									
//								}
//							}
//							}
//							else{
//							}
//						}
//					});
//			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
//	}	

}
