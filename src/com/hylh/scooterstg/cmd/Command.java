package com.hylh.scooterstg.cmd;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.NetworkUtils;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.ToastUtil;
import com.hylh.scooterstg.utils.Utils;


/**
 * 
 * @version v1.0
 * *******************************************************************************************************
 *     Date              Full Name            Task/Defect ID              Reason
 *  -------------      ------------        -------------------        ----------------
 *    2015-07-23          ycf                    BR2                      (Reachability check)网络连接检测                                                                  
 * *******************************************************************************************************
 */

public class Command {
	static public final int CMD_UNKNOWN = 0;
	static public final int CMD_LOCK = 1;
	static public final int CMD_UNLOCK = 2;
	static public final int CMD_TRUNK = 3;
	static public final int CMD_PAINC = 4;
	static public final int CMD_START = 5;
	static public final int CMD_STOP = 6;
	static public final int CMD_CAMERA = 7;
	static public final int CMD_SIGN1 = 8;
	static public final int CMD_SIGN2 = 9;
	static public final int CMD_BIND = 10;
	static public final int CMD_UNBIND = 11;
	
	public final static int CTRL_TCP = 0;
	public final static int CTRL_BT = 1;
	public final static int CTRL_SMS = 2;
	public final static int CTRL_WWW = 3;
	public static int ctrlmode = CTRL_WWW;
	
	public final static int MODE_UNKNOWN = 0;
	public final static int MODE_SILENT = 1;
	public final static int MODE_TOAST = 2;
	
	public final static int TIME_OUT = 15;

	private static ICommand _cmd;
	private CmdBuilder _cmdBuild;
	private MyApplication _app;
	private CmdTcp _tcp;
	private SpUtil _sp;
	public Command(MyApplication app) {
		_app = app;
		_cmdBuild = new CmdBuilder( app );
		_tcp = new CmdTcp( _cmdBuild );
		_sp = SpUtil.getInstance();
	}
    
    public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode) {
    	if( _cmd == null ){
    		setCtrlMode( SpUtil.getInstance().getInt( SpUtil.APP_CTRL_MODE ) );
    	}
    	if( keys == null ){
    		keys = new JSONObject();
    	}
    	if( _sp.getTid().isEmpty() ){
    		Message msg = new Message();
    		msg.arg1 = -1;
    		h.sendMessage(msg);
    		Utils.showDialog(R.string.result_title_err, R.string.excepton_bind_scooter, c, null);
    		return;
    	}
    	_cmd.send(trcd, seq, keys, c, h, mode);
    }
    public void abort( int seq ){
    	if( _cmd != null )
    		_cmd.abort(seq);
    }
    
    public void sendHttps(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode) {
    	CmdHttps http = new CmdHttps(  _cmdBuild );
    	if( http == null ){
    		Utils.response( 1, R.string.exception_memory, h );
    	} else {
    		http.send(trcd, seq, keys, c, h, mode);
    	}
    }
    public void sendHttpsPut(String url, List<BasicNameValuePair> params, Context c, Handler h, int mode) {
    	
    	//added by ycf on 20150724 begin 
    	if(!NetworkUtils.isConnect( c ) ) {
    		ToastUtil.makeText(c, R.string.title_network_error, Toast.LENGTH_SHORT);
			return;
    	}
    	//added by ycf on 20150724 end 
    	
    	ConnHttp.sendHttpsPut(h, params, url, mode);
    }
    
    public void sendHttpsPost(String url, List<BasicNameValuePair> params, Context c, Handler h, int mode) {
    	
    	//added by ycf on 20150724 begin 
    	if(!NetworkUtils.isConnect( c ) ) {
    		ToastUtil.makeText(c, R.string.title_network_error, Toast.LENGTH_SHORT);
			return;
    	}
    	//added by ycf on 20150724 end 
    	
    	ConnHttp.sendHttpsPost(h, params, url, mode);
    }
    
    public void sendHttpsGet(String url, List<BasicNameValuePair> params, Context c, Handler h, int mode) {
    	
    	//added by ycf on 20150724 begin 
    	if(!NetworkUtils.isConnect( c ) ) {
    		ToastUtil.makeText(c, R.string.title_network_error, Toast.LENGTH_SHORT);
			return;
    	}
    	//added by ycf on 20150724 end 
    	
    	ConnHttp.sendHttpsGet(h, params, url, mode);
    }
    
    public void sendTcp(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode) {
    	
    	//added by ycf on 20150724 begin 
    	if(!NetworkUtils.isConnect( c ) ) {
    		ToastUtil.makeText(c, R.string.title_network_error, Toast.LENGTH_SHORT);
			return;
    	}
    	//added by ycf on 20150724 end 
    	
    	if( _tcp == null ){
    		Utils.response( 1, R.string.exception_memory, h );
    	} else {
    		_tcp.send(trcd, seq, keys, c, h, mode);
    	}
    }
    
    public void setCtrlMode( int mode ){
    	if( mode == CTRL_BT ){
        	_cmd = new CmdBluebooth(_cmdBuild );
    	}
    	else if( mode == CTRL_SMS ){
        	_cmd = new CmdSMS( _cmdBuild );
    	}else{
        	_cmd = _tcp;
    	}
    	ctrlmode = mode;
    	
    	SpUtil.getInstance().setInt( SpUtil.APP_CTRL_MODE, mode );
    }
}
