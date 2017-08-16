package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.mall.model.WalletBill;

@Repository 
public interface WalletBillMapper {

    List<WalletBill> getList(@Param("model") WalletBill model);
    
    WalletBill getById(@Param("id") String id);
    
    Integer delById(@Param("id") String id);
    
    Integer delInId(@Param("ids") List<String> ids);
    
    Integer add(@Param("model") WalletBill model);
    
    Integer delPo(@Param("model") WalletBill model);
    
}
