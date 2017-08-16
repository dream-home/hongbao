package com.yanbao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.WalletRecord;
import com.yanbao.vo.RecordVo;
import com.yanbao.vo.WalletRecordVo;

/**
 * Created by summer on 2016-12-09:10:14;
 */
public interface WalletRecordDao extends CommonDao<WalletRecord> {

    List<Map> getScoreCoutByType();

    List<RecordVo> getRecordVoList(@Param("model")RecordVo recordVo, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);
    List<WalletRecordVo> getRecordVoList1(@Param("model")WalletRecordVo walletRecordVo, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);
    
    
    Integer getRecordVoCount(@Param("model") RecordVo recordVo);
    Integer getRecordVoCount1(@Param("model") WalletRecordVo walletRecordVo);

    //根据类型 获得一些费用总和
    Double getSomeFee(@Param("model") WalletRecord walletRecord);

    //获取扫码支付订单详情
    List<WalletRecordVo> getPaymentDetail(@Param("orderNo") String orderNo);
    
    //用户详情积分记录
    List<RecordVo> getUserRecordVoList(@Param("model")RecordVo recordVo, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);
    //用户详情积分记录
    Integer getUserRecordVoCount(@Param("model") RecordVo recordVo);

}
