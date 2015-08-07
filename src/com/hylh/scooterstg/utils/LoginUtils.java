package com.hylh.scooterstg.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class LoginUtils {
	static public boolean hasLogin = false;
	static public boolean init = false;
	static public Activity ctlAct = null;
	static private Handler hLogin = null;
	static private Message msgLogin = null;
	static private boolean checkpwd = false;

	public static JSONObject veclist = null;
	public static String tid = null;
	public static String uid = null;
	public static String vna = null;
	public static String sim = null;
	public static String dna = null;
	public static String una = null;
	public static String pwd = null;
	public static String ssk = null;
	public static String txnkey = null;
	public static String phone_mask = null;
	public static String phone = null;
	public static String newphone = null;
	
	public static int pwdChkSt = 0;
	public static int lgFlag = 0;
	
	public static final int PWD_CHK_UNKNOWN = 0;
	public static final int PWD_CHK_PASS = 1;
	public static final int PWD_CHK_LOGIN = 2;
	public static final int PWD_CHK_FAILED = 3;
	public static final int PWD_CHK_CHANGE = 4;

	
	public static String enPassword( String p ){
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(p.getBytes("iso-8859-1"), 0,
					p.length());
			sha1hash = md.digest();
			p = Utils.convertToHex(sha1hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p = "";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p = "";
		}
		
		return p;
	}

}
