package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysFileLinkDao;
import com.mall.model.SysFileLink;
import com.yanbao.service.SysFileLinkService;

/**
 * Created by summer on 2017-01-12:15:46;
 */
@Service
public class SysFileLinkServiceImpl extends CommonServiceImpl<SysFileLink> implements SysFileLinkService {

    @Autowired
    SysFileLinkDao sysFileLinkDao;

    @Override
    protected CommonDao<SysFileLink> getDao() {
        return sysFileLinkDao;
    }

    @Override
    protected Class<SysFileLink> getModelClass() {
        return SysFileLink.class;
    }
    
    @Override
   public  List<SysFileLink> readAllData(String id){
    	
    	
    return sysFileLinkDao.readAllData(id);	
    }
    
    
}
