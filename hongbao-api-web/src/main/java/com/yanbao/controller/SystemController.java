package com.yanbao.controller;

import com.mall.model.FeedBack;
import com.mall.model.Parameter;
import com.yanbao.core.page.JsonResult;
import com.yanbao.service.FeedBackService;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.QiNiuUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.FeedBackVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {

    @Autowired
	private FeedBackService feedBackService;

	/**
	 * 关于系统
	 */
	@ResponseBody
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public JsonResult about(HttpServletRequest request) throws Exception {
		ParamUtil util = ParamUtil.getIstance();
		if (util == null) {
			throw new RuntimeException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("systemCopyright", util.get(Parameter.SYSTEMCOPYRIGHT));
		result.put("systemDomain", util.get(Parameter.SYSTEMDOMAIN));
		result.put("remark", util.get(Parameter.REMARK));
		return new JsonResult(result);
	}

	/**
	 * 系统更新检查
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public JsonResult update(HttpServletRequest request, Integer osType) throws Exception {
		if (osType == null) {
			return new JsonResult(1, "缺少必要的参数:osType");
		}
		ParamUtil util = ParamUtil.getIstance();
		if (util == null) {
			throw new RuntimeException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (0 == osType) {
			result.put("androidAppForceUpdate", util.get(Parameter.ANDROIDAPPFORCEUPDATE));
			result.put("androidAppVersion", util.get(Parameter.ANDROIDAPPVERSION));
			result.put("androidAppUrl", util.get(Parameter.ANDROIDAPPURL));
			result.put("androidAppDetail", util.get(Parameter.ANDROIDAPPDETAIL));
			result.put("isAndroidPopup", util.get(Parameter.ISANDROIDPOPUP));
		} else if (1 == osType) {
			result.put("iosAppForceUpdate", util.get(Parameter.IOSAPPFORCEUPDATE));
			result.put("iosAppVersion", util.get(Parameter.IOSAPPVERSION));
			result.put("iosAppUrl", util.get(Parameter.IOSAPPURL));
			result.put("iosAppDetail", util.get(Parameter.IOSAPPDETAIL));
			result.put("isIOSPopup", util.get(Parameter.ISIOSPOPUP));
		} else {
			return new JsonResult(2, "未知的参数:osType");
		}
		return new JsonResult(result);
	}

	/**
	 * 获取系统设置
	 */
	@ResponseBody
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public JsonResult setting(HttpServletRequest request) throws Exception {
		ParamUtil util = ParamUtil.getIstance();
		if (util == null) {
			throw new RuntimeException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("donateSwich", util.get(Parameter.DONATESWICH));
		result.put("donateMax", util.get(Parameter.DONATEMAX));
		result.put("donateMin", util.get(Parameter.DONATEMIN));
		result.put("exchangeSwitch", util.get(Parameter.EXCHANGESWITCH));
		result.put("exchangeMax", util.get(Parameter.EXCHANGEMAX));
		result.put("exchangeMin", util.get(Parameter.EXCHANGEMIN));
		result.put("exchangePoundageScale", ToolUtil.parseDouble(util.get(Parameter.EXCHANGEPOUNDAGESCALE),0.05d));
		result.put("drawNumMax", util.get(Parameter.DRAWNUMMAX));
		result.put("drawNumMin", util.get(Parameter.DRAWNUMMIN));
		result.put("createStoreCondition", util.get(Parameter.CREATESTORECONDITION));
		result.put("storeStockMax", util.get(Parameter.STORESTOCKMAX));
		result.put("storeGoodsMax", util.get(Parameter.STOREGOODSMAX));
		result.put("inviteSwich", util.get(Parameter.INVITESWICH));
		result.put("shareTitle", util.get(Parameter.SHARETITLE));
		result.put("shareMessage", util.get(Parameter.SHAREMESSAGE));
		result.put("joinEp", util.get(Parameter.JOINEP));
		result.put("EPToDouScale", util.get(Parameter.EPTODOUSCALE));
		result.put("minEPConvertNum", util.get(Parameter.MINEPCONVERTNUM));
		result.put("maxEPConvertNum", util.get(Parameter.MAXEPCONVERTNUM));
		result.put("epSwitch", util.get(Parameter.EPSWITCH));
		result.put("minSignDouNum", util.get(Parameter.MINSIGNDOUNUM));
		result.put("servicePhone", util.get(Parameter.SERVICEPHONE));
		return new JsonResult(result);
	}

	/**
	 * 获取系统设置
	 */
	@ResponseBody
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public JsonResult setting(HttpServletRequest request, @RequestBody FeedBackVo vo) throws Exception {

		if (ToolUtil.isEmpty(vo.getTitle()) || ToolUtil.isEmpty(vo.getType()) || ToolUtil.isEmpty(vo.getDetail())){
			return  new JsonResult(-1,"反馈信息不能为空");
		}
		FeedBack feedBack=new FeedBack();
		feedBack.setUserId(vo.getUserId());
		feedBack.setType(vo.getType());
		feedBack.setDetail(vo.getDetail());
		feedBack.setTitle(vo.getTitle());
		feedBack.setType(vo.getType());
		if ((vo.getIcons() != null) && vo.getIcons().size()>0){
			feedBack.setIcons(StringUtils.join(vo.getIcons(),","));
		}
        if ((vo.getDelIcons() != null) && vo.getDelIcons().size()>0){
            if (ToolUtil.isEmpty(vo.getBucket())){
                return  new JsonResult(-1,"命名空间不能为空");
            }
            QiNiuUtil.batchDelFile(vo.getBucket(), (String[]) vo.getDelIcons().toArray(new String[vo.getDelIcons().size()]));
        }
        feedBackService.add(feedBack);
		return new JsonResult();
	}

}
