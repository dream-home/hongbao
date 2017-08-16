package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.PayDistribution;

/* 
 * 文件名：付款分销数据库操作类  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
@Repository
public interface PayDistributionMapper {
	
	/**
	 * 新增付款分销
	 * @param model
	 * @return
	 */
	public Integer addPayDistribution(@Param("model") PayDistribution model) throws Exception;
	
	/**
	 * 保存付款分销
	 * @param model
	 * @return
	 */
	public Integer updatePayDistribution(@Param("model") PayDistribution model) throws Exception;
	
	/**
	 * 根据会员id查询付款分销
	 * @param userId
	 * @param type
     * @return
	 * @throws Exception
	 */
	public PayDistribution getByUserId(@Param("userId") String userId,@Param("type") Integer type) throws Exception;

}

