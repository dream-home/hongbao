package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.WalletRecord;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletRecordDao {

	Integer count(String userId, String[] recordTypes) throws Exception;

	List<WalletRecord> getList(String userId, String[] recordTypes, Page page) throws Exception;

	Integer add(WalletRecord model) throws Exception;

	Integer update(String id, WalletRecord model) throws Exception;

	Double sumScore(String userId, Integer recordType) throws Exception;

	List<WalletRecord> getLatestList(String userId) throws Exception;
}
