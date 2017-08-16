package com.yanbao.dao;

import java.util.List;

import com.mall.model.UserAddress;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserAddressDao {

	List<UserAddress> getList(String userId) throws Exception;

	Integer add(UserAddress model) throws Exception;

	Integer update(String id, UserAddress model) throws Exception;

	Integer updateDefaultAddr(String userId) throws Exception;

}
