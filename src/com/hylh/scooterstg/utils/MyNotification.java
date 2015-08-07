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
    //֪ͨ������  
    private NotificationManager nm;  
      
    //֪ͨ��ʾ����  
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
        
        //����֪ͨ��״̬����ʾ��ͼ��  
        baseNF.icon = R.drawable.icon;  
          
        //֪ͨʱ��״̬����ʾ������  
        baseNF.tickerText = _title;  
        
          
        //֪ͨ��Ĭ�ϲ��� DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.   
        //���Ҫȫ������Ĭ��ֵ, �� DEFAULT_ALL.  
        //�˴�����Ĭ������  
        baseNF.defaults = 0;  
//        baseNF.defaults |= Notification.DEFAULT_SOUND;  
//        baseNF.defaults |= Notification.DEFAULT_VIBRATE;  
//        baseNF.defaults |= Notification.DEFAULT_LIGHTS;  
          
        //��������������ѭ����ֱ���û���Ӧ  
//        baseNF.flags |= Notification.FLAG_INSISTENT;  
          
//        //֪ͨ��������Զ���ʧ  
//        baseNF.flags |= Notification.FLAG_AUTO_CANCEL;  
          
        //���'Clear'ʱ���������֪ͨ(QQ��֪ͨ�޷�����������õ����)  
        baseNF.flags |= Notification.FLAG_NO_CLEAR;  
          
          
        //�ڶ������� ������״̬��ʱ��ʾ����Ϣ���� expanded message title  
        //����������������״̬��ʱ��ʾ����Ϣ���� expanded message text  
        //���ĸ������������֪ͨʱִ��ҳ����ת  
        baseNF.setLatestEventInfo(mcontext, _title, txt, pd);  
          
        //����״̬��֪ͨ  
        //The first parameter is the unique ID for the Notification   
        // and the second is the Notification object.  
        nm.notify(Notification_ID_BASE, baseNF);  
    }

    public void soundNotification( String txt){
        mediaNF = new Notification();  
        mediaNF.icon = R.drawable.icon;  
        mediaNF.tickerText = txt;  
          
        //�Զ�������  
        mediaNF.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");  
          
        //֪ͨʱ��������  
        //��һ������: ��ǰ�ȴ���ʱ��  
        //�ڶ��������� ��һ���񶯵�ʱ�����Դ�����  
        long[] vir = {0,100,200,300,200,300,200,300};  
        mediaNF.vibrate = vir;  
        mediaNF.setLatestEventInfo(mcontext, "scootert", txt, pd);  
        nm.notify(Notification_ID_MEDIA, mediaNF);  
    }

    public void updateNotification(String title, String txt){
        //����֪ͨ  
        //����״̬����ʾ��һ���¶��ţ���û���ü��鿴������һ���¶��ŵ���ʾ��  
        //��ʱ���ø���ԭ��֪ͨ�ķ�ʽ�Ƚϡ�  
        //(�����·�һ��֪ͨҲ���ԣ��������������֪ͨ�Ļ��ң�������ʾ���֪ͨ���û������û�Ҳ���Ѻ�)  
        baseNF.setLatestEventInfo(mcontext, _title, txt, pd);  
        nm.notify(Notification_ID_BASE, baseNF);  
    }

    public void clearNotification(){
        //��� baseNF  
        nm.cancel(Notification_ID_BASE);  
    }
}
