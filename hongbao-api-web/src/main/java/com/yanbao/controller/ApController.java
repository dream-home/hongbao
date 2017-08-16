package com.yanbao.controller;

import com.yanbao.constant.ResultCode;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.util.TokenUtil;
import com.yanbao.vo.DistanceVo;
import com.yanbao.vo.StoreVo;
import com.yanbao.vo.UserV42Vo;
import com.yanbao.vo.WalletV42Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/test")
public class ApController {

    private static final Logger logger = LoggerFactory.getLogger(ApController.class);


    /**
     * 商铺列表有距离排序
     */
    @ResponseBody
    @RequestMapping(value = "/mall/store/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, Page page, DistanceVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        List<StoreVo> row = new ArrayList<>();
        PageResult<StoreVo> result2 = new PageResult<StoreVo>(page.getPageNo(), page.getPageSize(), 0, row);
        return new JsonResult(result2);
    }

    /**
     * 完善资料
     */
    @ResponseBody
    @RequestMapping(value = "/user/completeinfo", method = RequestMethod.POST)
    public JsonResult completeInfo(HttpServletRequest request, @RequestBody UserV42Vo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        return new JsonResult(ResultCode.ERROR.getCode(), "完善信息成功，数据稍后会刷新");
    }

    /**
     * 积分兑换/提现
     */
    @ResponseBody
    @RequestMapping(value = "/wallet/v42/exchange", method = RequestMethod.POST)
    public JsonResult v42Exchange(HttpServletRequest request, @RequestBody WalletV42Vo vo) throws Exception {
        return new JsonResult("该功能暂时关闭");
    }
}
