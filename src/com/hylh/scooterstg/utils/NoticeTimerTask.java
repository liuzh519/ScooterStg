package com.hylh.scooterstg.utils;

import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;



/**
 * @author Michael.Yu
 *
 * @date  2015年7月16日
 * 
 * @Description TODO
 *
 * @version　1.0
 *
 */

public class NoticeTimerTask extends TimerTask {

	 @Override    
	    public void run() {    
	        Message message = new Message();    
	        message.what = 2;    
	        myHandler.sendMessage(message);  
	    }    
	 
	//Handler可以通过message在各个线程间传递通信    
		Handler myHandler = new Handler() {    
		    // 接收到消息后处理    
		    public void handleMessage(Message msg) {    
		        switch (msg.what) {    
		        case 1:    
		            //UI操作  
		            break;    
		        case 2:    
		            //UI操作  
		            break;    
		        }    
		        super.handleMessage(msg);    
		    }    
		}; 
		
		
		
	    
}
