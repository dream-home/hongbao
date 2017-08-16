package com.yanbao.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletRecordDao;
import com.mall.model.WalletRecord;
import com.yanbao.service.WalletRecordService;
import com.yanbao.vo.RecordVo;
import com.yanbao.vo.WalletRecordVo;

/**
 * Created by summer on 2016-12-09:10:26;
 */
@Service
public class WalletRecordServiceImpl extends CommonServiceImpl<WalletRecord> implements WalletRecordService {

    @Autowired
    WalletRecordDao walletRecordDao;

    @Override
    protected CommonDao<WalletRecord> getDao() {
        return walletRecordDao;
    }

    @Override
    protected Class<WalletRecord> getModelClass() {
        return WalletRecord.class;
    }

    @Override
    public List<Map> getScoreCoutByType() {
        return walletRecordDao.getScoreCoutByType();
    }

    @Override
    public List<RecordVo> getRecordVoList(RecordVo recordVo, int pageNo, int pageSize) {
        return walletRecordDao.getRecordVoList(recordVo,pageNo,pageSize);
    }
    
    @Override
    public List<WalletRecordVo> getRecordVoList1(WalletRecordVo walletRecordVo, int pageNo, int pageSize) {
        return walletRecordDao.getRecordVoList1(walletRecordVo,pageNo,pageSize);
    }
    
    

    @Override
    public Integer getRecordVoCount(RecordVo recordVo) {
        Integer count=walletRecordDao.getRecordVoCount(recordVo);
        count=count==null?0:count;
        return count;
    }
    
    @Override
    public Integer getRecordVoCount1(WalletRecordVo walletRecordVo) {
        Integer count=walletRecordDao.getRecordVoCount1(walletRecordVo);
        count=count==null?0:count;
        return count;
    }

    @Override
    public double getSomeFee(WalletRecord walletRecord) {
        Double fee=walletRecordDao.getSomeFee(walletRecord);
        return fee==null?0:fee;
    }
    @Override
    public List<WalletRecordVo> getPaymentDetail(String orderNo){
        return walletRecordDao.getPaymentDetail(orderNo);
    }
    
    /**
     * 用户详情积分记录
     * @param recordVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    
    
    @Override
    public List<RecordVo> getUserRecordVoList(RecordVo recordVo, int pageNo, int pageSize) {
    	
        return walletRecordDao.getUserRecordVoList(recordVo,pageNo,pageSize);
    }
    
    @Override
    public Integer getUserRecordVoCount(RecordVo recordVo) {
        Integer count=walletRecordDao.getUserRecordVoCount(recordVo);
        count=count==null?0:count;
        return count;
    }
    
    

}
