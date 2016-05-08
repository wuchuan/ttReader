package com.zqgame.yyreader.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;

import android.widget.EditText;

public class StringUtil {

	/**
	 * 判断是否为null或空字符串
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 是否为空或空串
	 */
	public static boolean isEmptyString(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
	/**
	 * 验证输入的用户名是否合法 错误，会弹出提示框
	 * 
	 * @return null-合法 !null-不合法
	 */
	public static String isValidateEmail(EditText view) {
		String emailStr = view.getText().toString().trim();
		String toastStr = isValidateEmail(emailStr);
		return toastStr;
	}
	/**
	 * 验证输入的用户名是否为电话号码的正确格式
	 * @param str
	 * @return
	 */
	public static boolean filterPhone(String str) {
		// 只允许字母和数字和下划线
		String regEx = "^[1][0-9]{10}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}



	/**
	 * 判断是否为合法email
	 * 
	 * @param emailStr
	 * @return 返回错误原因的str，为null则表示email为合法字段
	 */
	public static String isValidateEmail(String emailStr) {
		String toastStr = null;
		if (isEmptyString(emailStr)) {
			toastStr = "请输入电子邮箱";
		} else if (emailStr.length() > 60) {
			toastStr = "邮箱地址长度不能超过60位";
		} else if (!Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+")
				.matcher(emailStr).matches()) {
			toastStr = "电子邮箱格式不正确，请重新输入";
		}
		return toastStr;
	}

	/**
	 * 验证输入的密码是否合法
	 * 
	 * @return null-合法 !null-不合法
	 */
	public static String isValidatePwd(EditText view) {
		String toastStr = null;
		String pwdString = view.getText().toString();
		if (isEmptyString(pwdString)) {
			toastStr = "请输入密码";
		} else if (pwdString.length() < 6 || pwdString.length() > 30) {
			toastStr = "密码长度必须介于6~30位";
		} else if (pwdString.contains(" ")) {
			toastStr = "密码不允许有空格";
		}


		return toastStr;
	}

	/**
	 * 验证输入的用户名是否合法
	 * 
	 * @return null-合法 !null-不合法
	 */
	public static String isValidateUname(EditText view) {
		String toastStr = null;
		String unameString = view.getText().toString().trim();

		if (isEmptyString(unameString)) {
			toastStr = "请输入用户名";
		} else if (unameString.length() < 1 || unameString.length() > 10) {
			toastStr = "用户名长度必须介于1~10个字符";
		} else if (isContainInvalidChar(unameString)) {
			toastStr = "用户名格式不正确，必须为中文、字母、数字或下划线组合";
		}

		return toastStr;
	}

	/**
	 * 验证输入的签名是否合法
	 * 
	 * @return null-合法 !null-不合法
	 */
	public static String isValidateSign(EditText view) {
		String toastStr = null;
		String signString = view.getText().toString().trim();

		if (signString.length() > 30) {
			toastStr = "签名长度必须小于30个字符";
		} else if (isContainInvalidChar(signString)) {
            // removed by zzj, start
            // 根据v4.5版本需求中的4.7，将签名设置成所有字符
            // toastStr = "签名格式不正确，必须为中文、字母、数字或下划线组合";
            // removed by zzj, end
		}

		return toastStr;
	}

	/**
	 * 字符串是否包含非法字符（字母、数字、下划线以外的字符）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainInvalidChar(String str) {
		boolean result = false;
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (!Character.isLetterOrDigit(c) && '_' != c) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 使用字节数组生成一个utf8字符串
	 * 
	 * @param strByte
	 *            字节数组
	 * @return utf8字符串或null
	 */
	public static String newStringUtf8(byte[] strByte) {
		String result = null;
		try {
			result = new String(strByte, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 使用字符串生成一个utf8字节数组
	 * 
	 * @param pArray
	 *            字符串
	 * @return utf8字节数组或null
	 */
	public static byte[] getBytesUtf8(String pArray) {
		byte[] strByte = null;
		try {
			strByte = pArray.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strByte;
	}

	/**
	 * 将字符串进行转码
	 * 
	 * @param str
	 *            将要转码的字符串
	 * 
	 * @return 转码后的字符串
	 */
	public static String convert2Utf8(String str) {
		String resultString = "";
		if (!isEmptyString(str)) {
			try {
				resultString = URLEncoder.encode(str, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	/**
	 * 转换为utf8后的字符个数
	 * 
	 * @param str
	 * @return
	 */
	public static int countUtf8Length(String str) {
		int result = 0;
		if (!isEmptyString(str)) {
			int count = 0;
			String regEx = "[\\u4e00-\\u9fa5]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			while (m.find()) {
				for (int i = 0; i <= m.groupCount(); i++) {
					count++;
				}
			}
			result = count * 2 + str.length();
		}
		return result;
	}

	/**
	 * 是否为http开头的连接
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isHttpUrl(String str) {
		if (!isEmptyString(str)) {
			return str.startsWith("http://") || str.startsWith("https://");
		}
		return false;
	}

	/**
	 * 去掉字符串中所有空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		return tmpstr;
	}
}
