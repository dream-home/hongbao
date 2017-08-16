package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.PayCallback;

@Repository
public interface PayCallbackMapper {
	
	Integer add(@Param("model") PayCallback model);
	
	Integer del(@Param("model") PayCallback model);
	
	Integer delByPk(@Param("id") String id);
	
	Integer delList(@Param("list") List<String> ids);
	
	Integer modUpdate(@Param("model") PayCallback model);
	
	Integer modUpdateNotNull(@Param("model") PayCallback model);
	
	PayCallback getByPk(@Param("id") String id);
	
	Long getCount(@Param("model") PayCallback model);
	
	List<PayCallback> getList(@Param("model") PayCallback model);
	
}
