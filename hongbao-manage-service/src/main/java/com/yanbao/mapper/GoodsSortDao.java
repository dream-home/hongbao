package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.GoodsSort;
import com.yanbao.vo.GoodsSortVo;


/**
 * Created by summer on 2016-12-20:17:29;
 */
public interface GoodsSortDao extends CommonDao<GoodsSort> {

    List<GoodsSortVo> getGoodsSortVoListAll(@Param("model") GoodsSort goodsSort,@Param("page") Page page);
}
