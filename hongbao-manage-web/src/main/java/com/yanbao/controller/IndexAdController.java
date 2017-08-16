package com.yanbao.controller;

import com.mall.model.IndexAd;
import com.mall.model.IndexBanner;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.IndexAdService;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/***
 * 
 * @author zyc 2017-05-19 10:00:00
 * 
 *
 */


@Controller
@RequestMapping("/indexAd")
public class IndexAdController extends BaseController {
	@Autowired
    IndexAdService indexAdService;

    @ResponseBody
    @RequestMapping("/create")
    public JsonResult ad(IndexAd indexAd) {
    	indexAd.setId(UUIDUtil.getUUID());
       
    	indexAdService.create(indexAd);
        return success();
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(String id) {
    	indexAdService.deleteById(id);
        return success();
    }

    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(IndexAd indexAd) {
    	indexAdService.updateById(indexAd.getId(), indexAd);
        return success();
    }

    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(Page page) {
    	
    	IndexBanner indexBanner = new IndexBanner();
    	indexBanner.setStatus(1);
    	
    	IndexAd indexAd   = new IndexAd();
    	indexAd.setStatus(1);
    	
    	
        int count = indexAdService.readCount(indexAd);
        if (count==0){
            return fail("没有资源");
        }
        List list = indexAdService.readList(indexAd, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }

}
