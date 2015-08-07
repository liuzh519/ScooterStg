package com.hylh.scooterstg.activity;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.ToastUtil;
import com.hylh.scooterstg.utils.Utils;

public class LoginActivity extends Activity {

	private MyApplication mApp;
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
	private Button mTextViewURL;

	private EditText una;
	private EditText pwd;
	private TextView txtVer;
	private CheckBox save;
	private TextView mAlarm;

	private String u = "";
	private String p = "";
	private String n = "";
	
	private SpUtil sp;
	
    //added by ycf on 20150725 begin
	private RelativeLayout lfLayout; 
	private RelativeLayout alphaLayout;
	private Button lfOkBtn;
	//added by ycf on 20150725 end

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext=this;
		mApp = (MyApplication)this.getApplication();
		findView();
		initTvUrl();
		init();
	}
	
	private void findView(){
		rl_user=(RelativeLayout) findViewById(R.id.rl_user);
		mLogin=(Button) findViewById(R.id.login);
		register=(Button) findViewById(R.id.register);
		mTextViewURL=(Button) findViewById(R.id.tv_forget_password);

		una = (EditText) findViewById(R.id.user);
		pwd = (EditText) findViewById(R.id.password);
		txtVer = (TextView) findViewById(R.id.ver);
		save = (CheckBox) findViewById(R.id.checkBox1);
		mAlarm = (TextView) findViewById(R.id.user_alarm);
		
		//added by ycf on 20150725 begin
		lfLayout = (RelativeLayout)findViewById(R.id.login_fail_layout);
		alphaLayout = (RelativeLayout)findViewById(R.id.alpha_layout);
		lfOkBtn = (Button)findViewById(R.id.btn_login_fail_ok);
		//added by ycf on 20150725 end
		
	}

	private void init(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		mAlarm.setVisibility(View.GONE);

		try {
			txtVer.setText( "V" + getPackageManager().getPackageInfo("com.hylh.scooterstg", 0).versionName );
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		
		mLogin.setOnClickListener(loginOnClickListener); 
		register.setOnClickListener(registerOnClickListener);

		sp = SpUtil.getInstance();
		u = sp.getLoginName();
		if( !u.isEmpty() ){
			una.setText(u);
			save.setChecked( true );
		}else {
			una.setText( "" );
			save.setChecked( false );
		}
		mTextViewURL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Uri uri = Uri.parse("http://www.scootawayscooters.com/register.html");  
//				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//				startActivity(it);

				Intent it = new Intent(mContext, WebkitActivity.class);  
				//6. Forgot password連結path改成 “forgot_password.html”
				it.putExtra("url", Utils.urlForgetPwd );//modify by ycf on 20150626
				it.putExtra("mode", "");
				startActivity(it);
			}
		});

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Uri uri = Uri.parse("http://www.scootawayscooters.com/register.html");  
//				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//				startActivity(it);

				Intent it = new Intent(mContext, WebkitActivity.class);  
				it.putExtra("url", Utils.urlReg );
				it.putExtra("mode", "finish");
				startActivity(it);
			}
		});
		
		//added by ycf on 20150725 begin
		lfOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lfLayout.setVisibility(View.GONE);
				alphaLayout.setVisibility(View.GONE);
			}
		});
		//added by ycf on 20150725 end
	}
	
	private void initTvUrl(){
		mTextViewURL.setText(R.string.forget_password);
	}
	
	private OnClickListener loginOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			p = pwd.getText().toString();
			u = una.getText().toString();
			
//			u = "suzhou168168@163.com";
//			p = "password";
			mAlarm.setVisibility(View.GONE);

			if( u.length() < 4 ) {
				ToastUtil.makeText(LoginActivity.this, R.string.exception_usr_name_emtpy, Toast.LENGTH_SHORT );
				return;
			}
			if( p.length() < 4 ) {
				ToastUtil.makeText(LoginActivity.this, R.string.exception_usr_pwd_emtpy, Toast.LENGTH_SHORT );
				return;
			}
//			p = LoginUtils.enPassword( p );
			
			if( p.length() == 0 ) {
				Utils.showDialog( R.string.excepton_error, R.string.exception_usr_pwd_emtpy, LoginActivity.this, null);
				return;
			}

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			if( u.indexOf('@') < 0 )
			{
				long lp = 0;
				try{
					lp = Long.parseLong(u);
					if( lp > 10000000000L ){
						if(u.getBytes()[0] != '1') {
							mAlarm.setVisibility(View.VISIBLE);
							return;
						} else {
							params.add(new BasicNameValuePair("phone", u));
						}
					} else {
						ToastUtil.makeText(LoginActivity.this, R.string.exception_usr_name_invalid, Toast.LENGTH_SHORT );
						return;
					}
				} catch( NumberFormatException e){
					ToastUtil.makeText(LoginActivity.this, R.string.exception_usr_name_invalid, Toast.LENGTH_SHORT );
					return;
				}
				if( lp <= 0 ){
					return;
				}
			}
			else
			{
				params.add(new BasicNameValuePair("email", u));
			}
			params.add(new BasicNameValuePair("password", p));
			params.add(new BasicNameValuePair("key", "dda405b2-f302-4e60-9e7b-2e90378d" ));//sp.getSTKey())); 
			Log.i("login", params.toString() );

			Utils.showProcess( LoginActivity.this );
			mApp.getCmd().sendHttpsPost( Utils.urlLogin
										, params
										, LoginActivity.this
										, hlogin
										, Command.MODE_UNKNOWN );
		}
	};
	
	private Handler hlogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utils.dismissProcess();
			if( msg.arg1 == 0 ){
				JSONObject rsp = (JSONObject) msg.obj;
				if( rsp.has( "k" ) ){
					try {

						sp.setInt( SpUtil.LOGIN_KEEP, 1 );
						sp.setSTKey(rsp.getString("k"));
						sp.setTime(0);
						mApp.setLogin( 1 );
						sp.setLegal(true);
						StatusUtils.setCheckBind(true);

//						Intent intent=new Intent(mContext, MainActivity.class);
//						startActivity(intent);
						
						
						ToastUtil.makeText(LoginActivity.this, R.string.toast_login_success, Toast.LENGTH_SHORT);//added by ycf on 20150725
						
						
						finish();
						
					} catch (JSONException e) {
						e.printStackTrace();
						Utils.showDialog(R.string.excepton_error, R.string.exception_response, R.drawable.icon_error, LoginActivity.this, null);
					}
				} else {
					Utils.showDialog(R.string.excepton_error, R.string.exception_response, R.drawable.icon_error, LoginActivity.this, null);
				}
			} else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, LoginActivity.this, null);
			} else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, LoginActivity.this, null);
			} else if( msg.arg1 == -10 ){
				//modify by ycf on 20150725 begin
				//Utils.showDialog("Error", ErrUtils.getLogin( (String)msg.obj ), R.drawable.icon_error, LoginActivity.this, null);
				alphaLayout.setVisibility(View.VISIBLE);
				lfLayout.setVisibility(View.VISIBLE);
				//modify by ycf on 20150725 end
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, LoginActivity.this, null);
			}
		}
	};
	
	private OnClickListener registerOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
//			Intent intent=new Intent(mContext, RegisterUserActivity.class);
//			startActivity(intent);
			
		}
	};
}
