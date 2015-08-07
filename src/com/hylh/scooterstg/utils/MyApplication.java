package com.hylh.scooterstg.utils;


import org.json.JSONObject;

import com.hylh.scooterstg.activity.LoginActivity;
import com.hylh.scooterstg.cmd.BluetoothServer;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.cmd.SMSUtils;
import android.app.Application;  
import android.content.Context;
import android.content.Intent;

public class MyApplication extends Application { 
	private long mLastTime = 0;
	private SpUtil sp;
	private MyNotification _anf;
	private Command _cmd;
	private JSONObject _session;
	private static int _sequence = 0; 
	private static MyApplication mInstance = null;
	
    @Override  
    public void onCreate() {
        super.onCreate();
        
        MyCrashHandler crashHandler = MyCrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());  

		mInstance = this;
		ErrUtils.init();
        sp = SpUtil.getInstance();
        sp.init( this );
//        _anf = new AbsNotification( getApplicationContext() );
        _cmd = new Command(this);
        Utils.init( this );
        
        SMSUtils.init( getBaseContext(), sp.getString( SpUtil.TXN_DPHONE ) );
        BluetoothServer.init( getBaseContext() );
        
        StatusUtils.load();
    }
	
	public static MyApplication getInstance() {
		return mInstance;
	}
    
    @Override  
    public void onTerminate(){
//    	BackTask.cancelStateTimer();
    }
    
    
    public long lastTime(){
    	return mLastTime;
    }
    public void setLastTime( long lastTime ){
    	mLastTime = lastTime;
    }
    public MyNotification notification(){
    	return _anf;
    }
    public void setSession( JSONObject json ){
    	if( json != null ){
    		_session = json;
    	} else {
    		
    	}
    }
    public JSONObject getSession(){
    	return _session;
    }
    public Command getCmd(){
    	return _cmd;
    }

	public int sequence() {
		_sequence ++;
		return _sequence;
	}
	
	public void setLogin( int login ){
		sp.setLogin( login );
	}

	public boolean isLogin(){
		if( sp.getLogin() > 0 ){
			return true;
		} else {
			return false;
		}
	}
	public boolean checkLogin(Context context){
		if( !isLogin() ){
			Intent intent = new Intent(context, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return false;
		}
		return true;
	}
	
	public void logout(){
	}
}  