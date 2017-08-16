package com.yanbao.util.h5;

/**
 * @author jay.zheng
 * @date 2017/7/26
 */
public interface IWxAction {
    String doAction(String orderNo, String userId, Double fee, int payType) throws Exception;
}
