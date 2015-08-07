package com.hylh.scooterstg.view;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.SystemMethod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBarView extends RelativeLayout {

	private Context mContext;
	private RelativeLayout layoutLeft;
	private Button btnLeft;
	private RelativeLayout layoutRight;
	private ImageView imgRight;
	private RelativeLayout layoutRight2;
	private ImageView imgRight2;
	private Button btn_titleLeft;
	private Button btn_titleRight;
	private TextView tv_center;
	private LinearLayout common_constact;
	private boolean display_name = false;
	private SpUtil sp;
	private int titleId = 0;
	
	public TitleBarView(Context context){
		super(context);
		mContext=context;
		sp = SpUtil.getInstance();
		initView();
	}
	
	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		sp = SpUtil.getInstance();
		initView();
	}
	
	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		layoutLeft=(RelativeLayout) findViewById(R.id.title_btn_left);
		btnLeft=(Button) findViewById(R.id.title_btn_left_btn);
		layoutRight=(RelativeLayout) findViewById(R.id.title_btn_right);
		imgRight=(ImageView) findViewById(R.id.title_btn_right_img);
		layoutRight2=(RelativeLayout) findViewById(R.id.title_btn_right_sec);
		imgRight2=(ImageView) findViewById(R.id.title_btn_right_sec_img);
		btn_titleLeft=(Button) findViewById(R.id.constact_group);
		btn_titleRight=(Button) findViewById(R.id.constact_all);
		tv_center=(TextView) findViewById(R.id.title_txt);
		common_constact=(LinearLayout) findViewById(R.id.common_constact);
		
	}
	
	public void setCommonTitle(int LeftVisibility,int centerVisibility,int center1Visibilter,int rightVisibility){
		layoutLeft.setVisibility(LeftVisibility);
		btnLeft.setVisibility(LeftVisibility);
		layoutRight.setVisibility(rightVisibility);
		imgRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_constact.setVisibility(center1Visibilter);

		layoutRight2.setVisibility(View.GONE);
	}
	
	public void setRight2( int visible, int icon ){
		layoutRight2.setVisibility(visible);
		if(icon > 0){
			setBtnRight2(icon);
		}
	}
	
	public void setBtnLeft(int icon,int txtRes){
		Drawable img=mContext.getResources().getDrawable(icon);
		int height=SystemMethod.dip2px(mContext, 20);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setText(txtRes);
		btnLeft.setCompoundDrawables(img, null, null, null);
	}
	
	public void setBtnLeft(int txtRes){
		btnLeft.setText(txtRes);
	}
	
	public void setBtnLeftImg(int icon){
		if( icon > 0 ){
			layoutLeft.setVisibility( View.VISIBLE );
			layoutLeft.setBackgroundResource(icon);
		} else {
			layoutLeft.setVisibility( View.INVISIBLE );
		}
	}
	
	
	public void setBtnRight(int icon){
		Drawable img=mContext.getResources().getDrawable(icon);
		int height=SystemMethod.dip2px(mContext, 30);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		imgRight.setImageDrawable(img);
	}
	
	public void setBtnRight2(int icon){
		Drawable img=mContext.getResources().getDrawable(icon);
		int height=SystemMethod.dip2px(mContext, 30);
		int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		imgRight2.setImageDrawable(img);
	}
	
	public void setTitleLeft(int resId){
		btn_titleLeft.setText(resId);
	}
	
	
	public void setTitleRight(int resId){
		btn_titleRight.setText(resId);
	}
	
	public void setBtnRightCmd(int tp){
		switch(tp)
		{
		case Command.CTRL_BT:
			setBtnRight(R.drawable.main_icon_bt_sel);
			break;
		case Command.CTRL_SMS:
			setBtnRight(R.drawable.main_icon_sms_sel);
			break;
		case Command.CTRL_TCP:
		default:
			setBtnRight(R.drawable.main_icon_wifi_sel);
			break;
		}
	}
	
	@SuppressLint("NewApi")
	public void setPopWindow(PopupWindow mPopWindow,TitleBarView titleBaarView){
			mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
			mPopWindow.showAsDropDown(titleBaarView, 0,-15);
			mPopWindow.setAnimationStyle(R.style.popwin_anim_style);
			mPopWindow.setFocusable(true);
			mPopWindow.setOutsideTouchable(true);
			mPopWindow.update();
			
//D			setBtnRight(R.drawable.skin_conversation_title_right_btn_selected);
		}
	
	public void setTitleText(int resId){
		titleId = resId;
		if( display_name ){
			tv_center.setText(mContext.getResources().getString( resId ) + "-" + sp.getTName() );
		}else {
			tv_center.setText(resId);
		}
	}
	
	public void disDevName( boolean flag ){
		display_name = flag;
		
		if( display_name && titleId > 0 && tv_center != null ){
			tv_center.setText(mContext.getResources().getString( titleId ) + "-" + sp.getTName() );
		} else {
			tv_center.setText(sp.getTName() );
		}
	}
	
	public void setBtnLeftOnclickListener(OnClickListener listener){
		layoutLeft.setOnClickListener(listener);
		btnLeft.setOnClickListener(listener);
	}
	
	public void setBtnRightOnclickListener(OnClickListener listener){
		layoutRight.setOnClickListener(listener);
	}
	
	public void setBtnRight2OnclickListener(OnClickListener listener){
		layoutRight2.setOnClickListener(listener);
	}
	
	public Button getTitleLeft(){
		return btn_titleLeft;
	}
	
	public Button getTitleRight(){
		return btn_titleRight;
	}
	
	public void destoryView(){
		btnLeft.setText(null);
//		btnRight.setText(null);
		tv_center.setText(null);
	}

}
