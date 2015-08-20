package com.hylh.scooterstg.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SpUtil {
	public static final String NAME="hylh.scootert";
	private static SpUtil instance;
	private SharedPreferences sp;
	private JSONObject mSession;
	static{
		instance=new SpUtil();
	}

	public static final String APP_CTRL_MODE = "app.ctrl.mode";
	public static final String APP_VIBRATE = "app.ctrl.vibrate";
	public static final String APP_MEDIA = "app.ctrl.media";
	public static final String APP_WARN_CONFIRM = "app.ctrl.warn_confirm";
	
	public static final String APP_VER = "app.cfg.version";
	public static final String APP_MAP_TYPE = "app.map.type";
	public static final String TXN_BTKEY = "sys.txn.btkey";
	public static final String TXN_TID = "sys.txn.tid";
	public static final String TXN_TNAME = "sys.txn.tname";
	public static final String TXN_DPHONE = "sys.txn.dphone";
	public static final String TXN_SEQUENCE = "sys.txn.sequence";
	public static final String TXN_SSK = "sys.txn.ssk";
	public static final String TXN_MACKEY = "sys.txn.txnkey";
	public static final String TXN_UID = "sys.txn.uid";
	public static final String TXN_PMASK = "sys.txn.pmask";
	public static final String TXN_LNAME = "sys.txn.name";
	public static final String TXN_UPHONE = "sys.txn.phone";
	public static final String TXN_VNAME = "sys.txn.vna";
	public static final String TXN_TIME = "sys.txn.time";
	public static final String TXN_CHECK = "sys.txn.check";
	public static final String TXN_LEGAL = "sys.txn.lagel";
	
	public static final String LOGIN_SESSION = "sys.login.session";
	public static final String LOGIN_KEEP = "sys.login.keep";
	public static final String LOGIN_LOCAL = "sys.login.local";
	public static final String LOGIN_SAVE = "sys.login.save";
	public static final String LOGIN_NAME = "sys.login.name";
	public static final String LOGIN_PHONE = "sys.login.phone";
	public static final String LOGIN_PSWD = "sys.login.paswd";
	public static final String LOGIN_LOC_PWD = "sys.login.locpwd";
	public static final String LOGIN_DEV_LIST = "sys.login.vlst";
	public static final String LOGIN_DEV_STAT = "sys.login.stat";
	public static final String LOGIN_DEV_INFO = "sys.login.info";

	public static final String SYS_VER_CLIENT = "sys.ver.client";

	public static final String DEV_LIST = "dev.vlst";
	
	//added by ycf on 20150818 begin
	//租车次数统计
	private  int rentCnt = 0;//
	public int getRentCnt() {
		return rentCnt;
	}

	public void setRentCnt(int rentCnt) {
		this.rentCnt = rentCnt;
	}

	public void addRentCnt() {
		this.rentCnt++;
	}
	//added by ycf on 20150818 end

	public int mCheck = 0;
	public long mTime = 0;
	public int mLogin = 0;
	public int mCmdCtrl = 0;
	public boolean mVibrate = true;
	public boolean mMedia = true;
	public boolean mWarnConfirm = false;
	public boolean mLagel = false;
	
	public static SpUtil getInstance(){
		if(instance==null){
			instance=new SpUtil();
		}
		return instance;
	}
	
	public void init(Context context){
		sp=context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		mSession = getJSONObject( LOGIN_SESSION );

		mLogin = instance.getInt(LOGIN_KEEP,0);
		mCmdCtrl = instance.getInt(APP_CTRL_MODE,0);
		mVibrate = instance.getBool(APP_VIBRATE,true);
		mMedia = instance.getBool(APP_MEDIA,true);
		mWarnConfirm = instance.getBool(APP_WARN_CONFIRM,false);
		mTime = getLong(TXN_TIME, 0 );
		mCheck = getInt(TXN_CHECK, 0 );
		mLagel = instance.getBool(TXN_LEGAL,false);
	}
	
	public static SharedPreferences getSharePerference(Context context){
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	public boolean isFirst(){
		return sp.getBoolean("isFirst", true);
	}
	
	public void setFirst(boolean flg){
		Editor editor=sp.edit();
		editor.putBoolean("isFirst", flg);
		editor.commit();
	}
	
	public boolean showLegal(){
		return sp.getBoolean(TXN_LEGAL, true);
	}
	public void setLegal(boolean flg){
		Editor editor=sp.edit();
		editor.putBoolean(TXN_LEGAL, flg);
		editor.commit();
	}

	public void setTime( long t ){
		mTime = t;
		setLong(TXN_TIME, mTime );
	}
	public long getTime(){
		return mTime;
	}

	public void setCheck( int t ){
		mCheck = t;
		setInt(TXN_CHECK, mCheck );
	}
	public int getCheck(){
		return mCheck;
	}

	public void setLogin( int n ){
		mLogin = n;
		setInt(LOGIN_KEEP, n );
	}
	public int getLogin(){
		return mLogin;
	}

	public void setCmdCtrl( int n ){
		mCmdCtrl = n;
		setInt(APP_CTRL_MODE, n );
	}
	public int getCmdCtrl(){
		return mCmdCtrl;
	}
	public void setVibrate( boolean n ){
		mVibrate = n;
		setBool(APP_VIBRATE, n );
	}
	public boolean getVibrate(){
		return mVibrate;
	}
	public void setMedia( boolean n ){
		mMedia = n;
		setBool(APP_MEDIA, n );
	}
	public boolean getMedia(){
		return mMedia;
	}
	public void setWarnConfirm( boolean n ){
		mWarnConfirm = n;
		setBool(APP_WARN_CONFIRM, n );
	}
	public boolean getWarnConfirm(){
		return mWarnConfirm;
	}
	
	public static void setStringSharedPerference(SharedPreferences sp,String key,String value){
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void setBooleanSharedPerference(SharedPreferences sp,String key,boolean value){
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void saveSession(JSONObject json) {
		mSession = json;
		setJSONObject(LOGIN_SESSION, mSession );
	}
	
	public void setSession( JSONObject json ){
		mSession = json;
	}
	public JSONArray getDevs(){
		JSONArray devs = null;
		try {
			devs = mSession.getJSONArray( "dev" );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devs;
	}
	public String getSSK() throws JSONException{
		if( mSession != null ){
			return mSession.getString( "ssk" );
		} else {
			return "";
		}
	}
	public String getUid() throws JSONException{
		if( mSession != null ){
			return mSession.getString( "uid" );
		} else {
			return "";
		}
	}
	public String getTxnkey() throws JSONException{
		if( mSession != null ){
			return mSession.getString( "txnkey" );
		} else {
			return "";
		}
	}
	public String getLName() {
		try {
			if( mSession != null ){
				return mSession.getString( "lname" );
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public String getUName(){
		try {
			if( mSession != null ){
				return mSession.getString( "uname" );
			} 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public JSONArray getDevice() throws JSONException{
		if( mSession != null ){
			return mSession.getJSONArray( "dev" );
		} else {
			return null;
		}
	}
	public String getPhone() throws JSONException{
		if( mSession != null ){
			return mSession.getString( "phone" );
		} else {
			return "";
		}
	}
	public String getPhoneMask(){
		try {
			String phone = mSession.getString( "phone" );
			String phoneMask = "***********";
			
			if( phone.length() > 5 ){
				phoneMask = "******" + phone.substring( phone.length() - 5 );
			}
			
			return phoneMask;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public void setTid(String val) {
		Editor editor=sp.edit();
		editor.putString(TXN_TID, val);
		editor.commit();
	}
	public String getTid() {
		return sp.getString( TXN_TID, "" );
	}

	public void setTName(String val) {
		Editor editor=sp.edit();
		editor.putString(TXN_TNAME, val);
		editor.commit();
	}
	public String getTName() {
		return sp.getString( TXN_TNAME, "" );
	}

	public void setLoginName(String val) {
		Editor editor=sp.edit();
		editor.putString(LOGIN_NAME, val);
		editor.commit();
	}
	public String getLoginName() {
		return sp.getString(LOGIN_NAME, "" );
	}
	
	
	public void setString( String key, String val ){
		Editor editor=sp.edit();
		editor.putString(key, val);
		editor.commit();
	}
	
	public String getString( String key){
		return sp.getString( key, "" );
	}
	public String getString( String key, String def ){
		return sp.getString( key, def );
	}
	
	public void setInt( String key, int val ){
		Editor editor=sp.edit();
		editor.putInt(key, val);
		editor.commit();
	}
	public int getInt( String key){
		return sp.getInt( key, 0 );
	}
	public int getInt( String key, int def ){
		return sp.getInt( key, def );
	}
	
	public void setLong( String key, long val ){
		Editor editor=sp.edit();
		editor.putLong(key, val);
		editor.commit();
	}
	public long getLong( String key ){
		return sp.getLong( key, 0 );
	}
	public long getLong( String key, long def ){
		return sp.getLong( key, def );
	}
	
	public void setBool( String key, boolean val ){
		Editor editor=sp.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}
	public boolean getBool( String key ){
		return sp.getBoolean( key, false );
	}
	public boolean getBool( String key, boolean def ){
		return sp.getBoolean( key, def );
	}
	
	public void setJSONObject( String key, JSONObject val ){
		Editor editor=sp.edit();
		editor.putString(key, val.toString());
		editor.commit();
	}
	public JSONObject getJSONObject( String key ){
		String lst = sp.getString(key, "{}");
		if (lst.length() > 1) {
			try {
				JSONTokener jsonParser = new JSONTokener(lst);
				JSONObject json;
				json = (JSONObject) jsonParser.nextValue();
				return json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setJSONArray(String key, JSONArray val) {
		// TODO Auto-generated method stub
		Editor editor=sp.edit();
		editor.putString(key, val.toString());
		editor.commit();
		
	}
	public JSONArray getJSONArray( String key ){
		String lst = sp.getString(key, "[]");
		if (lst.length() > 1) {
			try {
				JSONArray json = new JSONArray(lst);
				return json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public long getSeq(int times){
		return sp.getLong(TXN_SEQUENCE, 1) + times;
	}
	
	public void setSeq( int times ){
		long seq = sp.getLong(TXN_SEQUENCE, 1) + times;
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("sys.txn.sequence", seq + 1 );
		editor.commit();
	}

	public String getSTKey() {
		return sp.getString("sys.txn.usakey", "dda405b2-f302-4e60-9e7b-2e90378d");
	}

	public void setSTKey(String val) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("sys.txn.usakey", val);
		editor.commit();
	}
	
	public String getBtKey( int times) {
		return sp.getString(TXN_BTKEY, "");
	}

	public void setBtKey(String val) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(TXN_BTKEY, val);
		editor.commit();
	}

	public void setBtKey(int retry) {
		SharedPreferences.Editor editor = sp.edit();
		String btkey = sp.getString(TXN_BTKEY, "");
		try{
		if( btkey.length() > 0 ){
			editor.putString(TXN_BTKEY, Long.toString( Long.parseLong( btkey, 16 ) + retry, 16 ) );
			editor.commit();
		}
		}catch( NumberFormatException e ){
			
		}
	}
}
