package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.GoodsIssue;

@Repository
public interface GoodsIssueMapper {

	GoodsIssue getById(@Param("id") String id);

	Integer add(@Param("model") GoodsIssue model);

	Integer update(@Param("id") String id, @Param("model") GoodsIssue model);

	Integer updateCurNum(@Param("id") String id);

	List<GoodsIssue> getTimeoutList(@Param("time") String time);
}
