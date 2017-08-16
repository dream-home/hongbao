package com.yanbao.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.model.AgentEpOrder;
import com.mall.model.AgentInfo;
import com.mall.model.AgentStaff;
import com.mall.model.EpRecord;
import com.mall.model.Image;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Admin;
import com.mall.model.User;
import com.yanbao.service.AdminService;
import com.yanbao.service.AgentEpOrderService;
import com.yanbao.service.AgentInfoService;
import com.yanbao.service.AgentStaffService;
import com.yanbao.service.EpRecordService;
import com.yanbao.service.HbImgageService;
import com.yanbao.service.UserService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.AccountVo;
import com.yanbao.vo.AgentInfoVo;
import com.yanbao.vo.EpOrderVo;
import com.yanbao.vo.ParameterVo;


/**
 * Created by Administrator on 2017/6/19.
 */
@Controller
@RequestMapping("/agentList")
public class AgentListController extends BaseController{

    @Autowired 
    private AgentInfoService agentInfoService;
    @Autowired
    private HbImgageService imgageService;
    @Autowired
    private AgentEpOrderService agentEpOrderService;
    @Autowired
    private EpRecordService epRecordService;
    @Autowired
    private AgentStaffService agentStaffService;
    @Autowired
    private UserService userService;
    
    
    @Autowired
    AdminService adminService;
    @Value("${systemSalt}")
    String salt;
    
    DecimalFormat df = new DecimalFormat("######0.00");   
    /**
     * 查询代理信息
     * @param agentInfo
     * @return
     */
    @RequestMapping("/getAgentByID")
    @ResponseBody
    public JsonResult getAgentByID(Page page,String agentId) throws Exception{
    	if(null==agentId){
    		return fail(1,"参数错误");
    	}
    	AgentInfo ai=agentInfoService.readById(agentId);
    	if(null==ai){
    		return fail(2,"未查询到代理");
    	}
    	if(null!=ai.getStatus()&&ai.getStatus()==3){
    		return fail(3,"代理被关闭");
    	}
    	List<String> cradids=new ArrayList<String>();
    	List<String> licenseids=new ArrayList<String>();
    	List<Image> idcardImg=new ArrayList<Image>();
    	List<Image> licensesImg=new ArrayList<Image>();
    	if(null!=ai.getCardIconId()){
	    	String idcards=ai.getCardIconId();
	    	String[] arrid=idcards.split(",");
	    	for (String string : arrid) {
	    		cradids.add(string);
			}
	    	idcardImg=imgageService.readByIds(licenseids);
    	}
    	if(null!=ai.getLicenseIconId()){
    		String license=ai.getLicenseIconId();
	    	String[] arrlic=license.split(",");
	    	for (String string : arrlic) {
	    		licenseids.add(string);
			}
	    	licensesImg=imgageService.readByIds(licenseids);
    	}
    	
    	AgentInfoVo avo=new AgentInfoVo();
    	BeanUtils.copyProperties(avo, ai);

    	avo.setIdCard(idcardImg);
    	avo.setLicense(licensesImg);
    	return success(avo);
    }
    
    /**
     * ID查看代理(会员)姓名
     * @return
     */
    @RequestMapping("/getInfoByid")
    @ResponseBody
    public JsonResult getAgentByUid(Page page,String id) throws Exception{
    	if(null==id){
    		return fail(1,"参数错误");
    	}
    	//查询代理
    	AgentInfo selectai=new AgentInfo();
    	selectai.setLoginName(id);
    	selectai.setStatus(1);
    	AgentInfo ai=agentInfoService.readOne(selectai);
    	int type=0;//0为代理 1为会员
    	User user=null;
    	if(null==ai){
    		Integer ID=null;
    		try {
    			ID=Integer.parseInt(id);
			} catch (Exception e) {
				return fail(2,"未查询到账户");
			}
    		//查询用户
    		User find=new User();
        	find.setUid(ID);
        	find.setStatus(1);
        	user = userService.readOne(find);
        	if(null==user){
        		return fail(2,"未查询到账户");
        	}else{
        		type=1;
        	}
    	}
    	Map<String, String> resultMap=new HashMap<String, String>();
    	if(type==0){
    		resultMap.put("id", ai.getId());
    		resultMap.put("name", ai.getUserName());
    		resultMap.put("type", "agent");
    	}else{
    		resultMap.put("id", user.getId());
    		resultMap.put("name", user.getUserName());
    		resultMap.put("type", "member");
    	}
    	return success(resultMap);
    }
    
