package com.zqgame.yyreader.encryption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;

import com.zqgame.yyreader.util.LogUtil;

/*-- JAVA --*/

/**
 * Is used for encrypting and decrypting Strings and JSONObjects. <br>
 * The JSON Objects can then be sent to a PHP script where they can be encrypted and decrypted with the same algorithm. 
 * @throws CryptingException
 */
@SuppressLint("NewApi")
public class Cryptor {

    private Cipher cipher;
    private final String secretKey = "cn.show.hovn.www";
    private final String iv = "cn.show.hovn.www";
    private final String CIPHER_MODE = "AES/CFB8/NoPadding";

    private SecretKey keySpec;
    private IvParameterSpec ivSpec;
    private Charset CHARSET = Charset.forName("UTF-8");
    private static Cryptor one;
    private static Object lock=new Object();

    private Cryptor() {
        keySpec = new SecretKeySpec(secretKey.getBytes(CHARSET), "AES");
        ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
        try {
            cipher = Cipher.getInstance(CIPHER_MODE);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (NoSuchPaddingException e) {
            throw new SecurityException(e);
        }
    }

    public  static Cryptor getCryptor(){
    	synchronized (lock) {
    		if(one==null){
        		one=new Cryptor();
        	}
        	return one;
		}
    }
    
    /**
     * @param input A "AES/CFB8/NoPadding" encrypted String
     * @return The decrypted String
     * @throws CryptingException
     */
//    public String decrypt(String input) {
//    	if(input==null || input.equals("")){
//    		throw new IllegalArgumentException("参数为空");
//    	}
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//            
//            return  new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(new String(input.getBytes(CHARSET),CHARSET))),CHARSET); 
//        } catch (IllegalBlockSizeException e) {
//            throw new SecurityException(e);
//        } catch (BadPaddingException e) {
//            throw new SecurityException(e);
//        } catch (InvalidKeyException e) {
//            throw new SecurityException(e);
//        } catch (InvalidAlgorithmParameterException e) {
//            throw new SecurityException(e);
//        }
//    }

    /**
     * @param input Any String to be encrypted
     * @return An "AES/CFB8/NoPadding" encrypted String
     * @throws CryptingException
     */
    public String encrypt(String input)  {
        try {
        	LogUtil.log("请求参数: " + input);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            String ss = Base64.encode(cipher.doFinal(input.getBytes(CHARSET)));
//            RunningInfo.out("ase加密: " + ss);
            String encode = URLEncoder.encode(ss, "UTF-8");
//            RunningInfo.out("encode: " + encode);
            return encode;
//            return DatatypeConverter.printBase64Binary(cipher.doFinal(input.getBytes(CHARSET))).trim();
        } catch (InvalidKeyException e) {
            throw new SecurityException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new SecurityException(e);
        } catch (IllegalBlockSizeException e) {
            throw new SecurityException(e);
        } catch (BadPaddingException e) {
            throw new SecurityException(e);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
		}
    }

//    public static void main(String Args[]) throws JSONException {
//
//            Cryptor c = Cryptor.getCryptor();
//            String original = "devType=2&devId=464646538644686&version=1.0&devTypeName=MI2&devOS=android10.0&devNet=1&channel=100&isfirstOn=1";
//            System.out.println("Original: " + original);
//            String encrypted = c.encrypt(original);
//            System.out.println("Encoded: " + encrypted);
//            System.out.println("Decoded: " + c.decrypt(encrypted));
//            
//            System.out.println(URLEncoder.encode(encrypted));
//
//
//
//    }
}


