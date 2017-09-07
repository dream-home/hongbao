package com.yanbao.service.impl;

import java.util.Date;
import java.util.List;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.service.*;
import com.yanbao.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.WalletDonateDao;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class WalletDonateServiceImpl implements WalletDonateService {

	@Autowired
	private WalletDonateDao walletDonateDao;
	@Autowired
	private UserService userService;
	@Autowired
	private WalletRecordService walletRecordService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private EPRecordService epRecordService;

	@Override
	public WalletDonate getByOrderNo(String orderNo, String userId) throws Exception {
		if (StringUtils.isBlank(orderNo)) {
			return null;
		}
		return walletDonateDao.getByOrderNo(orderNo, userId);
	}

	@Override
	public PageResult<WalletDonate> getPage(String userId, Page page) throws Exception {
		if (userId == null) {
			return null;
		}
		PageResult<WalletDonate> pageResult = new PageResult<WalletDonate>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = walletDonateDao.count(userId);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<WalletDonate> list = walletDonateDao.getList(userId, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(WalletDonate model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(model.getUserId());
		record.setOrderNo(model.getOrderNo());
		record.setScore(model.getScore());
		record.setRecordType(RecordType.DONATE.getCode());
		record.setRemark(RecordType.DONATE.getMsg());
		walletRecordService.add(record);
		// 增加赠送记录
		return walletDonateDao.add(model);
	}

	private void setDefaultValue(WalletDonate model) {
		model.setId(UUIDUtil.getUUID());
		model.setOrderNo(OrderNoUtil.get());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

	/**
	 * 赠送积分处理流程
	 * 
	 * @param user
	 * @param donateUser
	 * @param score
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public Boolean donateHandler(User user, User donateUser, Double score) throws Exception {
		// 修改用户积分
		userService.updateScore(user.getId(), -score);
		userService.updateScore(donateUser.getId(), score);
		// 增加赠送记录
		WalletDonate donateFrom = new WalletDonate();
		donateFrom.setUserId(user.getId());
		donateFrom.setScore(-score);
		donateFrom.setRemark(donateUser.getNickName());
		donateFrom.setDonateUserId(donateUser.getId());
		donateFrom.setDonateUid(donateUser.getUid());
		this.add(donateFrom);

		WalletDonate donateTo = new WalletDonate();
		donateTo.setUserId(donateUser.getId());
		donateTo.setScore(score);
		donateTo.setRemark(user.getNickName());
		donateTo.setDonateUserId(user.getId());
		donateTo.setDonateUid(user.getUid());
		this.add(donateTo);
		// 添加获赠消息
		Message message = new Message();
		message.setUserId(donateUser.getId());
		message.setOrderNo(donateTo.getOrderNo());
		message.setTitle("收到赠送金额");
		message.setType(MessageType.DONATE.getCode());
		message.setDetail("收到[" + user.getNickName() + "]赠送金额:" + score);
		message.setRemark(MessageType.DONATE.getMsg());
		messageService.add(message);
		// websocket
		Hash.hincrby(RedisKey.ALL_IDS.getKey(), donateUser.getId(), 1);
		Sets.sadd(RedisKey.WAITING_IDS.getKey(), donateUser.getId());
		return true;
	}

	/**
	 * 赠送Ep处理流程
	 *
	 * @param user
	 * @param donateUser
	 * @param ep
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public Boolean donateEpHandler(User user, User donateUser, Double ep) throws Exception {
		// 计算手续费
		Double poundageScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.DONATEEPPOUNDAGESCALE), 0d);
		if (poundageScale == null) {
			poundageScale = 0d;
		}
		Double poundage = PoundageUtil.getPoundage(ep, poundageScale);

		//计算扣除完手续费赠送的ep
		Double reallyEp = PoundageUtil.getPoundage(ep - poundage,1d);

		// 修改用户ep
		userService.updateEp(user.getId(), -ep);
		userService.updateEp(donateUser.getId(),reallyEp);

		//加入ep消费记录
		epRecordService.consumeEpRecord(user,-ep,OrderNoUtil.get(), EPRecordType.DONATEEP,user.getId(),donateUser.getId(),"");
		epRecordService.consumeEpRecord(user,ep,OrderNoUtil.get(), EPRecordType.DONATEEP,donateUser.getId(),user.getId(),"");

		// 增加赠送记录
		WalletDonate donateFrom = new WalletDonate();
		donateFrom.setUserId(user.getId());
		donateFrom.setScore(-ep);
		donateFrom.setRemark(donateUser.getNickName());
		donateFrom.setDonateUserId(donateUser.getId());
		donateFrom.setDonateUid(donateUser.getUid());
		this.add(donateFrom);

		WalletDonate donateTo = new WalletDonate();
		donateTo.setUserId(donateUser.getId());
		donateTo.setScore(ep);
		donateTo.setRemark(user.getNickName());
		donateTo.setDonateUserId(user.getId());
		donateTo.setDonateUid(user.getUid());
		this.add(donateTo);
		// 添加获赠消息
		Message message = new Message();
		message.setUserId(donateUser.getId());
		message.setOrderNo(donateTo.getOrderNo());
		message.setTitle("收到赠送ep");
		message.setType(MessageType.DONATE.getCode());
		message.setDetail("收到[" + user.getNickName() + "]赠送ep:" + ep);
		message.setRemark(MessageType.DONATE.getMsg());
		messageService.add(message);
		// websocket
		Hash.hincrby(RedisKey.ALL_IDS.getKey(), donateUser.getId(), 1);
		Sets.sadd(RedisKey.WAITING_IDS.getKey(), donateUser.getId());
		return true;
	}

}
