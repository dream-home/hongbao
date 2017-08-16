package com.yanbao.service.impl;

import com.yanbao.dao.WalletSubsidySignDao;
import com.mall.model.WalletSubsidySign;
import com.yanbao.service.WalletSubsidySignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zcj
 * @date 2017年06月05日
 */
@Service
public class WalletSubsidySignServiceImpl implements WalletSubsidySignService {
    @Autowired
    private WalletSubsidySignDao subsidySignDao;

    @Override
    public Integer add(WalletSubsidySign model) throws Exception {
        if (model == null) {
            return 0;
        }
        return subsidySignDao.add(model);
    }

    @Override
    public Integer update(WalletSubsidySign model, String id) throws Exception {
        if (model == null) {
            return 0;
        }
        if (StringUtils.isEmpty(id)) {
            return 0;
        }
        return subsidySignDao.update(model, id);
    }

    @Override
    public WalletSubsidySign getByUserId(String userid,Integer status) throws Exception {
        if (StringUtils.isEmpty(userid)) {
            return null;
        }
        return subsidySignDao.getByUserId(userid,status);
    }
}
