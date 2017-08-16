package com.yanbao.controller;

import com.mall.model.AgentBillDetail;
import com.mall.model.City;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.service.AgentBillDetailService;
import com.yanbao.service.SysCityService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.vo.AgentBillVo;
import com.yanbao.vo.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

@Controller
@RequestMapping("/agentBillDetail")
public class AgentBillDetailController extends BaseController {

    @Autowired
    private AgentBillDetailService agentBillDetailService;
    @Autowired
    private SysCityService sysCityService;

    /**
     * 根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩记录
     * @param agentBillVo
     * @param page
     * @return
     */
    @RequestMapping("/getAgentBillDetail")
    @ResponseBody
    public JsonResult getAgentBillDetail(AgentBillVo agentBillVo, Page page) throws Exception{

        AgentBillDetail agentBillDetail = new AgentBillDetail();

        if(!ToolUtil.isEmpty(agentBillVo.getPhone())){
            agentBillDetail.setPhone(agentBillVo.getPhone());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentLevel())){
            agentBillDetail.setAgentLevel(agentBillVo.getAgentLevel());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentProvince())){
            agentBillDetail.setAgentProvince(agentBillVo.getAgentProvince());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCity())){
            agentBillDetail.setAgentCity(agentBillVo.getAgentCity());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getAgentCountry())){
            agentBillDetail.setAgentCountry(agentBillVo.getAgentCountry());
        }
        if(!ToolUtil.isEmpty(agentBillVo.getStatus())){
            agentBillDetail.setStatus(agentBillVo.getStatus());
        }

        //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩总数
        int count = agentBillDetailService.readAgentBillDetailCount(agentBillDetail,agentBillVo.getFromTime(),agentBillVo.getStopTime());

        if(count == 0){
            return fail("没有代理业绩结算记录");
        }

        //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
        List<AgentBillDetail> list =  agentBillDetailService.getAgentBillDetail(agentBillDetail,agentBillVo.getFromTime(),agentBillVo.getStopTime(),page);

        if(list.size() <= 0){
            return fail("没有代理业绩结算记录");
        }

        //设置地区参数
        City city = null;

        List<AgentBillDetail> resultList = new ArrayList<>();

        for (AgentBillDetail bill : list){
            city = new City();
            city.setCode(bill.getAgentAreaId());
            City city1= sysCityService.readOne(city);
            bill.setAgentAreaId(city1.getName());
            resultList.add(bill);
        }

        //返回列表
        return success(getPageResult(page, count, resultList));
    }
    
    
  //下载
   	@RequestMapping("/extAgentBillDetail")
      public void extScoreRecords(AgentBillVo agentBillVo, Page page, HttpServletResponse response) {
          String fileName = "代理结算记录统计" + RandomUtil.randomString(5) + ".csv";
          response.setContentType("application/CSV");
          response.setHeader("Content-disposition", "attachment;filename=" + fileName);
          response.addHeader("Cache-Control", "must-revalidate");
          response.addHeader("Pragma", "must-revalidate");
          OutputStream outputStream = null;
          
          AgentBillDetail agentBillDetail = new AgentBillDetail();

          if(!ToolUtil.isEmpty(agentBillVo.getPhone())){
              agentBillDetail.setPhone(agentBillVo.getPhone());
          }
          if(!ToolUtil.isEmpty(agentBillVo.getAgentLevel())){
              agentBillDetail.setAgentLevel(agentBillVo.getAgentLevel());
          }
          if(!ToolUtil.isEmpty(agentBillVo.getAgentProvince())){
              agentBillDetail.setAgentProvince(agentBillVo.getAgentProvince());
          }
          if(!ToolUtil.isEmpty(agentBillVo.getAgentCity())){
              agentBillDetail.setAgentCity(agentBillVo.getAgentCity());
          }
          if(!ToolUtil.isEmpty(agentBillVo.getAgentCountry())){
              agentBillDetail.setAgentCountry(agentBillVo.getAgentCountry());
          }
          if(!ToolUtil.isEmpty(agentBillVo.getStatus())){
              agentBillDetail.setStatus(agentBillVo.getStatus());
          }
          try {
              outputStream = response.getOutputStream();
              outputStream.write("id,结算单号,结算类型,收款公司名称,姓名,手机号,银行账户,银行,开户支行,代理区域,结算比例,结算金额,状态,结算日期\r\n".getBytes());
              
          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
        	  int count = agentBillDetailService.readAgentBillDetailCount(agentBillDetail,agentBillVo.getFromTime(),agentBillVo.getStopTime());
            /*  if (count == 0) {
                  return;
              }*/
              //设置地区参数
              City city = null;
              //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
              List<AgentBillDetail> list =  agentBillDetailService.getAgentBillDetail(agentBillDetail,agentBillVo.getFromTime(),agentBillVo.getStopTime(),page);
              List<AgentBillDetail> resultList = new ArrayList<>();

              for (AgentBillDetail bill : list){
                  city = new City();
                  city.setCode(bill.getAgentAreaId());
                  City city1= sysCityService.readOne(city);
                  bill.setAgentAreaId(city1.getName());
                  resultList.add(bill);
              }

              
              
             
              String type = "";
              for (AgentBillDetail vo : resultList) {
              	
              	String status = "";
              	
              	String type1 ="";
              	
        		
              	if(vo.getType() == "1"){
              		type1 = "已提现业绩结算";
              	}else if(vo.getType() =="2"){
              		type1 = "未ep业绩结算";
              	}else{
              		
              	}
              	
              	
              			
              	if(vo.getStatus() == 1){
              		status = "结算成功";
              	}else if(vo.getStatus() == 0){
              		status = "待付款";
              	}else{
              		
              	}
              	
              	
              	
              	outputStream.write((vo.getId() + ",\t" + vo.getOrderNo()+"," +type1 + "," +vo.getCompany()+","+vo.getUserName()
              	
              	+","+vo.getPhone()+","+vo.getBankCard()+","+vo.getBankType()+","+vo.getBankBranch()+","+vo.getAgentAreaId()+","+vo.getScale()+","+vo.getAmount()+","+status+","+DateTimeUtil.formatDate(vo.getUpdateTime(),DateTimeUtil.PATTERN_LONG)
                  +"\r\n").getBytes());
              	outputStream.flush();
              	
              	
              	
                  /*outputStream.write((vo.getId() + ",\t" + vo.getLoginName().toString() +"," + vo.getCompany() + "," +vo.getUserName()+","+vo.getPhone()
                          +"," +vo.getServicePhone()+","+vo.getEmail()+","+agentLevel+","+vo.getAgentAreaId()+","+vo.getAgentProvinceName()+","+vo.getAgentCityName()+","+vo.getAgentCountryName()+","+vo.getAddress()+","+DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG)+","+status+"\r\n").getBytes());
                  outputStream.flush();*/
              }

          } catch (Exception e) {
              System.out.println(e.getMessage());
          }
      }
    
    
    

}
