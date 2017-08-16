package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.WalletRecord;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletRecordService {

	PageResult<WalletRecord> getPage(String userId, String[] recordTypes, Page page) throws Exception;

	Integer add(WalletRecord model) throws Exception;

	Integer update(String id, WalletRecord model) throws Exception;

	Double sumScore(String userId, Integer recordType) throws Exception;

	List<WalletRecord> getLatestList(String userId) throws Exception;
}
