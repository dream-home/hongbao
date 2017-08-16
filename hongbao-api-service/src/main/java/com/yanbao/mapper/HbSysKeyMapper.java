package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.HbSysKey;

@Repository
public interface HbSysKeyMapper {

	void insert(@Param("model") HbSysKey hbSysKey);
	
	void delById(@Param("model") HbSysKey hbSysKey);
}
