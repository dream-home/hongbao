package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ConSumeEPRecordDao;
import com.mall.model.ConsumeEPRecord;

import com.yanbao.service.ConSumeEPRecordService;

@Service
public class ConSumeEPRecordImpl extends CommonServiceImpl<ConsumeEPRecord> implements ConSumeEPRecordService{
	
	@Autowired
	ConSumeEPRecordDao conDao;
	
	
	 @Override
	    protected CommonDao<ConsumeEPRecord> getDao() {
	        return conDao;
	    }

	    @Override
	    protected Class<ConsumeEPRecord> getModelClass() {
	        return ConsumeEPRecord.class;
	    }

	
	
	
	@Override
	public int count(){
		return conDao.count();
	}
	
	@Override
	public double ConSumeEPSUM(){
		return conDao.ConsumeEPSUM();
	}
	
	
	

}
