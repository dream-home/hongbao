package com.yanbao.dao;

import java.util.List;

import com.mall.model.WalletBill;



/**
 * Title:WalletBillDao
 * Description:店铺收益DAO接口类
 * Copyright: Copyright (c) 2017
 * Company: 炎宝科技
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-06-05
 */
public interface WalletBillDao {
	
	List<WalletBill> getList(WalletBill model)throws Exception;
    
    WalletBill getById(String id)throws Exception;
    
    Integer delById(String id)throws Exception;
    
    Integer delInId(List<String> ids)throws Exception;
    
    Integer add(WalletBill model)throws Exception;
    
    Integer delPo(WalletBill model)throws Exception;
}