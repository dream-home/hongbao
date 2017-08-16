package com.yanbao.service.impl;

import com.yanbao.constant.GoodsWinType;
import com.yanbao.constant.MessageType;
import com.yanbao.constant.RecordType;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.GoodsWinDao;
import com.mall.model.GoodsWin;
import com.mall.model.Message;
import com.mall.model.User;
import com.mall.model.WalletRecord;
import com.yanbao.service.*;
import com.yanbao.util.JPushUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.GoodsSalesVo;
import com.yanbao.vo.TopVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class GoodsWinServiceImpl implements GoodsWinService {

	private static final Integer SYSTEM_USER_UID = 200000;
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsWinDao goodsWinDao;
	@Autowired
	private UserService userService;
	@Autowired
	private WalletRecordService walletRecordService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private GoodsWinDetailService goodsWinDetailService;

	@Override
	public List<TopVo> getTopList(Integer num) throws Exception {
		if (num == null || num < 1) {
			return null;
		}
		return goodsWinDao.getTopList(num);
	}

	@Override
	public GoodsWin getByOrderNo(String orderNo, String userId) throws Exception {
		if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(userId)) {
			return null;
		}
		return goodsWinDao.getByOrderNo(orderNo, userId);
	}

	@Override
	public GoodsWin getUserOrderByOrderNo(String orderNo) throws Exception {
		if (StringUtils.isBlank(orderNo)) {
			return null;
		}
		return goodsWinDao.getUserOrderByOrderNo(orderNo);
	}


	@Override
	public GoodsWin getStoreOrderByOrderNo(String orderNo, String storeId) throws Exception {
		if (StringUtils.isBlank(orderNo)) {
			return null;
		}
		GoodsWin goodsWin =goodsWinDao.getStoreOrderByOrderNo(orderNo, storeId);
		return goodsWin;
	}

	@Override
	public PageResult<GoodsWin> getPage(String userId, Integer status, Page page,Integer orderType,String storeId) throws Exception {
		PageResult<GoodsWin> pageResult = new PageResult<GoodsWin>();
		BeanUtils.copyProperties(pageResult, page);
		Integer count = goodsWinDao.count(userId, status,orderType,storeId);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<GoodsWin> list = goodsWinDao.getList(userId, status, page, orderType,storeId);
			if (CollectionUtils.isEmpty(list)) {
				list=Collections.EMPTY_LIST;
			}
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(GoodsWin model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return goodsWinDao.add(model);
	}

	@Override
	public Integer update(String id, GoodsWin model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return goodsWinDao.update(id, model);
	}

	private void setDefaultValue(GoodsWin model) {
		if(StringUtils.isBlank(model.getId()) || model.getId() == null){
			model.setId(UUIDUtil.getUUID());
		}

		model.setCreateTime(new Date());
		if (model.getNum()==null || model.getNum()==0) {
			model.setNum(1);
		}
	}

	@Override
	public PageResult<GoodsWin> getStoreOrderPage(String storeId, Integer status, Page page) throws Exception {
		if (storeId == null) {
			return null;
		}
		PageResult<GoodsWin> pageResult = new PageResult<GoodsWin>();
		BeanUtils.copyProperties(pageResult, page);
		Integer count = goodsWinDao.countStoreOrder(storeId, status);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<GoodsWin> list = goodsWinDao.getStoreOrderList(storeId, status, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	@Transactional
	public Boolean saleHandler(User user, GoodsWin model) throws Exception {
		// 修改用户积分
		userService.updateScore(user.getId(), model.getScore());
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(model.getUserId());
		record.setOrderNo(model.getOrderNo());
		record.setScore(model.getScore());
		record.setRecordType(RecordType.SALE.getCode());
		record.setRemark(RecordType.SALE.getMsg());
		walletRecordService.add(record);
		// 更新订单状态
		model.setStatus(GoodsWinType.SALED.getCode());
		model.setRemark("委托出售");
		this.update(model.getId(), model);
		return true;
	}

	@Override
	@Transactional
	public Boolean buyHandler(User user, GoodsWin model) throws Exception {
		// 修改用户积分
		userService.updateScore(user.getId(), -(model.getPrice() - model.getScore()));
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(model.getUserId());
		record.setOrderNo(model.getOrderNo());
		record.setScore(model.getScore() - model.getPrice());
		record.setRecordType(RecordType.BUY.getCode());
		record.setRemark(RecordType.BUY.getMsg());
		walletRecordService.add(record);
		// 更新订单状态
		model.setStatus(GoodsWinType.BUYED.getCode());
		model.setRemark("积分购买");
		this.update(model.getId(), model);

		if (StringUtils.isNotBlank(model.getStoreId())) {
			// 增加商铺积分
			User cond = new User();
			cond.setStoreId(model.getStoreId());
			User storeUser = userService.getByCondition(cond);
			if (storeUser != null) {
				
				/*
				 *积分购买原有逻辑是：购买成功即给商家加积分  现改为：发货时处理
				 * userService.updateScore(storeUser.getId(), model.getPrice());
				// 增加积分流水
				WalletRecord record2 = new WalletRecord();
				record2.setUserId(storeUser.getId());
				record2.setOrderNo(model.getOrderNo());
				record2.setScore(model.getPrice());
				record2.setRecordType(RecordType.STORE_INCOME.getCode());
				record2.setRemark(RecordType.STORE_INCOME.getMsg());
				walletRecordService.add(record2);*/
				
				//新增商家卖商品消息
				Message scoreMessage = new Message();
				scoreMessage.setUserId(storeUser.getId());
				scoreMessage.setOrderNo(model.getOrderNo());
				scoreMessage.setTitle(MessageType.ORDER.getMsg());
				scoreMessage.setType(MessageType.ORDER.getCode());
				scoreMessage.setDetail("有人购买了你的商品："+model.getGoodsName()+",请及时发货!");
				scoreMessage.setRemark("积分购买");
				messageService.add(scoreMessage);
				
				//新增用户消费消息
				Message userMessage = new Message();
				userMessage.setUserId(model.getUserId());
				userMessage.setOrderNo(model.getOrderNo());
				userMessage.setTitle(MessageType.EXPENSE.getMsg());
				userMessage.setType(MessageType.EXPENSE.getCode());
				userMessage.setDetail(model.getPrice()+"");
				userMessage.setRemark("积分购买");
				messageService.add(userMessage);
				
				//普通用户购买成功后推送给商家，提醒发货
				JPushUtil.pushPayloadByid(storeUser.getRegistrationId(),"您有新的商品需要发货，请尽快处理",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.STORE_ORDER));
			}
		} else {
			// 增加系统积分
			User cond = new User();
			cond.setUid(SYSTEM_USER_UID);
			User systemUser = userService.getByCondition(cond);
			if (systemUser != null) {
				userService.updateScore(systemUser.getId(), model.getPrice());
				// 增加积分流水
				WalletRecord record2 = new WalletRecord();
				record2.setUserId(systemUser.getId());
				record2.setOrderNo(model.getOrderNo());
				record2.setScore(model.getPrice());
				record2.setRecordType(RecordType.STORE_INCOME.getCode());
				record2.setRemark(RecordType.STORE_INCOME.getMsg());
				walletRecordService.add(record2);
			}
		}
		return true;
	}

	@Override
	public Integer countStoreSales(String storeId) throws Exception {
		if (StringUtils.isBlank(storeId)) {
			return 0;
		}
		return goodsWinDao.countStoreSales(storeId);
	}

	@Override
	public List<GoodsSalesVo> countStoreGoodsSales(String storeId) throws Exception {
		if (StringUtils.isBlank(storeId)) {
			return null;
		}
		return goodsWinDao.countStoreGoodsSales(storeId);
	}

	@Override
	public Integer count(String userId,Integer status) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		User user = userService.getById(userId);
		return goodsWinDao.countStoreOrder(user.getStoreId(), status);
	}

	@Override
	public GoodsWin getById(String goodsWinId) throws Exception {
		if (StringUtils.isBlank(goodsWinId)) {
			return null;
		}
		return goodsWinDao.getById(goodsWinId);
	}

	@Override
	public Integer count(String userId, Integer status, Integer orderType, String storeId) throws Exception {
		Integer count = goodsWinDao.count(userId,status,orderType,storeId);
		if (count==null){
			count=0;
		}
		return count;
	}

	@Override
	public Double sumUserBuyAmt(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		return goodsWinDao.sumUserBuyAmt(userId);
	}
}
