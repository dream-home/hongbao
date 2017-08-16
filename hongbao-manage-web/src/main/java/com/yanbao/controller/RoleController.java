package com.yanbao.controller;

import com.yanbao.core.page.JsonResult;
import com.mall.model.SysAdminMenu;
import com.yanbao.service.SysAdminMenuService;
import com.yanbao.service.SysMenuService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zyc
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	
	@Autowired
	SysAdminMenuService sysAdminMenuService;
	@Autowired
	SysMenuService sysMenuService;
	
	
	
	
	
	 @RequestMapping("/list")
	 @ResponseBody
	 public JsonResult list(SysAdminMenu sysAdminMenu){ 
		 
		  List<SysAdminMenu> sysAdminMenuList = sysAdminMenuService.readAll(sysAdminMenu);
		  
		  for(int i  = 0; i<sysAdminMenuList.size();i++){
			  sysAdminMenuList.get(i).setRemark(sysMenuService.readById(sysAdminMenuList.get(i).getMenuId()).getName());
		  }
		  return  success(sysAdminMenuList);
	 }
	 
	 
	 /*@RequestMapping(value="/update", method ={RequestMethod.POST})*/
	 @RequestMapping("/update")
	 @ResponseBody
	 public JsonResult update(@RequestParam(required = false, value = "sysAdminMenu[]")  List<SysAdminMenu> sysAdminMenu){
		 
		 System.out.println(sysAdminMenu);
		 /*for(int i = 0; i<sysAdminMenu.length;i++){
			 //sysAdminMenu.get(i)
			 
			 SysAdminMenu sysAdminMenu1 = new SysAdminMenu();
			 sysAdminMenu1.setStatus(sysAdminMenu[i].getStatus());
			 sysAdminMenuService.updateById(sysAdminMenu[i].getId(), sysAdminMenu1);
		 }*/

		 
		 return success();
	 }
	 
	 
	 

	    @ResponseBody
	    @RequestMapping("/update2")
	    public JsonResult update2(String sysAdminMenu) {
	    	//JSONObject jsStr = JSONObject.fromObject(sysAdminMenu); 
	    	//SysAdminMenu sys = 	  (SysAdminMenu) JSONObject.toBean(jsStr, SysAdminMenu.class);
	    	
	    	//System.out.println(sys.getId());  
	    	
	    	
	    	JSONArray array = new JSONArray();
	    	array = JSONArray.fromObject(sysAdminMenu);
	    	
	    	List<SysAdminMenu> newsList1 =new ArrayList<SysAdminMenu>();
	    	for (int i = 0; i < array.size(); i++) {  
	            //JSONObject对象  
	            JSONObject jsonObj =(JSONObject) array.get(i);  
	              
	            //根据key获取对应的值  
	            //System.out.println(jsonObj.getString("title"));  
	            //将JSONObject对象转换成实体类后添加到List列表中  
	            newsList1.add((SysAdminMenu) JSONObject.toBean(jsonObj ,SysAdminMenu.class));  
	          //  System.out.println(array.get(i).+":"+array.get(i).getLink());  
	        }
	    	
	    	for(int i = 0 ;i<newsList1.size();i++){
	    		sysAdminMenuService.updateById(newsList1.get(i).getId(),newsList1.get(i));
	    	}

	        return success("修改成功");
	    }
	 
	 
	 
	/*@RequestMapping("/update1")
	 @ResponseBody
	 public JsonResult update1(SysAdminMenu sysAdminMenu){
		 		 
		
		SysAdminMenu sysAdminMenu1 = new SysAdminMenu();
		sysAdminMenu1.setStatus(1);
		
		sysAdminMenuService.updateById(sysAdminMenu.getId(), sysAdminMenu1);
		 return success();
	 }
	
	*/
	
	
	
	
	
	
	
	
	

}
