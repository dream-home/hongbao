package com.yanbao.service.impl;

import com.mall.model.MenuPermission;
import com.mall.model.Store;
import com.mall.model.WalletExchange;
import com.yanbao.constant.*;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletExchangeMapper;
import com.yanbao.service.StoreService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletExchangeService;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class WalletExchangeServiceImpl  extends CommonServiceImpl<WalletExchange> implements WalletExchangeService {

    @Autowired
    private WalletExchangeMapper walletExchangeMapper;
    @Autowired
    private StoreService storeService;

    @Override
    protected CommonDao<WalletExchange> getDao() {
        return null;
    }
    @Override
    protected Class<WalletExchange> getModelClass() {
        return null;
    }

    @Override
    public List<StoreExchangeRecord> getStoreList(Date starttime, Date endtime) {

        List<StoreExchangeRecord> storeExchangeRecordList = walletExchangeMapper.getStoreList(starttime,endtime);
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
	public List<WalletExchange> getByStoreIds(List<String> storeIds) {
		return walletExchangeMapper.getByStoreIds(storeIds);
	}

}
