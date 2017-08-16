package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mall.model.Goods;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;

/**
 * Created by summer on 2016-12-08:15:27;
 */
public interface GoodsMapper extends CommonDao<Goods> {
	
	List<Goods> readListWithTime(@Param("model") Goods goods,@Param("page") Page page);
	void proDown(@Param("model") Goods goods);
	List<Goods> readByStoreId(@Param("storeIds") List<String> storeIds,@Param("startRow") Integer startRow,@Param("pageSize")Integer pageSize);
}
