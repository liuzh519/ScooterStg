package com.hylh.scooterstg.activity;


import java.util.Calendar;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.fragments.CtrlTs001Fragment;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScooterCtrlAcitivity extends FragmentActivity {
	//private MyDialog dialog;
	private MyApplication mApp;


	public ImageView mCtl;
	public ImageView mSet;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		mApp = MyApplication.getInstance();
		
		findView();
		init();
	}

	private void findView() {
		mCtl = (ImageView) findViewById(R.id.img_ctrl);
		mSet = (ImageView) findViewById(R.id.img_set);
	}

	private void init() {
		mCtl.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				initCtrl();
			}
		});
		mSet.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				initSet();
			}
		});
		
		initCtrl();
	}
	
	public void initCtrl(){
		mCtl.setBackgroundResource( R.drawable.ctl_tab_control_sel );
		mSet.setBackgroundResource( R.drawable.ctl_tab_set_nor );
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		Fragment fragment = new CtrlTs001Fragment();
		ft.replace(R.id.fl_content_active, fragment, "ACT_Ctrl");
		ft.commit();
	}
	
	public void initSet(){
//		mCtl.setBackgroundResource( R.drawable.ctl_tab_control_nor );
//		mSet.setBackgroundResource( R.drawable.ctl_tab_set_sel );
//		FragmentManager fm=getSupportFragmentManager();
//		FragmentTransaction ft=fm.beginTransaction();
//		Fragment fragment = new CtrlTs001SetFragment();
//		ft.replace(R.id.fl_content_active, fragment, "ACT_Ctrl");
//		ft.commit();
	}
	
	public void setDate( TextView ctl, long dt, int sec ){
		 Calendar can = Calendar.getInstance(); 
		 can.setTimeInMillis(dt);
		 int year = can.get(Calendar.YEAR);
		 int month = can.get(Calendar.MONTH);
		 int day = can.get(Calendar.DAY_OF_MONTH);
		 int hour = can.get(Calendar.HOUR_OF_DAY);
		 int min = can.get(Calendar.MINUTE);
		 
		 ctl.setText( String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month + 1, day, hour, min, sec ) );
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


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent (ScooterCtrlAcitivity.this, MainActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
