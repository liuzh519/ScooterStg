package com.hylh.scooterstg.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.R.color;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.fragments.CtrlTs001Fragment;
import com.hylh.scooterstg.fragments.LocationGMapFragment;
import com.hylh.scooterstg.fragments.MoreFragment;
import com.hylh.scooterstg.utils.MediaUtil;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.UpdateManager;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.MyViewPager;


public class MainActivity extends FragmentActivity {

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private MainActivity mActivity;
	private LinearLayout mTab1, mTab2, mTab3;
	private ImageView mImg1, mImg2, mImg3, mImg;
	private TextView mTxt1, mTxt2, mTxt3, mTxt;
	private Fragment mFag,mFag1,mFag2,mFag3;

	private View currentButton;

	private MyViewPager mTabPager;
	final ArrayList<Fragment> views = new ArrayList<Fragment>();
	private int mLastRes = R.drawable.tab_gps_nor;
	private MyApplication mApp;
	

	Handler mUpdate = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONObject body = json.getJSONObject( "body" );
					JSONArray rows = body.getJSONArray( "rows" );
					if( rows.length() > 0 ){
						JSONObject dev = rows.getJSONObject( 0 );
						String tid = dev.getString( "tid" );
						String lpn = tid.substring( 5 );

						SpUtil sp = SpUtil.getInstance();
						sp.setTid( tid );
						sp.setTName( lpn );
						if( mTabPager.getCurrentItem() == 0 ){
							((LocationGMapFragment)views.get(0)).initTitleView();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_content);

		mApp=MyApplication.getInstance();
		mActivity = this;
		mContext = this;

		findView();
		init();

//		mApp.getCmd().sendTcp("201051", mApp.sequence(), null, mApp, mUpdate, Command.MODE_TOAST);

		UpdateManager manager = new UpdateManager(this);
		manager.checkUpdate( false );
		
//		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	}

	private void findView() {
		mTxt1 = (TextView) findViewById(R.id.txt_location);
		mTxt2 = (TextView) findViewById(R.id.txt_status);
		mTxt3 = (TextView) findViewById(R.id.txt_more);
		mImg1 = (ImageView) findViewById(R.id.img_location);
		mImg2 = (ImageView) findViewById(R.id.img_status);
		mImg3 = (ImageView) findViewById(R.id.img_more);
		mTab1 = (LinearLayout) findViewById(R.id.lay_park);
		mTab2 = (LinearLayout) findViewById(R.id.lay_scooter);
		mTab3 = (LinearLayout) findViewById(R.id.lay_settings);

		mImg1.setImageDrawable(getResources().getDrawable(
				R.drawable.tab_gps_sel));

		mTabPager = (MyViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void init() {
		mImg = mImg1;
		MyOnClickListener ptr;
		ptr = new MyOnClickListener(0);
		mTab1.setOnClickListener(ptr);
		mImg1.setOnClickListener(ptr);
		ptr = new MyOnClickListener(1);
		mTab2.setOnClickListener(ptr);
		mImg2.setOnClickListener(ptr);
		ptr = new MyOnClickListener(2);
		mTab3.setOnClickListener(ptr);
		mImg3.setOnClickListener(ptr);

		// 
		mFag3 = new MoreFragment();
		mFag2 = new CtrlTs001Fragment();
		mFag1 = new LocationGMapFragment();
		views.add(mFag1);
		views.add(mFag2);
		views.add(mFag3);
		// 
		MyAdapter mPagerAdapter = new MyAdapter( mActivity.getSupportFragmentManager());
		mTabPager.setAdapter(mPagerAdapter);
		
		//added by ycf on 20150813 begin
		Intent intent = this.getIntent();
		String frmName = intent.getStringExtra("show");
		if(frmName != null && "mFag2".equals(frmName)){
			mFag2.onResume();
		}else{
			mFag1.onResume();
		}//added by ycf on 20150813 end

		setIndex(1);
	}
	
	public void setIndex(int i){
		mTabPager.setCurrentItem(i, true);
		if( i == 0 ){
			((LocationGMapFragment)views.get(i)).initTitleView();
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index, true);
			if( index == 0 ){
				
				((LocationGMapFragment)views.get(index)).initTitleView();
			}
//			mTabPager.setCurrentItem(index);
//			views.get(index).onResume();
		}
	};
	

	public class MyAdapter extends FragmentStatePagerAdapter {
		LocationGMapFragment gmap;
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return views.size();
		}

		// �õ�ÿ��item
		@Override
		public Fragment getItem(int position) {
			if( position == 0){
				mFag1 = new LocationGMapFragment();
				views.set( 0, mFag1 );
			}
			
			return views.get(position);
		}

		// ��ʼ��ÿ��ҳ��ѡ��
		@Override
		public Object instantiateItem(ViewGroup arg0, int arg1) {
			return super.instantiateItem(arg0, arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}

	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int idx) {
			if (mImg != null) {
				mImg.setImageDrawable(getResources().getDrawable(mLastRes));
			}
			if( mFag != null ){
				mFag.onPause();
				mTxt.setTextColor( getResources().getColor(color.gray) );
			}
			switch (idx) {
			case 0: {
				mLastRes = R.drawable.tab_gps_nor;
				mImg = mImg1;
				mImg1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_gps_sel));
				mFag1.onResume();
				mTxt1.setTextColor( getResources().getColor(color.blue) );
				
				mFag = mFag1;
				mTxt = mTxt1;
				((LocationGMapFragment)views.get(0)).initTitleView();
				break;
			}
			case 1: {
				mLastRes = R.drawable.tab_status_nor;
				mImg = mImg2;
				mImg2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_status_sel));
				mFag2.onResume();
				mTxt2.setTextColor( getResources().getColor(color.blue) );
				mFag = mFag2;
				mTxt = mTxt2;
				break;
			}
			case 2: {
				mLastRes = R.drawable.tab_set_nor;
				mImg = mImg3;
				mImg3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_set_sel));
				mFag3.onResume();
				mTxt3.setTextColor( getResources().getColor(color.blue) );
				mFag = mFag3;
				mTxt = mTxt3;
				break;
			}
			default:
				break;
			}

			MediaUtil.onAction(Command.CMD_TRUNK, mContext);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			// �൱��Fragment��onResume
//			mTabPager.setCurrentItem(0);
//			Log.i("main", "onResume");
//		} else {
//			// �൱��Fragment��onPause
//			Log.i("main", "onPause");
//		}
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
////		Log.i( "aaaa", "AAAAAAAAAA____onActivityCreated");
//	}

	@Override
	public void onStart() {
		super.onStart();
//		Log.i( "aaaa", "AAAAAAAAAA____onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		if( mTabPager != null ){
			if(Utils.mShowCtrl){
				mTabPager.setCurrentItem( 1 );
				Utils.mShowCtrl = false;
			}
//			if( SpUtil.getInstance().getTid().isEmpty() ){
//				mTabPager.setCurrentItem( 0 );
//				((LocationGMapFragment)views.get(0)).initTitleView();
//			} else {
//				mTabPager.setCurrentItem( 1 );
//			}
		}
		StatusUtils.start();
		StatusUtils.setCheckBind(true);
		
	}

	@Override
	public void onPause() {
		super.onPause();
		if( null != mFag ){
			mFag.onPause();
		}

		StatusUtils.stop();
	}

	@Override
	public void onStop() {
		super.onStop();
//		Log.i( "aaaa", "AAAAAAAAAA____onStop");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
//		Log.i( "aaaa", "AAAAAAAAAA____onDestroy");
	}

}
