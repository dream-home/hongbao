package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.Message;

/**
 * Created by summer on 2016-12-09:12:02;
 */
public interface MessageDao extends CommonDao<Message> {
	
	
	void insertAll(@Param("model") Message message);
	
}
