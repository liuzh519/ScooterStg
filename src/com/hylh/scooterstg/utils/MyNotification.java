package com.hylh.scooterstg.utils;



import com.hylh.scooterstg.R;
import com.hylh.scooterstg.activity.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio;



public class MyNotification  {
	private Context mcontext;
    //通知管理器  
    private NotificationManager nm;  
      
    //通知显示内容  
    private PendingIntent pd;  
    
    private Notification baseNF;  
    private String _title;
    
    private int Notification_ID_BASE = 110;  
      
    //Notification ID  
    private int Notification_ID_MEDIA = 119;  
      
    private Notification mediaNF;  
    
    public MyNotification(Context context)
    {
    	mcontext = context;
        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        _title = mcontext.getResources().getString( R.string.notify_title );

        Intent intent = new Intent(context, MainActivity.class);

        pd = PendingIntent.getActivity(context, 0, intent, 0);  
    }
    
    public void baseNotification(String title, String txt){
        
    	baseNF = new Notification();  
        
        //设置通知在状态栏显示的图标  
        baseNF.icon = R.drawable.icon;  
          
        //通知时在状态栏显示的内容  
        baseNF.tickerText = _title;  
        
          
        //通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.   
        //如果要全部采用默认值, 用 DEFAULT_ALL.  
        //此处采用默认声音  
        baseNF.defaults = 0;  
//        baseNF.defaults |= Notification.DEFAULT_SOUND;  
//        baseNF.defaults |= Notification.DEFAULT_VIBRATE;  
//        baseNF.defaults |= Notification.DEFAULT_LIGHTS;  
          
        //让声音、振动无限循环，直到用户响应  
//        baseNF.flags |= Notification.FLAG_INSISTENT;  
          
//        //通知被点击后，自动消失  
//        baseNF.flags |= Notification.FLAG_AUTO_CANCEL;  
          
        //点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)  
        baseNF.flags |= Notification.FLAG_NO_CLEAR;  
          
          
        //第二个参数 ：下拉状态栏时显示的消息标题 expanded message title  
        //第三个参数：下拉状态栏时显示的消息内容 expanded message text  
        //第四个参数：点击该通知时执行页面跳转  
        baseNF.setLatestEventInfo(mcontext, _title, txt, pd);  
          
        //发出状态栏通知  
        //The first parameter is the unique ID for the Notification   
        // and the second is the Notification object.  
        nm.notify(Notification_ID_BASE, baseNF);  
    }

    public void soundNotification( String txt){
        mediaNF = new Notification();  
        mediaNF.icon = R.drawable.icon;  
        mediaNF.tickerText = txt;  
          
        //自定义声音  
        mediaNF.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");  
          
        //通知时发出的振动  
        //第一个参数: 振动前等待的时间  
        //第二个参数： 第一次振动的时长、以此类推  
        long[] vir = {0,100,200,300,200,300,200,300};  
        mediaNF.vibrate = vir;  
        mediaNF.setLatestEventInfo(mcontext, "scootert", txt, pd);  
        nm.notify(Notification_ID_MEDIA, mediaNF);  
    }

    public void updateNotification(String title, String txt){
        //更新通知  
        //比如状态栏提示有一条新短信，还没来得及查看，又来一条新短信的提示。  
        //此时采用更新原来通知的方式比较。  
        //(再重新发一个通知也可以，但是这样会造成通知的混乱，而且显示多个通知给用户，对用户也不友好)  
        baseNF.setLatestEventInfo(mcontext, _title, txt, pd);  
        nm.notify(Notification_ID_BASE, baseNF);  
    }

    public void clearNotification(){
        //清除 baseNF  
        nm.cancel(Notification_ID_BASE);  
    }
}
