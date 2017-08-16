package com.yanbao.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.AdDao;
import com.mall.model.Ad;
import com.yanbao.service.AdService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class AdServiceImpl implements AdService {

	private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

	@Autowired
	private AdDao adDao;

	@Override
	public List<Ad> getList(Ad ad) throws Exception {
		return adDao.getList(ad);
	}

}
