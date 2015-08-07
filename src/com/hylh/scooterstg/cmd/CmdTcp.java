package com.hylh.scooterstg.cmd;


import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.ToastUtil;
import com.hylh.scooterstg.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;



@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class CmdTcp implements ICommand {
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
	private SpUtil sp;
	public CmdTcp(CmdBuilder cb){
		_cb = cb;
		sp = SpUtil.getInstance();
	}
	
	static public void TestCmd() {
		String txt = "^^^^^^";
		ConnTcp.Execute(null, txt, Utils.echotcp, 0);
	}

	@Override
	public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int type){
		if( c != null ){
//			if( NetworkUtils.isConnect( c ) ) {
				String cmd = _cb.tcpString(trcd, keys);
				if( cmd != null && cmd.length() > 0 ){
					cmdInfo.put( seq,  new CmdInfo( h, c, type ) );
					ConnTcp.Execute(handle, cmd, Utils.mobitcp, seq );
				} else{
					if( Command.MODE_UNKNOWN == type ){
						Utils.showDialog( "99999999", R.string.exception_cmd_not_support, c, null );
					} else if( Command.MODE_TOAST == type ){
						ToastUtil.makeText( c, R.string.exception_cmd_not_support, Toast.LENGTH_SHORT);
					}
					Utils.response( -1, null, h );
				}
//			} else {
//				Utils.showDialog( "99999999", R.string.exception_network, c );
//				Utils.response( -1, null, h );
//			}
		} else {
			String cmd = _cb.tcpString(trcd, keys); 
			if( cmd != null && cmd.length() > 0 ){
				cmdInfo.put( seq,  new CmdInfo( h, c, type ) );
				ConnTcp.Execute(handle, cmd, Utils.mobitcp, seq );
			}
		}
	}

	@Override
	public void abort(int seq) {
		// TODO Auto-generated method stub
		cmdInfo.remove( seq );
	}
	
	public class CmdInfo {
		public Handler handle;
		public Context context;
		public int type;
		
		public CmdInfo( Handler h, Context c, int t ){
			handle = h;
			context = c;
			type = t;
		}
	}
	
	public static void process( CmdInfo info, Message msg ){
		if( msg.arg1 == 0 ){
			normal( info, msg );
		} else if( msg.arg1 == 1 ){
			if( Command.MODE_UNKNOWN == info.type ){
				Utils.showDialog( "99999999", Integer.parseInt( msg.obj.toString() ), info.context, null );
			} else if( Command.MODE_TOAST == info.type ){
				ToastUtil.makeText( info.context, Integer.parseInt( msg.obj.toString() ), Toast.LENGTH_SHORT);
			}
			Utils.response(-1, null, info.handle);
		}
		else{
			if( Command.MODE_UNKNOWN == info.type ){
				Utils.showDialog( "99999999", msg.obj.toString(), R.drawable.icon_error, info.context, null );
			} else if( Command.MODE_TOAST == info.type ){
				ToastUtil.makeText( info.context, msg.obj.toString(), Toast.LENGTH_SHORT);
			}
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
				if( code.compareTo( "SE1001" ) == 0 
						|| code.compareTo( "SE1002" ) == 0 
						|| code.compareTo( "SE0002" ) == 0 ) {
					MyApplication app = (MyApplication)info.context.getApplicationContext();
					SpUtil sp = SpUtil.getInstance();
					sp.setString( SpUtil.TXN_SSK, "");
					sp.setString(SpUtil.TXN_MACKEY, "");
					sp.setBool( SpUtil.LOGIN_LOCAL, false );
					app.setLogin( 0 );

					Utils.showRelogin( code, info.context );
				}
				else {
					String rcode = head.getString("ret_code");
					String txt = head.getString("ret_msg");
					if( rcode.isEmpty() ){
						rcode = "FFFFFF";
					}
					if( Command.MODE_UNKNOWN == info.type ){
						Utils.showDialog( rcode, txt, R.drawable.icon_error, info.context, null );
					} else if( Command.MODE_TOAST == info.type ){
						ToastUtil.makeText( info.context, txt, Toast.LENGTH_SHORT);
					}
				}
				Utils.response(-10000, json, info.handle);
			}
			else {
				Utils.response(0, json, info.handle);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if( Command.MODE_UNKNOWN == info.type ){
				Utils.showDialog( "88888888", R.string.exception_invalid, info.context, null );
			} else if( Command.MODE_TOAST == info.type ){
				ToastUtil.makeText( info.context, R.string.exception_invalid, Toast.LENGTH_SHORT);
			}
			Utils.response(-1, null, info.handle);
		} catch (Exception e) {
			e.printStackTrace();
			if( Command.MODE_UNKNOWN == info.type ){
				Utils.showDialog( "88888888", R.string.exception_invalid, info.context, null );
			} else if( Command.MODE_TOAST == info.type ){
				ToastUtil.makeText( info.context, R.string.exception_invalid, Toast.LENGTH_SHORT);
			}
			Utils.response(-1, null, info.handle);
		}
	}
}
