package com.hylh.scooterstg.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.BindActivity;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.ErrUtils;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.StatusUtils.OnStatusEvent;
import com.hylh.scooterstg.utils.Utils;

public class TrackListTab implements OnStatusEvent {

	protected static final String TAG = "StatusFragment";
	private Context mContext;
	private Fragment mParent;
	private MyApplication mApp;
	private View mBaseView;

	private List<Map<String, Object>> mData;
	private ListView statusView;
	private StatusAdapter adapter;
	private TextView mTitle;
	private RelativeLayout mConfirm;
	
	public String mTid = "";
	public String mLpn = "";

	Handler mPark = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if( msg.arg1 == 0 ){
				try {
					JSONObject json = (JSONObject) msg.obj;
					JSONArray rows = json.getJSONArray( "s" );
					Utils.mShowCtrl = false;
					StatusUtils.setDevList(rows);
					Intent intent=new Intent(mContext, BindActivity.class);
					mContext.startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
					Utils.showDialog( R.string.error, R.string.exception_invalid_response, mContext, null);
				}
			}else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", mContext.getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -10 ){
				Utils.showDialog("Error", ErrUtils.getPark( (String)msg.obj ), R.drawable.icon_error, mContext, null);
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
			
			Utils.dismissProcess();
		}
	};
	
	public void onCreateView(Fragment parent, Context context, View view) {
		mParent=parent;
		mContext=context;
		mApp=MyApplication.getInstance();
		mBaseView=view;
		findView(mBaseView);
		init();
	}
	
	private void findView(View view){
		statusView = (ListView) view.findViewById(R.id.listView1);
		mTitle = (TextView) view.findViewById(R.id.l_title);
		mConfirm = (RelativeLayout) view.findViewById(R.id.confirm_layout);
	}
	
	void show( boolean show ){
		if( show ){
			statusView.setVisibility( View.VISIBLE );
		} else {
			statusView.setVisibility( View.GONE );
		}
	}
	
	private void init(){
		mData = getData(null);
		adapter = new StatusAdapter(mContext);
		statusView.setAdapter(adapter);
	}

	private List<Map<String, Object>> getData(JSONArray devs) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		if( devs == null )
			devs = StatusUtils.getStatus();
		
		//4. 地圖序停車場表單依照距離遠近做排(sort by distance near->far)
		//added by ycf on 20150706 begin
		for( int i = 0; i < devs.length(); i ++ ){
			map = new HashMap<String, Object>();
			try {
				JSONObject park = devs.getJSONObject(i);
				map.put("title", "Scooter" + (i+1) );
				map.put("short", park.getString("short_name") );
				map.put("long", park.getString("long_name") );
				map.put("count", park.getString("scooter_count") );
				map.put("id", park.getString("id") );
				map.put("dev", park );
				
				//added by ycf on 20150706 begin
				//取得停车场坐标
		/*		String sLoc = park.getString("location");
				JSONTokener jsonParser = new JSONTokener(sLoc);
				JSONObject location = (JSONObject)jsonParser.nextValue();
					
			    JSONArray coordinates = location.getJSONArray("coordinates");
			    map.put("coordinates", coordinates );
			    double lat = coordinates.getDouble(0);
				double lng = coordinates.getDouble(1);
				map.put("lat",lat);
				map.put("lng",lng);*/
				//added by ycf on 20150706 end
				
				list.add( map );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		//added by ycf on 20150706 begin
		if(list.size() > 0){
			Collections.sort(list, new Comparator(){
				@Override
				public int compare(Object lhs, Object rhs) {
					
					//当前手机位置坐标
//					double mLat = StatusUtils.mLat;
//					double mLng = StatusUtils.mLng;
					
					Map<String,Object> lhsMap = (Map)lhs;
					Map<String,Object> rhsMap = (Map)rhs;
					
//					double lat1 = (double)lhsMap.get("lat");
//					double lng1 = (double)lhsMap.get("lng");
//					double lat2 = (double)rhsMap.get("lat");
//					double lng2 = (double)rhsMap.get("lng");
//					
//					double dis = Utils.getDistance(lat1, lng1, lat2, lng1);
//					
//					if(0 < dis){
//						return 1;
//					}else if(dis < 0){
//						return -1;
//					}
					
					//modify by ycf on 20150827 begin
					//停車場表單照簡稱(short name) 來排序(A-Z)
					String lShortName = (String)lhsMap.get("short");
					String rShortname = (String)rhsMap.get("short");
					
					return lShortName.compareTo(rShortname);
					//modify by ycf on 20150827 end
						
				}
				
			});
		}
		//added by ycf on 20150706 end
		
		return list;
	}

	public class StatusAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public StatusAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}


		public final class ViewHolder {
			public TextView title;
			public TextView info;
			public TextView status;
			public RelativeLayout button;
			// public Button viewBtn;
		}
		public class OnItemClick implements OnClickListener{
			public String mTitle;
			
			OnItemClick( String title) {
				this.mTitle = title;
			}

			@Override
			public void onClick(View arg0) {
	        	JSONObject park = StatusUtils.getPark( mTitle );
	        	if( park != null ){
					StatusUtils.setSelPark( park );
					try {
						showConfirm(park.getString("long_name"), park.getString("short_name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.fragment_park_info_item, null);
				holder.title = (TextView) convertView.findViewById(R.id.txt_short);
				holder.info = (TextView) convertView.findViewById(R.id.txt_long);
				holder.status = (TextView) convertView.findViewById(R.id.count);
				holder.button = (RelativeLayout) convertView.findViewById(R.id.layout);
				holder.button.setOnClickListener( new OnItemClick((String)mData.get(position).get("id")) );
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.button.setOnClickListener( new OnItemClick((String)mData.get(position).get("id")) );
			}

			holder.title.setText((String) mData.get(position).get("short") );
			holder.info.setText((String) mData.get(position).get("long") );
			holder.status.setText((String) mData.get(position).get("count") );

			return convertView;
		}
	}
	
	public void showConfirm(String tid, String lpn){
//		mTitle.setText(lpn );
//		
//		mTid = tid;
//		mLpn = lpn;
//		mConfirm.setVisibility(View.VISIBLE);
		showParkInfo();
	}
	
	public void showParkInfo(){
		JSONObject dev = StatusUtils.getSelPark();
		String id;
		try {
			id = dev.getString("id");
			
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
			params.add(new BasicNameValuePair("command", "list_scooters" ));  
			params.add(new BasicNameValuePair("parking_station_id", id )); 
			
			mApp.getCmd().sendHttpsGet( Utils.urlParkStation, params, mApp, mPark, Command.MODE_TOAST);

			mConfirm.setVisibility(View.GONE);
			
			Utils.showProcess(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showDialog(R.string.error, R.string.exception_invalid_response, mContext, null );
		}
	}

	public void onResume() {
		StatusUtils.setDelay( mContext, this, StatusUtils.MAX_DELAY);
		if( adapter != null ){
			mData = getData(null);
			adapter.notifyDataSetChanged();
		}
	}

	public void onPause() {
		StatusUtils.clearDelay();
	}

	@Override
	public void onTimer(int t) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatus(JSONArray st) {
		// TODO Auto-generated method stub
		mData = getData(st);
		adapter.notifyDataSetChanged();
		
	}
}