package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.mall.model.Bank;
import com.yanbao.service.BankService;

/**
 * Created by summer on 2016-12-14:18:17;
 */
@Controller
@RequestMapping("/bank")
public class BankContrller extends BaseController {

    @Autowired
    BankService bankService;

    @ResponseBody
    @RequestMapping("/list")
    public JsonResult getbankList(){
        List<Bank> list=bankService.readAll(new Bank());
        return success(list);
    }

}
