package com.yanbao.dao.impl;

import com.mall.model.PartnerBillDetail;
import com.yanbao.core.page.Page;
import com.yanbao.dao.PartnerBillDetailDao;
import com.yanbao.mapper.PartnerBillDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */
@Repository
public class PartnerBillDetailDaoImpl implements PartnerBillDetailDao {

    @Autowired
    private PartnerBillDetailMapper partnerBillDetailMapper;

    @Override
    public List<PartnerBillDetail> readList(PartnerBillDetail model, Page page) throws Exception {
        return partnerBillDetailMapper.readList(model,page.getStartRow(),page.getPageSize());
    }

    @Override
    public Integer readCount(PartnerBillDetail model) throws Exception {
        return partnerBillDetailMapper.readCount(model);
    }

    @Override
    public Integer create(PartnerBillDetail model) throws Exception {
        return partnerBillDetailMapper.create(model);
    }

    @Override
    public Integer updateById(String id, PartnerBillDetail model) throws Exception {
        return partnerBillDetailMapper.updateById(id,model);
    }

    @Override
    public PartnerBillDetail readById(String id) throws Exception {
        return partnerBillDetailMapper.readById(id);
    }

    @Override
    public Double deleteById(String id) throws Exception {
        return partnerBillDetailMapper.deleteById(id);
    }
}
