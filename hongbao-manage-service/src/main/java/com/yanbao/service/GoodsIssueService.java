package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.GoodsIssue;
import com.yanbao.vo.GoodsIssueVo;

/**
 * Created by summer on 2016-12-13:14:11;
 */
public interface GoodsIssueService extends CommonService<GoodsIssue> {

    List<GoodsIssueVo> getGoodsIssueListByUserCount();

}
