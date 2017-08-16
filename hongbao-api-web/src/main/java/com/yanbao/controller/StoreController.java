package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Hash;
import com.yanbao.redis.SortSet;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mall/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreCollectService storeCollectService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileLinkService fileLinkService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private HbSysKeyService hbSysKeyService;
    @Autowired
    private ImageService imageService;

    /**
     * 商铺列表
     */
    @ResponseBody
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult getPage(HttpServletRequest request, Page page, String location) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (location == null) {
            location = "";
        }
        PageResult<Store> result = storeService.getPage(page, location);

        List<Store> rows = result.getRows();
        if (rows == null || rows.size() < 0) {
            return new JsonResult(result);
        }
        List<StoreVo> rows2 = new ArrayList<StoreVo>();
        for (Store model : rows) {
            StoreVo vo = new StoreVo();
            BeanUtils.copyProperties(vo, model);
            if (token != null) {
                // 判断用户是否收藏
                Boolean collected = storeCollectService.isCollected(token.getId(), model.getId());
                if (collected) {
                    vo.setIsCollect(StatusType.TRUE.getCode());
                }
            }
            if (StringUtils.isNotBlank(vo.getInviteCode())) {
                vo.setHasInviteCode(StatusType.TRUE.getCode());
                vo.setInviteCode(null);
            }
            rows2.add(vo);
        }
        PageResult<StoreVo> result2 = new PageResult<StoreVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
        return new JsonResult(result2);
    }

    /**
     * 商铺搜索
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JsonResult search(HttpServletRequest request, DistanceVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        Boolean isHasCoordinate = true;
        if (ToolUtil.isEmpty(vo.getSearchWord())) {
            return new JsonResult(1, "请输入搜索关键字");
        }
        if (ToolUtil.isEmpty(vo.getAddress())) {
            vo.setAddress("");
        }

        if (ToolUtil.isEmpty(vo.getLatitude()) || ToolUtil.isEmpty(vo.getLongitude())) {
            isHasCoordinate = null;
        }
        List<Store> rows = null;
        List<StoreVo> rows2 = new ArrayList<StoreVo>();
        rows = storeService.getListByName(vo.getSearchWord(), vo.getAddress(), isHasCoordinate);
        if (rows == null || rows.size() < 0) {
            return new JsonResult(rows);
        }
        for (Store model : rows) {
            StoreVo storeVo = new StoreVo();
            BeanUtils.copyProperties(storeVo, model);
            if (ToolUtil.isNotEmpty(vo.getLongitude()) && ToolUtil.isNotEmpty(vo.getLatitude()) && ToolUtil.isNotEmpty(model.getLatitude()) && ToolUtil.isNotEmpty(model.getLatitude())) {
                Double distance = ToolUtil.getDistance(vo.getLongitude(), vo.getLatitude(), Double.valueOf(model.getLongitude()), Double.valueOf(model.getLatitude()));
                storeVo.setDistance(PoundageUtil.getPoundage(distance, 1d, 2));
            } else {
                storeVo.setDistance(0d);
            }
            if (token != null) {
                // 判断用户是否收藏
                Boolean collected = storeCollectService.isCollected(token.getId(), model.getId());
                if (collected) {
                    storeVo.setIsCollect(StatusType.TRUE.getCode());
                }
            }
            if (StringUtils.isNotBlank(storeVo.getInviteCode())) {
                storeVo.setHasInviteCode(StatusType.TRUE.getCode());
                storeVo.setInviteCode(null);
            }
            rows2.add(storeVo);
        }
        DistanceComparator comparator = new DistanceComparator();
        Collections.sort(rows2, comparator);
        for (StoreVo model : rows2) {
            model.setDistanceRank(ToolUtil.getDistance(model.getDistance()));
        }
        return new JsonResult(rows2);
    }

    /**
     * 商铺详情
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult getInfo(HttpServletRequest request, String storeId, String inviteCode) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isNotBlank(inviteCode)) {
            inviteCode = inviteCode.trim();
        }
        if (StringUtils.isBlank(storeId)) {
            return new JsonResult(1, "商铺Id不能为空");
        }
        Store store = storeService.getById(storeId);
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(2, "商铺不存在");
        }
        // 获取相关图片
        List<ImageVo> icons = new ArrayList<ImageVo>();
        /*List<SysFileLink> links = fileLinkService.getList(storeId);
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
		}*/
        StoreVo vo = new StoreVo();
        BeanUtils.copyProperties(vo, store);
        if (token != null) {
            // 判断用户是否收藏
            Boolean collected = storeCollectService.isCollected(token.getId(), store.getId());
            if (collected) {
                vo.setIsCollect(1);
            }
        }
        vo.setIcons(icons);
        User user = userService.getById(store.getUserId());
        if (user != null) {
            vo.setPhone(user.getPhone());
        }
        return new JsonResult(vo);
    }

    /**
     * 商铺商品列表
     */
    @ResponseBody
    @RequestMapping(value = "/goods/page", method = RequestMethod.GET)
    public JsonResult getGoodsPage(HttpServletRequest request, String storeId, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        PageResult<Goods> result = goodsService.getStoreGoodsPage(storeId, StatusType.TRUE.getCode(), page);
        List<Goods> rows = result.getRows();
        if (rows == null || rows.size() < 0) {
            return new JsonResult(result);
        }
	/*	// 查询商品当前期数竞拍人数
		List<UserGoodsVo> rows2 = new ArrayList<UserGoodsVo>();
		for (Goods model : rows) {
			UserGoodsVo vo = new UserGoodsVo();
			BeanUtils.copyProperties(vo, model);
			GoodsIssue issue = goodsIssueService.getById(model.getCurIssueId());
			if (issue != null) {
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
		PageResult<UserGoodsVo> result2 = new PageResult<UserGoodsVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
		*/

        return new JsonResult(result);
    }

    /**
     * 商铺商品详情
     */
    @ResponseBody
    @RequestMapping(value = "/goods/info", method = RequestMethod.GET)
    public JsonResult getGoodsInfo(HttpServletRequest request, String goodsId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        Goods goods = goodsService.getById(goodsId);
        if (null == goods || goods.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(1, "商品不存在已或已下架");
        }
        List<ImageVo> icons = new ArrayList<ImageVo>();
	/*	List<SysFileLink> links = fileLinkService.getList(goodsId);
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
		}*/
        UserGoodsVo vo = new UserGoodsVo();
        BeanUtils.copyProperties(vo, goods);
        vo.setIcons(icons);

        return new JsonResult(vo);
        /**
         * 斗拍功能暂时屏蔽 V3.8
         * **/

/*
		GoodsIssue issue = goodsIssueService.getById(goods.getCurIssueId());
	`	if (issue != null) {
			vo.setDrawPrice(issue.getDrawPrice());
			vo.setDrawNum(issue.getDrawNum());
			vo.setCurNum(issue.getCurNum());
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
		// 查询用户是否已参与竞拍
		if (token != null) {
			if (goodsIssueDetailService.checkDraw(goods.getCurIssueId(), token.getId())) {
				vo.setIsDraw(1);
			}
		}*/
    }


    /**
     * 商品详情图片
     */
    @ResponseBody
    @RequestMapping(value = "/icons", method = RequestMethod.GET)
    public JsonResult getGoodsIcons(String id, Integer type) throws Exception {
        if (StringUtils.isEmpty(id)) {
            return new JsonResult(1, "id不能为空");
        }
        //查询七牛存储的店铺图或者商品图
        List<Image> goodsIcons = imageService.getByLinkId(id, type);
        List<SysFile> fileList = new ArrayList<>();
        if (ToolUtil.isEmpty(goodsIcons)) {
            List<SysFileLink> links = fileLinkService.getList(id);
            List<String> fileIds = null;
            if (links != null && links.size() > 0) {
                fileIds = new ArrayList<>();
                for (SysFileLink link : links) {
                    fileIds.add(link.getFileId());
                }
            }
            fileList = fileService.getListByFileIds(fileIds);
            if (CollectionUtils.isEmpty(fileList)) {
                fileList = new ArrayList<>();
            }
        } else {
            for (Image image : goodsIcons) {
                SysFile sysFile = new SysFile();
                sysFile.setId(image.getImageLinkId());
                sysFile.setPath(image.getPath());
                fileList.add(sysFile);
            }
        }
        return new JsonResult(fileList);
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
        if (goods.getStock() <= 0) {
            return new JsonResult(9, "商品库存不足");
        }
        Store store = storeService.getById(goods.getStoreId());
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(11, "商铺已关闭，禁止斗拍");
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
            return new JsonResult(8, "您的积分不足");
        }
        // 竞拍
        final String watchkey = RedisKey.WATCH_KEY.getKey() + issue.getId();
        final String watchlist = RedisKey.WATCH_LIST.getKey() + issue.getId();

        final String watch = Strings.get(watchkey);
        final HbSysKey sysKey1 = new HbSysKey();
        if (StringUtils.isBlank(watch) && issue.getCurNum() == 0) {
            final Integer curNum = issue.getCurNum();
            try {
                sysKey1.setId(issue.getId() + curNum + 1);
                sysKey1.setIssId(issue.getId());
                hbSysKeyService.insert(sysKey1);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonResult(3, "商品竞拍活动已结束");
            }
            // 异步处理竞拍业务
            new Thread(new Runnable() {
                public void run() {
                    try {
                        mallStoreService.drawHandler(user, goods, issue);
                        Strings.set(watchkey, curNum + 1 + "");
                        Hash.hset(watchlist, curNum + 1 + "", token.getId());
                    } catch (Exception e) {
                        try {
                            hbSysKeyService.delById(sysKey1);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
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
                GoodsIssue iss = goodsIssueService.getById(goods.getCurIssueId());
                if (iss.getCurNum() < iss.getDrawNum()) {
                    Integer count = goodsIssueDetailService.countSumPersonByIssueId(goods.getCurIssueId(), DrawType.PENDING.getCode());
                    if (count == null || count == 0 || count < iss.getDrawNum()) {
                        Strings.set(RedisKey.WATCH_KEY.getKey() + goods.getCurIssueId(), iss.getCurNum() + "");
                        string = Strings.get(watchkey);
                    }
                } else {
                    return new JsonResult(3, "商品竞拍活动已结束");
                }
            }
            final Integer watchNum = Integer.parseInt(string);
            final HbSysKey sysKey = new HbSysKey();
            if (watchNum < issue.getDrawNum()) {
                try {
                    sysKey.setId(issue.getId() + watchNum + 1);
                    sysKey.setIssId(issue.getId());
                    hbSysKeyService.insert(sysKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JsonResult(3, "商品竞拍活动已结束");
                }

                // 异步处理竞拍业务
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            mallStoreService.drawHandler(user, goods, issue);
                            Strings.set(watchkey, watchNum + 1 + "");
                            Hash.hset(watchlist, watchNum + 1 + "", token.getId());
                        } catch (Exception e) {
                            try {
                                hbSysKeyService.delById(sysKey);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
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
        if (goods.getStock() <= 0) {
            return new JsonResult(9, "商品库存不足");
        }
        if (StringUtils.isBlank(vo.getPayPwd())) {
            return new JsonResult(5, "请输入支付密码");
        }
        Store store = storeService.getById(goods.getStoreId());
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(11, "商铺已关闭，禁止购买");
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
            return new JsonResult(8, "您的积分不足");
        }
        if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPhone()) || StringUtils.isBlank(vo.getAddr())) {
            return new JsonResult(10, "请完善收货信息");
        }
        mallStoreService.purchaseHandler(user, goods, vo);
        // 获取商家信息唯一的极光推送Id
        User storeUser = userService.getById(store.getUserId());
        // 普通用户购买成功后推送给商家，提醒发货
        boolean rs = JPushUtil.pushPayloadByid(storeUser.getRegistrationId(), "您有新的商品需要发货，请尽快处理", new JpushExtraModel(JpushExtraModel.NOTIFIYPE, JpushExtraModel.STORE_ORDER));
        logger.info("推送ID:" + storeUser.getRegistrationId() + ";用户ID:" + storeUser.getUid() + ";推送结果：" + rs);
        return new JsonResult();
    }

    /**
     * 获取新店开张的商铺信息
     */
    @ResponseBody
    @RequestMapping(value = "/getNewStore", method = RequestMethod.GET)
    public JsonResult getNewStore(Integer maxRow) throws Exception {
        //不传参数，默认显示最新创建的3个商铺
        if (maxRow == null || maxRow <= 0) {
            maxRow = 3;
        }
        List<Store> result = storeService.getNewStore(maxRow);
        return new JsonResult(result);
    }


    /**
     * 商品详情返回商铺信息：商铺地址，电话，是否收藏，商铺名称
     */
    @ResponseBody
    @RequestMapping(value = "/storeinfo", method = RequestMethod.GET)
    public JsonResult getStoreInfo(HttpServletRequest request, String storeId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isEmpty(storeId)) {
            return new JsonResult(1, "商铺信息不能为空");
        }
        Store store = storeService.getById(storeId);
        if (null == store || store.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(1, "商铺已关闭");
        }
        StoreVo vo = new StoreVo();
        BeanUtils.copyProperties(vo, store);
        vo.setIcon("");
        vo.setDetail("");
        vo.setIcon("");
        vo.setIdcardIcons(null);
        vo.setLicenseIcons(null);
        if (token != null) {
            // 判断用户是否收藏
            Boolean collected = storeCollectService.isCollected(token.getId(), store.getId());
            if (collected) {
                vo.setIsCollect(1);
            }
        }
        if (StringUtils.isEmpty(vo.getServicePhone())) {
            User user = userService.getById(store.getUserId());
            if (user != null) {
                vo.setServicePhone(StringUtils.isEmpty(user.getPhone()) ? "" : user.getPhone());
            }
        }
        return new JsonResult(vo);
    }


    /**
     * 商铺列表有距离排序
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, Page page, DistanceVo vo) throws Exception {
        System.out.println(vo.getAddress());
        Token token = TokenUtil.getSessionUser(request);
        PageResult<Store> result = null;
        Boolean flag = true;
        if ((ToolUtil.isEmpty(vo.getLatitude()) || ToolUtil.isEmpty(vo.getLongitude()))) {
            flag = false;
            result = storeService.getPage(page, vo.getAddress());
        } else {
            result = storeService.getNewPage(page, vo);
        }
        List<Store> rows = result.getRows();

        List<StoreVo> rows2 = new ArrayList<StoreVo>();

        for (Store model : rows) {
            StoreVo storeVo = new StoreVo();
            BeanUtils.copyProperties(storeVo, model);
            if (flag) {
                Double finalDistance = SortSet.zscore(RedisKey.STORE_CODE_PREFIX.getKey() + vo.getSerialNumber() + vo.getAddress(), model.getId());
                storeVo.setDistance(PoundageUtil.getPoundage(finalDistance, 1d, 2));
            }
            rows2.add(storeVo);
        }
        for (StoreVo model : rows2) {
            model.setDistanceRank(ToolUtil.getDistance(model.getDistance()));
        }
//		DistanceComparator comparator =new DistanceComparator();
//		Collections.sort(rows2,comparator);

        //如果计算经纬度返回的数据为空，再按照pageNo,pageSize查一次分页数据
		/*if (ToolUtil.isEmpty(rows2)){
			result= storeService.getPage(page,vo.getAddress());
			  rows  = result.getRows();
			if (rows == null || rows.size() < 0) {
				return new JsonResult(Collections.emptyList());
			}
		   rows2 = new ArrayList<StoreVo>();
			for (Store model : rows) {
				StoreVo storeVo = new StoreVo();
				BeanUtils.copyProperties(storeVo, model);
				rows2.add(storeVo);
			}
		}*/
        PageResult<StoreVo> result2 = new PageResult<StoreVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
        return new JsonResult(result2);
    }
}
