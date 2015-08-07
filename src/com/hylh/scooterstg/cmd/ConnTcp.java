package com.hylh.scooterstg.cmd;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnTcp {

	static public void Execute(Handler h, String m, int port, int seq) {

		TcpThread tcp = new TcpThread(h, m, port, seq, 10000);
		tcp.start();
	}

	static public JSONObject sucJson() {
		try {
			JSONObject json = new JSONObject();
			JSONObject res = new JSONObject();
			res.put("code", "000000");
			res.put("msg", "");
			json.put("result", res);
			return json;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	static public class TcpThread extends Thread {
		private Socket client;
		private InputStream sockInStream;
		private PrintWriter sockOutStream;
		private String msg = null;
		private Handler hand;
		private int port;
		private int timeout;
		private int _seq;

		public TcpThread(Handler h, String m, int port_, int seq, int to ) {
			hand = h;
			msg = m;
			port = port_;
			timeout = to;
			_seq = seq;
		}


		public void response(int arg1, Object obj, Handler h) {
			if (null != h) {
				Message m = new Message();
				m.obj = obj;
				m.arg1 = arg1;
				m.arg2 = _seq;
				h.sendMessage(m);
			}
		}

		public void run() {
			Utils.checkDomain();
			switch (execute()) {
			case 1: {
				response( 1, R.string.exception_read_timeout, hand);
				break;
			}
			case -1: {
				response( 1, R.string.exception_read_failed, hand);
				break;
			}
			case -2: {
				response( 1,  R.string.exception_network, hand);
				break;
			}
			case -3: {
				response( 1,  R.string.exception_network, hand);
				break;
			}
			default:
			}
		}

		public int execute() {
			if (!connect()) {
				return -2;
			}

			if (sendMsg()) {
			}
			if( !client.isConnected() ){
				return -3;
			}

			return readMsg();
		}

		public boolean connect() {
			boolean bRet = false;
			try {
				InetAddress ip = InetAddress.getByName(Utils.conurl);
				InetSocketAddress isa = new InetSocketAddress(ip, port);

				client = new Socket();
				client.connect(isa);

				sockOutStream = new PrintWriter(client.getOutputStream(), true);
				sockInStream = client.getInputStream();
				client.setSoTimeout( timeout );

				bRet = true;
				Log.i("Tcp", "connect success");
			} catch (Exception e) {
				Log.i("Tcp", "connect faild");
			}

			return bRet;
		}

		public void process(String txt) {
			response(0, txt, hand);
		}

		// 发送消息
		public boolean sendMsg() {
			sockOutStream.print(String.format("%08d", msg.length()));
			sockOutStream.print(msg);
			sockOutStream.flush();
			return true;
		}

		// 读取消息
		public int readMsg() {
			String rtx = "";
			String len;
			byte buf[] = new byte[512];
			int rd;
			int body;
			try {
				rd = sockInStream.read(buf, 0, 8);
				if (rd == 8) {
					len = new String(buf, 0, 8);
					body = Integer.parseInt(len);
					Log.i("Tcp", "readMsg body " + body);
					while (body > 0) {
							rd = sockInStream.read(buf, 0, 512);
							rtx += new String(buf, 0, rd);
							body -= rd;
						}
					Log.i("Tcp", "readMsg :" + body );
					close();
					process(rtx);
				} else if (rd < 0) {
					Log.i("Tcp", "readMsg close ");
					return -2;
				}
			} catch (SocketTimeoutException e) {
				Log.i("Tcp", "readMsg timeout ");
				return 1;
			} catch (SocketException e) {
				Log.i("Tcp", "readMsg SocketException ");
				return -1;
			} catch (Exception e) {
				Log.i("Tcp", "readMsg timeout " + e.toString());
				return -1;
			}

			return 0;
		}

		public void close() {
			try {
				client.close();
			} catch (Exception e) {
			}
		}
	}
}
