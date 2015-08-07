package com.hylh.scooterstg.fragments;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrackFragment extends Fragment {

	protected static final String TAG = "TrackFragment";
	private MainActivity mContext;
	private View mBaseView;
	private Fragment mFrage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=(MainActivity)getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_content_track, null);
		findView();
		initTitleView();
		init();
		return mBaseView; 
	}
	
	private void findView(){
	}
	
	private void initTitleView(){

//		mTitleBarView.setBtnRightOnclickListener( new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mCanversLayout.getVisibility() == View.GONE ){
//					mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn_selected);
//					mCanversLayout.setVisibility(View.VISIBLE);
//				} else {
//					mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
//					mCanversLayout.setVisibility(View.GONE);
//				}
//			}
//		});
//		
//		mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
	}
	
	private void init(){
		mContext=(MainActivity)getActivity();
		if( mContext == null ){
			Log.i( "TrackFragment goole map mContext", "null");
			return;
		}
		
		FragmentManager fm=mContext.getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
//		TrackBMapFragment mBaiduTrack=new TrackBMapFragment();
		LocationGMapFragment mBaiduTrack=new LocationGMapFragment();
		mBaiduTrack.setUpMapIfNeeded();
		mFrage = mBaiduTrack;
		ft.replace(R.id.fl_content_track, mBaiduTrack,TrackFragment.TAG);
		ft.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		if( mFrage != null ){
			mFrage.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if( mFrage != null ){
			mFrage.onPause();
		}
	}
}
