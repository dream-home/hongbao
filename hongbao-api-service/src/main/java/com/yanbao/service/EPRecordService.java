package com.yanbao.service;

import com.yanbao.constant.EPRecordType;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.EpRecord;
import com.mall.model.User;

/**
 * 
 * @author zzwei
 * @date 2017年06月17日
 */
public interface EPRecordService {

	PageResult<EpRecord> getPage(String userId, String[] recordTypes, Page page) throws Exception;

	Integer add(EpRecord model) throws Exception;

	Integer update(String id, EpRecord model) throws Exception;

	public void consumeEpRecord(User user, Double ep, String orderNo, EPRecordType recordType,String sendUserId,String receiveUserId,String remark) throws Exception ;
}
