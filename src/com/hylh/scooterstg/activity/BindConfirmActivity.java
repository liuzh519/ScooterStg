package com.hylh.scooterstg.activity;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.R.color;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;

public class BindConfirmActivity extends FragmentActivity {
	private MyApplication mApp;
	private Context mContext;
	
	private TitleBarView mTitleBarView;
	protected CheckBox check1;
	protected CheckBox check2;
	protected CheckBox check3;
	protected CheckBox check4;
	protected CheckBox check5;//modify by ycf on 20150808
	protected CheckBox check6;
	protected CheckBox check7;
    protected RelativeLayout rent;
    protected TextView text;
    protected RelativeLayout lconfirm;
    protected RelativeLayout linfo;
    
    //added by ycf on 20150724
    protected RelativeLayout rentSuc;
    protected RelativeLayout rentFail;
    protected Button greatBtn;
    protected Button failBtn;
    protected TextView rfTitleTv;
    protected TextView rfMsgTv;
    //added by ycf on 20150724
    
    //added by ycf on 20150806 begin
    protected RelativeLayout alphaRl;
    protected RelativeLayout paymentFailRl;
    protected Button okBtn;
    //added by ycf on 20150806 end
    
    
    protected Button mOk;
    protected Button mCancel;
    protected Button mVideo;
    protected Button mRent;
    protected Button mThank;

    
	protected SpUtil mSp;
	protected String tid;
	protected String num;
	protected boolean mShow = true;

	Handler mBind = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				SpUtil.getInstance().setTid(tid);
				SpUtil.getInstance().setTName(num);
				
				long currentTime = System.currentTimeMillis();//add by ycf on 20150629
				SpUtil.getInstance().setTime(currentTime);
				Log.i("bind", tid + " " + num );
				Utils.mShowCtrl = true;
				
				//3. 用戶租車每兩小時跳出提示 (Local notification)
				//added by ycf on 20150716 begin
//				showDialog(getResources().getString( R.string.result_title_suc), getResources().getString( R.string.result_success), R.drawable.icon_error);
				Utils.noticeTimer.schedule(new TimerTask(){
					@Override
					public void run() {
						 new Handler(){
							    public void handleMessage(Message msg) {
							    	showDialog(getResources().getString( R.string.result_title_suc), getResources().getString( R.string.result_success), R.drawable.icon_error);
							    	super.handleMessage(msg);
							    }
							}.sendMessage(new Message());
						
					}}, 0, Utils.NOTIFY_INTERVAL_TIME);
				//added by ycf on 20150716 end
				
