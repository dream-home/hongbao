package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.*;
import com.yanbao.constant.ImageType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.constant.VerifyType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.ImageVo;
import com.yanbao.vo.UserGoodsVo;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/user/goods")
public class UserGoodsController {

    private static final Logger logger = LoggerFactory.getLogger(UserGoodsController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileLinkService fileLinkService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private ImageService imageService;

    /**
     * 添加商品
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult addGoods(HttpServletRequest request, @RequestBody UserGoodsVo goodsVo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(0, "您未创建商铺");
        }

        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            logger.error(String.format("Illegal store id[%s]", user.getStoreId()));
            throw new IllegalArgumentException();
        }
        if (org.springframework.util.StringUtils.isEmpty(store.getId())) {
            logger.error("获取商家商铺信息失败 1000");
            return new JsonResult(0, "获取商家商铺信息失败");
        }
        ParamUtil util = ParamUtil.getIstance();
        Integer countStoreGoods = goodsService.countStoreGoods(user.getStoreId());
        if (countStoreGoods >= ToolUtil.parseInt(util.get(Parameter.STOREGOODSMAX), 0)) {
            return new JsonResult(1, "您发布的商品数量已达上限");
        }
        if (goodsVo == null) {
            return new JsonResult(2, "请填写商品资料");
        }
        if (goodsVo.getIcons() == null || goodsVo.getIcons().size() <= 0) {
            return new JsonResult(3, "请上传商品图片");
        }
        if (StringUtils.isBlank(goodsVo.getName())) {
            return new JsonResult(4, "请填写商品名称");
        }
        if (goodsVo.getPrice() == null || goodsVo.getPrice() <= 0) {
            return new JsonResult(5, "请填写商品价格");
        }
        int storeStockMax = ToolUtil.parseInt(util.get(Parameter.STORESTOCKMAX),0);
        if (goodsVo.getStock() == null || goodsVo.getStock() < 1 || goodsVo.getStock() > storeStockMax) {
            return new JsonResult(7, "商品库存只能1-" + storeStockMax + "之间");
        }
        if (StringUtils.isBlank(goodsVo.getGoodsSortId())) {
            return new JsonResult(8, "请选择商品类别");
        }
        //设置分销
        if (goodsVo.getFirstReferrerScale() == null || goodsVo.getFirstReferrerScale() < 0d) {
            goodsVo.setFirstReferrerScale(0d);
        }
        if (goodsVo.getSecondReferrerScale() == null || goodsVo.getSecondReferrerScale() < 0d) {
            goodsVo.setSecondReferrerScale(0d);
        }
        if (goodsVo.getThirdReferrerScale() == null || goodsVo.getThirdReferrerScale() < 0d) {
            goodsVo.setThirdReferrerScale(0d);
        }
        if (goodsVo.getFirstReferrerScale() + goodsVo.getSecondReferrerScale() + goodsVo.getThirdReferrerScale() >= 100d) {
            return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
        }

        //设置Ep
        if (goodsVo.getBusinessSendEp() == null || goodsVo.getBusinessSendEp() < 0d) {
            goodsVo.setSecondReferrerScale(0d);
        }
        //设置Ep
        if (goodsVo.getDiscountEP() == null || goodsVo.getDiscountEP() < 0d) {
            goodsVo.setDiscountEP(0d);
        }
        if (goodsVo.getDiscountEP() > 100) {
            return new JsonResult(10, "折扣优惠EP不能大于商品的价格");
        }
        if (goodsVo.getDiscountEP() == 0) {
            double afterDistribution = goodsVo.getPrice() - (((goodsVo.getFirstReferrerScale() + goodsVo.getSecondReferrerScale() + goodsVo.getThirdReferrerScale()) / 100d) * goodsVo.getPrice());
            if (afterDistribution < goodsVo.getBusinessSendEp()) {
                return new JsonResult(10, "赠送Ep值不能大于商品分销后的商品价格");
            }
        }
        String key = RedisKey.STORE_ADD_GOODS.getKey() + token.getId();
        if (!org.springframework.util.StringUtils.isEmpty(Strings.get(key))) {
            return new JsonResult(11, "商品已添加成功，请不要重复提交");
        }
        Strings.setEx(RedisKey.STORE_ADD_GOODS.getKey() + token.getId(), RedisKey.STORE_ADD_GOODS.getSeconds(), token.getId());
        Goods goods = new Goods();
        setGoods(goodsVo, store, goods);
        if (ToolUtil.isEmpty(goods.getIcon())){
            goods.setIcon(goodsVo.getIcons().get(0).getPath());
        }
        goods.setVerify(VerifyType.CHECKING.getCode());
        goodsService.add(goods);
        //上传到七牛的图片处理
        if (ToolUtil.isNotEmpty(goodsVo.getIcons())) {
            int i = 0;
            for (ImageVo vo : goodsVo.getIcons()) {
                Image addModel = new Image();
                addModel.setStatus(StatusType.FALSE.getCode());
                addModel.setPath(vo.getPath());
                addModel.setRank(i++);
                addModel.setRemark(ImageType.GOODS.getMsg());
                addModel.setType(ImageType.GOODS.getCode());
                addModel.setImageLinkId(goods.getId());
                imageService.add(addModel);
            }
        }
        //删除七牛图片
        if (ToolUtil.isNotEmpty(goodsVo.getDelIcons())) {
            QiNiuUtil.batchDelFile(goodsVo.getBucket(), goodsVo.getDelIcons().toArray(new String[goodsVo.getDelIcons().size()]));
        }
        // 处理图片
        /*if (goodsVo.getIcons() != null && goodsVo.getIcons().size() > 0) {
            boolean isSetDefaultFlag = false;
			// 新增图片
			for (ImageVo icon : goodsVo.getIcons()) {
				// 保存图片
				SysFile base64File = fileService.saveForBase64(icon.getPath(), token.getId());
				// 关联图片
				SysFileLink fileLink = new SysFileLink();
				fileLink.setFileId(base64File.getId());
				fileLink.setLinkId(goods.getId());
				fileLink.setLinkType(1);
				if (icon.getIsDefault() != null && StatusType.TRUE.getCode() == icon.getIsDefault() && !isSetDefaultFlag) {
					fileLink.setIsDefault(StatusType.TRUE.getCode());
					isSetDefaultFlag = true;
					goods.setIcon(BASE64Util.resize(100,100,icon.getPath()));//默认放小图
				}
				fileLinkService.add(fileLink);
			}*/

        Map<String, String> map = new HashMap<String, String>();
        map.put("goodsid", goods.getId());
        return new JsonResult(200, "ok", map);
    }

