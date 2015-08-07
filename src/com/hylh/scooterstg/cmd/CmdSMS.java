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

public class CmdSMS implements ICommand {
	static public Map<Integer, CmdInfo>	cmdInfo = new TreeMap< Integer, CmdInfo >();
	static public Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CmdInfo info = cmdInfo.get( msg.arg2 );
			if( info != null ) {
				if( msg.arg1 == 0 ){
//					Utils.showDialog( R.string.result_title_suc, R.string.set_suc, info.context );
					Utils.response(0, null, info.handle);
				} else if( msg.arg1 == 1 ){
					Utils.showDialog( "99999999", Integer.parseInt( msg.obj.toString() ), info.context, null );
					Utils.response(-1, null, info.handle);
				}
				else{
					Utils.showDialog( "99999999", msg.obj.toString(), R.drawable.icon_error, info.context, null );
					Utils.response(-1, null, info.handle);
				}
			}
		}
	};

	private CmdBuilder _cb;
	private SpUtil sp;
	public CmdSMS(CmdBuilder cb){
		_cb = cb;
		sp = SpUtil.getInstance();
	}

	@Override
	public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int mode){
		String cmd = _cb.smsString(trcd, keys);
		if( cmd != null && cmd.length() > 0 ){
			cmdInfo.put( seq,  new CmdInfo( h, c ) );
			SMSUtils.SendCmd(handle, sp.getString( SpUtil.TXN_DPHONE), cmd, seq, c);
//			SMSUtils.SendCmd(handle, "18948323337", cmd, seq, c);
		} else{
			Utils.showDialog( "99999999", R.string.exception_system, c, null );
			Utils.response( -1, null, h );
		}
	}
	
	public class CmdInfo {
		public Handler handle;
		public Context context;
		
		public CmdInfo( Handler h, Context c ){
			handle = h;
			context = c;
		}
	}

	@Override
	public void abort(int seq) {
		// TODO Auto-generated method stub
		cmdInfo.remove( seq );
	}
}