				//启动定时通知
//				PollingUtils.startPollingService(mContext, currentTime, 2 * 60 * 60 , PollingService.class);//added by ycf on 20150629
			
				
				rentSuc.setVisibility(View.VISIBLE);//added by ycf on 20150724
				
			}else if( msg.arg1 == -1 ){
				showDialog("Error", (String)msg.obj, R.drawable.icon_error);
			}else if( msg.arg1 == -2 ){
				showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error);
			}else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				
				//added by ycf on 20150724 begin
				if("1102".equals(code)){
					rfTitleTv.setText(getResources().getString(R.string.title_already_rented));
					rfMsgTv.setText(getResources().getString(R.string.title_choose_another));
					rentFail.setVisibility(View.VISIBLE);
				}else if( code.compareTo("1002") == 0 ){
					rfTitleTv.setText(getResources().getString(R.string.title_could_not_rented));
					rfMsgTv.setText(getResources().getString(R.string.title_try_again));
					rentFail.setVisibility(View.VISIBLE);
					
					Utils.login(mContext);
				} else if("1201".equals(code)){//added by ycf on 20150806 begin
					alphaRl.setVisibility(View.VISIBLE);
					paymentFailRl.setVisibility(View.VISIBLE);//added by ycf on 20150806 end
				}else {
//					showDialog("Error", ErrUtils.getRental( (String)msg.obj ), R.drawable.icon_error);
					rfTitleTv.setText(getResources().getString(R.string.title_rental_failed));
					rfMsgTv.setText(getResources().getString(R.string.title_try_again));
					rentFail.setVisibility(View.VISIBLE);
				}//added by ycf on 20150724 end
			}else{
				showDialog(getResources().getString( R.string.error), getResources().getString( R.string.exception_unknown), R.drawable.icon_error);
			}

			Utils.dismissProcess();
		}
	};
	
	
	public void showDialog(String title, String txt, int icon){
		AlertDialog aldlg = new AlertDialog.Builder(BindConfirmActivity.this)
		.setTitle(title)
		.setMessage(txt)
		.setIcon(icon)
		.setPositiveButton(
				getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						finish();
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_confirm);
		mApp=MyApplication.getInstance();
		mContext = this;
		mSp = SpUtil.getInstance();
		findView();
		initView();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		check1=(CheckBox) findViewById(R.id.checkBox1);
		check2=(CheckBox) findViewById(R.id.checkBox2);
		check3=(CheckBox) findViewById(R.id.checkBox3);
		check4=(CheckBox) findViewById(R.id.checkBox4);
		check5=(CheckBox) findViewById(R.id.checkBox5);//modify ycf on 20150808
		check6=(CheckBox) findViewById(R.id.checkBox6);
		check7=(CheckBox) findViewById(R.id.checkBox7);
		text=(TextView) findViewById(R.id.btn_txt);
		rent = (RelativeLayout) findViewById(R.id.btn_scooter);
		
		lconfirm = (RelativeLayout) findViewById(R.id.confirm_layout);
		mOk = (Button) findViewById(R.id.ok);
		mCancel = (Button) findViewById(R.id.cancel);
		
		linfo = (RelativeLayout) findViewById(R.id.info_layout);
		mVideo = (Button) findViewById(R.id.i_video);
		mRent = (Button) findViewById(R.id.i_rent);
		mThank = (Button) findViewById(R.id.i_cancel);
		
		//added by ycf on 20150724 begin
		rentSuc = (RelativeLayout) findViewById(R.id.rent_suc_layout);
		greatBtn = (Button) findViewById(R.id.great);
		
		rentFail = (RelativeLayout) findViewById(R.id.rent_fail_rlayout);
		failBtn = (Button) findViewById(R.id.btn_fail); 
		
		rfTitleTv = (TextView) findViewById(R.id.tx_rent_fail_title);
		rfMsgTv = (TextView) findViewById(R.id.tx_rent_fail_msg);
		//added by ycf on 20150724 end
		
		//added by ycf on 20150806 begin
		this.alphaRl = (RelativeLayout) findViewById(R.id.alpha_layout);
		this.paymentFailRl = (RelativeLayout) findViewById(R.id.payment_fail_layout);
		this.okBtn = (Button) findViewById(R.id.btn_payment_fail_ok);  
		//added by ycf on 20150806 end
		
		
		if( mSp.getCheck() > 0 ){
			check1.setChecked(true);
			check2.setChecked(true);
			check3.setChecked(true);
			check4.setChecked(true);
			check5.setChecked(true);
			check6.setChecked(true);
			//5. 用戶同意條款，每次重新登入時都必須讓用戶看到，並且在用戶同意時發送API request
//			check7.setChecked(true);  //modify by ycf on 20150630
		}
	}

	
	private void initView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_rent_scooter);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		lconfirm.setVisibility(View.GONE);
		linfo.setVisibility(View.GONE);
		rentSuc.setVisibility(View.GONE);//added by ycf on 20150724
		
		
	    mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lconfirm.setVisibility(View.GONE);
				Intent it = new Intent(BindConfirmActivity.this, WebkitActivity.class);  
				it.putExtra("url", Utils.urlVideo);
				it.putExtra("mode", "full");
				startActivity(it);
				check6.setChecked(true);
			}
		});
	    
	    mCancel.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				lconfirm.setVisibility(View.GONE);
			}
		});
	    mVideo.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				linfo.setVisibility(View.GONE);
				lconfirm.setVisibility(View.VISIBLE);
			}
		});
	    mRent.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				linfo.setVisibility(View.GONE);
				if( SpUtil.getInstance().getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){//登录状态
					
					if( checkStatus() == 0 || checkStatus() == 7){//modify by ycf on 20150723 
				        	Utils.showProcess(mContext);
							List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
							params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
							params.add(new BasicNameValuePair("command", "rent" ));  
							params.add(new BasicNameValuePair("scooter_id", tid )); 

							mSp.setCheck(1);
							
							mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, BindConfirmActivity.this, mBind, Command.MODE_SILENT);
					}
				} else {//未登录状态
					Intent intent = new Intent (mContext,LoginActivity.class);
					startActivity(intent);
				}
			}
		});
	    mThank.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				linfo.setVisibility(View.GONE);
			}
		});
	    
	    //added by ycf on 20150724 begin
	    greatBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rentSuc.setVisibility(View.GONE);
			}
		});
	    failBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rentFail.setVisibility(View.GONE);
			}
		});
	    //added by ycf on 20150724 end
	    
	    //added by ycf on 20150806 begin
	    okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alphaRl.setVisibility(View.GONE);
				paymentFailRl.setVisibility(View.GONE);
			}
		});
	  //added by ycf on 20150806 end
	    
		rent.setOnClickListener(RentClick);
		
		text.setTextColor( getResources().getColor(color.gray) );
	}
	
	OnClickListener RentClick = new Button.OnClickListener() {
		public void onClick(View v) {

			if( SpUtil.getInstance().getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){
				
				//added by ycf on 20150723 begin
				if(checkStatus() == 6){		
					linfo.setVisibility( View.VISIBLE );
				}
				//added by ycf on 20150723 end
				
				if( checkStatus() == 0 ){
					if( !check6.isChecked() ){
						linfo.setVisibility(View.VISIBLE);
					} else {
			        	Utils.showProcess(mContext);
						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
						params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
						params.add(new BasicNameValuePair("command", "rent" ));  
						params.add(new BasicNameValuePair("scooter_id", tid )); 
						
						mApp.getCmd().sendHttpsGet( Utils.urlScooter, params, BindConfirmActivity.this, mBind, Command.MODE_SILENT);
					}
				}
			} else {
				Intent intent = new Intent (mContext,LoginActivity.class);
				startActivity(intent);
			}
		}
	};

	private void init(){
		Intent intent = this.getIntent();
		tid = intent.getStringExtra( "tid" );
		num = intent.getStringExtra( "num" );
		mSp.setLegal(true);
		//added by ycf on 20150811 begin
		String action = intent.getStringExtra("action");
		if("accept".equals(action)){
			check7.setChecked(true);
		}else if("cancle".equals(action)){
			check7.setChecked(false);
		}//added by ycf on 20150811 end
		
		this.checkStatus();
		
		check1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checkStatus();
			}
        });
		check7.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				//modify by ycf on 20150811 begin
