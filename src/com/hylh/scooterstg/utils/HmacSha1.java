package com.hylh.scooterstg.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha1 {

    public static String hmac_sha1(String key, String datas) {  
            String reString = "";  
      
            try  
            {
                byte[] data = key.getBytes("UTF-8");    
                //���ݸ������ֽ����鹹��һ����Կ,�ڶ�����ָ��һ����Կ�㷨������     
                SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");     
                //����һ��ָ�� Mac �㷨 �� Mac ����     
                Mac mac = Mac.getInstance("HmacSHA1");     
                //�ø�����Կ��ʼ�� Mac ����     
                mac.init(secretKey);
                            
                byte[] text = datas.getBytes("UTF-8");      
                //��� Mac ����      
                byte[] text1 = mac.doFinal(text);     
                         
                reString = Hex.encodeHexStr(text1);  
      
            } catch (Exception e)  
            {  
                // TODO: handle exception   
            }  
              
            return reString;  
        }  
}
