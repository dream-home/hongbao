package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.Goods;

/**
 * Created by summer on 2016-12-08:15:27;
 */
public interface GoodsDao extends CommonDao<Goods> {
	
	List<Goods> readListWithTime(@Param("model") Goods goods,@Param("page") Page page);
	void proDown(@Param("model") Goods goods);
	int updateSort(@Param("originalType")String originalType,@Param("newType")String newType,@Param("goodsids")List<String> goodsids);
}
