package com.yanbao.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.service.CommonService;
import com.mall.model.WalletRecord;
import com.yanbao.vo.RecordVo;
import com.yanbao.vo.WalletRecordVo;

/**
 * Created by summer on 2016-12-09:10:25;
 */
public interface WalletRecordService extends CommonService<WalletRecord> {

    List<Map> getScoreCoutByType();

    /**
     * 获取记录流水
     */
    List<RecordVo> getRecordVoList(RecordVo recordVo,int pageNo,int pageSize);

    
    List<WalletRecordVo> getRecordVoList1(WalletRecordVo walletRecordVo,int pageNo,int pageSize);	
    
    Integer getRecordVoCount(RecordVo recordVo);
    Integer getRecordVoCount1(WalletRecordVo walletRecordVo);

    //根据类型 获取一些系统费用总和
    double getSomeFee(WalletRecord walletRecord);

    //获取扫码支付订单详情
    List<WalletRecordVo> getPaymentDetail(String orderNo);
    
    
    //用户详情积分记录
    List<RecordVo> getUserRecordVoList(RecordVo recordVo,int pageNo,int pageSize);
    //用户详情积分记录
    Integer getUserRecordVoCount(RecordVo recordVo);

    

}
