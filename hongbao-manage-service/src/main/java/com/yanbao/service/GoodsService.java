package com.yanbao.service;

import java.util.List;


import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.Goods;


/**
 * Created by summer on 2016-12-08:15:24;
 */
public interface GoodsService extends CommonService<Goods> {
	 List<Goods> readListWithTime(Goods goods, Page page);
	 void proDown(Goods goods);
	 int updateSort(String originalType,String newType,List<String> goodsids);
}
