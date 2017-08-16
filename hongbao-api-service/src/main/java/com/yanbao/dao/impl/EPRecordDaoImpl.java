package com.yanbao.dao.impl;

import com.yanbao.core.page.Page;
import com.yanbao.dao.EPRecordDao;
import com.yanbao.mapper.EPRecordMapper;
import com.mall.model.EpRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zzwei
 * @date 2017年06月17日
 */
@Repository
public class EPRecordDaoImpl implements EPRecordDao {

    @Autowired
    private EPRecordMapper epRecordMapper;

    @Override
    public Integer count(String userId, String[] recordTypes) throws Exception {
        return epRecordMapper.count(userId, recordTypes);
    }

    @Override
    public List<EpRecord> getList(String userId, String[] recordTypes, Page page) throws Exception {
        return epRecordMapper.getList(userId, recordTypes, page);
    }

    @Override
    public Integer add(EpRecord model) throws Exception {
        return epRecordMapper.add(model);
    }

    @Override
    public Integer update(String id, EpRecord model) throws Exception {
        return epRecordMapper.update(id, model);
    }

}
