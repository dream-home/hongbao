package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.yanbao.constant.RedisKey;
import com.yanbao.dao.BankDao;
import com.yanbao.mapper.BankMapper;
import com.mall.model.Bank;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class BankDaoImpl implements BankDao {

	@Autowired
	private BankMapper bankMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getList() throws Exception {
		List<Bank> list = null;
		String temp = Strings.get(RedisKey.BANK_LIST.getKey());
		if (null != temp) {
			list = (List<Bank>) JSONArray.parse(temp);
		} else {
			list = bankMapper.getList();
			Strings.setEx(RedisKey.BANK_LIST.getKey(), RedisKey.BANK_LIST.getSeconds(), JSONArray.toJSONString(list));
		}
		return list;
	}

}