    /**
     * UID查看会员信息
     * @return
     *//*
    @RequestMapping("/getMemberByUid")
    @ResponseBody
    public JsonResult getMemberByUid(Page page,Integer uid) throws Exception{
    	if(null==uid){
    		return new JsonResult(1,"参数错误");
    	}
    	User find=new User();
    	find.setUid(uid);
    	User user = userService.readOne(find);
    	if(null==user){
    		return new JsonResult(2,"未查询到会员");
    	}
    	if(null!=user.getStatus()&&user.getStatus()==0){
    		return new JsonResult(3,"会员被关闭");
    	}
    	UserVo avo=new UserVo();
    	BeanUtils.copyProperties(avo, user);
    	return new JsonResult(avo);
    }*/
    
    
    /**
     * (会员)代理EP充值
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/rechargeEp",method= {RequestMethod.POST,RequestMethod.OPTIONS})
    public JsonResult rechargeEp(@RequestBody ParameterVo vo,HttpServletRequest request) throws Exception{
    	
    	Token token = getToken(request);
        Admin admin = adminService.readById(token.getId());
    	
    	
    	String id=vo.getId();
    	Double epCount=vo.getEpCount();
    	String password=vo.getPassword();
    	String remark=vo.getRemark();
    	if(null==id||null==epCount||null==password){
    		return fail(1,"参数错误");
    	}
    	AgentInfo selectai=new AgentInfo();
    	selectai.setLoginName(id);
    	AgentInfo ai=null;
    	List<AgentInfo> list=agentInfoService.readAll(selectai);
    	for (AgentInfo agentInfo : list) {
			if(agentInfo.getStatus()==1){
				ai=agentInfo;
			}
		}
    	int type=0;//0为代理 1为会员
    	User user=null;
    	if(null==ai){
    		Integer ID=null;
    		try {
    			ID=Integer.parseInt(id);
			} catch (Exception e) {
				return fail(2,"未查询到账户");
			}
    		//查询用户
    		User find=new User();
        	find.setUid(ID);
        	find.setStatus(1);
        	user = userService.readOne(find);
        	if(null==user){
        		return fail(2,"未查询到账户");
        	}else{
        		type=1;
        	}
    	}else{
    		if(ai.getStatus()!=1){
    			return fail(2,"未查询到代理账户");
    		}
    	}
    	AgentEpOrder aeo=new AgentEpOrder();
    	EpRecord er=new EpRecord();
    	//如果是负数判断当前账户是否够EP
    	if(epCount<0){
    		if(type==0){//代理
    			if(ai.getExchangeEP()-Double.parseDouble(df.format(Math.abs(epCount)))<=0){
    				return fail(2,"余额不足");
    			}
    			//减去或加上EP
				ai.setExchangeEP(ai.getExchangeEP()-Double.parseDouble(df.format(Math.abs(epCount))));
    		}else{//会员
    			if(user.getExchangeEP()-Double.parseDouble(df.format(Math.abs(epCount)))<=0){
    				return fail(2,"余额不足");
    			}
    			//减去或加上EP
    			user.setExchangeEP(user.getExchangeEP()-Double.parseDouble(df.format(Math.abs(epCount))));
    		}
    		aeo.setRecordType(3);//扣除类型	
    	}else{
			//减去或加上EP
    		if(type==0){//代理
	    		ai.setExchangeEP(ai.getExchangeEP()+Double.parseDouble(df.format(epCount)));
    		}else{
    			user.setExchangeEP(user.getExchangeEP()+Double.parseDouble(df.format(epCount)));
    		}
    		aeo.setRecordType(0);//充值类型
    	}
    	//当前登录用户密码验证
    	if(!Md5Util.md5(password+salt).equals(admin.getPayPassWord())){
    		return fail(3,"操作密码有误");
    	}
    	if(type==0){//代理
    		agentInfoService.updateById(ai.getId(), ai);
    		aeo.setAgentAreaId(ai.getAgentAreaId());
        	aeo.setProvince(ai.getAgentProvince());
        	aeo.setCity(ai.getAgentCity());
        	aeo.setCounty(ai.getAgentCountry());
        	aeo.setUserId(ai.getId());
        	
        	er.setReceiveUserId(ai.getId());
        	er.setAddressId(ai.getAgentAreaId());
        	er.setProvince(ai.getAgentProvince());
        	er.setCity(ai.getAgentCity());
        	er.setCounty(ai.getAgentCountry());
        	er.setUserType(3);//用户类型 1:普通会员 2：商家 3：代理商
    	}else{//会员
    		userService.updateById(user.getId(), user);
    		er.setUserType(1);//用户类型 1:普通会员 2：商家 3：代理商
    		aeo.setUserId(user.getId());
    		er.setReceiveUserId(user.getId());
    	}
    	
    	//加入流水订单
    	aeo.setId(UUIDUtil.getUUID());
    	aeo.setOrderNo(UUIDUtil.getUUID());
    	aeo.setEp(epCount);
    	aeo.setCreateTime(new Date());
    	aeo.setUpdateTime(new Date());
    	aeo.setStatus(StatusType.TRUE.getCode());
    	aeo.setRemark(null==remark?"系统充值":remark);
    	agentEpOrderService.create(aeo);
    	
    	
    	er.setId(UUIDUtil.getUUID());
    	er.setOrderNo(UUIDUtil.getUUID());
    	er.setEp(epCount);
    	er.setCreateTime(new Date());
    	er.setUpdateTime(new Date());
    	er.setStatus(StatusType.TRUE.getCode());
    	er.setRemark(null==remark?"系统充值":remark);
    	er.setSendUserId(admin.getNickName());
    	//er.setSendUserId(sendUserId);//发送用户ID
    	er.setRecordTypeDesc("系统充值");//流水类型
    	er.setRecordType(9);//EP充值扣除类型
    	epRecordService.create(er);
    	return success();
    }
    
    /**
     * EP充值列表查询
     * @return
     */
    @RequestMapping("/rechargeEpTable")
    @ResponseBody
    public JsonResult rechargeEpTable(String phone,String username,String account,Page page) throws Exception{
    	//查出代理账户
    	AgentInfo ai=new AgentInfo();
    	User finduser=new User();
    	boolean is_condition=false;
    	if(null!=phone&&!"".equals(phone.trim())){
    		ai.setPhone(phone);
    		finduser.setPhone(phone);
    		is_condition=true;
    	}
    	if(null!=username&&!"".equals(username.trim())){
    		ai.setUserName(username);
    		finduser.setUserName(username);
    		is_condition=true;
		}
    	if(null!=account&&!"".equals(account.trim())){
			ai.setLoginName(account);
			Pattern pattern = Pattern.compile("[0-9]*"); 
			Matcher isNum = pattern.matcher(account);
			if(isNum.matches() ){
				try {
					finduser.setUid(Integer.parseInt(account));
				} catch (Exception e) {
				}
			} 
			is_condition=true;
		}
		//所有ID
		List<String> idStrings=new ArrayList<String>();
		Map<String,AccountVo> map=new HashMap<String,AccountVo>();
		List<AgentInfo> allagent=null;
		List<User> alluser=null;
		int count=0;
		List<EpRecord> list=null;
		if(is_condition){//有条件查询
			//查询代理
			allagent=agentInfoService.readAllByOR(ai);
			//所有用户
			alluser=userService.readAllByOR(finduser);
		}else{//无条件查询
			list=epRecordService.readList(page);
			//统计总数不需要分页
			int pageSize=page.getPageSize();
			int pageNum=page.getPageNo();
			page.setPageSize(null);
			count=epRecordService.readList(page).size();
			page.setPageSize(pageSize);
			page.setPageNo(pageNum);
			for (EpRecord epRecord : list) {
				idStrings.add(epRecord.getReceiveUserId());
			}
			alluser=userService.getByUserids(idStrings);
			allagent=agentInfoService.getByUserids(idStrings);
		}
		if(allagent.size()>0){
			for (AgentInfo ag : allagent) {
				idStrings.add(ag.getId());
				map.put(ag.getId(), AccountVo.create(ag.getId(), ag.getLoginName(), ag.getLoginName(), ag.getUserName(), ag.getPhone()));
			}
		}
		if(alluser.size()>0){
			for (User u : alluser) {
				idStrings.add(u.getId());
				map.put(u.getId(), AccountVo.create(u.getId(), u.getUid()+"", u.getAccount(), u.getUserName(), u.getPhone()));
			}
		}
		List<EpOrderVo> result=new ArrayList<EpOrderVo>();
		if(idStrings.size()==0){
			return success();
		}
		if(is_condition){//有条件查询
			list=epRecordService.getByUserids(idStrings,page);
			//统计总数不需要分页
			int pageSize=page.getPageSize();
			int pageNum=page.getPageNo();
			page.setPageSize(null);
			count=epRecordService.getByUserids(idStrings,page).size();
			page.setPageSize(pageSize);
			page.setPageNo(pageNum);
		}
		for (EpRecord epRecord : list) {
			EpOrderVo vo=new EpOrderVo();
			BeanUtils.copyProperties(vo, epRecord);
			vo.setUser(map.get(vo.getReceiveUserId()));
			result.add(vo);
		}
		 return  success(new PageResult(page.getPageNo(), page.getPageSize(), count, result));
    }
    
