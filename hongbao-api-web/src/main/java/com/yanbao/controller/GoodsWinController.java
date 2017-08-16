package com.yanbao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yanbao.constant.OrderType;
import com.mall.model.Goods;
import com.yanbao.service.*;
import com.yanbao.vo.OrderVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.GoodsWinType;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsIssue;
import com.mall.model.GoodsWin;
import com.mall.model.User;
import com.yanbao.util.FormatUtils;
import com.yanbao.util.Md5Util;
import com.yanbao.util.TokenUtil;
import com.yanbao.vo.GoodsWinVo;

@Controller
@RequestMapping("/user/goods/win")
public class GoodsWinController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsWinController.class);

	@Autowired
	private UserService userService;
	@Autowired
	GoodsWinService goodsWinService;
	@Autowired
	private UserAddressService userAddressService;
	@Autowired
	GoodsIssueDetailService goodsIssueDetailService;
	@Autowired
	GoodsIssueService goodsIssueService;
	@Autowired
	GoodsService goodsService;

	/**
	 * 我的竞拍中奖列表
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public JsonResult getPage(HttpServletRequest request, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		PageResult<GoodsWin> result = goodsWinService.getPage(token.getId(), GoodsWinType.PENDING.getCode(), page,0,null);
		return new JsonResult(result);
	}

	/**
	 * 我的竞拍-委托出售
	 */
	@ResponseBody
	@RequestMapping(value = "/sale", method = RequestMethod.POST)
	public JsonResult sale(HttpServletRequest request, @RequestBody OrderVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		GoodsWin goodsWin = goodsWinService.getByOrderNo(vo.getOrderNo(), token.getId());
		if (goodsWin == null) {
			return new JsonResult(0, "订单号不存在");
		}
		if (GoodsWinType.PENDING.getCode() != goodsWin.getStatus()) {
			return new JsonResult(1, "订单已出售或购买");
		}
		if (StatusType.FALSE.getCode() == goodsWin.getSaleSwitch()) {
			return new JsonResult(2, "此商品本期禁止委托出售");
		}
		User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		goodsWinService.saleHandler(user, goodsWin);
		// 操作成功返回用户当前积分
		Map<String, Object> result = new HashMap<String, Object>();
		user = userService.getById(token.getId());
		result.put("score", FormatUtils.formatDouble(user.getScore()));
		return new JsonResult(result);
	}

	/**
	 * 我的竞拍-积分购买
	 */
	@ResponseBody
	@RequestMapping(value = "/buy", method = RequestMethod.POST)
	public JsonResult buy(HttpServletRequest request, @RequestBody OrderVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		GoodsWin goodsWin = goodsWinService.getByOrderNo(vo.getOrderNo(), token.getId());
		if (goodsWin == null) {
			return new JsonResult(0, "订单号不存在");
		}
		if (GoodsWinType.PENDING.getCode() != goodsWin.getStatus()) {
			return new JsonResult(1, "订单已出售或购买");
		}
		User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(user.getPayPwd())) {
			return new JsonResult(2, "请先设置支付密码");
		}
		if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
			return new JsonResult(3, "支付密码不正确");
		}
		if (user.getScore() == null || user.getScore() < (goodsWin.getPrice() - goodsWin.getScore())) {
			return new JsonResult(4, "您的积分不足");
		}
		if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPhone()) || StringUtils.isBlank(vo.getAddr())) {
			return new JsonResult(10, "请完善收货信息");
		}
		// 设置收货信息
		goodsWin.setUserName(vo.getUserName());
		goodsWin.setPhone(vo.getPhone());
		goodsWin.setAddr(vo.getAddr());
		goodsWinService.buyHandler(user, goodsWin);
		// 操作成功返回用户当前积分
		Map<String, Object> result = new HashMap<String, Object>();
		user = userService.getById(token.getId());
		result.put("score", FormatUtils.formatDouble(user.getScore()));
		return new JsonResult(result);
	}

	/**
	 * 我的发货订单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/buy/page", method = RequestMethod.GET)
	public JsonResult getBuyPage(HttpServletRequest request, Integer status, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (status != null && GoodsWinType.BUYED.getCode() != status && GoodsWinType.DELIVERED.getCode() != status) {
			return new JsonResult(0, "参数非法");
		}
		PageResult<GoodsWin> result = goodsWinService.getPage(token.getId(), status, page,null,null);
		PageResult<GoodsWinVo> voPage = new PageResult<GoodsWinVo>();
		List<GoodsWinVo> voList = new ArrayList<>();
		for (GoodsWin goodsWin : result.getRows()) {
			GoodsWinVo vo = new GoodsWinVo();
			BeanUtils.copyProperties(vo, goodsWin);
			if(goodsWin.getOrderType().equals(OrderType.PURCHASE.getCode())){
				Goods goods = goodsService.getById(goodsWin.getGoodsId());
				if (goods != null && goods.getBusinessSendEp() != null) {
					vo.setBusinessSendEp(goods.getBusinessSendEp());
				} else {
					vo.setBusinessSendEp(0.0d);
				}
			}else{
				GoodsIssue goodsIssue = goodsIssueService.getById(goodsWin.getIssueId());
				if (goodsIssue != null && goodsIssue.getBusinessSendEp() != null) {
					vo.setBusinessSendEp(goodsIssue.getBusinessSendEp());
				} else {
					vo.setBusinessSendEp(0.0d);
				}
			}
			voList.add(vo);
		}
		voPage.setRows(voList);
		voPage.setPageNo(result.getPageNo());
		voPage.setPageSize(result.getPageSize());
		voPage.setTotalSize(result.getTotalSize());
		return new JsonResult(voPage);
	}

	/**
	 * 我的订单数量
	 */
	@ResponseBody
	@RequestMapping(value = "/orderNum", method = RequestMethod.GET)
	public JsonResult getorderNum(HttpServletRequest request, Integer status) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (status != null && GoodsWinType.BUYED.getCode() != status && GoodsWinType.DELIVERED.getCode() != status) {
			return new JsonResult(0, "参数非法");
		}
		Integer orderNum = goodsWinService.count(token.getId(),status);
		Map<String,Integer> rs = new HashMap<String,Integer>();
		rs.put("orderNum",orderNum);
		return new JsonResult(rs);
	}
}

