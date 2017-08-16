package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.constant.StatusType;
import com.yanbao.core.page.Page;
import com.yanbao.dao.MessageDao;
import com.yanbao.mapper.MessageMapper;
import com.mall.model.Message;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class MessageDaoImpl implements MessageDao {

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public Message getById(String id) throws Exception {
		return messageMapper.getById(id);
	}

	@Override
	public Integer countUnread(List<String> userIds, String[] types) throws Exception {
		return messageMapper.count(userIds, types, StatusType.FALSE.getCode());
	}

	@Override
	public Integer count(List<String> userIds, String[] types, Integer status) throws Exception {
		return messageMapper.count(userIds, types, status);
	}

	@Override
	public List<Message> getList(List<String> userIds, String[] types, Page page) throws Exception {
		return messageMapper.getList(userIds, types, page);
	}

	@Override
	public Integer add(Message model) throws Exception {
		return messageMapper.add(model);
	}

	@Override
	public Integer update(String id, Message model) throws Exception {
		return messageMapper.update(id, model);
	}

}
