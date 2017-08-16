package com.yanbao.dao;

import java.util.Date;
import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.User;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserDao {

	User getById(String id) throws Exception;

	User getByCondition(User model) throws Exception;

	Integer count(User condition) throws Exception;

	List<User> getList(User condition, Page page) throws Exception;

	Integer add(User model) throws Exception;

	Integer update(String id, User model) throws Exception;

	Integer updateBySignIn(String id, User model) throws Exception;

	Integer updateScore(String id, Double score) throws Exception;

	Integer updateDoudou(String id, Double doudou, Date douSignTime) throws Exception;

	Integer updateEp(String id, Double exchangeEP) throws Exception;

	Integer updateGroupChildCountA(String id) throws Exception;

	Integer updateGroupChildCountB(String id) throws Exception;

	Integer updateGroupChildCountC(String id) throws Exception;
	
	Integer updateGroupChildCount(String id,Integer A ,Integer B,Integer C) throws Exception;

	Integer updateRegistrationId(String id, String registrationId) throws Exception;

	Integer updateEpById(String id, Double ep) throws Exception;

	Integer updateConsumeEPById(String id, Double consumeEP) throws Exception;

	Integer updateAllPerformance(String id, Double performanceOne,Double performanceTwo, Double performanceThree) throws Exception;

	Integer updatePerformanceOne(String id, Double count);

	Integer updatePerformanceTwo(String id, Double count);

	Integer updatePerformanceThree(String id, Double count);

	String getMinGroupType(String firstreferrer);

	List<User> getInnerList(List<Integer> uidList) throws Exception;

	Integer updateBindEpById(String id, Double ep) throws Exception;

	Integer updateBindDoudouById(String id, Double doudou) throws Exception;

	List<User> getUpdateList(Page page) throws Exception;

	List<User> getLeafList(Page page) throws Exception;

	Integer batchUpdatePerformanceOne(List<String> listA, Double epCount);

	Integer batchUpdatePerformanceTwo(List<String> listB, Double epCount);

	Integer batchUpdatePerformanceThree(List<String> listC, Double epCount);
}
