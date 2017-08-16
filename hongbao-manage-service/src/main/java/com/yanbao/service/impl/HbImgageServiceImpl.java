package com.yanbao.service.impl;

import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ImgageMapper;
import com.yanbao.service.HbImgageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbImgageServiceImpl")
public class HbImgageServiceImpl extends CommonServiceImpl<Image> implements HbImgageService  {
	
	@Autowired
    private ImgageMapper dao;
	
	
	@Override
	public List<Image> readByIds(List<String> ids) {
		return dao.readByIds(ids);
	}

	@Override
	protected CommonDao<Image> getDao() {
		return dao;
	}

	@Override
	protected Class<Image> getModelClass() {
		return null;
	}
	
	@Override
	public List<Image> getPoList(Image image){
		
		return dao.getPoList(image);
		
	}
	
}