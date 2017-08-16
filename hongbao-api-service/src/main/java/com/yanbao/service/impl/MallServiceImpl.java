package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.PurchaseVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
@Service
public class MallServiceImpl implements MallService {

	private static final Logger logger = LoggerFactory.getLogger(MallServiceImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsIssueService goodsIssueService;
	@Autowired
	private GoodsIssueDetailService goodsIssueDetailService;
	@Autowired
	private GoodsWinService goodsWinService;
	@Autowired
	private WalletRecordService walletRecordService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserVipService userVipService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private EPRecordService epRecordService;

	/**
	 * 竞拍
	 */
	@Override
	@Transactional
	public void drawHandler(final User user, final Goods goods, final GoodsIssue issue) throws Exception {
		// 增加竞拍记录
		GoodsIssueDetail detail = new GoodsIssueDetail();
		setGoodsIssueDetail(user, goods, issue, detail);
		goodsIssueDetailService.add(detail);
		// 扣减用户积分
		userService.updateScore(user.getId(), -issue.getDrawPrice());
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(user.getId());
		record.setOrderNo(detail.getOrderNo());
		record.setScore(-issue.getDrawPrice());
		record.setRecordType(RecordType.DRAW.getCode());
		record.setRemark(RecordType.DRAW.getMsg());
		walletRecordService.add(record);
		// 增加消费记录
		Message message = new Message();
		message.setUserId(user.getId());
		message.setOrderNo(detail.getOrderNo());
		message.setTitle(issue.getGoodsName());
		message.setType(MessageType.EXPENSE.getCode());
		message.setDetail("-" + issue.getDrawPrice() + "积分");
		message.setRemark("斗拍消费");
		messageService.add(message);
		// 更新当前竞拍人数
		goodsIssueService.updateCurNum(issue.getId());
		GoodsIssue newIssue = goodsIssueService.getById(issue.getId());
		if (newIssue.getCurNum() >= issue.getDrawNum()) {
			if (newIssue.getStatus() == IssueType.PENDING.getCode()) {
				// 修改期数状态为待开奖
				issue.setStatus(IssueType.PROCESSING.getCode());
				goodsIssueService.update(issue.getId(), issue);
				// 达到上限，异步开奖
				finishDrawHandler(goods, issue);
			}
		}
	}

	/**
	 * 测试竞拍开奖
	 */
	@Override
	@Transactional
	public void finishDrawHandlerTest(Goods goods, GoodsIssue issue) throws Exception {
		logger.error("****************************finishDrawHandlerTest start**************************************");
		// 执行开奖
		List<GoodsIssueDetail> drawList = goodsIssueDetailService.getDrawList(issue.getId(), DrawType.PENDING.getCode());
		logger.error("****************************finishDrawHandlerTest drawList  ************************************** " + drawList.size());
		logger.error("****************************finishDrawHandlerTest drawList  ************************************** " + drawList.size());
		logger.error("****************************finishDrawHandlerTest drawList  ************************************** " + drawList.size());
		logger.error("****************************finishDrawHandlerTest drawList  ************************************** " + drawList.size());
		logger.error("****************************finishDrawHandlerTest drawList  ************************************** " + drawList.size());
		if (drawList == null) {
			return;
		}
		if (drawList.size() != issue.getDrawNum()) {
			logger.error("****************************finishDrawHandlerTest 退款  ************************************** ");
			// 参与人数与竞拍人数不匹配，多的人执行退款
			Integer size = drawList.size();
			for (int i = size - 1; i > issue.getDrawNum() - 1; i--) {
				GoodsIssueDetail detail = drawList.get(i);
				GoodsIssueDetail rollback = new GoodsIssueDetail();
				rollback.setStatus(DrawType.CANCEL.getCode());
				goodsIssueDetailService.update(detail.getId(), rollback);
				// 退回积分
				userService.updateScore(detail.getUserId(), detail.getDrawPrice());
				// 增加积分流水
				WalletRecord record = new WalletRecord();
				record.setUserId(detail.getUserId());
				record.setOrderNo(detail.getOrderNo());
				record.setScore(detail.getDrawPrice());
				record.setRecordType(RecordType.ROLLBACK.getCode());
				record.setRemark(RecordType.ROLLBACK.getMsg());
				walletRecordService.add(record);
				// 从参与列表内移除
				drawList.remove(i);
			}
		}
		logger.error("****************************finishDrawHandlerTest 开始开奖  ************************************** ");

		//获取系统设置
		ParamUtil util = ParamUtil.getIstance();
		//分销比例
		double systemPoundageScale = ToolUtil.parseDouble(util.get(Parameter.SYSTEMPOUNDAGESCALE), 0.01d);
		double firstReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.FIRSTREFERRERSCALE), 0.02d);
		double secondReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.SECONDREFERRERSCALE), 0.03d);
		double thirdReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.THIRDREFERRERSCALE), 0.04d);

		Double systemPoundage = PoundageUtil.getPoundage(issue.getPrice(), systemPoundageScale);
		Double firstReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), firstReferrerFeeScale);
		Double secondReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), secondReferrerFeeScale);
		Double thirdReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), thirdReferrerFeeScale);
		logger.error("****************************finishDrawHandlerTest 系统分销  ************************************** " + systemPoundage);
		logger.error("****************************finishDrawHandlerTest 一级分销  ************************************** " + firstReferrerFee);
		logger.error("****************************finishDrawHandlerTest 二级分销  ************************************** " + secondReferrerFee);
		logger.error("****************************finishDrawHandlerTest 三级分销  ************************************** " + thirdReferrerFee);

		//中奖比例
		double winnerScaleMin = ToolUtil.parseDouble(util.get(Parameter.WINNERSCALEMIN),0.6);
		double winnerScaleMax = ToolUtil.parseDouble(util.get(Parameter.WINNERSCALEMAX),0.8);
		double drawMinScore = ToolUtil.parseDouble(util.get(Parameter.DRAWMINSCORE),0.01);
		double vipWinScale = ToolUtil.parseDouble(util.get(Parameter.VIPWINSCALE),0.1);

		// 开奖积分=商品积分-系统费用-分销费用
		Double drawScore = issue.getPrice() - systemPoundage - ((firstReferrerFee + secondReferrerFee + thirdReferrerFee) * issue.getDrawNum());
		double[] drawResult = DrawUtil.randomScoreArr(drawScore, winnerScaleMin, winnerScaleMax, issue.getDrawNum(), drawMinScore);
		// 随机生成的大奖index
		int winnerIndex = RandomUtil.randomInt(0, issue.getDrawNum());
		// VIP升级
		List<String> ids = new ArrayList<String>();
		Map<String, Integer> idIndex = new HashMap<String, Integer>();
		for (int i = 0; i < drawList.size(); i++) {
			ids.add(drawList.get(i).getUserId());
			idIndex.put(drawList.get(i).getUserId(), i);
		}
		List<String> vipIds = userVipService.getVipList(ids);
		if (vipIds != null && vipIds.size() > 0) {
			int winIndex = LotteryUtil.getWinIndex(ids.size(), vipIds.size(), vipWinScale);
			if (winIndex < vipIds.size()) {
				winnerIndex = idIndex.get(vipIds.get(winIndex));
			} else {
				winnerIndex = winIndex;
			}
		}
		logger.error("****************************finishDrawHandlerTest 设置大奖积分奖励  ************************************** ");

		// 设置大奖积分奖励
		GoodsIssueDetail winnerDetail = drawList.get(winnerIndex);
		winnerDetail.setIsWinner(StatusType.TRUE.getCode());
		winnerDetail.setScore(drawResult[0]);
		winnerDetail.setStatus(DrawType.FINISH.getCode());
		goodsIssueDetailService.update(winnerDetail.getId(), winnerDetail);
		logger.error("****************************finishDrawHandlerTest 添加中奖消息  ************************************** ");
		// 添加中奖消息
		User winUser = userService.getById(winnerDetail.getUserId());
		addWinMessage(issue, winnerDetail, winUser);
		logger.error("****************************finishDrawHandlerTest  添加竞拍中奖记录  ************************************** ");
		// 添加竞拍中奖记录
		addGoodsWinRecord(goods, winnerDetail);
		logger.error("****************************finishDrawHandlerTest  发放系统费用  ************************************** ");
		Map<String, Double> updateMap = new HashMap<String, Double>();
		// 发放系统费用
		User cond = new User();
		cond.setUid(Constant.SYSTEM_USER_UID);
		User systemUser = userService.getByCondition(cond);
		addAwardScore(systemPoundage, systemUser, updateMap, true);
		logger.error("****************************finishDrawHandlerTest  发放分销奖励  ************************************** ");
		// 发放分销奖励
		sendReferrerFee(firstReferrerFee, secondReferrerFee, thirdReferrerFee, winUser, systemUser, updateMap);
		logger.error("****************************finishDrawHandlerTest   设置普通用户积分奖励 ************************************** ");
		// 设置普通用户积分奖励
		drawList.remove(winnerIndex);
		for (int i = 0; i < drawList.size(); i++) {
			// 设置积分奖励
			GoodsIssueDetail detail = drawList.get(i);
			detail.setIsWinner(StatusType.FALSE.getCode());
			detail.setScore(drawResult[i + 1]);
			detail.setStatus(DrawType.FINISH.getCode());
			goodsIssueDetailService.update(detail.getId(), detail);
			// 发放中奖积分
			User user = userService.getById(detail.getUserId());
			userService.updateScore(user.getId(), detail.getScore());
			// 增加积分流水
			addWalletRecord(detail);
			// 添加中奖消息
			addWinMessage(issue, detail, user);
			// 发放分销奖励
			sendReferrerFee(firstReferrerFee, secondReferrerFee, thirdReferrerFee, user, systemUser, updateMap);
		}
		logger.error("****************************finishDrawHandlerTest   批量更新用户积分  ************************************** ");
		// 批量更新用户积分
		for (Map.Entry<String, Double> entry : updateMap.entrySet()) {
			User user = userService.getById(entry.getKey());
			userService.updateScore(user.getId(), entry.getValue());
		}
		logger.error("****************************finishDrawHandlerTest  修改期数状态为已开奖  ************************************** ");
		// 修改期数状态为已开奖
		issue.setStatus(IssueType.FINISH.getCode());
		goodsIssueService.update(issue.getId(), issue);
		// 自动开展下一期竞拍
		addNewIssue(goods);
		logger.error("****************************finishDrawHandlerTest 自动开展下一期竞拍 addNewIssue**************************************");
	}

	/**
	 * 竞拍开奖
	 */
	@Transactional
	public void finishDrawHandler(Goods goods, GoodsIssue issue) throws Exception {
		String watch = Strings.get(RedisKey.WATCH_KEY.getKey() + issue.getId());
		if (StringUtils.isBlank(watch)) {
			return;
		}
		// 执行开奖
		List<GoodsIssueDetail> drawList = goodsIssueDetailService.getDrawList(issue.getId(), DrawType.PENDING.getCode());
		if (drawList == null) {
			return;
		}
		if (drawList.size() != issue.getDrawNum()) {
			// 参与人数与竞拍人数不匹配，多的人执行退款
			Integer size = drawList.size();
			for (int i = size - 1; i > issue.getDrawNum() - 1; i--) {
				GoodsIssueDetail detail = drawList.get(i);
				GoodsIssueDetail rollback = new GoodsIssueDetail();
				rollback.setStatus(DrawType.CANCEL.getCode());
				goodsIssueDetailService.update(detail.getId(), rollback);
				// 退回积分
				userService.updateScore(detail.getUserId(), detail.getDrawPrice());
				// 增加积分流水
				WalletRecord record = new WalletRecord();
				record.setUserId(detail.getUserId());
				record.setOrderNo(detail.getOrderNo());
				record.setScore(detail.getDrawPrice());
				record.setRecordType(RecordType.ROLLBACK.getCode());
				record.setRemark(RecordType.ROLLBACK.getMsg());
				walletRecordService.add(record);
				// 从参与列表内移除
				drawList.remove(i);
			}
		}
		// 删除redis计数
		Strings.del(RedisKey.WATCH_KEY.getKey() + issue.getId());
		Hash.hdel(RedisKey.WATCH_LIST.getKey() + issue.getId());
		//获取系统设置
		ParamUtil util = ParamUtil.getIstance();
		//分销比例
		double systemPoundageScale = ToolUtil.parseDouble(util.get(Parameter.SYSTEMPOUNDAGESCALE), 0.01d);
		double firstReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.FIRSTREFERRERSCALE), 0.02d);
		double secondReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.SECONDREFERRERSCALE), 0.03d);
		double thirdReferrerFeeScale = ToolUtil.parseDouble(util.get(Parameter.THIRDREFERRERSCALE), 0.04d);
		Double systemPoundage = PoundageUtil.getPoundage(issue.getPrice(), systemPoundageScale);
		Double firstReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), firstReferrerFeeScale);
		Double secondReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), secondReferrerFeeScale);
		Double thirdReferrerFee = PoundageUtil.getPoundage(issue.getDrawPrice(), thirdReferrerFeeScale);
		//中奖比例
		double winnerScaleMin = ToolUtil.parseDouble(util.get(Parameter.WINNERSCALEMIN), 0.6);
		double winnerScaleMax = ToolUtil.parseDouble(util.get(Parameter.WINNERSCALEMAX), 0.8);
		double drawMinScore = ToolUtil.parseDouble(util.get(Parameter.DRAWMINSCORE), 0.01);
		double vipWinScale = ToolUtil.parseDouble(util.get(Parameter.VIPWINSCALE), 0.1);
		// 开奖积分=商品积分-系统费用-分销费用
		Double drawScore = issue.getPrice() - systemPoundage - ((firstReferrerFee + secondReferrerFee + thirdReferrerFee) * issue.getDrawNum());
		double[] drawResult = DrawUtil.randomScoreArr(drawScore, winnerScaleMin, winnerScaleMax, issue.getDrawNum(), drawMinScore);
		// 随机生成的大奖index
		int winnerIndex = RandomUtil.randomInt(0, issue.getDrawNum());
		// VIP升级
		List<String> ids = new ArrayList<String>();
		Map<String, Integer> idIndex = new HashMap<String, Integer>();
		for (int i = 0; i < drawList.size(); i++) {
			ids.add(drawList.get(i).getUserId());
			idIndex.put(drawList.get(i).getUserId(), i);
		}
		List<String> vipIds = userVipService.getVipList(ids);
		if (vipIds != null && vipIds.size() > 0) {
			int winIndex = LotteryUtil.getWinIndex(ids.size(), vipIds.size(), vipWinScale);
			if (winIndex < vipIds.size()) {
				winnerIndex = idIndex.get(vipIds.get(winIndex));
			} else {
				winnerIndex = winIndex;
			}
		}
		// 设置大奖积分奖励
		GoodsIssueDetail winnerDetail = drawList.get(winnerIndex);
		winnerDetail.setIsWinner(StatusType.TRUE.getCode());
		winnerDetail.setScore(drawResult[0]);
		winnerDetail.setStatus(DrawType.FINISH.getCode());
		goodsIssueDetailService.update(winnerDetail.getId(), winnerDetail);
		// 添加中奖消息
		User winUser = userService.getById(winnerDetail.getUserId());
		addWinMessage(issue, winnerDetail, winUser);
		// 添加竞拍中奖记录
		addGoodsWinRecord(goods, winnerDetail);

		Map<String, Double> updateMap = new HashMap<String, Double>();
		// 发放系统费用
		User cond = new User();
		cond.setUid(Constant.SYSTEM_USER_UID);
		User systemUser = userService.getByCondition(cond);
		addAwardScore(systemPoundage, systemUser, updateMap, true);
		// 发放分销奖励
		sendReferrerFee(firstReferrerFee, secondReferrerFee, thirdReferrerFee, winUser, systemUser, updateMap);
		// 设置普通用户积分奖励
		drawList.remove(winnerIndex);
		for (int i = 0; i < drawList.size(); i++) {
			// 设置积分奖励
			GoodsIssueDetail detail = drawList.get(i);
			detail.setIsWinner(StatusType.FALSE.getCode());
			detail.setScore(drawResult[i + 1]);
			detail.setStatus(DrawType.FINISH.getCode());
			goodsIssueDetailService.update(detail.getId(), detail);
			// 发放中奖积分
			User user = userService.getById(detail.getUserId());
			userService.updateScore(user.getId(), detail.getScore());
			// 增加积分流水
			addWalletRecord(detail);
			// 添加中奖消息
			addWinMessage(issue, detail, user);
			// 发放分销奖励
			sendReferrerFee(firstReferrerFee, secondReferrerFee, thirdReferrerFee, user, systemUser, updateMap);
		}
		// 批量更新用户积分
		for (Map.Entry<String, Double> entry : updateMap.entrySet()) {
			User user = userService.getById(entry.getKey());
			userService.updateScore(user.getId(), entry.getValue());
		}
		// 修改期数状态为已开奖
		issue.setStatus(IssueType.FINISH.getCode());
		goodsIssueService.update(issue.getId(), issue);
		// 自动开展下一期竞拍
		addNewIssue(goods);
	}

	private void addGoodsWinRecord(Goods goods, GoodsIssueDetail winner) throws Exception {
		GoodsWin goodsWin = new GoodsWin();
		goodsWin.setOrderNo(winner.getOrderNo());
		goodsWin.setOrderType(OrderType.DRAW.getCode());
		goodsWin.setUserId(winner.getUserId());
		goodsWin.setGoodsId(winner.getGoodsId());
		goodsWin.setGoodsName(winner.getGoodsName());
		goodsWin.setDetail(goods.getDetail());
		goodsWin.setIcon(winner.getIcon());
		goodsWin.setIssueId(winner.getIssueId());
		goodsWin.setIssueNo(winner.getIssueNo());
		goodsWin.setPrice(winner.getPrice());
		goodsWin.setDrawPrice(winner.getDrawPrice());
		goodsWin.setScore(winner.getScore());
		goodsWin.setStoreId(goods.getStoreId());
		goodsWin.setStoreName(goods.getStoreName());
		goodsWin.setSaleSwitch(goods.getSaleSwitch());
		goodsWin.setStatus(GoodsWinType.PENDING.getCode());
		goodsWinService.add(goodsWin);
	}

	private void sendReferrerFee(Double firstReferrerFee, Double secondReferrerFee, Double thirdReferrerFee, User winUser, User systemUser, Map<String, Double> updateMap) throws Exception {
		User firstReferrer = userService.getById(winUser.getFirstReferrer());
		if (null != firstReferrer) {
			addAwardScore(firstReferrerFee, firstReferrer, updateMap, false);
		} else {
			addAwardScore(firstReferrerFee, systemUser, updateMap, false);
		}

		User secondReferrer = userService.getById(winUser.getSecondReferrer());
		if (null != secondReferrer) {
			addAwardScore(secondReferrerFee, secondReferrer, updateMap, false);
		} else {
			addAwardScore(secondReferrerFee, systemUser, updateMap, false);
		}

		User thirdReferrer = userService.getById(winUser.getThirdReferrer());
		if (null != thirdReferrer) {
			addAwardScore(thirdReferrerFee, thirdReferrer, updateMap, false);
		} else {
			addAwardScore(thirdReferrerFee, systemUser, updateMap, false);
		}
	}

	private void addAwardScore(double score, User user, Map<String, Double> updateMap, boolean isSystemFee) throws Exception {
		double curScore = updateMap.get(user.getId()) == null ? 0d : updateMap.get(user.getId());
		updateMap.put(user.getId(), curScore + score);

		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(user.getId());
		record.setOrderNo("");
		record.setScore(score);
		if (isSystemFee) {
			record.setRecordType(RecordType.SYSTEM_FEE.getCode());
			record.setRemark(RecordType.SYSTEM_FEE.getMsg());
		} else {
			record.setRecordType(RecordType.AWARD.getCode());
			record.setRemark(RecordType.AWARD.getMsg());
		}
		walletRecordService.add(record);
	}

	private void addNewIssue(Goods goods) throws Exception {
		GoodsIssue newIssue = new GoodsIssue();
		newIssue.setIssueNo(goods.getCurIssueNo() + 1);
		newIssue.setGoodsId(goods.getId());
		newIssue.setGoodsName(goods.getName());
		newIssue.setIcon(goods.getIcon());
		newIssue.setPrice(goods.getPrice());
		newIssue.setDrawPrice(goods.getDrawPrice());
		newIssue.setDrawNum(goods.getDrawNum());
		newIssue.setCurNum(0);
		newIssue.setStoreId(goods.getStoreId());
		newIssue.setStoreName(goods.getStoreName());
		newIssue.setSaleSwitch(goods.getSaleSwitch());
		newIssue.setBusinessSendEp(goods.getBusinessSendEp());
		newIssue.setFirstReferrerScale(goods.getFirstReferrerScale());
		newIssue.setSecondReferrerScale(goods.getSecondReferrerScale());
		newIssue.setThirdReferrerScale(goods.getThirdReferrerScale());
		goodsIssueService.add(newIssue);

		goods.setCurIssueId(newIssue.getId());
		goods.setCurIssueNo(newIssue.getIssueNo());
		goodsService.update(goods.getId(), goods);
	}

	private void addWinMessage(GoodsIssue issue, GoodsIssueDetail detail, User user) throws Exception {
		Message message = new Message();
		message.setUserId(user.getId());
		message.setOrderNo(detail.getOrderNo());
		message.setType(MessageType.WIN.getCode());
		message.setRemark("中拍消息");
		if (detail.getIsWinner() == StatusType.TRUE.getCode()) {
			message.setTitle(issue.getPrice() + "积分" + issue.getGoodsName());
		} else {
			message.setTitle(issue.getGoodsName() + "积分中奖");
			message.setDetail("+" + detail.getScore() + "积分");
		}
		messageService.add(message);
	}

	private void addWalletRecord(GoodsIssueDetail detail) throws Exception {
		WalletRecord record = new WalletRecord();
		record.setUserId(detail.getUserId());
		record.setOrderNo(detail.getOrderNo());
		record.setScore(detail.getScore());
		record.setRecordType(RecordType.DRAWWIN.getCode());
		record.setRemark(RecordType.DRAWWIN.getMsg());
		walletRecordService.add(record);
	}

	private void setGoodsIssueDetail(User user, Goods goods, GoodsIssue issue, GoodsIssueDetail detail) {
		detail.setUserId(user.getId());
		detail.setNickName(user.getNickName());
		detail.setIssueId(issue.getId());
		detail.setIssueNo(issue.getIssueNo());
		detail.setGoodsId(goods.getId());
		detail.setGoodsName(goods.getName());
		detail.setIcon(goods.getIcon());
		detail.setPrice(issue.getPrice());
		detail.setDrawPrice(issue.getDrawPrice());
		detail.setSaleSwitch(goods.getSaleSwitch());
	}

	private void setDefaultSetting(SysSetting setting) {
		setting.setDrawMinScore(0.01);
		setting.setWinnerScaleMin(0.6);
		setting.setWinnerScaleMax(0.8);
		setting.setSystemPoundageScale(0.01);
		setting.setFirstReferrerScale(0.02);
		setting.setSecondReferrerScale(0.03);
		setting.setThirdReferrerScale(0.04);
		setting.setVipWinScale(0.1);
	}

	/**
	 * 直接购买
	 */
	@Transactional
	public void purchaseHandler(User user, Goods goods, PurchaseVo vo) throws Exception {
		// 新增购买记录
		GoodsWin goodsWin = new GoodsWin();
		this.addGoodsPurchaseRecord(goods, user, vo, goodsWin);
		// 扣减用户积分
		userService.updateScore(user.getId(), -goods.getPrice());
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(user.getId());
		record.setOrderNo(goodsWin.getOrderNo());
		record.setScore(-goods.getPrice());
		record.setRecordType(RecordType.PURCHASE.getCode());
		record.setRemark(RecordType.PURCHASE.getMsg());
		walletRecordService.add(record);
		// 增加消费记录
		Message message = new Message();
		message.setUserId(user.getId());
		message.setOrderNo(goodsWin.getOrderNo());
		message.setTitle(goods.getName());
		message.setType(MessageType.PURCHASE.getCode());
		message.setDetail("-" + goods.getPrice() + "金额");
		message.setRemark("直接购买");
		messageService.add(message);
		// 增加商家卖商品消息
		Store store = storeService.getById(goods.getStoreId());
		Message scoreMessage = new Message();
		scoreMessage.setUserId(store.getUserId());
		scoreMessage.setOrderNo(goodsWin.getOrderNo());
		scoreMessage.setTitle(MessageType.ORDER.getMsg());
		scoreMessage.setType(MessageType.ORDER.getCode());
		scoreMessage.setDetail("有人购买了你的商品：" + goods.getName() + ",请及时发货!");
		scoreMessage.setRemark("直接购买");
		messageService.add(scoreMessage);
		// 扣减商品库存
		goodsService.updateStock(goods.getId(), -1);
		// 增加系统账号积分
		/*
		 * User cond = new User(); cond.setUid(SYSTEM_USER_UID); User systemUser
		 * = userService.getByCondition(cond); if (systemUser != null) {
		 * userService.updateScore(systemUser.getId(), goods.getPrice()); //
		 * 增加积分流水 WalletRecord record2 = new WalletRecord();
		 * record2.setUserId(systemUser.getId());
		 * record2.setOrderNo(goodsWin.getOrderNo());
		 * record2.setScore(goods.getPrice());
		 * record2.setRecordType(RecordType.STORE_INCOME.getCode());
		 * record2.setRemark(RecordType.STORE_INCOME.getMsg());
		 * walletRecordService.add(record2); }
		 */
	}

	
	
	private void addGoodsPurchaseRecord(Goods goods, User user, PurchaseVo vo, GoodsWin goodsWin) throws Exception {
		goodsWin.setOrderNo(OrderNoUtil.get());
		goodsWin.setOrderType(OrderType.PURCHASE.getCode());
		goodsWin.setUserId(user.getId());
		goodsWin.setGoodsId(goods.getId());
		goodsWin.setGoodsName(goods.getName());
		goodsWin.setDetail(goods.getDetail());
		goodsWin.setIcon(goods.getIcon());
		goodsWin.setPrice(goods.getPrice());
		goodsWin.setStatus(GoodsWinType.BUYED.getCode());
		goodsWin.setRemark(OrderType.PURCHASE.getMsg());
		// 收货信息
		goodsWin.setUserName(vo.getUserName());
		goodsWin.setPhone(vo.getPhone());
		goodsWin.setAddr(vo.getAddr());
		// 斗拍商城的商品都来自我的联盟里，现在每个商品都有商品ID了
		goodsWin.setStoreId(goods.getStoreId());
		goodsWin.setStoreName(goods.getStoreName());
		goodsWinService.add(goodsWin);
	}

	/**
	 * EP兑换
	 * 
	 */
	@Override
	@Transactional
	public void epExchangeHandler(User user, Goods goods, PurchaseVo vo) throws Exception {

		// 新增购买记录
		GoodsWin goodsWin = new GoodsWin();
		this.addEpGoodsRecord(goods, user, vo, goodsWin);
		// 扣减用户EP
		userService.updateEpById(user.getId(), -goods.getPrice()*vo.getNum());
		//EP流水记录
		epRecordService.consumeEpRecord(user,-goods.getPrice()*vo.getNum(),goodsWin.getOrderNo(), EPRecordType.MALL_EXCHANGE,user.getId(),Constant.SYSTEM_USERID,"");
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(user.getId());
		record.setOrderNo(goodsWin.getOrderNo());
		record.setScore(-goods.getPrice()*vo.getNum());
		record.setRecordType(RecordType.EXCHANGEEP.getCode());
		record.setRemark(RecordType.EXCHANGEEP.getMsg());
		walletRecordService.add(record);
		// 增加消费记录
		Message message = new Message();
		message.setUserId(user.getId());
		message.setOrderNo(goodsWin.getOrderNo());
		message.setTitle(MessageType.EXCHANGEEP.getMsg());
		message.setType(MessageType.EXCHANGEEP.getCode());
		message.setDetail("-" + goods.getPrice()*vo.getNum() + "EP");
		message.setRemark("EP兑换");
		messageService.add(message);
		// 增加商家卖商品消息
		Message scoreMessage = new Message();
		scoreMessage.setUserId(Constant.SYSTEM_USERID);
		scoreMessage.setOrderNo(goodsWin.getOrderNo());
		scoreMessage.setTitle(MessageType.EXCHANGEEP.getMsg());
		scoreMessage.setType(MessageType.SYSTEM.getCode());
		scoreMessage.setDetail("有EP兑换订单，" + goods.getName() + "需要处理,请及时发货!");
		scoreMessage.setRemark("EP兑换");
		messageService.add(scoreMessage);
		// 扣减商品库存
		goodsService.updateStock(goods.getId(), -vo.getNum());
	}
	
	private void addEpGoodsRecord(Goods goods, User user, PurchaseVo vo, GoodsWin goodsWin) throws Exception {
		goodsWin.setNum(vo.getNum());
		goodsWin.setOrderNo(OrderNoUtil.get());
		goodsWin.setOrderType(OrderType.EXCHANGEEP.getCode());
		goodsWin.setUserId(user.getId());
		goodsWin.setGoodsId(goods.getId());
		goodsWin.setGoodsName(goods.getName());
		goodsWin.setDetail(goods.getDetail());
		goodsWin.setIcon(goods.getIcon());
		goodsWin.setPrice(goods.getPrice()*vo.getNum());
		goodsWin.setStatus(GoodsWinType.BUYED.getCode());
		goodsWin.setRemark(OrderType.EXCHANGEEP.getMsg());
		// 收货信息
		goodsWin.setUserName(vo.getUserName());
		goodsWin.setPhone(vo.getPhone());
		goodsWin.setAddr(vo.getAddr());
		// 斗拍商城的商品都来自我的联盟里，现在每个商品都有商品ID了
		goodsWin.setStoreId(goods.getStoreId());
		goodsWin.setStoreName(goods.getStoreName());
		goodsWinService.add(goodsWin);
	}
}
