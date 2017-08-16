package com.yanbao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mall.model.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.yanbao.constant.MessageType;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.yanbao.constant.GoodsDetailType;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.util.JPushUtil;
import com.yanbao.util.StaticVariable;
import com.yanbao.util.TokenUtil;
import com.yanbao.vo.GoodsImageVo;
import com.yanbao.vo.GoodsVo;
import com.yanbao.vo.StoresImageVo;
/***
 * 图片审核
 */
@Controller
@RequestMapping("/image")
public class ImageReviewController extends BaseController{
	
	@Autowired 
    private StoreService storeService;
	@Autowired 
    private SysCityService sysCityService;
	@Autowired 
    private AgentInfoService agentInfoService;
	@Autowired 
    private GoodsService goodsService;
	@Autowired 
    private HbImgageService imgageService;
	@Autowired 
    private FileLinkService fileLinkService;
	@Autowired 
    private FileService fileService;
	@Autowired 
    private GoodsDetailService goodsDetailService;
	@Autowired 
    private UserService userService;
	@Autowired
	private MessageService messageService;
	 //当前类权限id
    private static String classPermissionId="4";
	
    private Integer pageSize=12;
	
    private static final Logger logger = LoggerFactory.getLogger(ImageReviewController.class);
    
    //商铺图片审核接口
    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/allStoreImg")
    public JsonResult allStoreImg(HttpServletRequest request,String agentId,Integer pageNum,Integer pagesize,String searh) {
    	if(!ispermission(classPermissionId, request)){
    		return new JsonResult(2,"无权限");
    	}
    	if(StringUtils.isEmpty(agentId)){
    		return new JsonResult(1,"代理不存在");
    	}
    	if(null==pageNum){
    		pageNum=1;
    	}
    	if(null==pagesize){
			pagesize=pageSize;
    	}
    	try {
    		//查出代理
    		Token token = TokenUtil.getSessionUser(request);
    		AgentInfo agent=getAgent(token);//agentInfoService.getById(agentId);
    		if(null==agent||!agent.getId().equals(agentId)){
    			return new JsonResult(2,"无权限");
    		}
    		if(null==agent||null==agent.getAgentAreaId()){
    			return new JsonResult(1,"代理不存在");
    		}
    		//查出所有地区
    		StaticVariable.setAll(sysCityService);
    		City city=StaticVariable.ALL_CITY_MAP.get(agent.getAgentAreaId());
    		Map<String,Object> resultmap=StaticVariable.getCitySetlower(city);
			List<String> list=(List<String>)resultmap.get("IDS");
    		//查出所有商家
    		List<Store> slist=storeService.getListByAreaId(list, null, null, null,"".equals(searh)?null:searh); 
    		Map<String,Store> storeMap=new HashMap<String, Store>();
    		List<String> storeIds=new ArrayList<String>();
    		for (Store store : slist) {
    			storeMap.put(store.getId(), store);
    			storeIds.add(store.getId());
			} 
    		//查出分页商品
    		List<Goods> goods=null;
    		if(storeIds.size() >0){
    			goods=goodsService.readByStoreId(storeIds, null, null);
    		}
    		//组装数据
    		PageResult<StoresImageVo> voPage = new PageResult<StoresImageVo>();
			List<StoresImageVo> rows=new ArrayList<StoresImageVo>();
			if(null==goods){
				voPage.setRows(rows);
				voPage.setPageNo(pageNum);
				voPage.setPageSize(pagesize);
				voPage.setTotalSize(0);
				return new JsonResult(voPage);
			}
			Map<String,StoresImageVo> goodsImgMap=new HashMap<String, StoresImageVo>();
			for (Goods gs : goods) {
				if(gs.getVerify()==2||gs.getVerify()==3){
					continue;
				}
				Store store=storeMap.get(gs.getStoreId());
				StoresImageVo giv=null;
				if(null==goodsImgMap.get(store.getId())){
					giv=new StoresImageVo();
					giv.setStoreId(store.getId());
					giv.setStoreName(store.getStoreName());
					giv.setIcon(store.getIcon());
				}else{
					giv=goodsImgMap.get(store.getId());
				}
				GoodsVo goodsvo=new GoodsVo();
				goodsvo.setGoodsId(gs.getId());
				goodsvo.setStoreId(store.getId());
				goodsvo.setIcon(gs.getIcon());
				List<GoodsVo> listgoodsvo=giv.getGoods();
				if(null==listgoodsvo){
					listgoodsvo=new ArrayList<GoodsVo>();
				}
				listgoodsvo.add(goodsvo);
				giv.setGoods(listgoodsvo);
				goodsImgMap.put(store.getId(), giv);
			}
			List<StoresImageVo> result=new ArrayList<StoresImageVo>();
			for(String key : goodsImgMap.keySet()){
				result.add(goodsImgMap.get(key));
			}
			int startRow=(pageNum-1)*pagesize;
			int endRow=((pageNum-1)*pagesize)+pagesize;
			for (int i = startRow;i<endRow;i++) {
				if(i<result.size()){
					rows.add(result.get(i));
				}
			}
			voPage.setRows(rows);
			voPage.setPageNo(pageNum);
			voPage.setPageSize(pagesize);
			voPage.setTotalSize(result.size());

			//返回
			return new JsonResult(voPage);

		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1,"异常错误!");
		}
    }
    
    
    //商品图片接口
    @ResponseBody
    @RequestMapping(value="/goodsImg")
    public JsonResult goodsImg(HttpServletRequest request,String goodsId) {
    	/*if(!ispermission(classPermissionId, request)){
			return new JsonResult(2,"无权限");
		}*/
    	if(StringUtils.isEmpty(goodsId)){
    		return new JsonResult(1,"商品不存在");
    	}
    	try {
    		Goods goods=goodsService.readById(goodsId);
    		if(null==goods){
        		return new JsonResult(1,"商品不存在");
        	}
    		if(goods.getVerify()!=1){
        		return new JsonResult(1,"商品已审核");
        	}
    		//查出3张表里面的数据
    		Image imgfind=new Image();
    		imgfind.setImageLinkId(goods.getId());
    		//imgfind.setStatus(1);
    		List<Image> imglist=imgageService.readAll(imgfind);
    		
    		SysFileLink sflfind=new SysFileLink();
    		sflfind.setLinkId(goods.getId());
			sflfind.setIsDefault(null);
    		sflfind.setStatus(1);
    		List<SysFileLink> filelinklist=fileLinkService.getListByPo(sflfind);
    	    
    		List<String> fileids=new ArrayList<String>();
    	    for (SysFileLink sysFileLink : filelinklist) {
    	    	fileids.add(sysFileLink.getFileId());
			}
    	    List<SysFile> sysFilesList=null;
    	    if(fileids.size()>0){
    	    	sysFilesList=fileService.getListByFileIds(fileids);
    	    }
    	    
    	    GoodsDetail gdfind=new GoodsDetail();
    	    gdfind.setGoodsId(goods.getId());
    	    gdfind.setType(GoodsDetailType.IMG.getCode());
    	    gdfind.setStatus(1);
    	    List<GoodsDetail> gooddetailList=goodsDetailService.readAll(gdfind);
    		//组装数据
    	    List<GoodsImageVo> givlist=new ArrayList<GoodsImageVo>();
    	    if(null!=imglist&&imglist.size()>0){
    	    	for (Image image : imglist) {
    	    		GoodsImageVo giv=new GoodsImageVo();
    	    		giv.setImgId(image.getId());
    	    		giv.setPath(image.getPath());
    	    		giv.setGoodsId(goods.getId());
					giv.setGoodName(goods.getName());
    	    		giv.setType(0);
    	    		givlist.add(giv);
    	    	}
    	    }
    	    if(null!=sysFilesList&&sysFilesList.size()>0){
    	    	for (SysFile sysFile : sysFilesList) {
    	    		GoodsImageVo giv=new GoodsImageVo();
    	    		giv.setImgId(sysFile.getId());
    	    		giv.setPath(sysFile.getPath());
    	    		giv.setGoodsId(goods.getId());
					giv.setGoodName(goods.getName());
    	    		giv.setType(1);
    	    		givlist.add(giv);
    	    	}
    	    }
    	    if(null!=gooddetailList&&gooddetailList.size()>0){
    	    	for (GoodsDetail goodsDetail : gooddetailList) {
    	    		GoodsImageVo giv=new GoodsImageVo();
    	    		giv.setImgId(goodsDetail.getId());
    	    		giv.setPath(goodsDetail.getContent());
    	    		giv.setGoodsId(goods.getId());
					giv.setGoodName(goods.getName());
    	    		giv.setType(2);
    	    		givlist.add(giv);
    	    	}
    	    }
    		//返回
    		return new JsonResult(givlist);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1,"异常错误!");
		}
    }
    
    //商品图片接口
    @ResponseBody
    @RequestMapping(value="/ImageReview")
    public JsonResult ImageReview(HttpServletRequest request,String goodsId,String failCause,Integer isverify) {
    	/*if(!ispermission(classPermissionId, request)){
			return new JsonResult(2,"无权限");
		}*/
    	if(StringUtils.isEmpty(goodsId)){
    		return new JsonResult(1,"商品不存在");
    	}
    	try {
    		Goods goods=goodsService.readById(goodsId);
    		if(null==goods){
        		return new JsonResult(1,"商品不存在");
        	}
    		if(goods.getVerify()!=1){
        		return new JsonResult(1,"商品已审核");
        	}
    		//用户ID
    		Store store=storeService.readById(goods.getStoreId());
    		if(null==store){
    			return new JsonResult(1,"商铺不存在");
    		}
    		User user=userService.readById(store.getUserId());
    		Goods upgoods=new Goods();
    		//更改商品状态
    		if(isverify==3){//不通过
    			upgoods.setStatus(StatusType.FALSE.getCode());//下架
				Message message = new Message();
				message.setUserId(store.getUserId());
				message.setOrderNo("");
				message.setTitle(MessageType.SYSTEM.getMsg());
				message.setType(MessageType.SYSTEM.getCode());
				message.setDetail("您的商品因:"+failCause+" 等原因,未通过审核,暂时被下架.请前往处理");
				message.setRemark("商品审核不通过");
				messageService.createWithUUID(message);

				//极光推送
				if(null!=user&&null!=user.getRegistrationId()){
					boolean rs = JPushUtil.pushPayloadByid(user.getRegistrationId(), "您的商品因:"+failCause+" 等原因,未通过审核,暂时被下架.请前往处理.",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.SYSTEM_MSM));
				}
    		}
    		upgoods.setVerify(isverify);
    		goodsService.updateById(goods.getId(), upgoods);



    		return new JsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1,"异常错误!");
		}
    }
}
