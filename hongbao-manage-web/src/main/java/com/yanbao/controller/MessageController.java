package com.yanbao.controller;

import com.mall.model.FeedBack;
import com.mall.model.Message;
import com.mall.model.User;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.FeedBackService;
import com.yanbao.service.ImgageService;
import com.yanbao.service.MessageService;
import com.yanbao.service.UserService;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by summer on 2016-12-09:12:09;
 * 系统消息 公告等信息
 */
@Controller
@RequestMapping("/notice")
public class MessageController extends BaseController {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    FeedBackService feedBackService;
    @Autowired
	ImgageService imgageService;

    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(Message message, Page page) {
        int count = messageService.readCount(message);
        List<Message> list = messageService.readList(message, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(pageResult);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(String id, Page page) {
        String[] ids = id.split(",");
        for (String a : ids) {
            messageService.deleteById(a);
        }
        return success();
    }

    @ResponseBody
    @RequestMapping("/create")
    public JsonResult issue(Message message, Integer uid, String phone) {
        if (uid != null || phone != null) {
            User user = new User();
            user.setUid(uid);
            user.setPhone(phone);
            message.setStatus(0);
            user = userService.readOne(user);
            if (user == null) {
                return fail("用户不存在");
            }
            message.setUserId(user.getId());
            message.setId(UUIDUtil.getUUID());
            messageService.create(message);
        } else {
            message.setStatus(0);
            message.setUserId("all");
            message.setId(UUIDUtil.getUUID());
            messageService.insertAll(message);
        }
        
        return success();
    }
    @ResponseBody
    @RequestMapping("/feedBackList")
    public JsonResult feedBackList(FeedBack feedBack,Page page){
    	int count = feedBackService.readCount(feedBack);
    	 
    	 List<FeedBack> list = feedBackService.readList(feedBack, page.getPageNo(), page.getPageSize(), count);
    	 PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
         return success(pageResult);
    }
    
    

}
