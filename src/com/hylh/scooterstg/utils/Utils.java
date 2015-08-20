package com.hylh.scooterstg.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.LoginActivity;

public class Utils {
//	public static TabActivity maintab;
	public static Map<String, String> keys = new HashMap<String, String>();
	public static String phoneNo;
	public static String phoneNoMask;
	public static String domain = "http://www.anbaostar.com";
	public static String cdomain = "http://www.anbaostar.com:7070";
//	public static String httpsuri = "https://www.anbaostar.com/mobi/do.html";
	public static String httpsuri = "https://www.anbaostar.com/7070/do.aspx";
	
	final public static String urlLogin = "https://stg-api.scootawayscooters.com/api/1.0/user/login";
	final public static String urlParking = "https://stg-api.scootawayscooters.com/api/1.0/scooter/parking";
	final public static String urlParkStation = "https://stg-api.scootawayscooters.com/api/1.0/scooter/parking/info";
	final public static String urlScooter = "https://stg-api.scootawayscooters.com/api/1.0/scooter";
	final public static String urlCoupon = "https://stg-api.scootawayscooters.com/api/1.0/coupon";
	final public static String urlAgreement  = "https://stg-api.scootawayscooters.com/api/1.0/user/acknowledgment";
	final public static String urlUser = "https://stg-api.scootawayscooters.com/api/1.0/user";

//	final public static String urlLogin = "https://api.scootawayscooters.com/api/1.0/user/login";
//	final public static String urlParking = "https://api.scootawayscooters.com/api/1.0/scooter/parking";
//	final public static String urlParkStation = "https://api.scootawayscooters.com/api/1.0/scooter/parking/info";
//	final public static String urlScooter = "https://api.scootawayscooters.com/api/1.0/scooter";
//	final public static String urlCoupon = "https://api.scootawayscooters.com/api/1.0/coupon";
//	final public static String urlAgreement  = "https://api.scootawayscooters.com/api/1.0/user/acknowledgment";
//	final public static String urlUser = "https://api.scootawayscooters.com/api/1.0/user";
	
	final public static String urlLegal = "https:///stg.scootawayscooters.com//legal.html";
	final public static String urlProfile = "https://stg.scootawayscooters.com/register.html";//modify by ycf on 20150820
	final public static String urlReg = "https://stg.scootawayscooters.com/register.html";
	final public static String urlVideo = "http://youtu.be/yQW3QcjBrJU";
	
	public final static String urlForgetPwd = "https://stg.scootawayscooters.com/forgot_password.html";//add by ycf on 20150626
	public final static int NOTIFY_INTERVAL_TIME = 1000 * 2 * 60 * 60;//added by ycf on 20150629
	public static final double EARTH_RADIUS = 6378.137;//added by ycf on 20150706


	public static JSONObject veclState = null;
	public static boolean bindSev = false;
	public static long lgtime = 0;
	public static String bluetoothPsw = "0000";
	public static String bluetoothAddr = "00:18:E4:1B:64:93";
	public static int socktimeout = 15000;
	public static int sockport = 7310;
	public static int alarmport = 6031;
//	public static int mobitcp = 7310;
	public static int mobitcp = 7050;
	public static int mobifile = 7311;
	public static int echotcp = 7777;
	public static String conurl = "www.anbaostar.com";
	public static String conip = "113.106.94.222";
	public static String lang = "en";
	public static boolean 	isForceground = false;
	public static int 		initCount = 0;
	public static boolean    mShowCtrl = false;

	static public int resTitle = 0;
	static public String imgName = "";

	static public ProgressDialog procdlg;
	
	public static Timer noticeTimer;
	
	public static AlertDialog aldlg;
	public static boolean checked = false;
	static public void checkDomain(){
		if( checked )
			return;
		
		try{
			InetAddress.getByName("www.anbaostar.com");
		} catch( UnknownHostException e ){
			conurl = "113.106.94.222";
			domain = "http://113.106.94.222";
			cdomain = "http://113.106.94.222:16210";
			httpsuri = "https://113.106.94.222:8810/smart/mobi/do.html";
		}
		checked = true;
	}

	public static void init(Context context) {
		lang = context.getResources().getString( R.string.lang ).toString();

 
//		if (!MyService.isServiceRunning(context)) {
//			Intent it = new Intent(context, MyService.class);
//			context.startService(it);
//		}
		

//		SIMCardInfo siminfo = new SIMCardInfo(context);
//		if( null != siminfo )
//		{
//			phoneNo = siminfo.getNativePhoneNumber();
//			if( phoneNo.length() == 14 )
//			{
//				phoneNo = phoneNo.substring(3);
//			}
//			else if( phoneNo.length() != 11 )
//			{
//				phoneNo = "";
//			}
//			
//			if( phoneNo.length() > 0 )
//			{
//				phoneNoMask = new String(phoneNo).substring(0, 3) + "****" + new String(phoneNo).substring(7);	
//			}
//			else
//			{
//				phoneNoMask = "";
//			}
//		}
	}

	static public void login(Context context)  
	{
		SpUtil sp = SpUtil.getInstance();
		sp.setInt( SpUtil.LOGIN_KEEP, 0 );
		sp.setSTKey( "" );
		sp.setTid("" );
		Intent i = new Intent();
		i.putExtra( "only", 1 );
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setClass( context, LoginActivity.class );
		context.startActivity(i);
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	public static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		int length = data.length;
		for (int i = 0; i < length; ++i) {
			int halfbyte = (data[i] >>> 4) & 0x0F;

			if ((0 <= halfbyte) && (halfbyte <= 9))
				buf.append((char) ('0' + halfbyte));
			else
				buf.append((char) ('A' + (halfbyte - 10)));

			halfbyte = data[i] & 0x0F;
			if ((0 <= halfbyte) && (halfbyte <= 9))
				buf.append((char) ('0' + halfbyte));
			else
				buf.append((char) ('A' + (halfbyte - 10)));
		}
		return buf.toString();
	}
	
