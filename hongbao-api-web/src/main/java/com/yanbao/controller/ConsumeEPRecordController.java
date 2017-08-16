package com.yanbao.controller;

import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.ConsumeEPRecord;
import com.yanbao.service.ConsumeEPRecordService;
import com.yanbao.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/* 
 * 文件名：消费ep流水记录 
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月4日
 * 版本号：v1.0
*/
@Controller
@RequestMapping("/ep")
public class ConsumeEPRecordController {
	
    //private static final Logger logger = LoggerFactory.getLogger(ConsumeEPecordController.class);

    @Autowired
    private ConsumeEPRecordService consumeEPecordService;


    //查询消费ep流水记录
    @ResponseBody
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public JsonResult record(HttpServletRequest request, Page page) throws Exception {

        Token token = TokenUtil.getSessionUser(request);
        PageResult<ConsumeEPRecord> result = consumeEPecordService.getPage(token.getId(), page);
        return new JsonResult(result);
    }

}

