package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsIssueDetailDao;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.vo.GoodsIssueDetailVo;

/**
 * Created by summer on 2016-12-22:14:28;
 */
@Service
public class GoodsIssueDetailServiceImpl extends CommonServiceImpl<GoodsIssueDetail> implements GoodsIssueDetailService{

    @Autowired
    GoodsIssueDetailDao goodsIssueDetailDao;

    @Override
    protected CommonDao<GoodsIssueDetail> getDao() {
        return goodsIssueDetailDao;
    }

    @Override
    protected Class<GoodsIssueDetail> getModelClass() {
        return GoodsIssueDetail.class;
    }

    @Override
    public List<GoodsIssueDetailVo> getGoodsIssueDetailVoList(GoodsIssueDetailVo goodsIssueDetailVo, int pageNo, int pageSize) {
        return goodsIssueDetailDao.getGoodsIssueDetailVoList(goodsIssueDetailVo,pageNo,pageSize);
    }

    @Override
    public int getGoodsIssueDetailVoCount(GoodsIssueDetailVo goodsIssueDetailVo) {
        Integer count=goodsIssueDetailDao.getGoodsIssueDetailVoCount(goodsIssueDetailVo);
        return count==null?0:count;
    }
}
