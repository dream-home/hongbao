package com.yanbao.service.impl;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ADDao;
import com.mall.model.Ad;
import com.yanbao.service.ADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by summer on 2016-12-08:13:56;
 */
@Service
public class ADServiceImpl extends CommonServiceImpl<Ad> implements ADService {

    @Autowired
    private ADDao adDao;

    @Override
    protected CommonDao<Ad> getDao() {
        return adDao;
    }

    @Override
    protected Class<Ad> getModelClass() {
        return Ad.class;
    }
    
    
    @Override
	public int readStartCount(Ad ad){
		return adDao.readStartCount(ad);
		
	}
	
	@Override
	public List<Ad> readStartList(Ad ad,Page page){
		return adDao.readStartList(ad,page);
		
	}
    
    
}
