package com.yanbao.mapper;

import com.mall.model.IndexAd;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexAdMapper {

	List<IndexAd> getList();
	
}
