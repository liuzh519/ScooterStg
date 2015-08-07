package com.hylh.scooterstg.utils;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.gms.maps.model.Marker;
import com.hylh.scooterstg.cmd.Command;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class StatusUtils {
	public static int mDelay;
	public static int mMax;
	public static OnStatusEvent mEvt;
	public static OnStatusEvent mEvt2;
	private static MyApplication mApp;
	private static int mRun = 0;
	private static JSONArray mRows; 
	private static long mLast;
	private static SpUtil mSp;
	private static boolean mExec = false;
	private static boolean mCheckBind = false;
	
	public static double mLat;
	public static double mLng;
	
	public final static int MAX_DELAY = 15;
	
	public static JSONObject mSelPark;
	public static JSONArray mDevList;
	public static JSONObject mDevLoc;
	
	final static int ACC_BIT = 1;
	final static int ENG_BIT = 1 << ( 1 );
	final static int BRK_BIT = 1 << ( 2 );
	final static int AIR_BIT = 1 << ( 5 );
	final static int DOR_BIT = 1 << ( 6 );
	final static int RVH_BIT = 1 << ( 7 );
	
	final static int PWR_BIT = 1;
	final static int FUL_BIT = 1 << ( 1 );
	final static int SOS_BIT = 1 << ( 2 );
	final static int GAO_BIT = 1 << ( 3 );
	final static int GAC_BIT = 1 << ( 4 );
	final static int OSP_BIT = 1 << ( 5 );
	
	final static int FLS_BIT = 1;
	final static int RAM_BIT = 1 << ( 1 );
	final static int DFG_BIT = 1 << ( 2 );
	final static int TOW_BIT = 1 << ( 3 );
	final static int GP1_BIT = 1 << ( 4 );
	final static int GP2_BIT = 1 << ( 5 );
	final static int GS1_BIT = 1 << ( 6 );
	final static int GS2_BIT = 1 << ( 7 );
	
	final static int TRK_BIT = 1;
	final static int LIG_BIT = 1 << ( 1 );
	final static int RVL_BIT = 1 << ( 2 );
	final static int BRK_HAN = 1 << ( 3 );

	final static String preZero = "000000";

	public interface OnStatusEvent {
		void onTimer( int t );
		void onStatus( JSONArray st );
	};

	public static void setCheckBind( boolean chk ){
		mCheckBind = chk;
	}
	public static boolean isCheckBind( ){
		return mCheckBind;
	}
	public static void setSelPark( JSONObject park ){
		mSelPark = park;
	}
	public static JSONObject getSelPark(){
		return mSelPark;
	}
	
	public static void setDevList( JSONArray rows ){
		mDevList = rows;
	}
	public static JSONArray getDevList(){
		return mDevList;
	}
	
	public static void setPos( double lng, double lat ){
		mLat = lat;
		mLng = lng;
	}
	
	public static void start(){
		if( mApp == null ){
			mApp = MyApplication.getInstance();
			mSp = SpUtil.getInstance();
		}
		
		mRun = 1;
		mDelay = -1;
		mEvt = null;
		setTimer();
		Log.i("StatusUtils", "start");
	}
	
	public static void stop(){
		mRun = 0;
		save();
		Log.i("StatusUtils", "stop");
	}
	
	public static void clearDelay(){
		mEvt = null;
	}
	
	public static void clearDelay2(){
		mEvt2 = null;
	}
	
	public static void save(){
		SpUtil sp = SpUtil.getInstance();
		sp.setJSONArray(SpUtil.LOGIN_DEV_STAT, mRows);
	}
	
	public static void load(){
		SpUtil sp = SpUtil.getInstance();
		mRows = sp.getJSONArray(SpUtil.LOGIN_DEV_STAT);
		mEvt = null;
	}

	public static JSONObject getDev() {
		SpUtil sp = SpUtil.getInstance();
		String tid = sp.getTid();
		return getPark( tid );
	}

	public static JSONObject getPark(String pid) {
		JSONObject park;
		if( mRows != null && pid != null ){
			try {
				for( int i = 0; i < mRows.length(); i ++ ){
					park = mRows.getJSONObject( i );
					if( park.has( "id" ) ){
						if( park.getString( "id" ).compareTo( pid ) == 0 ){
							return park;
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void parserSt( JSONObject dev ){
		String s = "";
		byte st[] = {0,0,0,0};
		boolean val;
		
		if( dev.has( "acc") && dev.has( "brh" ) ){
			return;
		}
		
		try {
			s = dev.getString( "st" );
			if( s.length() != 8 ){
				s = preZero.substring( 0, 8 - s.length() ) + s;
			}
			st = Hex.decodeHex( s.toCharArray() );
			val = (st[3] & ACC_BIT ) > 0 ? true : false;				dev.put( "acc", val );
			val = (st[3] & ENG_BIT ) > 0 ? true : false;				dev.put( "eng", val );
			val = (st[3] & BRK_BIT ) > 0 ? true : false;				dev.put( "brk", val );
			val = (st[3] & AIR_BIT ) > 0 ? true : false;				dev.put( "air", val );
			val = (st[3] & DOR_BIT ) > 0 ? true : false;				dev.put( "dor", val );
			val = (st[3] & RVH_BIT ) > 0 ? true : false;				dev.put( "rvh", val );
			                                                                      
			val = (st[2] & PWR_BIT ) > 0 ? true : false;				dev.put( "pwr", val );
			val = (st[2] & FUL_BIT ) > 0 ? true : false;				dev.put( "ful", val );
			val = (st[2] & SOS_BIT ) > 0 ? true : false;				dev.put( "sos", val );
			val = (st[2] & GAO_BIT ) > 0 ? true : false;				dev.put( "gao", val );
			val = (st[2] & GAC_BIT ) > 0 ? true : false;				dev.put( "gac", val );
			val = (st[2] & OSP_BIT ) > 0 ? true : false;				dev.put( "osp", val );
			                                                                      
			val = (st[1] & FLS_BIT ) > 0 ? true : false;				dev.put( "fls", val );
			val = (st[1] & RAM_BIT ) > 0 ? true : false;				dev.put( "ram", val );
			val = (st[1] & DFG_BIT ) > 0 ? true : false;				dev.put( "dfg", val );
			val = (st[1] & TOW_BIT ) > 0 ? true : false;				dev.put( "tow", val );
			val = (st[1] & GP1_BIT ) > 0 ? true : false;				dev.put( "gp1", val );
			val = (st[1] & GP2_BIT ) > 0 ? true : false;				dev.put( "gp2", val );
			val = (st[1] & GS1_BIT ) > 0 ? true : false;				dev.put( "gs1", val );
			val = (st[1] & GS2_BIT ) > 0 ? true : false;				dev.put( "gs2", val );
			                                                                      
			val = (st[0] & TRK_BIT ) > 0 ? true : false;				dev.put( "trk", val );
			val = (st[0] & LIG_BIT ) > 0 ? true : false;				dev.put( "lig", val );
			val = (st[0] & RVL_BIT ) > 0 ? true : false;				dev.put( "rvl", val );
			val = (st[0] & BRK_HAN ) > 0 ? true : false;				dev.put( "brh", val );

			dev.put( "anti", (int)(st[0] >> 4) & 0x0F );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	public static JSONArray getStatus(){
		return mRows;
	}
	
	public static void setDelay( Context c, OnStatusEvent e, int d ){
		mMax = d;
//		mContext = c;
		mEvt = e;
		if( mApp == null ){
			mApp = MyApplication.getInstance();
		}
		update();
	}
	
	public static void setDelay2( Context c, OnStatusEvent e, int d ){
		mMax = d;
//		mContext = c;
		mEvt2 = e;
		if( mApp == null ){
			mApp = MyApplication.getInstance();
		}
		update();
	}
	
	public static void setDelay( int d ){
		mDelay = d;
	}

	public static void setTimer(){
		if( mRun == 1 ){
			mLast = System.currentTimeMillis();
			hTimer.postDelayed(mRunable,1000);
		} else if( mRun > 1 ){
			mRun --;
		}
		if( mEvt != null ){
			mEvt.onTimer( mDelay );
		}
	}
	public static void delTimer(){
		mRun = 0;
	}
	
	public static void UpdaeState(){
		if( mRows != null ){
			if( null != mEvt ){
				mEvt.onStatus(mRows);
			} 
			if( null != mEvt2 ){
				mEvt2.onStatus(mRows);
			}
		}
	}


	static Handler mRefresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONObject tent = json.getJSONObject( "rental" );
//					if( mCheckBind ){
						if( tent.has("id") ){
							mSp.setTid( tent.getString("id"));
							mSp.setTName( tent.getString("number"));
							if( mDevList != null && mDevList.length() > 0 ){
								if( mDevList.getJSONObject(0).has("long_name") ){
									mDevList.put(0, tent);
								} else {
									JSONArray ar = new JSONArray();
									ar.put(0, tent);
									for( int i = 1; i < mDevList.length(); i ++ ){
										ar.put( mDevList.getJSONObject(i) );
									}
									mDevList = ar;
								}
							} else {
								mDevList = new JSONArray();
								mDevList.put(0, tent);
							}
						} else {
							mSp.setTid( "" );
							mSp.setTName( "" );
						}
//					}
//					if( tent.has("location") )
//					{
//						String sLoc = tent.getString( "location" );
//						JSONTokener jsonParser = new JSONTokener( sLoc );
//						JSONObject location = (JSONObject) jsonParser.nextValue();						
//				    	JSONArray coordinates = location.getJSONArray( "coordinates" );
//
//						double lat = coordinates.getDouble( 0 );
//						double lng = coordinates.getDouble( 1 );
//						
//						UpdaeState();
//					}
					UpdaeState();
					mCheckBind = false;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	static Handler mQuery = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONArray rows = json.getJSONArray( "ps" );
//					if( rows.length() > 0 )
//					{
						mRows = rows;
						UpdaeState();
//					} 
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			mExec = false;
		}
	};
	
	public static void update(){
		if( mSp ==  null )
			mSp = SpUtil.getInstance();
		
		Log.i( "update", "update" );
//		if( mCheckBind ){
			mExec = true;
			
			if( mSp.getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){
//				mExec = false;
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
				params.add(new BasicNameValuePair("query", "rental" ));  
				mApp.getCmd().sendHttpsGet(Utils.urlUser, params, mApp, mRefresh, Command.MODE_TOAST);
//				return;
			}
//			else {
//				mCheckBind = false;
//			}
//		}
//		if( !mSp.getTid().isEmpty() ){
//			mExec = false;
//			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
//			params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
//			params.add(new BasicNameValuePair("query", "rental" ));  
//			mApp.getCmd().sendHttpGet(Utils.urlUser, params, mApp, mRefresh, Command.MODE_TOAST);
//		} else {
//			if( Math.abs( mLat) < 0.000001 && Math.abs(mLng) < 0.0000001 ){
//				setTimer();
//				return;
//			}
		
			
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
			params.add(new BasicNameValuePair("lat", String.format( "%f", mLat) ));  
			params.add(new BasicNameValuePair("lng", String.format( "%f", mLng)));
			
//				params.add(new BasicNameValuePair("lat", "40.646598" )); //String.format( "%f", mLat) ));  
//				params.add(new BasicNameValuePair("lng", "-73.974413" )); //String.format( "%f", mLng))); 
			params.add(new BasicNameValuePair("radius", "10" ));  
			
			mApp.getCmd().sendHttpsGet( Utils.urlParking, params, mApp, mQuery, Command.MODE_TOAST);
//		}
	}
	
	public static Handler hTimer = new Handler( );
	public static Runnable mRunable = new Runnable( ) {
		public void run ( ) {
			long cur = System.currentTimeMillis();
			if( cur - mLast < 500 ){
				return;
			}
			mLast = cur;
			mDelay --;

			if( mDelay <= 0 ){
				mDelay = mMax;
				if( !mExec && mEvt != null ){
//					mEvt.onTimer( mDelay );
					update();
				}
			}
			setTimer();
		}
	};
}