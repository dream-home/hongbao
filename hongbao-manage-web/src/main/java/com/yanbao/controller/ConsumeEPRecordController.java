package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.ConsumeEPRecord;
import com.yanbao.service.ConSumeEPRecordService;



@Controller
@RequestMapping("/consumeEpRecord")
public class ConsumeEPRecordController extends BaseController {
	
	@Autowired
	ConSumeEPRecordService conSumeEPRecordService;
	
	
	
	@ResponseBody
    @RequestMapping("/list")
	public JsonResult list(ConsumeEPRecord consumeEPRecord, Page page){
		
		int count =conSumeEPRecordService.readCount(consumeEPRecord); 	
		if(count == 0){
			return fail("没有数据");
		}
		
		List<ConsumeEPRecord> list = conSumeEPRecordService.readList(consumeEPRecord, page.getPageNo(), page.getPageSize(), count);
		
		return success(new PageResult(page.getPageNo(), page.getPageSize(), count, list));
		
	}
	

}
