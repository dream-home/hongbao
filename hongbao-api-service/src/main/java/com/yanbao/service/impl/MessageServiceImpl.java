package com.yanbao.service.impl;

import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.MessageDao;
import com.mall.model.Message;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Keys;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.MessageService;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public Message getById(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return messageDao.getById(id);
	}

	@Override
	public Integer countUnread(String userId, String[] types) throws Exception {
		if (userId == null || types.length <= 0) {
			return 0;
		}
		List<String> userIds = new ArrayList<String>();
		userIds.add(userId);
		if (Arrays.asList(types).contains("0")) {
			userIds.add("all");
		}
		return messageDao.countUnread(userIds, types);
	}
	@Override
	public Integer countAllread(String userId, String[] types) throws Exception {
		if (userId == null || types.length <= 0) {
			return 0;
		}
		List<String> userIds = new ArrayList<String>();
		userIds.add(userId);
		if (Arrays.asList(types).contains("0")) {
			userIds.add("all");
		}
		return messageDao.count(userIds, types, null);
	}

	@Override
	public PageResult<Message> getPage(String userId, String[] types, Page page) throws Exception {
		if (userId == null || types.length <= 0) {
			return null;
		}
		PageResult<Message> pageResult = new PageResult<Message>();
		BeanUtils.copyProperties(pageResult, page);

		List<String> userIds = new ArrayList<String>();
		userIds.add(userId);
		if (Arrays.asList(types).contains("0")) {
			userIds.add("all");
		}
		Integer count = messageDao.count(userIds, types, null);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<Message> list = messageDao.getList(userIds, types, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(Message model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		// websocket
		if (Keys.exists(RedisKey.ALL_IDS.getKey()) && !org.springframework.util.StringUtils.isEmpty(model.getUserId())) {
			String value=Hash.hget(RedisKey.ALL_IDS.getKey(), model.getUserId());
			if (!org.springframework.util.StringUtils.isEmpty(value)) {
				Hash.hincrby(RedisKey.ALL_IDS.getKey(), model.getUserId(), 1);
			}
			Sets.sadd(RedisKey.WAITING_IDS.getKey(), model.getUserId());
		}
		return messageDao.add(model);
	}

	@Override
	public Integer update(String id, Message model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return messageDao.update(id, model);
	}

	private void setDefaultValue(Message model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.FALSE.getCode()); // 默认未读状态
		model.setCreateTime(new Date());
	}
 
	@Override
	public Integer countAllUnread(Token token) throws Exception {

		Integer total = 0;
		Map<String, Object> result = new HashMap<String, Object>();
		// 系统消息未读统计
		String[] systemTypes = { "0", "4", "5", "6", "7" };
		Integer system = this.countUnread(token.getId(), systemTypes);
		result.put("system", system);
		total += system;
		// 消费消息
		String[] expenseTypes = { "1", "3" };
		Integer expense = this.countUnread(token.getId(), expenseTypes);
		result.put("expense", expense);
		total += expense;
		// 中拍消息
		String[] winTypes = { "2" };
		Integer win = this.countUnread(token.getId(), winTypes);
		result.put("win", win);
		total += win;
		Hash.hset(RedisKey.ALL_IDS.getKey(), token.getId(), total.toString());
		Sets.sadd(RedisKey.WAITING_IDS.getKey(),  token.getId());
		Strings.set( token.getId(), total.toString());
		return total;
	
	}
	public static void main(String[] args) {
		System.out.println(Hash.hget(RedisKey.ALL_IDS.getKey(), "id"));
	}

}
