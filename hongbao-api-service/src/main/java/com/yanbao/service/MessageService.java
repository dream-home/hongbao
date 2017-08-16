package com.yanbao.service;

import com.yanbao.core.model.Token;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Message;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface MessageService {

	Message getById(String id) throws Exception;

	Integer countUnread(String userId, String[] types) throws Exception;

	Integer countAllread(String userId, String[] types) throws Exception;

	PageResult<Message> getPage(String userId, String[] types, Page page) throws Exception;

	Integer add(Message model) throws Exception;

	Integer update(String id, Message model) throws Exception;

	Integer countAllUnread(Token token) throws Exception;
}
