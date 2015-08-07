package com.hylh.scooterstg.activity;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class AboutActivity extends Activity {
	private MyApplication mApp;
	private Context mContext;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mContext=this;
		mApp = (MyApplication)this.getApplication();
		findView();
		init();
	}
	
	private void findView(){
	}

	private void init(){
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
}
