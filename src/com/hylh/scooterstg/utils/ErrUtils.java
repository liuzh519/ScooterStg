package com.hylh.scooterstg.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrUtils {
	
	public static Map<String,String> mLogin = new HashMap<String,String>();
	public static Map<String,String> mPark = new HashMap<String,String>();
	public static Map<String,String> mLock = new HashMap<String,String>();
	public static Map<String,String> mUnlock = new HashMap<String,String>();
	public static Map<String,String> mTrunk = new HashMap<String,String>();
	public static Map<String,String> mRental = new HashMap<String,String>();
	public static Map<String,String> mReturn = new HashMap<String,String>();
	public static Map<String,String> mCoupon = new HashMap<String,String>();
	
	public static void init(){
//		mCodes.put( "1000", "RECORD FOUND" );
//		mCodes.put( "1001", "RECORD KEY MISSING" );
//		mCodes.put( "1002", "RECORD NOT FOUND" );
//		mCodes.put( "1003", "OPERATION NOT PERMITTED" );
//		mCodes.put( "1004", "OPERATION REQUIRES USER INPUT" );
//		mCodes.put( "-1111", "VALUE UNNASSIGNED" );
//		mCodes.put( "1002", "ACCESS NOT PERMITTED" );
//		mCodes.put( "1000", "HEALTH GOOD" );
//		mCodes.put( "1002", "HEALTH BAD" );
		mLogin.put( "1000", "USER EXISTS" );
		mLogin.put( "1002", "USER AUTHENTICATION FAILED" );
		mLogin.put( "1000", "USER AUTHENTICATED" );
		mLogin.put( "1001", "USER PHONE AND EMAIL MISSING" );
		mLogin.put( "1001", "USER PASSWORD MISSING" );
		mLogin.put( "1002", "USER PASSWORD DOES NOT MATCH" );
		mLogin.put( "1003", "USER DISABLED" );
		mLogin.put( "1004", "USER PHONE NOT CONFIRMED" );
		mLogin.put( "1004", "USER EMAIL NOT CONFIRMED" );
		mLogin.put( "1000", "USER LOGOUT SUCCEDED" );
		mLogin.put( "1002", "USER LOGOUT FAILED" );
		
		mPark.put( "1000", "PARKING LOCATIONS EXIST" );
		mPark.put( "1002", "PARKING LOCATIONS REQUEST FAILED" );
		
//		mCodes.put( "1000", "NOTIFICATION SUCCEEDED" );
//		mCodes.put( "1002", "NOTIFICATION FAILED" );
//		mCodes.put( "1002", "COMMAND FAILED" );
//		mCodes.put( "1000", "LIST SCOOTERS REQUEST SUCCEDED" );
		mRental.put( "-1111", "RENTAL STATUS CODE NOT STARTED" );
		mRental.put( "1100", "RENTAL STATUS CODE RENTAL SUCCEDED" );
		mRental.put( "1101", "RENTAL STATUS CODE RENTAL FAILED" );
		mRental.put( "1102", "RENTAL STATUS CODE ALREADY HAS RENTAL" );
		mRental.put( "1103", "RENTAL STATUS CODE NOT ENABLED" );
		mRental.put( "1104", "RENTAL STATUS CODE SCOOTER NOT FOUND" );
		mRental.put( "1105", "RENTAL STATUS CODES PAYMENT METHOD DISABLED" );
		mRental.put( "1106", "RENTAL STATUS CODE SCOOTER ID MISSING" );
		mRental.put( "1002", "RECORD NOT FOUND" );
		mRental.put( "1001", "RECORD KEY MISSING" );
		
		mReturn.put( "1100", "RETURN STATUS CODE RETURN SUCCEDED" );
		mReturn.put( "1101", "RETURN STATUS CODE PARKING STATION NOT FOUND" );
		mReturn.put( "1102", "RETURN STATUS CODE RENTAL NOT FOUND" );
		mReturn.put( "1105", "RETURN STATUS CODE PARKING STATION ID MISSING" );
		mReturn.put( "1106", "RETURN STATUS CODE SCOOTER NOT ASSOCIATED WITH RENTAL" );
		mReturn.put( "1107", "RETURN STATUS CODE SCOOTER NOT IN PARKING STATION" );
		mReturn.put( "1002", "ACCESS NOT PERMITTED" );
		
		mUnlock.put( "1100", "UNLOCK STATUS CODE UNLOCK SUCCEEDED" );
		mUnlock.put( "1101", "UNLOCK STATUS CODE UNLOCK FAILED" );
		mUnlock.put( "1002", "ACCESS NOT PERMITTED" );
		
		mTrunk.put( "1100", "OPEN TRUNK STATUS CODE OPEN TRUNK SUCCEEDED" );
		mTrunk.put( "1101", "OPEN TRUNK STATUS CODE OPEN TRUNK FAILED" );
		mTrunk.put( "1002", "ACCESS NOT PERMITTED" );
		
//		mCodes.put( "1100", "DEVICE QUERY STATUS CODE SUCCEEDED" );
//		mCodes.put( "1101", "DEVICE QUERY STATUS CODE FAILED" );
//		mCodes.put( "-1111", "PAYMENT INFO UPDATE NOT STARTED" );
//		mCodes.put( "1000", "PAYMENT INFO UPDATED" );
//		mCodes.put( "1002", "PAYMENT INFO UPDATE FAILED" );
		
		mCoupon.put( "-1111", "COUPON APPLICATION NOT STARTED" );
		mCoupon.put( "1100", "COUPON APPLIED" );
		mCoupon.put( "1101", "COUPON APPLICATION FAILED" );
		mCoupon.put( "1102", "COUPON USER NOT IN SESSION" );
		mCoupon.put( "1103", "COUPON APPLICATION FAILED" );
		mCoupon.put( "1104", "COUPON CODE INVALID" );
		mCoupon.put( "1002", "ACCESS NOT PERMITTED" );
	}
	
	public static String getCoupon( String code ){
		String msg = mCoupon.get(code);
		if( msg != null ){
			return msg;
		} else {
			return code;
		}
	}
	
	public static String getUnlock( String code ){
		String msg = mUnlock.get(code);
		if( msg != null ){
			return msg;
		} else {
			return code;
		}
	}
	
	public static String getTrunk( String code ){
		String msg = mTrunk.get(code);
		if( msg != null ){
			return msg;
		} else {
			return code;
		}
	}
	
	public static String getLogin( String code ){
		String msg = mLogin.get(code);
		if( msg != null ){
			return msg;
		} else {
			return code;
		}
	}

	public static String getRental(String obj) {
		String msg = mRental.get(obj);
		if( msg != null ){
			return msg;
		} else {
			return obj;
		}
	}
	public static String getPark(String obj) {
		String msg = mPark.get(obj);
		if( msg != null ){
			return msg;
		} else {
			return obj;
		}
	}
	
	public static String getReturn(String obj) {
		String msg = mReturn.get(obj);
		if( msg != null ){
			return msg;
		} else {
			return obj;
		}
	}
}
