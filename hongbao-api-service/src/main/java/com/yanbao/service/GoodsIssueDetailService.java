package com.yanbao.service;

import java.util.List;
import java.util.Map;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsIssueDetail;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsIssueDetailService {

	Long players() throws Exception;

	GoodsIssueDetail getByOrderNo(String orderNo, String userId) throws Exception;

	PageResult<GoodsIssueDetail> getPage(String userId, Page page) throws Exception;

	Integer add(GoodsIssueDetail model) throws Exception;

	Integer update(String id, GoodsIssueDetail model) throws Exception;

	List<GoodsIssueDetail> getDrawList(String issueId, Integer status) throws Exception;

	List<GoodsIssueDetail> getRollbackDrawList() throws Exception;

	boolean checkDraw(String issueId, String userId) throws Exception;

	Double sumUserDrawScore(String userId) throws Exception;
	
	Integer  countSumPersonByIssueId(String issueId, Integer status) throws Exception;

	boolean checkDrawV3(Map<String, String> map,String userId) throws Exception;

}
