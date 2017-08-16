package com.yanbao.controller;

import java.util.List;

import com.mall.model.Parameter;
import com.yanbao.service.ParameterService;
import com.yanbao.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Ad;
import com.yanbao.service.ADService;
import com.yanbao.util.UUIDUtil;


/**
 * Created by summer on 2016-12-08:13:38;
 * 
 * 幻灯片管理(广告管理)
 */
@Controller
@RequestMapping("/ad")
public class ADController extends BaseController {

    @Autowired
    ADService adService;
    @Autowired
    ParameterService parameterService;

    @ResponseBody
    @RequestMapping("/create")
    public JsonResult ad(Ad ad) throws Exception {
        ad.setId(UUIDUtil.getUUID());
        if (ad.getRank() == null) {
            ad.setRank(1);
        }

        if (ad.getType() == 1) {
            Parameter model = ParamUtil.getIstance().getModel(Parameter.GUIDEFLAG);
            Parameter update = new Parameter();
            update.setValue(UUIDUtil.getUUID());
            parameterService.updateById(model.getId(), update);
        }
        adService.create(ad);
        return success();
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(String adId) {
        adService.deleteById(adId);
        return success();
    }

    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Ad ad) throws Exception {
    	if(ad.getRemark()==""){
    		System.out.println("asd");
    	}
    	if(ad.getType() == 1){
            Parameter model = ParamUtil.getIstance().getModel(Parameter.GUIDEFLAG);
            Parameter update = new Parameter();
            update.setValue(UUIDUtil.getUUID());
            parameterService.updateById(model.getId(), update);
    	}
        adService.updateById(ad.getId(), ad);
        return success();
    }

    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(Page page) {
        Ad ad = new Ad();
        ad.setStatus(1);
        ad.setType(0);
        int count = adService.readCount(ad);
        if (count==0){
            return fail("没有资源");
        }
        List list = adService.readList(ad, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }

    @RequestMapping("/rank")
    @ResponseBody
    public JsonResult rank(String Id, int rank) {
        Ad ad = adService.readById(Id);
        ad.setRank(rank);
        adService.updateById(Id, ad);
        return success();
    }

    @ResponseBody
    @RequestMapping("/exchangeOrder")
    public JsonResult exchangeOrder(String upId, String downId) {
        Ad adUp = adService.readById(upId);
        Ad adDown = adService.readById(downId);
        int rank = adUp.getRank();
        adUp.setRank(adDown.getRank());
        adDown.setRank(rank);
        adService.updateById(adUp.getId(), adUp);
        adService.updateById(adDown.getId(), adDown);
        return success();
    }
    
    @ResponseBody
    @RequestMapping("/readStartList")
    public JsonResult readStartList(Page page){
    	 Ad ad = new Ad();
         ad.setStatus(1);
         int count = adService.readStartCount(ad);
         if (count==0){
             return fail("没有资源");
         }
         List list = adService.readStartList(ad, page);
         PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
         return success(pageResult);
    	
    	
    }
    
    
    


}
