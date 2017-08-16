package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.StoreCollect;

@Repository
public interface StoreCollectMapper {

	Integer add(@Param("model") StoreCollect model);

	Integer update(@Param("id") String id, @Param("model") StoreCollect model);

	StoreCollect get(@Param("userId") String userId, @Param("storeId") String storeId);

	Integer count(@Param("userId") String userId);

	List<String> getList(@Param("userId") String userId, @Param("page") Page page);

}
