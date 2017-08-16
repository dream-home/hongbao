package com.yanbao.controller;

import com.mall.model.AgentInfo;
import com.mall.model.AgentStaff;
import com.mall.model.City;
import com.mall.model.Image;
import com.yanbao.constant.AgentType;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 代理相关
 */
@Controller
@RequestMapping("/agentInfo")
public class AgentController extends BaseController{

    @Autowired 
    private AgentInfoService agentInfoService;
    @Autowired
    private HbAgentStaffService agentStaffService;
    @Autowired
    private HbImgageService imgageService;
    @Autowired
    private InAgentStaticsService inAgentStaticsService;
    @Autowired
    private HbAgentStaffService hbAgentStaffService;
    @Autowired
    private SysCityService sysCityService;

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);
    //当前类权限id
    private static String classPermissionId = "2";

    /**
     * h5看代理商资料:身份证打点隐藏
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public JsonResult getInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        AgentInfoVo vo = new AgentInfoVo();
        AgentInfo agentInfo = null;
        if (token.getLoginType() == 1) {
            agentInfo = agentInfoService.getById(token.getId());
            BeanUtils.copyProperties(vo, agentInfo);
        } else if (token.getLoginType() == 2) {
            //AgentStaff agentStaff = agentStaffService.getPoByPk(token.getId());
            agentInfo = getAgent(token);
            BeanUtils.copyProperties(vo, agentInfo);
        }
        if (null == agentInfo) {
            return new JsonResult(1, "代理信息错误");
        }
        //获取代理相关图片
        List<String> cradids = null;
        List<String> licenseids = null;
        List<Image> idcardImg = null;
        List<Image> licensesImg = null;
        if (null != agentInfo.getCardIconId()) {
            cradids = new ArrayList<>();
            String idcards = agentInfo.getCardIconId();
            String[] arrid = idcards.split(",");
            for (String string : arrid) {
                cradids.add(string);
            }
            idcardImg = imgageService.readByIds(cradids);
        }
        if (null != agentInfo.getLicenseIconId()) {
            licenseids = new ArrayList<String>();
            String license = agentInfo.getLicenseIconId();
            String[] arrlic = license.split(",");
            for (String string : arrlic) {
                licenseids.add(string);
            }
            licensesImg = imgageService.readByIds(licenseids);
        }

        //代理相关图片
        if (idcardImg != null && idcardImg.size() > 0) {
            vo.setIdCard(idcardImg);
        }
        if (licensesImg != null && licensesImg.size() > 0) {
            List<Image> licensesImgs = new ArrayList<>();
            for (Image image : licensesImg) {
                if (image.getPath().indexOf("http") != -1) {
                    licensesImgs.add(image);
                }
            }
            vo.setLicense(licensesImgs);
        }
        //身份证打点隐藏
        if (!org.apache.commons.lang.StringUtils.isEmpty(vo.getIDCard())) {
            String regex = "(\\w{3})(\\w+)(\\w{3})";
            vo.setIDCard(vo.getIDCard().replaceAll(regex, "$1****$3"));
        }

        if (!ToolUtil.isEmpty(vo.getAgentAreaId())) {
            StaticVariable.setAll(sysCityService);
            City city = StaticVariable.ALL_CITY_MAP.get(vo.getAgentAreaId());
            Map<String, Object> maps = StaticVariable.getCitySetParent(city);
            List<City> cityList = (List<City>) maps.get("LIST");
            String priovinceName = "";
            String cityName = "";
            String countryName = "";
            for (City city1 : cityList) {
                if (city1.getLevel() == 1) {
                    priovinceName = city1.getName();
                }
                if (city1.getLevel() == 2) {
                    cityName = city1.getName();
                }
                if (city1.getLevel() == 3) {
                    countryName = city1.getName();
                }
            }
            vo.setAgentAreaId(priovinceName + cityName + countryName);
        }

        return new JsonResult(vo);
    }

    /**
     * 手机号登录
     */
    @ResponseBody
    @RequestMapping(value = "/login/loginIn", method = RequestMethod.POST)
    public JsonResult loginByUserName(HttpServletRequest request, @RequestBody AgentLoginVo vo) throws Exception {

        if (org.springframework.util.StringUtils.isEmpty(vo.getLoginName())) {
            return new JsonResult(-1, "登录用户名不能为空");
        }

        //处理图片验证码的值
        if (vo.getErrorCount() > 3) {
            if (org.springframework.util.StringUtils.isEmpty(vo.getKey())) {
                return new JsonResult(-1, "key不能为空");
            }
            String picCode2 = Strings.get(RedisKey.FORGET_LOGIN_PIC_CODE.getKey() + vo.getKey());
            if (org.springframework.util.StringUtils.isEmpty(picCode2) || !vo.getPicCode().equalsIgnoreCase(picCode2)) {
                return new JsonResult(-1, "图片验证码不正确或已过期");
            }
            //int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
            //SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.COMMON, smsCode + "");
            ///Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
            //删除图片验证码的key值
            Strings.del(RedisKey.FORGET_LOGIN_PIC_CODE.getKey() + vo.getKey());
        }

        AgentInfo condition = new AgentInfo();
        AgentStaff staff = new AgentStaff();

        //判断是否用账户登录
        //Boolean flag = judgeContainsStr(vo.getLoginName());

//        if(flag){
        condition.setLoginName(vo.getLoginName());
        condition.setStatus(1);
        staff.setLoginName(vo.getLoginName());
        staff.setStatus(1);

//        }
 

/*        else if (vo.getLoginName().length() < 11 ) {
            condition.setUid(Integer.valueOf(vo.getLoginName()));
            staff.setUid(Integer.valueOf(vo.getLoginName()));
        } else{
            condition.setPhone(vo.getLoginName());
            staff.setPhone(vo.getLoginName());
        }*/

        LoginReturnVo loginReturnVo = agentInfoService.AgentLogin(condition, vo);

        if (loginReturnVo.getErrorCode().intValue() == 10001) {
            //代理登录失败，尝试员工登录
            loginReturnVo = agentStaffService.staffLogin(staff, vo);
        }

        if (loginReturnVo.getErrorCode().intValue() != 200) {
            return new JsonResult(loginReturnVo.getErrorCode(), loginReturnVo.getErrorMessage(), loginReturnVo);
        }

        //判断是代理登录还是员工登录
        AgentLoginVo u = new AgentLoginVo();
        if (loginReturnVo.getAgentInfo() != null) {
            BeanUtils.copyProperties(u, loginReturnVo.getAgentInfo());
            u.setAgentType(1);
        } else if (loginReturnVo.getAgentStaff() != null) {
            AgentInfo agent = agentInfoService.readById(loginReturnVo.getAgentStaff().getAgentId());
            if (agent == null) {
                return new JsonResult(-1, "代理已被禁用，员工不支持登录");
            }
            BeanUtils.copyProperties(u, agent);
            BeanUtils.copyProperties(u, loginReturnVo.getAgentStaff());
            u.setAgentType(2);
            //获取权限
            String permissions = loginReturnVo.getAgentStaff().getMenuPermissions();
            List<Integer> plist = new ArrayList<>();
            if (permissions.contains(",")) {
                String[] pes = permissions.split(",");
                int plength = pes.length;
                for (int i = 0; i < plength; i++) {
                    plist.add(Integer.parseInt(pes[i]));
                }
            }
            u.setMenuPermissionsList(plist);
        }
        u.setToken(loginReturnVo.getToken());

        System.out.println("token:" + u.getToken());

        return new JsonResult(u);
    }

    public static boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    //将token放入redis
    private void loginRedis(AgentInfo agentInfo, String token) {

        String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + agentInfo.getId());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + agentInfo.getId(), RedisKey.TOKEN_API.getSeconds(), token);

        /*String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + user.getId());
        if (StringUtils.isEmpty(redisToken) || StringUtils.isEmpty(user.getStoreId())) {
            Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
            return;
        }
        if (!StringUtils.isEmpty(redisToken) && !StringUtils.isEmpty(user.getStoreId())) {
            SortSet.zadd(RedisKey.TOKEN_API_STORE.getKey() + user.getId(), System.currentTimeMillis(), token);
            Sets.expireSetSecond(RedisKey.TOKEN_API_STORE.getKey() + user.getId(), RedisKey.TOKEN_API_STORE.getSeconds());
        }*/
    }

    /**
     * 忘记登录密码
     */
    @ResponseBody
    @RequestMapping(value = "/forget/loginPassword", method = RequestMethod.POST)
    public JsonResult forgetLoginPassword(HttpServletRequest request, @RequestBody loginPasswordVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(3, "短信验证码不正确或已失效");
        }
        AgentInfo condition = new AgentInfo();
        condition.setPhone(vo.getPhone());
        AgentInfo agentInfo = agentInfoService.getByCondition(condition);
        //定义一个变量保存salt
        String salt = "";
        AgentStaff agentStaff = new AgentStaff();
        if (agentInfo == null) {
            agentStaff.setPhone(vo.getPhone());
            agentStaff = agentStaffService.getByCondition(agentStaff);
            salt = agentStaff.getSalt();
        } else {
            salt = agentInfo.getSalt();
        }

        if(agentInfo == null && agentStaff.getId() == null){
            return new JsonResult(-1, "该手机号不存在");
        }
        if (StringUtils.isBlank(vo.getNewLoginPass())) {
            return new JsonResult(1, "请输入新密码");
        }
        boolean b = PasswordUtil.checkPass(vo.getNewLoginPass());
        if (!b) {
            return new JsonResult(6, "密码不符合规范");
        }
        if (StringUtils.isBlank(vo.getNewLoginPassConfirm())) {
            return new JsonResult(3, "请输入确认新密码");
        }
        if (!vo.getNewLoginPass().equals(vo.getNewLoginPassConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }

        // 更新用户登录密码
        if (agentInfo != null) {
            AgentInfo model = new AgentInfo();
            model.setPassword(Md5Util.MD5Encode(vo.getNewLoginPass(), salt));
            model.setPayPassWord(Md5Util.MD5Encode(vo.getNewLoginPass(), salt));
            agentInfoService.update(agentInfo.getId(), model);
        } else if (agentStaff != null) {
            AgentStaff staffModel = new AgentStaff();
            staffModel.setPassword(Md5Util.MD5Encode(vo.getNewLoginPass(), salt));
            staffModel.setPayPassWord(Md5Util.MD5Encode(vo.getNewLoginPass(), salt));
            agentStaffService.updateById(agentStaff.getId(), staffModel);
        }

        return new JsonResult();
    }

    /**
     * 设置/修改登录密码
     */
    @ResponseBody
    @RequestMapping(value = "/change/loginPwd", method = RequestMethod.POST)
    public JsonResult changePayPwd(HttpServletRequest request, @RequestBody LoginPwdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        AgentInfo agentInfo = agentInfoService.getById(token.getId());
        AgentStaff agentStaff = null;
        //定义一个变量保存密码
        String loginPassWord = "";
        String salt = "";
        if (agentInfo == null) {
            //如果代理不存在的话，就进行员工登录
            agentStaff = agentStaffService.readById(token.getId());
            loginPassWord = agentStaff.getPassword();
            salt = agentStaff.getSalt();
        } else {
            salt = agentInfo.getSalt();
            loginPassWord = agentInfo.getPassword();
        }

        if (agentInfo == null && agentStaff == null) {
            return new JsonResult(-1, "当前用户已失效");
        }

        if (StringUtils.isNotBlank(loginPassWord)) {
            if (StringUtils.isBlank(vo.getOldLoginPwd())) {
                return new JsonResult(1, "请输入原登录密码");
            }

            if (!loginPassWord.equals(Md5Util.MD5Encode(vo.getOldLoginPwd(), salt))) {
                return new JsonResult(7, "原登录密码错误");
            }
        }
        if (StringUtils.isBlank(vo.getNewLoginPwd())) {
            return new JsonResult(3, "请输入新登录密码");
        }
        if (vo.getOldLoginPwd().equals(vo.getNewLoginPwd())) {
            return new JsonResult(4, "新旧登录密码不能相同");
        }
        if (!vo.getNewLoginPwd().equals(vo.getNewLoginPwdConfirm())) {
            return new JsonResult(5, "两次登录密码不一致");
        }
        boolean b = PasswordUtil.checkPass(vo.getNewLoginPwd());
        if (!b) {
            return new JsonResult(6, "密码不符合规范");
        }
        // 更新用户登录密码
        if (agentInfo != null) {
            AgentInfo model = new AgentInfo();
            model.setPassword(Md5Util.MD5Encode(vo.getNewLoginPwd(), salt));
            model.setPayPassWord(Md5Util.MD5Encode(vo.getNewLoginPwd(), salt));
            agentInfoService.update(token.getId(), model);
        } else if (agentStaff != null) {
            AgentStaff staffModel = new AgentStaff();
            staffModel.setPassword(Md5Util.MD5Encode(vo.getNewLoginPwd(), salt));
            staffModel.setPayPassWord(Md5Util.MD5Encode(vo.getNewLoginPwd(), salt));
            agentStaffService.updateById(token.getId(), staffModel);
        }
        return new JsonResult();
    }

    /**
     * 注销当前用户
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult logingOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //获取当前用户
        AgentInfo agentInfo = agentInfoService.getById(token.getId());
        AgentStaff agentStaff = null;
        if (agentInfo == null) {
            agentStaff = agentStaffService.readById(token.getId());
        }
        if (agentInfo == null && agentStaff == null) {
            return new JsonResult(-1, "当前用户不存在");
        }
        //从redis中获取当前登录用户的token
        //String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + token.getId());

        //从redis中删除token
        Strings.del(RedisKey.TOKEN_API.getKey() + token.getId());
        return new JsonResult();
    }
    /**
     * 获取员工权限
     */
    @ResponseBody
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public JsonResult getPermission(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        List<Integer> plist = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (token.getLoginType() == AgentType.STAFF_TYPE.getCode().intValue()) {
            AgentStaff as = hbAgentStaffService.readById(token.getId());
            if (as.getMenuPermissions().contains(",")) {
                String[] pes = as.getMenuPermissions().split(",");
                int plength = pes.length;
                for (int i = 0; i < plength; i++) {
                    plist.add(Integer.parseInt(pes[i]));
                }
            } else if (!"".equals(as.getMenuPermissions())) {
                //System.out.print("参数为:"+as.getMenuPermissions());
                plist.add(Integer.parseInt(as.getMenuPermissions()));
            }
            resultMap.put("IsStaff", true);
            resultMap.put("Permission", plist);
        } else {
            resultMap.put("IsStaff", false);
            resultMap.put("Permission", "");
        }
        return new JsonResult(resultMap);
    }

    /**
     * 测试代理业绩统计
     */
    @ResponseBody
    @RequestMapping(value = "/testInAgentStatistics", method = RequestMethod.GET)
    public JsonResult testInAgentStatistics(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        inAgentStaticsService.inAgentStatistics(sdf.parse(startTime), sdf.parse(endTime));
        return new JsonResult();
    }

    public static void main(String[] args) {

        System.out.print(Md5Util.MD5Encode("123456", "www.yanbao.com"));
    }


}
