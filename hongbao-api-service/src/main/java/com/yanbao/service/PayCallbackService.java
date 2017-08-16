package com.yanbao.service;

import java.util.List;

import com.mall.model.PayCallback;

/**
 * <p>Title:PayCallbackService</p>
 * <p>Description:支付回调记录服务接口类</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 炎宝科技</p>
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-05-26
 */
public interface PayCallbackService{
	Integer add(PayCallback model)throws Exception;
	
	Integer del(PayCallback model)throws Exception;
	
	Integer delByPk(String id)throws Exception;
	
	Integer delList(List<String> ids)throws Exception;
	
	Integer modUpdate(PayCallback model)throws Exception;
	
	Integer modUpdateNotNull(PayCallback model)throws Exception;
	
	PayCallback getByPk(String id)throws Exception;
	
	Long getCount(PayCallback model)throws Exception;
	
	List<PayCallback> getList(PayCallback model)throws Exception;

    void doCallBackAction(String orderNo, String uri, String params,boolean flag);
}