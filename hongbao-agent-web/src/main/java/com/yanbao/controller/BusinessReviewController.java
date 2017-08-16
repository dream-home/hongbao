package com.yanbao.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yanbao.constant.RedisKey;
import com.yanbao.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.model.AgentInfo;
import com.mall.model.AgentMessage;
import com.mall.model.City;
import com.mall.model.Goods;
import com.mall.model.Message;
import com.mall.model.Store;
import com.mall.model.User;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.page.JsonResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.AgentInfoService;
import com.yanbao.service.GoodsService;
import com.yanbao.service.HbAgentMessageService;
import com.yanbao.service.MessageService;
import com.yanbao.service.StoreService;
import com.yanbao.service.SysCityService;
import com.yanbao.service.SysFileLinkService;
import com.yanbao.service.UserService;
import com.yanbao.vo.MessagePush;
/***
 * 商家审核
 */
@Controller
@RequestMapping("/business")
public class BusinessReviewController extends BaseController{
	
	@Autowired 
    private StoreService storeService; 
	@Autowired 
    private UserService userService;
	@Autowired 
    private MessageService messageService;
	@Autowired 
    private SysFileLinkService sysFileLinkService;
	@Autowired 
    private GoodsService goodsService;
	 //当前类权限id
    private static String classPermissionId="3";
	
	
    private static final Logger logger = LoggerFactory.getLogger(BusinessReviewController.class);
    
    //商铺审核接口
    @ResponseBody
    @RequestMapping(value="/shopCheck",method = RequestMethod.POST)
    public JsonResult checkStore(HttpServletRequest request,String id,Integer status,String failureCause) {
    	if(!ispermission(classPermissionId, request)){
    		return new JsonResult(2,"无权限");
    	}
        Store store = storeService.readById(id);
        Store model = new Store();
        if (store == null) {
        	return new JsonResult(1,"店铺不存在");
        }
        model.setStatus(status);
        if (status == 2){
            model.setMessage(failureCause);
        }else{
            model.setMessage("");
        }
        storeService.updateById(id, model);
        User user = userService.readById(store.getUserId());
        Message message = new Message();
        message.setUserId(store.getUserId());
        message.setType(0);
        message.setStatus(0);
        message.setRemark(getToken(request).getNickName());
        if (status == 1) {
            message.setTitle("商铺申请审核成功");
            message.setDetail("恭喜，你申请的店铺" + store.getStoreName() + "在" + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + "审核成功！ 可以开始发布商品!");
            //极光推送
            boolean rs = JPushUtil.pushPayloadByid(user.getRegistrationId(), "您的商铺已通过审核",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.SYSTEM_MSM));
        } else if (status == 2) {
            sysFileLinkService.deleteById(store.getIDCardIcon());
            sysFileLinkService.deleteById(store.getStoreLicense());
            message.setTitle("店铺审核失败");
            message.setDetail("你申请的店铺" + store.getStoreName() + "在" + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + "审核不通过！ 请重新提交资料审核!");
            //极光推送
            boolean rs = JPushUtil.pushPayloadByid(user.getRegistrationId(), "您的商铺未通过审核，请尽快处理",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.SYSTEM_MSM));
        }
        
        messageService.createWithUUID(message);
        return new JsonResult();
    }
    //关闭商家接口
    /**
     * 商家禁用 则 冻结 商铺 关闭帐号 下架商品
     *
     * @param id
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping("/isBan")
    public JsonResult isBan(String id, Integer status, HttpServletRequest request,String failureCause) {
    	if(!ispermission(classPermissionId, request)){
    		return new JsonResult(2,"无权限");
    	}
        Store store = storeService.readById(id);
        if (store == null) {
            return fail("店铺不存在");
        }
        store.setStatus(status);
        if (status == 3) {
        	store.setMessage(failureCause);
        }else{
        	store.setMessage("");
        }
        User user = userService.readById(store.getUserId());
        Message message = new Message();
        message.setUserId(store.getUserId());
        message.setType(0);
        message.setStatus(0);
        message.setTitle("店铺消息");
        message.setRemark(getToken(request).getNickName());
        if (user == null) {
            return fail("用户不存在");
        }
        if (status == 3) {
            //修改商铺为审核中状态，提供资料重新审核
            storeService.updateById(id, store);
            //App4.1改版,不禁用商户会员账号，只禁用商铺，并下架商品，删除token
            Strings.del("token_api_"+user.getId());
            message.setDetail("您的店铺已被关闭,关闭原因："+failureCause);
            //商铺违规，根据商铺id下架该商铺中的所有商品
            Goods goods = new Goods();
            goods.setStoreId(store.getId());
            goods.setStatus(0);
            goodsService.proDown(goods);
            //极光推送
            boolean rs = JPushUtil.pushPayloadByid(user.getRegistrationId(), "您的商铺已经被关闭，请尽快处理",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.SYSTEM_MSM));
        } else if (status == 1) {
            //修改商铺状态为审核通过，表明该商铺解封，商家会员状态不作任何修改
            storeService.updateById(id, store);
//            user.setStatus(1);
            Strings.del("token_api_"+user.getId());
//            userService.updateById(user.getId(), user);
            message.setDetail("解封商家成功");
           
        }
        messageService.createWithUUID(message);
        //删除商家定位
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getCounty());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getCity());
        ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getProvince());
        return success();
    }
}
