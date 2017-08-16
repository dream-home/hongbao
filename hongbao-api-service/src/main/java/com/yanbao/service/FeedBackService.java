package com.yanbao.service;

import com.mall.model.FeedBack;

/**
 * 
 * @author zzwei
 * @date 2017年06月27日
 */
public interface FeedBackService {

	FeedBack getById(String id) throws Exception;

	Integer add(FeedBack model) throws Exception;

}
