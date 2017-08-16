package com.yanbao.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mall.model.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.*;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.TokenUtil;
import com.yanbao.vo.AgentBillVo;
import com.yanbao.vo.StoreVo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.yanbao.core.page.JsonResult;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.StaticVariable;

/***
 * 业绩查看
 */
@Controller
@RequestMapping("/wallet")
public class WalletController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    @Autowired 
    private AgentInfoService agentInfoService;
    @Autowired 
    private SysCityService sysCityService;
    @Autowired 
    private StoreService storeService;
    @Autowired 
    private EpRecordService epRecordService;
    @Autowired 
    private WalletExchangeService walletExchangeService;
	@Autowired
	private AgentBillService agentBillService;
	@Autowired
	private GoodsWinService goodsWinService;
	@Autowired
    private UserService userService;
    //当前类权限id
    private static String classPermissionId="6";
    
    //区域统计
    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/RangeStatistics")
    public JsonResult RangeStatistics(HttpServletRequest request,String agentId) {
    	if(!ispermission(classPermissionId, request)){
			return new JsonResult(2,"无权限");
		}
		if(StringUtils.isEmpty(agentId)){
			return new JsonResult(1,"代理不存在");
		}
		Map<String,String> result=new HashMap<String, String>();
		try {
			//查出代理
			Token token = TokenUtil.getSessionUser(request);
			AgentInfo agent=getAgent(token);//agentInfoService.getById(agentId);
			if(null==agent||null==agent.getAgentAreaId()||agent.getStatus()!=1){
				return new JsonResult(1,"代理不存在");
			}
			if(!agent.getId().equals(agentId)){
				return new JsonResult(2,"无权限");
			}
    		//查出所有地区
    		StaticVariable.setAll(sysCityService);
    		City city=StaticVariable.ALL_CITY_MAP.get(agent.getAgentAreaId());
    		Map<String,Object> resultmap=StaticVariable.getCitySetlower(city);
			List<String> list=(List<String>)resultmap.get("IDS");
			List<Integer> statusList = new ArrayList<>();
			//statusList.add(0);
			statusList.add(1);
    		//查出所有商家
    		List<Store> slist=storeService.getListByAreaId(list, statusList, null, null);
    		//所有商铺ID
    		List<String> storIds=new ArrayList<String>();
    		//入驻数量
    		int todayCount=0,monthCount=0;//商家入驻数量
    		//今日时间
    		Date day=DateTimeUtil.getDayFirst();
    		//本月时间
    		Date month=DateTimeUtil.getMonthFirst();
    		for (Store store : slist) {
    			storIds.add(store.getId());
    			if(store.getCreateTime().getTime()>=day.getTime()){
    				todayCount++;
    			}
    			if(store.getCreateTime().getTime()>=month.getTime()){
    				monthCount++;
    			}
			}
    		result.put("allStoreCount", slist.size()+"");//总商家数
    		result.put("todayStoreCount", todayCount+"");//今日入住
    		result.put("monthStoreCount", monthCount+"");//本月入住
    		
    		//提现统计
    		Double todayExCount=0d,monthExCount=0d,allExCountDouble=0d;
    		if(storIds.size()>0){
    			List<GoodsWin> gdlist=goodsWinService.getByStoreIds(storIds);
	    		for (GoodsWin gdwin : gdlist) {
	    			Double allscore=0d;
	    			if(gdwin.getOrderType()==1){
						allscore=(gdwin.getPrice()*gdwin.getNum());
	    			}else if(gdwin.getOrderType()==3){
						allscore=gdwin.getPrice();
	    			}else{
	    				continue; 
	    			}
	    			if(gdwin.getCreateTime().getTime()>=day.getTime()){
	    				todayExCount=todayExCount+allscore;
	    			}
	    			if(gdwin.getCreateTime().getTime()>=month.getTime()){
	    				monthExCount=monthExCount+allscore;
	    			}
	    			allExCountDouble=allExCountDouble+allscore;
				}
    		}
    		result.put("allExCount", PoundageUtil.getPoundage(allExCountDouble,1d)+"");//总金额
    		result.put("todayExCount", PoundageUtil.getPoundage(todayExCount,1d)+"");//今日金额
    		result.put("monthExCount", PoundageUtil.getPoundage(monthExCount,1d)+"");//本月金额
    		//ep统计
    		Double todayEpCount=0d,monthEpCount=0d,allEpCountDouble=0d;
    		if(storIds.size()>0){
    			List<User> users=userService.getByStoreIds(storIds);
    			List<String> userids=new ArrayList<String>();
    			if(null!=users&&users.size()>0){
    				for (User user : users) {
    					userids.add(user.getId());
    				}
				}
    			if(userids.size()!=0){
    				List<EpRecord> eplist=epRecordService.getByStoreIds(userids);
    				for (EpRecord eprecord : eplist) {
    					if(eprecord.getCreateTime().getTime()>=day.getTime()){
    						todayEpCount=todayEpCount+Math.abs(eprecord.getEp());
    					}
    					if(eprecord.getCreateTime().getTime()>=month.getTime()){
    						monthEpCount=monthEpCount+Math.abs(eprecord.getEp());
    					}
    					allEpCountDouble=allEpCountDouble+Math.abs(eprecord.getEp());
    				}
    			}
    		}
    		result.put("allEpCount", PoundageUtil.getPoundage(allEpCountDouble,1d)+"");//总EP
    		result.put("todayEpCount", PoundageUtil.getPoundage(todayEpCount,1d)+"");//今日EP
    		result.put("monthEpCount", PoundageUtil.getPoundage(monthEpCount,1d)+"");//本月EP
    		return new JsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1,"异常错误!");
		} 
    }

	/**
	 * 根据id查询代理业绩
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/getAgentPerformance", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getAgentPerformance(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception{
		Token token = TokenUtil.getSessionUser(request);

		AgentBill agentBill = new AgentBill();
		agentBill.setAgentId(token.getId());
		//根据id查询代理业绩总数
		int count = agentBillService.readPerformanceCount(agentBill);
		//根据id查询代理业绩
		List<AgentBill> list =  agentBillService.getAgentPerformance(agentBill,page);

		if(count == 0){
			return new JsonResult(4,"没有代理业绩结算记录");
		}
		if(list.size() <= 0){
			return new JsonResult(3,"该代理业绩结算记录不存在");
		}
		//定义一个集合保存
		List<AgentBillVo> rows = new ArrayList<>();
		AgentBillVo agentBillVo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(AgentBill ab : list){
			Double EP=PoundageUtil.getPoundage(ab.getEP()*ab.getEPScale(),1d); 
			ab.setEP(EP);
			ab.setBalance(PoundageUtil.getPoundage(ab.getTotalAmount() - EP,1d));
			agentBillVo = new AgentBillVo();
			BeanUtils.copyProperties(agentBillVo, ab);
			//获取结算时间
			String date = sdf.format(ab.getCreateTime());
			String[] dates = date.split("-");
			agentBillVo.setYear(dates[0]);
			agentBillVo.setMonth(dates[1]);
			rows.add(agentBillVo);
		}

		PageResult<AgentBillVo> voPage = new PageResult<AgentBillVo>();
		voPage.setRows(rows);
		voPage.setPageNo(page.getPageNo());
		voPage.setPageSize(page.getPageSize());
		voPage.setTotalSize(count);

		//返回列表
		return new JsonResult(voPage);
	}

}
