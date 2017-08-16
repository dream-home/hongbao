package com.yanbao.service;

import java.util.List;

import com.mall.model.Goods;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;


/**
 * Created by summer on 2016-12-08:15:24;
 */
public interface GoodsService extends CommonService<Goods> {
	 List<Goods> readListWithTime(Goods goods, Page page);
	 void proDown(Goods goods);
	 List<Goods> readByStoreId(List<String> storeIds,Integer startRow,Integer pageSize);
}
