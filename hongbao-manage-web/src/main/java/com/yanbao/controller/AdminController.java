package com.yanbao.controller;

import com.yanbao.constant.CacheKey;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.mall.model.Admin;
import com.mall.model.SysAdminMenu;
import com.mall.model.SysMenu;
import com.yanbao.redis.Strings;
import com.yanbao.service.AdminService;
import com.yanbao.service.SysAdminMenuService;
import com.yanbao.service.SysMenuService;
import com.yanbao.util.Md5Util;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2016-12-08:10:32;
 * <p>
 * 管理员信息管理
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Autowired
    AdminService adminService;
    @Value("${systemSalt}")
    String salt;

    @Autowired
    SysAdminMenuService sysAdminMenuService;
    @Autowired
    SysMenuService sysMenuService;


    List<SysMenu> roles;


    @RequestMapping("/login")
    @ResponseBody
    public JsonResult login(String password, String nickName, String picCode, HttpServletResponse response) {
        password = password == null ? "password" : password;
        Admin admin = new Admin();
        admin.setNickName(nickName);
        admin = adminService.readOne(admin);
        if (admin == null) {
            return fail("用户不存在");
        }
        if (admin.getFlag() > 3) {
            if (picCode == null) {
                return fail("请获取图片验证码");
            }
            String code = Strings.get(CacheKey.getPicCodeKey(nickName));
            if (code == null) {
                return fail(1400, "图片验证码错误");
            }
            if (!code.equals(picCode)) {
                return fail(1400, "图片验证码错误");
            }
        }
        if (!admin.getPassword().equals(Md5Util.md5(password + salt))) {
            admin.setFlag(admin.getFlag() + 1);
            adminService.updateById(admin.getId(), admin);
            return fail(1400, "密码错误", admin.getFlag());
        }

        Map map = new HashMap();
        //获取到当前用户的权限
        String role = Integer.toString(admin.getRole());

        //SysAdminMenu sysAdminMenu = new SysAdminMenu();
        // sysAdminMenu.setRole_id(role);


        List<SysAdminMenu> sysAdminMenuList = sysAdminMenuService.readAllData(role);
        // System.out.println(sysAdminMenuList.get(0).getMenu_id());

        SysMenu sysMenu = new SysMenu();
        List<SysMenu> sysMenuList = new ArrayList<SysMenu>();
        for (int i = 0; i < sysAdminMenuList.size(); i++) {
            String str = sysAdminMenuList.get(i).getMenuId();
            sysMenuList.add(sysMenuService.readById(str));
        }
        //int Num =  sysMenuList.size();


        String name = StringUtils.isEmpty(admin.getUserName()) ? "" : admin.getUserName();
        String token = TokenUtil.generateToken(admin.getId(), name, admin.getNickName());
        //token 放入缓存,有效时间十分钟
        Strings.setEx(CacheKey.getTokenKey(admin.getId()), 10 * 60, token);
        response.setHeader("token", token);
        map.put("token", token);
        admin.setPassword(null);
        admin.setSalt(null);
        admin.setStatus(null);
        map.put("admin", admin);
        //map.put("role", sysMenuList);
        roles = sysMenuList;

        admin.setFlag(0);
        adminService.updateById(admin.getId(), admin);
        return success(map);
    }

    @RequestMapping("/roles")
    @ResponseBody
    public JsonResult roles() {

        return success(roles);
    }


    @RequestMapping("/logOut")
    @ResponseBody
    public JsonResult logOut(String id) {
        Admin admin = adminService.readById(id);
        if (admin == null) {
            return fail("用户不存在");
        }
        Strings.del(CacheKey.getTokenKey(id));
        return success();
    }

    @RequestMapping("/modifyPassword")
    @ResponseBody
    public JsonResult modifyPassword(String password, String passwordNew, HttpServletRequest request) {
        Token token = getToken(request);
        Admin admin = adminService.readById(token.getId());
        if (admin.getPassword().equals(Md5Util.md5(password + salt))) {
            admin.setPassword(Md5Util.md5(passwordNew + salt));
            adminService.updateById(admin.getId(), admin);
            return success();
        }
        return fail("密码错误");
    }
    
    
    /**
     * 修改操作密码 zyc 2017-07-06
     * @param payPassword
     * @param payPasswordNew
     * @param request
     * @return
     */
    @RequestMapping("/modifypayPassword")
    @ResponseBody
    public JsonResult modifypayPassword(String payPassword, String payPasswordNew, HttpServletRequest request) {
        Token token = getToken(request);
        Admin admin = adminService.readById(token.getId());
        if (admin.getPayPassWord().equals(Md5Util.md5(payPassword + salt))) {
            //admin.setPassword(Md5Util.md5(passwordNew + salt));
            admin.setPayPassWord(Md5Util.md5(payPasswordNew + salt));
        	adminService.updateById(admin.getId(), admin);
            return success();
        }
        return fail("操作密码错误");
    }
    
    
    

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult add(Admin admin, HttpServletRequest request) {
        if (!checkPermission(request)) {
            return fail(1402, "没有权限");
        }
        Admin admin1 = adminService.getByNickName(admin.getNickName());
        if (admin1 != null) {
            return fail(1400, "用户已存在");
        }
        admin.setId(UUIDUtil.getUUID());
        admin.setPassword(Md5Util.md5(admin.getPassword() + salt));
        admin.setPayPassWord(Md5Util.md5(admin.getPayPassWord()+ salt));
        adminService.create(admin);
        return success("创建成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(String userId, HttpServletRequest request) {
        if (!checkPermission(request)) {
            return fail(1402, "没有权限");
        }
        Admin admin = adminService.readById(userId);
        if (admin == null) {
            return fail("用户不存在");
        }
        adminService.deleteById(userId);
        return success("删除成功");

    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult update(Admin admin, HttpServletRequest request) {
        if (!checkPermission(request)) {
            return fail(1402, "没有权限");
        }
        adminService.updateById(admin.getId(), admin);
        return success("修改成功");
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(Admin admin, Page page) {
        List<Admin> admins = adminService.readAll(admin);
        page.setPageSize(admins.size());
        return success(getPageResult(page, admins.size(), admins));
    }

    public boolean checkPermission(HttpServletRequest request) {
        Token token = getToken(request);
        Admin admin = adminService.readById(token.getId());
        if (!admin.getNickName().equals("admin")) {
            return false;
        }
        return true;
    }

}
