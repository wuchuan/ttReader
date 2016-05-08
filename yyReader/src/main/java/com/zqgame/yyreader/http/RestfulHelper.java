package com.zqgame.yyreader.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Build;

import com.zqgame.yyreader.util.LogUtil;
import com.zqgame.yyreader.util.LogUtil.LOG_ENUM;

public class RestfulHelper {

	public enum RequestMethod {
		GET, POST, PUT, DELETE
	};
	
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient httpClient;

	private RestfulHelper() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			HttpParams params = new BasicHttpParams();
			
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, false);
			HttpConnectionParams.setTcpNoDelay(params, true);
	        HttpConnectionParams.setSocketBufferSize(params, 8192);
			//params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,true);
			HttpProtocolParams.setUserAgent(params, "Android "+ Build.VERSION.RELEASE + ";" + Build.MODEL);
	        //HttpProtocolParams.setUserAgent(params,"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");

			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 30000);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params,30*1000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 30*1000);
			
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 8002));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ThreadSafeClientConnManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);	
			//httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	/**
	 * 获取seesion
	 * 
	 * @param response
	 * @return
	 */
	public static String getSeesion() {
		List<Cookie> cockies=((AbstractHttpClient)httpClient).getCookieStore().getCookies();
		for (Cookie cookie : cockies) {
			if(cookie.getName().equals("JSESSIONID")){
				if(cookie.getPath().equals("/mbm/")){
					String seesion=cookie.getValue();
					return seesion;
				}
				
			}
		}
		return null;
	}

	public static String get(String url) throws IOException {
		int i =0;
		while(i<3){
			try {
				LogUtil.log("######：[get]:"+url);
				HttpGet request = new HttpGet(url);
				HttpClient client = getHttpClient();
				LogUtil.log("######：[get]:"+url+"请求发出！");
				HttpResponse response = client.execute(request);
				LogUtil.log("######：[get]:"+url+"请求返回！");
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					LogUtil.log("######：[get]:"+url+"返回码是："+response.getStatusLine().getStatusCode());
					throw new RuntimeException("请求失败,返回状态码"
							+ response.getStatusLine().getStatusCode());
				}
				HttpEntity resEntity = response.getEntity();
				if(resEntity == null) {
					LogUtil.log(LOG_ENUM.ERROR, "请求" + url + ",服务器返回数据为空！");
					return null;
				}else{
					String result = EntityUtils.toString(resEntity,CHARSET);
					resEntity.consumeContent();
					return result;
				}
			} catch (IOException e) {
				LogUtil.log("请求异常为："+e.getMessage());
				if(i>=3){
					throw e;
				}
			}
			i++;
		}
		return null;
	}

	public static String post(String url) throws IOException{
		int i = 0;
		while(i<3){
			try {
				LogUtil.log("######：[POST]:"+url);
				HttpPost request = new HttpPost(url);
				HttpClient client = getHttpClient();
				long time = System.currentTimeMillis();
				HttpResponse response = client.execute(request);
			
				time = System.currentTimeMillis() - time;
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					LogUtil.log("######：[POST]:"+url+"返回码是："+response.getStatusLine().getStatusCode());
					throw new RuntimeException("请求失败,返回状态码" + response.getStatusLine().getStatusCode());
				}
				HttpEntity resEntity = response.getEntity();
				if(resEntity == null) {
					LogUtil.log("######：[POST]:response is null");
					return null;
				}else{
					String result = EntityUtils.toString(resEntity,CHARSET);
					resEntity.consumeContent();
					return result;
				}
			} catch (IOException e) {
				LogUtil.log(e.getMessage());
				if(i>=3){
					throw e;
				}
			}
			i++;
		}
		return null;
	}
	

	
	
	public static String post(String url,Object parameters) throws IOException{
		String APPLICATION_JSON = "application/json";
		String CONTENT_TYPE_TEXT_JSON="text/json";
		int i = 0;
		while(i<3){
			try {
				LogUtil.log("######：[POST]:"+url);
				HttpPost request = new HttpPost(url);
				HttpEntity entity = null;
				if(parameters instanceof List){
					entity = new UrlEncodedFormEntity((List<NameValuePair>)parameters, "utf-8");
				}
				else if(parameters instanceof String){
					request.addHeader(HTTP.CONTENT_TYPE,APPLICATION_JSON);
					entity=new StringEntity((String)parameters, "utf-8");
					((StringEntity)entity).setContentType(CONTENT_TYPE_TEXT_JSON);
					((StringEntity)entity).setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
				}
				// 设置参数.
				request.setEntity(entity);
				HttpClient client = getHttpClient();
				long time = System.currentTimeMillis();
				HttpResponse response = client.execute(request);
			
				time = System.currentTimeMillis() - time;
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					throw new RuntimeException("请求失败,返回状态码" + response.getStatusLine().getStatusCode());
				}
				HttpEntity resEntity = response.getEntity();
				if(resEntity == null) {
					return null;
				}else{
					String result = EntityUtils.toString(resEntity,CHARSET);
					resEntity.consumeContent();
					return result;
				}
			} catch (IOException e) {
				LogUtil.log(e.getMessage());
				if(i>=3){
					throw e;
				}
			}
			i++;
		}
		return null;
	}
	
	
    public static synchronized String doFormPost(String url, List<NameValuePair> params){
        int count = 1;
        int i = 0;
        while (i < count) {
            try {
                LogUtil.log("######：[Form POST]:" + url);
                HttpPost postRequest = new HttpPost(url);
                postRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpClient client = RestfulHelper.getHttpClient();
                long time = System.currentTimeMillis();
                HttpResponse response = client.execute(postRequest);

                time = System.currentTimeMillis() - time;
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    //throw new RuntimeException("请求失败,返回状态码" + response.getStatusLine().getStatusCode());
                    throw new RuntimeException("请求失败,返回状态码" + response.getStatusLine().getStatusCode());
                }
                HttpEntity resEntity = response.getEntity();
                if (resEntity == null) {
                    return null;
                } else {
                    String result = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    resEntity.consumeContent();
                    return result;
                }
            } catch (IOException e) {
                LogUtil.log(e.getMessage());
                if (i >= count) {
                    try {
                        throw e;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            i++;
        }
        return null;
    }

	/**
	 * 销毁http客户端
	 */
	public static void deinit() {
		if (httpClient != null) {
			httpClient = null;
		}
	}

}
