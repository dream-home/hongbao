package com.yanbao.controller;

import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.EpRecord;
import com.mall.model.User;
import com.yanbao.service.EPRecordService;
import com.yanbao.service.UserService;
import com.yanbao.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zzwei on 2017/6/17 0017.
 */
@Controller
@RequestMapping("/eptrade")
public class EpTradeController {
    Logger logger = LoggerFactory.getLogger(EpTradeController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private EPRecordService ePRecordService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public JsonResult eptradeList(HttpServletRequest request, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        String[] recordTypes = request.getParameterValues("recordType");
        if (recordTypes.length == 0) {
            return new JsonResult(3, "EP流水类型不能为空");
        }
        if (page.getPageNo() == 0) {
            page.setPageNo(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        PageResult<EpRecord> result = ePRecordService.getPage(user.getId(), recordTypes, page);
        return new JsonResult(result);
    }
}
