package com.hylh.scooterstg.cmd;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.NetworkUtils;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;


@SuppressLint("SimpleDateFormat")
public class CmdHttps { //implements ICommand {
	static public Map<Integer, CmdInfo>	cmdInfo = new TreeMap< Integer, CmdInfo >();
	static public Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CmdInfo info = cmdInfo.get( msg.arg2 );
			if( info != null ) {
				process( info, msg );
			}
		}
	};
	
	private CmdBuilder _cb;
	public CmdHttps(CmdBuilder cb){
		_cb = cb;
		SpUtil.getInstance();
	}

	public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode){
		if( NetworkUtils.isConnect( c ) ) {
			String cmd = _cb.httpString2(trcd, keys);
			if( cmd != null && cmd.length() > 0 ){
				cmdInfo.put( seq,  new CmdInfo( h, c, mode ) );
//				ConnHttp.sendHttpsPost(handle, cmd, Utils.httpsuri, seq );
			} else{
				Utils.showDialog( "99999999", R.string.exception_system, c, null );
				Utils.response( -1, null, h );
			}
		} else {
			Utils.showDialog( "99999999", R.string.exception_network, c, null );
			Utils.response( -1, null, h );
		}
	}

	public void abort(int seq) {
		// TODO Auto-generated method stub
		cmdInfo.remove( seq );
	}
	
	public class CmdInfo {
		public Handler handle;
		public Context context;
		public int mode;
		
		public CmdInfo( Handler h, Context c, int m ){
			handle = h;
			context = c;
			mode = m;
		}
	}
	
	public static void process( CmdInfo info, Message msg ){
 		if( msg.arg1 == 0 ){
			normal( info, msg );
		} else if( msg.arg1 == 1 ){
			Utils.showDialog( "99999999", Integer.parseInt( msg.obj.toString() ), info.context, null );
			Utils.response(-1, null, info.handle);
		}
		else{
			Utils.showDialog( "99999999", msg.obj.toString(), R.drawable.icon_error, info.context, null );
			Utils.response(-1, null, info.handle);
		}
	}
	
	public static void normal( CmdInfo info, Message msg ){
		try {
			JSONTokener jsonParser = new JSONTokener( (String) msg.obj );
			JSONObject json = (JSONObject) jsonParser.nextValue();
			JSONObject head = json.getJSONObject("sys_head");
			String code = head.getString("ret_code");
			if (code.compareTo("000000") != 0) {
				if( code.compareTo( "SE000101" ) == 0 || code.compareTo( "SE000102" ) == 0 ) {
					MyApplication app = (MyApplication)info.context.getApplicationContext();
					SpUtil sp = SpUtil.getInstance();

					String txt = head.getString("ret_msg");
					sp.setString( SpUtil.TXN_SSK, "");
					sp.setString(SpUtil.TXN_MACKEY, "");
					Utils.showDialog( code, txt, R.drawable.icon_error, info.context, null );
				}
				else {
					String txt = head.getString("ret_msg");
					Utils.showDialog( code, txt, R.drawable.icon_error, info.context, null );
				}
				Utils.response(-1, null, info.handle);
			}
			else {
				Utils.response(0, json, info.handle);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showDialog( "88888888", R.string.exception_invalid, info.context, null );
			Utils.response(-1, null, info.handle);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.showDialog( "88888888", R.string.exception_invalid, info.context, null );
			Utils.response(-1, null, info.handle);
		}
	}
}
