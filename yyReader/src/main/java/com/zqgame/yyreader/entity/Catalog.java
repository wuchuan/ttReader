package com.zqgame.yyreader.entity;

public class Catalog {
	private int id; // 章节Id
	private int bookId; // 书籍Id
	private String name; // 章节名称
	private int wordType;// 字数类型(1、短篇;2、中篇;3、长篇)
	private int wordCount; // 字数
	private double price; // 电子书价格
	private double discount; // 电子书折扣
	private int isVip; // 是否是VIP章节
	private double vipPrice; // VIP价格
	private int vipDiscount; // VIP折扣
	private int isFree;// 是否免费
	private int browseCount; // 浏览数
	private int state;// 发布状态(0:未发布;1:已发布)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWordType() {
		return wordType;
	}

	public void setWordType(int wordType) {
		this.wordType = wordType;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

	public double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public int getVipDiscount() {
		return vipDiscount;
	}

	public void setVipDiscount(int vipDiscount) {
		this.vipDiscount = vipDiscount;
	}

	public int getIsFree() {
		return isFree;
	}

	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
