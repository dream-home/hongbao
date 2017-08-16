package com.yanbao.controller;

import com.mall.model.*;
import com.taobao.api.internal.util.Base64;
import com.yanbao.constant.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.util.qrcode.QRCodeUtil;
import com.yanbao.vo.*;
import org.apache.commons.beanutils.BeanUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/user/store")
public class UserStoreController {

    private static final Logger logger = LoggerFactory.getLogger(UserStoreController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileLinkService fileLinkService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PayDistributionService payDistributionService;
    @Value("${store_scan_page}")
    private String storeScanPage;


    /**
     * 获取用户店铺状态
     */
    @ResponseBody
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public JsonResult userStoreStatus(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (ToolUtil.isEmpty(user.getStoreId())) {
//            Double sumUserDrawScore = goodsIssueDetailService.sumUserDrawScore(token.getId());
            Double sumUserDrawScore = goodsWinService.sumUserBuyAmt(token.getId());
            map.put("isCreated", 0);
            map.put("createStoreCondition", ParamUtil.getIstance().get(Parameter.CREATESTORECONDITION));
            map.put("sumUserDrawScore", sumUserDrawScore == null ? 0d : sumUserDrawScore);
        } else {
            Store store = storeService.getById(user.getStoreId());
            if (null == store) {
                logger.error(String.format("Illegal store id[%s]", user.getStoreId()));
                throw new IllegalArgumentException();
            }
            map.put("isCreated", 1);
            map.put("status", store.getStatus());
        }
        return new JsonResult(map);
    }

    /**
     * 创建商铺
     */
    @ResponseBody
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public JsonResult applyStore(HttpServletRequest request, @RequestBody StoreVo storeVo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isNotBlank(user.getStoreId())) {
            return new JsonResult(0, "您已创建商铺");
        }
        if (StringUtils.isBlank(user.getPhone())) {
            return new JsonResult(1, "请先设置手机号");
        }
//        Double sumUserDrawScore = goodsIssueDetailService.sumUserDrawScore(token.getId());
        Double sumUserBuyAmt = goodsWinService.sumUserBuyAmt(token.getId());
        if (sumUserBuyAmt==null){
            sumUserBuyAmt=0d;
        }
        double createStoreCondition = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.CREATESTORECONDITION),100);
        if (sumUserBuyAmt < createStoreCondition) {
            return new JsonResult(2, "累计消费满 " + createStoreCondition + " 才能创建自己的店铺");
        }
        if (storeVo == null) {
            return new JsonResult(3, "请填写商铺资料");
        }


        logger.error("******图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getIcons().size() + "*************");
        logger.error("***********************");

        logger.error("******身份证图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getIdcardIcons().size() + "*************");
        logger.error("***********************");

        logger.error("******营业执照图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getLicenseIcons().size() + "*************");
        logger.error("***********************");
        if (storeVo.getIcons() == null || storeVo.getIcons().size() <= 0 || StringUtils.isBlank(storeVo.getIcons().get(0).getPath())) {
            return new JsonResult(4, "请上传商铺封面");
        }
        storeVo.setIcon(storeVo.getIcons().get(0).getPath());
        if (StringUtils.isBlank(storeVo.getStoreName())) {
            return new JsonResult(5, "请填写商铺名称");
        }
        if (StringUtils.isBlank(storeVo.getDetail())) {
            return new JsonResult(6, "请填写商铺介绍");
        }
        if (StringUtils.isBlank(storeVo.getAddr())) {
            return new JsonResult(7, "请填写商铺详细地址");
        }
        //商铺资质 身份证至少3张！ --V3.8
        //storeVo.getLicenseIcons() == null || storeVo.getLicenseIcons().size() <= 0||
        if (storeVo.getIdcardIcons() == null || storeVo.getIdcardIcons().size() <= 2) {
            return new JsonResult(8, "请完善商铺资质资料");
        }
        /*if (StringUtils.isBlank(storeVo.getBusinessScope())) {
            return new JsonResult(8, "请选择商铺经营类别");
		}*/
        /*if (StringUtils.isNotBlank(storeVo.getInviteCode())) {
            storeVo.setInviteCode(storeVo.getInviteCode().trim());
		}*/
        if (storeVo.getFirstReferrerScale() == null || storeVo.getFirstReferrerScale() < 0d) {
            storeVo.setFirstReferrerScale(0d);
        }
        if (storeVo.getSecondReferrerScale() == null || storeVo.getSecondReferrerScale() < 0d) {
            storeVo.setSecondReferrerScale(0d);
        }
        if (storeVo.getThirdReferrerScale() == null || storeVo.getThirdReferrerScale() < 0d) {
            storeVo.setThirdReferrerScale(0d);
        }
        if (storeVo.getFirstReferrerScale() + storeVo.getSecondReferrerScale() + storeVo.getThirdReferrerScale() >= 100d) {
            return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
        }
        Store store = new Store();
        BeanUtils.copyProperties(store, storeVo);
//		store.setDetail(EmojiUtil.emojiConvert(storeVo.getDetail()));
        store.setDetail(EmojiUtil.filterEmoji(store.getDetail()));
//		store.setDetail(EmojiUtil.removeFourChar(store.getDetail()));
        store.setUserId(user.getId());
        //客服电话  --V3.8
        if (StringUtils.isBlank(storeVo.getServicePhone())) {
            store.setServicePhone(user.getPhone());
        } else {
            store.setServicePhone(storeVo.getServicePhone());
        }
        //保存身份证图片 --V3.8
        store.setIDCardIcon(UUIDUtil.getUUID());
        ImageVo img = null;
        for (int i = 0; i < storeVo.getIdcardIcons().size(); i++) {
            img = storeVo.getIdcardIcons().get(i);
            if (img.getPath().length() > 100) {//非默认图路径才保存 默认路径小于100个字符
                SysFile base64File = fileService.saveForBase64(img.getPath(), user.getId());
                // 关联图片
                SysFileLink fileLink = fileLinkService.saveInfo(base64File.getId(), store.getIDCardIcon(), i == 0 ? StatusType.TRUE.getCode() : StatusType.FALSE.getCode());
                if (null == fileLink) {
                    logger.error("图片关联失败!图片ID:" + base64File.getId() + ";商铺ID:" + store.getId());
                }
            }
        }
        //保存营业执照图片 --V3.8
        store.setStoreLicense(UUIDUtil.getUUID());
        for (int i = 0; i < storeVo.getLicenseIcons().size(); i++) {
            img = storeVo.getLicenseIcons().get(i);
            if (img.getPath().length() > 100) {//非默认图路径才保存 默认路径小于100个字符
                SysFile base64File = fileService.saveForBase64(img.getPath(), user.getId());
                // 关联图片
                SysFileLink fileLink = fileLinkService.saveInfo(base64File.getId(), store.getStoreLicense(), i == 0 ? StatusType.TRUE.getCode() : StatusType.FALSE.getCode());
                if (null == fileLink) {
                    logger.error("图片关联失败!图片ID:" + base64File.getId() + ";商铺ID:" + store.getId());
                }
            }
        }
        store.setIcon(BASE64Util.resize(100, 100, storeVo.getIcons().get(0).getPath()));//默认放小图
        storeService.add(store);
        // 保存默认图片
        SysFile base64File = fileService.saveForBase64(storeVo.getIcons().get(0).getPath(), token.getId());
        // 关联图片
        SysFileLink link = new SysFileLink();
        link.setFileId(base64File.getId());
        link.setLinkId(store.getId());
        link.setLinkType(0);
        link.setIsDefault(StatusType.TRUE.getCode());
        fileLinkService.add(link);

        User updateStore = new User();
        updateStore.setStoreId(store.getId());
        userService.update(user.getId(), updateStore);
        //邮箱推送
        emailService.sendEmail("斗拍推送", "系统有一条新的店铺审核消息,请注意查看", ParamUtil.getIstance().get(Parameter.MAILPEOPLE));

        return new JsonResult(store);
    }

    private void setDefaultSetting(SysSetting setting) {
        setting.setCreateStoreCondition(100d);
    }

    /**
     * 生成店铺邀请码
     */
    @ResponseBody
    @RequestMapping(value = "/inviteCode", method = RequestMethod.GET)
    public JsonResult inviteCode(HttpServletRequest request) throws Exception {
        String random = RandomUtil.randomNumString(6);
        return new JsonResult(random);
    }

    /**
     * 获取我的商铺详情
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult storeInfo(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            logger.error(String.format("Illegal store id[%s]", user.getStoreId()));
            throw new IllegalArgumentException();
        }
        List<ImageVo> icons = new ArrayList<ImageVo>();
        //查询七牛存储的店铺图
        List<Image> storeIcons = imageService.getByLinkId(store.getId(), ImageType.STORE.getCode());
        if (ToolUtil.isEmpty(storeIcons)) {
            //兼容之前的老数据
            List<SysFileLink> links = fileLinkService.getList(store.getId());
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
            for (Image image : storeIcons) {
                ImageVo icon = new ImageVo();
                icon.setId(image.getId());
                icon.setPath(image.getPath());
                icon.setFileLinkId(image.getImageLinkId());
                icons.add(icon);
            }
        }

        StoreVo storeVo = new StoreVo();
        storeVo.setServicePhone(store.getServicePhone());
        //  查询数据 --V3.8
        //查询七牛存储的身份证图
        List<Image> idCardIcons = imageService.getByLinkId(store.getId(), ImageType.IDCARD.getCode());
        List<ImageVo> IDCardicons = new ArrayList<ImageVo>();
        if (ToolUtil.isEmpty(idCardIcons)) {
            if (null != store.getIDCardIcon()) {
                SysFileLink find = new SysFileLink();
                find.setLinkId(store.getIDCardIcon());
                find.setStatus(StatusType.TRUE.getCode());
                find.setIsDefault(null);
                List<SysFileLink> IDCardIconsLink = fileLinkService.getListByPo(find);
                if (IDCardIconsLink != null && IDCardIconsLink.size() > 0) {
                    String ids = "";
                    for (SysFileLink link : IDCardIconsLink) {
                        ids += "'" + link.getFileId() + "',";
                    }
                    List<SysFile> files = fileService.getSelectByIdIn(ids.length() > 0 ? ids.substring(0, ids.length() - 1) : ids);
                    if (files.size() > 0) {
                        ImageVo icon = new ImageVo();
                        icon.setPath("" + files.size());
                        IDCardicons.add(icon);
                    }
                }
            }
        } else {
            for (Image image : idCardIcons) {
                ImageVo icon = new ImageVo();
                icon.setId(image.getId());
                icon.setPath(image.getPath());
                icon.setFileLinkId(image.getImageLinkId());
                IDCardicons.add(icon);
            }
        }
        storeVo.setIdcardIcons(IDCardicons);
        //查询七牛存储的资质图
        List<Image> licensIcons = imageService.getByLinkId(store.getId(), ImageType.LIENSCE.getCode());
        List<ImageVo> storeLicens = new ArrayList<ImageVo>();
        if (ToolUtil.isEmpty(licensIcons)) {
            if (null != store.getStoreLicense()) {
                SysFileLink find = new SysFileLink();
                find.setLinkId(store.getStoreLicense());
                find.setStatus(StatusType.TRUE.getCode());
                find.setIsDefault(null);
                List<SysFileLink> StoreLicensLink = fileLinkService.getListByPo(find);
                if (StoreLicensLink != null && StoreLicensLink.size() > 0) {
                    String ids = "";
                    for (SysFileLink link : StoreLicensLink) {
                        ids += "'" + link.getFileId() + "',";
                    }
                    List<SysFile> files = fileService.getSelectByIdIn(ids.length() > 0 ? ids.substring(0, ids.length() - 1) : ids);
                    if (files.size() > 0) {
                        ImageVo icon = new ImageVo();
                        icon.setPath("" + files.size());
                        storeLicens.add(icon);
                    }
                }

            }
        } else {
            for (Image image : licensIcons) {
                ImageVo icon = new ImageVo();
                icon.setId(image.getId());
                icon.setPath(image.getPath());
                icon.setFileLinkId(image.getImageLinkId());
                storeLicens.add(icon);
            }
        }
        storeVo.setLicenseIcons(storeLicens);
        BeanUtils.copyProperties(storeVo, store);
        storeVo.setIcons(icons);
        return new JsonResult(storeVo);
    }

    /**
     * 修改商铺
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public JsonResult editStore(HttpServletRequest request, @RequestBody StoreVo storeVo) throws Exception {
        storeVo.setDetail(URLEncoder.encode(storeVo.getDetail()));
        storeVo.setDetail(URLDecoder.decode(storeVo.getDetail()));
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            logger.error(String.format("Illegal store id[%s]", user.getStoreId()));
            throw new IllegalArgumentException();
        }
        if (storeVo == null) {
            return new JsonResult(2, "请填写商铺资料");
        }
        if (StringUtils.isBlank(storeVo.getStoreName())) {
            return new JsonResult(3, "请填写商铺名称");
        }
        if (StringUtils.isBlank(storeVo.getDetail())) {
            return new JsonResult(4, "请填写商铺介绍");
        }
        if (StringUtils.isBlank(storeVo.getAddr())) {
            return new JsonResult(5, "请填写商铺详细地址");
        }
		/*if (StringUtils.isNotBlank(storeVo.getInviteCode())) {
			storeVo.setInviteCode(storeVo.getInviteCode().trim());
		}*/
        if (storeVo.getFirstReferrerScale() == null || storeVo.getFirstReferrerScale() < 0d) {
            storeVo.setFirstReferrerScale(0d);
        }
        if (storeVo.getSecondReferrerScale() == null || storeVo.getSecondReferrerScale() < 0d) {
            storeVo.setSecondReferrerScale(0d);
        }
        if (storeVo.getThirdReferrerScale() == null || storeVo.getThirdReferrerScale() < 0d) {
            storeVo.setThirdReferrerScale(0d);
        }
        if (storeVo.getFirstReferrerScale() + storeVo.getSecondReferrerScale() + storeVo.getThirdReferrerScale() >= 100d) {
            return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
        }
        //查询出所有的身份证图片 V3.8
        SysFileLink filelinkall = new SysFileLink();
        filelinkall.setIsDefault(null);
        filelinkall.setLinkId(store.getIDCardIcon());
        List<SysFileLink> fileLinkListByIdCard = fileLinkService.getListByPo(filelinkall);
        filelinkall.setLinkId(store.getStoreLicense());
        List<SysFileLink> fileLinkListByLicense = fileLinkService.getListByPo(filelinkall);
        int idcardcount = fileLinkListByIdCard.size();
        if (storeVo.getIdcardIcons() != null && storeVo.getIdcardIcons().size() > 0) {
            for (ImageVo img : storeVo.getIdcardIcons()) {
                if (1 == img.getOption()) {
                    idcardcount--;
                } else if (0 == img.getOption() && img.getPath().length() > 100) {//图片长度大于100
                    idcardcount++;
                }
            }
        }

        if (idcardcount < 3) {//||licensecount<3
            return new JsonResult(6, "商铺资质资料不得少于3张");
        }
        Store temp = new Store();
        temp.setStoreName(storeVo.getStoreName().trim());
        temp.setDetail(storeVo.getDetail().trim());
        temp.setAddr(storeVo.getAddr().trim());
        temp.setInviteCode(storeVo.getInviteCode());
        temp.setFirstReferrerScale(storeVo.getFirstReferrerScale());
        temp.setSecondReferrerScale(storeVo.getSecondReferrerScale());
        temp.setThirdReferrerScale(storeVo.getThirdReferrerScale());
        temp.setBusinessScope(storeVo.getBusinessScope());
        if (StringUtils.isBlank(storeVo.getServicePhone())) {
            temp.setServicePhone(user.getPhone());
        } else {
            temp.setServicePhone(storeVo.getServicePhone());
        }
        if (store.getStatus() == StoreType.FAILURE.getCode()) {
            temp.setStatus(StoreType.PENDING.getCode());
        }
        if (storeVo.getIcons() != null && storeVo.getIcons().size() > 0) {
            boolean isSetDefaultFlag = false;
            // 更新图片
            for (ImageVo icon : storeVo.getIcons()) {
                if (icon.getPath().length() <= 100 && 1 != icon.getOption()) {//不等于删除并且非默认图路径才保存
                    continue;
                }
                if (0 == icon.getOption()) { // 新增
                    // 保存图片
                    SysFile base64File = fileService.saveForBase64(icon.getPath(), token.getId());
                    // 关联图片
                    SysFileLink fileLink = new SysFileLink();
                    fileLink.setFileId(base64File.getId());
                    fileLink.setLinkId(user.getStoreId());
                    fileLink.setLinkType(0);
                    if (icon.getIsDefault() != null && 1 == icon.getIsDefault() && !isSetDefaultFlag) {
                        fileLink.setIsDefault(1);
                        isSetDefaultFlag = true;
                        temp.setIcon(BASE64Util.resize(100, 100, icon.getPath()));//默认放小图
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
                                temp.setIcon(BASE64Util.resize(100, 100, icon.getPath()));//默认放小图
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
        }
        //新增或修改时本身没有资质ID时创建ID --V3.8
        if (null == store.getIDCardIcon()) {
            store.setIDCardIcon(UUIDUtil.getUUID());
            temp.setIDCardIcon(store.getIDCardIcon());
        }
        if (null == store.getStoreLicense()) {
            store.setStoreLicense(UUIDUtil.getUUID());
            temp.setStoreLicense(store.getStoreLicense());
        }
        //修改身份证图片 --V3.8
        if (storeVo.getIdcardIcons() != null && storeVo.getIdcardIcons().size() > 0) {
            upImg(storeVo.getIdcardIcons(), user, store.getIDCardIcon());
        }
        //修改营业执照图片 --V3.8
        if (storeVo.getLicenseIcons() != null && storeVo.getLicenseIcons().size() > 0) {
            upImg(storeVo.getLicenseIcons(), user, store.getStoreLicense());
        }
        storeService.update(user.getStoreId(), temp);
        return new JsonResult();
    }

    public void upImg(List<ImageVo> listImg, User user, String typeID) throws Exception {
        for (ImageVo img : listImg) {
            if (img.getPath().length() <= 100 && 1 != img.getOption()) {//不等于删除并且非默认图路径才保存
                continue;
            }
            if (0 == img.getOption()) { // 新增
                SysFile base64File = fileService.saveForBase64(img.getPath(), user.getId());
                // 关联图片
                SysFileLink fileLink = fileLinkService.saveInfo(base64File.getId(), typeID, null == img.getIsDefault() || img.getIsDefault() == 0 ? StatusType.FALSE.getCode() : StatusType.TRUE.getCode());
            } else if (1 == img.getOption()) { // 删除
                SysFileLink filelink = new SysFileLink();
                filelink.setIsDefault(null);
                filelink.setLinkId(img.getFileLinkId());
                filelink.setFileId(img.getId());
                fileLinkService.delPo(filelink);
                fileService.del(img.getId());
            } else if (2 == img.getOption()) { // 编辑
                if (StringUtils.isNotBlank(img.getId())) { // 更新图片
                    SysFile model = new SysFile();
                    model.setPath(img.getPath());
                    fileService.update(img.getId(), model);
                }
            }
        }
    }

    /**
     * 商家发货订单列表
     */
    @ResponseBody
    @RequestMapping(value = "/order/page", method = RequestMethod.GET)
    public JsonResult getOrderPage(HttpServletRequest request, Integer status, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (status == null || (GoodsWinType.BUYED.getCode() != status && GoodsWinType.DELIVERED.getCode() != status)) {
            return new JsonResult(0, "请传入正常的状态");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        PageResult<GoodsWin> result = goodsWinService.getStoreOrderPage(user.getStoreId(), status, page);
        return new JsonResult(result);
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
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        GoodsWin result = goodsWinService.getStoreOrderByOrderNo(orderNo, user.getStoreId());
        return new JsonResult(result);
    }

    /**
     * 商家发货
     */
    @ResponseBody
    @RequestMapping(value = "/order/edit", method = RequestMethod.POST)
    public JsonResult OrderEdit(HttpServletRequest request, @RequestBody OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        logger.debug("商家发货**********订单号： " + vo.getOrderNo());
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        GoodsWin order = goodsWinService.getStoreOrderByOrderNo(vo.getOrderNo(), user.getStoreId());
        if (order == null) {
            return new JsonResult(2, "订单不存在");
        }
		/*if (org.springframework.util.StringUtils.isEmpty(order.getIssueId())) {
			return new JsonResult(4, "商品期数有误，请联系客服");
		}*/
        if (order.getStatus() != GoodsWinType.BUYED.getCode()) {
            return new JsonResult(3, "订单已发货");
        }
        if (StringUtils.isBlank(vo.getExpressNo()) || StringUtils.isBlank(vo.getExpressName())) {
            return new JsonResult(4, "请完善物流信息");
        }
        String key = RedisKey.STORE_EXPRESS_GOODS.getKey() + vo.getOrderNo();
        if (!org.springframework.util.StringUtils.isEmpty(Strings.get(key))) {
            return new JsonResult(5, "商品已发货，请不要重复提交");
        }
        Strings.setEx(RedisKey.STORE_EXPRESS_GOODS.getKey() + vo.getOrderNo(), RedisKey.STORE_EXPRESS_GOODS.getSeconds(), vo.getOrderNo());
        return userService.sendGoodService(vo, user, order);
    }


    /**
     * 商家商品销售统计
     */
    @ResponseBody
    @RequestMapping(value = "/goods/sales", method = RequestMethod.GET)
    public JsonResult countSaleNum(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        // 销售总数
        Integer total = 0;
        // 按商品统计
        List<GoodsSalesVo> salesVos = goodsWinService.countStoreGoodsSales(user.getStoreId());
        if (salesVos != null && salesVos.size() > 0) {
            for (int i = 0; i < salesVos.size(); i++) {
                GoodsSalesVo vo = salesVos.get(i);
                Goods goods = goodsService.getById(vo.getId());
                if (goods != null) {
                    BeanUtils.copyProperties(vo, goods);
                    total += vo.getTotal();
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("goodsTotal", salesVos);
        return new JsonResult(result);
    }

    /**
     * 商家收益
     */
    @ResponseBody
    @RequestMapping(value = "/income", method = RequestMethod.GET)
    public JsonResult countStoreIncome(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        Map<String, Double> result = new HashMap<String, Double>();
        Double system_fee = walletRecordService.sumScore(token.getId(), RecordType.SYSTEM_FEE.getCode());
        Double store_income = walletRecordService.sumScore(token.getId(), RecordType.STORE_INCOME.getCode());
        result.put("system_fee", system_fee);
        result.put("store_income", store_income);
        return new JsonResult(result);
    }


    /**
     * 生成商家收款二维码
     */
    @ResponseBody
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public JsonResult getPicBase64Code(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        PayDistribution distribution = payDistributionService.getByUserId(user.getId(), PayDistributionType.store.getCode());
        if (distribution == null) {
            return new JsonResult(2, "您未设置付款参数");
        }
        //http://120.76.43.39/h5test?index=4&storeUserId=ASDFSAFDSAFSAFSAFSA&uid=200000&storeId=JKLAJFADLKGADGDASFDSADFSAAF&type=2
        String content = storeScanPage + "&storeUserId=" + token.getId() + "&uid=" + user.getUid() + "&storeId=" + user.getStoreId() + "&type=2";
        String imgPath = request.getSession().getServletContext().getRealPath("/resources/") + "/css/img/logo.png";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeUtil.encode(content, imgPath, baos);
        String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
        baos.close();
        return new JsonResult(rstr);
    }


    /**
     * 创建商铺（七牛修改版）
     */
    @ResponseBody
    @RequestMapping(value = "/newapply", method = RequestMethod.POST)
    public JsonResult newApplyStore(HttpServletRequest request, @RequestBody NewApplyStoreVo storeVo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isNotBlank(user.getStoreId())) {
            return new JsonResult(0, "您已创建商铺");
        }
        if (StringUtils.isBlank(user.getPhone())) {
            return new JsonResult(1, "请先设置手机号");
        }
//        Double sumUserDrawScore = goodsIssueDetailService.sumUserDrawScore(token.getId());
        Double sumUserBuyAmt = goodsWinService.sumUserBuyAmt(token.getId());
        if(sumUserBuyAmt == null){
            sumUserBuyAmt = 0d;
        }
        double createStoreCondition = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.CREATESTORECONDITION), 100);
        if (sumUserBuyAmt < createStoreCondition) {
            return new JsonResult(2, "累计消费满" + createStoreCondition + " 才能创建自己的店铺");
        }
        if (storeVo == null) {
            return new JsonResult(3, "请填写商铺资料");
        }

        logger.error("******图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getIcons().size() + "*************");
        logger.error("***********************");

        logger.error("******身份证图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getIdcardIcons().size() + "*************");
        logger.error("***********************");

        logger.error("******营业执照图片个数************");
        logger.error("***********************");
        logger.error("******" + storeVo.getLicenseIcons().size() + "*************");
        logger.error("***********************");
        if (storeVo.getIcons() == null || storeVo.getIcons().size() <= 0 || StringUtils.isBlank(storeVo.getIcons().get(0).getPath())) {
            return new JsonResult(4, "请上传商铺封面");
        }
        storeVo.setIcon(storeVo.getIcons().get(0).getPath());
        if (StringUtils.isBlank(storeVo.getStoreName())) {
            return new JsonResult(5, "请填写商铺名称");
        }
        if (StringUtils.isBlank(storeVo.getDetail())) {
            return new JsonResult(6, "请填写商铺介绍");
        }
        if (StringUtils.isBlank(storeVo.getAddr())) {
            return new JsonResult(7, "请填写商铺详细地址");
        }
        //商铺资质 身份证至少3张
        //storeVo.getLicenseIcons() == null || storeVo.getLicenseIcons().size() <= 0||
        if (storeVo.getIdcardIcons() == null || storeVo.getIdcardIcons().size() <= 2) {
            return new JsonResult(8, "请完善商铺资质资料");
        }
        if (storeVo.getLongitude() == null || storeVo.getLatitude() == null || "".equals(storeVo.getLongitude()) || "".equals(storeVo.getLatitude())) {
//            return new JsonResult(9, "请设置商家定位");
        }

        Store store = new Store();
        BeanUtils.copyProperties(store, storeVo);
        store.setDetail(EmojiUtil.filterEmoji(store.getDetail()));
        store.setUserId(user.getId());
        //客服电话
        if (StringUtils.isBlank(storeVo.getServicePhone())) {
            store.setServicePhone(user.getPhone());
        } else {
            store.setServicePhone(storeVo.getServicePhone());
        }
        if (ToolUtil.isNotEmpty(storeVo.getCountyCode())){
            store.setAreaId(storeVo.getCountyCode());
        }
        if (ToolUtil.isNotEmpty(storeVo.getProvinceCode())){
            store.setProvinceCode(storeVo.getProvinceCode());
        }
        if (ToolUtil.isNotEmpty(storeVo.getCityCode())){
            store.setCityCode(storeVo.getCityCode());
        }
        if (ToolUtil.isNotEmpty(storeVo.getCountyCode())){
            store.setCountyCode(storeVo.getCountyCode());
        }
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getCounty());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getCity());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getProvince());
        storeService.add(store);
        storeVo.setId(store.getId());
        //保存店铺图
        saveStoreIcons(storeVo);
        //保存用户身份证
        saveIdCardIcons(storeVo);
        //保存店铺资质
        saveLicenseIcons(storeVo);
        //删除七牛图片
        if (ToolUtil.isNotEmpty(storeVo.getDelIcons())) {
            QiNiuUtil.batchDelFile(storeVo.getBucket(), storeVo.getDelIcons().toArray(new String[storeVo.getDelIcons().size()]));
        }

        User updateStore = new User();
        updateStore.setStoreId(store.getId());
        userService.update(user.getId(), updateStore);
        //邮箱推送
        emailService.sendEmail("斗拍推送", "系统有一条新的店铺审核消息,请注意查看", ParamUtil.getIstance().get(Parameter.MAILPEOPLE));
        return new JsonResult(store);
    }

    /**
     * 保存营业执照图片
     *
     * @param storeVo
     * @throws Exception
     */
    private void saveLicenseIcons(NewApplyStoreVo storeVo) throws Exception {
        ImageVo img;
        for (int i = 0; i < storeVo.getLicenseIcons().size(); i++) {
            img = storeVo.getLicenseIcons().get(i);
            Image addModel = new Image();
            addModel.setImageLinkId(storeVo.getId());
            addModel.setType(ImageType.LIENSCE.getCode());
            addModel.setRemark(ImageType.LIENSCE.getMsg());
            addModel.setRank(i);
            addModel.setPath(img.getPath());
            addModel.setStatus(StatusType.FALSE.getCode());
            imageService.add(addModel);
        }
    }

    /**
     * 保存用户身份证
     *
     * @param storeVo
     * @throws Exception
     */
    private void saveIdCardIcons(NewApplyStoreVo storeVo) throws Exception {
        ImageVo img;
        for (int i = 0; i < storeVo.getIdcardIcons().size(); i++) {
            img = storeVo.getIdcardIcons().get(i);
            Image addModel = new Image();
            addModel.setImageLinkId(storeVo.getId());
            addModel.setType(ImageType.IDCARD.getCode());
            addModel.setRemark(ImageType.IDCARD.getMsg());
            addModel.setRank(i);
            addModel.setPath(img.getPath());
            addModel.setStatus(StatusType.FALSE.getCode());
            imageService.add(addModel);
        }
    }

    /**
     * 保存店铺图片
     *
     * @param storeVo
     * @throws Exception
     */
    private void saveStoreIcons(NewApplyStoreVo storeVo) throws Exception {
        ImageVo img = null;
        for (int i = 0; i < storeVo.getIcons().size(); i++) {
            img = storeVo.getIcons().get(i);
            Image addModel = new Image();
            addModel.setImageLinkId(storeVo.getId());
            addModel.setType(ImageType.STORE.getCode());
            addModel.setRemark(ImageType.STORE.getMsg());
            addModel.setRank(i);
            addModel.setPath(img.getPath());
            addModel.setStatus(StatusType.FALSE.getCode());
            imageService.add(addModel);
        }
    }

    /**
     * 修改商铺(七牛版)
     */
    @ResponseBody
    @RequestMapping(value = "/newedit", method = RequestMethod.POST)
    public JsonResult newEditStore(HttpServletRequest request, @RequestBody NewEditStoreVo storeVo) throws Exception {
        storeVo.setDetail(URLEncoder.encode(storeVo.getDetail()));
        storeVo.setDetail(URLDecoder.decode(storeVo.getDetail()));
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getStoreId())) {
            return new JsonResult(1, "您未创建商铺");
        }
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            logger.error(String.format("Illegal store id[%s]", user.getStoreId()));
            throw new IllegalArgumentException();
        }
        if (storeVo == null) {
            return new JsonResult(2, "请填写商铺资料");
        }
        if (StringUtils.isBlank(storeVo.getStoreName())) {
            return new JsonResult(3, "请填写商铺名称");
        }
        if (StringUtils.isBlank(storeVo.getDetail())) {
            return new JsonResult(4, "请填写商铺介绍");
        }
        if (StringUtils.isBlank(storeVo.getAddr())) {
            return new JsonResult(5, "请填写商铺详细地址");
        }
        //如果已经审核通过
        if (store.getStatus().equals(StatusType.FALSE.getCode())){
            if (storeVo.getLicenseIcons() != null && storeVo.getLicenseIcons().size() < 3) {
                return new JsonResult(6, "商铺资质资料不得少于3张");
            }
            dealWithIdCard(storeVo);

            dealWithStoreLicense(storeVo);
        }
        if (storeVo.getLongitude() == null || storeVo.getLatitude() == null || "".equals(storeVo.getLongitude()) || "".equals(storeVo.getLatitude())) {
//            return new JsonResult(7, "请设置商家定位");
        }
        Store temp = new Store();
        temp.setIcon(storeVo.getIcon());
        dealWithStoreIcons(storeVo);
        //删除七牛图片
        if (ToolUtil.isNotEmpty(storeVo.getDelIcons())) {
            QiNiuUtil.batchDelFile(storeVo.getBucket(), storeVo.getDelIcons().toArray(new String[storeVo.getDelIcons().size()]));
        }
        temp.setStoreName(storeVo.getStoreName().trim());
        temp.setDetail(storeVo.getDetail().trim());
        temp.setAddr(storeVo.getAddr().trim());
        temp.setBusinessScope(storeVo.getBusinessScope());
        temp.setProvinceCode(storeVo.getProvinceCode());
        temp.setCityCode(storeVo.getCityCode());
        temp.setCountyCode(storeVo.getCountyCode());
        temp.setProvince(storeVo.getProvince());
        temp.setCity(storeVo.getCity());
        temp.setCounty(storeVo.getCounty());
        temp.setAreaId(storeVo.getCountyCode());
        if (StringUtils.isBlank(storeVo.getServicePhone())) {
            temp.setServicePhone(user.getPhone());
        } else {
            temp.setServicePhone(storeVo.getServicePhone());
        }
        if (store.getStatus() == StoreType.FAILURE.getCode()) {
            temp.setStatus(StoreType.PENDING.getCode());
        }
        if (!StringUtils.isEmpty(storeVo.getLatitude()) && !StringUtils.isEmpty(storeVo.getLongitude())) {
            temp.setLongitude(storeVo.getLongitude());
            temp.setLatitude(storeVo.getLatitude());
        }
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getCounty());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getCity());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+storeVo.getProvince());
        storeService.update(user.getStoreId(), temp);
        return new JsonResult();
    }

    private void dealWithIdCard(NewEditStoreVo storeVo) throws Exception {
        if (storeVo.getIdcardIcons() != null && storeVo.getIdcardIcons().size() > 0) {
            //查询出所有的身份证图片
            List<Image> imageList = imageService.getByLinkId(storeVo.getId(), ImageType.IDCARD.getCode());
            if (ToolUtil.isEmpty(imageList)) {
                //没有数据就新增
                int j = 0;
                for (ImageVo img : storeVo.getIdcardIcons()) {
                    Image addModel = new Image();
                    addModel.setImageLinkId(storeVo.getId());
                    addModel.setType(ImageType.IDCARD.getCode());
                    addModel.setPath(img.getPath());
                    addModel.setRank(j);
                    addModel.setRemark(ImageType.IDCARD.getMsg());
                    imageService.add(addModel);
                    j++;
                }
            } else {
                //记录要删除的七牛数据
                List<String> delList = new ArrayList<>();
                if (storeVo.getIdcardIcons().size() <= imageList.size()) {
                    //编辑时图片数量小于原有图片数量
                    for (int i = 0; i < imageList.size(); i++) {
                        Image image = imageList.get(i);
                        String path = image.getPath();
                        Boolean isUpdate = false;
                        Image updateModel = new Image();
                        updateModel.setId(image.getId());
                        updateModel.setUpdateTime(new Date());
                        if (i < storeVo.getIdcardIcons().size()) {
                            isUpdate = image.getPath().equals(storeVo.getIdcardIcons().get(i).getPath());
                            if (!isUpdate) {
                                //覆盖原有数据
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getIdcardIcons().get(i).getPath());
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
                    for (int i = 0; i < storeVo.getIdcardIcons().size(); i++) {
                        Boolean isUpdate = false;
                        if (i < imageList.size()) {
                            Image image = imageList.get(i);
                            isUpdate = image.getPath().equals(storeVo.getIdcardIcons().get(i).getPath());
                            if (!isUpdate) {
                                //覆盖原有数据
                                Image updateModel = new Image();
                                updateModel.setId(image.getId());
                                updateModel.setUpdateTime(new Date());
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getIdcardIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }

                        } else {
                            //插入新的数据
                            Image addModel = new Image();
                            addModel.setType(ImageType.IDCARD.getCode());
                            addModel.setPath(storeVo.getIdcardIcons().get(i).getPath());
                            addModel.setImageLinkId(storeVo.getId());
                            addModel.setRank(i);
                            addModel.setRemark(ImageType.IDCARD.getMsg());
                            imageService.add(addModel);
                        }
                    }
                }
            }
            //删掉七牛图片
            delQiniuImage(storeVo.getBucket(),storeVo.getIdcardIcons(), imageList);
        }
    }

    private void dealWithStoreLicense(NewEditStoreVo storeVo) throws Exception {
        if (storeVo.getLicenseIcons() != null && storeVo.getLicenseIcons().size() > 0) {
            //查询出所有的商品资质图片
            List<Image> imageList = imageService.getByLinkId(storeVo.getId(), ImageType.LIENSCE.getCode());
            if (ToolUtil.isEmpty(imageList)) {
                int j = 0;
                for (ImageVo img : storeVo.getLicenseIcons()) {
                    //新增数据
                    Image addModel = new Image();
                    addModel.setImageLinkId(storeVo.getId());
                    addModel.setType(ImageType.LIENSCE.getCode());
                    addModel.setPath(img.getPath());
                    addModel.setRank(j);
                    addModel.setRemark(ImageType.LIENSCE.getMsg());
                    imageService.add(addModel);
                    j++;
                }
            } else {
                if (storeVo.getLicenseIcons().size() <= imageList.size()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        Image image = imageList.get(i);
                        String path = image.getPath();
                        Boolean isUpdate = false;
                        isUpdate = image.getPath().equals(storeVo.getLicenseIcons().get(i).getPath());
                        Image updateModel = new Image();
                        updateModel.setId(image.getId());
                        updateModel.setUpdateTime(new Date());
                        if (i < storeVo.getLicenseIcons().size()) {
                            isUpdate = image.getPath().equals(storeVo.getLicenseIcons().get(i).getPath());
                            if (!isUpdate) {
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getLicenseIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }

                        } else {
                            updateModel.setStatus(StatusType.TRUE.getCode());
                            imageService.updateById(image.getId(), updateModel);
                        }
                    }
                } else {
                    for (int i = 0; i < storeVo.getLicenseIcons().size(); i++) {

                        Boolean isUpdate = false;
                        if (i < imageList.size()) {
                            Image image = imageList.get(i);
                            isUpdate = image.getPath().equals(storeVo.getLicenseIcons().get(i).getPath());
                            if (!isUpdate) {
                                Image updateModel = new Image();
                                updateModel.setId(image.getId());
                                updateModel.setUpdateTime(new Date());
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getLicenseIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }

                        } else {
                            Image addModel = new Image();
                            addModel.setType(ImageType.LIENSCE.getCode());
                            addModel.setPath(storeVo.getLicenseIcons().get(i).getPath());
                            addModel.setImageLinkId(storeVo.getId());
                            addModel.setRank(i);
                            addModel.setRemark(ImageType.LIENSCE.getMsg());
                            imageService.add(addModel);
                        }
                    }
                }
            }
            //删掉七牛图片
            delQiniuImage(storeVo.getBucket(),storeVo.getLicenseIcons(), imageList);
        }
    }


    private void dealWithStoreIcons(NewEditStoreVo storeVo) throws Exception {
        if (storeVo.getIcons() != null && storeVo.getIcons().size() > 0) {
            //查询出所有的店铺图片
            List<Image> imageList = imageService.getByLinkId(storeVo.getId(), ImageType.STORE.getCode());
            if (ToolUtil.isEmpty(imageList)) {
                int j = 0;
                for (ImageVo img : storeVo.getIcons()) {
                    //新增数据
                    Image addModel = new Image();
                    addModel.setImageLinkId(storeVo.getId());
                    addModel.setType(ImageType.STORE.getCode());
                    addModel.setPath(img.getPath());
                    addModel.setRank(j);
                    addModel.setRemark(ImageType.STORE.getMsg());
                    imageService.add(addModel);
                    j++;
                }
            } else {
                if (storeVo.getIcons().size() <= imageList.size()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        Image image = imageList.get(i);
                        String path = image.getPath();
                        Boolean isUpdate = false;

                        Image updateModel = new Image();
                        updateModel.setId(image.getId());
                        updateModel.setUpdateTime(new Date());
                        if (i < storeVo.getIcons().size()) {
                            isUpdate = image.getPath().equals(storeVo.getIcons().get(i).getPath());
                            if (!isUpdate) {
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }


                        } else {
                            updateModel.setStatus(StatusType.TRUE.getCode());
                            imageService.updateById(image.getId(), updateModel);
                        }
                    }
                } else {
                    for (int i = 0; i < storeVo.getIcons().size(); i++) {

                        Boolean isUpdate = false;

                        if (i < imageList.size()) {
                            Image image = imageList.get(i);
                            String path = image.getPath();
                            isUpdate = image.getPath().equals(storeVo.getIcons().get(i).getPath());
                            if (!isUpdate) {
                                Image updateModel = new Image();
                                updateModel.setId(image.getId());
                                updateModel.setUpdateTime(new Date());
                                updateModel.setStatus(StatusType.FALSE.getCode());
                                updateModel.setPath(storeVo.getIcons().get(i).getPath());
                                imageService.updateById(image.getId(), updateModel);
                            }

                        } else {
                            Image addModel = new Image();
                            addModel.setType(ImageType.STORE.getCode());
                            addModel.setPath(storeVo.getIcons().get(i).getPath());
                            addModel.setImageLinkId(storeVo.getId());
                            addModel.setRank(i);
                            addModel.setRemark(ImageType.STORE.getMsg());
                            imageService.add(addModel);
                        }
                    }
                }
            }
            //删掉七牛图片
            delQiniuImage(storeVo.getBucket(),storeVo.getIcons(), imageList);
        }
    }

    /**
     * 删掉七牛图片
     * @param imageVoList
     * @param imageList
     */
    private void delQiniuImage(String bucket,List<ImageVo> imageVoList, List<Image> imageList) {
        List<String> listA = new ArrayList<>();
        List<String> listB = new ArrayList<>();
        for (ImageVo imageVo:imageVoList){
            if (ToolUtil.isNotEmpty(imageVo.getPath())){
                if (imageVo.getPath().indexOf("http://")>-1){
                    listA.add(imageVo.getPath().substring(imageVo.getPath().indexOf("http://")+1,imageVo.getPath().length()));
                }
            }
        }
        for (Image image:imageList){
            if (ToolUtil.isNotEmpty(image.getPath())){
                listB.add(image.getPath().substring(image.getPath().indexOf("http://")+1,image.getPath().length()));
            }
        }
        ImageUtils.delQiniuImage(bucket,listA,listB);
    }


}
