package com.hylh.scooterstg.fragments;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.GMapFragment;
import com.hylh.scooterstg.view.TitleBarView;

public class LocationGMapFragment extends GMapFragment 
		implements StatusUtils.OnStatusEvent, 
        OnMarkerClickListener,
        OnInfoWindowClickListener {

	private MainActivity mContext;
	private MyApplication mApp;
	private static View mBaseView;
	private TitleBarView mTitleBarView;
	private TrackListTab mListView = new TrackListTab();

	public Button mOK;
	public Button mCl;
	private RelativeLayout mConfirm;
	private ImageButton car;
	private ListView list;
	private double lat,lng;
	private String tid;
	private int mLastView = 0;
	final int VIEW_MAP = 0;
	final int VIEW_LIST = 1;

	Handler mRefresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONObject tent = json.getJSONObject( "rental" );
					updateMarks(tent, true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	protected void bindDevice( JSONArray devs ){
		JSONObject dev;
		if( devs.length() > 0 ){
			try {
				dev = devs.getJSONObject(0);
				String tid = dev.getString( "tid" );
				String lpn = tid.substring( 5 );

				SpUtil sp = SpUtil.getInstance();
				sp.setTid( tid );
				sp.setTName( lpn );
				initTitleView();
				
				Utils.showDialog(mContext);
				mContext.setIndex(1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.showDialog(R.string.result_title_err, R.string.excepton_bind_error, mContext, null);
			}
		} else {
			Utils.showDialog(R.string.result_title_err, R.string.excepton_bind_error, mContext, null);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=(MainActivity)getActivity();
		mApp=MyApplication.getInstance();
		mSp=SpUtil.getInstance();
		
		clearUpMapIfNeeded();
		mBaseView=inflater.inflate(R.layout.fragment_dev_track_gmap, null);
		
		findView(mBaseView);
		mListView.onCreateView(this,mContext, mBaseView);
		initTitleView();
		init();
	    
		return mBaseView;
	}
	
	private void findView( View view ){
		mTitleBarView=(TitleBarView) view.findViewById(R.id.title_bar);
		car = (ImageButton) view.findViewById(R.id.car);
		mOK = (Button) view.findViewById(R.id.confirm);
		mCl = (Button) view.findViewById(R.id.cancel);
		mConfirm = (RelativeLayout) view.findViewById(R.id.confirm_layout);
	}

	public void initTitleView() {
		if( mTitleBarView == null )
			return;

			mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.GONE);

			mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					if (mTitleBarView.getTitleLeft().isEnabled()) {
						mTitleBarView.getTitleLeft().setEnabled(false);
						mTitleBarView.getTitleRight().setEnabled(true);

						mListView.show(true);
						mLastView = VIEW_LIST;
						car.setVisibility( View.GONE );
					}
				}
			});

			mTitleBarView.getTitleRight().setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					if (mTitleBarView.getTitleRight().isEnabled()) {
						mTitleBarView.getTitleLeft().setEnabled(true);
						mTitleBarView.getTitleRight().setEnabled(false);

						mListView.show(false);
						mLastView = VIEW_MAP;

						if( mSp.getTid().isEmpty() ){
							car.setVisibility( View.GONE );
						} else {
							car.setVisibility( View.VISIBLE );
						}
					}
				}
			});

			mTitleBarView.setVisibility( View.VISIBLE );
			if( mLastView == VIEW_LIST ) {
				mTitleBarView.getTitleLeft().performClick();
			} else {
				mTitleBarView.getTitleRight().performClick();
			}

		if( mSp.getTid().isEmpty() ){
			if( mMap != null )
				mMap.getUiSettings().setMyLocationButtonEnabled( true );
			car.setVisibility( View.GONE );//added by ycf on 20150820
			
		} else {
			if( mMap != null )
				mMap.getUiSettings().setMyLocationButtonEnabled( true );

			car.setVisibility( View.VISIBLE );

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
			params.add(new BasicNameValuePair("query", "rental" ));  
			mApp.getCmd().sendHttpsGet(Utils.urlUser, params, mApp, mRefresh, Command.MODE_TOAST);
		}
	}
	
	private void init(){
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		if( mMap == null ){
			return;
		}

//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
		
		mContext=(MainActivity)getActivity();
		if( mContext == null ){
			Log.i( "TrackGMapFragment goole map mContext", "null");
			return;
		}
		
		tid = SpUtil.getInstance().getTid();
		car.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
				params.add(new BasicNameValuePair("query", "rental" ));  
				mApp.getCmd().sendHttpsGet(Utils.urlUser, params, mApp, mRefresh, Command.MODE_TOAST);
				
			}
		});
		
		mCl.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				mSp.setTid( "" );
				mSp.setTName( "" );
				mConfirm.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if( mLocationClient != null ){
			mLocationClient.connect();
			Log.i("GMap", "location connect" );
		}
		mLastView = VIEW_MAP;
		initTitleView();
		
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		StatusUtils.setDelay( mContext, this, StatusUtils.MAX_DELAY);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if( mLocationClient != null )
			mLocationClient.disconnect();
		
		StatusUtils.clearDelay();
	}
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
		clearUpMapIfNeeded();
	}

	@Override
	public void onTimer(int t) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatus(JSONArray rows) {
		if( rows != null ){
			updateMarks( rows, false );
		}
		if( StatusUtils.isCheckBind() ){
			initTitleView();
		}
		mListView.onStatus(rows);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		JSONObject dev = StatusUtils.getPark(marker.getTitle());
		if( dev != null ){
			StatusUtils.setSelPark(dev );
			mListView.showConfirm( marker.getTitle(), marker.getSnippet() );
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
}
