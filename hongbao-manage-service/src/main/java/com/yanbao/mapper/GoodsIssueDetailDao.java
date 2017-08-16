package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.vo.GoodsIssueDetailVo;

/**
 * Created by summer on 2016-12-22:14:27;
 */
public interface GoodsIssueDetailDao extends CommonDao<GoodsIssueDetail> {

    List<GoodsIssueDetailVo> getGoodsIssueDetailVoList(@Param("model") GoodsIssueDetailVo goodsIssueDetailVo,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    Integer getGoodsIssueDetailVoCount(@Param("model") GoodsIssueDetailVo goodsIssueDetailVo);
}
