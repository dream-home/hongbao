package com.yanbao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mall.model.Ad;

@Repository
public interface AdMapper {

	List<Ad> getList(Ad ad);
	
}
