package com.yanbao.dao;

import com.mall.model.FeedBack;

/**
 * 
 * @author zzwei
 * @date 2017年06月27日
 */
public interface FeedBackDao {

	FeedBack getById(String id) throws Exception;


	Integer add(FeedBack model) throws Exception;

}
