package com.yanbao.controller;

import com.yanbao.constant.GradeType;
import com.yanbao.constant.SignType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.WalletSign;
import com.yanbao.service.WalletSignService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.vo.WalletSignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 
 * @author zyc  2016-06-02 15:33
 * 积分签到赠送
 *
 */


@Controller
@RequestMapping("/sign")
public class WalletSignController extends BaseController {
	@Autowired
    WalletSignService walletSignService;
	
	
	
		/**
		 * 获取所有的数据(签到流水表)
		*/
	   @ResponseBody
	    @RequestMapping("/list")
	    public JsonResult list(WalletSignVo walletSignVo,Page page) {
		   walletSignVo.setStatus(1);
	        int count = walletSignService.readSubsidyCount(walletSignVo);
	        
	        if (count==0){
	            return fail("没有资源");
	        }
	        List<WalletSignVo> list = walletSignService.readList1(walletSignVo,page);
	        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
	        return success(pageResult);
	    }
	   
	   /**
	    * 签到流水表下载
	    */
	   
	   
	   @RequestMapping("/extWalletSign")
	    public void extScoreRecords(WalletSignVo walletSignVo, Page page, HttpServletResponse response) {
	        String fileName = "签到流水统计" + RandomUtil.randomString(5) + ".csv";
	        response.setContentType("application/CSV");
	        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
	        response.addHeader("Cache-Control", "must-revalidate");
	        response.addHeader("Pragma", "must-revalidate");
	        OutputStream outputStream = null;
	        try {
	            outputStream = response.getOutputStream();
	            outputStream.write("记录编号,交易订单,签到领取金额,uid,姓名,手机号,签到次数,签到等级,签到类型,时间\r\n".getBytes());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            int count = walletSignService.readSubsidyCount(walletSignVo);
	            if (count == 0) {
	                return;
	            }
	            List<WalletSignVo> list = walletSignService.readList1(walletSignVo,page);
	            String type = "";
	           
	            for (WalletSignVo vo : list) {
	               //System.out.println(vo.getId()+ ",\t"+vo.getOrderNo() +"," +vo.getConfirmScore() + "," +vo.getDonateUid()+","+vo.getUserName()+","+vo.getPhone() +"," +vo.getSignNo() +","+GradeType.fromCode(vo.getGrade()).getMsg()+"," +DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG) +"," );
	            	 outputStream.write((vo.getId() + ",\t" + vo.getOrderNo()+"," + vo.getConfirmScore() + "," +vo.getDonateUid()+","+(null==vo.getUserName()?"":vo.getUserName())+","
	            	+(null==vo.getPhone()?"":vo.getPhone())+"," + (null==vo.getSignNo()?"":vo.getSignNo())+","+GradeType.fromCode(vo.getGrade()).getMsg()+","+SignType.fromCode(vo.getType()).getMsg()+"," +DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_C) +"," +"\r\n").getBytes());		            
	               
	            }
	            
	            outputStream.flush();

	           
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    }
	   
	   
	   
	   
	   
	   
	   /**
		 * 获取所有的数据(签到流水补贴表)（已弃）
		*/
	   @ResponseBody
	    @RequestMapping("/subsidyList")
	    public JsonResult subsidyList(WalletSignVo walletSignVo,Page page) {
	        WalletSign walletSign1 = new WalletSign();
	        walletSign1.setStatus(1);
	        int count = walletSignService.readCount(walletSign1);
	        if (count==0){
	            return fail("没有资源");
	        }
	        
	        List<WalletSignVo> list = walletSignService.readSubsidyList(walletSignVo,page);
	        
	        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
	        return success(pageResult);
	    }
	   
	   

	
	
	

}
