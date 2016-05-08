package com.zqgame.yyreader.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText register_num;
	private EditText register_sms;
	private EditText register_pwd;
	private Button register_send_sms;
	private CheckBox register_cb_agree;
	private TextView register_agreement;
	private Button register_into;
	
	private String Sms="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		initView();
	}

	private void initView() {
		register_num=(EditText)findViewById(R.id.register_num);
		register_sms=(EditText)findViewById(R.id.register_sms);
		register_pwd=(EditText)findViewById(R.id.register_pwd);
		register_send_sms=(Button)findViewById(R.id.register_send_sms);
		register_cb_agree=(CheckBox)findViewById(R.id.register_cb_agree);
		register_agreement=(TextView)findViewById(R.id.register_agreement);
		register_into=(Button)findViewById(R.id.register_into);
		register_send_sms.setOnClickListener(this);
		register_into.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.register_send_sms:
			if(StringUtil.filterPhone(register_num.getText().toString().trim())){
				send_sms(register_num.getText().toString().trim());
			}
			else{
				Spanned html = Html.fromHtml("<font color='red'>输入的格式不正确！</font>");
				register_num.setError(html);
			}
			break;
		case R.id.register_into:
			if(!StringUtil.isEmptyString(Sms)&&
					!StringUtil.isEmptyString(register_sms.getText().toString().trim())
					&&!StringUtil.isEmptyString(register_pwd.getText().toString().trim())
					){
				register(register_num.getText().toString().trim());
			}
			else{
				
			}
			break;
		}
		
	}
	
	
	@SuppressLint("NewApi")
	private void send_sms(String telphone){
		
		String url = Url.getUri(Url.POST_GET_SMS);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("telphone", telphone));
		parameters.add(new BasicNameValuePair("type", 1+""));	
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {
						
					}

					@Override
					public void handleNetWorkData(String result, String flagStr) {
						LogUtil.log("result is :" + result);
						if(JsonUtil.getCode(result)==200){
							Sms=JsonUtil.getObject(result, "keys").toString();
						}
						else{
							Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}
	
	@SuppressLint("NewApi")
	private void register(String telphone){
		
		String url = Url.getUri(Url.POST_REGISTER);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("keys", Sms));
		parameters.add(new BasicNameValuePair("username", telphone));	
		parameters.add(new BasicNameValuePair("password", register_pwd.getText().toString().trim()));	
		parameters.add(new BasicNameValuePair("validCode", register_sms.getText().toString().trim()));	
		parameters.add(new BasicNameValuePair("nickname", "sb胡云"));	
		parameters.add(new BasicNameValuePair("sex", 2+""));	
		parameters.add(new BasicNameValuePair("signature", "傻逼的世界无人理解"));	
		parameters.add(new BasicNameValuePair("loginType","5"));	
		parameters.add(new BasicNameValuePair("clientType", 1+""));	
		parameters.add(new BasicNameValuePair("version", PropertyUtil.getVersionName(this)));	
		parameters.add(new BasicNameValuePair("imei", PropertyUtil.getImei(this)));		
		parameters.add(new BasicNameValuePair("userImg", "xxxxx"));	
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
							Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
							startActivity(mIntent);
						}
						else{
							Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}
	
	

}
