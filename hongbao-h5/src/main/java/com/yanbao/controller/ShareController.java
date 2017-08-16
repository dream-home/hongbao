package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.ImageType;
import com.yanbao.constant.StatusType;
import com.yanbao.constant.StoreType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.util.h5.GenerateH5Order;
import com.yanbao.vo.ImageVo;
import com.yanbao.vo.PurchaseVo;
import com.yanbao.vo.StoreVo;
import com.yanbao.vo.UserGoodsVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18 0018.
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);
    @Value("${publicAppid}")
    private String publicAppid;
    private static String SHARE_SIGN_PUBLIC_APPID;

    @PostConstruct
    public void init() {
        SHARE_SIGN_PUBLIC_APPID = publicAppid;
    }

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private FileLinkService fileLinkService;
    @Autowired
    private FileService fileService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private StoreCollectService storeCollectService;
    @Autowired
    private ImageService imageService;

    /**
     * 商铺详情
     */
    @ResponseBody
    @RequestMapping(value = "/storeInfo", method = RequestMethod.GET)
    public JsonResult storeInfo(HttpServletRequest request, String storeId) throws Exception {

        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(storeId)) {
            return new JsonResult(1, "商铺Id不能为空");
        }
        Store store = storeService.getById(storeId);
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(2, "商铺不存在");
        }

        // 获取相关图片
        List<ImageVo> icons = new ArrayList<ImageVo>();
        List<SysFileLink> links = fileLinkService.getList(storeId);
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
     * 商品列表
     */
    @ResponseBody
    @RequestMapping(value = "/goodList", method = RequestMethod.GET)
    public JsonResult goodList(HttpServletRequest request, String storeId, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        PageResult<Goods> result = goodsService.getStoreGoodsPage(storeId, StatusType.TRUE.getCode(), page);
        List<Goods> rows = result.getRows();
        if (rows == null || rows.size() < 0) {
            return new JsonResult(result);
        }
        // 查询商品当前期数竞拍人数
        List<UserGoodsVo> rows2 = new ArrayList<UserGoodsVo>();
        UserGoodsVo vo = null;
        for (Goods model : rows) {
            vo = new UserGoodsVo();
            BeanUtils.copyProperties(vo, model);
            rows2.add(vo);
        }
        PageResult<UserGoodsVo> result2 = new PageResult<UserGoodsVo>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), rows2);
        return new JsonResult(result2);
    }

    /**
     * 商品详情
     */
    @ResponseBody
    @RequestMapping(value = "/goodsInfo", method = RequestMethod.GET)
    public JsonResult goodsInfo(HttpServletRequest request, String goodsId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //根据商品id获取商品详情
        Goods goods = goodsService.getById(goodsId);
        if (goods == null || goods.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(1, "商品不存在或已下架");
        }

        List<ImageVo> icons = new ArrayList<ImageVo>();
        //查询七牛存储的店铺图
        List<Image> goodsIcons = imageService.getByLinkId(goodsId, ImageType.GOODS.getCode());
        if (ToolUtil.isEmpty(goodsIcons)) {
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
        } else {
            for (Image image : goodsIcons) {
                ImageVo icon = new ImageVo();
                icon.setId(image.getId());
                icon.setPath(image.getPath());
                icon.setFileLinkId(image.getImageLinkId());
                icons.add(icon);
            }
        }

        UserGoodsVo vo = new UserGoodsVo();
        vo.setIcons(icons);
        BeanUtils.copyProperties(vo, goods);
        return new JsonResult(vo);
    }


    /**
     * h5版商品直接购买
     */
    @ResponseBody
    @RequestMapping(value = "/goods/purchase", method = RequestMethod.POST)
    public JsonResult goodsPurchase(HttpServletRequest request, @RequestBody PurchaseVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(vo.getGoodsId())) {
            return new JsonResult(1, "请选择商品");
        }
        Goods goods = goodsService.getById(vo.getGoodsId());
        if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
            return new JsonResult(2, "商品不存在或已下架");
        }
        if (goods.getStock() <= 0) {
            return new JsonResult(3, "商品库存不足");
        }
        Store store = storeService.getById(goods.getStoreId());
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(4, "商铺已关闭，禁止购买");
        }
        if (vo.getPayType().intValue() != BankCardType.ALIPAY.getCode().intValue() && vo.getPayType().intValue() != BankCardType.WECHATPAY.getCode().intValue()) {
            return new JsonResult(5, "支付方式只有支付宝和微信");
        }

        final User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }

        if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPhone()) || StringUtils.isBlank(vo.getAddr())) {
            return new JsonResult(6, "请完善收货信息");
        }
        //购买数量不传默认为1
        if (vo.getNum() == null || vo.getNum() <= 0) {
            vo.setNum(1);
        }

        //生成待付款的订单
        GoodsWin goodsWin = mallStoreService.addOrderByH5(user, goods, vo);
        //返回参数给前端
        Map<Object, Object> map = new HashedMap();
        if (vo.getPayType().intValue() == BankCardType.WECHATPAY.getCode().intValue()) {
            String ip = request.getRemoteAddr();
            String attach = token.getId() + "@" + BankCardType.SHARE_WEIXIN.getCode();
            GenerateH5Order order = new GenerateH5Order();
            double price = PoundageUtil.getPoundage(goodsWin.getPrice() * goodsWin.getNum() * 100, 1d, 2);
            String m = price + "";
            String money = m.substring(0, m.indexOf("."));
            Map<String, String> wxMap = order.generate(money, ip, attach, goodsWin.getOrderNo(), user.getOpenId(), WechatUtil._NOTIFY_URL_H5);
            map.put("appid", wxMap.get("appId"));
            map.put("noncestr", wxMap.get("nonceStr"));
            map.put("timestamp", wxMap.get("timeStamp"));
            map.put("package", wxMap.get("package"));
            map.put("prepayid", wxMap.get("prepayid"));
            map.put("sign", wxMap.get("paySign"));
        }
        map.put("orderNo", goodsWin.getOrderNo());
        map.put("payTime", goodsWin.getCreateTime());
        map.put("userId", goodsWin.getUserId());
        return new JsonResult(map);
    }


    /**
     * 商家发货订单详情
     */
    @ResponseBody
    @RequestMapping(value = "/order/info", method = RequestMethod.GET)
    public JsonResult getOrderInfo(HttpServletRequest request, String orderNo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        GoodsWin result = goodsWinService.getByOrderNo(orderNo, user.getId());
        if (null == result) {
            logger.error("订单不存在：" + orderNo + ";userid:" + token.getId());
            throw new IllegalArgumentException();
        }
        return new JsonResult(result);
    }

    /**
     * 微信分享签名
     *
     * @param request
     * @param url
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/sharesign", method = RequestMethod.GET)
    public JsonResult shareSign(HttpServletRequest request, String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return new JsonResult(-1, "分享链接不能为空");
        }
        String accessToken = WechatApiUtil.accessTokenForAPi();
        if (StringUtils.isEmpty(accessToken)) {
            return new JsonResult(-1, "获取accessToken失败");
        }
        String ticket = WechatApiUtil.getJsApiTicket(accessToken);
        if (StringUtils.isEmpty(ticket)) {
            return new JsonResult(-1, "获取ticket失败");
        }
        Map<String, String> map = ShareSignUtils.sign(ticket, url);
//        map.put("appId", WechatUtil.YANBAO_APPID);
//        map.put("appId", WechatCommonUtil.YANBAO_PUBLIC_APPID);
        map.put("appId", SHARE_SIGN_PUBLIC_APPID);
        return new JsonResult(map);
    }

}
