package com.hylh.scooterstg.utils;

import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;

public class MediaUtil {
	MyApplication mApp;
	
	static private int loop = 0;
	static private MediaPlayer mp = null;
	static private MediaPlayer mpStart = null;
	static private MediaPlayer mpPanic = null;
	static private boolean startst = false;
	static private boolean panicst = false;
	static private Vibrator vibrator;  
	static private long[] pattern = {0, 70 }; // OFF/ON/OFF/ON...
	
	MediaUtil(MyApplication app){
		mApp = app;
		vibrator = (Vibrator)app.getSystemService(Service.VIBRATOR_SERVICE); 
	}
	

	static public void onAction( int lastcmd, Context context ) {
//		if( !SpUtil.getInstance().getMedia() )
//			return;
		
		switch (lastcmd) {
		case Command.CMD_BIND:
		case Command.CMD_LOCK: {
			onMP(R.raw.lock, context, 1);
			break;
		}
		case Command.CMD_UNLOCK:
		case Command.CMD_UNBIND: {
			onMP(R.raw.unlock, context, 1);
			break;
		}
		case Command.CMD_TRUNK: {
			onMP(R.raw.fob_sound2, context, 1);
			break;
		}
		case Command.CMD_PAINC: {
			onMP(R.raw.painc, context, 1);
//			onPainc(R.raw.painc);
			break;
		}
		case Command.CMD_START: {
			onMP(R.raw.fob_sound2, context, 1);
//			onStart(R.raw.enginestart);
			break;
		}
		case Command.CMD_STOP: {
			onMP(R.raw.fob_sound2, context, 1);
//			onStop(R.raw.enginestart);
			break;
		}
		case Command.CMD_CAMERA: {
			onMP(R.raw.fob_sound2, context, 1);
			break;
		}
		case Command.CMD_SIGN1: {
			onMP(R.raw.fob_sound2, context, 1);
			break;
		}
		case Command.CMD_SIGN2: {
			onMP(R.raw.fob_sound2, context, 1);
			break;
		}
		default:
			break;
		}
	}

	static public void onVibrate() {
//		if( !SpUtil.getInstance().getVibrate() )
//			return;
		
		try {
			if( null != vibrator ){
				vibrator.vibrate(pattern, -1);//-1不重复，非-1为从pattern的指定下标开始重复
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void onMP(int resid, Context context, int cnt) {
		try {
			if (true) {
				mp = MediaPlayer.create(context.getApplicationContext(), resid);
				mp.setLooping(false);

                mp.setOnCompletionListener( new MyOnCompletionListener(mp, cnt-1) );
			}

			if (null != mp) {
				mp.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class MyOnCompletionListener implements OnCompletionListener{
		private int loop;
		private MediaPlayer lmp;
		
		public MyOnCompletionListener( MediaPlayer mp, int cnt ){
			loop = cnt;
			lmp = mp;
		}
		@Override
		public void onCompletion(MediaPlayer mp) {
            if( loop > 0 ){
         	   loop--;
         	  lmp.start();
            }
            else{
            	lmp.release();
            }
		}
	}
}
