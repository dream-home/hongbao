package com.yanbao.dao;

import java.util.List;
import com.mall.model.PayCallback;

/**
 * Title:PayCallbackDao
 * Description:支付回调记录DAO接口类
 * Copyright: Copyright (c) 2017
 * Company: 炎宝科技
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-05-26
 */
public interface PayCallbackDao {
	Integer add(PayCallback model)throws Exception;
	
	Integer del(PayCallback model)throws Exception;
	
	Integer delByPk(String id)throws Exception;
	
	Integer delList(List<String> ids)throws Exception;
	
	Integer modUpdate(PayCallback model)throws Exception;
	
	Integer modUpdateNotNull(PayCallback model)throws Exception;
	
	PayCallback getByPk(String id)throws Exception;
	
	Long getCount(PayCallback model)throws Exception;
	
	List<PayCallback> getList(PayCallback model)throws Exception;
}