package com.yanbao.service.impl;

import com.mall.model.Parameter;
import com.mall.model.PartnerBillDetail;
import com.mall.model.Store;
import com.mall.model.User;
import com.yanbao.constant.StatusType;
import com.yanbao.core.page.Page;
import com.yanbao.dao.PartnerBillDetailDao;
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
import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */
@Service
public class PartnerBillDetailServiceImpl implements PartnerBillDetailService{

    @Autowired
    private PartnerBillDetailDao partnerBillDetailDao;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    @Override
    public List<PartnerBillDetail> readList(PartnerBillDetail model, Page page) throws Exception {
        return partnerBillDetailDao.readList(model,page);
    }

    @Override
    public Integer readCount(PartnerBillDetail model) throws Exception {
        return partnerBillDetailDao.readCount(model);
    }

    @Override
    public Integer create(PartnerBillDetail model) throws Exception {
        if(model == null){
            return null;
        }
        return partnerBillDetailDao.create(model);
    }

    @Override
    public Integer updateById(String id, PartnerBillDetail model) throws Exception {
        if(ToolUtil.isEmpty(id)){
            return null;
        }
        return partnerBillDetailDao.updateById(id,model);
    }

    @Override
    public PartnerBillDetail readById(String id) throws Exception {
        if(ToolUtil.isEmpty(id)){
            return null;
        }
        return partnerBillDetailDao.readById(id);
    }

    @Override
    public Double deleteById(String id) throws Exception {
        if(ToolUtil.isEmpty(id)){
            return null;
        }
        return partnerBillDetailDao.deleteById(id);
    }

    @Transactional
    @Override
    public Integer createPartnerBillDetail(String storeId, String orderNo,Double score) throws Exception {

        //获取当前商家
        Store store = storeService.getById(storeId);

        //获取当前商家的推荐人
        User storeUser = userService.getById(store.getUserId());
        User recommendUser = userService.getById(storeUser.getFirstReferrer());

        //判断是否是合伙人并且加入合伙人时间大于加入商家时间
        if (recommendUser != null && ToolUtil.isNotEmpty(recommendUser.getJoinPartnerTime()) && recommendUser.getJoinPartnerTime().getTime() < store.getCreateTime().getTime()) {
            //如果是合伙人并且加入合伙人时间大于加入商家时间，就新增赠送ep业绩明细记录
            PartnerBillDetail partnerBillDetail = new PartnerBillDetail();
            partnerBillDetail.setId(UUIDUtil.getUUID());
            partnerBillDetail.setPartnerId(recommendUser.getId());
            partnerBillDetail.setUserId(storeUser.getId());
            partnerBillDetail.setUserGrade(storeUser.getGrade());
            partnerBillDetail.setUserStoreId(store.getId());
            partnerBillDetail.setType("2");
            partnerBillDetail.setOrderNo(orderNo);
            partnerBillDetail.setAmount(score);
            partnerBillDetail.setScale(PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.EPSCALE), 0d),100,4));
            partnerBillDetail.setUserName(recommendUser.getUserName());
            partnerBillDetail.setPhone(recommendUser.getPhone());
            partnerBillDetail.setAreaId(recommendUser.getAreaId());
            partnerBillDetail.setProvince(recommendUser.getProvince());
            partnerBillDetail.setCity(recommendUser.getCity());
            partnerBillDetail.setCountry(recommendUser.getCounty());
            partnerBillDetail.setGrade(recommendUser.getGrade());
            partnerBillDetail.setStatus(StatusType.TRUE.getCode());
            partnerBillDetail.setCreateTime(new Date());
            partnerBillDetail.setRemark("合伙人邀请商家，赠送ep业绩");
            partnerBillDetailDao.create(partnerBillDetail);
        }
        return 1;
    }
}
