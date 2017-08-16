package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.Store;

@Repository
public interface StoreMapper {

	Store getById(@Param("id") String id);

	Integer add(@Param("model") Store model);

	Integer update(@Param("id") String id, @Param("model") Store model);

	Integer updateCollectNum(@Param("id") String id, @Param("num") Integer num);

	List<Store> getCollectList(@Param("storeIds") List<String> storeIds);

	Integer count(@Param("location") String location);

	List<Store> getList(@Param("page") Page page,@Param("location") String location);

	List<Store> getSimpleList(@Param("page") Page page,@Param("location") String location);

	List<Store> getListByIds( @Param("list") List<String> list);
	
	List<Store> getListByName(@Param("searchWord") String searchWord,@Param("location") String location,@Param("isHasCoordinate") Boolean isHasCoordinate);

	List<Store> getNewStore(@Param("maxRow") Integer maxRow);
	
	List<Store> getCollectListByUserId(@Param("userIds")List<String> userIds) throws Exception;

	Integer updateSumSaleCount(@Param("id") String id, @Param("num") Integer num);
}
