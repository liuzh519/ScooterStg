package com.hylh.scooterstg.fragments;


import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrackConfirmTab {
	private MyApplication mApp;
	private Context mContext;
	
	public final static int GET_REQCODE = 10002;
	public final static int GET_OK = 0;
	public final static int GET_DATE = 1;
	public final static int GET_TIME = 2;

	private Intent intent;
	public TextView mTitle;
	private String mTid;
	private String mLpn;

	protected TrackConfirmTab(View view) {
		mApp = MyApplication.getInstance();
		
		findView(view);
	}

	private void findView(View view) {
		mTitle = (TextView) view.findViewById(R.id.l_title);
	}

	private void init() {
//		intent = getIntent();
//		mTid = intent.getStringExtra("tid");
//		mLpn = intent.getStringExtra("lpn");

		mTitle.setText(mLpn);
	}

}
