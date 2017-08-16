package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.GoodsIssueDetail;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsIssueDetailDao {

	Long players() throws Exception;

	GoodsIssueDetail getByOrderNo(String orderNo, String userId) throws Exception;

	Integer count(String userId) throws Exception;

	List<GoodsIssueDetail> getList(String userId, Page page) throws Exception;

	Integer add(GoodsIssueDetail model) throws Exception;

	Integer update(String id, GoodsIssueDetail model) throws Exception;

	List<GoodsIssueDetail> getDrawList(String issueId, Integer status) throws Exception;

	List<GoodsIssueDetail> getRollbackDrawList() throws Exception;

	Double sumUserDrawScore(String userId) throws Exception;
	
	Integer  countSumPersonByIssueId(String issueId, Integer status) throws Exception;

}
