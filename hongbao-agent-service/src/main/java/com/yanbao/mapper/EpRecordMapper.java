package com.yanbao.mapper;

import com.mall.model.EpRecord;
import com.mall.model.WalletExchange;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * ep相关接口
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface EpRecordMapper extends CommonDao<EpRecord> {

    /**
     * EP赠送记录列表(加盟赠送，代理转入，转赠代理，转赠会员)
     * @param page
     * @return
     * @throws Exception
     */
    List<EpRecord> getSendRecordList(@Param("userId") String userId,@Param("page")Page page) throws Exception;

    /**
     * 计算ep赠送记录列表数量
     * @return
     * @throws Exception
     */
    Integer countSendRecordList(@Param("userId") String userId) throws Exception;

    /**
     * 获取商家ep业绩记录
     * @param starttime
     * @param endtime
     * @return
     * @throws Exception
     */
    List<StoreExchangeRecord> getEpList(@Param("starttime") Date starttime,@Param("endtime") Date endtime) throws Exception;
    /**
	 * 根据商家ID查询ep记录
	 * @param storeIds
	 * @return
	 */
    List<EpRecord> getByStoreIds(@Param("storeIds")List<String> storeIds);
}
