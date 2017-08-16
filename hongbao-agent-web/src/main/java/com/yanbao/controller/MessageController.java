package com.yanbao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.yanbao.core.model.Token;
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
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.JPushUtil;
import com.yanbao.util.StaticVariable;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.MessagePush;
/***
 * 消息相关
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController{
	
	@Autowired  
    private HbAgentMessageService agentMessageService;
	@Autowired 
    private StoreService storeService;
	@Autowired 
    private SysCityService sysCityService;
	@Autowired 
    private AgentInfoService agentInfoService;
	@Autowired 
    private GoodsService goodsService;
	
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    //给代理推送商铺申请消息接口
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/pushToTheAgent", method = RequestMethod.POST)
    public JsonResult pushToTheAgent(HttpServletRequest request, HttpServletResponse response,@RequestBody MessagePush mp) throws Exception {
    	//获取下级代理
    	if(null==mp.getAreaId()){
    		return new JsonResult(1,"区域ID为空!");
    	}
    	Store store=storeService.readById(mp.getStoreId());
    	if(null==store){
    		return new JsonResult(3,"商铺不存在!");
    	}
    	//将所有的地区读入内存中
    	StaticVariable.setAll(sysCityService);
    	City city=StaticVariable.ALL_CITY_MAP.get(mp.getAreaId());
    	if(null==city){
    		return new JsonResult(4,"区域ID不存在!");
    	}
    	//递归出地区集合
    	Map<String,Object> result=StaticVariable.getCitySetParent(city);
    	//查询代理
    	List<AgentInfo> allAgent=null;
    	if(null!=result){
			List<String> list=(List<String>)result.get("IDS");
    		if(list.size()>0){
    			allAgent=agentInfoService.readListByAreaid((List<String>)result.get("IDS"));
    		}
    	}
    	Map<String,AgentInfo> allagent=new HashMap<String, AgentInfo>();
    	if(null!=allAgent){
	    	for (AgentInfo agentInfo : allAgent) {
	    		allagent.put(agentInfo.getAgentAreaId(), agentInfo);
			}
    	}
    	String code=city.getCode();
    	Map<String,City> citymap=(Map<String,City>)result.get("MAP");
    	AgentInfo saveagent=null;
    	//查询10个等级
    	for (int i = 0; i < 10; i++) {
    		saveagent=allagent.get(code);
    		if(null==saveagent){
    			if(null==citymap.get(code)){
    				code="system";
    				break;
    			}else{
    				code=citymap.get(code).getParentCode();
    			}
    		}else{
    			break;
    		}
		}
    	//代理消息绑定
    	AgentMessage ag=new AgentMessage();
    	ag.setId(UUIDUtil.getUUID());
    	ag.setUserId(store.getId());
    	ag.setStatus(0);
    	ag.setAgentId((null==saveagent?"system":saveagent.getId()));
    	ag.setAreaId(code);
    	ag.setRemark("agentID:"+(null==saveagent?"system":saveagent.getId())+";areaId:"+code);
    	ag.setCreateTime(new Date());
    	ag.setUpdateTime(new Date());
    	if(null==mp.getType()||1==mp.getType()){
    		ag.setType(1);//商铺申请
    		ag.setTitle("商铺申请");
        	ag.setDetail("申请创建商铺:"+store.getStoreName());
    	}else{
    		ag.setType(2);//图片审核
    		ag.setTitle("图片审核申请");
        	ag.setDetail(store.getStoreName()+"商铺申请审核图片:");
    	}
    	agentMessageService.create(ag);
    	return new JsonResult();
    }

    //修改消息为已读
    @ResponseBody
    @RequestMapping(value = "/readMessage", method = RequestMethod.GET)
    public JsonResult readMessage(HttpServletRequest request, HttpServletResponse response,String msgId) throws Exception {
    	if(null==msgId||"".equals(msgId)){
    		return new JsonResult(1,"请选择正确的消息!");
    	}
    	AgentMessage am=agentMessageService.readById(msgId);
    	if(null==am){
    		return new JsonResult(3,"请选择正确的消息!");
    	}
    	am.setStatus(1);
    	agentMessageService.updateById(am.getId(), am);
    	return new JsonResult();
    }
    //消息列表接口
    @ResponseBody
    @RequestMapping(value = "/messageList", method = RequestMethod.GET)
    public JsonResult messageList(HttpServletRequest request, HttpServletResponse response,String agentId,Integer pageNo,Integer pageSize) throws Exception {
    	//代理ID
    	if(null==agentId||"".equals(agentId)){
    		return new JsonResult(1,"代理信息错误");
    	}
    	Token token = TokenUtil.getSessionUser(request);
		AgentInfo ag=getAgent(token);//agentInfoService.getById(agentId);
		if(null==ag){
			return new JsonResult(1,"代理信息错误");
		}
		if(!ag.getId().equals(agentId)){
			return new JsonResult(2,"无权限");
		}
    	if(null==pageNo){
    		pageNo=1;
    	}
    	if(null==pageSize){
    		pageSize=15;
    	}
    	AgentMessage am=new AgentMessage();
    	am.setAreaId(ag.getAgentAreaId());
    	List<AgentMessage> agList=agentMessageService.readList(am, (pageNo-1), pageSize, 9999);
    	return new JsonResult(agList);
    }
    
    //审核消息统计接口 //未读
    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/messageCount", method = RequestMethod.GET)
    public JsonResult messageCount(HttpServletRequest request, HttpServletResponse response,String agentId) throws Exception {
    	if(null==agentId||"".equals(agentId)){
    		return new JsonResult(1,"代理信息错误");
    	}
    	Token token = TokenUtil.getSessionUser(request);
		AgentInfo ag=getAgent(token);//agentInfoService.getById(agentId);
		if(null==ag){
			return new JsonResult(1,"代理信息错误");
		}
		if(!ag.getId().equals(agentId)){
			return new JsonResult(2,"无权限");
		}
    	StaticVariable.setAll(sysCityService);
		City city=StaticVariable.ALL_CITY_MAP.get(ag.getAgentAreaId());
		Map<String,Object> resultmap=StaticVariable.getCitySetlower(city);
		List<String> list=(List<String>)resultmap.get("IDS");
		List<Store> slist=storeService.getListByAreaId(list, null, null, null);
		Map<String,Store> storeMap=new HashMap<String, Store>();
		List<String> storeIds=new ArrayList<String>();
		int shopCount=0,photoCount=0;
		for (Store store : slist) {
			storeMap.put(store.getId(), store);
			storeIds.add(store.getId());
			if(store.getStatus()==0){
				shopCount++;
			}
		}
		//查出分页商品
		if(storeIds.size()>0){
			List<Goods> goods=goodsService.readByStoreId(storeIds, null, null);
			for (Goods good : goods) {
				if(good.getVerify()==1){
					photoCount++;
				}
			}
		}
    	/*AgentMessage am=new AgentMessage();
    	am.setAreaId(ag.getAgentAreaId());
    	List<AgentMessage> amlist=agentMessageService.readAll(am);
    	int shopCount=0,photoCount=0;
    	for (AgentMessage agentMessage : amlist) {
			if(agentMessage.getType()==1){
				shopCount++;
			}else if(agentMessage.getType()==2){
				photoCount++;
			}
		}*/
    	Map<String,Integer> result= new HashMap<String, Integer>();
    	result.put("shopCount", shopCount);
    	result.put("photoCount", photoCount);
    	return new JsonResult(result);
    }
}
