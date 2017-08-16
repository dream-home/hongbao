package com.yanbao.service;

import java.util.List;

import com.mall.model.Image;
import com.yanbao.core.service.CommonService;

/**
 * @author Pay - 1091945691@qq.com
 */
public interface HbImgageService  extends CommonService<Image>{
	
	List<Image> readByIds(List<String> ids);
	List<Image> getPoList(Image image);
	
}