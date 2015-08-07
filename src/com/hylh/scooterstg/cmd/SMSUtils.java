package com.hylh.scooterstg.cmd;

import org.json.JSONException;
import org.json.JSONObject;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.Utils;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSUtils {
	private static mServiceReceiver mReceiver01 = null;
	private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	private static Handler hand;
	private static String _sim;
	private static int _seq;


	public SMSUtils() {
		// TODO Auto-generated constructor stub
	}

	static public void init(Context context, String sim) {
		if( null == mReceiver01 )
		{
			IntentFilter mFilter01;
			mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
			mReceiver01 = new mServiceReceiver();
			if( mReceiver01 != null )
			{
				context.registerReceiver(mReceiver01, mFilter01);
			}
		}
		_sim = sim;
	}

	static public void release(Context context) {
		if( null != mReceiver01 )
		{
			try
			{
				context.unregisterReceiver( mReceiver01 );
				mReceiver01 = null;
			}
			catch( IllegalArgumentException e )
			{
				e.printStackTrace();
			}
		}
	}

	static public void SendCmd(Handler h, String sim, String msg, int seq, Context context) {
		hand = h;
		_seq = seq;
		
		if( sim == null || sim.length() < 6 ){
			Utils.response( 1, _seq, R.string.exception_msm_sim, h);
			return ;
		}
		
		SmsManager sms = SmsManager.getDefault();
		if (null != sms) {
			Intent itSend = new Intent(SMS_SEND_ACTIOIN);

			/* sentIntent参数为传送后接受的广播信息PendingIntent */
			PendingIntent mSendPI = PendingIntent.getBroadcast( context.getApplicationContext(), 0, itSend, 0);
			sms.sendTextMessage(sim, null, msg, mSendPI, null);
		} 
		else {
			Utils.response( 1, _seq, R.string.exception_msmmgr, h);
		}
	}

	static public class mServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			// mTextView01.setText(intent.getAction().toString());
			if (intent.getAction().equals(SMS_SEND_ACTIOIN)) {
				try {
					/* android.content.BroadcastReceiver.getResultCode()方法 */
					// Retrieve the current result code, as set by the previous
					// receiver.
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						/* 发送短信成功 */
						try {
							JSONObject json = new JSONObject();
							JSONObject res = new JSONObject();
							res.put("code", "000000");
							res.put("msg", "");
							json.put("result", res);
							Utils.response(0, _seq, json.toString(), hand);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i("SMSUtils", "发送短信成功");
						
						ToastMaster.makeText(context, "命令通过短信发送",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						/* 发送短信失败 */
						Utils.response( 1, _seq, R.string.exception_msmmgr, hand );
						Log.i("SMSUtils", "发送短信失败");
						ToastMaster.makeText(context, "命令发送,请检查网络",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						break;
					}
				} catch (Exception e) {
					// mTextView01.setText(e.toString());
					e.getStackTrace();
				}
			} 
		}
	}
}
