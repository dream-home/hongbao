package com.yanbao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mall.model.GoodsSort;

@Repository
public interface GoodsSortMapper {

	List<GoodsSort> getList();
	
	List<GoodsSort> getEpList();
	
}
