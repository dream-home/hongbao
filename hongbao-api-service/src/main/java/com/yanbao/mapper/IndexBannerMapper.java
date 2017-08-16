package com.yanbao.mapper;

import com.mall.model.IndexBanner;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexBannerMapper {

	List<IndexBanner> getList();
	
}
