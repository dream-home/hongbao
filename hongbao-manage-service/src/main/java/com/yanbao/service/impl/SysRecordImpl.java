package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysRecordDao;
import com.mall.model.SysRecord;

import com.yanbao.service.SysRecordService;

/**
 * 
 * @author zyc 2017-03-06 17:23
 *
 */
@Service
public class SysRecordImpl  extends CommonServiceImpl<SysRecord> implements SysRecordService {
	
	
	
	@Autowired
	SysRecordDao sysRecordDao;
	
	
	@Override
    protected CommonDao<SysRecord> getDao() {
        return sysRecordDao;
    }

    @Override
    protected Class<SysRecord> getModelClass() {
        return SysRecord.class;
    }
	
	
	
	
	
	
	@Override
	public double sysRecordSUM(){
		return sysRecordDao.sysRecordSUM();
		
	}
	
	@Override
	public double sysRecordRegister(){
		return sysRecordDao.sysRecordRegister();
		
	}
	
	

}
