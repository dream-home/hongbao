package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletDonateDao;
import com.mall.model.WalletDonate;
import com.yanbao.service.WalletDonateService;
import com.yanbao.vo.WalletDonateVo;

/**
 * Created by summer on 2016-12-13:16:31;
 */
@Service
public class WalletDonateServiceImpl extends CommonServiceImpl<WalletDonate> implements WalletDonateService {

    @Autowired
    WalletDonateDao walletDonateDao;

    @Override
    protected CommonDao<WalletDonate> getDao() {
        return walletDonateDao;
    }

    @Override
    protected Class<WalletDonate> getModelClass() {
        return WalletDonate.class;
    }

    @Override
    public double getIncome() {
        Double count=walletDonateDao.getIncome();
        count=count==null?0:count;
        return count;
    }

    @Override
    public List<WalletDonateVo> readVoList(WalletDonateVo walletDonateVo, Page page) {
        return walletDonateDao.readVoList(walletDonateVo,page);
    }

    @Override
    public Integer readVoCount(WalletDonateVo walletDonateVo) {
        Integer count=walletDonateDao.readVoCount(walletDonateVo);
        return count==null?0:count;
    }
}
