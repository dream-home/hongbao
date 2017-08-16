package com.yanbao.controller;

import com.mall.model.City;
import com.yanbao.core.page.JsonResult;
import com.yanbao.service.SysCityService;
import com.yanbao.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sysCity")
public class SysCityController {

	@Autowired
	private SysCityService sysCityService;

	/**
	 * 模糊查询城市形象
	 */
	@ResponseBody
	@RequestMapping(value = "/getCityByField", method = RequestMethod.GET)
	public JsonResult about(HttpServletRequest request,@RequestParam String field) throws Exception {
		if(ToolUtil.isEmpty(field)){
			return new JsonResult(1,"请输入要搜索的信息");
		}
		List<City> list = sysCityService.getAllCity(field);
		return new JsonResult(list);
	}

}
