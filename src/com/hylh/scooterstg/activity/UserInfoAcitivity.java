package com.hylh.scooterstg.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.LoginUtils;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;


public class UserInfoAcitivity extends FragmentActivity {
	public static final String TAG = "ReportAcitivity";
	private MyApplication mApp;
	private Context mContext;
	private TitleBarView mTitleBarView;

	private Button set;
	private EditText old;
	private EditText pwd;
	private EditText pwd2;
	
	private String strOld;
	private String strPwd;
	private String strPwd2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_user_passwd);

		mApp = MyApplication.getInstance();
		mContext=this;

		findView();
		initTitleView();
		init();
	}

	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);

		set = (Button) findViewById(R.id.set);
		old = (EditText) findViewById(R.id.old_pwd);
		pwd = (EditText) findViewById(R.id.new_pwd);
		pwd2 = (EditText) findViewById(R.id.cof_pwd);
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.user_settings);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void init(){

		set.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				strOld = old.getText().toString();
				strPwd = pwd.getText().toString();
				strPwd2 = pwd2.getText().toString();

				if( strOld.length() < 4 )
				{
					Utils.showDialog( R.string.error, R.string.exception_usr_pwd_emtpy, UserInfoAcitivity.this, null);
					return;
				}
				if( strPwd.length() < 4 )
				{
					Utils.showDialog( R.string.error, R.string.exception_usr_pwd_emtpy, UserInfoAcitivity.this, null);
					return;
				}
				if( strPwd.compareTo( strPwd2 ) != 0 )
				{
					Utils.showDialog( R.string.error, R.string.title_pattern_notfit, UserInfoAcitivity.this, null);
					return;
				}
				strOld = LoginUtils.enPassword( strOld );
				strPwd = LoginUtils.enPassword( strPwd );
				
				if( strPwd.length() == 0 )
				{
					Utils.showDialog( R.string.error, R.string.exception_usr_pwd_emtpy, UserInfoAcitivity.this, null);
					return;
				}
				
//				JSONObject json = new JSONObject();
//				json.put( "old_pwd", strOld );
//				json.put( "paswd", strPwd );
//				
//
//				Utils.showProcess( mContext );
//				
//				mApp.getCmd().sendHttp( "100012", mApp.sequence(), json, mContext, handler, Command.MODE_UNKNOWN );

			}
				
		});
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
				if( msg.arg1 == 0 ){
					Utils.showDialog( mContext );
					finish();
				} else if( msg.arg1 == 1 ){
//					Utils.showDialog( "99999999", Integer.parseInt( msg.obj.toString() ), UserPassword.this );
				}
				else{
					if( msg.obj == null ){
//						Utils.showDialog( "99999999", R.string.exception_unknown, UserPassword.this );
					}else{
//						Utils.showDialog( "99999999", msg.obj.toString(), UserPassword.this );
					}
				}
				Utils.dismissProcess();
			
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_MENU){
		}
		return super.onKeyDown(keyCode, event);
		
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (!Utils.isAppOnForeground(getApplicationContext())) {
        	mApp.setLogin(0);
        }
	}
}
