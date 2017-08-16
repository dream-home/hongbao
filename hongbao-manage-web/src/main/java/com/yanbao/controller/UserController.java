package com.yanbao.controller;

import com.yanbao.constant.RecordType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.*;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.RechargeVo;
import com.yanbao.vo.RecordVo;
import com.yanbao.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 会员信息管理类
 */

@Controller
@RequestMapping("/member")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private WalletExchangeService walletExchangeService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private GoodsIssueDetailService issueDetailService;
    @Autowired
    private BankService bankService;
    @Autowired
    private UserBankcardService userBankcardService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private GoodsWinService goodsWinService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public JsonResult list(HttpServletRequest request, UserVo userVo, Page page) {
        User user = new User();
        user.setFromTime(userVo.getFromTime());
        user.setToTime(userVo.getStopTime());
        user.setUid(userVo.getUid());
        user.setPhone(userVo.getPhone());
        user.setId(userVo.getId());
        int count = userService.readCount(user);
        List<UserVo> users = userService.readUserVoList(userVo, page.getStartRow(), page.getPageSize());
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, users);
        return success(pageResult);
    }

    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(User user, UserBankcard userBankcard, UserAddress userAddress, HttpServletRequest request) {
        if(user.getStatus()==0){
            Strings.del("token_api_"+user.getId());
        }
        userBankcard.setUserId(user.getId());
        userBankcard.setStatus(null);
        UserBankcard userBankcard1=new UserBankcard();
        userBankcard1.setUserId(user.getId());
        userBankcard1=userBankcardService.readOne(userBankcard1);
        if (userBankcard1==null){
            userBankcardService.createWithUUID(userBankcard);
        }else{

            userBankcardService.updateByUserId(userBankcard);
        }
        userService.updateById(user.getId(), user);
        userAddress.setUserId(user.getId());
        userAddress.setStatus(null);
        UserAddress userAddress1=new UserAddress();
        userAddress1.setUserId(user.getId());
        userAddress1=userAddressService.readOne(userAddress1);
        if (userAddress1==null){
            userAddressService.createWithUUID(userAddress);
        }else {

            userAddressService.updateByUserId(userAddress);
        }
        return success();
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(String userId) {
        userService.deleteById(userId);
        return success();
    }

    @ResponseBody
    @RequestMapping("/detail")
    public JsonResult detail(String userId) {
        User user = userService.readById(userId);
        if (user == null) {
            return fail("用户不存在");
        }
        UserVo userVo=new UserVo();
        userVo.setId(user.getId());
        userVo.setHeadImgUrl(user.getHeadImgUrl());
        userVo.setUid(user.getUid());
        userVo.setScore(user.getScore());
        userVo.setNickName(user.getNickName());
        userVo.setWeixin(user.getWeixin());
        userVo.setFirstReferrer(user.getFirstReferrer());
        userVo.setPhone(user.getPhone());
        userVo.setStatus(user.getStatus());
        userVo.setUserName(user.getUserName());
        userVo.setCreateTime(user.getCreateTime());
        userVo.setIsKF(user.getIsKF());

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress = userAddressService.readOne(userAddress);

        if (userAddress!=null){
            userVo.setAddr(userAddress.getAddr());
        }

        UserBankcard userBankcard=new UserBankcard();
        userBankcard.setUserId(userId);
        userBankcard=userBankcardService.readOne(userBankcard);
        if (userBankcard!=null){
            userVo.setBankId(userBankcard.getBankId());
            userVo.setBankName(userBankcard.getBankName());
            userVo.setCardNo(userBankcard.getCardNo());
            userVo.setType(userBankcard.getType());
        }
        return success(userVo);
    }

    /**
     * 积分充值表
     *
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping("/rechargeRecords")
    public JsonResult recharge(Integer uid, Page page) {
        RechargeVo rechargeVo = new RechargeVo();
        rechargeVo.setUid(uid);
        int count = walletRechargeService.getRechargeVoCount(rechargeVo);
        if (count == 0) {
            return fail("没有充值记录");
        }
        List<RechargeVo> list = walletRechargeService.getRechargeVoList(rechargeVo, page.getStartRow(), page.getPageSize());
        PageResult result = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        
        return success(result);
    }

    /**
     * 人脉查询
     *
     * @param userId
     * @param underLine
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/underline")
    public JsonResult underline(String userId, Integer underLine, Page page) {
        User user = new User();
        if (underLine ==0) {
            int count = userService.getUnderlineAllCount(userId);
            if (count == 0) {
                return fail("没有人脉");
            }
            List users = userService.getUnderlineAll(userId, page.getStartRow(), page.getPageSize());
            PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, users);
            return success(pageResult);
        }
        if (underLine == 1) {
            user.setFirstReferrer(userId);
        } else if (underLine == 2) {
            user.setSecondReferrer(userId);
        } else if (underLine == 3) {
            user.setThirdReferrer(userId);
        }
        int count = userService.readCount(user);
        if (count == 0) {
            return fail("没有人脉");
        }
        List users = userService.readList(user, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, users);
        return success(pageResult);

    }

    /**
     * 获取会员提现记录
     *
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/withdrawRecord")
    public JsonResult withdrawRecord(WalletExchange walletExchange, Page page) {
        int count = walletExchangeService.readCount(walletExchange);
        List list = walletExchangeService.readList(walletExchange, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }

    /**
     * 审核提现
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/check")
    public JsonResult check(String id, int status) {
        WalletExchange walletExchange = new WalletExchange();
        walletExchange.setStatus(status);
        walletExchangeService.updateById(id, walletExchange);
        return success();
    }

    /**
     * 积分流水
     *
     * @param userId
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/pointRecord")
    public JsonResult pointRecord(String userId, Page page) {
        WalletRecord walletRecord = new WalletRecord();
        walletRecord.setUserId(userId);
        int count = walletRecordService.readCount(walletRecord);
        List list = walletRecordService.readList(walletRecord, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }
    
    
    /**
     * 用户详情积分流水
     *
     * @param userId
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/pointUserRecord")
    public JsonResult pointUserRecord(String userId, Page page) {
        RecordVo recordVo = new RecordVo();
        recordVo.setUserId(userId);
        int count = walletRecordService.getUserRecordVoCount(recordVo);
        List<RecordVo> list = walletRecordService.getUserRecordVoList(recordVo, page.getStartRow(), page.getPageSize());
        
        
        for(RecordVo recordVos:list){
        	recordVos.setRecordTypeName(RecordType.fromCode(recordVos.getRecordType()).getMsg());
        	
        }
        
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }
    

    /**
     * 会员竞拍记录
     *
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/auctionRecord")
    public JsonResult auctionRecord(GoodsIssueDetail issueDetail, Page page) {
        int count = issueDetailService.readCount(issueDetail);
        if (count == 0) {
            return fail("没有记录");
        }
        List list = issueDetailService.readList(issueDetail, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }
    
    
    
    //注销用户
    @ResponseBody
    @RequestMapping("/delete1")
    public JsonResult delete1(String id){
    	
    	//充值成功
    	WalletRecharge walletRecharge = new WalletRecharge();
    	walletRecharge.setStatus(2);
    	walletRecharge.setUserId(id);
    	
    	int count1 =  walletRechargeService.readCount(walletRecharge);
    	//扫码成功
    	WalletRecharge walletRecharge1 = new WalletRecharge();
    	walletRecharge1.setStatus(3);
    	walletRecharge1.setUserId(id);
    	int count2 =  walletRechargeService.readCount(walletRecharge1);
    	
    	//是否有购买东西
    	GoodsWin goodsWin = new GoodsWin();
    	goodsWin.setStatus(3);
    	goodsWin.setUserId(id);
    	
    	int count3 = goodsWinService.readCount(goodsWin);
    	
    	
    	
    	//查询是否有下线
    	User user  = new User();
    	user.setFirstReferrer(id);
    	
    	int count4 =   userService.readCount(user);
    			
    	
    	String msg = "";
    	if(count1+count2>0){
    		msg ="该用户有充值记录";
    		return fail(msg);
    				
    	}
    	if(count3>0){
    		msg ="该用户有消费记录";
    		return fail(msg);
    	}
    	
    	if(count4>0){
    		msg = "该用户有下线";
    		return fail(msg);
    	}
    	
    	if(count1 ==0 && count2 ==0 && count3 ==0 && count4 ==0){
    		User user1  = new User();
    		user1.setStatus(0);
    		user1.setWeixin(UUIDUtil.getUUID());
    		user1.setRemark("该用户已注销");
    		userService.updateById(id, user1);
    		msg = "成功注销该用户";
    	}
    	
    	
    	
    	return success(msg);
    }
    
    
    
    
    

}
