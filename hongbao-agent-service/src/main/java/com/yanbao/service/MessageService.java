package com.yanbao.service;

import com.mall.model.Message;
import com.yanbao.core.service.CommonService;

/**
 * Created by summer on 2016-12-09:12:03;
 */
public interface MessageService extends CommonService<Message> {
	
	
	void insertAll(Message message);
}
