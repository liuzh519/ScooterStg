package com.hylh.scooterstg.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.ErrUtils;
import com.hylh.scooterstg.utils.MediaUtil;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.PickerView;
import com.hylh.scooterstg.view.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BindActivity extends FragmentActivity {
	private MyApplication mApp;
	private Context mContext;
	
    protected GoogleMap mMap;
	private TitleBarView mTitleBarView;
    protected PickerView picker;
    protected Button rent;

	protected SpUtil mSp;
	protected String tid;
	protected String num;

//	Handler mBind = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			if( msg.arg1 == 0 ){
//				SpUtil.getInstance().setTid(tid);
//				Utils.showDialog(mContext);
//				MediaUtil.onAction(Command.CMD_BIND, mContext );
//				finish();
//			}else if( msg.arg1 == -1 ){
//				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, BindActivity.this);
//			}else if( msg.arg1 == -2 ){
//				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, BindActivity.this);
//			}else if( msg.arg1 == -10 ){
//				Utils.showDialog("Error", ErrUtils.getRental( (String)msg.obj ), R.drawable.icon_error, BindActivity.this);
//			}else{
//				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, BindActivity.this);
//			}
//			
//			Utils.dismissProcess();
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		mApp=MyApplication.getInstance();
		mContext = this;
		findView();
		initView();
		setUpMapIfNeeded();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		picker = (PickerView) findViewById(R.id.picker);
		rent = (Button) findViewById(R.id.rent);
	}

	
	private void initView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_rent_scooter);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		rent.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String id = picker.getSelectedValue();
				JSONArray devs = StatusUtils.getDevList();
	        	try {
			        for (int i = 0; i < devs.length(); i++)
			        {
						JSONObject dev = devs.getJSONObject(i);
			        	if( dev.getString("number").compareTo( id ) == 0 ) {
			        		tid = dev.getString( "id" );
			        		num = dev.getString( "number" );
			        		break;
			        	}
			        }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	{
					Intent intent=new Intent(mContext, BindConfirmActivity.class);
					intent.putExtra("tid", tid);
					intent.putExtra("num", num);
					mContext.startActivity(intent);
	        	}
//	        	Utils.showProcess(mContext);
//				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
//				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
//				params.add(new BasicNameValuePair("command", "rent" ));  
//				params.add(new BasicNameValuePair("scooter_id", tid )); 
//				
//				mApp.getCmd().sendHttpGet( Utils.urlCommand, params, BindActivity.this, mBind, Command.MODE_SILENT);
			}
		});
	}

	private void init(){
		JSONArray devs = StatusUtils.getDevList();
		JSONObject park = StatusUtils.getSelPark();
		if( devs != null ){
        	try {
				List<String> data = new ArrayList<String>();
		        for (int i = 0; i < devs.length(); i++)
		        {
					JSONObject dev = devs.getJSONObject(i);
		        	data.add( dev.getString("number") );
		        }
		        picker.setData( data );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rent.setEnabled(false);
			}
		}
		else{
			rent.setEnabled(false);
		}
		
		try {
			updateMark(park);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	protected void clearUpMapIfNeeded(){
//		try{
//			if( mMap == null ){
//				return;
//			}
//			
//			FragmentManager fmgr = getFragmentManager();
//			if( fmgr == null ){
//				return;
//			}
//			
//		    SupportMapFragment fragment = (SupportMapFragment)(fmgr.findFragmentById(R.id.map));
//		    if( fragment != null ){
//			    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//			    ft.remove(fragment);
//			    ft.commit();
//		    }
//			
//			mMap = null;
//			if( mLocationClient != null ){
//				mLocationClient.disconnect();
//				mLocationClient = null;
//			}
//		}
//		catch ( IllegalStateException e ){
//
//		}
//		catch( RuntimeException e ){
//			
//		}
	}
	
    public void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            }
        }
    }

    
    protected void updateMark( JSONObject park ) throws JSONException{
		String sLoc = park.getString( "location" );
		JSONTokener jsonParser = new JSONTokener( sLoc );
		JSONObject location = (JSONObject) jsonParser.nextValue();
	    Marker mark;
		
    	JSONArray coordinates = location.getJSONArray( "coordinates" );

		double lat = coordinates.getDouble( 0 );
		double lng = coordinates.getDouble( 1 );

		mark = mMap.addMarker(
        		new MarkerOptions()
                .title( park.getString("short_name") )
                .snippet( park.getString("long_name") )
        		.position(new LatLng( lat, lng ) ) 
	        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking))
        		);
		lat += 0.015;
		setCenter( lat,lng, 12);
		mark.showInfoWindow();
	}

	protected void setCenter( double lat, double lng, float z ){
		if( mMap == null ){
			return;
		}
		if( z < 0 ){
			z = mMap.getCameraPosition().zoom;
		}
		
        mMap.moveCamera( CameraUpdateFactory.newCameraPosition( 
        		new CameraPosition.Builder().target(new LatLng( lat, lng ))
	                .zoom(z)
	                .bearing(0)
	                .build()) );
	}

	@Override
	public void onResume() {
		super.onResume();
		if( rent != null ){
			if( !SpUtil.getInstance().getTid().isEmpty() ){
				rent.setEnabled(false);
			} else {
				rent.setEnabled(true);
			}
		}
		
		if(Utils.mShowCtrl){
			finish();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		
	}

}
