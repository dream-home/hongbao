package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.Message;

@Repository
public interface MessageMapper {

	Message getById(@Param("id") String id);

	Integer count(@Param("userIds") List<String> userIds, @Param("types") String[] types, @Param("status") Integer status);

	List<Message> getList(@Param("userIds") List<String> userIds, @Param("types") String[] types, @Param("page") Page page);

	Integer add(@Param("model") Message model);

	Integer update(@Param("id") String id, @Param("model") Message model);

}
