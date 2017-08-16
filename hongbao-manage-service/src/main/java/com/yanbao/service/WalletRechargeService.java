package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.WalletRecharge;
import com.yanbao.vo.RechargeVo;

/**
 * Created by summer on 2016-12-08:18:03;
 */
public interface WalletRechargeService extends CommonService<WalletRecharge>{

    double getIncome();

    List<RechargeVo> getRechargeVoList(RechargeVo rechargeVo,int pageNo,int pageSize);

    Integer getRechargeVoCount( RechargeVo rechargeVo);
    //获取各种支付方式的和
    double getSomeSum(WalletRecharge walletRecharge);
    
    
    List<RechargeVo> readListPartner(RechargeVo recharge,int pageNo,int pageSize,int totalRow);

    Integer readCountPartner( RechargeVo recharge);

    List<RechargeVo> getRechargeVoList2(RechargeVo rechargeVo,int pageNo,int pageSize);

    Integer getRechargeVoCount2( RechargeVo rechargeVo);


}
