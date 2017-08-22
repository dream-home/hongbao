package com.yanbao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yanbao.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.DrawType;
import com.yanbao.constant.GoodsDetailType;
import com.yanbao.constant.IssueType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Goods;
import com.mall.model.GoodsDetail;
import com.mall.model.GoodsIssue;
import com.mall.model.GoodsIssueDetail;
import com.mall.model.GoodsSort;
import com.mall.model.HbSysKey;
import com.mall.model.Store;
import com.mall.model.SysFile;
import com.mall.model.SysFileLink;
import com.mall.model.User;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Strings;
import com.yanbao.service.FileLinkService;
import com.yanbao.service.FileService;
import com.yanbao.service.GoodsDetailService;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.service.GoodsService;
import com.yanbao.service.GoodsSortService;
import com.yanbao.service.HbSysKeyService;
import com.yanbao.service.MallService;
import com.yanbao.service.StoreService;
import com.yanbao.service.UserService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.QiNiuUtil;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.UUIDUtil;

@Controller
@RequestMapping("/mall")
public class MallController {

	private static final Logger logger = LoggerFactory.getLogger(MallController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsSortService goodsSortService;
	@Autowired
	private GoodsIssueService goodsIssueService;
	@Autowired
	private GoodsIssueDetailService goodsIssueDetailService;
	@Autowired
	private MallService mallService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FileLinkService fileLinkService;
	@Autowired
	private HbSysKeyService hbSysKeyService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private GoodsDetailService goodsDetailService;

	/**
	 * 商品分类
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/sorts", method = RequestMethod.GET)
	public JsonResult goodsSorts(HttpServletRequest request) throws Exception {
		List<GoodsSort> list = goodsSortService.getList();
		return new JsonResult(list);
	}

	/**
	 * 商品列表
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/page", method = RequestMethod.GET)
	public JsonResult goodsPage(HttpServletRequest request, String goodsSortId, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		PageResult<Goods> result = goodsService.getPage(goodsSortId, page);
		List<Goods> rows = result.getRows();
		if (rows == null || rows.size() < 0) {
			return new JsonResult(result);
		}
		// 查询商品当前期数竞拍人数
		List<GoodsVo> rows2 = new ArrayList<GoodsVo>();
		for (Goods model : rows) {
			GoodsVo vo = new GoodsVo();
			BeanUtils.copyProperties(vo, model);
			GoodsIssue issue = goodsIssueService.getById(model.getCurIssueId());
			if (issue != null) {
//				vo.setPrice(issue.getPrice());
				vo.setDrawPrice(issue.getDrawPrice());
				vo.setDrawNum(issue.getDrawNum());
				vo.setCurNum(issue.getCurNum());
				// 查询用户是否已参与竞拍
				if (token != null) {
					if (goodsIssueDetailService.checkDraw(issue.getId(), token.getId())) {
						vo.setIsDraw(1);
					}
				}
				rows2.add(vo);
			}
		}

		PageResult<GoodsVo> result2 = new PageResult<GoodsVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
		return new JsonResult(result2);
	}

	/**
	 * 商品详情
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/detail", method = RequestMethod.GET)
	public JsonResult goodsDetail(HttpServletRequest request, String goodsId) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		GoodsVo vo = null;
		Goods goods = goodsService.getById(goodsId);
		if (goods != null) {
			vo = new GoodsVo();
			BeanUtils.copyProperties(vo, goods);
			GoodsIssue issue = goodsIssueService.getById(goods.getCurIssueId());
			if (issue != null) {
//				vo.setPrice(issue.getPrice());
				vo.setDrawPrice(issue.getDrawPrice());
				vo.setDrawNum(issue.getDrawNum());
				vo.setCurNum(issue.getCurNum());
//				vo.setBusinessSendEp(issue.getBusinessSendEp());
				// 获取参与记录
				if (issue.getCurNum() > 0) {
					List<DrawUserVo> drawUsers = new ArrayList<DrawUserVo>();
					List<GoodsIssueDetail> drawList = goodsIssueDetailService.getDrawList(goods.getCurIssueId(), DrawType.PENDING.getCode());
					if (drawList != null && drawList.size() > 0) {
						for (GoodsIssueDetail draw : drawList) {
							User drawUser = userService.getById(draw.getUserId());
							if (drawUser != null) {
								DrawUserVo drawUserVo = new DrawUserVo();
								BeanUtils.copyProperties(drawUserVo, drawUser);
								drawUserVo.setCreateTime(draw.getCreateTime());
								drawUsers.add(drawUserVo);
							}
						}
					}
					vo.setDrawUsers(drawUsers);
				}
			}
			// 获取相关图片
			List<ImageVo> icons = new ArrayList<ImageVo>();
			List<SysFileLink> links = fileLinkService.getList(goodsId);
			if (links != null && links.size() > 0) {
				for (SysFileLink link : links) {
					SysFile file = fileService.getById(link.getFileId());
					if (file != null) {
						ImageVo icon = new ImageVo();
						icon.setId(file.getId());
						icon.setPath(file.getPath());
						icon.setIsDefault(link.getIsDefault());
						icons.add(icon);
					}
				}
			}
			vo.setIcons(icons);
			// 查询用户是否已参与竞拍
			if (token != null) {
				if (goodsIssueDetailService.checkDraw(goods.getCurIssueId(), token.getId())) {
					vo.setIsDraw(1);
				}
			}
		}
		return new JsonResult(vo);
	}

	/**
	 * 商品竞拍
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/draw", method = RequestMethod.POST)
	public JsonResult goodsDraw(HttpServletRequest request, @RequestBody DrawVo vo) throws Exception {
		final Token token = TokenUtil.getSessionUser(request);
		if (StringUtils.isBlank(vo.getGoodsId())) {
			return new JsonResult(1, "请选择竞拍商品");
		}
		final Goods goods = goodsService.getById(vo.getGoodsId());
		if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
			return new JsonResult(2, "商品不存在或已下架");
		}
		final GoodsIssue issue = goodsIssueService.getById(goods.getCurIssueId());
		if (issue == null || issue.getStatus() != IssueType.PENDING.getCode()) {
			return new JsonResult(3, "商品竞拍活动已结束");
		}
		if (issue.getCurNum() >= issue.getDrawNum()) {
			return new JsonResult(4, "本期参与人数已达上限");
		}
		if (StringUtils.isBlank(vo.getPayPwd())) {
			return new JsonResult(5, "请输入支付密码");
		}
		final User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(user.getPayPwd())) {
			return new JsonResult(6, "请先设置支付密码");
		}
		if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
			return new JsonResult(7, "支付密码不正确");
		}
		if (user.getScore() == null || user.getScore() < issue.getDrawPrice()) {
			return new JsonResult(8, "您的余额不足");
		}

		// 竞拍
		final String watchkey = RedisKey.WATCH_KEY.getKey() + issue.getId();
		final String watchlist = RedisKey.WATCH_LIST.getKey() + issue.getId();

		String watch = Strings.get(watchkey);
		if (StringUtils.isBlank(watch) && issue.getCurNum() == 0) {
			Integer curNum = issue.getCurNum();
			Strings.set(watchkey, curNum + 1 + "");
			Hash.hset(watchlist, curNum + 1 + "", token.getId());

			try {
				HbSysKey sysKey = new HbSysKey();
				sysKey.setId(issue.getId() + curNum + 1);
				sysKey.setIssId(issue.getId());
				hbSysKeyService.insert(sysKey);
			} catch (Exception e) {
				return new JsonResult(3, "商品竞拍活动已结束");
			}

			// 异步处理竞拍业务
			new Thread(new Runnable() {
				public void run() {
					try {
						mallService.drawHandler(user, goods, issue);
					} catch (Exception e) {
						logger.error("参与竞拍失败！！" + e.getMessage());
					}
				}
			}).start();
		} else {
			Map<String, String> all = Hash.hgetAll(watchlist);
			for (Map.Entry<String, String> entry : all.entrySet()) {
				if (token.getId().equals(entry.getValue())) {
					return new JsonResult(9, "您已参与了本次竞拍");
				}
			}
			String string = Strings.get(watchkey);
			if (StringUtils.isBlank(string)) {
				return new JsonResult(3, "商品竞拍活动已结束");
			}
			final Integer watchNum = Integer.parseInt(string);
			if (watchNum < issue.getDrawNum()) {
				Strings.set(watchkey, watchNum + 1 + "");
				Hash.hset(watchlist, watchNum + 1 + "", token.getId());

				try {
					HbSysKey sysKey = new HbSysKey();
					sysKey.setId(issue.getId() + watchNum + 1);
					sysKey.setIssId(issue.getId());
					hbSysKeyService.insert(sysKey);
				} catch (Exception e) {
					return new JsonResult(3, "商品竞拍活动已结束");
				}

				// 异步处理竞拍业务
				new Thread(new Runnable() {
					public void run() {
						try {
							mallService.drawHandler(user, goods, issue);
						} catch (Exception e) {
							Strings.set(watchkey, watchNum + "");
							Hash.hset(watchlist, watchNum + "", token.getId());
							logger.error("添加竞拍记录失败！！" + e.getMessage());
						}
					}
				}).start();
			} else {
				return new JsonResult(4, "本期参与人数已达上限");
			}
		}
		return new JsonResult();
	}

	/**
	 * 商品直接购买
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/purchase", method = RequestMethod.POST)
	public JsonResult goodsPurchase(HttpServletRequest request, @RequestBody PurchaseVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (StringUtils.isBlank(vo.getGoodsId())) {
			return new JsonResult(1, "请选择商品");
		}
		final Goods goods = goodsService.getById(vo.getGoodsId());
		if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
			return new JsonResult(2, "商品不存在或已下架");
		}
		if (goods == null || (goods.getStock() != null && goods.getStock() == 0)) {
			return new JsonResult(2, "商品库存不足");
		}
		if (StringUtils.isBlank(vo.getPayPwd())) {
			return new JsonResult(5, "请输入支付密码");
		}
		final User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(user.getPayPwd())) {
			return new JsonResult(6, "请先设置支付密码");
		}
		if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
			return new JsonResult(7, "支付密码不正确");
		}
		if (user.getScore() == null || user.getScore() < goods.getPrice()) {
			return new JsonResult(8, "您的余额不足");
		}
		if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPhone()) || StringUtils.isBlank(vo.getAddr())) {
			return new JsonResult(10, "请完善收货信息");
		}
		mallService.purchaseHandler(user, goods, vo);
		return new JsonResult();
	}

	/**
	 * 
	 * 商品兑换 v3.3
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/epexchange", method = RequestMethod.POST)
	public JsonResult epGoodsExchange(HttpServletRequest request, @RequestBody PurchaseVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (StringUtils.isBlank(vo.getGoodsId())) {
			return new JsonResult(1, "请选择兑换商品");
		}
		final Goods goods = goodsService.getById(vo.getGoodsId());
		if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
			return new JsonResult(2, "兑换商品不存在或已下架");
		}
		if (vo.getNum()==null) {
			vo.setNum(1);
		}
		if (vo.getNum()>goods.getStock()) {
			return new JsonResult(3, "EP商品库存不足");
		}
		if (StringUtils.isBlank(vo.getPayPwd())) {
			return new JsonResult(4, "请输入支付密码");
		}
		final User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(user.getPayPwd())) {
			return new JsonResult(5, "请先设置支付密码");
		}
		if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
			return new JsonResult(6, "支付密码不正确");
		}
		if (user.getExchangeEP() == null || user.getExchangeEP() < goods.getPrice()) {
			return new JsonResult(7, "您的EP不足");
		}
		if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPhone()) || StringUtils.isBlank(vo.getAddr())) {
			return new JsonResult(8, "请完善收货信息");
		}
		if (!org.springframework.util.StringUtils.isEmpty(Strings.get(RedisKey.EPEXCHANGE_GOODS.getKey()+token.getId()))) {
			return new JsonResult(9, "请不要重复兑换");
		}
		Strings.setEx(RedisKey.EPEXCHANGE_GOODS.getKey()+token.getId(),RedisKey.EPEXCHANGE_GOODS.getSeconds(), token.getId());
		mallService.epExchangeHandler(user, goods, vo);
		return new JsonResult();
	}

	/**
	 * 商品列表 v3.3
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	public JsonResult goodsList(HttpServletRequest request, String goodsSortId, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Integer timeSort = page.getTimeSort();
		Integer priceSort = page.getPriceSort();
	    if (timeSort==null) {
			timeSort=0;
		}
	    if (priceSort==null) {
	    	priceSort=0;
		}
	    if (timeSort>0 && priceSort>0) {
			timeSort=0;
			priceSort=0;
		}

	    page.setTimeSort(timeSort);
	    page.setPriceSort(priceSort);

		PageResult<Goods> result = goodsService.getPage(goodsSortId, page);
		List<Goods> rows = result.getRows();
		if (rows == null || rows.size() < 0) {
			return new JsonResult(result);
		}
		// 查询商品当前期数竞拍人数
		List<GoodsVo> rows2 = new ArrayList<GoodsVo>();
//		Long forstart = System.currentTimeMillis();
		for (Goods model : rows) {
			GoodsVo vo = new GoodsVo();
			Long aLong = System.currentTimeMillis();
			BeanUtils.copyProperties(vo, model);
			GoodsIssue issue = goodsIssueService.getById(model.getCurIssueId());
			rows2.add(vo);
			if (issue != null) {
//				vo.setPrice(issue.getPrice());
				vo.setDrawPrice(issue.getDrawPrice());
				vo.setDrawNum(issue.getDrawNum());
				vo.setCurNum(issue.getCurNum());
//				vo.setBusinessSendEp(issue.getBusinessSendEp());
//				vo.setStock(model.getStock());
				// 查询用户是否已参与竞拍
				/*if (token != null) {
					 if (goodsIssueDetailService.checkDraw(issue.getId(), token.getId())) {
						vo.setIsDraw(1);
					} 
					String watchlist = RedisKey.WATCH_LIST.getKey() + issue.getId();
					Map<String, String> map = Hash.hgetAll(watchlist);
					if (goodsIssueDetailService.checkDrawV3(map, token.getId())) {
						vo.setIsDraw(1);
					}
				}*/
			}
		}

		PageResult<GoodsVo> result2 = new PageResult<GoodsVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
		
		return new JsonResult(result2);
	}

	/**
	 * ep商品列表 v3.3
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/eplist", method = RequestMethod.GET)
	public JsonResult goodsEpList(HttpServletRequest request, String goodsSortId, Page page) throws Exception {
		if (org.springframework.util.StringUtils.isEmpty(goodsSortId)) {
			List<GoodsSort> sortList = goodsSortService.getEpList();
			if (!CollectionUtils.isEmpty(sortList)) {
				goodsSortId = sortList.get(0).getId();
				if (org.springframework.util.StringUtils.isEmpty(goodsSortId)) {
					goodsSortId = "E694266A369F4C1AAF337C37B8575FA2";
				}
			}
		}
		PageResult<Goods> result = goodsService.getEpPage(goodsSortId, page);
		List<Goods> rows = result.getRows();
		if (rows == null || rows.size() <= 0) {
			return new JsonResult(result);
		}
		List<GoodsEpVo> rows2 = new ArrayList<GoodsEpVo>();
		for (Goods model : rows) {
			GoodsEpVo vo = new GoodsEpVo();
			BeanUtils.copyProperties(vo, model);
			rows2.add(vo);
		}
		PageResult<GoodsEpVo> result2 = new PageResult<GoodsEpVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
		return new JsonResult(result2);
	}

	/**
	 * 商品竞拍修订版
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/auction", method = RequestMethod.POST)
	public JsonResult goodsAuction(HttpServletRequest request, @RequestBody DrawVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (StringUtils.isBlank(vo.getPayPwd())) {
			return new JsonResult(5, "请输入支付密码");
		}
		if (StringUtils.isBlank(vo.getGoodsId())) {
			return new JsonResult(1, "请选择竞拍商品");
		}
		final User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(user.getPayPwd())) {
			return new JsonResult(6, "请先设置支付密码");
		}
		if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
			return new JsonResult(7, "支付密码不正确");
		}

		final Goods goods = goodsService.getById(vo.getGoodsId());
		if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
			return new JsonResult(2, "商品不存在或已下架");
		}
		final GoodsIssue issue = goodsIssueService.getById(goods.getCurIssueId());
		if (issue == null || issue.getStatus() != IssueType.PENDING.getCode()) {
			return new JsonResult(3, "商品竞拍活动已结束");
		}
		if (issue.getCurNum() >= issue.getDrawNum()) {
			return new JsonResult(4, "本期参与人数已达上限");
		}
		if (user.getScore() == null || user.getScore() < issue.getDrawPrice()) {
			return new JsonResult(8, "您的积分不足");
		}

		// 竞拍
		String watchkey = RedisKey.WATCH_KEY.getKey() + issue.getId();
		String watchlist = RedisKey.WATCH_LIST.getKey() + issue.getId();

		String watch = Strings.get(watchkey);
		if (StringUtils.isBlank(watch) && issue.getCurNum() == 0) {
			Integer curNum = issue.getCurNum();
			Strings.set(watchkey, curNum + 1 + "");
			Hash.hset(watchlist, curNum + 1 + "", token.getId());
			// 异步处理竞拍业务
			new Thread(new Runnable() {
				public void run() {
					try {
						mallService.drawHandler(user, goods, issue);
					} catch (Exception e) {
						logger.error("参与竞拍失败！！" + e.getMessage());
					}
				}
			}).start();
		} else {
			Map<String, String> all = Hash.hgetAll(watchlist);
			for (Map.Entry<String, String> entry : all.entrySet()) {
				if (token.getId().equals(entry.getValue())) {
					return new JsonResult(9, "您已参与了本次竞拍");
				}
			}
			String string = Strings.get(watchkey);
			if (StringUtils.isBlank(string)) {
				return new JsonResult(3, "商品竞拍活动已结束");
			}
			Integer watchNum = Integer.parseInt(string);
			if (watchNum < issue.getDrawNum()) {
				Strings.set(watchkey, watchNum + 1 + "");
				Hash.hset(watchlist, watchNum + 1 + "", token.getId());
				// 异步处理竞拍业务
				new Thread(new Runnable() {
					public void run() {
						try {
							mallService.drawHandler(user, goods, issue);
						} catch (Exception e) {
							logger.error("添加竞拍记录失败！！" + e.getMessage());
						}
					}
				}).start();
			} else {
				return new JsonResult(4, "本期参与人数已达上限");
			}
		}
		return new JsonResult();
	}
	
	/**
	 * 首页商品按ep倒序
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/indexlist", method = RequestMethod.GET)
	public JsonResult indexEpGoodsList(Page page) throws Exception {
		if (page.getPageSize()==null || page.getPageSize()==0) {
			page.setPageSize(1);
		}
		if (page.getPageNo()==null || page.getPageNo()==0) {
			page.setPageSize(10);
		}
		PageResult<Goods> result = goodsService.getIndexEpListPage(page);
		return new JsonResult(result);
	}
	
	
	/*
	 * 新增商品简介
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/addorupdate", method = RequestMethod.POST)
	public JsonResult addIntroduction(HttpServletRequest request,@RequestBody GoodsDetailParamsVo paramsVo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if(user.getStatus()!=StatusType.TRUE.getCode()){
			return new JsonResult(1, "用户被禁用");
		}
		if(StringUtils.isBlank(paramsVo.getGoodsId())){
			return new JsonResult(2, "商品id为空");
		}
		//商品
		Goods goods=goodsService.getById(paramsVo.getGoodsId());
		if(null==goods){
			return new JsonResult(3, "未找到商品");
		}
		//获取所属商铺
		Store store=storeService.getById(goods.getStoreId());
		if(null==store){
			return new JsonResult(4, "未找到商铺");
		}
		//判断是否有权限添加商品简介
		if(!user.getStoreId().equals(store.getId())){
			return new JsonResult(5, "无权编辑此商品");
		}
		/*if(goods.getStatus()!=StatusType.FALSE.getValue()){
			return new JsonResult(6, "商品处于不可编辑状态");
		}*/
		//查询商品详情记录
		List<GoodsDetail> list=goodsDetailService.getByGoodsId(goods.getId());
		List<String> ids=new ArrayList<String>();
		for (GoodsDetail gd : list) {
			ids.add(gd.getId());
		}
		int addcount=0;
		List<String> addids=new ArrayList<String>();
		//需要删除的图片
		List<Map<String,Object>> delimglist=new ArrayList<Map<String,Object>>();
		//新增数据
		int dowithout=0;//content为空  不需要添加的数据
		for (GoodsDetailVo gdv: paramsVo.getGoodsDetail()) {
			if(null!=gdv.getImglist()&&gdv.getImglist().size()>0){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("namespace", gdv.getNamespace());
				String delimgs="";
				for (String img: gdv.getImglist()) {
					delimgs+=img+",";
				}
				map.put("imgs", delimgs.substring(0, delimgs.length()));
				delimglist.add(map);
			}
			GoodsDetail gd=new GoodsDetail();
			gd.setId(UUIDUtil.getUUID());
			gd.setGoodsId(goods.getId());
			gd.setModel(gdv.getModel());
			gd.setContent(gdv.getContent());
			gd.setRank(gdv.getRank());
			gd.setType(gdv.getType()==GoodsDetailType.IMG.getCode()?GoodsDetailType.IMG.getCode():GoodsDetailType.TEXT.getCode());
			gd.setStatus(1);
			gd.setCreateTime(new Date());
			gd.setUpdateTime(new Date());
			gd.setRemark(gdv.getRemark());
			if(!StringUtils.isEmpty(gd.getContent())){
				try {
					addids.add(gd.getId());
					addcount+=goodsDetailService.add(gd);
				} catch (Exception e) {
					//添加异常 删除数据
					e.printStackTrace();
					if(addids.size()>0){
						delInfo(goodsDetailService, addids);
					}
					return new JsonResult(7, "保存异常");
				}
			}else{
				dowithout++;
			}
		}
		if(delimglist.size()>0){//删除不需要的图片
			delImg(delimglist);
		}
		if(addcount==(paramsVo.getGoodsDetail().size()-dowithout)){//全部添加成功  删除原来的数据
			//删除原来数据
			if(ids.size()>0){
				delInfo(goodsDetailService, ids);
			}
		}else{//添加失败  删除新添加的数据
			if(addids.size()>0){
				delInfo(goodsDetailService, addids);
			}
			return new JsonResult(8, "保存失败");
		}
		return new JsonResult();
	}
	
	@ResponseBody
	@RequestMapping(value = "/goods/detaillist", method = RequestMethod.POST)
	public JsonResult selectIntroduction(HttpServletRequest request,@RequestBody GoodsDetailParamsVo paramsVo) throws Exception {
		/*Token token = TokenUtil.getSessionUser(request);
		User user = userService.getById(token.getId());
		if (null == user) {
			logger.error(String.format("Illegal user id[%s]", token.getId()));
			throw new IllegalArgumentException();
		}
		if(user.getStatus()!=StatusType.TRUE.getValue()){
			return new JsonResult(1, "用户被禁用");
		}*/
		if(StringUtils.isBlank(paramsVo.getGoodsId())){
			return new JsonResult(2, "商品id为空");
		}
		//商品
		Goods goods=goodsService.getById(paramsVo.getGoodsId());
		if(null==goods){
			return new JsonResult(3, "未找到商品");
		}
		List<GoodsDetail> list=goodsDetailService.getByGoodsId(goods.getId(),StatusType.TRUE.getCode());
		return new JsonResult(list);
	}
	
	
	//线程内删除数据
	private void delInfo(final GoodsDetailService services,final List<String> ids){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					services.delInId(ids);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}	
	
	//线程内删除数据
	private void delImg(final List<Map<String,Object>> delimglist){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (Map<String, Object> map : delimglist) {
						String namespace=map.get("namespace").toString();
						String imgs=map.get("imgs").toString();
						QiNiuUtil.batchDelFile(namespace, imgs.split(","));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 首页商品按ep倒序
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/getNesGoods", method = RequestMethod.GET)
	public JsonResult getNesGoodsList(Integer maxRow) throws Exception {
		if(maxRow == null || maxRow <=0){
			maxRow =6;
		}
		List<Goods> list = goodsService.getNesGoodsList(maxRow);
		return new JsonResult(list);
	}



	/**
	 * 用户模糊搜索商品，返回商品列表
	 */
	@ResponseBody
	@RequestMapping(value = "/goods/searchlist", method = RequestMethod.GET)
	public JsonResult goodsList(HttpServletRequest request, GoodsSearchVo goodsSearch, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Integer timeSort = page.getTimeSort();
		Integer priceSort = page.getPriceSort();
		if (timeSort==null) {
			timeSort=0;
		}
		if (priceSort==null) {
			priceSort=0;
		}
		if (timeSort>0 && priceSort>0) {
			timeSort=0;
			priceSort=0;
		}

		page.setTimeSort(timeSort);
		page.setPriceSort(priceSort);

		PageResult<Goods> result = goodsService.getSearchPage(goodsSearch, page);
		List<Goods> rows = result.getRows();
		if (rows == null || rows.size() < 0) {
			return new JsonResult(result);
		}
		List<GoodsVo> rows2 = new ArrayList<GoodsVo>();
		for (Goods model : rows) {
			GoodsVo vo = new GoodsVo();
			BeanUtils.copyProperties(vo, model);
			rows2.add(vo);
		}
		PageResult<GoodsVo> result2 = new PageResult<GoodsVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
		return new JsonResult(result2);
	}
}
