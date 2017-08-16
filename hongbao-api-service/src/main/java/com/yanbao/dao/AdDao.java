package com.yanbao.dao;

import java.util.List;

import com.mall.model.Ad;

/**
 *
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface AdDao {

	/**
	 *  获取首页广告列表
	 */
	List<Ad> getList(Ad ad) throws Exception;
}
