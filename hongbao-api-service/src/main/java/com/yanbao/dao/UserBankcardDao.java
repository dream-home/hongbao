package com.yanbao.dao;

import java.util.List;

import com.mall.model.UserBankcard;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserBankcardDao {

	List<UserBankcard> getList(String userId) throws Exception;

	Integer add(UserBankcard model) throws Exception;

	Integer update(String id, UserBankcard model) throws Exception;

}
