package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.model.Parameter;
import com.yanbao.core.page.JsonResult;
import com.yanbao.service.ParameterService;
import com.yanbao.util.ParamUtil;


/**
 * Created by summer on 2017年7月31日15:12:52;
 * 
 * 系统设置
 */
@Controller
@RequestMapping("/systemSetting")
public class SystemSettingController extends BaseController {
	
	@Autowired
	ParameterService parameterService;
	
    @ResponseBody
    @RequestMapping("/getAllSetting")
    public JsonResult getAllSetting(){
    	try {
    		return success(ParamUtil.getIstance().getAllModel());
		} catch (Exception e) {
			e.printStackTrace();
			return fail("系统错误！");
		}
        
    }
    
    @ResponseBody
    @RequestMapping("/saveSetting")
    public JsonResult saveSetting(@RequestBody List<Parameter> list){
    	try {
    		parameterService.batchUpdate(list);
    		return success();
		} catch (Exception e) {
			e.printStackTrace(); 
			return fail("系统错误！");
		}
        
    }
    


}
