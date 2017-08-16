package com.yanbao.dao;

import com.yanbao.core.page.Page;
import com.mall.model.EpRecord;

import java.util.List;

/**
 * 
 * @author zzwei
 * @date 2017年06月27日
 */
public interface EPRecordDao {

	Integer count(String userId, String[] recordTypes) throws Exception;

	List<EpRecord> getList(String userId, String[] recordTypes, Page page) throws Exception;

	Integer add(EpRecord model) throws Exception;

	Integer update(String id, EpRecord model) throws Exception;

}
