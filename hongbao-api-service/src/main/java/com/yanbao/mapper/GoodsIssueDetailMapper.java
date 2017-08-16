package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.GoodsIssueDetail;

@Repository
public interface GoodsIssueDetailMapper {

	Long players();

	GoodsIssueDetail getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

	Integer count(@Param("userId") String userId);

	List<GoodsIssueDetail> getList(@Param("userId") String userId, @Param("page") Page page);

	Integer add(@Param("model") GoodsIssueDetail model);

	Integer update(@Param("id") String id, @Param("model") GoodsIssueDetail model);

	List<GoodsIssueDetail> getDrawList(@Param("issueId") String issueId, @Param("status") Integer status);

	List<GoodsIssueDetail> getRollbackDrawList();

	Double sumUserDrawScore(@Param("userId") String userId);
	
	Integer  countSumPersonByIssueId(@Param("issueId") String issueId,@Param("status") Integer status) throws Exception;

}
