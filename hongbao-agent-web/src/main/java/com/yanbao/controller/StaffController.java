package com.yanbao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mall.model.EpRecord;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
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
import com.mall.model.AgentStaff;
import com.mall.model.MenuPermission;
import com.yanbao.constant.StatusType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.service.HbAgentStaffService;
import com.yanbao.service.HbMenuPermissionService;
import com.yanbao.service.impl.HbMenuPermissionServiceImpl;
import com.yanbao.vo.AgentStaffVo;
import com.yanbao.vo.MenuPermissionVo;
/***
 * 员工相关
 */
@Controller
@RequestMapping("/staff")
public class StaffController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
	private HbAgentStaffService hbAgentStaffService;
    @Autowired
	private HbMenuPermissionService hbMenuPermissionService;
    //当前类权限id
    private static String classPermissionId="7";
    
    @ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(HttpServletRequest request,@RequestBody AgentStaffVo staff) {
    	if(!ispermission(classPermissionId, request)){
    		return new JsonResult(2,"无权限");
    	}
    	try {
    		if(null==staff.getAgentId()
    				||null==staff.getStaffAccount()
    				||null==staff.getStaffPassword()
    				||null==staff.getStaffName()){
    			return new JsonResult(1,"参数错误!");
    		}
    		Token token = TokenUtil.getSessionUser(request);
    		AgentInfo ag=getAgent(token);//agentInfoService.getById(agentId);
    		if(null==ag){
    			return new JsonResult(1,"代理信息错误");
    		}
    		if(!ag.getId().equals(staff.getAgentId())){
    			return new JsonResult(2,"无权限");
    		}
        	//判断登录用户名是否存在
			AgentStaff agentStaff = new AgentStaff();
			agentStaff.setLoginName(staff.getStaffAccount());
			agentStaff.setStatus(StatusType.TRUE.getCode());
			agentStaff = hbAgentStaffService.getByCondition(agentStaff);
			if(agentStaff != null && !ToolUtil.isEmpty(agentStaff.getAgentId())){
				return new JsonResult(3,"账号已存在");
			}

			boolean b = PasswordUtil.checkPass(staff.getStaffPassword());
			if (!b) {
				return new JsonResult(6, "密码不符合规范");
			}

        	AgentStaff as=new AgentStaff();
        	as.setId(UUIDUtil.getUUID());
        	as.setAgentId(staff.getAgentId());
        	as.setNickName(staff.getStaffName());
        	as.setPassword(staff.getStaffPassword());
        	as.setPayPassWord(staff.getStaffPassword());
        	as.setUserName(staff.getStaffName());
        	as.setLoginName(staff.getStaffAccount());
        	as.setStatus(StatusType.TRUE.getCode());
        	Map<String, MenuPermission> map=hbMenuPermissionService.getAllByMap();
        	String permissionStr="";
        	for (MenuPermissionVo ps : staff.getPermission()) {
        		MenuPermission mps=map.get(ps.getPermissionId());
        		if(null!=mps){
        			permissionStr+=mps.getId()+",";
        		}
			}
        	as.setMenuPermissions((!"".equals(permissionStr)?permissionStr.substring(0, permissionStr.length()-1):""));
        	int count=hbAgentStaffService.addPo(as);
        	if(count>0){
        		return new JsonResult();
        	}else{
        		return new JsonResult(3,"保存错误!");
        	}
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}
    
    @ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(HttpServletRequest request,@RequestBody AgentStaffVo staff){
    	try {
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    		if(null==staff.getStaffId()){
        		return new JsonResult(1,"参数错误!");
        	}
    		AgentStaff as=hbAgentStaffService.getPoByPk(staff.getStaffId());
    		if(null==as){
    			return new JsonResult(3,"员工不存在!");
    		}
			//判断登录用户名是否存在
			if(!ToolUtil.isEmpty(staff.getStaffAccount())){
				AgentStaff agentStaff = new AgentStaff();
				agentStaff.setLoginName(staff.getStaffAccount());
				agentStaff.setStatus(StatusType.TRUE.getCode());
				agentStaff = hbAgentStaffService.getByCondition(agentStaff);
				if(agentStaff != null && !ToolUtil.isEmpty(agentStaff.getAgentId())){
					return new JsonResult(3,"登录账号已存在");
				}
			}

			//判断是否修改密码
			if(!ToolUtil.isEmpty(staff.getStaffPassword())){
				boolean b = PasswordUtil.checkPass(staff.getStaffPassword());
				if (!b) {
					return new JsonResult(6, "密码不符合规范");
				}
				//如果不为空,就进行修改密码
				String pwd = Md5Util.MD5Encode(staff.getStaffPassword(),as.getSalt());
				as.setPassword(pwd);
				as.setPayPassWord(pwd);
			}

    		as.setNickName(staff.getStaffName());
        	as.setUserName(staff.getStaffName());
        	as.setLoginName(staff.getStaffAccount());
        	Map<String, MenuPermission> map=hbMenuPermissionService.getAllByMap();
        	String permissionStr="";
        	for (MenuPermissionVo ps : staff.getPermission()) {
        		MenuPermission mps=map.get(ps.getPermissionId());
        		if(null!=mps){
        			permissionStr+=mps.getId()+",";
        		}
			}
        	as.setMenuPermissions((!"".equals(permissionStr)?permissionStr.substring(0, permissionStr.length()-1):""));
        	int count=hbAgentStaffService.modPoNotNull(as);
        	if(count>0){
        		return new JsonResult();
        	}else{
        		return new JsonResult(3,"保存错误!");
        	}
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}
    
    @ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public JsonResult delete(HttpServletRequest request,@RequestBody AgentStaffVo staff){
    	try {
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    		if(null==staff.getStaffId()){
        		return new JsonResult(1,"参数错误!");
        	}
    		AgentStaff as=hbAgentStaffService.getPoByPk(staff.getStaffId());
    		if(null==as){
    			return new JsonResult(3,"员工不存在!");
    		}
    		if(as.getStatus()!=StatusType.TRUE.getCode()){
    			return new JsonResult();
    		}
    		as.setStatus(StatusType.FALSE.getCode());
    		int count=hbAgentStaffService.modPoNotNull(as);
        	if(count>0){
        		return new JsonResult();
        	}else{
        		return new JsonResult(3,"保存错误!");
        	}
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}
    
    @ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public JsonResult info(HttpServletRequest request,String staffId){
    	try {
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    		if(null==staffId){
        		return new JsonResult(1,"参数错误!");
        	}
    		AgentStaff as=hbAgentStaffService.getPoByPk(staffId);
    		if(null==as||as.getStatus()!=StatusType.TRUE.getCode()){
    			return new JsonResult(3,"员工不存在!");
    		}
    		List<AgentStaff> list = new ArrayList<AgentStaff>();
    		list.add(as);
    		return new JsonResult(getStaffVo(list));
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}
    
    @ResponseBody
	@RequestMapping(value = "/infotab", method = RequestMethod.GET)
	public JsonResult infotab(HttpServletRequest request, String agentId, Page page){
    	try {
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    		if(null==agentId){
        		return new JsonResult(1,"参数错误!");
        	}
    		//获取代理 验证代理是否正确
    		AgentStaff searchModel=new AgentStaff();
    		searchModel.setAgentId(agentId);
    		searchModel.setStatus(StatusType.TRUE.getCode());
    		List<AgentStaff> listas=hbAgentStaffService.getPoListByPage(searchModel,page);
			int count = hbAgentStaffService.countPoListByPage(searchModel);

			List<AgentStaffVo> agentStaffVoList = getStaffVo(listas);
			PageResult<AgentStaffVo> voPage = new PageResult<AgentStaffVo>();
			voPage.setRows(agentStaffVoList);
			voPage.setPageNo(page.getPageNo());
			voPage.setPageSize(page.getPageSize());
			voPage.setTotalSize(count);

    		return new JsonResult(voPage);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}

    @ResponseBody
	@RequestMapping(value = "/permission", method = RequestMethod.GET)
	public JsonResult permission(HttpServletRequest request){
    	try {
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    		return new JsonResult(getPermissionVo(hbMenuPermissionService.getAll()));
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
	}
    
    /*//接口添加权限数据   
    @ResponseBody
   	@RequestMapping(value = "/addpermission", method = RequestMethod.POST)
   	public JsonResult addpermission(HttpServletRequest request){
    	try {
    		MenuPermission mp=new MenuPermission();
    		mp.setId(UUIDUtil.getUUID());
    		mp.setStatus(0);
    		mp.setRemark("----------------");
    		mp.setCreateTime(new Date());
    		mp.setUpdateTime(new Date());
    		mp.setDescription("============");
    		System.out.println(hbMenuPermissionService.addPo(mp));
    		return new JsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(500,"异常错误!");
		}
   	}*/
    
    //重置内存中的权限数据
    @ResponseBody
	@RequestMapping(value = "/reloadPermission", method = RequestMethod.GET)
	public JsonResult reloadPermission(HttpServletRequest request) throws Exception {
    	if(null!=HbMenuPermissionServiceImpl.staticPermission){
    		HbMenuPermissionServiceImpl.staticPermission.clear();
    	}
    	if(null!=HbMenuPermissionServiceImpl.staticPermissionMap){
    		HbMenuPermissionServiceImpl.staticPermissionMap.clear();
    	}
    	hbMenuPermissionService.getAllByMap();
		return new JsonResult();
	}
    
    
    
    
    
    
    
    public List<AgentStaffVo> getStaffVo(List<AgentStaff> list){
    	List<AgentStaffVo> result=new ArrayList<AgentStaffVo>();
    	for (AgentStaff staff : list) {
    		AgentStaffVo vo = new AgentStaffVo();
    		vo.setStaffId(staff.getId());
    		vo.setAgentId(staff.getAgentId());
    		vo.setStaffAccount(staff.getLoginName());
    		vo.setStaffName(staff.getUserName());
    		vo.setStaffPassword(staff.getPassword());
    		vo.setStatus(staff.getStatus());
    		String[] ids=staff.getMenuPermissions().trim().split(",");
    		List<MenuPermission> listmenu=new ArrayList<MenuPermission>();
    		for (String string : ids) {
    			if(null!=hbMenuPermissionService.getAllByMap().get(string)){
    				listmenu.add(hbMenuPermissionService.getAllByMap().get(string));
    			}else{
    				continue;
    			}
			}
    		vo.setPermission(getPermissionVo(listmenu));
    		result.add(vo);
		}
    	return result;
    }
    
    public List<MenuPermissionVo> getPermissionVo(List<MenuPermission> list){
    	List<MenuPermissionVo> result=new ArrayList<MenuPermissionVo>();
    	for (MenuPermission mp : list) {
    		MenuPermissionVo vo = new MenuPermissionVo();
    		vo.setPermissionId(mp.getId());
    		vo.setDescription(mp.getDescription());
    		result.add(vo);
		}
    	return result;
    }
}
