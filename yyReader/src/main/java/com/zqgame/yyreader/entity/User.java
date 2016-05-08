package com.zqgame.yyreader.entity;

import java.sql.Timestamp;

public class User {
	private int	id;		//用户Id
	private String username	;		//用户名
	private String nickname	;		//用户昵称
	private int sex;		//1-男；2-女	性别
	private String signature;		//	签名
	private String userImg	;		//头像
	private String city	;		//城市
	private Timestamp regTime;			///注册时间
	private Timestamp loginTime;	//0-正常  1-审核中	登录时间
	private int loginNum;			//连续登录天数
	private Timestamp logoutTime;		//0-正常  1-审核中	退出系统时间
	private int loginType	;	
	private int clientType	;	
	private String version;			//版本号
	private int onLine	;	//1-在线 0-不在线 2-忙碌	在线状态
     private 	int state;		//0-可用 1-冻结	账号状态
	private String imei;		////	设备码
	private int currency;		//	优币
	private int dynScore;		//	动态积分
	private int gradeScore;		//	等级积分
	private int grade	;		//用户等级
	private float coefficient;	//		积分加速系数
	private int ticket	;		//阅票
	private String phone;		//	手机号码
	private String email;		//	邮箱
	private int friendCount	;	//	我关注的数量
	private int fansCount;		//	粉丝数
	private Timestamp signInTime;	//		签到时间
	private int signInNum;			//连续登录天数
	private Timestamp commentTime;		//	签到时间
	private int commentNum	;		//连续登录天数
	private Timestamp shareTime	;	//	最后一次分享时间
	private int shareNum;			//当天累计分享次数
	private Timestamp examTime	;	//最后一次做题时间
	private int examNum	;	//	当天做题次数
	private int isFirLogin	;	//0是/1否	是否首次登陆(0是/1否)
	private int isFirUpLoad	;	//0是/1否	是否首次上传头像
	private String interestType;	//		喜爱书籍类型
	private Timestamp vipStartTime	;//		VIP开始时间
	private Timestamp vipEndTime;		//	VIP结束时间
	private int percentage	;	//百分比的整数	用户资料完善度
	private Timestamp blockeTime;//			冻结/解冻账户时间
	/**0是/1否	是否首次充值*/
	private int isFirRecharge;		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Timestamp getRegTime() {
		return regTime;
	}
	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	public int getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
	public Timestamp getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getOnLine() {
		return onLine;
	}
	public void setOnLine(int onLine) {
		this.onLine = onLine;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	public int getDynScore() {
		return dynScore;
	}
	public void setDynScore(int dynScore) {
		this.dynScore = dynScore;
	}
	public int getGradeScore() {
		return gradeScore;
	}
	public void setGradeScore(int gradeScore) {
		this.gradeScore = gradeScore;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public float getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getFriendCount() {
		return friendCount;
	}
	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}
	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public Timestamp getSignInTime() {
		return signInTime;
	}
	public void setSignInTime(Timestamp signInTime) {
		this.signInTime = signInTime;
	}
	public int getSignInNum() {
		return signInNum;
	}
	public void setSignInNum(int signInNum) {
		this.signInNum = signInNum;
	}
	public Timestamp getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public Timestamp getShareTime() {
		return shareTime;
	}
	public void setShareTime(Timestamp shareTime) {
		this.shareTime = shareTime;
	}
	public int getShareNum() {
		return shareNum;
	}
	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}
	public Timestamp getExamTime() {
		return examTime;
	}
	public void setExamTime(Timestamp examTime) {
		this.examTime = examTime;
	}
	public int getExamNum() {
		return examNum;
	}
	public void setExamNum(int examNum) {
		this.examNum = examNum;
	}
	public int getIsFirLogin() {
		return isFirLogin;
	}
	public void setIsFirLogin(int isFirLogin) {
		this.isFirLogin = isFirLogin;
	}
	public int getIsFirUpLoad() {
		return isFirUpLoad;
	}
	public void setIsFirUpLoad(int isFirUpLoad) {
		this.isFirUpLoad = isFirUpLoad;
	}
	public String getInterestType() {
		return interestType;
	}
	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}
	public Timestamp getVipStartTime() {
		return vipStartTime;
	}
	public void setVipStartTime(Timestamp vipStartTime) {
		this.vipStartTime = vipStartTime;
	}
	public Timestamp getVipEndTime() {
		return vipEndTime;
	}
	public void setVipEndTime(Timestamp vipEndTime) {
		this.vipEndTime = vipEndTime;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public Timestamp getBlockeTime() {
		return blockeTime;
	}
	public void setBlockeTime(Timestamp blockeTime) {
		this.blockeTime = blockeTime;
	}
	public int getIsFirRecharge() {
		return isFirRecharge;
	}
	public void setIsFirRecharge(int isFirRecharge) {
		this.isFirRecharge = isFirRecharge;
	}	
}
