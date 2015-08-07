package com.hylh.scooterstg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.SwitchButton;
import com.hylh.scooterstg.view.TitleBarView;


public class SysSettingAcitivity extends FragmentActivity {
	public static final String TAG = "ReportAcitivity";
	private MyApplication mApp;
	private Context mContext;
	private TitleBarView mTitleBarView;
	private SpUtil mSp;

	private RelativeLayout mAbout;
	private RelativeLayout mExit;
	
	private SwitchButton mVirbate;
	private SwitchButton mSound;
	private SwitchButton mAlarm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_sys_setting);

		mApp = MyApplication.getInstance();
		mSp = SpUtil.getInstance();
		mContext=this;

		findView();
		initTitleView();
		init();
	}

	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);

		mAbout=(RelativeLayout) findViewById(R.id.btn_app_about);
		mExit=(RelativeLayout) findViewById(R.id.btn_app_exit);

		mVirbate=(SwitchButton) findViewById(R.id.sw_vibrate);
		mSound=(SwitchButton) findViewById(R.id.sw_sound);
		mAlarm=(SwitchButton) findViewById(R.id.sw_alarm);
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.app_settings);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void init(){

		mVirbate.setChecked(mSp.getVibrate());
		mSound.setChecked(mSp.getMedia());
		mAlarm.setChecked(mSp.getWarnConfirm());
		
		mVirbate.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
		    @Override  
		    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	        	mSp.setVibrate(isChecked);
		    }  
		});
		mSound.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
		    @Override  
		    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
		    	mSp.setMedia(isChecked);
		    }  
		});
		mAlarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
		    @Override  
		    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
		    	mSp.setWarnConfirm(isChecked);
		    }  
		});
		mAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,AboutActivity.class);
				startActivity(intent);
			}
		});
		
		mExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyApplication.getInstance().setLogin( 0 );
				finish();
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
