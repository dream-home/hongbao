package com.yanbao.service.impl;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.ConsumeEPRecordDao;
import com.mall.model.ConsumeEPRecord;
import com.yanbao.service.ConsumeEPRecordService;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.UUIDUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
@Service
public class ConsumeEPRecordServiceImpl implements ConsumeEPRecordService {

    @Autowired
    private ConsumeEPRecordDao consumeEPRecordDao;
    @Override
    public Integer add(ConsumeEPRecord model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return consumeEPRecordDao.add(model);
    }
    private void setDefaultValue(ConsumeEPRecord model) {
		model.setOrderNo(OrderNoUtil.get());
        model.setId(UUIDUtil.getUUID());
        model.setCreateTime(new Date());
    }
    
    /**
     * 查询消费ep流水
     */
	@Override
	public PageResult<ConsumeEPRecord> getPage(String userId, Page page) throws Exception {
		if (userId == null) {
			return null;
		}
		PageResult<ConsumeEPRecord> pageResult = new PageResult<ConsumeEPRecord>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = consumeEPRecordDao.count(userId);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<ConsumeEPRecord> list = consumeEPRecordDao.getList(userId, page);
			pageResult.setRows(list);
		}
		
		return pageResult;
	}
}
