package com.hylh.scooterstg.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.UUID;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.ClsUtils;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BluetoothServer {
	public static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final String name = "abssrv";
	public static final int UNKNOWN = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECTED = 2;
	public static final int RUNNING = 3;
	public static final int STOP = 4;
	public static int status = UNKNOWN;

	public static BluetoothSocket client;
	public static InputStreamReader sockInStream;
	public static InputStream inStream;
	public static PrintWriter sockOutStream;
	public static Activity activity = null;
	public static BluetoothAdapter btAdapter;
	public static BluetoothDevice btDevice = null;
	public static Handler handler;
	public static String tid;
	public static char translate[] = { 0x3D, 0x1C };
	
	public BluetoothServer() {
	}
	
	static public boolean init(Context context) {
		if( btAdapter == null ) {
			btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter == null) {
				return false;
			}
		}
		return true;
	}
	
	static public boolean check() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			return false;
		} else {
			if (!btAdapter.isEnabled()) {
				btAdapter.enable();
			}
		}
		return true;
	}

	static public int initConn(Context context) {
//		if (!checkEnv()) {
//			Utils.response( 1, R.string.bluetooth_not_support, context );
//			return -1;
//		}
//		Log.i("Bluetooth", "getOriDev");
//		if (null == getOriDev()) {
//			Log.i("Bluetooth", "findDev");
////			findDev();
//
//			Utils.response( 1, "还没有与终端配对", h );
//			return -1;
//		}

		return 0;
	}

	static public void release() {
//		if( null != mReceiver && bReceiver ) {
//			Utils.maintab.unregisterReceiver(mReceiver);
//			bReceiver = false;
//		}
		
//		if( !oriEnable ){
//			btAdapter.disable();
//			oriEnable = false;
//		}
	}

	public static void SendCmd(String tid, Handler h, String m, int seq, Context c) {
		Log.i("Bluetooth", tid + " SendCmd dev:" + btDevice);
		if( !checkEnv(tid) ) {
			Utils.response( -1, "还没有与终端配对", h );
			return;
		}
		
		Log.i("Bluetooth", "BlueboothThread");
		BlueboothThread blue = new BlueboothThread(h, m, seq, c);
		blue.start();
	}

	static public void start() {
		// if( null != btDevice && (status == UNKNOWN || status == STOP) ){
		// Log.i( "Bluetooth", "start " );
		// at = new AcceptThread();
		// if( 0 == at.init(handler) ){
		// //if( 0 == at.initClient(handler) ){
		// status = CONNECTING;
		// adapter.cancelDiscovery();
		// at.start();
		// }
		// }
	}

	static public boolean checkEnv(String tid) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			return false;
		} else {
			if (!btAdapter.isEnabled()) {
				btAdapter.enable();
			}
		}

		if (null == getOriDev(tid)) {
			return false;
//			findDev();
		}
		return true;
	}

	static public BluetoothDevice getOriDev(String t) {
		if( btDevice != null && tid == t ){
			return btDevice;
		} else {
			tid = t;
			client = null;
		}
		
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		Log.i( "bt dev", "tid:" + tid );
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice dev : pairedDevices) {
				//if (dev.getAddress().compareTo(Utils.bluetoothAddr) == 0) {
				if (dev.getName().compareTo(tid) == 0) {
					Utils.bluetoothAddr = dev.getAddress();
					btDevice = dev;
					return dev;
				}
				Log.i( "bt dev", dev.getName() );
			}
		}
		return null;
	}

	static private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			SpUtil sp = SpUtil.getInstance();
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice dev = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.i("Bluetooth", "findDev " + dev.getName() + " MAC:"  + dev.getAddress());
				try{
				if ( dev != null && dev.getName().compareTo(sp.getString( SpUtil.TXN_TID, "") ) == 0) {
					Utils.bluetoothAddr = dev.getAddress();
					btDevice = dev;
					Log.i("Bluetooth", "pair " + dev.getName() + " MAC:"  + dev.getAddress() + Utils.bluetoothPsw );
					if (pair(dev.getAddress(), Utils.bluetoothPsw)) {
						start();
					}
				}
				}
				catch( NullPointerException e )
				{
				}
			}
		}
	};

	static public void findDev() {
		// Register the BroadcastReceiver
//		Log.i("Bluetooth", "findDev ");
//		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//		Utils.maintab.registerReceiver(mReceiver, filter);
//		bReceiver = true;
//		adapter.startDiscovery();
	}

	static public boolean pair(String strAddr, String strPsw) {
		boolean result = false;

		if (null == btAdapter) {
			return result;
		}

		btAdapter.cancelDiscovery();

		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
		}

		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
			Log.i("Bluetooth", "devAdd un effient!");
		}

		BluetoothDevice device = btAdapter.getRemoteDevice(strAddr);

		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.i("Bluetooth", "NOT BOND_BONDED");
				ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
				ClsUtils.createBond(device.getClass(), device);
				btDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("Bluetooth", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.i("Bluetooth", "HAS BOND_BONDED");
			try {
				ClsUtils.createBond(device.getClass(), device);
				ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
				ClsUtils.createBond(device.getClass(), device);
				btDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Bluetooth", "setPiN failed!");
				e.printStackTrace();
			}
		}
		return result;
	}

	static public class BlueboothThread extends Thread {
		private String msg = null;
		private int seq;
		private Context context;
		private Handler hand;
		private int count = 10;
		private int loop = 0;
		private boolean retry = false;

		public BlueboothThread(Handler h, String m, int s, Context c) {
			hand = h;
			msg = m;
			context = c;
			seq = s;
		}

		public void run() {
			switch (execute()) {
			case 0:
				break;
			case -1: {
				Utils.response( 1, seq, R.string.exception_read_failed, hand );
				break;
			}
			case -2: {
				Utils.response( 1, seq, R.string.excepton_con_bluetooth_error, hand );
				break;
			}
			case -3: {
				Utils.response( 10000, seq, R.string.exception_read_timeout, hand );
				break;
			}
			case -5:{
				if( retry == false ){
					retry = true;
					run();
				}else {
					Utils.response( 1, seq, R.string.exception_read_timeout, hand );
				}
				break;
			}
			default:
				Utils.response( 1, seq, R.string.exception_system, hand );
				break;
			}
//			close();
		}

		public int execute() {
			int res;

//			if (null == btDevice) {
//				initConn(handler);
//				return -2;
//			}

			if (null == btDevice) {
				return -2;
			}
			if (!connect()) {
				return -2;
			}
			if (!sendMsg()) {
				return -2;
			}

			res = readMsg();
			return res;
		}

		public boolean connect() {
			if (null != client) {
				Log.i("Bluetooth", "connect finish");
				return true;
			}

			try {
//				Method m = btDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
//				client = (BluetoothSocket) m.invoke(btDevice, 1);
				client = btDevice.createRfcommSocketToServiceRecord(myUUID);
				if (null != client) {
					client.connect();
					if( null != client.getOutputStream() && null != client.getInputStream() ) {
						sockOutStream = new PrintWriter(client.getOutputStream(), true);
//						sockInStream = new InputStreamReader( client.getInputStream(), "UTF-8");
						
						inStream = client.getInputStream();
						Thread.sleep(20);
						sockOutStream.print( "SIMCOMSPPFORAPP" );
						Thread.sleep(50);
						sockOutStream.flush();
					} else {
						return false;
					}
				}
//			} catch (NoSuchMethodException e) {
//				return false;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
			} catch( NullPointerException e ){
				e.printStackTrace();
				return false;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("Bluetooth", "connect finish");

			return true;
		}

		// 发送消息
		public boolean sendMsg() {
			if (null == sockOutStream) {
				return false;
			}
			sockOutStream.print(msg);
			sockOutStream.flush();
			Log.i("Bluetooth", "sendMsg");
			return true;
		}

		// 读取消息
		public int readMsg() {
			if (null == inStream) {
				return -1;
			}

			byte buf[] = new byte[256];
			String txt;
			String recv;
			int rd = 0;
			int i = 0;
			while( true ){
				try {
					int available;
					while((available = inStream.available()) == 0 && loop < count) {
						loop++;
					    // throws interrupted exception
					    Thread.sleep(200);
					}
					if( available > 0 ){
						available = inStream.read(buf);
						recv = new String( buf );
						int start = recv.indexOf( "(" );
						if( start >= 0 ){
							recv = recv.substring( start );
							int end = recv.indexOf( ")" );
							if( end > 0 )
								recv = recv.substring( 0, end + 1 );
							else
								recv = "";
						} else {
							recv = "";
						}
						if (recv.length() > 0) {
							Log.i("Bluetooth", recv );
							if( msg.substring(0, 26).compareTo( recv.substring(0, 26) ) == 0 ) {
								Utils.response(0, seq, recv, hand );
								break;
							}
						}
					}
//					if( available > 0 ){
//						for (i = 0; i < available; i++) {
//							rd += sockInStream.read(buf, rd, 1);
////							buf2[buf2.length] = buf[rd - 1];
//							Log.i("aaaa", "" + buf[rd - 1] );
//							if( buf[rd - 1] != '(' && rd == 1 ) {
//								rd = 0;
//							}
//							if (buf[rd-1] == ')') {
//								available = -1;
//								break;
//							}
//						}
//						if( -1 == available ){
//							txt = new String( buf, 0, rd );
//							Log.i("aaaa", buf2.toString());
//							if( msg.substring(0, 26).compareTo( txt.substring(0, 26) ) == 0 ) {
//								Utils.response(0, seq, txt, hand );
//								break;
//							}
//						}
//					}
					if( loop == count ){
						btDevice = null;
						close();
						return -5;
					}
//					Log.i("Bluetooth", "readMsg " + String.valueOf(buf, 0, rd));
				} catch (SocketTimeoutException e) {
					Log.i("Bluetooth", "readMsg timeout ");
					return 1;
				} catch (SocketException e) {
					Log.i("Bluetooth", "readMsg SocketException ");
					return -1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					close();
					btDevice = null;
					return -5;
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("Bluetooth", "readMsg timeout " + e.toString());
					return -1;
				}
			}
//			return 0;
			return 0;
		}

		public void close() {
			try {
				if (null != client) {
					client.close();
					client = null;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
}
