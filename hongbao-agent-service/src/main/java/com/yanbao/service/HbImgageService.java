package com.yanbao.service;

import com.mall.model.Image;
import com.yanbao.core.service.CommonService;

import java.util.List;

/**
 * @author Pay - 1091945691@qq.com
 */
public interface HbImgageService  extends CommonService<Image>{

    List<Image> readByIds(List<String> ids);
}