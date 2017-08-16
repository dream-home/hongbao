package com.yanbao.dao;

import java.util.List;

import com.mall.model.WalletBillDetail;

/**
 * Title:WalletBillDetailDao
 * Description:店铺收益详细DAO接口类
 * Copyright: Copyright (c) 2017
 * Company: 炎宝科技
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-06-05
 */
public interface WalletBillDetailDao {
	
	List<WalletBillDetail> getList(WalletBillDetail model)throws Exception;
    
    WalletBillDetail getById(String id)throws Exception;
    
    Integer delById(String id)throws Exception;
    
    Integer delInId(List<String> ids)throws Exception;
    
    Integer add(WalletBillDetail model)throws Exception;
    
    Integer delPo(WalletBillDetail model)throws Exception;
}