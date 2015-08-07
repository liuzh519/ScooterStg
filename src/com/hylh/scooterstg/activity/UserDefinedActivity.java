package com.hylh.scooterstg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;



/**
 * @author ycf
 *
 * @date  2015年7月25日
 * 
 * @Description TODO
 *
 * @version　1.0
 *
 */

public class UserDefinedActivity extends Activity {
	
	protected Button mThank;
    protected RelativeLayout linfo;
	
	 @Override  
	 public void onCreate(Bundle savedInstanceState)  {  
	      super.onCreate(savedInstanceState);  
	      setContentView(R.layout.activity_user_defined);
	      
	      findView();
			initView();
	 }  
	 
	 
	 private void findView(){

			mThank = (Button) findViewById(R.id.i_cancel);
			linfo = (RelativeLayout) findViewById(R.id.info_layout);
	
		}
	 
	 private void initView(){
		

		 mThank.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					linfo.setVisibility(View.GONE);
				}
			});
	 }

}
