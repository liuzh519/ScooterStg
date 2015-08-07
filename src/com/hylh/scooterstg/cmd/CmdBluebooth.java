package com.hylh.scooterstg.cmd;


import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CmdBluebooth implements ICommand {
	// public BluetoothServer btsrv;
	static int times = 1;
	static int retry;
	static String btkey;
	static String mTrcd;
	static int mMode;
	static int mSeq;
	static JSONObject mKeys;
	static Context mC;
	Handler mH;

	static public Map<Integer, CmdInfo>	cmdInfo = new TreeMap< Integer, CmdInfo >();
	public Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CmdInfo info = cmdInfo.get( msg.arg2 );
			if( info != null ) {
				if( msg.arg1 == 0 ){
					JSONObject json = new JSONObject();
					if( _cb.parserString( info.trcd, json, msg.obj.toString() ) ){
						Utils.response(0, json, info.handle);
					} else {
						Utils.showDialog( "99999999", msg.obj.toString(), R.drawable.icon_error, info.context, null );
						Utils.response(-1, null, info.handle);
					}
					_cb.saveBtkey();
//					Utils.showDialog( R.string.result_title_suc, R.string.set_suc, info.context );
				} else if( msg.arg1 == 1 ){
					Utils.showDialog( "99999999", Integer.parseInt(msg.obj.toString()), info.context, null );
					Utils.response(-1, null, info.handle);
				} else if( msg.arg1 == 10000 && times <= 3 ){
					times ++;
					resend();
				}
				else{
					Utils.showDialog( "99999999", Integer.parseInt( msg.obj.toString() ), info.context, null );
					Utils.response(-1, null, info.handle);
				}
			}
		}
	};

	private CmdBuilder _cb;
	private SpUtil sp;
	public CmdBluebooth(CmdBuilder cb){
		_cb = cb;
		sp = SpUtil.getInstance();
		
	}

	@Override
	public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode){
		times = 1;
		mTrcd = trcd;
		mSeq = seq;
		mKeys = keys;
		mC = c;
		mH = h;
		mMode = mode;
		_cb.resetRetry();
		String cmd = _cb.btString(trcd, keys);
		if( cmd != null && cmd.length() > 0 ){
			Log.i("bluebooth" , cmd);
			cmdInfo.put( seq,  new CmdInfo( trcd, h, c, mode ) );
			BluetoothServer.SendCmd(sp.getString( SpUtil.TXN_TID), handle, cmd, seq, c);
		} else{
			Utils.showDialog( "99999999", R.string.exception_cmd_not_support, c, null );
			Utils.response( -1, null, h );
		}
	}
	
	public void resend(){
		String cmd = _cb.btString(mTrcd, mKeys);
		if( cmd != null && cmd.length() > 0 ){
			Log.i("bluebooth" , cmd);
			cmdInfo.put( mSeq,  new CmdInfo( mTrcd, mH, mC, mMode ) );
			BluetoothServer.SendCmd(sp.getString( SpUtil.TXN_TID), handle, cmd, mSeq, mC);
//			BluetoothServer.SendCmd("6131007101", handle, cmd, seq, c);
		} else{
			Utils.showDialog( "99999999", R.string.exception_cmd_not_support, mC , null);
			Utils.response( -1, null, mH );
		}
	}
	
	public class CmdInfo {
		public Handler handle;
		public Context context;
		public String trcd;
		public int		mode;
		
		public CmdInfo( String t, Handler h, Context c, int m ){
			trcd = t;
			handle = h;
			context = c;
			mode = m;
		}
	}

	@Override
	public void abort(int seq) {
		// TODO Auto-generated method stub
		cmdInfo.remove( seq );
	}
}
