package com.hylh.scooterstg.fragments;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.ErrUtils;
import com.hylh.scooterstg.utils.MediaUtil;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.ToastUtil;
import com.hylh.scooterstg.utils.Utils;

public class CtrlTs001Fragment extends Fragment 
	implements StatusUtils.OnStatusEvent {
	
	private final static String TAG = "CtrlTs001Fragment";  
	
	private MyApplication mApp;
	private MainActivity mContext;
	private View mBaseView;
	

	private Button btnLock;
	private Button btnUnlock;
	private Button btnEsat;
	private Button btnReturn;
	private RelativeLayout rent;
	private RelativeLayout unrent;
	private TextView mNumber;
	private TextView mTimer;
	
	//added by ycf on 20150725 begin
    protected RelativeLayout returnLayout;
    protected RelativeLayout supportRtnLayout;
    protected RelativeLayout rtnFailLayout;
    protected Button btnReturnNow;
    protected Button btnOpenSeat;
    protected Button btnCancle;
    protected Button btnSupport;
    protected Button btnCle;
    protected TextView rfTitleTv;
    protected TextView rfMsgTv;
  //added by ycf on 20150725 end
    
    //added by ycf on 20150806 begin
    private RelativeLayout alphaRl;
//    private RelativeLayout paymentFailRl;
    private Button okBtn;
    private Button failBtn;
    private TextView payFailTitleTipTv;
    private TextView payFailMsgTv;
    //added by ycf on 20150806 end
    

	private int lastcmd = 0;
	private static SpUtil mSp;
	
	Handler mUnlock = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utils.dismissProcess();
			String code;
			if (msg.arg1 == 0) {
				StatusUtils.setDelay( 10 );
				MediaUtil.onAction(lastcmd, mContext );
				Utils.showDialog(mContext);
			}else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			} else if( msg.arg1 == -10 ){
				code = (String)msg.obj;
				if( code.compareTo("1002") == 0 ){
					Utils.login(mContext);
				} else {
					//modify by ycf on 20150725 begin
					//瑙ｉ帠閫ｇ窔澶辨晽椤ず
//					Utils.showDialog("Error", ErrUtils.getUnlock( code ), R.drawable.icon_error, mContext, null);
					ToastUtil.makeText(mContext, getResources().getString(R.string.text_ctrl_scootaway_fail), Toast.LENGTH_SHORT);
					//modify by ycf on 20150725 end
				}
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
		}
	};
	Handler mEsat = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utils.dismissProcess();
			
			if (msg.arg1 == 0) {
				StatusUtils.setDelay( 10 );
				MediaUtil.onAction(lastcmd, mContext );
				Utils.showDialog(mContext);
			}else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			} else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				if( code.compareTo("1002") == 0 ){
					Utils.login(mContext);
				} else {
					//modify by ycf on 20150725 begin
					//闁嬪熬绠遍�绶氬け鏁楅’绀�
//					Utils.showDialog("Error", ErrUtils.getTrunk( (String)msg.obj ), R.drawable.icon_error, mContext, null);
					ToastUtil.makeText(mContext, getResources().getString(R.string.text_ctrl_scootaway_fail), Toast.LENGTH_SHORT);
					//modify by ycf on 20150725 end
				}
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
		}
	};

	Handler mRefresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONObject tent = json.getJSONObject( "rental" );
					if( tent.has("id") ){
						mSp.setTid( tent.getString("id"));
						mSp.setTName( tent.getString("number"));
					} else {
						mSp.setTid( "" );
						mSp.setTName( "" );
					}
					upateUi();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	//added by ycf on 20150626 begin
	Handler mLocation = new Handler(){
		
		public void handleMessage(Message msg) {
			
			Utils.dismissProcess();
			
			Log.i(TAG, "use machine positon to locate the park");
			
			if( msg.arg1 == 0 ){
				try {
					//added by ycf on 20150725 begin 
					String code = (String)msg.obj;
					if( "1000".equals(code)){
						ToastUtil.makeText(mContext, getResources().getString(R.string.result_title_suc), Toast.LENGTH_SHORT);
					} 
					//added by ycf on 20150725 end
					
					JSONObject json = (JSONObject) msg.obj;
					JSONObject tent = json.getJSONObject( "rental" );
					if( tent.has("id") ){
						mSp.setTid( tent.getString("id"));
						mSp.setTName( tent.getString("number"));
					} else {
						mSp.setTid( "" );
						mSp.setTName( "" );
					}

					String sLoc = tent.getString( "location" );
					JSONTokener jsonParser = new JSONTokener( sLoc );
					JSONObject location = (JSONObject) jsonParser.nextValue();
			    	JSONArray coordinates = location.getJSONArray( "coordinates" );

					double lat = coordinates.getDouble( 0 );
					double lng = coordinates.getDouble( 1 );
					
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
					params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
					params.add(new BasicNameValuePair("lat", String.format( "%f", lat) ));  
					params.add(new BasicNameValuePair("lng", String.format( "%f", lng)));
					params.add(new BasicNameValuePair("radius", "10" ));  
					
					mApp.getCmd().sendHttpsGet( Utils.urlParking, params, mApp, mPark, Command.MODE_TOAST);
					
					upateUi();
					
					//3. 鐢ㄦ埗绉熻粖姣忓叐灏忔檪璺冲嚭鎻愮ず (Local notification)
					Utils.cancelNoticeTimer();//added by ycf on 20150716
					
					//
					
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());

				}
			}else if( msg.arg1 == -1 ){//added by ycf on 20150725 begin //閭勮粖閫ｇ窔澶辨晽椤ず
				ToastUtil.makeText(mContext, getResources().getString(R.string.text_ctrl_scootaway_fail), Toast.LENGTH_SHORT);
			}else if( msg.arg1 == -2){
				
				supportRtnLayout.setVisibility(View.VISIBLE);
				
				//Utils.showDialog("Error", getResources().getString(R.string.text_ctrl_scootaway_fail), R.drawable.icon_error, mContext, null);
				
			}else if( msg.arg1 == -10){
				
				String code = (String)msg.obj;
				if("1107".equals(code)){
					
					rfTitleTv.setText(getResources().getString(R.string.title_return_fail_1));
					rfMsgTv.setText(getResources().getString(R.string.msg_return_fail_1));
					rtnFailLayout.setVisibility(View.VISIBLE);
				}
				
				if(code != null && 1000 < Integer.parseInt(code) && Integer.parseInt(code) < 1007){
					rfTitleTv.setText(getResources().getString(R.string.title_return_fail_2));
					rfMsgTv.setText(getResources().getString(R.string.msg_return_fail_2));
					rtnFailLayout.setVisibility(View.VISIBLE);
				}
				
				else if("1201".equals(code)){//added by ycf on 20150806 begin
					payFailMsgTv.setText(getResources().getString(R.string.text_payment_fail));
					payFailTitleTipTv.setText(getResources().getString(R.string.title_payment_fail_2));
					alphaRl.setVisibility(View.VISIBLE);
//					paymentFailRl.setVisibility(View.VISIBLE);
				}
				
				else if("1105".equals(code)){
					payFailMsgTv.setText(getResources().getString(R.string.text_payment_fail_3));
					payFailTitleTipTv.setText(getResources().getString(R.string.title_payment_fail_3));
					alphaRl.setVisibility(View.VISIBLE);
//					paymentFailRl.setVisibility(View.VISIBLE);
				}
				//added by ycf on 20150806 end
			}
			
			//added by ycf on 20150725 end
			
			
		};
	};
	//added by ycf on 20150626 end
	
	Handler mUnBind = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utils.dismissProcess();
			if( msg.arg1 == 0 ){
				mSp.setTid("");
				mSp.setTName( "" );
				mSp.setTime(0);
	    		rent.setVisibility( View.GONE);
	    		unrent.setVisibility( View.VISIBLE );
				updateUi();
	    		
				Utils.showDialog(mContext);
				MediaUtil.onAction(Command.CMD_UNBIND, mContext );
//				mContext.setIndex(0);
				
				//3. 鐢ㄦ埗绉熻粖姣忓叐灏忔檪璺冲嚭鎻愮ず (Local notification)
				Utils.cancelNoticeTimer();//added by ycf on 20150716
				
				return;
			}else if( msg.arg1 == -1 ){//added by ycf on 20150725 begin //閭勮粖閫ｇ窔澶辨晽椤ず
				ToastUtil.makeText(mContext, getResources().getString(R.string.text_ctrl_scootaway_fail), Toast.LENGTH_SHORT);
			}else if( msg.arg1 == -2 ){
				supportRtnLayout.setVisibility(View.VISIBLE);
//				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				
				if( "1000".equals(code)){
					ToastUtil.makeText(mContext, getResources().getString(R.string.result_title_suc), Toast.LENGTH_SHORT);
				}else if("1107".equals(code)){
					// rfTitleTv  rfMsgTv
					rfTitleTv.setText(getResources().getString(R.string.title_return_fail_1));
					rfMsgTv.setText(getResources().getString(R.string.msg_return_fail_1));
					rtnFailLayout.setVisibility(View.VISIBLE);
				}else if(code != null && 1000 < Integer.parseInt(code) && Integer.parseInt(code) < 1007){
					rfTitleTv.setText(getResources().getString(R.string.title_return_fail_2));
					rfMsgTv.setText(getResources().getString(R.string.msg_return_fail_2));
					rtnFailLayout.setVisibility(View.VISIBLE);
				}else if("1201".equals(code)){//added by ycf on 20150806 begin
					payFailMsgTv.setText(getResources().getString(R.string.text_payment_fail));
					payFailTitleTipTv.setText(getResources().getString(R.string.title_payment_fail_2));
					alphaRl.setVisibility(View.VISIBLE);
				}else if("1105".equals(code)){
					payFailMsgTv.setText(getResources().getString(R.string.text_payment_fail_3));
					payFailTitleTipTv.setText(getResources().getString(R.string.title_payment_fail_3));
					alphaRl.setVisibility(View.VISIBLE);
				}else if( code.compareTo("1002") == 0 ){//added by ycf on 20150806 end
					Utils.login(mContext);
				} else {
					Utils.showDialog("Error", ErrUtils.getReturn( (String)msg.obj ), R.drawable.icon_error, mContext, null);
				}
			}else{
				StatusUtils.setDelay(0);
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
			
		
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
			params.add(new BasicNameValuePair("query", "rental" ));  
			mApp.getCmd().sendHttpsGet(Utils.urlUser, params, mApp, mRefresh, Command.MODE_TOAST);
		}
	};
	
	Handler mPark = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				String id = "";
				double dis = -1;
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONArray rows = json.getJSONArray( "ps" );
					for( int i = 0; i < rows.length(); i ++ )
					{
						JSONObject park = rows.getJSONObject(i);
						if( park.has( "location" ) ){
							try {
								String sLoc = park.getString( "location" );
								JSONTokener jsonParser = new JSONTokener( sLoc );
								JSONObject location = (JSONObject) jsonParser.nextValue();
								
						    	JSONArray coordinates = location.getJSONArray( "coordinates" );

								double lat = coordinates.getDouble( 0 );
								double lng = coordinates.getDouble( 1 );
								double sqrt = Math.sqrt( (StatusUtils.mLat - lat)*(StatusUtils.mLat - lat) 
										+ (StatusUtils.mLng - lng) * (StatusUtils.mLng - lng) );
								if( dis < 0 || dis > sqrt ){
									dis = sqrt;
									id = park.getString("id");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (JSONException e) {
					
				}
				if( dis >= 0 ){
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
					params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
					params.add(new BasicNameValuePair("command", "return" ));  
					params.add(new BasicNameValuePair("parking_station_id", id ));  
					params.add(new BasicNameValuePair("scooter_id", mSp.getTid() )); 
					
					mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, mContext, mUnBind, Command.MODE_SILENT);
					return;
				} else {
					Utils.showDialog(R.string.error, R.string.exception_park_not_found, R.drawable.icon_error, mContext, null);
				}
			}else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				if( code.compareTo("1002") == 0 ){
					Utils.login(mContext);
				} else {
					Utils.showDialog("Error", ErrUtils.getPark( (String)msg.obj ), R.drawable.icon_error, mContext, null);
				}
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
			Utils.dismissProcess();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApp = MyApplication.getInstance();
		mContext=(MainActivity)getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_dev_ctrl_ts001, null);
		findView();
		init();
		return mBaseView;
	}
	
	private void findView(){
		lastcmd = Command.CMD_UNKNOWN;

		btnLock = (Button) mBaseView.findViewById(R.id.lock_car);
		btnUnlock = (Button) mBaseView.findViewById(R.id.unlock_car);
		btnReturn = (Button) mBaseView.findViewById(R.id.btn_return);
		btnEsat = (Button) mBaseView.findViewById(R.id.east_car);
		rent = (RelativeLayout)mBaseView.findViewById(R.id.rent);
		unrent = (RelativeLayout)mBaseView.findViewById(R.id.unrent);
		mNumber = (TextView)mBaseView.findViewById(R.id.carname);
		mTimer = (TextView)mBaseView.findViewById(R.id.rlent_time);
		
		//added by ycf on 20150725 begin
		returnLayout = (RelativeLayout)mBaseView.findViewById(R.id.return_layout);
		supportRtnLayout = (RelativeLayout)mBaseView.findViewById(R.id.return_layout2);
		rtnFailLayout = (RelativeLayout)mBaseView.findViewById(R.id.ud_rtn_fail_rlayout);
		btnReturnNow = (Button) mBaseView.findViewById(R.id.btn_return_now);
		btnOpenSeat = (Button) mBaseView.findViewById(R.id.btn_open_seat);
		btnCancle = (Button) mBaseView.findViewById(R.id.btn_cancel);
		btnSupport = (Button) mBaseView.findViewById(R.id.btn_call_support);
		btnCle = (Button) mBaseView.findViewById(R.id.btn_cancel2);
		rfTitleTv = (TextView)mBaseView.findViewById(R.id.tv_rtn_fail_title);
		rfMsgTv = (TextView)mBaseView.findViewById(R.id.tv_rtn_fail_msg);
		this.failBtn = (Button) mBaseView.findViewById(R.id.btn_rtn_fail_ok);  
		//added by ycf on 20150725 end
		
		//added by ycf on 20150806 begin
		this.alphaRl = (RelativeLayout) mBaseView.findViewById(R.id.alpha_layout);
//		this.paymentFailRl = (RelativeLayout) mBaseView.findViewById(R.id.payment_fail_layout);
		this.okBtn = (Button) mBaseView.findViewById(R.id.btn_payment_fail_ok);  
		this.payFailMsgTv = (TextView)mBaseView.findViewById(R.id.tx_payment_fail_msg);
		this.payFailTitleTipTv = (TextView)mBaseView.findViewById(R.id.tx_payment_fail_title);
		//added by ycf on 20150806 end
	}
	
	private void init(){
		mSp = SpUtil.getInstance();
		UpdaeState(null);
		
		btnLock.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				lastcmd = Command.CMD_LOCK;
				StatusUtils.setDelay( Command.TIME_OUT );
				Utils.showProcess(mContext);
				MediaUtil.onVibrate();

				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
				params.add(new BasicNameValuePair("command", "lock" ));  
				params.add(new BasicNameValuePair("scooter_id", mSp.getTid() )); 
				
				mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, mContext, mUnlock, Command.MODE_UNKNOWN);
			}
		});

		btnUnlock.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
		    	if( mSp.getTid().isEmpty() ){
		    		Utils.showDialog(R.string.result_title_err, R.string.excepton_bind_scooter, mContext, null);
		    		return;
		    	}
		    	
				lastcmd = Command.CMD_UNLOCK;
				StatusUtils.setDelay( Command.TIME_OUT );
				Utils.showProcess(mContext);
				MediaUtil.onVibrate();

				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
				params.add(new BasicNameValuePair("command", "unlock" ));  
				params.add(new BasicNameValuePair("scooter_id", mSp.getTid() )); 
				
				mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, mContext, mUnlock, Command.MODE_UNKNOWN);
			}
		});

		//modify by ycf on 20150725 begin
		
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnReturnNow.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnLayout.setVisibility(View.GONE);//added by ycf on 20150813
				AlertDialog dialog = new AlertDialog.Builder(mContext)
				.setTitle(getResources().getString(R.string.confirm))
				.setMessage(
						getResources().getString(R.string.result_info_free_vecl))
				.setPositiveButton(
						getResources().getString(R.string.result_btn),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Utils.showProcess(mContext);
								lastcmd = Command.CMD_UNBIND;

								List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
								params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
//
//								//1. 閭勮粖鏅傦紝鍋滆粖鍫翠綅缃噳鐢ㄨ粖瀛愪綅缃煡瑭紝鑰屼笉鏄敤鎴舵墜姗熶綅缃�
//								//add by ycf on 20150626 begin
//								params.add(new BasicNameValuePair("query", "rental" ));  
//								returnLayout.setVisibility(View.GONE);
//								mApp.getCmd().sendHttpsGet(Utils.urlUser, params, mApp, mLocation, Command.MODE_TOAST);
//								params.clear();
//								//add by ycf on 20150626 end
								
								//modify by ycf on 20150812 begin
								//杩樿溅鏃犻渶浼犲仠杞﹀満ID
								params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
								params.add(new BasicNameValuePair("command", "return" ));  
								params.add(new BasicNameValuePair("scooter_id", mSp.getTid() )); 
								mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, mContext, mUnBind, Command.MODE_SILENT);
								//add by ycf on 20150812 end
							}
						})
				.setNegativeButton(
						getResources()
								.getString(R.string.result_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		dialog.show();

//				Intent intent = new Intent (mContext, MainActivity.class);
//				startActivity(intent);
			
				
			}  
			
		});
	    
		btnOpenSeat.setOnClickListener(openSeatListener);
		btnCancle.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnLayout.setVisibility(View.GONE);
			}
		});
		
		btnSupport.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
			      String number = "8004498219";  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
	                startActivity(intent); 
			}
		});
		btnCle.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnLayout.setVisibility(View.GONE);
				supportRtnLayout.setVisibility(View.GONE);
			}
		});
		
		
		btnEsat.setOnClickListener(openSeatListener);
		
		 //added by ycf on 20150806 begin
	    okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alphaRl.setVisibility(View.GONE);