//				if( arg1 ){
//					if( mSp.showLegal() ){
//						Intent it = new Intent(mContext, WebkitActivity.class);  
//						it.putExtra("url", Utils.urlLegal);
//						it.putExtra("mode", "legal");
//						it.putExtra("title", R.string.title_legal );
//						
//						startActivity(it);
//					}
//				}
				//modify by ycf on 20150811 end
				checkStatus();
			}
        });
	}
	
	protected int checkStatus(){
		if( !check1.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 1;
		}
		if( !check2.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 2;
		}
		if( !check3.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 3;
		}
		if( !check4.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 4;
		}
		if( !check5.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 5;
		}
		if( !check6.isChecked() ){
//			text.setTextColor( getResources().getColor(color.gray) );
//			return 6;
		}
		if( !check7.isChecked() ){
			text.setTextColor( getResources().getColor(color.gray) );
			return 7;
		}

		text.setTextColor( getResources().getColor(color.blue) );
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if( lconfirm.getVisibility() == View.VISIBLE ){
				lconfirm.setVisibility( View.GONE );
				return true;
			}
			if( linfo.getVisibility() == View.VISIBLE ){
				linfo.setVisibility( View.GONE );
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onResume() {
		super.onResume();
		//modify by ycf on 20150811 begin
//		if( mSp != null ){
////			if( mSp.getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){
//				if( mSp.showLegal() ){
//					check7.setChecked(false);
//		    	} else {
//		    		check7.setChecked(true);
//		    	}
////			} else {
////				
////			}
//		}
		//modify by ycf on 20150811 end
	}

	@Override
	public void onPause() {
		super.onPause();
		
	}
}
