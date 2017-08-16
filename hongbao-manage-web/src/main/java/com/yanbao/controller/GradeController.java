package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Grade;

import com.yanbao.service.GradeService;
import com.yanbao.service.UserService;

/**
 * 
 * @author zhengyicai	2017-03-03 17:25
 * ep会员等级
 */

@Controller
@RequestMapping("/grade")
public class GradeController extends BaseController {
	 @Autowired
	 GradeService gradeService;
	 
	 @Autowired
	 UserService userService;
	 
	 
	 @ResponseBody
	 @RequestMapping("/list")
    public JsonResult list(Grade grade, Page page) {
        int count = gradeService.readCount(grade);
        
        List<Grade> list = gradeService.readList(grade, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }
	 
	@ResponseBody
	@RequestMapping("/update")
    public JsonResult list(Grade grade) {
        //int count = gradeService.readCount(grade);
        
        //List<Grade> list = gradeService.readList(grade, page.getPageNo(), page.getPageSize(), count);
        gradeService.updateById(grade.getId(), grade);
		
		//PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success();
    }
	 


}
