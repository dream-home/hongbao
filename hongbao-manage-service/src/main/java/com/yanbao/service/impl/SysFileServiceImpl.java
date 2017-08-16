package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysFileDao;
import com.mall.model.SysFile;
import com.yanbao.service.SysFileService;

/**
 * Created by summer on 2017-01-12:15:37;
 */
@Service
public class SysFileServiceImpl extends CommonServiceImpl<SysFile> implements SysFileService {

    @Autowired
    SysFileDao sysFileDao;

    @Override
    protected CommonDao<SysFile> getDao() {
        return sysFileDao;
    }

    @Override
    protected Class<SysFile> getModelClass() {
        return SysFile.class;
    }
}