	public static void showProcess(Context context) {
		if( procdlg != null ){
			procdlg.dismiss();
			procdlg = null;
		}
		procdlg = new ProgressDialog(context);
		procdlg.setCancelable(false);
		procdlg.setMessage(context.getResources().getString(R.string.execute_plsease_wait));

		procdlg.show();
	}
	public static void dismissProcess() {
		if( procdlg != null ){
			procdlg.dismiss();
			procdlg = null;
		}
	}
	public static void showRelogin(String code, Context ctx) {
		if( null == ctx ){
			return;
		}
		final Context lctx = ctx;
		MyApplication app = (MyApplication)ctx.getApplicationContext();
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle(code)
		.setMessage(ctx.getResources().getString(R.string.login_pattren_error))
//		.setIcon(icon)
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						Intent intent = new Intent (lctx, LoginActivity.class);
						lctx.startActivity(intent);
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}
	
	public static void showDialog(Context ctx) {
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle( ctx.getResources().getString(R.string.result_title_suc) )
		.setMessage(ctx.getResources().getString(R.string.result_success))
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}

	public static void showDialog(int title, int txt, Context ctx, DialogInterface.OnClickListener click) {
		if( null == ctx ){
			return;
		}
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle( ctx.getResources().getString(title) )
		.setMessage(ctx.getResources().getString(txt))
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}
	
	public static void showDialog(String title, int txt, Context ctx, DialogInterface.OnClickListener click) {
		if( null == ctx ){
			return;
		}
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle( title )
		.setMessage(ctx.getResources().getString(txt))
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}
	public static void showDialog(int title, String txt, Context ctx, DialogInterface.OnClickListener click) {
		if( null == ctx ){
			return;
		}
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle( ctx.getResources().getString(title) )
		.setMessage(txt)
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}
	
	public static void showDialog(String title, String txt, int icon, Context ctx, DialogInterface.OnClickListener click) {
		if( null == ctx ){
			return;
		}
		aldlg = new AlertDialog.Builder(ctx)
		.setTitle(title)
		.setMessage(txt)
		.setIcon(icon)
		.setPositiveButton(
				ctx.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}
	
	public static void showDialog(int title, int txt, int icon, Context act, DialogInterface.OnClickListener click) {
		if( null == act ){
			return;
		}
		aldlg = new AlertDialog.Builder(act)
		.setTitle( act.getResources().getString(title) )
		.setMessage(act.getResources().getString(txt))
		.setIcon(icon)
		.setPositiveButton(
				act.getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}

	public static String getGMTStr(String date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(4, 6)) - 1,
				Integer.parseInt(date.substring(6, 8)),
				Integer.parseInt(date.substring(8, 10)),
				Integer.parseInt(date.substring(10, 12)),
				Integer.parseInt(date.substring(12, 14)));
		return "" + cal.getTimeInMillis() / 1000;
	}

	static public void response(int arg1, Object obj, Handler h) {
		if (null != h) {
			Message m = new Message();
			m.obj = obj;
			m.arg1 = arg1;
			h.sendMessage(m);
		}
	}

	static public void response(int arg1, String obj, Handler h) {
		if (null != h) {
			Message m = new Message();
			m.obj = obj;
			m.arg1 = arg1;
			h.sendMessage(m);
		}
	}

	static public void response(int arg1, int objid, Handler h) {
		if (null != h) {
			Message m = new Message();
			m.obj = objid;
			m.arg1 = 1;
			h.sendMessage(m);
		}
	}
	static public void response(int arg1, int arg2, Object obj, Handler h) {
		if (null != h) {
			Message m = new Message();
			m.obj = obj;
			m.arg1 = arg1;
			m.arg2 = arg2;
			h.sendMessage(m);
		}
	}
	
	
	//added by ycf on 20150706 begin
	/**
	 * 
	 * 计算2点之间的距离
	 * @param lat1  纬度
	 * @param lng1  经度
	 * @param lat2
	 * @param lng2
	 * @return
	 *double
	 * @exception
	 * @since  1.0.0
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = Math.asin(Math.sqrt(Math.pow(Math.sin(a/2), 2)
        +Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2), 2)));
       
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
     }

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	//added by ycf on 20150706 end
	
	//added by ycf on 20150716 begin
	public static Timer getNoticeTimer(){
		if(noticeTimer == null){
			noticeTimer = new Timer(true);
		}
		return noticeTimer;
	}
	
	public static void cancelNoticeTimer(){
		if(noticeTimer != null){
			noticeTimer.cancel();
		}
	}
	
	//added by ycf on 20150716 end
	
	
	//added by ycf on 20150723 begin
	/**
	 * 
	 * 显示自定义对话框
	 * @param layoutId  布局ID
	 * @param viewId    视图ID
	 * @param context
	 */
//	public static void showUserDefinedDialog(int layoutId, int viewId, Context context){
//		
//		try{
//			Activity activity = (Activity) context;
//			View layout = activity.getLayoutInflater().inflate(layoutId,
//					(ViewGroup) activity.findViewById(viewId),false);
//			new AlertDialog.Builder(context)
//				.setTitle("ppppppppppp")
//				.setView(layout).show();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	
//	}
	//added by ycf on 20150723 end
	
	
//	public static void startActivity(Context context, Class<VeclImageView> class1) {
//		// TODO Auto-generated method stub
//        Intent intent = new Intent(context, class1);
//        context.startActivity(intent);
//	}
	
	
	public static boolean isEmpty(String source){
		if(source == null || "".equals(source)){
			return true;
		}
		
		return false;
	}
}
