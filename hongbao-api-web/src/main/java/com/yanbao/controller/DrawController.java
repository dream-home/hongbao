package com.yanbao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.DrawType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.util.TokenUtil;

@Controller
@RequestMapping("/user/draw")
public class DrawController {

	private static final Logger logger = LoggerFactory.getLogger(DrawController.class);

	@Autowired
	private GoodsIssueDetailService goodsIssueDetailService;

	/**
	 * 幸运排行列表
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public JsonResult getPage(HttpServletRequest request, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		PageResult<GoodsIssueDetail> result = goodsIssueDetailService.getPage(token.getId(), page);
		return new JsonResult(result);
	}

	/**
	 * 幸运排行详情
	 */
	@ResponseBody
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JsonResult getDetail(HttpServletRequest request, String issueId) throws Exception {
		List<GoodsIssueDetail> drawList = goodsIssueDetailService.getDrawList(issueId, DrawType.FINISH.getCode());
		return new JsonResult(drawList);
	}

}