    private void setGoods(UserGoodsVo goodsVo, Store store, Goods goods) {
        goods.setIcon(goodsVo.getIcon());
        goods.setGoodsType(1); // 商家发布
        goods.setStoreId(store.getId());
        goods.setStoreName(store.getStoreName());
        goods.setName(goodsVo.getName().trim());
        goods.setPrice(goodsVo.getPrice());
        goods.setDrawNum(goodsVo.getDrawNum());
        goods.setDrawPrice(PoundageUtil.getPoundage(goodsVo.getPrice() / goodsVo.getDrawNum(), 1.0));
        goods.setStock(goodsVo.getStock());
        goods.setDetail(goodsVo.getDetail());
        goods.setSaleSwitch(goodsVo.getSaleSwitch() == null ? 0 : goodsVo.getSaleSwitch());
        goods.setStatus(goodsVo.getStatus() == null ? 0 : goodsVo.getStatus());
        goods.setFirstReferrerScale(goodsVo.getFirstReferrerScale());
        goods.setSecondReferrerScale(goodsVo.getSecondReferrerScale());
        goods.setThirdReferrerScale(goodsVo.getThirdReferrerScale());
        goods.setBusinessSendEp(goodsVo.getBusinessSendEp());
        goods.setGoodsSortId(goodsVo.getGoodsSortId());
        goods.setOriginalPrice(goodsVo.getOriginalPrice());
        goods.setDiscountEP(goodsVo.getDiscountEP());//Ep折扣率
    }

    private void addNewIssue(Goods goods) throws Exception {
        GoodsIssue newIssue = new GoodsIssue();
        newIssue.setStoreId(goods.getStoreId());
        newIssue.setStoreName(goods.getStoreName());
        newIssue.setSaleSwitch(goods.getSaleSwitch());
        newIssue.setIssueNo(1);
        newIssue.setGoodsId(goods.getId());
        newIssue.setGoodsName(goods.getName());
        newIssue.setIcon(goods.getIcon());
        newIssue.setPrice(goods.getPrice());
        newIssue.setDrawPrice(goods.getDrawPrice());
        newIssue.setDrawNum(goods.getDrawNum());
        newIssue.setCurNum(0);
        newIssue.setFirstReferrerScale(goods.getFirstReferrerScale());
        newIssue.setSecondReferrerScale(goods.getSecondReferrerScale());
        newIssue.setThirdReferrerScale(goods.getThirdReferrerScale());
        newIssue.setBusinessSendEp(goods.getBusinessSendEp());
        goodsIssueService.add(newIssue);

        goods.setCurIssueId(newIssue.getId());
        goods.setCurIssueNo(newIssue.getIssueNo());
        goodsService.update(goods.getId(), goods);
    }

