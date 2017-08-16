package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.City;
import com.yanbao.service.SysCityService;


/****
 * 
 * @author zyc 2017-05-13 11:02
 *获取省市区
 *
 *
 */


@Controller
@RequestMapping("/city")
public class CityController extends BaseController {
	
	 @Autowired
	 SysCityService sysCityService;
	 
	 
	 
	 @ResponseBody
	 @RequestMapping("/list")
	 
	 public JsonResult list(City city,Page page){
		 int count = sysCityService.readCount(city);
	        if (count==0){
	            return fail("没有资源");
	        } 
	       List<City> cityList = sysCityService.readAll(city);
		 //List<City> cityList = sysCityService.readList(city, page.getPageNo(), page.getPageSize(),count);
		 PageResult pageResult=new PageResult(page.getPageNo(),page.getPageSize(),count,cityList);
		 
		 return success(pageResult);
		 
	 }	 
	 
	 
	 
	

}
