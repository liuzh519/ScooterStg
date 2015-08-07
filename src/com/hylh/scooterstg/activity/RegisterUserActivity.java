package com.hylh.scooterstg.activity;
import org.json.JSONException;
import org.json.JSONObject;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.LoginUtils;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TextURLView;
import com.hylh.scooterstg.view.TitleBarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUserActivity extends Activity {

	private Context mContext;
	private TitleBarView mTitleBarView;
	private TextURLView mTextViewURL;
	private Button next;
	
	private EditText una;
	private EditText pwd;
	private EditText pwd2;
	private EditText phone;
	private EditText email;

	private EditText tid;
	private EditText tcode;
	private EditText tname;
	private EditText tphone;
	
	private String strName;
	private String strPwd;
	private String strPwd2;
	private String strPhone;
	private String strEmail;
	

	private String vid;
	private String vcode;
	private String vname;
	private String vphone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		mContext=this;
		findView();
		initTitleView();
		initTvUrl();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		mTextViewURL=(TextURLView) findViewById(R.id.tv_url);
		next=(Button) findViewById(R.id.register);

		una = (EditText) findViewById(R.id.usr);
		pwd = (EditText) findViewById(R.id.pwd);
		pwd2 = (EditText) findViewById(R.id.pwd2);
		phone = (EditText) findViewById(R.id.phone);
		email = (EditText) findViewById(R.id.email);


		tid = (EditText) findViewById(R.id.devid);
		tcode = (EditText) findViewById(R.id.dev_code);
		tname = (EditText) findViewById(R.id.dev_name);
		tphone = (EditText) findViewById(R.id.dev_phone);
		
	}
	
	private void init(){
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent(mContext,RegisterDeviceActivity.class);
//				startActivity(intent);
				

				strName = una.getText().toString();
				strPwd = pwd.getText().toString();
				strPwd2 = pwd2.getText().toString();
				strPhone = phone.getText().toString();
				strEmail = email.getText().toString();

				vid = tid.getText().toString();
				vcode = tcode.getText().toString();
				vname = tname.getText().toString();
				vphone = tphone.getText().toString();

				if( strName.length() < 6 ) 
				{
					Utils.showDialog( R.string.error, R.string.usr_error_name_emtpy, mContext, null );
					return;
				}
				if( strPwd.length() < 6 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_pwd_emtpy, mContext, null );
					return;
				}
				if( strPhone.length() < 6 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_phone_emtpy, mContext, null );
					return;
				}
				if( strEmail.length() < 6 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_email_invalid, mContext, null );
					return;
				}
				if( strPwd.compareTo( strPwd2 ) != 0 )
				{
					Utils.showDialog( R.string.error, R.string.title_pattern_notfit, mContext, null );
					return;
				}

				if( vid.length() != 10 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_tid_emtpy, mContext, null );
					return;
				}
				if( vcode.length() != 10 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_tcode_emtpy, mContext, null );
					return;
				}
				if( vname.length() < 4 ) 
				{
					Utils.showDialog( R.string.error, R.string.usr_error_name_emtpy, mContext, null );
					return;
				}
				if( vphone.length() < 6 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_phone_emtpy, mContext, null );
					return;
				}
				
				strPwd = LoginUtils.enPassword( strPwd );
				
				if( strPwd.length() == 0 )
				{
					Utils.showDialog( R.string.error, R.string.usr_error_pwd_emtpy, mContext, null );
					return;
				}

				MyApplication app = MyApplication.getInstance();
				JSONObject parm = new JSONObject();
				try {
					parm.put( "name", strName );
					parm.put( "dname", strName );
					parm.put( "person", "" );
					parm.put( "phone", strPhone );
					parm.put( "email", strEmail );
					parm.put( "paswd", strPwd );
					parm.put( "tid",  vid );
					parm.put( "tcode", vcode );
					parm.put( "tphone", vphone );
					parm.put( "tname", vname );

					Utils.showProcess( mContext );
					app.getCmd().sendHttps( "100011", app.sequence(), parm, mContext, handler, Command.MODE_UNKNOWN );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
			}
		});
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_register_user);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initTvUrl(){
		mTextViewURL.setText(R.string.tv_xieyi_url);
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){

				AlertDialog aldlg = new AlertDialog.Builder(mContext)
				.setTitle( mContext.getResources().getString(R.string.result_title_suc) )
				.setMessage(mContext.getResources().getString(R.string.user_register_suc))
				.setPositiveButton(
						mContext.getResources().getString(R.string.result_btn),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent();
								i.setClass(mContext, LoginActivity.class);
								startActivity(i);
								finish();
							}
						}).create();
				aldlg.setCancelable(false);
				aldlg.show();
			}
			Utils.dismissProcess();
		}
	};
}
