package com.yanbao.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.mall.model.Image;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.mall.model.Goods;
import com.mall.model.Message;
import com.mall.model.Store;
import com.mall.model.SysFile;
import com.mall.model.SysFileLink;
import com.mall.model.User;
import com.yanbao.redis.Strings;
import com.yanbao.service.GoodsService;
import com.yanbao.service.HbImgageService;
import com.yanbao.service.MessageService;
import com.yanbao.service.StoreService;
import com.yanbao.service.SysFileLinkService;
import com.yanbao.service.SysFileService;
import com.yanbao.service.UserService;
import com.yanbao.util.CopyFileUtil;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.QiNiuUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.StoreVo;

/**
 * Created by summer on 2017-01-11:15:24;
 * <p>
 * 商家管理
 */
@Controller
@RequestMapping("/storeManage")
public class StoreController extends BaseController {

    @Autowired
    StoreService storeService;
    @Autowired
    MessageService messageService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    SysFileService sysFileService;
    @Autowired
    SysFileLinkService sysFileLinkService;
    
    
    @Autowired
    HbImgageService hbImgageService;
    
    /*
     * 开启(修改)微信店铺
     */
    @RequestMapping("/openWeChat")
    @ResponseBody
    public JsonResult openWeChat(StoreVo storeVo) {
    	Store store=storeService.readById(storeVo.getId());
    	if(null==store){
    		return fail("店铺未找到"); 
    	}
    	if(store.getStatus()!=1){
    		return fail("店铺不可操作");
    	}
    	/*String content=CopyFileUtil.getQrCodeContent(storeVo.getQrcodeUrl());
    	if(null==content){
    		return fail("二维码内容不正确");
    	}*/
    	Store updateStore=new Store();
    	if(null!=storeVo.getMenuUrl()&&!"".equals(storeVo.getMenuUrl())){
    		updateStore.setWeixinStatus(2); 
    		updateStore.setMenuUrl(storeVo.getMenuUrl());
    	}
    	updateStore.setQrcodeUrl(storeVo.getQrcodeUrl());
    	updateStore.setShareUrl("http://doupaimall.com/wxpage");
    	storeService.updateById(store.getId(), updateStore);
        return success();
    }
    /*
     * 关闭微信店铺
     */
    @RequestMapping("/closeWeChat")
    @ResponseBody
    public JsonResult closeWeChat(StoreVo storeVo,String namespace) {
    	Store store=storeService.readById(storeVo.getId());
    	if(null==store){
    		return fail("店铺未找到");
    	}
    	Store updateStore=new Store();
    	updateStore.setWeixinStatus(3); 
    	updateStore.setQrcodeUrl("");
    	updateStore.setShareUrl("");
    	updateStore.setMenuUrl("");
    	storeService.updateById(store.getId(), updateStore);
    	//删除二维码图片
    	if (ToolUtil.isNotEmpty(store.getQrcodeUrl())) {
    		String[] keyList={store.getQrcodeUrl()};
    		QiNiuUtil.batchDelFile(namespace, keyList);
        }
        return success();
    }
    
    
    /**
     * 商家列表
     *
     * @param storeVo
     * @param page
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult storeList(StoreVo storeVo, Page page) {
        int count = storeService.readVoCount(storeVo);
        if (count == 0) {
            return fail("没有资源");
        }
        List<StoreVo> storeVos = storeService.readVoList(storeVo, page);
        return success(getPageResult(page, count, storeVos));
    }

    /**
     * 审核商家入住资料
     *
     * @param id
     * @param status
     * @param request
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public JsonResult checkStore(String id, int status, HttpServletRequest request) {
        Store store = storeService.readById(id);
        store.setRemark(getToken(request).getNickName());
        if (store == null) {
            return fail("店铺不存在");
        }
        store.setStatus(status);
        storeService.updateById(id, store);
        Message message = new Message();
        message.setUserId(store.getUserId());
        message.setType(0);
        message.setStatus(0);
        message.setRemark(getToken(request).getNickName());
        if (status == 1) {
            message.setTitle("商铺申请审核成功");
            message.setDetail("恭喜，你申请的店铺" + store.getStoreName() + "在" + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + "审核成功！ 可以开始发布商品!");
        } else if (status == 2) {
        	                        
           sysFileLinkService.deleteById(store.getIDCardIcon());
           sysFileLinkService.deleteById(store.getStoreLicense());

            message.setTitle("店铺审核失败");
            message.setDetail("你申请的店铺" + store.getStoreName() + "在" + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + "审核不通过！ 请重新提交资料审核!");
        }
        messageService.createWithUUID(message);
        return success();
    }

    /**
     * 店铺推荐
     * @param id
     * @param isRecommend
     * @return
     */
    @ResponseBody
    @RequestMapping("/recommend")
    public JsonResult recommend(String id, int isRecommend) {
        Store store = storeService.readById(id);
        if (store == null) {
            return fail("店铺不存在");
        }
        store.setIsRecommend(isRecommend);
        return success();

    }

