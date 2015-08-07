package com.hylh.scooterstg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static boolean isConnect(Context context) { 
        // ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ��� 
//	    try { 
//	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
//	        if (connectivity != null) { 
//	            // ��ȡ�������ӹ���Ķ��� 
//	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
//	            if (info != null&& info.isConnected()) { 
//	                // �жϵ�ǰ�����Ƿ��Ѿ����� 
//	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
//	                    return true; 
//	                } 
//	            } 
//	        } 
//	    } 
//	    catch (Exception e) { 
//	// TODO: handle exception 
//	    	Log.v("error",e.toString()); 
//	    } 
//        return false; 
        

        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo[] info = mgr.getAllNetworkInfo();  
        if (info != null) {  
            for (int i = 0; i < info.length; i++) {  
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }
}
