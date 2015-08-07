package com.hylh.scooterstg.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {
	
	   public MyViewPager(Context context) {  
	        super(context);  
	    }  
	  
	    public MyViewPager(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    } 
	    
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
//		return true;
        return super.dispatchTouchEvent(ev);
    }
    @Override  
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {  
        if(v.getClass().getName().equals("com.baidu.mapapi.map.MapView")) {
            return true;  
        }else if(v.getClass().getName().equals("android.widget.RadioGroup") ||
        		v.getClass().getName().equals("android.widget.RadioButton") ) {
        	return true;
        }
//    	Log.i( "viewpager", v.getClass().getName() );

        return super.canScroll(v, checkV, dx, x, y); 
    }
}
