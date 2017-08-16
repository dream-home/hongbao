package com.yanbao.mapper;

import java.util.List;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.GoodsIssue;
import com.yanbao.vo.GoodsIssueVo;

/**
 * Created by summer on 2016-12-13:14:10;
 */
public interface GoodsIssueDao extends CommonDao<GoodsIssue> {


    List<GoodsIssueVo> getGoodsIssueListByUserCount();

}
