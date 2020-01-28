package com.pjy.nchu2.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetApiDataUtil {
    public static String getData(String apiUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            //连接
            URL url = new URL(apiUrl);//访问链接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            //
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);

            //将bufferReader的值给放到str里
            String str = null;
            while ((str = br.readLine()) != null) {
                buffer.append(str);
            }
            //关闭bufferReader和输入流
            br.close();
            isr.close();
            is.close();
            // inputStream = null;

            //断开连接
            httpURLConnection.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回字符串
        return buffer.toString();
    }


}