    /**
     * (开启)关闭代理
     * @return
     */
    @RequestMapping(value="/closeAgent", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult closeAgent(String agentId,String remark,Integer status) throws Exception{
    	if(null==agentId||null==remark||"".equals(remark.trim())){
    		return fail(1,"参数错误");
    	}
    	AgentInfo findai=agentInfoService.readById(agentId);
    	if(null==findai){
    		return fail(1,"未查询到此账户");
    	}
    	//是否开启
    	if(status!=3){
    		AgentInfo isexist=new AgentInfo();
    		isexist.setAgentAreaId(findai.getAgentAreaId());
    		isexist.setStatus(1);
    		int count=agentInfoService.readCount(isexist);
    		if(count>0){
    			return fail(2,"当前区域已存在代理!");
    		}
    	}
    	AgentInfo ai=new AgentInfo();
    	ai.setStatus(status);
    	if(status==3){
    		ai.setRemark(remark);
    	}else{
    		ai.setRemark("");
    	}
    	agentInfoService.updateById(findai.getId(), ai);
    	AgentStaff ast= new AgentStaff();
    	ast.setAgentId(findai.getId());
    	ast.setStatus(status);
    	if(status==3){
    		ast.setRemark("代理账号停用!");
    	}else{
    		ast.setRemark("");
    	}
    	agentStaffService.modPoNotNullByAgent(ast);
    	return success();
    }
}
