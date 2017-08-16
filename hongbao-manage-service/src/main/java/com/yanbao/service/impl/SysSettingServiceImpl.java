package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysSettingDao;
import com.mall.model.SysSetting;
import com.yanbao.service.SysSettingService;

/**
 * Created by summer on 2016-12-13:17:05;
 */
@Service
public class SysSettingServiceImpl extends CommonServiceImpl<SysSetting> implements SysSettingService {

    @Autowired
    SysSettingDao sysSettingDao;

    @Override
    protected CommonDao<SysSetting> getDao() {
        return sysSettingDao;
    }

    @Override
    protected Class<SysSetting> getModelClass() {
        return SysSetting.class;
    }
}
