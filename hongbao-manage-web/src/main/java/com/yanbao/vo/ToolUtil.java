package com.yanbao.vo;

import java.util.Map;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ToolUtil {
	
	
	/***
	 * 判断传入的对象是否为空
	 *
	 * @param obj
	 *            待检查的对象
	 * @return 返回的布尔值, 为空或等于0时返回true
	 */
	public static boolean isEmpty(Object obj) {
		return checkObjectIsEmpty(obj, true);
	}
	
	/**
	 * java對象轉換成json
	 * 
	 * @param object  java  對象
	 * 
	 * @return
	 */
	 public static String objectToJson(Object object){
		 return  JSON.toJSON(object).toString();
	 }
	
	
	/***
	 * 通过jsonObjectString字符串  获取 java 对象
	 * 
	 * 
	 * @param jsonObjectString  json 字符串
	 * @param clazz
	 * @return
	 */
	
	public static Object getJavaObject(String jsonObjectString,Class<?> clazz){
		if (ToolUtil.isEmpty(jsonObjectString)) {
			return null;
		}
		
		return  JSON.parseObject(jsonObjectString,clazz);
	}


	/**
	 * 获取 JSONObject 实例
	 * 
	 * 
	 * @return
	 */
	public static JSONObject getJSONObject(){
	    return	new JSONObject();
		
	}
	
	private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
		if (null == obj)
			return bool;
		else if (obj == "")
			return bool;
		else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
			try {
				Double.parseDouble(obj + "");
			} catch (Exception e) {
				return bool;
			}
		} else if (obj instanceof String) {
			if (((String) obj).length() <= 0)
				return bool;
			if ("null".equals(obj))
				return bool;
		} else if (obj instanceof Map) {
			if (((Map<?, ?>) obj).size() == 0)
				return bool;
		} else if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0)
				return bool;
		}
		return !bool;
	}
	
}
