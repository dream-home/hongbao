package com.yanbao.dao.impl;

import com.yanbao.core.page.Page;
import com.yanbao.dao.ConsumeEPRecordDao;
import com.yanbao.mapper.ConsumeEPRecordMapper;
import com.mall.model.ConsumeEPRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ZCJ
 * @date 2017年03月03日
 */
@Repository
public class ConsumeEPRecordDaoImpl implements ConsumeEPRecordDao {

    @Autowired
    private ConsumeEPRecordMapper goodsMapper;
    
    @Override
    public Integer add(ConsumeEPRecord model) {
        return goodsMapper.add(model);
    }
    
    //查询消费ep总数
	@Override
	public Integer count(String userId) throws Exception {
		
		return goodsMapper.count(userId);
	}
	
	/**
	 * 查询消费ep流水记录
	 */
	@Override
	public List<ConsumeEPRecord> getList(String userId, Page page) throws Exception {
		
		return goodsMapper.getList(userId, page);
	}
}
