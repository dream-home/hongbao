package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yanbao.constant.RedisKey;
import com.yanbao.dao.GoodsSortDao;
import com.yanbao.mapper.GoodsSortMapper;
import com.mall.model.GoodsSort;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsSortDaoImpl implements GoodsSortDao {

	@Autowired
	private GoodsSortMapper goodsSortMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<GoodsSort> getList() throws Exception {
		List<GoodsSort> list = null;
//		String temp = Strings.get(RedisKey.GOODS_SORT_LIST.getKey());
//		String temp=null;
//		if (null != temp) {
//			list = (List<GoodsSort>) JSONArray.parse(temp);
//		} else {
			list = goodsSortMapper.getList();
//		}
		return list;
	}

	@Override
	public List<GoodsSort> getEpList() throws Exception {
		List<GoodsSort> list = null;
		String temp = Strings.get(RedisKey.EPGOODS_SORT_LIST.getKey());
		if (null != temp) {
			list=JSON.parseArray(temp, GoodsSort.class);
		} else {
			list = goodsSortMapper.getEpList();
			Strings.setEx(RedisKey.EPGOODS_SORT_LIST.getKey(), RedisKey.EPGOODS_SORT_LIST.getSeconds(), JSONArray.toJSONString(list));
		}
		return list;
	}

}
