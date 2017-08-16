package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.WalletExchange;

@Repository
public interface WalletExchangeMapper {

	WalletExchange getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

	Integer count(@Param("userId") String userId);

	List<WalletExchange> getList(@Param("userId") String userId, @Param("page") Page page);

	Integer add(@Param("model") WalletExchange model);

	Integer update(@Param("id") String id, @Param("model") WalletExchange model);

}
