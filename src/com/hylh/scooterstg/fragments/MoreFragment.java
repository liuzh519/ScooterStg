package com.hylh.scooterstg.fragments;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.LoginActivity;
import com.hylh.scooterstg.activity.MainActivity;
import com.hylh.scooterstg.activity.OrientationActivity;
import com.hylh.scooterstg.activity.SysSettingAcitivity;
import com.hylh.scooterstg.activity.UserSettingAcitivity;
import com.hylh.scooterstg.activity.WebkitActivity;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.ErrUtils;
import com.hylh.scooterstg.utils.MediaUtil;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.StatusUtils;
import com.hylh.scooterstg.utils.UpdateManager;
import com.hylh.scooterstg.utils.Utils;

public class MoreFragment extends Fragment {
	private MainActivity mContext;
	private View mBaseView;

	private RelativeLayout mLogin;
	private RelativeLayout mLgout;
	private RelativeLayout mRegister;
	private RelativeLayout mProfile;
	private RelativeLayout mPromoCode;
	private RelativeLayout mOrientation;
	private RelativeLayout mRoadside;
	private RelativeLayout mCallUs;
	private RelativeLayout mEmergency;
	private RelativeLayout mWriteUs;
	private RelativeLayout mLeage;
	

    protected RelativeLayout lconfirm;
    protected Button mOk;
    protected Button mCancel;
    protected TextView mPromo;
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=(MainActivity)getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_more, null);
		findView();
		init();
		return mBaseView;
	}
	
	private void findView(){
		mLogin = (RelativeLayout) mBaseView.findViewById(R.id.more_login);
		mLgout = (RelativeLayout) mBaseView.findViewById(R.id.more_logout);
		mRegister = (RelativeLayout) mBaseView.findViewById(R.id.more_register);
		mProfile = (RelativeLayout) mBaseView.findViewById(R.id.more_profile);
		mPromoCode = (RelativeLayout) mBaseView.findViewById(R.id.more_promo_code);
		mOrientation = (RelativeLayout) mBaseView.findViewById(R.id.more_orientation);
		mRoadside = (RelativeLayout) mBaseView.findViewById(R.id.more_roaddide);
		mCallUs = (RelativeLayout) mBaseView.findViewById(R.id.more_callus);
		mEmergency = (RelativeLayout) mBaseView.findViewById(R.id.more_emergency);
		mWriteUs = (RelativeLayout) mBaseView.findViewById(R.id.more_writeus);
		mLeage = (RelativeLayout) mBaseView.findViewById(R.id.more_legal);

		lconfirm = (RelativeLayout) mBaseView.findViewById(R.id.confirm_layout);
		mOk = (Button) mBaseView.findViewById(R.id.ok);
		mCancel = (Button) mBaseView.findViewById(R.id.cancel);
		mPromo = (TextView) mBaseView.findViewById(R.id.editText1);
	}
	
	private void setControl(){
		if( SpUtil.getInstance().getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){
			mRegister.setVisibility(View.GONE);
			mLgout.setVisibility(View.VISIBLE);
			mLogin.setVisibility(View.GONE);
			mProfile.setVisibility(View.VISIBLE);
		} else {
			mLogin.setVisibility(View.VISIBLE);
			mProfile.setVisibility(View.GONE);
			mLgout.setVisibility(View.GONE);
			mRegister.setVisibility(View.VISIBLE);
		}
	}
	
	private void init(){
		lconfirm.setVisibility(View.GONE);

		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (mContext,LoginActivity.class);
				startActivity(intent);
			}
		});
		mLgout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SpUtil sp = SpUtil.getInstance();
				sp.setInt( SpUtil.LOGIN_KEEP, 0 );
				sp.setSTKey( "" );
				sp.setTid("" );
				SpUtil.getInstance().setLegal(true);
				mLogin.setVisibility(View.VISIBLE);
				mProfile.setVisibility(View.VISIBLE);
				setControl();
			}
		});
		mRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Uri uri = Uri.parse("http://www.scootawayscooters.com/register.html");  
//				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//				startActivity(it);

				Intent it = new Intent(mContext, WebkitActivity.class);  
				it.putExtra("url", Utils.urlReg );
				it.putExtra("mode", "finish");
				startActivity(it);
			}
		});
		mProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(Utils.urlProfile);  
				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(it);
			}
		});

		mOrientation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, OrientationActivity.class);  
				startActivity(it);
			}
		});
		mRoadside.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                String number = "8004498219";  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
                startActivity(intent); 
			}
		});
		mCallUs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                String number = "8004498219";  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
                startActivity(intent); 
			}
		});
		mEmergency.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                String number = "8004498219";  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
                startActivity(intent); 
			}
		});
		mWriteUs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {Intent data=new Intent(Intent.ACTION_SENDTO);
			data.setData(Uri.parse("mailto:support@scootawayscooters.com"));
			data.putExtra(Intent.EXTRA_SUBJECT, "Title");
			data.putExtra(Intent.EXTRA_TEXT, "Context");
			startActivity(data); 
			}
		});
		mLeage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Uri uri = Uri.parse("http://www.scootawayscooters.com/legal.html");  
//				Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//				startActivity(it);
				Intent it = new Intent(mContext, WebkitActivity.class);  
				it.putExtra("url", Utils.urlLegal);
				it.putExtra("mode", "finish");
				it.putExtra("title", R.string.title_legal );
				startActivity(it);
			}
		});
		
		mPromoCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( SpUtil.getInstance().getInt( SpUtil.LOGIN_KEEP, 0 ) > 0 ){
					lconfirm.setVisibility(View.VISIBLE);
				} else {
					Utils.showDialog("Info", "Login to apply promo code!", R.drawable.option_blank, mContext, null);
				}
			}
		});
		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String txt = mPromo.getText().toString();
				if( txt.isEmpty() ){
					Utils.showDialog( R.string.excepton_error, R.string.exception_promo, mContext, null);
					return;
				}

				Utils.showProcess(mContext);
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("coupon_code", txt));
				params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
				Log.i("login", params.toString() );
				MyApplication.getInstance().getCmd().sendHttpsPut(Utils.urlCoupon
											, params
											, mContext
											, hpromo
											, Command.MODE_UNKNOWN );
				
				lconfirm.setVisibility(View.GONE);
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lconfirm.setVisibility(View.GONE);
			}
		});
	}

	private Handler hpromo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utils.dismissProcess();
			if (msg.arg1 == 0) {
				Utils.showDialog(mContext);
			}else if( msg.arg1 == -1 ){
				Utils.showDialog("Error", (String)msg.obj, R.drawable.icon_error, mContext, null);
			}else if( msg.arg1 == -2 ){
				Utils.showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error, mContext, null);
			} else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				if( code.compareTo("1002") == 0 ){
					Utils.login(mContext);
				} else {
					Utils.showDialog("Error", ErrUtils.getCoupon((String)msg.obj ), R.drawable.icon_error, mContext, null);
				}
			}else{
				Utils.showDialog(R.string.error, R.string.exception_unknown, R.drawable.icon_error, mContext, null);
			}
		}
	};

	
	@Override
	public void onResume() {
		super.onResume();
		if( mRegister != null ){
			setControl();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
