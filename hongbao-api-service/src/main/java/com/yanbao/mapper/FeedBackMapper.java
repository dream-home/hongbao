package com.yanbao.mapper;

import com.mall.model.FeedBack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackMapper {

	FeedBack getById(@Param("id") String id);

	Integer add(@Param("model") FeedBack model);

}
