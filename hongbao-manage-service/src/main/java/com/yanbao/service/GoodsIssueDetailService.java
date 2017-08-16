package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.vo.GoodsIssueDetailVo;

/**
 * Created by summer on 2016-12-22:14:28;
 */
public interface GoodsIssueDetailService extends CommonService<GoodsIssueDetail> {

    List<GoodsIssueDetailVo> getGoodsIssueDetailVoList(GoodsIssueDetailVo goodsIssueDetailVo,int pageNo,int pageSize);

    int getGoodsIssueDetailVoCount(GoodsIssueDetailVo goodsIssueDetailVo);
}
