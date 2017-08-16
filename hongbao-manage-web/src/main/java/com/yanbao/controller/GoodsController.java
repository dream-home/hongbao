package com.yanbao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mall.model.*;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.ToolUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.IssueType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.service.GoodsService;
import com.yanbao.service.GoodsSortService;
import com.yanbao.service.HbImgageService;
import com.yanbao.service.SysFileLinkService;
import com.yanbao.service.SysFileService;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.GoodIconsVo;
import com.yanbao.vo.GoodsIssueDetailVo;
import com.yanbao.vo.GoodsIssueVo;
import com.yanbao.vo.GoodsSortVo;
import com.yanbao.vo.GoodsTransferVo;

/**
 * Created by summer on 2016-12-08:15:24;
 * <p>
 * 商品管理 商品推荐
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    GoodsIssueService goodsIssueService;
    @Autowired
    GoodsSortService goodsSortService;
    @Autowired
    GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    SysFileService sysFileService;
    @Autowired
    SysFileLinkService sysFileLinkService;
    
    @Autowired
    HbImgageService hbImgageService;
    

    

    /**
     * 商品人气推荐删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/goodsRec/delete")
    @ResponseBody
    public JsonResult delete(String id) {
        Goods goods = new Goods();
        goods.setIsRecommend(0);
        goods.setId(id);
        goodsService.updateById(id, goods);
        return success();
    }

    /**
     * 商品人气推荐添加
     *
     * @param id
     * @return
     */
    @RequestMapping("/goodsRec/add")
    @ResponseBody
    public JsonResult add(String id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setIsRecommend(1);
        goods.setStatus(1);
        int count = goodsService.readCount(goods);
        if (count >= 6) {
            return fail(500, "推荐商品已满");
        }
        goodsService.updateById(id, goods);
        return success();
    }

    /**
     * 商品人气推荐 列表
     *
     * @return
     */
    @RequestMapping("/goodsRec/list")
    @ResponseBody
    public JsonResult list() {
        Goods goods = new Goods();
        goods.setIsRecommend(1);
        goods.setStatus(1);
        List<Goods> goodses = goodsService.readAll(goods);
        if (goodses.size() == 0) {
            return fail("没有推荐商品");
        }
        PageResult pageResult = new PageResult(1, goodses.size(), goodses.size(), goodses);
        return success(pageResult);
    }

    /**
     * 商品人气推荐 商品推荐---去推荐页
     *
     * @return
     */
    @RequestMapping("/goodsRec/goodsIssueList")
    @ResponseBody
    public JsonResult goodsList(GoodsIssue goodsIssue, Page page) {
        List<GoodsIssueVo> goodsIssueVos = goodsIssueService.getGoodsIssueListByUserCount();
        return success(new PageResult(page.getPageNo(), page.getPageSize(), goodsIssueVos.size(), goodsIssueVos));
    }

    /**
     * 添加商品 添加商品是自动发出商品一期竞购
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public JsonResult goodsAdd(Goods goods) throws Exception {
        //获取系统设置
        ParamUtil util = ParamUtil.getIstance();
        int drawNumMax = ToolUtil.parseInt(util.get(Parameter.DRAWNUMMAX),0);
        int drawNumMin = ToolUtil.parseInt(util.get(Parameter.DRAWNUMMIN),0);
        if (goods.getDrawNum() > drawNumMax|| goods.getDrawNum() < drawNumMin) {
            return fail("系统开奖人数出现错误");
        }
        goods.setId(UUIDUtil.getUUID());
        goods.setGoodsType(0);
        goods.setCurIssueNo(1);
        goods.setIsRecommend(0);
        goods.setDrawPrice(PoundageUtil.getPoundage(goods.getPrice() / goods.getDrawNum(), 1.0));
        //添加商品竞拍第一期数据
        GoodsIssue goodsIssue = new GoodsIssue();
        goodsIssue.setId(UUIDUtil.getUUID());
        goods.setCurIssueId(goodsIssue.getId());//获取期数id
        goodsIssue.setIssueNo(1);
        goodsIssue.setGoodsId(goods.getId());
        goodsIssue.setGoodsName(goods.getName());
        goodsIssue.setIcon(goods.getIcon());
        goodsIssue.setPrice(goods.getPrice());
        goodsIssue.setDrawPrice(goods.getDrawPrice());
        goodsIssue.setDrawNum(goods.getDrawNum());
        goodsIssue.setCurNum(0);
        goodsIssue.setSaleSwitch(goods.getSaleSwitch());
        goodsIssue.setStatus(IssueType.PENDING.getCode());
        try {

            goodsIssueService.create(goodsIssue);
            goodsService.create(goods);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        return success();
    }
    
    
    /**
     * 商品添加
     * 
     * @param goodIons
     * @return 
     */
    @RequestMapping("/addgoodEps")
    @ResponseBody
    public JsonResult goodsAdds(@RequestBody GoodIconsVo goodIons) {
       /* SysSetting sysSetting = new SysSetting();
        sysSetting.setStatus(1);
        sysSetting = sysSettingService.readOne(sysSetting);
        if (goods.getDrawNum() > sysSetting.getDrawNumMax() || goods.getDrawNum() < sysSetting.getDrawNumMin()) {
            return fail("系统开奖人数出现错误");
        }*/
    	
    	
    	
    	
    	Goods goods = new Goods();
        goods.setId(UUIDUtil.getUUID());
        goods.setGoodsSortId(goodIons.getGoodsSortId());
        goods.setGoodsType(0);
        goods.setName(goodIons.getName());
        goods.setIsRecommend(0);
        goods.setStatus(1);
        goods.setPrice(goodIons.getPrice());
        goods.setDetail(goodIons.getDetail());
        goods.setIcon(goodIons.getIcons().get(0));
        goods.setStock(goodIons.getStock());
       

        try {
            goodsService.create(goods);
            
            int count = 0;
            for(int i = 0 ; i<goodIons.getIcons().size();i++){
            	String str = goodIons.getIcons().get(i);
            		if(!("0".equals(str))){

	            		SysFile sysFile = new SysFile();
	                    SysFileLink fileLink = new SysFileLink();
                    
	                    sysFile.setId(UUIDUtil.getUUID());
	                	sysFile.setPath(str);
	                	sysFile.setUploadUserId("systemEP");
	                	sysFile.setFileType(0);
	                	sysFile.setStatus(1);
	                	sysFileService.create(sysFile);
	                	if(count==0){
	                		fileLink.setIsDefault(1);
	                	}else{
	                		fileLink.setIsDefault(0);
	                	}
	                	count++;
	                	
	                	
	                	fileLink.setId(UUIDUtil.getUUID());
	                	fileLink.setFileId(sysFile.getId());
	            		fileLink.setLinkId(goods.getId());
	            		fileLink.setLinkType(1);
	            		fileLink.setStatus(1);
	            		
	            		
	            		sysFileLinkService.create(fileLink);
	            		
	            		
	            		
	            	}
	            	
            	
            }
            
           /* for(String str:goodIons.getIcons()){
            	
                
        		
            }*/
            
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        return success();
    }
   
    
    
    
    
    
    
    

    /**
     * 商品下架
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/down")
    @ResponseBody
    public JsonResult goodsDown(String goodsId) {
        Goods goods = new Goods();
        goods.setStatus(0);
        goods.setId(goodsId);
        goodsService.updateById(goodsId, goods);
        return success();
    }

    /**
     * 商品列表
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult goodsList(Goods goods, Page page) {
    	goods.setFirstReferrerScale(null);
    	goods.setSecondReferrerScale(null);
    	goods.setThirdReferrerScale(null);
    	goods.setBusinessSendEp(null);
    	goods.setDiscountEP(null);
        int count = goodsService.readCount(goods);
        if (count == 0) {
            return fail("没有商品");
        }
        List<Goods> list =  goodsService.readListWithTime(goods, page);
        for(Goods good:list){
        		good.setDetail("");	
        }
        //List<Goods> list = goodsService.readList(goods, page.getPageNo(), page.getPageSize(), count);
        return success(getPageResult(page, count, list));
    }

    /**
     * 商品属性修改
     */
    @ResponseBody
    @RequestMapping("/sortUpdate")
    public JsonResult sortUpdate(String id,String sortId){
    	Goods goods1 = new Goods();
    	goods1.setGoodsSortId(sortId);
    	goodsService.updateById(id, goods1);
    	 return success();
    	
    }
    
    
    
    /**
     * 商品更新
     */
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult goodsUpdate(@RequestBody GoodIconsVo goodIons) {
    	
    	
    	//System.out.println(goodIons.getIconKey().size());
    	for(int i = 0; i<goodIons.getIconKey().size();i++){
    		
    		if(!("0".equals(goodIons.getIconKey().get(i)))){
    			
    	        Goods goods = new Goods();
    	        goods.setIcon(goodIons.getIcons().get(0));  
    	        goods.setDetail(goodIons.getDetail());
    	        goods.setName(goodIons.getName());
    	        goods.setPrice(goodIons.getPrice());
    	        goods.setStock(goodIons.getStock());
    	        goodsService.updateById(goodIons.getId(), goods);
    	        if(!("0".equals(goodIons.getIcons().get(i)))){
	    	        SysFile sysFile = new SysFile();
	                SysFileLink fileLink = new SysFileLink();
	                
	                SysFileLink syslink =  sysFileLinkService.readById(goodIons.getIconKey().get(i));
	
	            	sysFile.setPath(goodIons.getIcons().get(i));
	            	sysFile.setUploadUserId("systemEP");
	            	sysFile.setFileType(0);
	            	sysFile.setStatus(1);
	            	
	                sysFileService.updateById(syslink.getFileId(),sysFile);
	                
	            	fileLink.setFileId(sysFile.getId());
	        		fileLink.setLinkId(goodIons.getId());
	        		fileLink.setLinkType(1);
	        		fileLink.setStatus(1);
	        		//fileLink.setIsDefault(0);
	        		
        			sysFileLinkService.updateById(goodIons.getIconKey().get(i), fileLink);
        		}
        		
        		
    		}else{
    				if(!("0".equals(goodIons.getIcons().get(i)))){
    					SysFile sysFile = new SysFile();
        	            SysFileLink fileLink = new SysFileLink();
        	            sysFile.setId(UUIDUtil.getUUID());
        	        	sysFile.setPath(goodIons.getIcons().get(i));
        	        	sysFile.setUploadUserId("systemEP");
        	        	sysFile.setFileType(0);
        	        	sysFile.setStatus(1);
        	        	sysFileService.create(sysFile);
        	        	fileLink.setId(UUIDUtil.getUUID());
        	        	fileLink.setFileId(sysFile.getId());
        	    		fileLink.setLinkId(goodIons.getId());
        	    		fileLink.setLinkType(1);
        	    		fileLink.setStatus(1);
        	    		//fileLink.setIsDefault(0);
        	    		sysFileLinkService.create(fileLink);
        	    	}
    			    
    		}
    		
    		
    		
    	}
    	
    	
    	
    	
    	
    	
    	
    	/*
        SysSetting sysSetting = new SysSetting();
        sysSetting.setStatus(1);
        sysSetting = sysSettingService.readOne(sysSetting);
        //Goods goodsRc=goodsService.readById(goodIons.getId());
        Goods goods = new Goods();
        goods.setIcon(goodIons.getIcons().get(0));  
        
        goodsService.updateById(goodIons.getId(), goods);
        sysFileLinkService.deleteById(goodIons.getId()); 
        for(String str:goodIons.getIcons()){
            SysFile sysFile = new SysFile();
            SysFileLink fileLink = new SysFileLink();
            sysFile.setId(UUIDUtil.getUUID());
        	sysFile.setPath(str);
        	sysFile.setUploadUserId("systemEP");
        	sysFile.setFileType(0);
        	sysFile.setStatus(1);
        	sysFileService.create(sysFile);
        	System.out.println("0");
        	fileLink.setId(UUIDUtil.getUUID());
        	fileLink.setFileId(sysFile.getId());
    		fileLink.setLinkId(goodIons.getId());
    		fileLink.setLinkType(1);
    		fileLink.setStatus(1);
    		fileLink.setIsDefault(0);
    		sysFileLinkService.create(fileLink);
    		
        }*/
        

        // goodsService.updateById(goods.getId(), goods);
        return success();
    }
    /*@ResponseBody
    @RequestMapping("/update")
    public JsonResult goodsUpdate(Goods goods) {
        if (goods.getDrawNum()==null||goods.getPrice()==null){
            return fail(400,"竞拍人数和商品价格不可以是空");
        }
        SysSetting sysSetting = new SysSetting();
        sysSetting.setStatus(1);
        sysSetting = sysSettingService.readOne(sysSetting);
        Goods goodsRc=goodsService.readById(goods.getId());
        int max = sysSetting.getDrawNumMax();
        int min = sysSetting.getDrawNumMin();
        if (goods.getDrawNum() < min || goods.getDrawNum() > max) {
            return fail(1400, "商品竞拍人数不合法");
        }
        if (goodsRc.getPrice()!=goods.getPrice()){
            goods.setDrawPrice(PoundageUtil.getPoundage(goods.getPrice() / goods.getDrawNum(), 1.0));
        }
        goodsService.updateById(goods.getId(), goods);
        return success();
    }*/

    /**
     * 商品详细  查看商品详情
     *
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping("/detail")
    public JsonResult goodsDetail(String goodsId) {
        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setStatus(1);
        goods = goodsService.readById(goodsId);
        if (goods == null) {
            return fail("商品不存在");
        }
        List<String> urls = new ArrayList<String>();
        List<String> iconId = new ArrayList<String>();
        SysFileLink fileLink = new SysFileLink();
        fileLink.setStatus(1);
        fileLink.setLinkId(goodsId);
        fileLink.setLinkType(1);
        
        Image image = new Image();
        image.setImageLinkId(goodsId);
        image.setStatus(0);
        
        
        
        
        List<Image> images   =     hbImgageService.getPoList(image);
        if(images.size()>0){
        	 
           for(Image img:images){
        	   	urls.add(img.getPath());
        	   	iconId.add(img.getId());
        	   
        	   
           }
        	
        	
        	
        	
            Map<String, Object> result = new HashedMap();
            result.put("urls", urls);
            result.put("detail", goods);
            result.put("iconId", iconId);
          
        
        
        
        
            return success(result);
        
        }else{
        	
        	
        	List<SysFileLink> sysFileLinks = sysFileLinkService.readAllData(goodsId);	
        	
        	for (SysFileLink sysFileLink : sysFileLinks) {
                SysFile sysFile = sysFileService.readById(sysFileLink.getFileId());
                if (sysFile != null) {
                	//if(sysFile.getPath())
                	
                	if(!("0".equals(sysFile.getPath()))){
                		  urls.add(sysFile.getPath());
                          iconId.add(sysFileLink.getId());  
                        
                	}
                    
                }
            }
            Map<String, Object> result = new HashedMap();
            result.put("urls", urls);
            result.put("detail", goods);
            result.put("iconId", iconId);
            return success(result);
        	
        }
        
        
        
        
        
        
        
        
       
       
    }




    /**
     * @return 商品管理---商品期数列表 商品竞拍列表
     */
    @RequestMapping("/goodsIssueList")
    @ResponseBody
    public JsonResult goodsIssueList(GoodsIssue goodsIssue, Page page) {
        int count = goodsIssueService.readCount(goodsIssue);
        if (count == 0) {
            return fail("商品列表为空");
        }
        List<GoodsIssue> goodsIssues = goodsIssueService.readList(goodsIssue, page.getPageNo(), page.getPageSize(), count);
        return success(getPageResult(page, count, goodsIssues));
    }

    /**
     * 统计生肖商城 每个生肖下有多少商品
     */
    @RequestMapping("/animalsGoods/list")
    @ResponseBody
    public JsonResult getAnimalsGoodsList(GoodsSort goodsSort,  Page page) {
       // Page page = new Page();
    	GoodsSort goodsSort1 = new GoodsSort();
    	goodsSort1.setStatus(1);
    	int count = goodsSortService.readCount(goodsSort1);
        List<GoodsSortVo> goodsSortVos = goodsSortService.getGoodsSortVoListAll(goodsSort1,page);
        return success(getPageResult(page, count, goodsSortVos));
    }

    /**
     * 统计生肖商城 生肖更新
     */
    @RequestMapping("/goodsSort/update")
    @ResponseBody
    public JsonResult updateGoodsSort(GoodsSort goodsSort) {
        goodsSortService.updateById(goodsSort.getId(), goodsSort);
        return success();
    }

    /**
     * 统计生肖商城 生肖创建
     */
    @RequestMapping("/goodsSort/create")
    @ResponseBody
    public JsonResult createGoodsSort(GoodsSort goodsSort) {
        goodsSort.setId(UUIDUtil.getUUID());
        goodsSort.setGoodSortType("COMMON");
        goodsSortService.create(goodsSort);
        return success();
    }

    /**
     * 商品管理 ---  所有竞拍记录查询  	竞拍订单管理
     *
     * @return
     */
    @RequestMapping("/goodsIssueDetail")
    @ResponseBody
    public JsonResult goodsIssueDetail(GoodsIssueDetailVo goodsIssueDetailVo, Page page) {
        int count = goodsIssueDetailService.getGoodsIssueDetailVoCount(goodsIssueDetailVo);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsIssueDetailVo> goodsIssueDetailVos = goodsIssueDetailService.getGoodsIssueDetailVoList(goodsIssueDetailVo, page.getStartRow(), page.getPageSize());
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsIssueDetailVos));
    }

    //所有商品分类
    @RequestMapping("/allGoodsSort")
    @ResponseBody
    public JsonResult allGoodsSort(GoodsTransferVo goodsTransferVo) {
    	GoodsSort model=new GoodsSort();
    	model.setGoodSortType("COMMON");
    	List<GoodsSort> goodsSorts=goodsSortService.readAll(model);
        return success(goodsSorts);
    }
    
    //根据商品分类查询商品
    @RequestMapping("/getGoodsBySort")
    @ResponseBody
    public JsonResult getGoodsBySort(GoodsTransferVo goodsTransferVo) {
    	Goods goods=new Goods();
    	goods.setGoodsSortId(goodsTransferVo.getOriginalType());
    	goods.setFirstReferrerScale(null);
    	goods.setSecondReferrerScale(null);
    	goods.setThirdReferrerScale(null);
    	goods.setBusinessSendEp(null);
    	goods.setDiscountEP(null);
    	int count=goodsService.readCount(goods);
    	List<Goods> resultList=goodsService.readList(goods, goodsTransferVo.getPageNum(), goodsTransferVo.getPageSize(), 9999);
    	if(resultList.size()==0){
    		goodsTransferVo.setPageNum(1);
    		resultList=goodsService.readList(goods, goodsTransferVo.getPageNum(), goodsTransferVo.getPageSize(), 9999);
    	}
        return success(new PageResult(goodsTransferVo.getPageNum(), goodsTransferVo.getPageSize(), count, resultList));
    }
    
    //根据商品分类查询商品
    @RequestMapping("/transferType")
    @ResponseBody
    public JsonResult transferType(GoodsTransferVo goodsTransferVo) {
    	//原分类ID
    	String OriginalType=goodsTransferVo.getOriginalType();
    	//新分类ID
    	String NewType=goodsTransferVo.getNewType();
    	int count=0;
    	List<String> ids=null;
    	if(goodsTransferVo.getSelectAll()!=0){//选择全部商品
    		ids=new ArrayList<String>();
    		//选择的商品ID
        	String SelectInput=goodsTransferVo.getSelectInput();
        	if(SelectInput.substring(SelectInput.length()-1, SelectInput.length()).equals("_")){
        		SelectInput=SelectInput.substring(0, SelectInput.length()-1);
        	}
        	String[] idStrings=SelectInput.split("_");
        	for (String string : idStrings) {
        		ids.add(string);
			}
    	}
    	count=goodsService.updateSort(OriginalType, NewType,ids);
    	if(count>0){
    		return success();
    	}else{
    		return fail("转移失败,请重试");
    	}
    }
}
