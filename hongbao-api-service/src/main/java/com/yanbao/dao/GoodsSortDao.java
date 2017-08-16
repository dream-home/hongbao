package com.yanbao.dao;

import java.util.List;

import com.mall.model.GoodsSort;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsSortDao {

	List<GoodsSort> getList() throws Exception;
	
	List<GoodsSort> getEpList() throws Exception;

}
