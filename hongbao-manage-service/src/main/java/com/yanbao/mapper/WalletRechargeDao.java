package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.WalletRecharge;
import com.yanbao.vo.RechargeVo;

/**
 * Created by summer on 2016-12-08:18:02;
 */
public interface WalletRechargeDao  extends CommonDao<WalletRecharge>{

    Double getIncome();

    List<RechargeVo> getRechargeVoList(@Param("model") RechargeVo rechargeVo,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    Integer getRechargeVoCount(@Param("model") RechargeVo rechargeVo);

    Double getSomeSum(@Param("model") WalletRecharge walletRecharge);
    
    
    List<RechargeVo> readListPartner(@Param("model") RechargeVo recharge, @Param("startRow") int startRow, @Param("pageSize") int pageSize, int totalRow);
    
    Integer readCountPartner(@Param("model") RechargeVo recharge);


    List<RechargeVo> getRechargeVoList2(@Param("model") RechargeVo rechargeVo,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    Integer getRechargeVoCount2(@Param("model") RechargeVo rechargeVo);
    
}
