package com.yanbao.util;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class HttpsUtil {

    /**
     * 发起GET请求并获取结果
     *
     * @param requestUrl 请求地址
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject doGetRequest(String requestUrl) {
        return httpRequest(requestUrl, "GET", null);
    }

    /**
     * 发起POST请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param outputStr  提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject doPostRequest(String requestUrl, String outputStr) {
        return httpRequest(requestUrl, "POST", outputStr);
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                conn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            System.out.println("Server connection timed out.");
        } catch (Exception e) {
            System.out.println("Https request error.");
        }
        return jsonObject;
    }

    public  static String getXmlFromRequest(HttpServletRequest request) {
        String xml;
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((xml = in.readLine()) != null) {
                sb.append(xml);
            }
            in.close();
            inputStream.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
