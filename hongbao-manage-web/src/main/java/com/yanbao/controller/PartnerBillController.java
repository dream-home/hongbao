package com.yanbao.controller;


import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.model.PartnerBill;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.PartnerBillService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;

/**
 * @author Pay - 1091945691@qq.com
 */
@Controller
@RequestMapping("partnerBill")
public class PartnerBillController extends BaseController{
	/**初始化日志对象*/
	private final static Logger log= Logger.getLogger(PartnerBillController.class);
	/**注入合伙人业绩表Service类*/
	@Autowired
	private PartnerBillService partnerBillService; 
	
	@RequestMapping("/list")
	@ResponseBody
	public JsonResult list(Page page,String year,String month,String search){ 
		PartnerBill pb=new PartnerBill();
		if(null!=year&&!"".equals(year)&&null!=month&&!"".equals(month)){
			pb.setBillday(year+month);
		}
		if(null!=search&&!"".equals(search)){
			pb.setPhone(search);
			pb.setUserName(search);
			if(StringUtils.isNumeric(search)&&search.length()<8){
				pb.setUid(Integer.parseInt(search));
			}
		}
		try {
			List<PartnerBill> count=partnerBillService.getBySearch(null, pb);
			List<PartnerBill> list= new ArrayList<PartnerBill>();  
			if(count.size()>0){
				list=partnerBillService.getBySearch(page, pb);
			}
			return  success(new PageResult(page.getPageNo(), page.getPageSize(), count.size(), list));
		} catch (Exception e) {
			e.printStackTrace();
			return  fail("系统错误!");
		}
	}
	
	@RequestMapping("/extPartnerBill")
    public void extPartnerBill(String year,String month,HttpServletResponse response) {
		try {
			PartnerBill pb=new PartnerBill();
			if(null!=year&&!"".equals(year)&&null!=month&&!"".equals(month)){
				pb.setBillday(year+month);
			}
			String fileName = new String("PartnerBill.csv".getBytes("GB2312"), "ISO_8859_1");
		    response.setContentType("application/CSV");
		    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		    response.addHeader("Cache-Control", "must-revalidate");
		    response.addHeader("Pragma", "must-revalidate");
			List<PartnerBill> list=partnerBillService.getBySearch(null, pb);
			OutputStream outputStream = response.getOutputStream();
            outputStream.write("uid,\t姓名,\t手机号,\t业绩统计,\t业绩提成,\tEP业绩统计,\tEP业绩提成,\t业绩合计,\t日期,\t状态,\t结算期\r\n".getBytes());
            DecimalFormat df = new DecimalFormat("######0.00");   
            for (PartnerBill pbBill : list) {
            	outputStream.write((pbBill.getUid() + ",\t" + pbBill.getUserName() + ",\t"+pbBill.getPhone()+",\t" +pbBill.getBalance()+",\t"+df.format(pbBill.getBalance()*pbBill.getBalanceScale())
                    	+",\t"+pbBill.getEP()+",\t"+df.format(pbBill.getEP()*pbBill.getEPScale())+",\t"+pbBill.getTotalAmount()+",\t"+DateTimeUtil.formatDate(pbBill.getCreateTime(),DateTimeUtil.PATTERN_C)+",\t"+(pbBill.getStatus()==1?"已发放":pbBill.getStatus()==2?"已取消":"未满足结算日期")+",\t"+pbBill.getBillday()
                        +"\r\n").getBytes());
                outputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}