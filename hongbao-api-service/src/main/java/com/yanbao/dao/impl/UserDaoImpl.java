package com.yanbao.dao.impl;

import com.yanbao.core.page.Page;
import com.yanbao.dao.UserDao;
import com.yanbao.mapper.UserMapper;
import com.mall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User getById(String id) throws Exception {
		return userMapper.getById(id);
	}

	@Override
	public User getByCondition(User model) throws Exception {
		return userMapper.getByCondition(model);
	}

	@Override
	public Integer count(User condition) throws Exception {
		return userMapper.count(condition);
	}

	@Override
	public List<User> getList(User condition, Page page) throws Exception {
		return userMapper.getList(condition, page);
	}

	@Override
	public Integer add(User user) throws Exception {
		return userMapper.add(user);
	}

	@Override
	public Integer update(String id, User model) throws Exception {
		return userMapper.update(id, model);
	}

	@Override
	public Integer updateBySignIn(String id, User model) throws Exception {
		return userMapper.updateBySignIn(id, model);
	}

	@Override
	public Integer updateScore(String id, Double score) throws Exception {
		return userMapper.updateScore(id, score);
	}

	@Override
	public Integer updateDoudou(String id, Double doudou, Date douSignTime) throws Exception {
		return userMapper.updateDoudou(id, doudou,douSignTime);
	}

	@Override
	public Integer updateEp(String id, Double exchangeEP) throws Exception {
		return userMapper.updateEp(id, exchangeEP);
	}

	@Override
	public Integer updateGroupChildCountA(String id) throws Exception {
		return userMapper.updateGroupChildCountA(id);
	}

	@Override
	public Integer updateGroupChildCountB(String id) throws Exception {
		return userMapper.updateGroupChildCountB(id);
	}

	@Override
	public Integer updateGroupChildCountC(String id) throws Exception {
		return userMapper.updateGroupChildCountC(id);
	}

	@Override
	public Integer updateRegistrationId(String id, String registrationId) throws Exception {
		return userMapper.updateRegistrationId(id, registrationId);
	}

	@Override
	public Integer updateEpById(String id, Double ep) throws Exception {
		return userMapper.updateEpById(id, ep);
	}

	@Override
	public Integer updateConsumeEPById(String id, Double consumeEP) throws Exception {
		return userMapper.updateConsumeEPById(id, consumeEP);
	}

	@Override
	public Integer updateAllPerformance(String id, Double performanceOne, Double performanceTwo, Double performanceThree) throws Exception {
		return userMapper.updateAllPerformance(id,performanceOne,performanceTwo,performanceThree);
	}

	@Override
	public Integer updatePerformanceOne(String id, Double count) {
		return userMapper.updatePerformanceOne(id, count);
	}

	@Override
	public Integer updatePerformanceTwo(String id, Double count) {
		return userMapper.updatePerformanceTwo(id, count);
	}

	@Override
	public Integer updatePerformanceThree(String id, Double count) {
		return userMapper.updatePerformanceThree(id, count);
	}

	@Override
	public String getMinGroupType(String firstreferrer) {
		if (firstreferrer == null || "".equals(firstreferrer)) {
			return "";
		}
		return userMapper.getMinGroupType(firstreferrer);
	}

	@Override
	public List<User> getInnerList(List<Integer> uidList) throws Exception {
		return userMapper.getInnerList(uidList);
	}

	@Override
	public Integer updateBindEpById(String id, Double ep) throws Exception {
		return userMapper.updateBindEpById(id, ep);
	}

	@Override
	public Integer updateBindDoudouById(String id, Double doudou) throws Exception {
		return userMapper.updateBindDoudouById(id, doudou);
	}

	@Override
	public List<User> getUpdateList(Page page) throws Exception {
		return userMapper.getUpdateList(page);
	}


	@Override
	public Integer updateGroupChildCount(String id, Integer A, Integer B, Integer C) throws Exception {
		return userMapper.updateGroupChildCount(id, A, B, C);
	}

	@Override
	public List<User> getLeafList(Page page) throws Exception {
		return userMapper.getLeafList(page);
	}

	@Override
	public Integer batchUpdatePerformanceOne(List<String> listA, Double epCount) {
		return userMapper.batchUpdatePerformanceOne(listA,epCount);
	}

	@Override
	public Integer batchUpdatePerformanceTwo(List<String> listB, Double epCount) {
		return userMapper.batchUpdatePerformanceTwo(listB,epCount);
	}

	@Override
	public Integer batchUpdatePerformanceThree(List<String> listC, Double epCount) {
		return userMapper.batchUpdatePerformanceThree(listC,epCount);
	}
}
