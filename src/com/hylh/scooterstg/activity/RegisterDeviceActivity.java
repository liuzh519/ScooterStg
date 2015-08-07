package com.hylh.scooterstg.activity;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterDeviceActivity extends Activity {
	private MyApplication mApp;
	private Context mContext;
	private Button btn_complete;
	private TitleBarView mTitleBarView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_device);
		mApp = (MyApplication)this.getApplication();
		mContext=this;
		findView();
		initTitleView();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		btn_complete=(Button) findViewById(R.id.register);
	}
	
	private void init(){
		btn_complete.setOnClickListener(completeOnClickListener);
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.title_register_device);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	private OnClickListener completeOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//		   Intent intent=new Intent(mContext, RegisterResultActivity.class);
//		   startActivity(intent);
		}
	};

	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
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
