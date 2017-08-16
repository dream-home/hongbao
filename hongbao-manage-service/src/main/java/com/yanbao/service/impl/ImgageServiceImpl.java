package com.yanbao.service.impl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ImgageDao;
import com.yanbao.service.ImgageService;

/**
 * @author zyc 2017-06-20  10:45
 */
@Service
public class ImgageServiceImpl extends CommonServiceImpl<Image> implements ImgageService  {
	
	@Autowired
	private ImgageDao imgageDao;
	
	@Override
	protected CommonDao<Image> getDao() {
		return imgageDao;
	}

	@Override
	protected Class<Image> getModelClass() {
		// TODO Auto-generated method stub
		return Image.class;
	}
	
}