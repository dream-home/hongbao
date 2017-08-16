package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.ConsumeEPRecord;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
public interface ConsumeEPRecordDao {
    Integer add(ConsumeEPRecord model);
    
    /**
     * 查询消费ep流水记录总数
     * @param userId
     * @return
     * @throws Exception
     */
    Integer count(String userId) throws Exception;

    /**
     * 查询ep消费流水记录
     * @param userId
     * @param page
     * @return
     * @throws Exception
     */
	List<ConsumeEPRecord> getList(String userId,Page page) throws Exception;
}
