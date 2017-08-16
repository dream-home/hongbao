package com.yanbao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import static com.yanbao.constant.PayDistributionType.store;

public class ToolUtil {
    /**
     * 赤道半径
     */
    private static final double EARTH_RADIUS = 6378137;

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

    public static Double getBigDecimalDouble4(Double cost) {
        BigDecimal b = new BigDecimal(cost);
        return b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值, 不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
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
        } else if (obj instanceof List) {
            if (((List<?>) obj).size() == 0)
                return bool;
        } else if (obj instanceof Set) {
            if (((Set<?>) obj).size() == 0)
                return bool;
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0)
                return bool;
        }
        return !bool;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * java对象转换成json
     *
     * @param object java 对象
     * @return
     */
    public static String objectToJson(Object object) {
        return JSON.toJSON(object).toString();
    }

    /***
     * json字符串 转换成java对象
     * @param jsonObjectString json 字符串
     * @param clazz
     * @return Object
     */
    public static Object jsonToObject(String jsonObjectString, Class<?> clazz) {
        if (ToolUtil.isEmpty(jsonObjectString)) {
            return null;
        }
        return JSON.parseObject(jsonObjectString, clazz);
    }

    /***
     * 通过jsonObjectString字符串 获取 java 对象
     *
     *
     * @param jsonObjectString
     *            json 字符串
     * @param clazz
     * @return
     */

    public static Object getJavaObject(String jsonObjectString, Class<?> clazz) {
        if (ToolUtil.isEmpty(jsonObjectString)) {
            return null;
        }

        return JSON.parseObject(jsonObjectString, clazz);
    }

    /**
     * 获取 JSONObject 实例
     *
     * @return
     */
    public static JSONObject getJSONObject() {
        return new JSONObject();

    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static boolean isMobileNumber(String mobiles) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}").matcher(mobiles).matches();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 获取两个数组不同的值的集合
     *
     * @param listA
     * @param listB
     * @return
     */
    public static List<String> getDifference(List<String> listA, List<String> listB) {
        Map<String, Integer> map = new HashMap<String, Integer>(listA.size() + listB.size());
        List<String> diff = new ArrayList<String>();
        for (String string : listA) {
            map.put(string, 1);
        }
        for (String string : listB) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }


    /**
     * String类型转Integer 默认为0.
     *
     * @param sValue
     * @return
     */
    public static int parseInt(String sValue) {
        return parseInt(sValue, 0);
    }

    /**
     * String类型转Integer
     *
     * @param sValue
     * @param iDefault
     * @return
     */
    public static int parseInt(String sValue, int iDefault) {
        if (sValue == null) return iDefault;
        int _iValue = iDefault;
        try {
            _iValue = Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _iValue;
    }

    /**
     * String类型转Long 默认为0.
     *
     * @param sValue
     * @return
     */
    public static long parseLong(String sValue) {
        return parseLong(sValue, 0L);
    }

    /**
     * String类型转Long
     *
     * @param sValue
     * @param lDefault
     * @return
     */
    public static long parseLong(String sValue, long lDefault) {
        if (sValue == null) return lDefault;
        long _lValue = lDefault;
        try {
            _lValue = Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _lValue;
    }

    /**
     * String类型转float 默认为0.
     *
     * @param sValue
     * @return
     */
    public static float parseFloat(String sValue) {
        return parseFloat(sValue, 0);
    }

    /**
     * String类型转float
     *
     * @param sValue
     * @param fDefault
     * @return
     */
    public static float parseFloat(String sValue, float fDefault) {
        if (sValue == null) return fDefault;
        float _fValue = fDefault;
        try {
            _fValue = Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
        }
        return _fValue;
    }

    /**
     * String类型转double 默认为0.
     *
     * @param sValue
     * @return
     */
    public static double parseDouble(String sValue) {
        return parseDouble(sValue, 0.0);
    }

    /**
     * String类型转double.
     *
     * @param sValue
     * @param dDefault
     * @return
     */
    public static double parseDouble(String sValue, double dDefault) {
        if (sValue == null) return dDefault;
        double _dValue = dDefault;
        try {
            _dValue = Double.parseDouble(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _dValue;
    }

    /**
     * 把Object转换成String，如Object==null 返回""
     *
     * @param obj
     * @return
     */
    public static String parseString(Object obj) {
        return parseString(obj, ""); //$NON-NLS-1$
    }

    /**
     * 把Object转换成String，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String parseString(Object obj, String defaultValue) {
        if (obj == null)
            return defaultValue;
        else
            return String.valueOf(obj);
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;//单位米
    }


    public static String getDistance(Double distance) {
        if (distance == null || distance < 0) {
            return " ";
        }
        if (distance >= 1000) {
            return PoundageUtil.divide(distance, 1000d, 2) + "km";
        } else {
            return distance + "米";
        }
    }

    public static String readFile(String Path) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }


    public static void recreateJsonFile(String oldPath, String descPath) throws IOException {
        String laststr = readFile(oldPath);//读取原json文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(descPath));// 输出新的json文件
        String ws = null;
        System.out.println(laststr);
        try {
            JSONObject dataJson = JSONObject.parseObject(laststr);// 创建一个包含原始json串的json对象
            JSONArray data = dataJson.getJSONArray("data");// 找到data的json数组
            for (int i = 0; i < data.size(); i++) {
                JSONObject province = data.getJSONObject(i);// 获取data数组的第i个json对象
                JSONArray poptions = province.getJSONArray("options");// 找到options的json对象
                for (int j = 0; j < poptions.size(); j++) {
                    String text = poptions.getJSONObject(j).getString("text");// 读取properties对象里的name字段值
                    poptions.getJSONObject(j).put("first", PinYin.getFirstLetter(text).toUpperCase());
                }
            }
            ws = dataJson.toString();
            System.out.println(ws);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bw.write(ws);
        bw.flush();
        bw.close();
    }


    public static void delRedisKey(String key) {
        if (Sets.exists(key)) {
            Strings.del(key);
        }
    }

    public void batchDelRedisKey(Jedis jedis, String keyPrefix) {
        Set<String> set = jedis.keys(keyPrefix + "*");
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String keyStr = it.next();
            System.out.println(keyStr);
            jedis.del(keyStr);
        }
    }

    public static void main(String[] args) throws IOException {

        String s = "{\"Im\":\"http://apitest.doupaimall.com/h5/index.html\", \"Store\":\"http://apitest.doupaimall.com/m/user/wdtransition?index=Store&uid=200000&storeId=FB5937DBB58E45488D38228FC01E4928&groupType=A&goodsId=null\"}\n";

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject.get("Store"));
        Map<String, String> map = (Map<String, String>) ToolUtil.getJavaObject(s, Map.class);
        System.out.println(JSON.toJSONString(map));
        System.out.println(map.get("Store"));
        if (!map.containsKey("Store")) {
            System.out.println(1);
        }
        String a = "E:\\\\ideawork\\\\serveManage\\\\hongbao\\\\hongbao-agent-service\\\\src\\\\main\\\\resources\\\\json.json";
        String b = "E:\\\\ideawork\\\\serveManage\\\\hongbao\\\\hongbao-agent-service\\\\src\\\\main\\\\resources\\\\new.json";
//        recreateJsonFile(a,b);
      /*  try {
            String laststr =readFile("E:\\ideawork\\serveManage\\hongbao\\hongbao-agent-service\\src\\main\\resources\\json.json");
            BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\ideawork\\serveManage\\hongbao\\hongbao-agent-service\\src\\main\\resources\\new.json"));// 输出新的json文件
            String    ws = null;
            System.out.println(laststr);
            try {
                JSONObject dataJson = JSONObject.parseObject(laststr);// 创建一个包含原始json串的json对象
                JSONArray data = dataJson.getJSONArray("data");// 找到data的json数组
                for (int i = 0; i < data.size(); i++) {
                    JSONObject info = data.getJSONObject(i);// 获取data数组的第i个json对象
                    JSONArray options = info.getJSONArray("options");// 找到options的json对象
                    for (int j = 0; j < options.size(); j++) {
                        String text = options.getJSONObject(j).getString("text");// 读取properties对象里的name字段值
                        options.getJSONObject(j).put("first", PinYin.getFirstLetter(text).toUpperCase());
                    }
                }
                ws = dataJson.toString();


                System.out.println("0000000000000000000000000000000000000000000000");
                System.out.println("0000000000000000000000000000000000000000000000");
                System.out.println(ws);
                System.out.println("0000000000000000000000000000000000000000000000");
                System.out.println("0000000000000000000000000000000000000000000000");

            } catch (Exception e) {
                e.printStackTrace();
            }
            bw.write(ws);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

