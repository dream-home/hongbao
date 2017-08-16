package com.yanbao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.model.AgentInfo;
import com.mall.model.AgentStaff;
import com.mall.model.EpRecord;
import com.mall.model.User;
import com.yanbao.constant.AgentType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.AgentInfoService;
import com.yanbao.service.EpRecordService;
import com.yanbao.service.HbAgentStaffService;
import com.yanbao.service.UserService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.EpRecordVo;
import com.yanbao.vo.ParameterVo;

/**
 * ep相关
 * Created by Administrator on 2017/6/20.
 */
@Controller
@RequestMapping("/ep")
public class EpRecordController extends BaseController{

    @Autowired
    private EpRecordService epRecordService;
    @Autowired
    private AgentInfoService agentInfoService;
    @Autowired
    private UserService userService;
    @Autowired
	private HbAgentStaffService hbAgentStaffService;
    //当前类权限id 
    private static String classPermissionId="1";

    /**
     * EP赠送记录列表(加盟赠送，代理转入，转赠代理，转赠会员)
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getEpSendList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getEpSendList(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception{
        Token token = TokenUtil.getSessionUser(request);

		AgentInfo agent = getAgent(token);
		if(null==agent){
    		return new JsonResult(1,"代理信息错误");
		}
        //获取ep列表
        List<EpRecord> epRecordList = epRecordService.getSendRecordList(agent.getId(),page);
        //统计ep列表总数
        int count = epRecordService.countSendRecordList(agent.getId());
        //判断ep列表是否为空
        if((epRecordList == null && epRecordList.size() <= 0) || count == 0){
            return new JsonResult(new ArrayList<EpRecord>()); 
        }

        //定义一个集合变量保存数据
        List<EpRecordVo> rows = new ArrayList<>();
        EpRecordVo vo = null;
        String sendUserName = "";
        String receiveUserName = "";
        //循环ep赠送记录
        for(EpRecord e : epRecordList){
            vo = new EpRecordVo();
            BeanUtils.copyProperties(vo, e);
            //获取赠送会员名称，或收取会员名称
            AgentInfo agentInfo = agentInfoService.getById(e.getSendUserId());
            if(agentInfo == null && ToolUtil.isEmpty(agentInfo)){
                //如果为空，说明是本公司赠送
                sendUserName = "斗拍总部";
            }else{
				sendUserName = agentInfo.getCompany();
			}
            agentInfo = agentInfoService.getById(e.getReceiveUserId());
            if(agentInfo == null && ToolUtil.isEmpty(agentInfo)){
                //如果为空，说明赠送给会员
                User user = userService.readById(e.getReceiveUserId());
				if(user == null){
					continue;
				}
                receiveUserName = user.getNickName();
            }else{
				receiveUserName = agentInfo.getCompany();
			}
            if(agent.getId().equals(e.getSendUserId())){//支出  前端显示负数为支出  如果登陆代理ID和发送者ID一致  则为支出
            	vo.setEp(-Math.abs(e.getEp())); 
            }else{
            	vo.setEp(Math.abs(e.getEp())); 
            }
            //赋值 
            vo.setSendUserName(sendUserName);
            vo.setReceiveUserName(receiveUserName);
            rows.add(vo);
        }

        PageResult<EpRecordVo> voPage = new PageResult<EpRecordVo>();
        voPage.setRows(rows);
        voPage.setPageNo(page.getPageNo());
        voPage.setPageSize(page.getPageSize());
        voPage.setTotalSize(count);
        //返回分页集合
        return new JsonResult(voPage);
    }

    /**
     * 根据id显示代理显示公司名称，会员显示会员昵称
     * @param request
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getNameById", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getNameById(HttpServletRequest request, HttpServletResponse response,String id) throws Exception{
        Token token = TokenUtil.getSessionUser(request);

        if(StringUtils.isEmpty(id)){
            return new JsonResult(1,"赠送id不能为空");
        }

        //定义一个变量保存返回参数
        String showName = "";

        //根据id查询代理
		AgentInfo model = new AgentInfo();
		model.setLoginName(id);
        AgentInfo agentInfo = agentInfoService.getByCondition(model);
        if(agentInfo != null){
            //判断如果是代理，显示代理公司名称
            showName = agentInfo.getCompany();
        }else{
            //判断如果不是代理，显示会员名称
            User user = userService.readByUid(id);
            if(user == null){
                return new JsonResult(3,"代理或会员不存在");
            }
            showName = user.getNickName();
        }

        //返回分页集合
        return new JsonResult(showName);
    }
    
    //EP赠送
    @RequestMapping(value = "/epPresented", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult epPresented(HttpServletRequest request, HttpServletResponse response,@RequestBody ParameterVo param) throws Exception{
    	if(!ispermission(classPermissionId, request)){
    		return new JsonResult(2,"无权限");
    	}
    	String id=param.getId();
		Double epCount=param.getEpCount();
		String password=param.getPassword();
    	if(null==id||null==epCount||null==password){
    		return new JsonResult(1,"参数错误");
    	}
    	if(epCount<=0){
    		return new JsonResult(1,"EP数量有误!");
    	} 
    	Token token = TokenUtil.getSessionUser(request);
    	String agentPass="";
    	AgentInfo loginAgent=getAgent(token);
    	if(null==loginAgent){
			return new JsonResult(1,"代理信息错误");
		}
    	if(token.getLoginType()==AgentType.STAFF_TYPE.getCode().intValue()){
    		AgentStaff as=hbAgentStaffService.readById(token.getId());
    		agentPass=Md5Util.MD5Encode(password,as.getSalt());
    		if(!agentPass.equals(as.getPassword())){
        		return new JsonResult(3,"支付密码错误");
        	}
    	}else{
    		agentPass=Md5Util.MD5Encode(password,loginAgent.getSalt());
    		if(!agentPass.equals(loginAgent.getPayPassWord())){
        		return new JsonResult(3,"支付密码错误");
        	}
    	}
    	if(loginAgent.getExchangeEP()<epCount){
    		return new JsonResult(3,"EP不足");
    	}
    	int sendType=10;//转账类型 10：代理互转 11：代理转会员 
    	User sendUser=null;
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setLoginName(id);
		agentInfo.setStatus(1);
    	AgentInfo sendAgent=agentInfoService.getByCondition(agentInfo);
    	if(null==sendAgent){
    		sendUser=userService.readByUid(id);
    		if(null==sendUser){
    			return new JsonResult(3,"不存在账户!");
    		}else{
    			sendType=11;
    		}
    	}else{
    		if(loginAgent.getId().equals(sendAgent.getId())){
    			return new JsonResult(4,"不允许转给自己!");
    		}
    	}
    	//订单号
    	String orderNum=OrderNoUtil.get();
    	//开始转账
    	String AgentUUID=UUIDUtil.getUUID();
    	String SendUUID=UUIDUtil.getUUID();
    	Double Agentold=loginAgent.getExchangeEP();//赠送者转账前拥有EP
    	Double Sendold=0d;//接受者转账前拥有EP
    	//创建记录
    	EpRecord epRecord=new EpRecord();
    	epRecord.setOrderNo(orderNum);
    	epRecord.setSendUserId(loginAgent.getId());
		epRecord.setUserType(3);
		if(sendType==10){
			epRecord.setReceiveUserId(sendAgent.getId());
		}else{
			epRecord.setReceiveUserId(sendUser.getId());
		}
		epRecord.setRecordType(sendType);
		epRecord.setCreateTime(new Date());
		epRecord.setUpdateTime(new Date());
		epRecord.setStatus(1);
		epRecord.setRemark((sendType==10?"代理互转":"代理转会员"));
		String area="";
		if(null!=loginAgent.getAgentProvince()&&!"".equals(loginAgent.getAgentProvince())){
			area+=loginAgent.getAgentProvince();
			epRecord.setProvince(loginAgent.getAgentProvince());
		}
		if(null!=loginAgent.getAgentCity()&&!"".equals(loginAgent.getAgentCity())){
			area+="-"+loginAgent.getAgentCity();
			epRecord.setCity(loginAgent.getAgentCity());
		}
		if(null!=loginAgent.getAgentCountry()&&!"".equals(loginAgent.getAgentCountry())){
			area+="-"+loginAgent.getAgentCountry();
			epRecord.setCounty(loginAgent.getAgentCountry());
		}
		if(!"".equals(area)){
			epRecord.setAddressId(area);
		}else{
			epRecord.setAddressId(loginAgent.getAgentAreaId());
		}
    	try {
			//扣除发送者
    		AgentInfo upAgent=new AgentInfo(); 
    		upAgent.setExchangeEP(loginAgent.getExchangeEP()-epCount);
    		//产生扣除记录
    		/*epRecord.setId(AgentUUID);
    		epRecord.setEp(-epCount);
    		epRecord.setRecordTypeDesc("赠送给"+(sendType==10?"代理":"会员")+"["+(sendType==10?sendAgent.getCompany():sendUser.getUid())+"]");
    		epRecordService.create(epRecord);*/
    		//扣除
    		agentInfoService.updateById(loginAgent.getId(), upAgent);
    		//产生接受者记录
    		epRecord.setId(SendUUID);
    		epRecord.setEp(epCount);
    		epRecord.setRecordTypeDesc("代理["+loginAgent.getCompany()+"]赠送给"+(sendType==10?"代理":"会员")+"["+(sendType==10?sendAgent.getCompany():sendUser.getUid())+"]");
    		epRecordService.create(epRecord);
    		//接受者增加
    		if(sendType==10){
        		sendAgent=agentInfoService.getByCondition(agentInfo);//取出即时数据
        		Sendold=sendAgent.getExchangeEP();
        		upAgent.setExchangeEP(sendAgent.getExchangeEP()+epCount);
        		agentInfoService.updateById(sendAgent.getId(), upAgent);
    		}else{
    			sendUser=userService.readByUid(id);//取出即时数据
    			Sendold=sendUser.getExchangeEP();
    			User upuser=new User();
    			upuser.setExchangeEP(sendUser.getExchangeEP()+epCount);
    			userService.updateById(sendUser.getId(), upuser);
    		}
    		return new JsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			//发送者回滚
			AgentInfo upAgent=new AgentInfo(); 
			epRecordService.deleteById(AgentUUID);
			upAgent.setExchangeEP(Agentold);
			agentInfoService.updateById(loginAgent.getId(), upAgent);
			//接受者回滚
			epRecordService.deleteById(SendUUID);
			if(sendType==10){
				upAgent.setExchangeEP(Sendold);
				agentInfoService.updateById(sendAgent.getId(), upAgent);
			}else{
				User upuser=new User();
    			upuser.setExchangeEP(Sendold);
				userService.updateById(sendUser.getId(), upuser);
			}
			return new JsonResult(4,"服务器异常,转账失败!");
		}
    }
}
