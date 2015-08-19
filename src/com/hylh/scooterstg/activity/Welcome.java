package com.hylh.scooterstg.activity;

import java.util.ArrayList;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.OrientationActivity.MyOnPageChangeListener;
import com.hylh.scooterstg.utils.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


public class Welcome extends Activity {
	private Context	 mContext;
	private ViewPager mViewPager;	
	private ImageView mPage;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
		
	private int currIndex = 0;
	

	public Handler hTimer = new Handler();
	public Runnable mRunable = new Runnable( ) {
		public void run ( ) {
			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation_viewpager);
		mContext = this;

		SpUtil sp = SpUtil.getInstance();
		int lg = sp.getInt( SpUtil.LOGIN_KEEP, 0 );
		if( lg > 0 )
		{
			hTimer.postDelayed(mRunable,1000);
		}
		else
		{
			if( sp.isFirst() ){
				sp.setFirst(false);
				initTrain();
			} else {
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
    }    
    
    
    void initTrain(){

        mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);        
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
       

        mPage = (ImageView)findViewById(R.id.page);
        mPage0 = (ImageView)findViewById(R.id.page0);
        mPage1 = (ImageView)findViewById(R.id.page1);
        mPage2 = (ImageView)findViewById(R.id.page2);
        mPage3 = (ImageView)findViewById(R.id.page3);
        
      //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view = mLi.inflate(R.layout.orientation0, null);
        View view1 = mLi.inflate(R.layout.orientation1, null);
        View view2 = mLi.inflate(R.layout.orientation2, null);
        View view3 = mLi.inflate(R.layout.orientation3, null);
        View view4 = mLi.inflate(R.layout.orientation4, null);
        
      //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
        view4.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				
				Uri uri = Uri.parse("http://youtu.be/yQW3QcjBrJU");  
				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(it);
				finish();
			}
		});
        
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:				
				mPage.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:				
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 2:
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 3:
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 4:
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			}
			currIndex = arg0;
			//animation.setFillAfter(true);// True:图片停在动画结束位置
			//animation.setDuration(300);
			//mPageImg.startAnimation(animation);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mContext = this;
//
//		sp=getSharedPreferences(SpUtil.NAME, Context.MODE_PRIVATE);
//		int lg = sp.getInt( SpUtil.LOGIN_KEEP, 0 );
//		if( lg > 0 )
//		{
//			Intent intent = new Intent(mContext, MainActivity.class);
//			startActivity(intent);
//			finish();
//			return;
//		}
//		
//		setContentView(R.layout.activity_welcome);
//		findView();
//		init();
//	}
//
//	private void findView() {
//		mImageView = (ImageView) findViewById(R.id.iv_welcome);
//	}
//
//	@SuppressWarnings("static-access")
//	private void init() {
//		mImageView.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Intent intent = new Intent(mContext, MainActivity.class);
//				startActivity(intent);
//				finish();
////				sp = SpUtil.getInstance().getSharePerference(mContext);
////				boolean isFirst = SpUtil.getInstance().isFirst(sp);
////				if (!isFirst) {
////					SpUtil.getInstance().setBooleanSharedPerference(sp,
////							"isFirst", true);
////					Intent intent = new Intent(mContext, LoginActivity.class);
////					startActivity(intent);
////					finish();
////				} else {
////					int lg = sp.getInt( SpUtil.LOGIN_KEEP, 0 );
////					if( lg > 0 )
////					{
////						Intent intent = new Intent(mContext, MainActivity.class);
////						startActivity(intent);
////					}
////					else
////					{
////						Intent intent = new Intent(mContext, LoginActivity.class);
////						startActivity(intent);
////					}
////					finish();
////				}
//			}
//		},2000);
//		
//	}
}