    /**
     * 编辑商品
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public JsonResult editGoods(HttpServletRequest request, @RequestBody UserGoodsVo goodsVo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);

        logger.error("0000000000000000000000000000000000000000000000000000000000000");
        logger.error(" UserGoodsVo ===>  "+ JSON.toJSONString(goodsVo));
        logger.error("0000000000000000000000000000000000000000000000000000000000000");
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (goodsVo == null) {
            return new JsonResult(1, "请填写商品资料");
        }
        Goods goods = goodsService.getById(goodsVo.getId());
        if (null == goods) {
            return new JsonResult(2, "商品不存在已或已删除");
        }
        Store stores=storeService.getById(goods.getStoreId());
        if(stores.getStatus()==3){
        	return new JsonResult(3, "商铺关闭,不允许操作");
        }
        if (goods.getStatus() == StatusType.TRUE.getCode()) {
            return new JsonResult(3, "商品銷售中，禁止修改");
        }
        if (StringUtils.isBlank(goodsVo.getName())) {
            return new JsonResult(4, "请填写商品名称");
        }

        if (goodsVo.getPrice() == null || goodsVo.getPrice() <= 0) {
            return new JsonResult(5, "请填写商品价格");
        }
        int storeStockMax = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.STORESTOCKMAX),0);
        if (goodsVo.getStock() == null || goodsVo.getStock() < 1 || goodsVo.getStock() > storeStockMax) {
            return new JsonResult(7, "商品库存只能1-" + storeStockMax + "之间");
        }
        if (StringUtils.isBlank(goodsVo.getGoodsSortId())) {
            return new JsonResult(8, "请选择商品类别");
        }
//        if (goodsVo.getIcons() == null || goodsVo.getIcons().size() <= 0) {
//            return new JsonResult(9, "请上传商品图片");
//        }
        //设置分销
        if (goodsVo.getFirstReferrerScale() == null || goodsVo.getFirstReferrerScale() < 0d) {
            goodsVo.setFirstReferrerScale(0d);
        }
        if (goodsVo.getSecondReferrerScale() == null || goodsVo.getSecondReferrerScale() < 0d) {
            goodsVo.setSecondReferrerScale(0d);
        }
        if (goodsVo.getThirdReferrerScale() == null || goodsVo.getThirdReferrerScale() < 0d) {
            goodsVo.setThirdReferrerScale(0d);
        }
        if (goodsVo.getFirstReferrerScale() + goodsVo.getSecondReferrerScale() + goodsVo.getThirdReferrerScale() >= 100d) {
            return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
        }

        //设置Ep
        if (goodsVo.getBusinessSendEp() == null || goodsVo.getBusinessSendEp() < 0d) {
            goodsVo.setBusinessSendEp(0d);
        }
        //设置Ep
        if (goodsVo.getDiscountEP() == null || goodsVo.getDiscountEP() < 0d) {
            goodsVo.setDiscountEP(0d);
        }
        if (goodsVo.getDiscountEP() > 100) {
            return new JsonResult(10, "折扣优惠EP不能大于商品的价格");
        }
        if (goodsVo.getDiscountEP() == 0) {
            double afterDistribution = goodsVo.getPrice() - (((goodsVo.getFirstReferrerScale() + goodsVo.getSecondReferrerScale() + goodsVo.getThirdReferrerScale()) / 100d) * goodsVo.getPrice());
            if (afterDistribution < goodsVo.getBusinessSendEp()) {
                return new JsonResult(10, "赠送Ep值不能大于商品分销后的商品价格");
            }
        }


        //处理编辑后的七牛图片
        dealWithGoodsIcons(goodsVo);
//        if (ToolUtil.isEmpty(goods.getIcon())){
//            goods.setIcon(goodsVo.getIcons().get(0).getPath());
//        }
        setUpdateGoods(goodsVo, goods);
        if (ToolUtil.isNotEmpty(goodsVo.getDelIcons())){
            if (goodsVo.getDelIcons().size() > 0) {
                QiNiuUtil.batchDelFile(goodsVo.getBucket(), goodsVo.getDelIcons().toArray(new String[goodsVo.getDelIcons().size()]));
            }
        }
        // 处理图片
      /*  if (goodsVo.getIcons() != null && goodsVo.getIcons().size() > 0) {
            boolean isSetDefaultFlag = false;
            // 更新图片
            for (ImageVo icon : goodsVo.getIcons()) {
                if (0 == icon.getOption()) { // 新增
                    SysFile fileModel = fileService.saveForBase64(icon.getPath(), user.getId());
                    SysFileLink fileLink = new SysFileLink();
                    fileLink.setFileId(fileModel.getId());
                    fileLink.setLinkId(goods.getId());
                    fileLink.setLinkType(1);
                    if (icon.getIsDefault() != null && 1 == icon.getIsDefault() && !isSetDefaultFlag) {
                        fileLink.setIsDefault(1);
                        isSetDefaultFlag = true;
                        goods.setIcon(BASE64Util.resize(100, 100, icon.getPath()));//默认放小图
                    }
                    fileLinkService.add(fileLink);
                } else if (1 == icon.getOption()) { // 删除
                    fileLinkService.del(icon.getFileLinkId());
                    fileService.del(icon.getId());
                } else if (2 == icon.getOption()) { // 编辑
                    if (StringUtils.isNotBlank(icon.getFileLinkId()) && null != icon.getIsDefault()) { // 更新关联信息
                        if (StatusType.FALSE.getCode() == icon.getIsDefault() || StatusType.TRUE.getCode() == icon.getIsDefault()) {
                            SysFileLink model = new SysFileLink();
                            model.setIsDefault(icon.getIsDefault());
                            fileLinkService.update(icon.getFileLinkId(), model);
                            if (StatusType.TRUE.getCode() == icon.getIsDefault() && !isSetDefaultFlag) {
                                goods.setIcon(BASE64Util.resize(100, 100, icon.getPath()));//默认放小图
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(icon.getId())) { // 更新图片
                        SysFile model = new SysFile();
                        model.setPath(icon.getPath());
                        fileService.update(icon.getId(), model);
                    }
                }
            }
        }*/
        goods.setVerify(VerifyType.CHECKING.getCode());
        goodsService.update(goods.getId(), goods);
        //如果当前期数没人竞拍，则期数的商品信息也自动更新
        //去除期数表 modify by jay.zheng 2016/06/29
        /*GoodsIssue goodsIssue = goodsIssueService.getById(goods.getCurIssueId());
        if(goodsIssue.getCurNum()==0){
			// 自动发布竞拍期数
			goodsService.addNewIssue(goods);
		}*/
        return new JsonResult();
    }

    private void setUpdateGoods(UserGoodsVo goodsVo, Goods goods) {
        goods.setIcon(goodsVo.getIcon());
//        if (ToolUtil.isEmpty(goods.getIcon())){
//            goods.setIcon(goodsVo.getIcons().get(0).getPath());
//        }
        goods.setName(goodsVo.getName().trim());
        goods.setPrice(goodsVo.getPrice());
        goods.setDrawNum(goodsVo.getDrawNum());
        goods.setDrawPrice(PoundageUtil.getPoundage(goodsVo.getPrice() / goodsVo.getDrawNum(), 1.0));
        goods.setStock(goodsVo.getStock());
        goods.setDetail(goodsVo.getDetail());
        goods.setSaleSwitch(goodsVo.getSaleSwitch() == null ? 0 : goodsVo.getSaleSwitch());
        goods.setStatus(goodsVo.getStatus() == null ? 0 : goodsVo.getStatus());
        goods.setFirstReferrerScale(goodsVo.getFirstReferrerScale());
        goods.setSecondReferrerScale(goodsVo.getSecondReferrerScale());
        goods.setThirdReferrerScale(goodsVo.getThirdReferrerScale());
        goods.setBusinessSendEp(goodsVo.getBusinessSendEp());
        goods.setGoodsSortId(goodsVo.getGoodsSortId());
        goods.setOriginalPrice(goodsVo.getOriginalPrice());
        goods.setDiscountEP(goodsVo.getDiscountEP());
    }

    /**
     * @param setting
     */
    private void setDefaultSetting(SysSetting setting) {
        setting.setDrawNumMin(2);
        setting.setDrawNumMax(10);
    }

    /**
     * 商铺商品列表
     */
    @ResponseBody
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult getGoodsPage(HttpServletRequest request, String storeId, Page page) throws Exception {
        PageResult<Goods> goods = goodsService.getStoreGoodsPage(storeId, null, page);
        return new JsonResult(goods);
    }

    /**
     * 商品详情
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult getInfo(HttpServletRequest request, String id) throws Exception {
        Goods goods = goodsService.getById(id);
        if (null == goods) {
            return new JsonResult(1, "商品不存在已或已删除");
        }
        List<ImageVo> icons = new ArrayList<ImageVo>();
        //查询七牛存储的店铺图
        List<Image> goodsIcons = imageService.getByLinkId(id, ImageType.GOODS.getCode());
        if (ToolUtil.isEmpty(goodsIcons)) {
            //兼容之前的老数据
            List<SysFileLink> links = fileLinkService.getList(id);
            if (links != null && links.size() > 0) {
                String ids = "";
                Map<String, SysFileLink> mapSysFile = new HashMap<String, SysFileLink>();
                for (SysFileLink link : links) {
                    ids += "'" + link.getFileId() + "',";
                    mapSysFile.put(link.getFileId(), link);
                }
                List<SysFile> files = fileService.getSelectByIdIn(ids.length() > 0 ? ids.substring(0, ids.length() - 1) : ids);
                for (SysFile file : files) {
                    SysFileLink link = mapSysFile.get(file.getId());
                    ImageVo icon = new ImageVo();
                    icon.setId(file.getId());
                    icon.setPath(file.getPath());
                    icon.setIsDefault(link.getIsDefault());
                    icon.setFileLinkId(link.getId());
                    icons.add(icon);
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
        BeanUtils.copyProperties(vo, goods);
        vo.setIcons(icons);
        return new JsonResult(vo);
    }

    /**
     * 停止竞拍
     */
    @ResponseBody
    @RequestMapping(value = "/stopDraw", method = RequestMethod.GET)
    public JsonResult stopDraw(HttpServletRequest request, String id) throws Exception {
        Goods goods = goodsService.getById(id);
        if (null == goods) {
            return new JsonResult(1, "商品不存在已或已删除");
        }
        if (goods.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(2, "商品已下架");
        }
        Store stores=storeService.getById(goods.getStoreId());
        if(stores.getStatus()==3){
        	return new JsonResult(3, "商铺关闭,不允许操作");
        }
        Goods update = new Goods();
        update.setStatus(StatusType.FALSE.getCode());
        update.setFirstReferrerScale(null);
        update.setSecondReferrerScale(null);
        update.setThirdReferrerScale(null);
        update.setBusinessSendEp(null);
        update.setDiscountEP(null);
        goodsService.update(goods.getId(), update);

        return new JsonResult();
    }

    /**
     * 开始竞拍
     */
    @ResponseBody
    @RequestMapping(value = "/startDraw", method = RequestMethod.GET)
    public JsonResult startDraw(HttpServletRequest request, String id) throws Exception {
        Goods goods = goodsService.getById(id);
        if (null == goods) {
            return new JsonResult(1, "商品不存在已或已删除");
        }
        if (goods.getStatus() == StatusType.TRUE.getCode()) {
            return new JsonResult(2, "商品已经上架");
        }
        Goods update = new Goods();
        update.setStatus(StatusType.TRUE.getCode());
        update.setFirstReferrerScale(null);
        update.setSecondReferrerScale(null);
        update.setThirdReferrerScale(null);
        update.setBusinessSendEp(null);
        update.setDiscountEP(null);
        goodsService.update(goods.getId(), update);

        return new JsonResult();
    }

    /**
     * 删除商品
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public JsonResult delGoods(HttpServletRequest request, String id) throws Exception {
        Goods goods = goodsService.getById(id);
        if (null == goods) {
            return new JsonResult(1, "商品不存在已或已删除");
        }
        if (goods.getStatus() == StatusType.TRUE.getCode()) {
            return new JsonResult(2, "商品销售中，禁止删除");
        }
        Goods update = new Goods();
        update.setStatus(StatusType.DEL.getCode());
        update.setFirstReferrerScale(null);
        update.setSecondReferrerScale(null);
        update.setThirdReferrerScale(null);
        update.setBusinessSendEp(null);
        goodsService.update(goods.getId(), update);

        return new JsonResult();
    }

    /**
     * 增加库存
     */
    @ResponseBody
    @RequestMapping(value = "/addStock", method = RequestMethod.POST)
    public JsonResult delGoods(HttpServletRequest request, @RequestBody UserGoodsVo goodsVo) throws Exception {
        Goods goods = goodsService.getById(goodsVo.getId());
        if (null == goods) {
            return new JsonResult(1, "商品不存在已或已删除");
        }
        int storeStockMax = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.STORESTOCKMAX), 0);
        if (goodsVo.getStock() == null || goodsVo.getStock() < 1 || goodsVo.getStock() + goods.getStock() > storeStockMax) {
            return new JsonResult(7, "商品库存只能1-" + storeStockMax + "之间");
        }
        goodsService.updateStock(goods.getId(), goodsVo.getStock());
        return new JsonResult();
    }


    private void dealWithGoodsIcons(UserGoodsVo goodsVo) throws Exception {
        if (goodsVo.getIcons() != null && goodsVo.getIcons().size() > 0) {
            //查询出所有的商品图片
            List<Image> imageList = imageService.getByLinkId(goodsVo.getId(), ImageType.GOODS.getCode());
            if (ToolUtil.isEmpty(imageList)) {
                //没有数据就新增
                int j = 0;
                for (ImageVo img : goodsVo.getIcons()) {
                    Image addModel = new Image();
                    addModel.setImageLinkId(goodsVo.getId());
                    addModel.setType(ImageType.GOODS.getCode());
                    addModel.setPath(img.getPath());
                    addModel.setRank(j);
                    addModel.setRemark(ImageType.GOODS.getMsg());
                    imageService.add(addModel);
                    j++;
                }
            } else {
                //记录要删除的七牛数据
                if (goodsVo.getIcons().size() <= imageList.size()) {
                    //编辑时图片数量小于原有图片数量
                    for (int i = 0; i < imageList.size(); i++) {
                        Image image = imageList.get(i);
                        String path = image.getPath();
                        Boolean isUpdate = false;
                        Image updateModel = new Image();
                        updateModel.setId(image.getId());
                        updateModel.setUpdateTime(new Date());
                        if (i < goodsVo.getIcons().size()) {
                            isUpdate = image.getPath().equals(goodsVo.getIcons().get(i).getPath());
                            if (!isUpdate) {
                                //覆盖原有数据
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(goodsVo.getIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }
                        } else {
                            //删除部分原有数据
                            updateModel.setStatus(StatusType.TRUE.getCode());
                            imageService.updateById(image.getId(), updateModel);
                        }
                    }
                } else {
                    //新增数据多余原有数据
                    for (int i = 0; i < goodsVo.getIcons().size(); i++) {
                        Boolean isUpdate = false;
                        if (i < imageList.size()) {
                            Image image = imageList.get(i);
                            String path = image.getPath();
                            isUpdate = image.getPath().equals(goodsVo.getIcons().get(i).getPath());
                            if (!isUpdate) {
                                //覆盖原有数据
                                Image updateModel = new Image();
                                updateModel.setId(image.getId());
                                updateModel.setUpdateTime(new Date());
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(goodsVo.getIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }
                        } else {
                            //插入新的数据
                            Image addModel = new Image();
                            addModel.setType(ImageType.GOODS.getCode());
                            addModel.setPath(goodsVo.getIcons().get(i).getPath());
                            addModel.setImageLinkId(goodsVo.getId());
                            addModel.setRank(i);
                            addModel.setRemark(ImageType.GOODS.getMsg());
                            imageService.add(addModel);
                        }
                    }
                }
            }
            //删掉七牛图片
            delQiniuImage(goodsVo.getBucket(), goodsVo.getIcons(), imageList);
        }
    }

    /**
     * 删掉七牛图片
     *
     * @param imageVoList
     * @param imageList
     */
    private void delQiniuImage(String bucket, List<ImageVo> imageVoList, List<Image> imageList) {
        List<String> listA = new ArrayList<>();
        List<String> listB = new ArrayList<>();
        for (ImageVo imageVo : imageVoList) {
            if (ToolUtil.isNotEmpty(imageVo.getPath())) {
                if (imageVo.getPath().indexOf("http://") > -1) {
                    listA.add(imageVo.getPath().substring(imageVo.getPath().indexOf("http://") + 1, imageVo.getPath().length()));
                }
            }
        }
        for (Image image : imageList) {
            if (ToolUtil.isNotEmpty(image.getPath())) {
                listB.add(image.getPath().substring(image.getPath().indexOf("http://") + 1, image.getPath().length()));
            }
        }
        ImageUtils.delQiniuImage(bucket, listA, listB);
    }

}
