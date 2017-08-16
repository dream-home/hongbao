package com.yanbao.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ImgageMapper;
import com.yanbao.service.HbImgageService;

import java.util.List;

/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbImgageServiceImpl")
public class HbImgageServiceImpl extends CommonServiceImpl<Image> implements HbImgageService  {
	
	@Autowired
	private ImgageMapper mapper;
	
	@Override
	protected CommonDao<Image> getDao() {
		return mapper;
	}

	@Override
	protected Class<Image> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Image> readByIds(List<String> ids) {
		return mapper.readByIds(ids);
	}

}