package com.yanbao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.mall.model.Agreement;
import com.yanbao.service.AgreementService;

@Controller
@RequestMapping("/agreement")
public class AgreementController {
	@Autowired
	private AgreementService agreementService;

	/**
	 * 关于协议
	 */
	@ResponseBody
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public JsonResult about(HttpServletRequest request) throws Exception {
		Agreement agreement = agreementService.get();

		return new JsonResult(agreement);
	}

}
