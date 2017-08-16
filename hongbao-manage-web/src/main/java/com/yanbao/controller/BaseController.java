package com.yanbao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.util.TokenUtil;

/**
 * Created by summer on 2016-12-08:10:35;
 */
public class BaseController {

    public static JsonResult success(int pageNo, int pageSize, int total, List list){
        PageResult pageResult=new PageResult(pageNo,pageSize,total,list);
        JsonResult jsonResult=new JsonResult(list);
        return jsonResult;
    }

    public static JsonResult success(){
        JsonResult jsonResult=new JsonResult();
        return  jsonResult;
    }


    public static JsonResult success(Object obj){
        JsonResult jsonResult=new JsonResult(obj);
        return jsonResult;
    }

    public static JsonResult fail(String message){
        JsonResult jsonResult=new JsonResult(1404,message);
        return jsonResult;
    }

    public static JsonResult fail(int code,String message){
        JsonResult jsonResult=new JsonResult(code,message);
        return jsonResult;
    }
    public static JsonResult fail(int code,String message,Object result){
        JsonResult jsonResult=new JsonResult(code,message,result);
        return jsonResult;
    }

    public static PageResult getPageResult(Page page,int count ,List list){
        PageResult pageResult=new PageResult(page.getPageNo(),page.getPageSize(),count,list);
        return pageResult;
    }

    public static Token getToken( HttpServletRequest request){
        String tokenBody=request.getHeader("token");
        Token token= (Token) TokenUtil.getTokenObject(tokenBody);
        return token;
    }

}
