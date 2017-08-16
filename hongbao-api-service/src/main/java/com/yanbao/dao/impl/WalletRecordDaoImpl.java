package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletRecordDao;
import com.yanbao.mapper.WalletRecordMapper;
import com.mall.model.WalletRecord;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class WalletRecordDaoImpl implements WalletRecordDao {

	@Autowired
	private WalletRecordMapper walletRecordMapper;

	@Override
	public Integer count(String userId, String[] recordTypes) throws Exception {
		return walletRecordMapper.count(userId, recordTypes);
	}

	@Override
	public List<WalletRecord> getList(String userId, String[] recordTypes, Page page) throws Exception {
		return walletRecordMapper.getList(userId, recordTypes, page);
	}

	@Override
	public Integer add(WalletRecord model) throws Exception {
		return walletRecordMapper.add(model);
	}

	@Override
	public Integer update(String id, WalletRecord model) throws Exception {
		return walletRecordMapper.update(id, model);
	}

	@Override
	public Double sumScore(String userId, Integer recordType) throws Exception {
		Double count = 0d;
		String temp = Strings.get(RedisKey.RECORD_SUM.getKey() + recordType + "_" + userId);
		if (null != temp) {
			count = Double.parseDouble(temp);
		} else {
			Double sumScore = walletRecordMapper.sumScore(userId, recordType);
			count = sumScore == null ? 0d : sumScore;
			Strings.setEx(RedisKey.RECORD_SUM.getKey() + recordType + "_" + userId, RedisKey.RECORD_SUM.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public List<WalletRecord> getLatestList(String userId) throws Exception {
		return walletRecordMapper.getLatestList(userId);
	}
}
