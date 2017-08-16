package com.yanbao.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzwei on 2017/6/30 0030.
 */
public class ImageUtils {
    /**
     * 处理七牛要删除的数据
     *
     * @param listA
     * @param listB
     * @return
     */
    public static void delQiniuImage(String bucket, List<String> listA, List<String> listB) {
        List<String> difference = ToolUtil.getDifference(listA, listB);
        if (ToolUtil.isEmpty(difference) || ToolUtil.isEmpty(listB)) {
            return;
        }
        if (ToolUtil.isEmpty(listA)) {
            QiNiuUtil.batchDelFile(bucket, listB.toArray(new String[listB.size()]));
        }
        for (String a:listA){
            if (difference.contains(a)){
                difference.remove(a);
            }
        }
        for (String ee:difference){
            System.out.println(ee);
        }
        if (ToolUtil.isNotEmpty(difference)){
            QiNiuUtil.batchDelFile(bucket, difference.toArray(new String[difference.size()]));
        }
    }

    public static void main(String[] args) {
        List<String> listA=new ArrayList<>();
        List<String> listB=new ArrayList<>();
        listA.add("a");
        listA.add("b");
        listA.add("c");
        listA.add("d");


        listB.add("a");
        listB.add("c");
        listB.add("e");

      ImageUtils.delQiniuImage("SDFSD",listA,listB);
        /*for (String dddd:dd){
            System.out.println(dddd);
        }*/
    }
}