    /**
     * 商家禁用 则 冻结 商铺 关闭帐号 下架商品
     *
     * @param id
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping("/isBan")
    public JsonResult isBan(String id, int status, HttpServletRequest request) {
        Store store = storeService.readById(id);
        if (store == null) {
            return fail("店铺不存在");
        }
        store.setStatus(status);
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
        	//关闭微信店铺
        	if(null!=store.getWeixinStatus()&&store.getWeixinStatus()==2){
        		store.setWeixinStatus(3);
        	}
            //修改商铺为审核中状态，提供资料重新审核
            storeService.updateById(id, store);

            //App4.1改版,不禁用商户会员账号，只禁用商铺，并下架商品，删除token
//            user.setStatus(0);
            Strings.del("token_api_"+user.getId());
//            userService.updateById(user.getId(), user);
            message.setDetail("由于违规，商铺已被冻结，请提供资料，联系客服解封");

            //商铺违规，根据商铺id下架该商铺中的所有商品
            Goods goods = new Goods();
            goods.setStoreId(store.getId());
            goods.setStatus(0);
            //goods.setRemark(getToken(request).getNickName());
            /*List<Goods> goodses = goodsService.readAll(goods);
            for (Goods goods1 : goodses) {
                goods1.setStatus(0);
                goodsService.updateById(goods1.getId(), goods1);
            }*/
            goodsService.proDown(goods);
            ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getCounty());
            ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getCity());
            ToolUtil.delRedisKey(RedisKey.STORE_CODE_PREFIX.getKey()+store.getProvince());

            

        } else if (status == 1) {
        	//重新开启微信店铺
        	if(null!=store.getMenuUrl()&&!"".equals(store.getMenuUrl())){
        		store.setWeixinStatus(2);
        	}
            //修改商铺状态为审核通过，表明该商铺解封，商家会员状态不作任何修改
            storeService.updateById(id, store);
//            user.setStatus(1);
            Strings.del("token_api_"+user.getId());
//            userService.updateById(user.getId(), user);
            message.setDetail("解封商家成功");
           
        }
        messageService.createWithUUID(message);
        return success();
    }

    /**
     * 店铺详情
     *
     * @param storeId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public JsonResult storeDetail(String storeId) {
        Store store = storeService.readById(storeId);
        if (store == null) {
            return fail("店铺不存在");
        }
        Goods goods = new Goods();
        goods.setStatus(1);
        goods.setStoreId(storeId);
        goods.setFirstReferrerScale(null);
        goods.setSecondReferrerScale(null);
        goods.setThirdReferrerScale(null);
        goods.setBusinessSendEp(null);
        goods.setDiscountEP(null);
        int count = goodsService.readCount(goods);
        List<String> urls = new ArrayList<String>();
        SysFileLink fileLink = new SysFileLink();
        fileLink.setStatus(1);
        fileLink.setLinkId(storeId);
        fileLink.setLinkType(0);
        
        
        
        
        /*新表的image数据*/
        Image image = new Image();
        image.setImageLinkId(storeId);
        image.setStatus(0);
        List<Image> images   =     hbImgageService.getPoList(image);
        if(images.size()>0){
        	 
           for(Image img:images){
        	   	urls.add(img.getPath());
 
        	   
           }
        }else{
        	
        	List<SysFileLink> sysFileLinks = sysFileLinkService.readAll(fileLink);
            for (SysFileLink sysFileLink : sysFileLinks) {
                SysFile sysFile = sysFileService.readById(sysFileLink.getFileId());
                if (sysFile != null) {
                    urls.add(sysFile.getPath());
                }
            }
        	
        	
        }    
        
        
        
        
        
        Map<String, Object> result = new HashedMap();
        result.put("urls", urls);
        result.put("detail", store);
        result.put("goodsCount", count);
        return success(result);
    }
    
    
    /**
     * 获取当前店铺的userId
     *zyc 2017-07-05 
     * @param storeId
     * @return
     */
    @RequestMapping("/detailUserId")
    @ResponseBody
    public JsonResult storeDetail2(String storeId) {
        Store store = storeService.readById(storeId);
        return success(store);
    }
    /**
     * 店铺资质审核图片
     * 
     */
    @RequestMapping("/AptitudeDetail")
    @ResponseBody
    public JsonResult aptitudeDetail(String storeId){
    	
    	Store store = storeService.readById(storeId);
    	List<String> IDCardIcons = new ArrayList<String>();
    	List<String> storeLicenses = new ArrayList<String>();
    	
    	
    	
    	/*新表的身份证image数据*/
        Image image = new Image();
        image.setImageLinkId(storeId);
        image.setStatus(0);
        image.setType(1);
        List<Image> images   =  hbImgageService.getPoList(image);
        if(images.size()>0){
           for(Image img:images){
        	   IDCardIcons.add(img.getPath());       	   
           }
        }
        
        /*新表的资质image数据*/
        Image image1 = new Image();
        image1.setImageLinkId(storeId);
        image1.setStatus(0);
        image1.setType(2);
        List<Image> images1   =  hbImgageService.getPoList(image1);
        if(images.size()>0){
           for(Image img:images1){
        	   storeLicenses.add(img.getPath());       	   
           }
        }
        
        
        if(images.size()>0 || images1.size()>0){
        	
        }else{
        	
        	 SysFileLink fileLink = new SysFileLink();
             fileLink.setStatus(1);
             fileLink.setLinkId(store.getIDCardIcon()==null?"0":store.getIDCardIcon());
             fileLink.setLinkType(0);
             List<SysFileLink> sysFileLinks = sysFileLinkService.readAll(fileLink);
         	if(sysFileLinks.size()>0){
         		for (SysFileLink sysFileLink : sysFileLinks) {
                     SysFile sysFile = sysFileService.readById(sysFileLink.getFileId());
                     if (sysFile != null) {
                     	IDCardIcons.add(sysFile.getPath());
                     }
                 }
         	}

         	SysFileLink fileLink1 = new SysFileLink();
             fileLink1.setStatus(1);
             fileLink1.setLinkId(store.getStoreLicense()==null?"0":store.getStoreLicense());
             fileLink1.setLinkType(0);
             List<SysFileLink> sysFileLinks1 = sysFileLinkService.readAll(fileLink1);
         	if(sysFileLinks1.size()>0){
         		for (SysFileLink sysFileLink : sysFileLinks1) {
                     SysFile sysFile = sysFileService.readById(sysFileLink.getFileId());
                     if (sysFile != null) {
                     	storeLicenses.add(sysFile.getPath());
                     }
                 }
         	}
             
        	
        	
        	
        	
        }
    	
    	
    	
    	
    	
    	
    	
    	
       
    	
    	 Map<String, Object> result = new HashedMap();
         result.put("IDCardIcons", IDCardIcons);
         result.put("detail", store);
         result.put("storeLicenses", storeLicenses);
    	

    	
    	return success(result);
    }
    
    
    
    
    
    
}
