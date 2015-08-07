package com.hylh.scooterstg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;


public class UserSettingAcitivity extends FragmentActivity {
	public static final String TAG = "ReportAcitivity";
	private MyApplication mApp;
	private Context mContext;
	private TitleBarView mTitleBarView;

	private RelativeLayout mPaswd;
	private RelativeLayout mInfo;
	private RelativeLayout mDevice;
	private RelativeLayout mLotout;
	private RelativeLayout mChange;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_user_setting);

		mApp = MyApplication.getInstance();
		mContext=this;

		findView();
		initTitleView();
		init();
	}

	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);

		mPaswd=(RelativeLayout) findViewById(R.id.btn_user_passwd);
		mInfo=(RelativeLayout) findViewById(R.id.btn_usr_info);
		mDevice=(RelativeLayout) findViewById(R.id.btn_user_device);
		mLotout=(RelativeLayout) findViewById(R.id.btn_user_logout);
		mChange=(RelativeLayout) findViewById(R.id.btn_user_change);
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
		mPaswd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,UserPasswdAcitivity.class);
				startActivity(intent);
			}
		});
		mInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,UserInfoAcitivity.class);
				startActivity(intent);
			}
		});
		mDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,RegisterDeviceActivity.class);
				startActivity(intent);
			}
		});
		mLotout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mApp.setLogin( 0 );
				Intent intent = new Intent (mContext,LoginActivity.class);
				startActivity(intent);
			}
		});
		mChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,LoginActivity.class);
				startActivity(intent);
			}
		});
	}

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
