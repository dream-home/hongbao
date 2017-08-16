package com.yanbao.service.impl;

import java.util.List;

import com.yanbao.constant.*;
import com.yanbao.core.model.JpushExtraModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsWinDao;
import com.mall.model.ConsumeEPRecord;
import com.mall.model.GoodsWin;
import com.mall.model.GoodsWinDetail;
import com.mall.model.Message;
import com.mall.model.User;
import com.mall.model.WalletRecord;
import com.yanbao.service.ConSumeEPRecordService;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.MessageService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRecordService;
import com.yanbao.util.JPushUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.GoodsWinVo;
import com.yanbao.vo.OrderVo;

/**
 * Created by summer on 2016-12-14:10:06;
 */
@Service
public class GoodsWinServiceImpl extends CommonServiceImpl<GoodsWin> implements GoodsWinService {

	@Autowired
	GoodsWinDao goodsWinDao;
	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;
	@Autowired
	WalletRecordService walletRecordService;
	@Autowired
	ConSumeEPRecordService consumeEPRecordService;

	@Override
	protected CommonDao<GoodsWin> getDao() {
		return goodsWinDao;
	}

	@Override
	protected Class<GoodsWin> getModelClass() {
		return GoodsWin.class;
	}

	@Override
	public void defaultStatus(GoodsWin model) {

	}

	@Override
	public double getIncome() {
		Double income = goodsWinDao.getIncome();
		income = income == null ? 0 : income;
		return income;
	}

	@Override
	public List<GoodsWinVo> readListWithTime(GoodsWinVo goodsWin, Page page) {
		return goodsWinDao.readListWithTime(goodsWin, page);
	}
	
	@Override
	public List<GoodsWinVo> comSaleList(GoodsWinVo goodsWin, Page page) {
		return goodsWinDao.comSaleList(goodsWin, page);
	}

	@Override
	public int readCountWithTime(GoodsWinVo goodsWinVo, Page page) {
		Integer count = goodsWinDao.readCountWithTime(goodsWinVo, page);
		return count == null ? 0 : count;
	}
	
	@Override
	public int readSaleCount(GoodsWinVo goodsWinVo, Page page) {
		Integer count = goodsWinDao.readSaleCount(goodsWinVo, page);
		return count == null ? 0 : count;
	}

	
	
	
	@Override
	public List<GoodsWinVo> readListWithTimeByStore(GoodsWinVo goodsWin, Page page) {
		return goodsWinDao.readListWithTimeByStore(goodsWin, page);
	}

	@Override
	public int readCountWithTimeByStore(GoodsWinVo goodsWinVo, Page page) {
		Integer count = goodsWinDao.readCountWithTimeByStore(goodsWinVo, page);
		return count == null ? 0 : count;
	}
	/**
	 * 获取购物车的商品详情
	 */
	
	@Override
	public List<GoodsWinDetail>  shoppGoodsList(String orderNo){
		return goodsWinDao.shoppGoodsList(orderNo);
	}
	
	
	/**
	 * 发货操作业务类
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public JsonResult sendGoodService(OrderVo vo, GoodsWin goodWin) throws Exception {
		
		//获取系统用户
		User systemUser = userService.readById(Constant.SYSTEM_USERID);
		
		if(systemUser == null){
			return new JsonResult(6,"系统用户不存在！");
		}
		
		// 获取下单者的对象
		User buyUser = userService.readById(goodWin.getUserId());
		if(buyUser == null){
			return new JsonResult(7,"买家用户不存在！");
		}
		
		//设置快递单号
		goodWin.setExpressNo(vo.getExpressNo());
		goodWin.setExpressName(vo.getExpressName());
		goodWin.setStatus(GoodsWinType.DELIVERED.getCode());
		this.updateById(goodWin.getId(), goodWin);

		//增加发货信息
		addSystemMessage(goodWin);
		
		//获取兑换ep
		Double ep = goodWin.getPrice();
		Double exchangeEP = goodWin.getPrice() + systemUser.getExchangeEP();
		Double consumeEP = goodWin.getPrice() + buyUser.getConsumeEP();
		
		//修改系统商家ep
		systemUser.setExchangeEP(exchangeEP);
		userService.updateEpById(systemUser);
		
		//修改买家消费额度
		buyUser.setConsumeEP(consumeEP);
		userService.updateEpById(buyUser);

		// 增加系统商家ep流水
		WalletRecord record = new WalletRecord();
		record.setId(UUIDUtil.getUUID());
		record.setUserId(systemUser.getId());
		record.setOrderNo(goodWin.getOrderNo());
		record.setScore(ep);
		record.setRecordType(RecordType.STORE_INCOME.getCode());
		record.setRemark(RecordType.STORE_INCOME.getMsg());
		walletRecordService.create(record);
		
		//增加买家用户消费ep流水表
		ConsumeEPRecord epRecord = new ConsumeEPRecord();
		epRecord.setId(UUIDUtil.getUUID());
		epRecord.setConsumeEp(ep);
		epRecord.setOrderNo(goodWin.getOrderNo());
		epRecord.setRemark(ConsumeEPType.MALL_EXCHANGE.getMsg());
		epRecord.setUserId(buyUser.getId());
		epRecord.setGrade(buyUser.getGrade());
		epRecord.setType(ConsumeEPType.MALL_EXCHANGE.getCode());
		consumeEPRecordService.create(epRecord);
		//兑换商品成功，向上累加EP销售业绩
		userService.updatePerformanceCount(buyUser.getId(), ep);
		
		// 普通用户购买成功后推送给商家，提醒发货
		boolean rs = JPushUtil.pushPayloadByid(buyUser.getRegistrationId(), "您的购买商品[" + goodWin.getGoodsName() + "]发货啦！",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.USER_ORDER));
		logger.info("推送ID:" + buyUser.getRegistrationId() + ";用户ID:" + buyUser.getUid() + ";推送结果：" + rs);
		
		return new JsonResult();
	}
	
	//发货消息
	private void addSystemMessage(GoodsWin order) throws Exception {
		// 增加发货消息
		Message message = new Message();
		message.setId(UUIDUtil.getUUID());
		message.setUserId(order.getUserId());
		message.setOrderNo(order.getOrderNo());
		message.setTitle(MessageType.SHIPPED.getMsg());
		message.setType(MessageType.SHIPPED.getCode());
		message.setDetail("您的购买商品[" + order.getGoodsName() + "]发货啦！");
		message.setRemark(MessageType.SHIPPED.getMsg());
		message.setStatus(0);
		messageService.create(message);
	}

	@Override
	public Integer count(Integer orderType) {
		return goodsWinDao.count(orderType);
	}
}