//				paymentFailRl.setVisibility(View.GONE);
			}
		});
	  //added by ycf on 20150806 end

	    failBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rtnFailLayout.setVisibility(View.GONE);
//				paymentFailRl.setVisibility(View.GONE);
			}
		});
	}

	Button.OnClickListener openSeatListener = new Button.OnClickListener() {
		public void onClick(View v) {
	    	if( mSp.getTid().isEmpty() ){
	    		Utils.showDialog(R.string.result_title_err, R.string.excepton_bind_scooter, mContext, null);
	    		return;
	    	}
	    	
	    	returnLayout.setVisibility(View.GONE);//added by ycf on 20150813
	    	
			AlertDialog dialog = new AlertDialog.Builder(mContext)
					.setTitle(getResources().getString(R.string.confirm))
					.setMessage(
							getResources().getString(R.string.openTrack))
					.setPositiveButton(
							getResources().getString(R.string.result_btn),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									lastcmd = Command.CMD_TRUNK;
									StatusUtils.setDelay( Command.TIME_OUT );
									Utils.showProcess(mContext);
									MediaUtil.onVibrate();

									List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
									params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
									params.add(new BasicNameValuePair("command", "open_trunk" ));  
									params.add(new BasicNameValuePair("scooter_id", mSp.getTid() )); 
									
									returnLayout.setVisibility(View.GONE);
									mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, mContext, mEsat, Command.MODE_UNKNOWN);
								}
							})
					.setNegativeButton(
							getResources()
									.getString(R.string.result_cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
			dialog.show();
		}
	};
	//added by ycf on 20150725 end
	
	@Override
	public void onTimer(int t) {
		// TODO Auto-generated method stub
		
	}

	private void UpdaeState(JSONObject dev) {
		int temp = 0;
		int volt = 120;
	}

	@Override
	public void onStatus(JSONArray devs) {
		// TODO Auto-generated method stub
		Log.i("state", "StatusUtils");
		upateUi();
//		if( StatusUtils.isCheckBind() ){
//	    	if( mSp.getTid().isEmpty() && rent.getVisibility() == View.VISIBLE ){
//	    		rent.setVisibility( View.GONE);
//	    		unrent.setVisibility( View.VISIBLE );
//	    		
//	    		btnUnlock.setEnabled( false );
//	    		btnEsat.setEnabled( false );
//	    		btnReturn.setEnabled( false );
//	    	} else {
//	    		setTimer();
//	    		mNumber.setText( mSp.getTName() );
//	    		rent.setVisibility( View.VISIBLE);
//	    		unrent.setVisibility( View.GONE );
//
//	    		btnUnlock.setEnabled( true );
//	    		btnEsat.setEnabled( true );
//	    		btnReturn.setEnabled( true );
//	    	}
////		}
//		if( null != devs && mSp != null ){
//			JSONObject dev;
//			try {
//				for( int i = 0; i < devs.length(); i ++ ){
//					dev = devs.getJSONObject( i );
//					if( dev.has( "tid" ) ){
//						if( dev.getString( "tid" ).compareTo( mSp.getTid() ) == 0 ){
//							UpdaeState(dev);
//						}
//					}
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public void updateUi(){
    	if( mSp.getTid().isEmpty() ){
    		rent.setVisibility( View.GONE);
    		unrent.setVisibility( View.VISIBLE );
    		mTimer.setVisibility(View.GONE);
    		
    		btnUnlock.setEnabled( false );
    		btnEsat.setEnabled( false );
    		btnReturn.setEnabled( false );
    	} else {
    		setTimer();
    		mNumber.setText( mSp.getTName() );
    		rent.setVisibility( View.VISIBLE);
    		unrent.setVisibility( View.GONE );
    		mTimer.setVisibility(View.VISIBLE);

    		btnUnlock.setEnabled( true );
    		btnEsat.setEnabled( true );
    		btnReturn.setEnabled( true );
    	}
	}
	
	public void upateUi(){
		if( btnUnlock != null ){
			updateUi();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if( mSp == null ){
			mSp = SpUtil.getInstance();
		}
		if( hTimer == null ){
			hTimer = new Handler();
		}
		upateUi();

		StatusUtils.setDelay2( mContext, this, StatusUtils.MAX_DELAY);
	}

	@Override
	public void onPause() {
		super.onPause();
		hTimer = null;
		StatusUtils.clearDelay2();
	}

	Handler mUpdateTimer = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( mSp.getTime() > 0 ){
				long cur = ( System.currentTimeMillis() - mSp.getTime() ) / 1000;
				long hour = cur / 60 / 60;
				long min = ( cur % (60 * 60) ) / 60;
				long sec = cur % 60;
				mTimer.setText( String.format( "%d hours %d minutes %d seconds", hour, min, sec ) );
			} else {
				mTimer.setText( "" );
			}
			mTimer.invalidate();
		}
	};

	public Handler hTimer = new Handler( );
	public Runnable mRunable = new Runnable( ) {
		public void run ( ) {
			setTimer();
		}
	};

	public void setTimer(){
		if( mTimer != null ){
			mUpdateTimer.sendEmptyMessage(0);
		}
		if( hTimer != null ){
			hTimer.postDelayed(mRunable,1000);
		}
	}
}
