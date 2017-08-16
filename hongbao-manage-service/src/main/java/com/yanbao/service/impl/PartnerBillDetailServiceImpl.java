package com.yanbao.service.impl;

import com.mall.model.Parameter;
import com.mall.model.PartnerBillDetail;
import com.mall.model.Store;
import com.mall.model.User;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.PartnerBillDetailDao;
import com.yanbao.service.PartnerBillDetailService;
import com.yanbao.service.StoreService;
import com.yanbao.service.UserService;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/2.
 */
@Service
public class PartnerBillDetailServiceImpl extends CommonServiceImpl<PartnerBillDetail> implements PartnerBillDetailService{

    @Autowired
    private PartnerBillDetailDao partnerBillDetailDao;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    @Override
    protected CommonDao<PartnerBillDetail> getDao() {
        return partnerBillDetailDao;
    }

    @Override
    protected Class<PartnerBillDetail> getModelClass() {
        return PartnerBillDetail.class;
    }


    @Transactional
    @Override
    public Integer createPartnerBillDetail(String storeId, String orderNo,Double score) throws Exception {

        //获取当前商家
        Store store = storeService.readById(storeId);

        //获取当前商家的推荐人
        User storeUser = userService.readById(store.getUserId());
        User recommendUser = userService.readById(storeUser.getFirstReferrer());

        //判断是否是合伙人并且加入合伙人时间大于加入商家时间
        if (recommendUser != null || ToolUtil.isEmpty(recommendUser.getJoinPartnerTime()) || recommendUser.getJoinPartnerTime().getTime() < store.getCreateTime().getTime()) {
            //如果是合伙人并且加入合伙人时间大于加入商家时间，就新增赠送ep业绩明细记录
            //获取参数信息

            PartnerBillDetail partnerBillDetail = new PartnerBillDetail();
            partnerBillDetail.setId(UUIDUtil.getUUID());
            partnerBillDetail.setPartnerId(recommendUser.getId());
            partnerBillDetail.setUserId(storeUser.getId());
            partnerBillDetail.setUserGrade(storeUser.getGrade());
            partnerBillDetail.setUserStoreId(store.getId());
            partnerBillDetail.setType("1");
            partnerBillDetail.setOrderNo(orderNo);
            partnerBillDetail.setAmount(score);
            partnerBillDetail.setScale(PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.BALANCESCALE), 0d),100,4));
            partnerBillDetail.setUserName(recommendUser.getUserName());
            partnerBillDetail.setPhone(recommendUser.getPhone());
            partnerBillDetail.setAreaId(recommendUser.getAreaId());
            partnerBillDetail.setProvince(recommendUser.getProvince());
            partnerBillDetail.setCity(recommendUser.getCity());
            partnerBillDetail.setCountry(recommendUser.getCounty());
            partnerBillDetail.setGrade(recommendUser.getGrade());
            partnerBillDetail.setStatus(1);
            partnerBillDetail.setCreateTime(new Date());
            partnerBillDetail.setRemark("合伙人邀请商家，赠送提现业绩");
            partnerBillDetailDao.create(partnerBillDetail);
        }

        return 1;
    }

}
