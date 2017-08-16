package com.yanbao.controller;

import com.yanbao.constant.GradeType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.WalletSubsidySignService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.vo.WalletSubsidySignVo;
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
 * @author zyc
 * @date 2017-06-07 11:44
 * 积分签到补贴
 *
 */

@Controller
@RequestMapping("/subsidySign")
public class WalletSubsidySignController extends BaseController {
	
	@Autowired
    WalletSubsidySignService walletSubsidySignService;
	
	
	/**
	 * 获取所有的数据(签到流水表)
	*/
   @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(WalletSubsidySignVo walletSubsidySignVo,Page page) {
	   
        int count = walletSubsidySignService.readSubsidyCount(walletSubsidySignVo);
        
        if (count==0){
            return fail("没有资源");
        }
        List<WalletSubsidySignVo> list = walletSubsidySignService.readList1(walletSubsidySignVo,page);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }
   
   /**
    * 签到流水表下载
    */
   
   
   @RequestMapping("/extWalletSubsidySign")
    public void extScoreRecords(WalletSubsidySignVo walletSubsidySignVo, Page page, HttpServletResponse response) {
        String fileName = "签到补贴流水统计" + RandomUtil.randomString(5) + ".csv";
        response.setContentType("application/CSV");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Pragma", "must-revalidate");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write("记录编号,交易订单,签到补贴金额,uid,姓名,手机号,补贴次数,补贴等级,日薪,补贴时间,状态\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
        	
            int count = walletSubsidySignService.readSubsidyCount(walletSubsidySignVo);
            if (count == 0) {
                return;
            }
            List<WalletSubsidySignVo> list = walletSubsidySignService.readList1(walletSubsidySignVo,page);
            String type = "";
           
            for (WalletSubsidySignVo vo : list) {
               //System.out.println(vo.getId()+ ",\t"+vo.getOrderNo() +"," +vo.getConfirmScore() + "," +vo.getDonateUid()+","+vo.getUserName()+","+vo.getPhone() +"," +vo.getSignNo() +","+GradeType.fromCode(vo.getGrade()).getMsg()+"," +DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG) +"," );
            	 outputStream.write((vo.getId() + ",\t" + vo.getOrderNo()+"," + vo.getConfirmScore() + "," +vo.getDonateUid()+","+vo.getUserName()+","
            	+vo.getPhone()+"," + vo.getSubsidyNo()+","+GradeType.fromCode(vo.getGrade()).getMsg()+","+vo.getScore()+","+DateTimeUtil.formatDate(vo.getSignTime(),DateTimeUtil.PATTERN_C) +","+(vo.getStatus()==1?"已补贴":"待补贴") +"\r\n").getBytes());		            
               
            }
            
            outputStream.flush();

           
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
   
   
	
	
	
	
	

}
