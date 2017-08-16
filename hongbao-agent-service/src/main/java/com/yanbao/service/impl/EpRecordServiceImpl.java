package com.yanbao.service.impl;

import com.mall.model.EpRecord;
import com.mall.model.Store;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.EpRecordMapper;
import com.yanbao.service.EpRecordService;
import com.yanbao.service.StoreService;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.StoreExchangeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ep相关类
 * Created by Administrator on 2017/6/20.
 */
@Service
public class EpRecordServiceImpl extends CommonServiceImpl<EpRecord> implements EpRecordService {

    @Autowired
    private EpRecordMapper epRecordMapper;
    @Autowired
    private StoreService storeService;

    @Override
    protected CommonDao<EpRecord> getDao() {
        return epRecordMapper;
    }

    @Override
    protected Class<EpRecord> getModelClass() {
        return EpRecord.class;
    }

    @Override
    public List<EpRecord> getSendRecordList(String userId,Page page) throws Exception {
        return epRecordMapper.getSendRecordList(userId,page);
    }

    @Override
    public Integer countSendRecordList(String userId) throws Exception {
        return epRecordMapper.countSendRecordList(userId);
    }

    @Override
    public List<StoreExchangeRecord> getEpList(Date starttime, Date endtime) throws Exception {

        List<StoreExchangeRecord> storeExchangeRecordList = epRecordMapper.getEpList(starttime,endtime);
        List<StoreExchangeRecord> storeExchangeRecords = new ArrayList<>();
        if(storeExchangeRecordList != null && storeExchangeRecordList.size() > 0){
            for (StoreExchangeRecord record : storeExchangeRecordList){
                if(!ToolUtil.isEmpty(record.getStoreId())){
                    Store store = storeService.readById(record.getStoreId());
                    record.setAreaId(store.getAreaId());
                }
                storeExchangeRecords.add(record);
            }
        }
        return storeExchangeRecords;
    }

	@Override
	public List<EpRecord> getByStoreIds(List<String> storeIds) {
		return epRecordMapper.getByStoreIds(storeIds);
	}
}
