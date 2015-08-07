package com.hylh.scooterstg.view;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.ToastUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GMapFragment extends Fragment implements 
	ConnectionCallbacks,
	OnConnectionFailedListener,
	OnMyLocationButtonClickListener,
	LocationListener 
	{
	private MainActivity mContext;
	private MyApplication mApp;
	protected View mBaseView;

    protected GoogleMap mMap;
    protected LocationClient mLocationClient;

    protected static double loc_lat = 0;
    protected static double loc_lng = 0;
    protected double lat = 0;
    protected double lng = 0;
    protected float zoom = 0;
    protected boolean first = true;
    protected Map<String, Marker>  mMarks = new HashMap<String, Marker>();
    protected Marker mMark;
    protected int  ver = 0;

	protected SpUtil mSp;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	public  void animateMapStatus(LatLng latlng){
	}

    class CustomInfoWindowAdapter implements InfoWindowAdapter {
//        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
//            mWindow = mContext.getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = mContext.getLayoutInflater().inflate(R.layout.fragment_park_map_item, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            render(marker, mWindow);
//            return mWindow;
        	return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            String name = marker.getTitle();
        	JSONObject park = StatusUtils.getPark( name );
        	if( park != null ){
    			TextView title = (TextView) view.findViewById(R.id.txt_short);
    			TextView info = (TextView) view.findViewById(R.id.txt_long);
    			TextView status = (TextView) view.findViewById(R.id.count);

    			try {
					title.setText(park.getString("short_name") );
	    			info.setText(park.getString("long_name") );
	    			status.setText(park.getString("scooter_count") );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	} else {
    			TextView title = (TextView) view.findViewById(R.id.txt_short);
    			TextView info = (TextView) view.findViewById(R.id.txt_long);
    			TextView status = (TextView) view.findViewById(R.id.count);
				title.setText(marker.getTitle());
    			info.setText(marker.getSnippet());
    			status.setText("");
        	}
        }
    }

	protected void clearUpMapIfNeeded(){
		try{
			if( mMap == null ){
				return;
			}
			
			FragmentManager fmgr = getFragmentManager();
			if( fmgr == null ){
				return;
			}
			
		    SupportMapFragment fragment = (SupportMapFragment)(fmgr.findFragmentById(R.id.map));
		    if( fragment != null ){
			    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			    ft.remove(fragment);
			    ft.commit();
		    }
			
			mMap = null;
			if( mLocationClient != null ){
				mLocationClient.disconnect();
				mLocationClient = null;
			}
		}
		catch ( IllegalStateException e ){

		}
		catch( RuntimeException e ){
			
		}
	}
	
    public void setUpMapIfNeeded() {
    	if( mMap != null ){
    		return;
    	}
    	
		mContext=(MainActivity)getActivity();
		mApp=MyApplication.getInstance();
		if( mContext == null ){
			return;
		}
		
		FragmentManager mgr = getFragmentManager();
		if( mgr != null ){
			SupportMapFragment smf = (SupportMapFragment)mgr.findFragmentById(R.id.map);
			if( smf != null ){
				Log.i( "goole map", smf.toString());
		        mMap = smf.getMap();
		        if (mMap != null) {
	                setUpMap();
		        }
			}
		} 
		if( loc_lat != 0 && loc_lng != 0 ){
			setCenter( loc_lat, loc_lng, 12 );
		}
    }

    private void setUpMap() {
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }
    
    protected void setUpLocationClientIfNeeded() {
    	if( mMap != null && mLocationClient == null ){
	    	mMap.setMyLocationEnabled(true);
	        mMap.setOnMyLocationButtonClickListener(this);
	        if (mLocationClient == null) {
	            mLocationClient = new LocationClient(
	            		mApp.getApplicationContext(),
	                    this,  // ConnectionCallbacks
	                    this); // OnConnectionFailedListener
	            mLocationClient.connect();
	        }
    	}
    }

	public void updateMarks(JSONArray rows, boolean focus) {
        if( zoom < 0.001 ){
        	zoom = 12;
        } else {
        	zoom = mMap.getCameraPosition().zoom;
        }
        updateMarks( rows, zoom, focus );
	}
	
	public void updateMarks(JSONArray rows, float z, boolean focus) {
		// TODO Auto-generated method stub
		JSONObject park;

		if( mMap == null ){
			return;
		}
		
		if( mSp.getTid().isEmpty() && mMark != null ){
			mMap.clear();
			mMarks.clear();
		}

		ver ++;
		
		try {
			for( int i = 0; i < rows.length(); i ++ ){
				park = rows.getJSONObject(i);
				if( park.has( "location" ) ){
					try {
						updateMarks( park, focus );
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//        if( z < 0.001 ){
//        	zoom = 12;
//        } else if( z < 1 ){
//        	zoom = mMap.getCameraPosition().zoom;
//        }
		
//        mMap.moveCamera( CameraUpdateFactory.newCameraPosition( 
//        		new CameraPosition.Builder().target(new LatLng( lat, lng ) )
//	                .zoom(zoom)
//	                .bearing(0)
//	                .build()) );
	}
    
    protected void updateMarks( JSONObject park, boolean focus ) throws JSONException{
    	if( !park.has("location") ){
    		return;
    	}
		String sLoc = park.getString( "location" );
		JSONTokener jsonParser = new JSONTokener( sLoc );
		JSONObject location = (JSONObject) jsonParser.nextValue();
	    Marker mark;
	    if( mMap == null ){
	    	return;
	    }
		
    	JSONArray coordinates = location.getJSONArray( "coordinates" );

		double lat = coordinates.getDouble( 0 );
		double lng = coordinates.getDouble( 1 );

		if( park.has("long_name") ){
			Object[] rows = mMarks.values().toArray();
			for( int i = 0; i < rows.length; i ++ ){
				Marker item = (Marker) rows[i];
				JSONObject pk = StatusUtils.getPark( item.getTitle() );
				if( pk.getString("long_name").compareTo( park.getString("long_name")) == 0 ){
					return;
				}
			}
			
			mark = mMap.addMarker(
	        		new MarkerOptions()
	                .title( park.getString("id") )
	                .snippet( park.getString("long_name") )
	        		.position(new LatLng( lat, lng ) ) 
		        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking))
	        		);
			mMarks.put(mark.getTitle(), mark);
		} else {
			if( !mSp.getTid().isEmpty() ){
				if( mMark != null ){
					mMark.setPosition(new LatLng( lat, lng ));
				} else {
				mMark = mMap.addMarker(
		        		new MarkerOptions()
		                .title( park.getString("number") )
		                .snippet( park.getString("id") )
		        		.position(new LatLng( lat, lng ) ) 
			        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
		        	);
				}
			}
		}
		
		if( focus )
			setCenter( lat, lng, -1 );

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
	public boolean onMyLocationButtonClick() {
		if( loc_lat != 0 && loc_lng != 0 ){
			setCenter( loc_lat, loc_lng, -1 );
		} else {
			ToastUtil.makeText(mContext, getResources().getString(R.string.wait_gps), Toast.LENGTH_SHORT );
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		loc_lat = arg0.getLatitude();
		loc_lng = arg0.getLongitude();
		
		StatusUtils.setPos( loc_lng, loc_lat );
		Log.i("GMap", "onLocationChanged");
		if( first ){
			first = false;
			StatusUtils.update();
			setCenter(loc_lat, loc_lng, 13);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.i("GMap", "Connect failed");
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
		Log.i("GMap", "onConnected");
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		mLocationClient.connect();
		
	}

}
