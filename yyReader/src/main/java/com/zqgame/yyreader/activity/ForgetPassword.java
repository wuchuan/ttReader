package com.zqgame.yyreader.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zqgame.yyreader.MainActivity;
import com.zqgame.yyreader.R;
import com.zqgame.yyreader.http.ReadAsyncTask;
import com.zqgame.yyreader.http.ReadAsyncTaskCallBack;
import com.zqgame.yyreader.http.Url;
import com.zqgame.yyreader.http.RestfulHelper.RequestMethod;
import com.zqgame.yyreader.util.JsonUtil;
import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPassword extends Activity implements OnClickListener {

	private EditText forget_num;
	private EditText forget_sms;
	private Button forget_send_sms;
	private EditText forget_pwd;
	private EditText forget_sure_pwd;
	private Button forget_into;
	private String Key;
	private ImageView forget_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		initView();
	}

	private void initView() {
		forget_back=(ImageView)findViewById(R.id.forget_back);
		forget_num = (EditText) findViewById(R.id.forget_num);
		forget_sms = (EditText) findViewById(R.id.forget_sms);
		forget_send_sms = (Button) findViewById(R.id.forget_send_sms);
		forget_pwd = (EditText) findViewById(R.id.forget_pwd);
		forget_sure_pwd = (EditText) findViewById(R.id.forget_sure_pwd);
		forget_into = (Button) findViewById(R.id.forget_into);

		forget_send_sms.setOnClickListener(this);
		forget_into.setOnClickListener(this);
		forget_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_into:
			if(StringUtil.filterPhone(forget_num.getText().toString().trim())){
				if(!StringUtil.isEmptyString(forget_sms.getText().toString().trim())){
					if(!StringUtil.isEmptyString(forget_pwd.getText().toString().trim())&&
							!StringUtil.isEmptyString(forget_pwd.getText().toString().trim())){
						if(forget_pwd.getText().toString().trim().equals(
								forget_pwd.getText().toString().trim())){
							resetPassWord();
						}
						else{
							Toast.makeText(this, "输入的密码不一致！", Toast.LENGTH_SHORT).show();
						}
						
					}
					else{
						Toast.makeText(this, "输入的密码格式不对！", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(this, "输入的验证码格式不对！", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this, "输入的手机号码格式不对！", Toast.LENGTH_SHORT).show();
			}
				resetPassWord();
			break;
		case R.id.forget_send_sms:
			if(StringUtil.filterPhone(forget_num.getText().toString().trim())){
				send_sms(forget_num.getText().toString().trim());
			}
			else{
				Toast.makeText(this, "输入的输入的手机号码格式不对！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.forget_back:
			finish();
			break;
		}

	}
	
	@SuppressLint("NewApi")
	private void send_sms(String telphone){
		
		String url = Url.getUri(Url.POST_GET_SMS);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("telphone", telphone));
		parameters.add(new BasicNameValuePair("type", 2+""));	
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {}
					@Override
					public void handleNetWorkData(String result, String flagStr) {
						LogUtil.log("result is :" + result);
						if(JsonUtil.getCode(result)==200){
							Key=JsonUtil.getObject(result, "keys").toString();
						}
						else{
							Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}
	
	@SuppressLint("NewApi")
	private void resetPassWord(){
		String url = Url.getUri(Url.POST_RESETPWD);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("keys", Key));
		parameters.add(new BasicNameValuePair("telphone", forget_num.getText().toString().trim()));	
		parameters.add(new BasicNameValuePair("validCode", forget_sms.getText().toString().trim()));	
		parameters.add(new BasicNameValuePair("newPwd", forget_pwd.getText().toString().trim()));	
		parameters.add(new BasicNameValuePair("reNewPwd", forget_sure_pwd.getText().toString().trim()));
		ReadAsyncTask task = new ReadAsyncTask(RequestMethod.POST, parameters,
				new ReadAsyncTaskCallBack() {
					@Override
					public void preRequest() {}
					@Override
					public void handleNetWorkData(String result, String flagStr) {
						LogUtil.log("result is :" + result);
						if(JsonUtil.getCode(result)==200){
							Toast.makeText(getApplicationContext(), "重置成功！", Toast.LENGTH_SHORT).show();
							Intent mIntent=new Intent(getApplicationContext(), LoginActity.class);
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
