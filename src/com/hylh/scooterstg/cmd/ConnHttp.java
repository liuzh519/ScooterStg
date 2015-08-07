package com.hylh.scooterstg.cmd;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.utils.ErrUtils;
import com.hylh.scooterstg.utils.Utils;

import android.os.Handler;
import android.util.Log;

public class ConnHttp {
    private static final int CONNECTION_TIMEOUT = 10000;

	public static void sendHttpGet(Handler handle, List<BasicNameValuePair> param, String uri, int seq) {
		HttpGetThread tcp = new HttpGetThread(handle, param, uri, seq);
		tcp.start();
	}

	static public class HttpGetThread extends Thread {
		public Handler _h;
		public String  _uri;
		public List<BasicNameValuePair> _param;
		public int _seq;

		public HttpGetThread( Handler h, List<BasicNameValuePair> param, String uri, int seq ) {
			_seq = seq;
			_h = h;
			_uri = uri;
			_param = param;
		}

		public void run() {
			String param = URLEncodedUtils.format(_param, "UTF-8");  
			doHttpGet(_uri + "?" + param);
		}

	    public void doHttpGet(String uri) {
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpClient hc = new DefaultHttpClient();
	        HttpGet get = new HttpGet(uri);
	        get.addHeader("Content-Type", "text/xml");
	        get.setParams(httpParameters);
	        HttpResponse response = null;
	        try {
				response = hc.execute(get);
				onResponse(response, _uri, _seq, _h);
			} catch (ClientProtocolException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_system, _h );
			} catch (UnknownHostException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			} catch (IOException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			}  catch (Exception e) {
				e.printStackTrace();
				Utils.response( -2 , _seq, R.string.exception_read_timeout, _h );
			}
	    }
	}

	public static void sendHttpsGet(Handler handle, List<BasicNameValuePair> param, String uri, int seq) {
		HttpsGetThread tcp = new HttpsGetThread(handle, param, uri, seq);
		tcp.start();
	}

	static public class HttpsGetThread extends Thread {
		public Handler _h;
		public String  _uri;
		public List<BasicNameValuePair> _param;
		public int _seq;

		public HttpsGetThread( Handler h, List<BasicNameValuePair> param, String uri, int seq ) {
			_seq = seq;
			_h = h;
			_uri = uri;
			_param = param;
		}

		public void run() {
			String param = URLEncodedUtils.format(_param, "UTF-8");  
			doHttpsGet(_uri + "?" + param);
		}
	    public void doHttpsGet(String uri){
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpClient hc = initHttpClient(httpParameters);
	        HttpGet get = new HttpGet(uri);
	        get.addHeader("Content-Type", "text/xml");
	        get.setParams(httpParameters);
	        HttpResponse response = null;
//	        Log.i("httpsget", uri);
	        try {
				response = hc.execute(get);
				onResponse(response, _uri, _seq, _h);
			} catch (ClientProtocolException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_system, _h );
			} catch (UnknownHostException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			} catch (IOException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			}  catch (Exception e) {
				e.printStackTrace();
				Utils.response( -2 , _seq, R.string.exception_read_timeout, _h );
			}
	    }
	}

	public static void sendHttpPost(Handler handle, List<BasicNameValuePair> param, String uri, int seq) {
		HttpPostThread tcp = new HttpPostThread(handle, param, uri, seq);
		tcp.start();
	}

	static public class HttpPostThread extends Thread {
		public Handler _h;
		public String  _uri;
		public List<BasicNameValuePair> _param;
		public int _seq;

		public HttpPostThread( Handler h, List<BasicNameValuePair> param, String uri, int seq ) {
			_seq = seq;
			_h = h;
			_uri = uri;
			_param = param;
		}

		public void run() {
			String param = URLEncodedUtils.format(_param, "UTF-8");  
			doHttpPost(_uri + "?" + param);
		}
		
	    public void doHttpPost(String uri) {
//	        Log.d("doHttpPost", "serverURL="+uri);
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
	        HttpClient hc = new DefaultHttpClient();
	        HttpPost post = new HttpPost(uri);
	        HttpResponse response = null;
//	        Log.i( "http post", uri );
	        try {
	            post.addHeader("Content-Type", "text/xml");
//	            post.setEntity(new StringEntity(xmlString, "UTF-8"));
	            post.setParams(httpParameters);
				response = hc.execute(post);
				onResponse(response, _uri, _seq, _h);
			} catch (ClientProtocolException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_system, _h );
			} catch (UnknownHostException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			} catch (IOException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			}  catch (Exception e) {
				e.printStackTrace();
				Utils.response( -2 , _seq, R.string.exception_read_timeout, _h );
			}
	    }
	}


	public static void sendHttpsPost(Handler handle, List<BasicNameValuePair> param, String uri, int seq) {
		HttpsPostThread tcp = new HttpsPostThread(handle, param, uri, seq);
		tcp.start();
	}

	static public class HttpsPostThread extends Thread {
		public Handler _h;
		public String  _uri;
		public List<BasicNameValuePair> _param;
		public int _seq;

		public HttpsPostThread( Handler h, List<BasicNameValuePair> param, String uri, int seq ) {
			_seq = seq;
			_h = h;
			_uri = uri;
			_param = param;
		}

		public void run() {
			String param = URLEncodedUtils.format(_param, "UTF-8");  
			doHttpsPost(_uri + "?" + param);
		}
		
	    public void doHttpsPost(String uri)  {
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpClient hc = initHttpClient(httpParameters);
	        HttpPost post = new HttpPost(uri);
	        post.setParams(httpParameters);
	        HttpResponse response = null;
	        try {
	            post.addHeader("Content-Type", "text/xml");
	//            post.setEntity(new StringEntity(xmlString, "UTF-8"));
	            post.setParams(httpParameters);
				response = hc.execute(post);
				onResponse(response, _uri, _seq, _h);
			} catch (ClientProtocolException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_system, _h );
			} catch (UnknownHostException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			} catch (IOException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			}  catch (Exception e) {
				e.printStackTrace();
				Utils.response( -2 , _seq, R.string.exception_read_timeout, _h );
			}
	    }
	}


	public static void sendHttpsPut(Handler handle, List<BasicNameValuePair> param, String uri, int seq) {
		HttpsPutThread tcp = new HttpsPutThread(handle, param, uri, seq);
		tcp.start();
	}

	static public class HttpsPutThread extends Thread {
		public Handler _h;
		public String  _uri;
		public List<BasicNameValuePair> _param;
		public int _seq;

		public HttpsPutThread( Handler h, List<BasicNameValuePair> param, String uri, int seq ) {
			_seq = seq;
			_h = h;
			_uri = uri;
			_param = param;
		}

		public void run() {
			String param = URLEncodedUtils.format(_param, "UTF-8");  
			doHttpsPut(_uri + "?" + param);
		}
		
	    public void doHttpsPut(String uri)  {
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpClient hc = initHttpClient(httpParameters);
	        HttpPut put = new HttpPut(uri);
	        put.setParams(httpParameters);
	        HttpResponse response = null;
	        try {
	            put.addHeader("Content-Type", "text/xml");
	//            put.setEntity(new StringEntity(xmlString, "UTF-8"));
	            put.setParams(httpParameters);
				response = hc.execute(put);
				onResponse(response, _uri, _seq, _h);
			} catch (ClientProtocolException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_system, _h );
			} catch (UnknownHostException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			} catch (IOException e) {
			    e.printStackTrace();  
				Utils.response( -2 , _seq, R.string.exception_network, _h );
			}  catch (Exception e) {
				e.printStackTrace();
				Utils.response( -2 , _seq, R.string.exception_read_timeout, _h );
			}
	    }
	}

    public static HttpClient initHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[] {
                tm
            }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


	public static void onResponse(HttpResponse response, String uri, int seq, Handler h){
		int statecode = 900;
		String msg;

		try {
	    	msg = EntityUtils.toString(response.getEntity(), "utf-8");
		    statecode = response.getStatusLine().getStatusCode();
//		    Log.i("http", msg);
		    
		    if( statecode >= 200 && statecode < 300 )  {
				JSONTokener jsonParser = new JSONTokener( msg );
				JSONObject json = (JSONObject) jsonParser.nextValue();
				try{
					if( json.has( "r" ) ){
						if( json.getString( "r" ).compareTo( "1000" ) == 0 
								|| json.getString( "r" ).compareTo( "1100" ) == 0 ){
							Utils.response( 0 , seq, json, h );
						} else {
							Utils.response( -10 , seq, json.getString("r"), h );
						}
					}
					else{
						Utils.response( -2 , seq, R.string.exception_invalid_response, h );
					}
				} catch ( Exception e ){
					Utils.response( -2, seq, R.string.exception_invalid_response, h );
				}
		    }
		    else {
				Utils.response( -1, statecode, getHttpCode(statecode), h );  
		    }

		} catch (ClientProtocolException e) {
		    e.printStackTrace();  
			Utils.response( -2, seq, R.string.exception_system, h );
		}  catch (Exception e) {
			Utils.response( -2, seq, R.string.exception_invalid_response, h );
		}
	}
	
	static String getHttpCode( int code ){
		return "server no response : " + code;
	}
//	static String getSrvCode(String uri, String code ){
//		return ErrUtils.get(uri + code);
//	}
}
