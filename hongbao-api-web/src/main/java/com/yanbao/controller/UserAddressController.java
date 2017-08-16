package com.yanbao.controller;

import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.mall.model.User;
import com.mall.model.UserAddress;
import com.yanbao.service.UserAddressService;
import com.yanbao.service.UserService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/address")
public class UserAddressController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserAddressService userAddressService;

    /*
     * 根据token获取用户收货地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/getUserAddress", method = RequestMethod.GET)
    public JsonResult getUserAddress(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        List<UserAddress> list = userAddressService.getList(token.getId());
        return new JsonResult(list);
    }

    /*
     * 新增用户收货地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/addUserAddress", method = RequestMethod.POST)
    public JsonResult addUserAddress(HttpServletRequest request, @RequestBody UserAddress vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (null == vo.getReceiveName() || StringUtils.isEmpty(vo.getReceiveName())) {
            return new JsonResult(4, "请输入收货姓名");
        }
        if (null == vo.getReceivePhone() || StringUtils.isEmpty(vo.getReceivePhone())) {
            return new JsonResult(5, "请输入收货人联系号码");
        }
        if (null == vo.getProvince() || StringUtils.isEmpty(vo.getProvince()) ||
                null == vo.getCity() || StringUtils.isEmpty(vo.getCity()) ||
                null == vo.getCounty() || StringUtils.isEmpty(vo.getCounty()) ||
                null == vo.getAddr() || StringUtils.isEmpty(vo.getAddr())) {
            return new JsonResult(6, "请选择收货地址");
        }
        vo.setId(null);
        vo.setUserId(token.getId());
        userAddressService.handleUserAddress(vo);
        return new JsonResult();
    }

    /*
     * 修改用户收货地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/updUserAddress", method = RequestMethod.POST)
    public JsonResult updUserAddress(HttpServletRequest request, @RequestBody UserAddress vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (null == vo.getId() || StringUtils.isEmpty(vo.getId())) {
            return new JsonResult(4, "请选择要修改的收货地址");
        }
        if (null == vo.getReceiveName() || StringUtils.isEmpty(vo.getReceiveName())) {
            return new JsonResult(4, "请输入收货姓名");
        }
        if (null == vo.getReceivePhone() || StringUtils.isEmpty(vo.getReceivePhone())) {
            return new JsonResult(5, "请输入收货人联系号码");
        }
        if (null == vo.getProvince() || StringUtils.isEmpty(vo.getProvince()) ||
                null == vo.getCity() || StringUtils.isEmpty(vo.getCity()) ||
                null == vo.getCounty() || StringUtils.isEmpty(vo.getCounty()) ||
                null == vo.getAddr() || StringUtils.isEmpty(vo.getAddr())) {
            return new JsonResult(6, "请选择收货地址");
        }
        vo.setUserId(token.getId());
        userAddressService.handleUserAddress(vo);
        return new JsonResult();
    }

    /*
     * 删除用户收货地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/delUserAddress", method = RequestMethod.POST)
    public JsonResult delUserAddress(HttpServletRequest request, @RequestBody UserAddress vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (StringUtils.isEmpty(vo.getId())) {
            return new JsonResult(4, "请选择要删除的收货地址");
        }
        UserAddress model = new UserAddress();
        model.setId(vo.getId());
        model.setStatus(StatusType.FALSE.getCode());
        userAddressService.update(model.getId(), model);
        return new JsonResult();
    }

    /*
     * 用户设置默认收货地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/setDefaultAddress", method = RequestMethod.POST)
    public JsonResult setDefaultAddress(HttpServletRequest request, @RequestBody UserAddress vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() == StatusType.FALSE.getCode()) {
            return new JsonResult(3, "您的帐号已被禁用");
        }
        if (StringUtils.isEmpty(vo.getId())) {
            return new JsonResult(4, "请选择要设置默认的收货地址");
        }
        userAddressService.updateDefaultAddr(token.getId(), vo.getId());
        return new JsonResult();
    }

    public static void main(String[] args) {
        System.out.println(Md5Util.MD5Encode("as12345","20170314"));
    }
}
