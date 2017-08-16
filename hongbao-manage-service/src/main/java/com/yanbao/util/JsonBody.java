package com.yanbao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.ArrayUtils;


/**
 * Created by summer on 2016-12-07:11:46;
 * 用于rap 参数和返回值json导入使用
 */
public class JsonBody {

    public static Field[] getFields(Class clz){

        Field[] fields = clz.getDeclaredFields();
        Field[] fields1 = clz.getSuperclass().getDeclaredFields();
        Field[] fields2=clz.getSuperclass().getSuperclass().getDeclaredFields();
        fields= (Field[]) ArrayUtils.addAll(fields,fields1);
        if (fields2!=null){
            fields=(Field[])ArrayUtils.addAll(fields,fields2);
        }
        return fields;
    }

    public static String getJsonRequestBody(Class clz){
        String body="{";
        Field[] fields=getFields(clz);
        for (Field f:fields){
            if (Modifier.isStatic(f.getModifiers()))continue;
            if (f.getName().equals("status"))continue;
            String className=f.getType().getSimpleName();
            if (className.equals("String")){

                body=body+"\""+f.getName()+"\""+":"+"\"\""+",";
            }else {
                body=body+"\""+f.getName()+"\""+":"+"1"+",";
            }
        }
        body=body+"}";
        System.out.println(body);
        return body;
    }
    public static String getJsonResponseBody(Class clz){
        String body="{ \n" +
                "    \"code\": 1, \n" +
                "    \"msg\": \"\", \n" +
                "    \"result\": {\n" +
                "        \"pageNo\": 1, \n" +
                "        \"pageSize\": 1, \n" +
                "        \"totalSize\": 1, \n" +
                "        \"rows\": [\n" +
                "            {";
        Field[] fields=getFields(clz);
        for (Field f:fields){
            if (Modifier.isStatic(f.getModifiers()))continue;
            if (f.getName().equals("status"))continue;
            String className=f.getType().getSimpleName();
            if (className.equals("String")){
                body=body+"\""+f.getName()+"\""+":"+"\"\""+",";
            }else {
                if (f.getName().equals("updateTime")){
                    body=body+"\""+"fUpdateTime"+"\""+":"+"\"yyyy-MM-dd HH:mm:ss\""+",";
                }else if (f.getName().equals("createTime")){
                    body=body+"\""+"fCreateTime"+"\""+":"+"\"yyyy-MM-dd HH:mm:ss\""+",";
                }else{

                    body=body+"\""+f.getName()+"\""+":"+"1"+",";
                }
            }
        }
        body=body+" }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        System.out.println(body);
        return body;
    }
    public static void getObjJson(Class clz){
        getJsonRequestBody(clz);
        getJsonResponseBody(clz);
    }


}
