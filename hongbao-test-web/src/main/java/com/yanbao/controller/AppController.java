package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/app")
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSortService goodsSortService;
    @Autowired
    private MallService mallService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private  GoodsWinService goodsWinService;
    @Autowired
    private  MallStoreService mallStoreService;
    @Autowired
    private OrderService orderService;

    // private static String
    // JP_URL="http://192.168.2.200:9010/api/mall/store/goods/draw";
    private static String JP_URL = "https://www.6pyun.com/api/mall/store/goods/draw";
    private static String INNER_TEST_URL = "https://www.6pyun.com/api/mall/store/goods/draw";
    private static String LOCAL_TEST_URL = "http://127.0.0.1:8090/api/mall/store/goods/draw";
    private static String TEST_200_URL = "http://192.168.2.200:9010/api/mall/store/goods/draw";

    @ResponseBody
    // @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult donate(String goodsId, Integer no) throws Exception {
        ThreadPoolManager manager = new ThreadPoolManager();
        final Set<String> tokenSet = Sets.smembers(RedisKey.ALL_TOKENS.getKey());
        logger.debug("***************** start ******************");
        int size = tokenSet.size();
        int i = 0;

        // UserVo userVo = (UserVo) TokenUtil.getTokenObject(token);
        // User user = userService.getById(userVo.getId());
        final Map<String, String> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("payPwd", "123456");
        /*
         * JSONObject jsonObject= HttpUtil.doPostRequest(JP_URL,
		 * JSON.toJSONString(map),token);
		 * System.out.println(jsonObject.toString()); logger.debug(
		 * "***************** res ******************  "+jsonObject.toString());
		 */
        for (int j = 0; j < no; j++) {
            manager.exec(new Runnable() {
                @Override
                public void run() {
                    for (final String token : tokenSet) {
                        JSONObject jsonObject = HttpUtils.doPostRequest(JP_URL, JSON.toJSONString(map), token);
                        System.out.println(jsonObject.toString());
                        logger.debug("***************** res ******************  " + jsonObject.toString());
                    }
                }
            });
        }

        return new JsonResult();
    }

    @ResponseBody
    // @RequestMapping(value = "/push", method = RequestMethod.GET)
    public JsonResult push(String id) {
        try {
            JPushUtil.pushPayloadByid("13065ffa4e3a1241c56", "清浅测试 " + id);
            if (!StringUtils.isEmpty(id)) {
                JPushUtil.pushPayloadByid(id, "for all " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult();
    }

    @ResponseBody
    // @RequestMapping(value = "/batchtest", method = RequestMethod.GET)
    public JsonResult batchtest(Page page, String goodsSortId) throws Exception {
        ThreadPoolManager manager = new ThreadPoolManager();
        User condition = new User();
        PageResult<User> result = userService.getPage(condition, page);
        List<User> rows = result.getRows();
        if (rows == null || rows.size() < 0) {
            return new JsonResult(result);
        }
        List<Map<String, Object>> rows2 = new ArrayList<Map<String, Object>>();
        for (User user : rows) {
            // 登录
            String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
            Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
            Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
            if (logger.isInfoEnabled()) {
                logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
            }
        }
        List<GoodsSort> goodsSortList = goodsSortService.getList();
        for (GoodsSort sort : goodsSortList) {
            PageResult<Goods> goodsPgae = goodsService.getPage(sort.getId(), page);
            List<Goods> goodsList = goodsPgae.getRows();
            for (Goods goods : goodsList) {
                Set<String> tokenSet = Sets.smembers(RedisKey.ALL_TOKENS.getKey());
                logger.debug("***************** start ******************");
                for (final String token : tokenSet) {
                    final Map<String, String> map = new HashMap<>();
                    map.put("goodsId", goods.getId());
                    map.put("payPwd", "123456");
                    manager.exec(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = HttpUtils.doPostRequest(JP_URL, JSON.toJSONString(map), token);
                            System.out.println(jsonObject.toString());
                            logger.debug("***************** res ******************  " + jsonObject.toString());
                        }
                    });
                }
            }
        }
        return new JsonResult();
    }

    @ResponseBody
    // @RequestMapping(value = "/drawtest", method = RequestMethod.GET)
    public JsonResult finishDrawHandlerTest(String goodsId, String issueId) throws Exception {
        // EEABA1DFB1C14781897B75B017A059D0
        // D752E32D793B483D85653A98F56EEE95
        Goods goods = goodsService.getById(goodsId);
        GoodsIssue issue = goodsIssueService.getById(issueId);
        mallService.finishDrawHandlerTest(goods, issue);
        return new JsonResult();
    }

    @ResponseBody
    @RequestMapping(value = "/mytest", method = RequestMethod.GET)
    public JsonResult mytest(String goodsId, String uid, Integer no) throws Exception {
        Set<String> tokenSetLast = Sets.smembers(RedisKey.ALL_TEST_TOKENS.getKey());
        for (String setMem : tokenSetLast) {
            Sets.srem(RedisKey.ALL_TEST_TOKENS.getKey(), setMem);
        }
        Integer[] innerIdArray = {200362, 200363, 200364, 200828, 200687, 200810, 200849, 200826, 200606};
        String[] goodsIdArray = {"9E1BC04784094B5AB6453DF3DF812506", "768DB795AC2F42F8B50DFFB00626626B"};
        Map<Integer, String> userMap = new HashMap<>();
        userMap.put(200362, "123456");
        userMap.put(200363, "123456");
        userMap.put(200364, "123456");
        userMap.put(200828, "123456");
        userMap.put(200687, "123456");
        userMap.put(200810, "123456");
        userMap.put(200849, "123456");
        userMap.put(200826, "123456");
        userMap.put(200606, "123456");

        ThreadPoolManager manager = new ThreadPoolManager();
        if (uid == null) {
            return new JsonResult("UID不能为空");
        }
        if (no == null || no == 0) {
            no = 1;
        }
        if (!Arrays.asList(goodsIdArray).contains(goodsId)) {
            return new JsonResult("商品id输入有误");
        }
        List<Integer> idList = new ArrayList<>();
        String[] ids = uid.split(",");
        for (String id : ids) {
            Integer innerUid = Integer.valueOf(id);
            if (Arrays.asList(innerIdArray).contains(innerUid)) {
                idList.add(innerUid);
            }
        }
        if (CollectionUtils.isEmpty(idList)) {
            return new JsonResult("uid不合法");
        }

        List<User> idUserList = userService.getInnerList(idList);
        if (CollectionUtils.isEmpty(idUserList)) {
            return new JsonResult("未找到用户");
        }

        for (User user : idUserList) {
            // 登录
            String token = TokenUtil.generateToken(user.getId(), user.getUserName(), user.getNickName());
            Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
            Sets.sadd(RedisKey.ALL_TEST_TOKENS.getKey(), token);
            Sets.expireSetSecond(RedisKey.ALL_TEST_TOKENS.getKey(), RedisKey.ALL_TEST_TOKENS.getSeconds());
            if (logger.isInfoEnabled()) {
                logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
            }
        }
        // 开始竞拍
        final Map<String, String> map = new HashMap<>();
        map.put("goodsId", goodsId);
        final Set<String> tokenSet = Sets.smembers(RedisKey.ALL_TEST_TOKENS.getKey());
        int tokenSize = tokenSet.size();
        Random rand = new Random();
        int i = rand.nextInt(tokenSize);
        StringBuffer sf = new StringBuffer();
        for (int j = 0; j < no; j++) {
            for (final String token : tokenSet) {
                Token userToken = (Token) TokenUtil.getTokenObject(token);
                User user = userService.getById(userToken.getId());
                sf.append("用户uid为：").append(user.getUid()).append(" 昵称是：").append(user.getNickName()).append(" ,参与了竞拍,");
                map.put("payPwd", userMap.get(user.getUid()));
                /*
                 * manager.exec(new Runnable() {
				 * 
				 * @Override public void run() { // JSONObject jsonObject =
				 * HttpUtil.doPostRequest(INNER_TEST_URL,
				 * JSON.toJSONString(map), token); JSONObject jsonObject =
				 * HttpUtil.doPostRequest(TEST_200_URL, JSON.toJSONString(map),
				 * token); logger.debug(
				 * "***************** res ******************  " +
				 * jsonObject.toString());
				 * sf.append(jsonObject.toString()+"\n"); } });
				 */
                JSONObject jsonObject = HttpUtils.doPostRequest(INNER_TEST_URL, JSON.toJSONString(map), token);
                logger.debug("***************** res ******************  " + jsonObject.toString());
                sf.append(jsonObject.toString() + "\n");

            }

        }

        return new JsonResult(sf.toString());
    }

    @ResponseBody
    @RequestMapping(value = "/updategroup", method = RequestMethod.GET)
    public JsonResult updateGroupCount(String userId, Page page) throws Exception {
        List<User> users = userService.getUpdateList(page);
        String finalGroupType = "";
        if (StringUtils.isEmpty(userId)) {
            for (User user : users) {
                userService.updateGroupCount(user.getId(), finalGroupType);
            }
        } else {
            userService.updateGroupCount(userId, finalGroupType);
        }
        return new JsonResult();
    }

    @ResponseBody
    @RequestMapping(value = "/checkgroup", method = RequestMethod.GET)
    public JsonResult checkGroup(String userId) throws Exception {
        if (!StringUtils.isEmpty(userId)) {
            userService.checkGroupNo(userId);
        }
        return new JsonResult();
    }

    /**
     * SOS接口
     * @param request
     * @param orderNo
     * @param auth
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/callscan", method = RequestMethod.GET)
    public JsonResult callScan(HttpServletRequest request ,String orderNo,String auth) throws Exception {
        JsonResult result=null;
        String uri = request.getRequestURI();
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        WalletRecharge model = walletRechargeService.getUserOrderByOrderNo(orderNo);
        if (null == model) {
            return new JsonResult("订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(model.getStoreUserId())){
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }

        if (model.getStatus() != RechargeType.PENDING.getCode()) {
            return new JsonResult("订单支付微信支付宝已经回调成功");
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }

        User user = null;
        //判断是否是支付宝网页扫码支付
        if(model.getSource() != BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode()){
            if (org.apache.commons.lang3.StringUtils.isEmpty(model.getUserId())) {
                return new JsonResult("用户id不能为空");
            }
            user = userService.getById(model.getUserId());
            if (user==null){
                return new JsonResult("用户不存在");
            }
        }

        Boolean isSuccess =false;
        //判断微信扫码支付方式：微信app内扫码支付，微信商家固定二维码app内扫码支付，微信商家固定二维码微信客户端直接发起扫码支付,加入合伙人
        if (model.getSource()== BankCardType.SCAN_CODE_WEIXIN.getCode().intValue() || model.getSource()==BankCardType.STORE_SCAN_APP_WEIXIN.getCode().intValue() || model.getSource()==BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode().intValue()|| model.getSource()==BankCardType.JOIN_WEIXIN.getCode().intValue()){
            isSuccess = WechatUtil.isAppPaySucess(orderNo);
            if (!isSuccess){
                return new JsonResult("微信扫码订单未支付");
            }
        }
        //判断支付宝是否是扫码支付，商家固定扫码app支付，商家固定扫码支付宝支付,加入合伙人
        if (model.getSource()== BankCardType.SCAN_CODE_ALIPAY.getCode().intValue() || model.getSource()==BankCardType.STORE_SCAN_APP_ALIPAY.getCode().intValue() || model.getSource()==BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode().intValue()|| model.getSource()==BankCardType.JOIN_ALIPAY.getCode().intValue()){
            isSuccess = WechatUtil.isAppScanAliPaySucess(orderNo);
            if (!isSuccess){
                return new JsonResult("支付宝扫码订单未支付");
            }
        }
        boolean b=false;
        b = model.getSource() == BankCardType.SCAN_CODE_WEIXIN.getCode().intValue() || model.getSource() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue();
        if (b && isSuccess){
            if (null == model || model.getStatus() != RechargeType.SUCCESS.getCode()) {
                return new JsonResult("面对面扫码支付订单不需要处理：订单状态--》"+model.getStatus()+"\n"+JSON.toJSONString(model));
            }
            walletRechargeService.scanCodeHandler(user, orderNo);
            result = new JsonResult("面对面扫码支付手工处理成功");
        }
        b = model.getSource() == BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_APP_WEIXIN.getCode().intValue() ||  model.getSource() == BankCardType.STORE_SCAN_APP_ALIPAY.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode().intValue();
        if (b && isSuccess){
            if (null == model || model.getStatus() != RechargeType.PENDING.getCode()) {
                return new JsonResult("商家二维码扫码支付订单不需要处理：订单状态--》"+model.getStatus()+"\n"+JSON.toJSONString(model));
            }
            walletRechargeService.storeScanCodeHandler(user, orderNo);
            result = new JsonResult("商家二维码扫码支付手工处理成功");
        }
        b = model.getSource() == BankCardType.JOIN_BALANCE.getCode().intValue();
        if (b && isSuccess){
            if (null == model || model.getStatus() != RechargeType.PENDING.getCode()) {
                return new JsonResult("加入合伙人支付订单不需要处理：订单状态--》"+model.getStatus()+"\n"+JSON.toJSONString(model));
            }
            walletRechargeService.joinPartnerHandler(user, orderNo);
            result = new JsonResult("加入合伙人手工处理成功");
        }
        return new JsonResult("callscan 处理成功");
    }

    @ResponseBody
    @RequestMapping(value = "/callh5", method = RequestMethod.GET)
    public JsonResult callh5(HttpServletRequest request,String orderNo,String auth) throws Exception {
        String uri = request.getRequestURI();
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        GoodsWin goodsWin = goodsWinService.getUserOrderByOrderNo(orderNo);
        if (goodsWin==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(goodsWin.getUserId())) {
            return new JsonResult("用户id不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }
        User user = userService.getById(goodsWin.getUserId());
        if (user==null){
            return new JsonResult("用户不存在");
        }
        if (goodsWin.getStatus() != GoodsWinType.PENDING.getCode()) {
            return new JsonResult("订单支付宝微信已经回调成功");
        }
        Boolean isSuccess = WechatUtil.isH5PaySucess(orderNo);
        if (!isSuccess){
            return new JsonResult("微信分享购买订单未支付");
        }
        mallStoreService.purchaseGoodsWinbyH5(orderNo,user.getId());
        return new JsonResult("callh5 处理成功");
    }



    /**
     * 积分充值回调(手工调用，备用)
     */
    @ResponseBody
    @RequestMapping(value = "/callpay", method = RequestMethod.GET)
    public JsonResult callPay(HttpServletRequest request,String orderNo, String auth) throws Exception {
        String uri = request.getRequestURI();
        if(org.apache.commons.lang3.StringUtils.isEmpty(orderNo) ){
            return new JsonResult("orderNo不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }
        WalletRecharge model = walletRechargeService.getUserOrderByOrderNo(orderNo);
        if (null == model) {
            return new JsonResult("订单号不存在");
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(model.getStoreUserId())){
            return new JsonResult("此订单不是充值订单，接口调用错误");
        }
        if (model.getStatus() != RechargeType.PENDING.getCode()) {
            return new JsonResult("订单已经充值成功（微信支付宝已回调）");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(model.getUserId())) {
            return new JsonResult("用户id不能为空");
        }
        Boolean isSuccess =false;
        if (model.getSource()== BankCardType.WECHATPAY.getCode().intValue()){
            isSuccess = WechatUtil.isAppPaySucess(orderNo);
            if (!isSuccess){
                return new JsonResult("微信钱包充值订单未支付");
            }
        }
        if (model.getSource()== BankCardType.ALIPAY.getCode().intValue()){
            isSuccess = WechatUtil.isAppScanAliPaySucess(orderNo);
            if (!isSuccess){
                return new JsonResult("支付宝钱包充值订单未支付");
            }
        }
        User user = userService.getById(model.getUserId());
        if (user==null){
            return new JsonResult("用户不存在");
        }
        walletRechargeService.rechargeHandler(user, orderNo);
        return new JsonResult("callpay 处理成功");
    }

    /**
     * 微信app直接购买对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/callwxapporder", method = RequestMethod.GET)
    public JsonResult wxapporder(HttpServletRequest request,String orderNo, String auth) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        GoodsWin goodsWin = goodsWinService.getUserOrderByOrderNo(orderNo);
        if (goodsWin==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(goodsWin.getUserId())) {
            return new JsonResult("用户id不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }
        User user = userService.getById(goodsWin.getUserId());
        if (user==null){
            return new JsonResult("用户不存在");
        }
        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信app直接购买订单已支付成功");
        }
        Boolean isSucess = WechatUtil.isAppPaySucess(orderNo);
        String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
        Boolean flag = RedisLock.redisLock(key, orderNo, 2);
        if (flag && isSucess) {
            //处理业务
            orderService.purchaseHandlerByApp(user, orderNo);
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信app直接购买订单支付确认回调成功");
        }
        if (goodsWin.getStatus().intValue() == GoodsWinType.BUYED.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信app直接购买订单微信已经回调成功");
        }
        logger.error("0000000000---> 微信app直接购买支付失败");
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }

    /**
     * 支付宝APP直接购买对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/callalipayapporder", method = RequestMethod.GET)
    public JsonResult alipayAppOrder(HttpServletRequest request,String orderNo, String auth) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        GoodsWin goodsWin = goodsWinService.getUserOrderByOrderNo(orderNo);
        if (goodsWin==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(goodsWin.getUserId())) {
            return new JsonResult("用户id不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }
        User user = userService.getById(goodsWin.getUserId());
        if (user==null){
            return new JsonResult("用户不存在");
        }

        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝APP直接购买订单已支付成功");
        }

        Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
        String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
        Boolean flag = RedisLock.redisLock(key,orderNo, 6);

        if (flag && isSucess) {
            //处理业务
            orderService.purchaseHandlerByApp(user, orderNo);
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝APP直接购买订单支付确认回调成功");
        }
        if (goodsWin.getStatus().intValue() == GoodsWinType.BUYED.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝APP直接购买订单微信已经回调成功");
        }

        logger.error("0000000000---> 支付宝APP直接购买支付失败");
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 后台人工新增账号
     * @param uid  根节点推荐人
     * @param childCount 每个推荐人推荐多少个子账号
     * @param layer 多少层级关系
     */
    @ResponseBody
    @RequestMapping(value = "/callAddUser", method = RequestMethod.GET)
    public JsonResult addUser(HttpServletRequest request,Integer uid,Integer childCount,Integer layer) throws Exception {
        User cond = new User();
        cond.setUid(uid);
        User parent = userService.getByCondition(cond);
        if(parent == null){
            return new JsonResult(1,"推荐人为空");
        }
        if(childCount == null || childCount <0){
            return new JsonResult(2,"childCount参数错误！！！");
        }
        if(layer == null || layer <0){
            return new JsonResult(3,"layer参数错误！！！");
        }
        List<Integer> uidList = new ArrayList<>();
        List<User> list = new ArrayList<User>();
        list.add(parent);
        String msg="";
        int m=0;
        int n=0;
        logger.error("开始层级为："+parent.getLevles());
        for (int i = 0; i < layer; i++) {
            List<User> list2 = new ArrayList<User>();
            msg+="第"+(i+1)+"层：";
            for(User user:list) {
                n++;
                logger.error("执行到第"+n+"层");
                msg+="上级："+user.getUid()+">>下级：";
                for (int j = 0; j < childCount; j++) {
                    logger.error("添加第"+m+"个账户");
                    m++;
                    User newUser = new User();
                    newUser.setFirstReferrer(user.getId());
                    newUser.setSecondReferrer(user.getFirstReferrer());
                    newUser.setThirdReferrer(user.getSecondReferrer());
                    newUser.setLevles(user.getLevles() + 1);
                    newUser.setLoginTime(null);
                    newUser.setUpdateTime(null);
                    newUser.setWeixin(UUIDUtil.getUUID()+"0000000");
                    newUser.setPassword("qwe123");
                    newUser.setPayPwd("123456");
                    userService.add(newUser);
                    newUser = userService.getById(newUser.getId());
                    User updateModel = new User();
                    updateModel.setNickName(newUser.getUid() + "");
                    updateModel.setHeadImgUrl("http://user.doupaimall.com/userDefault.png");
                    uidList.add(newUser.getUid());
                    userService.update(newUser.getId(), updateModel);
                    // 异步处理分组业务
                    final String userId = newUser.getId();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                //客户端微信注册登录，用户根据系统算法分组
                                userService.updateGroupCount(userId, "");
                            } catch (Exception e) {
                                logger.error("处理分组失败！！" + e.getMessage());
                            }
                        }
                    }).start();
                    list2.add(newUser);
                    msg+=newUser.getUid()+",";
                }
            }
            list.clear();
            list.addAll(list2);
        }
        return new JsonResult(msg);
    }

    private static double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static void main(String[] args) {
//        System.out.println(WechatUtil.isAppScanAliPaySucess("20170428115454480065"));
//        System.out.println(WechatUtil.isAppScanAliPaySucess("20170428115454480065"));
//          System.out.print(GetDistance(22.3662940847,113.5609553260,22.2680203098,113.5282144711));
        /*int test = 2;
        switch (test){
            case 0:
                System.out.println("0");
                break;
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
            case 3:
                System.out.println("3");
                break;
            case 4:
                System.out.println("4");
                break;
            default:
                System.out.println("other");
        }*/

        //定义一个数组
        Integer[] a = {1,2,3,4,5,6,7};

        List<Integer> a_int_List = Arrays.asList(a);

        //asList没有add和remove，会报错
        a_int_List.add(8);
        for(int str:a_int_List){//能遍历出各个元素
            System.out.println(str);
        }


    }
}
