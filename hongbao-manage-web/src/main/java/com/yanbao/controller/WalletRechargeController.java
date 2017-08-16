package com.yanbao.controller;

import com.yanbao.constant.BankCardType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Admin;
import com.mall.model.User;
import com.mall.model.WalletRecharge;
import com.mall.model.WalletRecord;
import com.yanbao.service.AdminService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.service.WalletRecordService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.RechargeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by summer on 2016-12-15:14:19;
 * 用户充值 查询
 */
@Controller
@RequestMapping("/recharge")
public class WalletRechargeController extends BaseController {

    @Autowired
    WalletRechargeService walletRechargeService;
    @Autowired
    UserService userService;
    @Autowired
    WalletRecordService walletRecordService;
    @Autowired
    AdminService adminService;
    @Value("${systemSalt}")
    String salt;


    /**
     * 管理员充值
     * 充值 暂时没有手续费 未来可能加上
     */
    @ResponseBody
    @RequestMapping("/addScore")
    public JsonResult addMoney(int uid, double score, String password, HttpServletRequest request){
        Token token=getToken(request);
        Admin admin=adminService.readById(token.getId());
        if (!admin.getPassword().equals(Md5Util.md5(password+salt))){
            return fail("管理员密码错误");
        }
        User user=new User();
        user.setUid(uid);
        user=userService.readOne(user);
        if (user==null||user.getStatus()!=1){
            return fail("充值失败,用户不存在或被禁用");
        }
        int a=userService.addScoreByUserId(user.getId(),score);
        if (a!=1){
            return fail("充值失败");
        }
        WalletRecharge walletRecharge=new WalletRecharge();
        walletRecharge.setId(UUIDUtil.getUUID());
        walletRecharge.setOrderNo(OrderNoUtil.get());
        walletRecharge.setUserId(user.getId());
        walletRecharge.setScore(score);
        walletRecharge.setConfirmScore(score);
        walletRecharge.setPoundage(0.0);
        walletRecharge.setStatus(2);
        walletRecharge.setSource(BankCardType.ADMIN_RECHARGE.getCode());
        walletRecharge.setRemark(admin.getNickName());
        walletRechargeService.createWithUUID(walletRecharge);
        WalletRecord walletRecord=new WalletRecord();
        walletRecord.setUserId(user.getId());
        walletRecord.setOrderNo(walletRecharge.getOrderNo());
        walletRecord.setRecordType(0);
        walletRecord.setScore(score);
        walletRecord.setProvince("");
        walletRecord.setCity("");
        walletRecord.setCounty("");
        walletRecord.setProvince("");
        walletRecord.setRemark("手工充值");
        walletRecordService.createWithUUID(walletRecord);
        return success();
    }
    
    
    
    @ResponseBody
    @RequestMapping("/partner")
    public JsonResult partner(RechargeVo recharge,Page page){
    	
    	
    	int count = walletRechargeService.readCountPartner(recharge);
    	if(count ==0){
    		
    		return fail("没有数据");
    	}
    	
    	
    	List<RechargeVo>  list =  walletRechargeService.readListPartner(recharge, page.getStartRow(), page.getPageSize(),count);
    	
    	
    	
    	
    	
    	return success(new PageResult(page.getPageNo(), page.getPageSize(), count, list));
    	
    }
    
    
    


}
