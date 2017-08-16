package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletRechargeDao;
import com.mall.model.WalletRecharge;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.vo.RechargeVo;

/**
 * Created by summer on 2016-12-08:18:04;
 */
@Service
public class WalletRechargeServieImpl extends CommonServiceImpl<WalletRecharge> implements WalletRechargeService {

    @Autowired
    WalletRechargeDao walletRechargeDao;

    @Override
    protected CommonDao<WalletRecharge> getDao() {
        return walletRechargeDao;
    }

    @Override
    protected Class<WalletRecharge> getModelClass() {
        return WalletRecharge.class;
    }

    @Override
    public double getIncome() {
        Double count=walletRechargeDao.getIncome();
        count=count==null?0:count;
        return count;
    }

    @Override
    public List<RechargeVo> getRechargeVoList(RechargeVo rechargeVo,int pageNo,int pageSize) {
        return walletRechargeDao.getRechargeVoList(rechargeVo,pageNo,pageSize);
    }

    @Override
    public Integer getRechargeVoCount(RechargeVo rechargeVo) {
        Integer count=walletRechargeDao.getRechargeVoCount(rechargeVo);
        count=count==null?0:count;
        return count;
    }

    @Override
    public double getSomeSum(WalletRecharge walletRecharge) {
        Double money=walletRechargeDao.getSomeSum(walletRecharge);
        return money==null?0:money;
    }
    
    @Override
    public List<RechargeVo> readListPartner(RechargeVo recharge,int pageNo,int pageSize,int totalRow) {
        return walletRechargeDao.readListPartner(recharge, pageNo, pageSize,totalRow);
    }

    @Override
    public Integer readCountPartner(RechargeVo recharge) {
        Integer count=walletRechargeDao.readCountPartner(recharge);
        count=count==null?0:count;
        return count;
    }


    @Override
    public List<RechargeVo> getRechargeVoList2(RechargeVo rechargeVo,int pageNo,int pageSize) {
        return walletRechargeDao.getRechargeVoList2(rechargeVo,pageNo,pageSize);
    }

    @Override
    public Integer getRechargeVoCount2(RechargeVo rechargeVo) {
        Integer count=walletRechargeDao.getRechargeVoCount2(rechargeVo);
        count=count==null?0:count;
        return count;
    }
    
}
