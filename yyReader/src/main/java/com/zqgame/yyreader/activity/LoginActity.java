package com.zqgame.yyreader.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zqgame.yyreader.MainActivity;
import com.zqgame.yyreader.R;
import com.zqgame.yyreader.entity.ReaderSession;
import com.zqgame.yyreader.entity.User;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.util.JsonUtil;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.PropertyUtil;
import com.zqgame.yyreader.util.ReaderUtil;
import com.zqgame.yyreader.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller.Session;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private EditText login_num;
	private EditText login_pwd;
	private CheckBox login_cb_rmbpwd;
	private TextView login_forgetpwd;
	private ImageView login_weibo;
	private ImageView login_weixin;
	private ImageView login_qq;
	private Button login_into;
	private Button login_register;
	private TextView login_look_around;
	private UMShareAPI mShareAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		mShareAPI = UMShareAPI.get(this);
		initView();
	}

	private void initView() {
		login_num = (EditText) findViewById(R.id.login_num);
		login_pwd = (EditText) findViewById(R.id.login_pwd);
		login_cb_rmbpwd = (CheckBox) findViewById(R.id.login_cb_rmbpwd);
		login_forgetpwd = (TextView) findViewById(R.id.login_forgetpwd);
		
		login_weibo = (ImageView) findViewById(R.id.login_weibo);
		login_weixin = (ImageView) findViewById(R.id.login_weixin);
		login_qq = (ImageView) findViewById(R.id.login_qq);
		login_into = (Button) findViewById(R.id.login_into);
		login_register = (Button) findViewById(R.id.login_register);
		login_look_around = (TextView) findViewById(R.id.login_look_around);
		
		login_look_around.setOnClickListener(this);
		login_cb_rmbpwd.setOnCheckedChangeListener(this);

		login_into.setOnClickListener(this);
		login_register.setOnClickListener(this);
		login_forgetpwd.setOnClickListener(this);

		login_weibo.setOnClickListener(this);
		login_weixin.setOnClickListener(this);
		login_qq.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent mIntent;
		switch (v.getId()) {
		case R.id.login_weibo:
			loginByThrid(SHARE_MEDIA.SINA);
			break;
		case R.id.login_qq:
			loginByThrid(SHARE_MEDIA.QQ);
			break;
		case R.id.login_weixin:
			loginByThrid(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.login_into:
			CharSequence html;
			if(StringUtil.filterPhone(login_num.getText().toString().trim())){
				if(!StringUtil.isEmptyString(login_pwd.getText().toString().trim())){
					login(login_num.getText().toString().trim(),login_pwd.getText().toString().trim(),5);
				}
				else{
					 html = Html.fromHtml("<font color='red'>请输入密码!</font>");
					login_pwd.setError(html);
				}
			}
			else{
				 html = Html.fromHtml("<font color='red'>输入的格式不正确！</font>");
				login_num.setError(html);
			}
			
			break;
		case R.id.login_register:
			mIntent=new Intent(getApplicationContext(), RegisterActivity.class);
			startActivity(mIntent);
			break;
		case R.id.login_look_around:
			mIntent=new Intent(getApplicationContext(), MainActivity.class);
			startActivity(mIntent);
			break;
		case R.id.login_forgetpwd:
			mIntent=new Intent(getApplicationContext(), ForgetPassword.class);
			startActivity(mIntent);
			break;

		}

	}
	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @param loginType 1：QQ 2：微信 3：微博 4：邮箱 5：手机号
	 */
	@SuppressLint("NewApi")
	private void login(String userName,String pwd,int loginType ){
		String url = Url.getUri(Url.POST_LOGIN);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username", userName));
		parameters.add(new BasicNameValuePair("password", pwd));
		parameters.add(new BasicNameValuePair("loginType", loginType+""));
		parameters.add(new BasicNameValuePair("clientType", 1+""));
		parameters.add(new BasicNameValuePair("imei", PropertyUtil.getImei(this)));
		parameters.add(new BasicNameValuePair("version",  PropertyUtil.getVersionName(this)));	
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {}
					@Override
					public void handleNetWorkData(String result, String flagStr) {
						LogUtil.log("result is :" + result);
						if(JsonUtil.getCode(result)==200){
							User u=(User)JsonUtil.json2bean(JsonUtil.getObject(result, "user").toString(), User.class);
							ReaderSession.user=u;
							Intent mIntent=new Intent(getApplicationContext(), MainActivity.class);
							startActivity(mIntent);
							finish();
						}
						else{
							Toast.makeText(getApplicationContext(), "登录失败！请确认填写信息是否正确！", Toast.LENGTH_SHORT).show();
						}
					}
				});
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}

	@Override
	public void onCheckedChanged(CompoundButton cb, boolean stuta) {	
	}
	
	
	
	//第三方登录
	public void loginByThrid(SHARE_MEDIA platform){
		mShareAPI.doOauthVerify(this, platform, umAuthListener);
	}
	
	private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> info) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            StringBuilder sb = new StringBuilder();
            Set<String> keys = info.keySet();
            for(String key : keys){
               sb.append(key+"="+info.get(key).toString()+"\r\n");
            }
            Log.d("TestData",sb.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 
	 * @param uid 
	 * @param token
	 * @param sex 1-男 2-女
	 * @param loginType 1：QQ 2：微信 3：微博 4：邮箱 5：手机号
	 * @param userImg
	 * @param nickName
	 */
	private void loginByThree(String uid,String token, int sex,int loginType,String userImg,String nickName) {
		String url = Url.getUri(Url.POST_LOGIN_CHANNEL);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("uid", uid));
		parameters.add(new BasicNameValuePair("token", token));
		parameters.add(new BasicNameValuePair("nickname", nickName));
		parameters.add(new BasicNameValuePair("userImg", userImg));
		parameters.add(new BasicNameValuePair("sex", sex+""));
		parameters.add(new BasicNameValuePair("loginType", loginType+""));
		parameters.add(new BasicNameValuePair("clientType", 1+""));
		parameters.add(new BasicNameValuePair("imei",PropertyUtil.getImei(this) ));
		parameters.add(new BasicNameValuePair("version", PropertyUtil.getVersionName(this)));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {}
					@Override
					public void handleNetWorkData(String result, String flagStr) {
						if(JsonUtil.getCode(result)==200){
							User u=(User)JsonUtil.json2bean(JsonUtil.getObject(result, "user").toString(), User.class);
							ReaderSession.user=u;
							Intent mIntent=new Intent(getApplicationContext(), MainActivity.class);
							startActivity(mIntent);
							finish();
						}
						else{
							Toast.makeText(getApplicationContext(), "登录失败!", Toast.LENGTH_SHORT).show();
						}
					}
				});

		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

	}
	
    
    
}
