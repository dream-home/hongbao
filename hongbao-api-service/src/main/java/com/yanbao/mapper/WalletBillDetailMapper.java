package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.WalletBillDetail;

@Repository 
public interface WalletBillDetailMapper {

    List<WalletBillDetail> getList(@Param("model") WalletBillDetail model);
    
    WalletBillDetail getById(@Param("id") String id);
    
    Integer delById(@Param("id") String id);
    
    Integer delInId(@Param("ids") List<String> ids);
    
    Integer add(@Param("model") WalletBillDetail model);
    
    Integer delPo(@Param("model") WalletBillDetail model);
    
}
