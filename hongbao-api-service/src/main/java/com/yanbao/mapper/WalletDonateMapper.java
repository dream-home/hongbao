package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.WalletDonate;

@Repository
public interface WalletDonateMapper {

	WalletDonate getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

	Integer count(@Param("userId") String userId);

	List<WalletDonate> getList(@Param("userId") String userId, @Param("page") Page page);

	Integer add(@Param("model") WalletDonate model);

}
