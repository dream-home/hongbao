package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.IndexBanner;
import com.yanbao.service.IndexBannerService;
import com.yanbao.util.UUIDUtil;

/***
 * 
 * @author zyc 2017-05-19 10:00:00
 * 
 *
 */



@Controller
@RequestMapping("/indexBanner")
public class IndexBannerController extends BaseController {
	
	 	@Autowired
	    IndexBannerService indexBannerService;

	    @ResponseBody
	    @RequestMapping("/create")
	    public JsonResult ad(IndexBanner indexBanner) {
	    	indexBanner.setId(UUIDUtil.getUUID());
	       
	    	indexBannerService.create(indexBanner);
	        return success();
	    }

	    @ResponseBody
	    @RequestMapping("/delete")
	    public JsonResult delete(String id) {
	    	indexBannerService.deleteById(id);
	        return success();
	    }

	    @ResponseBody
	    @RequestMapping("/update")
	    public JsonResult update(IndexBanner indexBanner) {
	    	indexBannerService.updateById(indexBanner.getId(), indexBanner);
	        return success();
	    }

	    @ResponseBody
	    @RequestMapping("/list")
	    public JsonResult list(Page page) {
	    	
	    	IndexBanner indexBanner = new IndexBanner();
	    	indexBanner.setStatus(1);
	    	
	    	
	        int count = indexBannerService.readCount(indexBanner);
	        if (count==0){
	            return fail("没有资源");
	        }
	        List list = indexBannerService.readList(indexBanner, page.getPageNo(), page.getPageSize(), count);
	        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
	        return success(pageResult);
	    }

	
	

	
	

}
