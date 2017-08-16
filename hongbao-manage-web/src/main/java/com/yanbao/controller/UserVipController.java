package com.yanbao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.User;
import com.mall.model.UserVip;
import com.yanbao.service.UserService;
import com.yanbao.service.UserVipService;

/**
 * 会员信息管理类
 */

@Controller
@RequestMapping("/member/vip")
public class UserVipController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserVipService userVipService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonResult list(HttpServletRequest request, Integer uid, Page page) {
		UserVip userVip = new UserVip();
		userVip.setUid(uid);
		int count = userVipService.readCount(userVip);
		List<UserVip> userVips = userVipService.readList(userVip, page.getPageNo(), page.getPageSize(), count);
		PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, userVips);
		return success(pageResult);
	}

	@ResponseBody
	@RequestMapping("/create")
	public JsonResult add(Integer uid) {
		if (uid == null) {
			return fail("请输入UID");
		}
		User cond = new User();
		cond.setUid(uid);
		User user = userService.readOne(cond);
		if (user == null) {
			return fail("UID不存在");
		}
		UserVip model = new UserVip();
		model.setUid(user.getUid());
		model.setUserId(user.getId());
		model.setNickName(user.getNickName());
		model.setUserName(user.getUserName());
		userVipService.add(model);
		return success();
	}

	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(String id) {
		userVipService.del(id);
		return success();
	}

}
