package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.mall.model.Message;
import com.yanbao.core.dao.CommonDao;

/**
 * Created by summer on 2016-12-09:12:02;
 */
public interface MessageMapper extends CommonDao<Message> {
	
	
	void insertAll(@Param("model") Message message);
	
}
