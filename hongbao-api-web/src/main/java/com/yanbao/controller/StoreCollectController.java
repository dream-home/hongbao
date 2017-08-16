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

import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Store;
import com.yanbao.service.StoreCollectService;
import com.yanbao.service.StoreService;
import com.yanbao.service.UserService;
import com.yanbao.util.TokenUtil;

@Controller
@RequestMapping("/store/collect")
public class StoreCollectController {

	private static final Logger logger = LoggerFactory.getLogger(StoreCollectController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private StoreCollectService storeCollectService;

	/**
	 * 收藏商铺
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public JsonResult collect(HttpServletRequest request, String storeId) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Boolean collect = storeCollectService.collect(token.getId(), storeId);
		if (collect) {
			storeService.updateCollectNum(storeId, 1);
		}
		return new JsonResult();
	}

	/**
	 * 取消收藏商铺
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public JsonResult cancelCollect(HttpServletRequest request, String storeId) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		Boolean cancelCollect = storeCollectService.cancelCollect(token.getId(), storeId);
		if (cancelCollect) {
			storeService.updateCollectNum(storeId, -1);
		}
		return new JsonResult();
	}

	/**
	 * 我的商铺收藏列表
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public JsonResult getPage(HttpServletRequest request, Page page) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		PageResult<String> result = storeCollectService.getPage(token.getId(), page);
		if (result.getTotalSize() <= 0) {
			return new JsonResult(result);
		}
		List<String> storeIds = result.getRows();
		List<Store> collectList = storeService.getCollectList(storeIds);
		PageResult<Store> result2 = new PageResult<Store>(result.getPageNo(), result.getPageSize(), result.getTotalSize(), collectList);
		return new JsonResult(result2);
	}

}
