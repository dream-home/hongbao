package com.yanbao.dao.impl;

import com.mall.model.Ad;
import com.yanbao.dao.AdDao;
import com.yanbao.mapper.AdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class AdDaoImpl implements AdDao {

	@Autowired
	private AdMapper adMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Ad> getList(Ad ad) throws Exception {
		List<Ad> list = null;
	/*	String temp = Strings.get(RedisKey.HOME_AD_LIST.getKey());
		if (null != temp) {
			list = (List<Ad>) JSONArray.parse(temp);
		} else {
			list = adMapper.getList();
			Strings.setEx(RedisKey.HOME_AD_LIST.getKey(), RedisKey.HOME_AD_LIST.getSeconds(), JSONArray.toJSONString(list));
		}*/
		list = adMapper.getList(ad);
		return list;
	}

}
