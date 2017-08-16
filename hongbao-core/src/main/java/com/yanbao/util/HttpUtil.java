package com.yanbao.util;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

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
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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
            e.printStackTrace();
            System.out.println("Https request error.");
        }
        return jsonObject;
    }

    public String  getParamForPostOrGet(HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        String queryString = "";
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                queryString += key + "=" + value + "&";
            }
        }
        // 去掉最后一个空格
        queryString = queryString.substring(0, queryString.length() - 1);
        return  queryString;
    }

    public String  getParamForGet(HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        String queryString = request.getQueryString();
        return  queryString;
    }
}
