package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.GoodsSort;
import com.yanbao.vo.GoodsSortVo;

/**
 * Created by summer on 2016-12-20:17:30;
 */
public interface GoodsSortService extends CommonService<GoodsSort> {

    List<GoodsSortVo> getGoodsSortVoListAll(GoodsSort goodsSort,Page page);
}
