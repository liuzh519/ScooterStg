package com.hylh.scooterstg.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;


/**
 * @author Michael.Yu
 *
 * @date  2015年6月29日
 * 
 * @Description TODO
 *
 * @version　1.0
 *
 */


public class PollingService extends Service {
    
    private Notification mNotification;  
    private NotificationManager mManager;  
   
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
   
    @Override  
    public void onCreate() {  
        initNotifiManager();  
    }  
       
    @Override  
    public void onStart(Intent intent, int startId) {  
    	
    	new Thread(new Runnable() {
			@Override
			public void run() {
				 showNotification();   
			}
		}).start();
    	
    }  
   
    //初始化通知栏配置  
    private void initNotifiManager() {  
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        int icon = R.drawable.icon;  
        mNotification = new Notification();  
        mNotification.icon = icon;  
        mNotification.tickerText = "New Message";  
        mNotification.defaults |= Notification.DEFAULT_SOUND;  
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;  
    }  
   
    //弹出Notification  
    private void showNotification() {  
        mNotification.when = System.currentTimeMillis();  
        //Navigator to the new activity when click the notification title  
        Intent i = new Intent(this, MainActivity.class);  
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);  
        mNotification.setLatestEventInfo(this,  getResources().getString(R.string.app_name), "You have new message!", pendingIntent);  
        mManager.notify(0, mNotification); //发动通知,id由自己指定，每一个Notification对应的唯一标志 
    }  
    
//    private void testNotification(int id){
//		  NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        //构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
//        Notification notification = new Notification(R.drawable.blue_button_selector,"通知",System.currentTimeMillis());
//        Intent intent = new Intent(this,MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);                                                                          notification.setLatestEventInfo(getApplicationContext(), "通知标题", "通知显示的内容", pendingIntent);
//        notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失
//        notification.defaults = Notification.DEFAULT_SOUND;//声音默认
//        manager.notify(id, notification);//发动通知,id由自己指定，每一个Notification对应的唯一标志
//        //其实这里的id没有必要设置,只是为了下面要用到它才进行了设置
//	}
   
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        System.out.println("Service:onDestroy");  
    }  

}
