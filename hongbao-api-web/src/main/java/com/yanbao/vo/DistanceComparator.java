package com.yanbao.vo;

import java.util.Comparator;

/**
 * Created by zzwei
 * 2017/7/14 0014.
 */
public class DistanceComparator  implements Comparator<StoreVo> {
    @Override
    public int compare(StoreVo o1, StoreVo o2) {
            // 先按年龄排序
            int flag = o1.getDistance().compareTo(o2.getDistance());
            // 年龄相等比较姓名
            if (flag == 0) {
                return o1.getDistance().compareTo(o2.getDistance());
            } else {
                return flag;
            }
    }
}
