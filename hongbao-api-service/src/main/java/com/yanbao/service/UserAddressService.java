package com.yanbao.service;

import java.util.List;

import com.mall.model.UserAddress;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserAddressService {

	List<UserAddress> getList(String userId) throws Exception;

	Integer add(UserAddress model) throws Exception;

	Integer update(String id, UserAddress model) throws Exception;

	void updateDefaultAddr(String userId,String addrId) throws Exception;

	@Transactional
	void handleUserAddress(UserAddress vo) throws Exception;
}
