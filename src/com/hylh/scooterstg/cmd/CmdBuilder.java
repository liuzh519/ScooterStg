package com.hylh.scooterstg.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;  
import org.xml.sax.SAXException;

import com.hylh.scooterstg.utils.MyApplication;
import com.hylh.scooterstg.utils.HmacSha1;
import com.hylh.scooterstg.utils.SpUtil;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class CmdBuilder {
	private Element doc = null;
	private MyApplication app;
	private SpUtil sp;
	private static int retry = 0;
	
	public CmdBuilder(MyApplication a){
		app = a;
		sp = SpUtil.getInstance();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try {
		    AssetManager mgr = app.getAssets();
		    String name = "cmd.xml";
	        InputStream inputStream = mgr.open(name,AssetManager.ACCESS_BUFFER);
			builder = factory.newDocumentBuilder();
	        Document document = builder.parse(inputStream);  
	        doc = document.getDocumentElement(); 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String httpString( String trcd, JSONObject keys){
		DateFormat dfdt =	new SimpleDateFormat("yyyyMMDD");
		DateFormat dfdt2 =	new SimpleDateFormat("yyMMDD");
		DateFormat dftm =	new SimpleDateFormat("hhmmss");
		Date	dt = new Date();
		Node nd = cmdNode( trcd );
		String cmd = null;
		
		if( nd != null ) {
	        try {
				JSONObject syshead = new JSONObject();
				JSONObject apphead = new JSONObject();
				JSONObject pack = new JSONObject();
				String ssn = sp.getString( SpUtil.TXN_TID ) + dfdt2.format( dt ) + dftm.format( dt );
				String tid = null;
				if( null != keys )
					tid = keys.getString( "tid" );
				if( null == tid )
					tid = sp.getTid();

				syshead.put( "ver", "1.0" );
				syshead.put( "txncode", trcd );
				syshead.put( "consumer_id", "281010" );
				syshead.put( "consumer_ssn", ssn );
				syshead.put( "trans_date", dfdt.format( dt ) );
				syshead.put( "trans_time", dftm.format( dt ) );

				apphead.put( "lang", "zh-CN" );
				apphead.put( "chnl", "ads" );
				apphead.put( "ssk", sp.getSSK() );
				apphead.put( "tid", tid );
				apphead.put( "uid", sp.getUid() );
				apphead.put( "phone", sp.getPhoneMask() );
	//			keys.put( "dev_id", "d95a585a9452b6cd00b266fc8ffbca8e12b96945557642cc352ded2c9a1d5371" );

				pack.put( "app_head", apphead );
				pack.put( "sys_head", syshead );
				pack.put( "body", keys );

				cmd = pack.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cmd;
	}
	public String httpString2( String trcd, JSONObject keys) {
		String cmd = null;

			try {
				DateFormat dfdt =	new SimpleDateFormat("yyyyMMDD");
				DateFormat dfdt2 =	new SimpleDateFormat("yyMMDD");
				DateFormat dftm =	new SimpleDateFormat("hhmmss");
				Date	dt = new Date();
				JSONObject syshead = new JSONObject();
				JSONObject apphead = new JSONObject();
				JSONObject pack = new JSONObject();
				String ssn = sp.getString( SpUtil.TXN_TID ) + dfdt2.format( dt ) + dftm.format( dt );
				
				syshead.put( "ver", "1.0" );
				syshead.put( "code", trcd );
				
				syshead.put( "consumer_id", "281010" );
				syshead.put( "consumer_ssn", ssn );
				syshead.put( "trans_date", dfdt.format( dt ) );
				syshead.put( "trans_time", dftm.format( dt ) );
	
				apphead.put( "lang", "zh-CN" );
				apphead.put( "chnl", "ads" );
				apphead.put( "phone", keys.getString( "phone" ) );

				pack.put( "app_head", apphead );
				pack.put( "sys_head", syshead );
				pack.put( "body", keys );
	
				cmd = pack.toString();
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch( StringIndexOutOfBoundsException e ){
				e.printStackTrace();
			}
		
		return cmd;
	}
	
	public String tcpString( String trcd, JSONObject keys) {
		String cmd = null;
		Node nd = cmdNode( trcd );

		if( nd != null ) {
			try {
				DateFormat dfdt =	new SimpleDateFormat("yyyyMMDD");
				DateFormat dfdt2 =	new SimpleDateFormat("yyMMDD");
				DateFormat dftm =	new SimpleDateFormat("hhmmss");
				Date	dt = new Date();
				JSONObject syshead = new JSONObject();
				JSONObject apphead = new JSONObject();
				JSONObject pack = new JSONObject();
				String ssn = sp.getString( SpUtil.TXN_TID ) + dfdt2.format( dt ) + dftm.format( dt );
				String txt;
				String tid;
				if( null != keys && keys.has( "tid") ){
					tid = keys.getString("tid" );
				}else{
					tid = sp.getTid();
				}
				if( null == tid )
					tid = sp.getTid();
				
				syshead.put( "ver", "1.0" );
				syshead.put( "code", trcd );
				syshead.put( "consumer_id", "281010" );
				syshead.put( "consumer_ssn", ssn );
				syshead.put( "trans_date", dfdt.format( dt ) );
				syshead.put( "trans_time", dftm.format( dt ) );
	
				apphead.put( "lang", "zh-CN" );
				apphead.put( "chnl", "ads" );
				apphead.put( "tid", tid );
				apphead.put( "biz_ssn", ssn );
				apphead.put( "ssk", sp.getSSK() );
				apphead.put( "uid", sp.getUid() );
				apphead.put( "phone", sp.getPhoneMask() );
//
//		        for(Map.Entry<String, String> entry : keys.entrySet()) {
//					if( entry.getValue() != null )
//						body.put(entry.getKey(), entry.getValue());
//				}
		        
				pack.put( "app_head", apphead );
				pack.put( "sys_head", syshead );
				pack.put( "body", keys );
	
				txt = pack.toString();
				String mac = HmacSha1.hmac_sha1( sp.getTxnkey(), txt ).toUpperCase();
				
				if( mac.length() == 40 ) {
					cmd = txt + mac.substring( 0, 8 );
				}
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch( StringIndexOutOfBoundsException e ){
				e.printStackTrace();
			}
		}
		
		return cmd;
	}
	
	@SuppressWarnings("null")
	public String smsString( String trcd, JSONObject keys){
		String cmd = null;
		NamedNodeMap attrs = null;
		Node cn = cmdNode( trcd );
		Node rn = reqNode( cn );
		Node nd;
		Node attr = null;
		String val;
		boolean selected = false;

		if( cn != null && rn != null ) {
			attrs = cn.getAttributes();
			String txn = attrs.getNamedItem("trcd").getNodeValue();
			cmd = String.format("(%s,2,%s,%08d,", sp.getString( SpUtil.TXN_TID ), txn, sp.getSeq( retry ) );
			
			NodeList nodelist = rn.getChildNodes();  
	        int size = nodelist.getLength();
	        for (int i = 0; i < size; i++) {
	        	nd = nodelist.item( i );
				if( nd.getNodeType() == Node.ELEMENT_NODE ){
		        	attrs = nd.getAttributes();
		        	try {
		        		//	field
		        		String tag = nd.getNodeName();
		        		if( tag == null ){
		        			continue;
		        		}
			        	if( tag.compareTo( "field" ) == 0 ){
					        	attr = attrs.getNamedItem( "value" );
					        	if( attr != null ){
					        		val = attr.getNodeValue();
					        	} else {
						        	attr = attrs.getNamedItem( "id" );
						        	if( attr == null ){
						        		return null;
						        	}
						        	String key = attr.getNodeValue();
						        	if( keys.has( key ) ){
						        		val = keys.getString( key );
						        	} else {
						        		return null;
						        	}
					        	}
					        	if( val.length() == 0 ) { 
					        		return null;
					        	}
					        	cmd += val + ",";
			        	} //	Choose 
			        	else if( tag.compareTo( "choose" ) == 0 ){
				        	Node ntest = attrs.getNamedItem( "test" );
			    			String test = keys.getString( ntest.getNodeValue() );
				        	if( test != null && test.length() > 0 ){
				    			NodeList caselist = nd.getChildNodes();  
				    			NamedNodeMap cattrs = null;
				    			Node cnd;
				    			Node cattr;
				    	        int csi = caselist.getLength();
				    	        for (int j = 0; j < csi; j++) {
				    	        	cnd = caselist.item( j );
				    				if( cnd.getNodeType() == Node.ELEMENT_NODE 
				    						&& cnd.getNodeName().compareTo( "case" ) == 0 ){
				    					cattrs = cnd.getAttributes();
							        	cattr = cattrs.getNamedItem( "value" );
							        	if( cattr == null ){
							        		continue;
							        	}
							        	String cval = cattr.getNodeValue();
							        	if( cval != null
							        			&& cval.compareTo( test ) == 0 ){
							        		selected = true;
							    			NodeList flist = cnd.getChildNodes(); 
							    			NamedNodeMap fattrs;
							    			Node fnd, fattr;
							    	        int fsize = flist.getLength();
							    	        for (int k = 0; k < fsize; k++) {
							    	        	fnd = flist.item( k );
							    				if( fnd.getNodeType() == Node.ELEMENT_NODE ){
							    		        	fattrs = fnd.getAttributes();
										        	if( fnd.getNodeName().compareTo( "field" ) == 0 ){
											        	fattr = fattrs.getNamedItem( "value" );
											        	if( fattr != null ){
											        		val = fattr.getNodeValue();
											        	} else {
											        		fattr = fattrs.getNamedItem( "id" );
												        	if( fattr == null ){
												        		return null;
												        	}
												        	String key = fattr.getNodeValue();
												        	if( keys.has( key ) ){
												        		val = keys.getString( key );
												        	} else {
												        		return null;
												        	}
											        	}
											        	if( val.length() == 0 ) { 
											        		return null;
											        	}
											        	cmd += val + ",";
										        	} //	Choose 
							    				}
							    	        }
							        		
							        	}
				    				}
				    	        }
				    	        if( !selected ){
				    	        	cmd += test + ",";
				    	        }
				    	        selected = false;
				        	}
			        		
			        	}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
	        if( cmd != null && cmd.length() > 0 ){
	        	cmd += sp.getPhoneMask() + ",FFFFFFFF)";
	        }
		}
		return cmd;
	}
	
	@SuppressWarnings("null")
	public String btString( String trcd, JSONObject keys){
		String cmd = null;
		NamedNodeMap attrs = null;
		Node cn = cmdNode( trcd );
		Node rn = reqNode( cn );
		Node nd;
		Node attr = null;
		String val;
		boolean selected = false;

		if( cn != null && rn != null ) {
			attrs = cn.getAttributes();
			String txn = attrs.getNamedItem("trcd").getNodeValue();
			cmd = String.format("(%s,2,%s,%08d,", sp.getTid(), txn, sp.getSeq( retry ) );
			
			NodeList nodelist = rn.getChildNodes();  
	        int size = nodelist.getLength();
	        for (int i = 0; i < size; i++) {
	        	nd = nodelist.item( i );
				if( nd.getNodeType() == Node.ELEMENT_NODE ){
		        	attrs = nd.getAttributes();
		        	try {
		        		//	field
		        		String tag = nd.getNodeName();
		        		if( tag == null ){
		        			continue;
		        		}
			        	if( tag.compareTo( "field" ) == 0 ){
					        	attr = attrs.getNamedItem( "value" );
					        	if( attr != null ){
					        		val = attr.getNodeValue();
					        	} else {
						        	attr = attrs.getNamedItem( "id" );
						        	if( attr == null ){
						        		return null;
						        	}
						        	String key = attr.getNodeValue();
						        	if( keys.has( key ) ){
						        		val = keys.getString( key );
						        	} else {
						        		return null;
						        	}
					        	}
					        	if( val.length() == 0 ) { 
					        		return null;
					        	}
					        	cmd += val + ",";
			        	} //	Choose 
			        	else if( tag.compareTo( "choose" ) == 0 ){
				        	Node ntest = attrs.getNamedItem( "test" );
			    			String test = keys.getString( ntest.getNodeValue() );
				        	if( test != null && test.length() > 0 ){
				    			NodeList caselist = nd.getChildNodes();  
				    			NamedNodeMap cattrs = null;
				    			Node cnd;
				    			Node cattr;
				    	        int csi = caselist.getLength();
				    	        for (int j = 0; j < csi; j++) {
				    	        	cnd = caselist.item( j );
				    				if( cnd.getNodeType() == Node.ELEMENT_NODE 
				    						&& cnd.getNodeName().compareTo( "case" ) == 0 ){
				    					cattrs = cnd.getAttributes();
							        	cattr = cattrs.getNamedItem( "value" );
							        	if( cattr == null ){
							        		continue;
							        	}
							        	String cval = cattr.getNodeValue();
							        	if( cval != null
							        			&& cval.compareTo( test ) == 0 ){
							        		selected = true;
							    			NodeList flist = cnd.getChildNodes(); 
							    			NamedNodeMap fattrs;
							    			Node fnd, fattr;
							    	        int fsize = flist.getLength();
							    	        for (int k = 0; k < fsize; k++) {
							    	        	fnd = flist.item( k );
							    				if( fnd.getNodeType() == Node.ELEMENT_NODE ){
							    		        	fattrs = fnd.getAttributes();
										        	if( fnd.getNodeName().compareTo( "field" ) == 0 ){
											        	fattr = fattrs.getNamedItem( "value" );
											        	if( fattr != null ){
											        		val = fattr.getNodeValue();
											        	} else {
											        		fattr = fattrs.getNamedItem( "id" );
												        	if( fattr == null ){
												        		return null;
												        	}
												        	String key = fattr.getNodeValue();
												        	if( keys.has( key ) ){
												        		val = keys.getString( key );
												        	} else {
												        		return null;
												        	}
											        	}
											        	if( val.length() == 0 ) { 
											        		return null;
											        	}
											        	cmd += val + ",";
										        	} //	Choose 
							    				}
							    	        }
							        		
							        	}
				    				}
				    	        }
				    	        if( !selected ){
				    	        	cmd += test + ",";
				    	        }
				    	        selected = false;
				        	}
			        		
			        	}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
			return calcMac(cmd);
		}
		return null;
	}
	
	public boolean parserString( String trcd, JSONObject keys, String txt ){
		NamedNodeMap attrs = null;
		Node cn = cmdNode( trcd );
		Node rn = rspNode( cn );
		Node nd;
		Node attr = null;
		String rsp;
		String split = null;
		String[] lst;
		boolean end = false;
		int idx = 0;
		int pos;
		int i;
		if( txt.length() < 27 ){
			return false;
		} else if( txt.length() == 27 ){
			return true;
		}
		rsp = txt.substring(27, txt.length() - 1 );
//		lst = rsp.split( "," );
		String last = rsp.substring( rsp.length() - 1 );

		if( cn != null && rn != null ) {
			attrs = cn.getAttributes();
			
			NodeList nodelist = rn.getChildNodes();  
	        int size = nodelist.getLength();
	        for (i = 0; i < size; i++) { 
	        	nd = nodelist.item( i );
				if( nd.getNodeType() == Node.ELEMENT_NODE ){
		        	attrs = nd.getAttributes();
		        	try {
			        	attr = attrs.getNamedItem( "split" );
			        	split = null;
			        	if( attr != null ){
			        		split = attr.getNodeValue();
			        	} 
			        	if( split == null || split.length() == 0 ){
			        		split = ",";
			        	}
			        	attr = attrs.getNamedItem( "id" );
			        	if( attr == null ){
			        		return false;
			        	}
			        	if( rsp.length() > 0 ){
			        		pos = rsp.indexOf(split);
			        		if( pos == -1 ){
			        			pos = rsp.length();
			        		}
			        		String val = rsp.substring( 0, pos );
				        	keys.put( attr.getNodeValue(), val );
				        	if( rsp.length() > pos ){
				        		rsp = rsp.substring( pos + 1 );
				        	} else {
				        		rsp = "";
				        	}
			        	} else if( rsp.length() == 0 && end == false ){
				        	keys.put( attr.getNodeValue(), "" );
				        	rsp = "";
				        	end = true;
			        		
			        	} else {
			        		return false;
			        	}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
				}
	        }
		}
		return true;
	}
	
	public void resetRetry(){
		retry = 0;
	}
	
	public void saveBtkey(){
		sp.setBtKey( retry );
	}
	
	private String calcMac( String cmd ){
		String mac;

		String btkey = sp.getBtKey(retry);
		if( cmd.length() > 26 ){
			if( btkey.length() == 0 )
			{
				btkey = "1111111111111111";
			}

			long key = Long.parseLong( btkey, 16 ) + retry;
			btkey = Long.toString( key, 16 ).toLowerCase();
			mac = HmacSha1.hmac_sha1( btkey, cmd.substring( 1, 26 ) );
			retry ++;
			return cmd + sp.getPhoneMask() + "," + mac.substring( 0, 8 ) + ")";	
		}
		
		return null;
	}
	
	public Node cmdNode( String trcd ){
		NodeList nl = null;
		Node nd;
		NamedNodeMap attrs = null;
		int size;
		nl = doc.getElementsByTagName( "item" );
		if( nl == null ){
			return null;
		}
//		nd = nl.item( 0 );
//		
//		nl = nd.getChildNodes();
//		if( nl != null ){
//			return null;
//		}
		
		size = nl.getLength();
		for( int i = 0; i < size; i ++ ){
			nd = nl.item( i );
			attrs = nd.getAttributes();
			String name = attrs.getNamedItem( "id" ).getNodeValue();
			if( name.compareTo( trcd ) == 0 ){
				return nd;
			}
		}
		return null;
	}
	
	public Node reqNode( Node nd ){
		NodeList cnl;
		Node rnd;
		int size;
		if( nd == null )
			return null;
	
		cnl = nd.getChildNodes();
		size = cnl.getLength();
		for( int i = 0; i < size; i ++ ){
			rnd = cnl.item( i );
			if( rnd.getNodeType() == Node.ELEMENT_NODE ){
				String name = rnd.getNodeName(); 
				if( name.compareTo( "request" ) == 0 ){
					return rnd;
				}
			}
		}
		return null;
	}
	
	public Node rspNode( Node nd ){
		NodeList cnl;
		Node rnd;
		int size;
		if( nd == null )
			return null;
	
		cnl = nd.getChildNodes();
		size = cnl.getLength();
		for( int i = 0; i < size; i ++ ){
			rnd = cnl.item( i );
			if( rnd.getNodeType() == Node.ELEMENT_NODE ){
				String name = rnd.getNodeName(); 
				if( name.compareTo( "response" ) == 0 ){
					return rnd;
				}
			}
		}
		return null;
	}
	
}
