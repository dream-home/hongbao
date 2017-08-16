package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.Message;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface MessageDao {

	Message getById(String id) throws Exception;

	Integer countUnread(List<String> userIds, String[] types) throws Exception;

	Integer count(List<String> userIds, String[] types, Integer status) throws Exception;

	List<Message> getList(List<String> userIds, String[] types, Page page) throws Exception;

	Integer add(Message model) throws Exception;

	Integer update(String id, Message model) throws Exception;

}
