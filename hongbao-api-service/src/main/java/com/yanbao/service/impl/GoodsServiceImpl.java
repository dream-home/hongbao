package com.yanbao.service.impl;

import com.mall.model.Goods;
import com.mall.model.GoodsIssue;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.GoodsDao;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.service.GoodsService;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.GoodsSearchVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsIssueService goodsIssueService;


    @Override
    public List<Goods> getRecommendList(Integer num) throws Exception {
        if (num == null || num < 1) {
            return null;
        }
        return goodsDao.getRecommendList(num);
    }

    @Override
    public Goods getById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return goodsDao.getById(id);
    }

    @Override
    public PageResult<Goods> getPage(String goodsSortId, Page page) throws Exception {
        PageResult<Goods> pageResult = new PageResult<Goods>();
        BeanUtils.copyProperties(pageResult, page);
        Integer count = goodsDao.count(goodsSortId);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<Goods> list = goodsDao.getList(goodsSortId, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }


    @Override
    public Integer add(Goods model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return goodsDao.add(model);
    }

    private void setDefaultValue(Goods model) {
        model.setId(UUIDUtil.getUUID());
        model.setCreateTime(new Date());
    }

    @Override
    public Integer update(String id, Goods model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return goodsDao.update(id, model);
    }

    @Override
    public Integer updateStock(String id, Integer stock) throws Exception {
        if (StringUtils.isBlank(id) || stock == null || stock == 0) {
            return 0;
        }
        return goodsDao.updateStock(id, stock);
    }

    @Override
    public PageResult<Goods> getStoreGoodsPage(String storeId, Integer status, Page page) throws Exception {
        if (StringUtils.isBlank(storeId)) {
            return null;
        }
        PageResult<Goods> pageResult = new PageResult<Goods>();
        BeanUtils.copyProperties(pageResult, page);

        Integer count = goodsDao.countStoreGoods(storeId, status);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<Goods> list = goodsDao.getStoreGoodsList(storeId, status, page);
            pageResult.setRows(list);
        }
        return pageResult;
    }

    @Override
    public Integer countStoreGoods(String storeId) throws Exception {
        if (StringUtils.isBlank(storeId)) {
            return null;
        }
        return goodsDao.countStoreGoods(storeId, null);
    }

    @Override
    public PageResult<Goods> getEpPage(String goodsSortId, Page page) throws Exception {
        PageResult<Goods> pageResult = new PageResult<>();
        Integer totalSize = goodsDao.countEp(goodsSortId);
        List<Goods> list = null;
        if (totalSize == null) {
            totalSize = 0;
        }
        if (totalSize != null && totalSize > 0) {
            list = goodsDao.getEpList(goodsSortId, page);
            for (Goods g : list) {
                g.setDetail("");
            }
            if (!CollectionUtils.isEmpty(list)) {
                BeanUtils.copyProperties(pageResult, page);
            } else {
                totalSize = 0;
            }
        } else {
            list = new ArrayList<Goods>();
            totalSize = 0;
        }
        pageResult.setRows(list);
        pageResult.setTotalSize(totalSize);
        return pageResult;
    }


    @Override
    @Transactional
    public void addNewIssue(Goods goods) throws Exception {
        GoodsIssue newIssue = new GoodsIssue();
        newIssue.setStoreId(goods.getStoreId());
        newIssue.setStoreName(goods.getStoreName());
        newIssue.setSaleSwitch(goods.getSaleSwitch());
        int issueNo = 1;
        if (goods.getCurIssueNo() != null && goods.getCurIssueNo() > 0) {
            issueNo = goods.getCurIssueNo();
        }
        newIssue.setIssueNo(issueNo);
        newIssue.setGoodsId(goods.getId());
        newIssue.setGoodsName(goods.getName());
        newIssue.setIcon(goods.getIcon());
        newIssue.setPrice(goods.getPrice());
        newIssue.setDrawPrice(goods.getDrawPrice());
        newIssue.setDrawNum(goods.getDrawNum());
        newIssue.setCurNum(0);
        newIssue.setFirstReferrerScale(goods.getFirstReferrerScale());
        newIssue.setSecondReferrerScale(goods.getSecondReferrerScale());
        newIssue.setThirdReferrerScale(goods.getThirdReferrerScale());
        newIssue.setBusinessSendEp(goods.getBusinessSendEp());
        goodsIssueService.add(newIssue);
        goods.setCurIssueId(newIssue.getId());
        goods.setCurIssueNo(newIssue.getIssueNo());
        this.update(goods.getId(), goods);
    }

    @Override
    public PageResult<Goods> getIndexEpListPage(Page page) throws Exception {
        PageResult<Goods> pageResult = new PageResult<>();
        List<Goods> list = goodsDao.getIndexEpList(page);
        if (!CollectionUtils.isEmpty(list)) {
            BeanUtils.copyProperties(pageResult, page);
            pageResult.setRows(list);
            pageResult.setTotalSize(list.size());
        }
        return pageResult;
    }

    @Override
    public List<Goods> getNesGoodsList(Integer maxRow) throws Exception {
        if (maxRow <= 0) {
            return null;
        }
        List<Goods> list = goodsDao.getNesGoodsList(maxRow);
        return list;
    }

    @Override
    public Integer updateSaleCount(String id, Integer sumSaleCount) throws Exception {
        if (StringUtils.isEmpty(id)) {
            return 0;
        }
        if (sumSaleCount == null || sumSaleCount < 0) {
            return 0;
        }
        return goodsDao.updateSaleCount(id, sumSaleCount);
    }

    @Override
    public PageResult<Goods> getSearchPage(GoodsSearchVo goodsSearch, Page page) throws Exception {
        PageResult<Goods> pageResult = new PageResult<Goods>();
        BeanUtils.copyProperties(pageResult, page);
        List<Goods> list = goodsDao.getSearchList(goodsSearch, page);
        if (list == null) {
            list = Collections.emptyList();
        }
        pageResult.setTotalSize(list.size());
        pageResult.setRows(list);
        return pageResult;
    }
}
