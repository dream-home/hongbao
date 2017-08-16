package com.yanbao.controller;

import com.yanbao.core.page.JsonResult;
import com.yanbao.service.IncomeStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/income")
public class IncomeStatisticsController {

	@Autowired
	private IncomeStatisticsService incomeStatistics;

	/**
	 * 收益统计
	 */
	@ResponseBody
	@RequestMapping(value = "/incomeStatistics", method = RequestMethod.GET)
	public JsonResult incomeStatistics(HttpServletRequest request,String Starttime,String Endtime) throws Exception {
		//获取上月时间
		/*Calendar startcalendar = Calendar.getInstance();
		startcalendar.add(Calendar.MONTH, -1);
		startcalendar.set(Calendar.DAY_OF_MONTH, 1);
		startcalendar.set(Calendar.HOUR_OF_DAY, 0);
		startcalendar.set(Calendar.MINUTE, 0);
		startcalendar.set(Calendar.SECOND, 0);
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.set(Calendar.DAY_OF_MONTH, 1); 
		endcalendar.set(Calendar.HOUR_OF_DAY, 0);
		endcalendar.set(Calendar.MINUTE, 0);
		endcalendar.set(Calendar.SECOND, 0);*/
		Date starttime=null; 
		Date endtime=null;	
		//统计方法
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			starttime=sdf.parse(Starttime);
			long endtimelong=sdf.parse(Endtime).getTime()+(1000*60*60*24);
			endtime=new Date(endtimelong);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1,"时间格式错误!");
		}
		return new JsonResult(incomeStatistics.IncomeStatistics(starttime,endtime));
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAllinfo", method = RequestMethod.GET)
	public JsonResult getAllinfo() throws Exception {
		return new JsonResult(incomeStatistics.getAllinfo());
	}
}
