package com.hylh.scooterstg.activity;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hylh.scooterstg.R;
import com.hylh.scooterstg.cmd.Command;
import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.SpUtil;
import com.hylh.scooterstg.utils.Utils;
import com.hylh.scooterstg.view.TitleBarView;

public class WebkitActivity extends Activity {
	public WebView webview;
	private TitleBarView mTitleBarView;
	private String mMode;

	//added by ycf on 20150725 begin
	private Context mContext;
	private RelativeLayout bottomLayout;
	private Button bottomBtn;
	private RelativeLayout mgLayout; 
	private Button btnMgOk;
	private String tid,num;
	//added by ycf on 20150725 end
	
	Handler mQuery = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if( msg.arg1 == 0 ){
				SpUtil.getInstance().setLegal(false);
				showDialog(getResources().getString( R.string.result_title_suc), getResources().getString( R.string.result_success), R.drawable.icon_error);
			}else if( msg.arg1 == -1 ){
				//modify by ycf on 20150725 begin
				mgLayout.setVisibility(View.VISIBLE);
//				showDialog("Error", (String)msg.obj, R.drawable.icon_error);
				//modify by ycf on 20150725 end
				
			}else if( msg.arg1 == -2 ){
				showDialog("Error", getResources().getString( (int)msg.obj ), R.drawable.icon_error);
			}else if( msg.arg1 == -10 ){
				String code = (String)msg.obj;
				if( code.compareTo("1002") == 0 ){
					Utils.login(WebkitActivity.this);
				} else {//modify by ycf on 20150725 begin
					//showDialog("Error", ErrUtils.getRental( (String)msg.obj ), R.drawable.icon_error);
					mgLayout.setVisibility(View.VISIBLE);//modify by ycf on 20150725 end
				}
			}else{
				showDialog(getResources().getString( R.string.error), getResources().getString( R.string.exception_unknown), R.drawable.icon_error);
			}

			Utils.dismissProcess();
		}
	};
	
	public void showDialog(String title, String txt, int icon){
		AlertDialog aldlg = new AlertDialog.Builder(WebkitActivity.this)
		.setTitle(title)
		.setMessage(txt)
		.setIcon(icon)
		.setPositiveButton(
				getResources().getString(R.string.result_btn),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
							finish();
					}
				}).create();
		aldlg.setCancelable(false);
		
		aldlg.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webkit);
		mContext = this;
		findView();
		initView();
		init();
	}
	
	private void findView(){
		webview = (WebView) findViewById(R.id.webView1);
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		//added by ycf on 20150725 begin
		mgLayout = (RelativeLayout)findViewById(R.id.ud_mg_rlayout);
		btnMgOk = (Button)findViewById(R.id.btn_mg_ok);
		bottomLayout = (RelativeLayout)findViewById(R.id.bottom_rlayout);
		bottomBtn = (Button)findViewById(R.id.bottom_btn);
		//added by ycf on 20150725 end
	}
	
	private void initView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( mMode.compareTo("legal") == 0 ) {
					SpUtil.getInstance().setLegal(false);
//					finish();

					SpUtil sp = SpUtil.getInstance();
					
		        	Utils.showProcess(WebkitActivity.this);
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
					params.add(new BasicNameValuePair("key", SpUtil.getInstance().getSTKey())); 
					params.add(new BasicNameValuePair("acknowledge", "user_agreement" ));  
					
					MyApplication.getInstance().getCmd().sendHttpsPut( Utils.urlAgreement, params, WebkitActivity.this, mQuery, Command.MODE_SILENT);
				}else if("rent".equals(mMode)){//added by ycf on 20150811 begin
					
					Intent intent=new Intent(mContext, BindConfirmActivity.class);
					intent.putExtra("tid", tid);
					intent.putExtra("num", num);
					intent.putExtra("action","cancle");
					startActivity(intent);
					
				}else {//added by ycf on 20150811 end
					finish();
				}
			}
		});
		
		//added by ycf on 20150725 begin
		btnMgOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mgLayout.setVisibility(View.GONE);
			}
			
		});
		
		bottomBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, BindConfirmActivity.class);
				
				intent.putExtra("tid", tid);
				intent.putExtra("num", num);
				intent.putExtra("action","accept");
				startActivity(intent);
			}
		});
		
		//added by ycf on 20150725 end
	}

	@SuppressLint("JavascriptInterface")
	private void init(){
		Intent intent = this.getIntent();
		String url = intent.getStringExtra( "url" );
		int title = intent.getIntExtra("title", R.string.app_null);
		mMode = intent.getStringExtra( "mode" );
		
		//added by ycf on 20150812 begin
		tid = intent.getStringExtra( "tid" );
		num = intent.getStringExtra( "num" );
		//added by ycf on 20150812 end
		
		if( mMode.compareTo( "full" ) == 0 ){
//			webview.setWebChromeClient( new WebChromeClient() );	
			webview.setWebViewClient(new MyWebViewClientClient());	
		} else {
			webview.setWebViewClient(new MyWebViewClientClient());
	        webview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj"); 	
		}
		
		if( mMode.compareTo("finish") == 0
			|| mMode.compareTo("legal") == 0 ){
			mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.done);
		}
		
		//added by ycf on 20150811 begin
		if("rent".equals(mMode)){
			mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.btn_cancel);
			bottomLayout.setVisibility(View.VISIBLE);
		}
		//added by ycf on 20150811 end
		mTitleBarView.setTitleText(title);
        
//		webview.setWebViewClient( new WebViewClient() );
		WebSettings webSettings = webview.getSettings();  
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setDefaultTextEncodingName("utf-8");  
		webSettings.setAllowFileAccess(true);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        webview.loadUrl(url);
	}
    /** 
     * ����ȫ�� 
     */  
    private void setFullScreen() {  
        // ����ȫ����������ԣ���ȡ��ǰ����Ļ״̬��Ȼ������ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }  
  
    /** 
     * �˳�ȫ�� 
     */  
    private void quitFullScreen() {
        // ������ǰ��Ļ״̬�Ĳ����ȡ  
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();  
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        getWindow().setAttributes(attrs);  
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
    }  

    private class DefaultWebChromeClient extends WebChromeClient {  
        CustomViewCallback customViewCallback;  
        @Override  
        public void onShowCustomView(View view, CustomViewCallback callback) {  
            customViewCallback = callback;  
            webview.setVisibility(View.GONE);  
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
            setFullScreen();  
        }  
        // �˳�ȫ����ʱ��  
        @Override  
        public void onHideCustomView() {  
            if (customViewCallback != null) {  
                customViewCallback.onCustomViewHidden();  
            }  
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);  
            quitFullScreen();  
            webview.setVisibility(View.VISIBLE);  
        }  
  
        @Override  
        public void onProgressChanged(WebView view, int newProgress) {  
            super.onProgressChanged(view, newProgress);  
        }
    }
    
    private class MyWebViewClientClient extends WebViewClient {
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {  
        }  
  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);  
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+"  
                    + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }
    }
    
    final class InJavaScriptLocalObj {
        public void showSource(String html) {
        	if( html.indexOf("r:1000") >= 0 ){
        		finish();
        	}
        }
        public void finish() {
        	finish();
        }
    }  
	
}
