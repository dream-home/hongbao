package com.yanbao.controller;

import com.mall.model.Message;
import com.mall.model.Parameter;
import com.yanbao.constant.MessageType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;
import com.yanbao.service.*;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private GoodsIssueDetailService goodsIssueDetailService;
	@Autowired
	private GoodsWinService goodsWinService;
	@Autowired
	private WalletRechargeService walletRechargeService;
	@Autowired
	private WalletExchangeService walletExchangeService;
	@Autowired
	private WalletDonateService walletDonateService;
	@Autowired
	private EmailService emailService;
	
	

	/**
	 * 消息未读统计
	 */
	@ResponseBody
	@RequestMapping(value = "/unread", method = RequestMethod.GET)
	public JsonResult countUnread(HttpServletRequest request, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		String userId = token.getId();
		Integer total = 0;
		Map<String, Object> result = new HashMap<String, Object>();
		// 系统消息未读统计
		String[] systemTypes = { "0", "4", "5", "6", "7","9"};
		Integer system = messageService.countUnread(userId, systemTypes);
		result.put("system", system);
		total += system;
		// 消费消息
		String[] expenseTypes = { "1", "3", "10" ,"11","13","14" ,"16"};
		Integer expense = messageService.countUnread(userId, expenseTypes);
		result.put("expense", expense);
		total += expense;
		// 中拍消息
		String[] winTypes = { "2" };
		Integer win = messageService.countUnread(userId, winTypes);
		result.put("win", win);
		total += win;
		Hash.hset(RedisKey.ALL_IDS.getKey(), token.getId(), total.toString());

		// 统计全部系统消息
		Integer allSystem = messageService.countAllread(userId,systemTypes);
		result.put("allSystem", allSystem);
		// 统计全部消费消息
		Integer allExpense = messageService.countAllread(userId,expenseTypes);
		result.put("allExpense", allExpense);
		// 统计全部系统消息
		Integer allWin = messageService.countAllread(userId,winTypes);
		result.put("allWin", allWin);
		return new JsonResult(result);
	}

	/**
	 * 消息列表
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public JsonResult getPage(HttpServletRequest request, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		String[] types = request.getParameterValues("type");
		PageResult<Message> result = messageService.getPage(token.getId(), types, page);
		return new JsonResult(result);
	}
	
	
	/**
	 * 邮箱推送
	 */
	@ResponseBody
	@RequestMapping(value = "/notice", method = RequestMethod.GET)
	public JsonResult getNotice(HttpServletRequest request, Page page) throws Exception {

		emailService.sendEmail("邮件推送", "邮件", ParamUtil.getIstance().get(Parameter.MAILPEOPLE));
		return new JsonResult();
	}
	
	
	

	/**
	 * 消息详情
	 */
	@ResponseBody
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JsonResult getDetail(HttpServletRequest request, String id) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Message message = messageService.getById(id);
		// 更新消息为已读
		if (StatusType.TRUE.getCode() != message.getStatus()) {
			message.setStatus(StatusType.TRUE.getCode());
			messageService.update(id, message);
			// websocket
			Hash.hincrby(RedisKey.ALL_IDS.getKey(), token.getId(), -1);
			Sets.sadd(RedisKey.WAITING_IDS.getKey(), token.getId());
		}
		return new JsonResult(message);
	}

	/**
	 * 消息详情
	 */
	@ResponseBody
	@RequestMapping(value = "/detail/orderNo", method = RequestMethod.GET)
	public JsonResult getOrderDetail(HttpServletRequest request, String orderNo, Integer type, String id) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Object result = null;
		if (MessageType.EXPENSE.getCode() == type) {
			result = goodsIssueDetailService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.WIN.getCode() == type) {
			result = goodsIssueDetailService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.PURCHASE.getCode() == type) {
			result = goodsWinService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.RECHARGE.getCode() == type) {
			result = walletRechargeService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.EXCHANGE.getCode() == type) {
			result = walletExchangeService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.DONATE.getCode() == type) {
			result = walletDonateService.getByOrderNo(orderNo, token.getId());

		} else if (MessageType.SHIPPED.getCode() == type) {
			result = goodsWinService.getByOrderNo(orderNo, token.getId());
		}else if (MessageType.EXCHANGEEP.getCode() == type) {
			result = goodsWinService.getByOrderNo(orderNo, token.getId());
		}
		// 更新消息为已读
		Message message = messageService.getById(id);
		if (message != null && StatusType.TRUE.getCode() != message.getStatus()) {
			message.setStatus(StatusType.TRUE.getCode());
			messageService.update(id, message);
			// websocket
			Hash.hincrby(RedisKey.ALL_IDS.getKey(), token.getId(), -1);
			Sets.sadd(RedisKey.WAITING_IDS.getKey(), token.getId());
		}
		return new JsonResult(result);
	}

}
