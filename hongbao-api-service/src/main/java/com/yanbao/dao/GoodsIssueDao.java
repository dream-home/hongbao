package com.yanbao.dao;

import java.util.List;

import com.mall.model.GoodsIssue;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsIssueDao {

	GoodsIssue getById(String id) throws Exception;

	Integer add(GoodsIssue model) throws Exception;

	Integer update(String id, GoodsIssue model) throws Exception;
	
	Integer updateCurNum(String id) throws Exception;

	List<GoodsIssue> getTimeoutList(String time) throws Exception;
}
