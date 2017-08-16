package com.yanbao.service;

import com.mall.model.EpRecord;
import com.mall.model.WalletExchange;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * ep相关接口
 * Created by Administrator on 2017/6/20.
 */
public interface EpRecordService extends CommonService<EpRecord> {

    /**
     * EP赠送记录列表(加盟赠送，代理转入，转赠代理，转赠会员)
     * @param page
     * @return
     * @throws Exception
     */
    List<EpRecord> getSendRecordList(String userId,Page page) throws Exception;

    /**
     * 计算ep赠送记录列表数量
     * @return
     * @throws Exception
     */
    Integer countSendRecordList(String userId) throws Exception;

    /**
     * 获取商家ep业绩记录
     * @param starttime
     * @param endtime
     * @return
     * @throws Exception
     */
    public List<StoreExchangeRecord> getEpList(Date starttime, Date endtime) throws Exception;
    /**
	 * 根据商家ID查询ep记录
	 * @param storeIds
	 * @return
	 */
    List<EpRecord> getByStoreIds(List<String> storeIds);

}
