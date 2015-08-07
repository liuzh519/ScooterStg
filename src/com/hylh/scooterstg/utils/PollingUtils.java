package com.hylh.scooterstg.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/**
 * @author Michael.Yu
 *
 * @date  2015年6月29日
 * 
 * @Description 消息推送工具类
 *
 * @version　1.0
 *
 */


public class PollingUtils {

	 //开启轮询服务  
	/**
	 * 
	 * 
	 * @param context  上下文
	 * @param triggerAtTime 触发服务的起始时间  
	 * @param seconds  相隔多少秒
	 * @param cls
	 * @param action
	 *void
	 * @exception
	 * @since  1.0.0
	 */
    public static void startPollingService(Context context, long triggerAtTime, int seconds, Class<?> cls) {  

		// 获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		// 包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(cls.getName());
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, seconds * 1000, pendingIntent);
    }  
   
    //停止轮询服务  
    public static void stopPollingService(Context context, Class<?> cls) {  
        AlarmManager manager = (AlarmManager) context  
                .getSystemService(Context.ALARM_SERVICE);  
        Intent intent = new Intent(context, cls);  
        intent.setAction(cls.getName());  
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,  
                intent, PendingIntent.FLAG_UPDATE_CURRENT);  
        //取消正在执行的服务  
        manager.cancel(pendingIntent);  
    }  
}
