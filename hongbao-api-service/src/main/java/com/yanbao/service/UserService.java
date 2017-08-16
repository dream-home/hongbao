package com.yanbao.service;

import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsWin;
import com.mall.model.User;
import com.yanbao.vo.OrderVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserService {

	User getById(String id) throws Exception;

	User getByCondition(User model) throws Exception;

	Integer count(User condition) throws Exception;

	PageResult<User> getPage(User condition, Page page) throws Exception;

	Integer add(User user) throws Exception;

	Integer update(String id, User model) throws Exception;

	Integer updateBySignIn(String id, User model) throws Exception;

	Integer updateScore(String id, Double score) throws Exception;

	Integer updateDoudou(String id, Double doudou) throws Exception;

	Integer updateDoudou(String id, Double doudou, Date douSignTime) throws Exception;

	Integer updateEp(String id, Double exchangeEP) throws Exception;

	void updateGroupCount(String id, String groupType) throws Exception;

	Integer updateRegistrationId(String id, String registrationId) throws Exception;

	Integer updateEpById(String id, Double ep) throws Exception;

	Integer updateConsumeEPById(String id, Double consumeEP) throws Exception;

	@Transactional
	JsonResult join(JsonResult jsonResult, User user, Double ep) throws Exception;

	/**
	 * 更新下线合伙人数量
	 * 
	 * @param id
	 *            用户id
	 * @param count
	 *            正数：向上统计增加1人；负数：向上统计减少1人
	 */
	void updatePerformanceCount(String id, Double count) throws Exception;

	JsonResult sendGoodService(OrderVo vo, User user, GoodsWin order) throws Exception;

	public List<User> getInnerList(List<Integer> uidList) throws Exception;

	public void updateUserPhoneSendEp(User user, String phone, Token token) throws Exception;

	Integer updateBindEpById(String id, Double ep) throws Exception;

	Integer updateBindDoudouById(String id, Double doudou) throws Exception;

	public List<User> getUpdateList(Page page) throws Exception;

	void updateGroupIsNull(String id, String groupType) throws Exception;

	public void checkGroupNo(String id) throws Exception;

	List<User> getLeafList(Page page) throws Exception;

	Integer updateAllPerformance(String id, Double performanceOne,Double performanceTwo, Double performanceThree) throws Exception;

    void updatePerformanceOne(String userId, Double count);

	void updatePerformanceTwo(String userId, Double count);

	void updatePerformanceThree(String userId, Double count);

	double tradeRate(String orderNo, String userid, double amt) throws Exception;
	/**
	 * 线程统计60层业绩
	 *
	 * @param user
	 *            用户
	 * @param orderNo
	 *            正数：向上统计增加业绩；负数：向上统计减少业绩
	 * @param cosumeEp    消费EP值
	 */
	void   updatePermanceByThread(User user,String orderNo,Double cosumeEp) throws Exception;

	void   completeInfo(User user) throws Exception;

	public  Boolean isCompleteInfo(User user);

}
