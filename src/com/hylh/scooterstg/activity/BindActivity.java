package com.hylh.scooterstg.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.PickerView;
import com.hylh.scooterstg.view.TitleBarView;

public class BindActivity extends FragmentActivity {
	private MyApplication mApp;
	private Context mContext;
	
    protected GoogleMap mMap;
	private TitleBarView mTitleBarView;
    protected PickerView picker;
    protected Button rent;
    protected TextView rentInfo;
    protected TextView rentTitle;

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
		rentTitle = (TextView) findViewById(R.id.title);
		rentInfo = (TextView) findViewById(R.id.rent_info);
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
	        	//modify by ycf on 20150811 begin
	        	/*
	        	   A.用戶選好車號後，按下"rent"按鈕 (如附檔 select scooter page)
	        	    B.出現"用戶同意條款" 下方顯示"agree"，右上方顯示 "cancel" (如附檔 member agreement page)
	        	    C.
	        	        i.用戶選擇agree後，畫面回到認知頁面，同時下方第七點選項自動打勾 (如圖acknowledgement page)
	        	        ii.用戶若選擇cancel，畫面一樣回到認知頁面，但下方第七點選項不打勾
	        	    D.除了第六點可選或不選之外，其他選項都必須勾選後才能讓用戶按下下方確認租車按鈕。*/  
	        	
	        	//modify by ycf on 20150818 begin
	        	//8.用戶同意條款只在用戶每次登入第一次租車時出現，除非重新登入或是換用戶登入，下次租車時部會再出現用戶條款。
	        	if( SpUtil.getInstance().getInt( SpUtil.LOGIN_KEEP, 0 ) <= 0 ){//未登录状态
	        		Intent intent = new Intent (mContext,LoginActivity.class);
					startActivity(intent);
					return;
	        	}
	        	
	        	Intent intent = null;
	        	if(SpUtil.getInstance().getRentCnt() == 0){
	        		intent=new Intent(mContext, WebkitActivity.class);
					intent.putExtra("title", "Member Agreement");
					intent.putExtra("mode", "rent");
					intent.putExtra("url", Utils.urlLegal);
	        	}else{
	        		intent=new Intent(mContext, BindConfirmActivity.class);
					intent.putExtra("action","accept");
	        	}
	        	
	        	intent.putExtra("tid", tid);
				intent.putExtra("num", num);
				startActivity(intent);
			
	        	//modify by ycf on 20150811 end
				
				//modify by ycf on 20150818 end
	        	
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
	
	private void updateRent(){
		
		Map<String,String> map;
		map = picker.getSelectedItem();
		if( map == null ){
			this.checkHadRented(); //added by ycf on 20150820 
			return;
		}

		if( map.get("in_service").compareTo("false") == 0 ){
			rentInfo.setText( "Not in service" );
			rent.setEnabled(false);
			picker.setBackgroundResource(R.drawable.scooter_number_service_off);
		} else if( map.get("online").compareTo("false") == 0 ){
			rentInfo.setText( "Not available for rent" );
			rent.setEnabled(false);
			picker.setBackgroundResource(R.drawable.scooter_number_offline);
		} else if( map.get("rented").compareTo("true") == 0 ){
			rentInfo.setText( "Already rented" );
			rent.setEnabled(false);
			picker.setBackgroundResource(R.drawable.scooter_number_rented);
		} else {
			rent.setEnabled(true);
			rentInfo.setText( "Available for rent" );
			picker.setBackgroundResource(R.drawable.scooter_number_background_m);
		}
		
		this.checkHadRented(); //added by ycf on 20150820 
	}
	
	
	//added by ycf on 20150820 begin
	/**
	 * 检测是否已租车
	 */
	private void checkHadRented(){
	
		if (!Utils.isEmpty(SpUtil.getInstance().getTid())) {
			rentTitle.setText(getResources().getString(R.string.title_had_rented));
			rent.setEnabled(false);
			return;
		}
	}
	//added by ycf on 20150820 end

	private void init(){
		JSONArray devs = StatusUtils.getDevList();
		JSONObject park = StatusUtils.getSelPark();
		picker.setOnSelectListener( new PickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				// TODO Auto-generated method stub
				updateRent();
			}
		});
		
		if( devs != null && devs.length() > 0 ){
        	try {
				List< Map<String,String> > data = new ArrayList<Map<String,String>>();
				Map<String,String> map;
		        for (int i = 0; i < devs.length(); i++)
		        {
		        	map = new TreeMap<String,String>();
					JSONObject dev = devs.getJSONObject(i);
					map.put("number", dev.getString("number") );
					map.put("rented", dev.getString("rented") );
					map.put("in_service", dev.getString("in_service") );
					map.put("online", dev.getString("online") );
					
		        	data.add( map );
		        }
		        picker.setData( data );
//		        picker.setBackgroundResource(R.drawable.scooter_number_background_m);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rent.setEnabled(false);
			}
		}
		else{
			rentTitle.setText("No scooters available at this station");
			rentInfo.setText( "Not in service" );
//			picker.setBackgroundResource(R.drawable.scooter_number_service_off);
			rent.setEnabled(false);
		}
		
		try {
			if(park != null){
				updateMark(park);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateRent();
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
        	//modify by ycf on 20150826 begin
        	try{
        		 // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
        	}catch(Exception ex){
        		
        	}//modify by ycf on 20150826 end
           
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
				updateRent();
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
