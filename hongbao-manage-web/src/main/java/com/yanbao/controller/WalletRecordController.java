package com.yanbao.controller;

import com.yanbao.constant.RecordType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.WalletRecordService;
import com.yanbao.vo.RecordVo;
import com.yanbao.vo.WalletRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by summer on 2016-12-19:17:14;
 */
@Controller
@RequestMapping("/logs")
public class WalletRecordController extends BaseController{

    @Autowired
    WalletRecordService walletRecordService;


    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(RecordVo recordVo, Page page){
        int count=walletRecordService.getRecordVoCount(recordVo);
        if (count==0){
            return fail("没有资源");
        }
        List<RecordVo> recordVos=walletRecordService.getRecordVoList(recordVo,page.getStartRow(),page.getPageSize());
        for(RecordVo record1Vos:recordVos){
        	record1Vos.setRecordTypeName(RecordType.fromCode(record1Vos.getRecordType()).getMsg());
        	
        }
        
        
        
        PageResult pageResult=new PageResult(page.getPageNo(),page.getPageSize(),count,recordVos);
        return success(pageResult);
    }
    
    
    
    /*系统获取费用*/
    @RequestMapping("/systemExpenses")
    @ResponseBody
    public JsonResult systemExpenses(WalletRecordVo walletRecordVo, Page page){
    	
    	int count=walletRecordService.getRecordVoCount1(walletRecordVo);
        if (count==0){
            return fail("没有资源");
        }
    	
    	
        List<WalletRecordVo> list =  walletRecordService.getRecordVoList1(walletRecordVo, page.getStartRow(), page.getPageSize());
       // List<WalletRecord> list =  walletRecordService.readList(walletRecord, page.getPageNo(),page.getPageSize(), count);
    	//List<WalletRecord> list =  walletRecordService.readAll(walletRecord);
    	PageResult pageResult=new PageResult(page.getPageNo(),page.getPageSize(),count,list);
    	
    	return success(pageResult);
    	
    	
    }

    /*系统获取费用*/
    @RequestMapping("/getPaymentDetail")
    @ResponseBody
    public JsonResult getPaymentDetail(String orderNo){


        List<WalletRecordVo> list = walletRecordService.getPaymentDetail(orderNo);
        for(WalletRecordVo recordVo : list){

            recordVo.setTypeName(RecordType.fromCode(recordVo.getRecordType()).getMsg());

        }



        return success(list);
    }
    


}
