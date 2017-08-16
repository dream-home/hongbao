package com.yanbao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.WalletRecharge;

@Repository
public interface WalletRechargeMapper {

	WalletRecharge getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

	WalletRecharge getUserOrderByOrderNo(@Param("orderNo") String orderNo);

	Integer count(@Param("userId") String userId);

	List<WalletRecharge> getList(@Param("userId") String userId, @Param("page") Page page);

	Integer add(@Param("model") WalletRecharge model);

	Integer update(@Param("id") String id, @Param("model") WalletRecharge model);

	List<WalletRecharge> getByscore(@Param("model")WalletRecharge wr,@Param("sources") List<Integer> sources,@Param("page") Page page);
	
	Integer countByscore(@Param("model")WalletRecharge wr,@Param("sources") List<Integer> sources) throws Exception;
	
	Double statistics(@Param("model")WalletRecharge wr,@Param("sources") List<Integer> sources,@Param("nowDate")String nowDate) throws Exception;
	
	List<WalletRecharge> getListByTime(@Param("starttime")Date starttime,@Param("endtime")Date endtime,@Param("status")Integer status,@Param("source")Integer source);
}
