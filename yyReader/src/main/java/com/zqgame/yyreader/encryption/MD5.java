package com.zqgame.yyreader.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String md54File(String filePath){
		MessageDigest md = null;
		StringBuilder sb = null;
		try {
			md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(filePath);
			int length = 0;
			byte[] buff = new byte[1024];
			while((length = fis.read(buff)) != -1){
				md.update(buff, 0, length);
			}
			
			sb = new StringBuilder();
			buff = md.digest();
			for(int i = 0 ; i < buff.length ; i++){
				sb.append(Character.forDigit((buff[i] & 240) >> 4, Character.SIZE));
				sb.append(Character.forDigit((buff[i] & 15), Character.SIZE));
			}
			
//			System.out.println(Base64.encodeToString(md.digest(), Base64.DEFAULT));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String md54String(String str){
		MessageDigest md = null;
		StringBuilder sb = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] buff = new byte[1024];
			md.update(str.getBytes());
			sb = new StringBuilder();
			buff = md.digest();
			for(int i = 0 ; i < buff.length ; i++){
				sb.append(Character.forDigit((buff[i] & 240) >> 4, Character.SIZE));
				sb.append(Character.forDigit((buff[i] & 15), Character.SIZE));
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

}
