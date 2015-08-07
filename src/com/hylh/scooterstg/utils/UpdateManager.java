package com.hylh.scooterstg.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import com.hylh.scooterstg.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.ProgressBar;
import android.widget.Toast;


public class UpdateManager
{
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	HashMap<String, String> mHashMap;
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;

	private Context mContext;
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	
	private boolean showMsg = false;
	private int serverVersion = 0;
	private SpUtil sp;

	private Handler hDoUpdate = new Handler(){
		public void handleMessage(Message msg){
			if (msg.arg1 == 1 ){
				if( msg.arg2 > 0 ){
					showDownloadDialog();
				}else {
					showNoticeDialog();
				}
			}
			else{
				if( showMsg ){
					ToastUtil.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_SHORT);
				}
				showMsg = false;
			}
		};
	};

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
		sp = SpUtil.getInstance();
	}

	public void checkUpdate( boolean alert )
	{
		UpdateThread ut = new UpdateThread( this );
		showMsg = alert;
		ut.start();
	}

	public class UpdateThread extends Thread {
		UpdateManager _um = null;
		int		_force = 0;
		
		public UpdateThread(UpdateManager um) {
			_um = um;
		}

		public void run() {
			Message msg = new Message();
			msg.arg1 = ( isUpdate() == true ? 1: 0 );
			msg.arg2 = _force;
			_um.hDoUpdate.sendMessage( msg );
		}

		private boolean isUpdate()
		{
			URL url;
			int versionCode = getVersionCode(_um.mContext);
			try
			{
				url = new URL("http://www.anbaostar.com/update/android/scooterstg/update.xml");
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setConnectTimeout(5000);
		        InputStream inStream = connection.getInputStream();
				ParseXmlService service = new ParseXmlService();
				_um.mHashMap = service.parseXml(inStream);
			} 
			catch (SocketTimeoutException e)
			{
				e.printStackTrace();
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (null != _um.mHashMap)
			{
				serverVersion = Integer.valueOf(_um.mHashMap.get("version"));
//				if( _um.mHashMap.get("force").compareTo( "true" ) == 0 ){
//					_force = 1;
//				}
				if (serverVersion > versionCode)
				{
					if( !showMsg )
					{
						int saveVer = sp.getInt( SpUtil.SYS_VER_CLIENT );
						Log.i("Update", serverVersion + " sav:" + saveVer );
						if( serverVersion > saveVer )
						{
							return true;
						}
					}
					else
					{
						return true;
					}
				}
			}
			return false;
		}

		private int getVersionCode(Context context)
		{
			int versionCode = 0;
			try
			{
				versionCode = context.getPackageManager().getPackageInfo("com.hylh.scooterstg", 0).versionCode;
			} catch (NameNotFoundException e)
			{
				e.printStackTrace();
			}
			return versionCode;
		}
	}

	private void showNoticeDialog()
	{ 
		try
		{
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(R.string.soft_update_title);
			builder.setMessage(R.string.soft_update_info);
	
			builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					showDownloadDialog();
				}
			});
	
			builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					sp.setInt( SpUtil.SYS_VER_CLIENT, serverVersion );
					dialog.dismiss();
				}
			});
			Dialog noticeDialog = builder.create();
			noticeDialog.setCancelable(false);
			noticeDialog.show();
		}
		catch( BadTokenException e ){
			e.printStackTrace();
		}
	}


	private void showDownloadDialog()
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.fragment_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);

		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.setCancelable(false);
		mDownloadDialog.show();
		downloadApk();
	}

	private void downloadApk()
	{
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
					mSavePath = sdpath + "scootert";
					URL url = new URL(mHashMap.get("url"));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do
					{
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}

						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安裑APK文件
	 */
	private void installApk()
	{
		File file = new File(mSavePath, mHashMap.get("name"));
		if (!file.exists())
		{
			return;
		}
		// 通过Intent安裑APK文件
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
//		mContext.startActivity(i);
		
		  Intent intent = new Intent();
		  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  intent.setAction(Intent.ACTION_VIEW);
		  String type = "application/vnd.android.package-archive";
		  intent.setDataAndType(Uri.fromFile(file), type);
		  mContext.startActivity(intent);
	}
}
