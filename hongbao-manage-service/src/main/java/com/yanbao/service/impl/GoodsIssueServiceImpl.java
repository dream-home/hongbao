package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsIssueDao;
import com.mall.model.GoodsIssue;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.vo.GoodsIssueVo;

/**
 * Created by summer on 2016-12-13:14:12;
 */
@Service
public class GoodsIssueServiceImpl extends CommonServiceImpl<GoodsIssue> implements GoodsIssueService {

    @Autowired
    GoodsIssueDao goodsIssueDao;

    @Override
    protected CommonDao<GoodsIssue> getDao() {
        return goodsIssueDao;
    }

    @Override
    protected Class<GoodsIssue> getModelClass() {
        return GoodsIssue.class;
    }


    @Override
    public List<GoodsIssueVo> getGoodsIssueListByUserCount() {
        return goodsIssueDao.getGoodsIssueListByUserCount();
    }

}
