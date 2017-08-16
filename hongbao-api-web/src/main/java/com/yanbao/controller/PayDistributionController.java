package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.User;
import com.taobao.api.internal.util.Base64;
import com.yanbao.constant.PayDistributionType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.mall.model.PayDistribution;
import com.yanbao.service.PayDistributionService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.qrcode.QRCodeUtil;
import com.yanbao.vo.PayDistributionVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;

/* 
 * 文件名：付款分销控制类  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
@Controller
@RequestMapping("/payDistribution")
public class PayDistributionController {
	
	@Autowired
	private PayDistributionService payDistributionService;
	@Autowired
	private UserService userService;
	@Autowired
	private WalletRechargeService walletRechargeService;
	
	/**
	 * 根据用户id查询付款分销
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
	public JsonResult getByUserId(HttpServletRequest request, Integer type, String userid) throws Exception {

		if(StringUtils.isEmpty(userid)){
			return new JsonResult(1,"请求参数错误，不能获取二维码参数");
		}
		if (type != null && type > 1) {
			User store = userService.getById(userid);
			if (StringUtils.isEmpty(store.getStoreId())) {
				return new JsonResult(1, "未开店，不能获取商家二维码参数");
			}
			type = PayDistributionType.store.getCode();
		} else {
			type = PayDistributionType.member.getCode();
		}
		PayDistribution payDistribution = payDistributionService.getByUserId(userid, type);

		if (payDistribution == null) {
			payDistribution = new PayDistribution();
			payDistribution.setBusinessSendEp(0d);
			payDistribution.setFirstReferrerScale(0d);
			payDistribution.setSecondReferrerScale(0d);
			payDistribution.setThirdReferrerScale(0d);
			payDistribution.setDiscountEP(0d);
		} else {
			PayDistributionVo vo = new PayDistributionVo();
			BeanUtils.copyProperties(vo, payDistribution);
		}

		return new JsonResult(payDistribution);
	}
	
	@ResponseBody
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public JsonResult updatePayDistribution(HttpServletRequest request, @RequestBody PayDistributionVo vo) throws Exception{
		
		final Token token = TokenUtil.getSessionUser(request);
		
		//设置分销
		if (vo.getFirstReferrerScale() == null || vo.getFirstReferrerScale() < 0d) {
			vo.setFirstReferrerScale(0d);
		}
		if (vo.getSecondReferrerScale() == null || vo.getSecondReferrerScale() < 0d) {
			vo.setSecondReferrerScale(0d);
		}
		if (vo.getThirdReferrerScale() == null || vo.getThirdReferrerScale() < 0d) {
			vo.setThirdReferrerScale(0d);
		}
		//设置Ep
		if (vo.getBusinessSendEp() == null || vo.getBusinessSendEp() < 0d) {
			vo.setBusinessSendEp(0d);
		}
		//设置参数类型type
		if (vo.getType() == null || vo.getType() < 1) {
			vo.setType(PayDistributionType.member.getCode());
		}
		//判断
		if (vo.getBusinessSendEp() + vo.getFirstReferrerScale() + vo.getSecondReferrerScale() + vo.getThirdReferrerScale() > 100d) {
			return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
		}
		//判断EP折扣率
		if (vo.getDiscountEP()>100d||vo.getDiscountEP()<0d) {
			return new JsonResult(10, "EP折扣率设置超出范围");
		}
		PayDistribution payDistribution = payDistributionService.getByUserId(token.getId(), vo.getType());
		
		if(payDistribution == null){

			payDistribution = new PayDistribution();
			if (vo.getType()==1){
				payDistribution.setRemark("面对面扫码");
			}else {
				payDistribution.setRemark("商家二维码扫码");
			}
			BeanUtils.copyProperties(payDistribution,vo);
			payDistribution.setUserId(token.getId());
			payDistributionService.addPayDistribution(payDistribution);
		}else{
			payDistribution.setBusinessSendEp(vo.getBusinessSendEp());
			payDistribution.setFirstReferrerScale(vo.getFirstReferrerScale());
			payDistribution.setSecondReferrerScale(vo.getSecondReferrerScale());
			payDistribution.setThirdReferrerScale(vo.getThirdReferrerScale());
			payDistribution.setDiscountEP(vo.getDiscountEP());
			payDistributionService.updatePayDistribution(payDistribution);
		}
		
		return new JsonResult();
	}
	
	/**
	 * 生成面对面扫码支付二维码
	 */
	@ResponseBody
	@RequestMapping(value = "/qrcode", method = RequestMethod.POST)
	public JsonResult getPicBase64Code(HttpServletRequest request,@RequestBody PayDistributionVo vo) throws Exception {
		
		final Token token = TokenUtil.getSessionUser(request);
		PayDistribution payDistribution = payDistributionService.getByUserId(token.getId(), PayDistributionType.member.getCode());
		User user = userService.getById(token.getId());
		
		//设置分销
		if (vo.getPayAmount() == null || vo.getPayAmount() < 0d) {
			vo.setPayAmount(0d);
		}
		if (vo.getFirstReferrerScale() == null || vo.getFirstReferrerScale() < 0d) {
			vo.setFirstReferrerScale(0d);
		}
		if (vo.getSecondReferrerScale() == null || vo.getSecondReferrerScale() < 0d) {
			vo.setSecondReferrerScale(0d);
		}
		if (vo.getThirdReferrerScale() == null || vo.getThirdReferrerScale() < 0d) {
			vo.setThirdReferrerScale(0d);
		}
		//设置Ep
		if (vo.getBusinessSendEp() == null || vo.getBusinessSendEp() < 0d) {
			vo.setBusinessSendEp(0d);
		}
		if (vo.getType() == null || vo.getType() < 1) {
			vo.setType(PayDistributionType.member.getCode());
		}
		//判断
		if (vo.getBusinessSendEp() + vo.getFirstReferrerScale() + vo.getSecondReferrerScale() + vo.getThirdReferrerScale() > 100d) {
			return new JsonResult(9, "分销比例不能小于0，总和必须小于100");
		}
		
		if (StringUtils.isEmpty(vo.getStoreUserId())) {
			return new JsonResult(10, "商家用户id不能为空");
		}
		if(vo.getDiscountEP() == null){
			vo.setDiscountEP(0d);
		}
		
		if(payDistribution == null){
			payDistribution = new PayDistribution();
			BeanUtils.copyProperties(payDistribution,vo);
			payDistribution.setUserId(token.getId());
			payDistribution.setType(PayDistributionType.member.getCode());
			payDistributionService.addPayDistribution(payDistribution);
		}else{
			payDistribution.setBusinessSendEp(vo.getBusinessSendEp());
			payDistribution.setFirstReferrerScale(vo.getFirstReferrerScale());
			payDistribution.setSecondReferrerScale(vo.getSecondReferrerScale());
			payDistribution.setThirdReferrerScale(vo.getThirdReferrerScale());
			payDistribution.setDiscountEP(vo.getDiscountEP());

			payDistributionService.updatePayDistribution(payDistribution);
		}
		
		//生成二维码
		String content = JSON.toJSONString(vo);
		String imgPath = request.getSession().getServletContext().getRealPath("/resources/") + "/css/img/logo.png";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		QRCodeUtil.encode(content, imgPath, baos);
		String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
		baos.close();
		
		return new JsonResult(rstr);
	}
	
	public static void main(String[] args) {
		
		Integer a = 1;
		Integer b = 1;
		
		int c = 1;
		int d = 1;
		
		System.out.println(a.equals(b));
		System.out.println(a.equals(c));
		System.out.println(c == d);
		System.out.println(a == b);
		System.out.println(a == c);
	}

}

