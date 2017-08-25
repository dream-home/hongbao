package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.taobao.api.internal.util.Base64;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.mall.model.User;
import com.yanbao.redis.Strings;
import com.yanbao.service.UserService;
import com.yanbao.util.*;
import com.yanbao.vo.DelImgVo;
import com.yanbao.vo.PhoneVo;
import com.yanbao.vo.UploadFileWithBase64Vo;
import com.yanbao.vo.UploadVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {

    private static int width = 230;// 定义图片的width
    private static int height = 60;// 定义图片的height
    private static int codeCount = 4;// 定义图片上显示验证码的个数
    private static int xx = 40;
    private static int fontHeight = 50;
    private static int codeY = 50;
    static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9'};

    private static String prefix = "data:image/jpeg;base64,";
    @Autowired
    private UserService userService;
    @Value("${buckey}")
    private String buckey;


    @ResponseBody
    @RequestMapping(value = "/picCode", method = RequestMethod.GET)
    public void getPicCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String picCode = null;

        response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Set-Cookie", "name=value; HttpOnly");// 设置HttpOnly属性,防止Xss攻击
        response.setDateHeader("Expire", 0);
        OutputStream outputStream = response.getOutputStream();
        picCode = generateCode(outputStream);
        Strings.setEx(RedisKey.PIC_CODE.getKey() + token.getId(), RedisKey.PIC_CODE.getSeconds(), picCode);
        outputStream.flush();
        outputStream.close();
    }

    @ResponseBody
    @RequestMapping(value = "/picBase64Code", method = RequestMethod.GET)
    public JsonResult getPicBase64Code(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String picCode = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picCode = generateCode(baos);
        Strings.setEx(RedisKey.PIC_CODE.getKey() + token.getId(), RedisKey.PIC_CODE.getSeconds(), picCode);
        String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
        baos.close();
        return new JsonResult(rstr);
    }

    @ResponseBody
    @RequestMapping(value = "/forgetLoginPic", method = RequestMethod.GET)
    public JsonResult getForgetLoginBase64PicCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String picCode = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picCode = generateCode(baos);
        String uuid = UUIDUtil.getUUID();
        Strings.setEx(RedisKey.FORGET_LOGIN_PIC_CODE.getKey() + uuid, RedisKey.FORGET_LOGIN_PIC_CODE.getSeconds(), picCode);
        String rstr = "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), false);
        baos.close();
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("picCode", rstr);
        resMap.put("key", uuid);
        return new JsonResult(resMap);
    }

    @ResponseBody
    @RequestMapping(value = "/forgetLoginSmsCode", method = RequestMethod.POST)
    public JsonResult forgetLoginSmsCode(HttpServletRequest request, @RequestBody PhoneVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (!PasswordUtil.isAllNumeric(vo.getPhone()) || vo.getPhone().length() != 11) {
            return new JsonResult(1, "请输入合法手机号");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp = userService.getByCondition(condition);
        if (temp == null) {
            return new JsonResult(-1, "该手机号尚未注册");
        }
        if (StringUtils.isBlank(vo.getPicCode())) {
            return new JsonResult(2, "请输入图片验证码");
        }
        if (org.springframework.util.StringUtils.isEmpty(vo.getKey())) {
            return new JsonResult(2, "key不能为空");
        }
        String picCode2 = Strings.get(RedisKey.FORGET_LOGIN_PIC_CODE.getKey() + vo.getKey());
        if (!vo.getPicCode().equalsIgnoreCase(picCode2)) {
            return new JsonResult(3, "图片验证码不正确或已过期");
        }
        int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
        SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.COMMON, smsCode + "");
        Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
        Strings.del(vo.getKey());
        return new JsonResult();
    }

    @ResponseBody
    @RequestMapping(value = "/smsCode", method = RequestMethod.POST)
    public JsonResult getSmsCode(HttpServletRequest request, @RequestBody PhoneVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (!PasswordUtil.isAllNumeric(vo.getPhone()) || vo.getPhone().length() != 11) {
            return new JsonResult(1, "请输入合法手机号");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp = userService.getByCondition(condition);
        if (temp != null && !temp.getId().equals(token.getId())) {
            return new JsonResult(4, "该手机号已注册");
        }
        if (StringUtils.isBlank(vo.getPicCode())) {
            return new JsonResult(2, "请输入图片验证码");
        }
        String picCode2 = Strings.get(RedisKey.PIC_CODE.getKey() + token.getId());
        if (!vo.getPicCode().equalsIgnoreCase(picCode2)) {
            return new JsonResult(3, "图片验证码不正确或已过期");
        }
        int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
        SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.BIND_PHONE, smsCode + "");
        Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
        Strings.del(RedisKey.PIC_CODE.getKey() + token.getId());
        return new JsonResult();
    }




    private static String generateCode(OutputStream outputStream) throws IOException {
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生100条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        ImageIO.write(buffImg, "jpeg", outputStream);
        String str = randomCode.toString();
        return str;
    }

    /***
     * 根据文件名删除上传图片
     */
    @RequestMapping(value = "/delupimg", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delUpImg(HttpServletRequest request, HttpServletResponse response, @RequestBody DelImgVo vo) {
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println(JSON.toJSONString(vo));
        System.out.println("+++++++++++++++++++++++++++++++++");
        Map<String, String> map = new HashMap<String, String>();
        JsonResult x = checkBuket(vo.getBucket());
        if (x != null) return x;
        if (CollectionUtils.isEmpty(vo.getIconList())) {
            x = new JsonResult(-1, "文件名为空，无法删除");
        }
        String[] keys = new String[vo.getIconList().size()];
        int i = 0;
        for (String key : vo.getIconList()) {
            keys[i] = key;
            i++;
        }
        try {
            QiNiuUtil.batchDelFile(vo.getBucket(), keys);
            return new JsonResult();
        } catch (Exception e) {
            throw  new RuntimeException("删除异常");
        } finally {

        }
    }

    /***
     * 获取上传文件的token
     */
    @RequestMapping("/uptoken")
    @ResponseBody
    public JsonResult upToken(HttpServletRequest request, HttpServletResponse response, String bucket) {
        Map<String, String> map = new HashMap<String, String>();
        JsonResult x = checkBuket(bucket);
        if (x != null) return x;
        map.put("domain", PropertiesUtil.getDomain(bucket));
        map.put("token", QiNiuUtil.getToken(bucket));
        return new JsonResult(map);
    }

    private JsonResult checkBuket(String bucket) {
        if (StringUtils.isEmpty(buckey)) {
            return new JsonResult(-1, "命名空间不能为空");
        }
        String[] b = buckey.split(",");
        List list = Arrays.asList(b);
        if (!list.contains(bucket)) {
            return new JsonResult(-1, "命名空间不合法");
        }
        try {
            PropertiesUtil.getDomain(bucket);
        } catch (Exception e) {
            return new JsonResult(-1, "获取空间域名错误");
        }
        return null;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFile(HttpServletRequest request, UploadVo vo) throws Exception {
        JsonResult result = checkBuket(vo.getBucket());
        if (result != null) {
            return result;
        }
        FileOutputStream fos = null;
        InputStream in = null;
        String fileUrl = null;
        List<Map> list = new ArrayList<>();
        try {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iterator = multiRequest.getFileNames();
                while (iterator.hasNext()) {
                    MultipartFile multipartFile = multiRequest.getFile(iterator.next().toString());
                    in = multipartFile.getInputStream();
                    Map map = QiNiuUtil.uploadStream(vo.getBucket(), null, in);
                    if (map != null) {
                        Map m = new HashMap();
                        m.put(map.get("key"), map.get("keyWithPrefix"));
                        list.add(m);
                    }
                }
                return new JsonResult(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return new JsonResult(-1, "");
    }


    @RequestMapping(value = "/uploadFileWithBase64", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFile(HttpServletRequest request, @RequestBody UploadFileWithBase64Vo vo) throws Exception {
        JsonResult result = checkBuket(vo.getBucket());
        if (result != null) {
            return result;
        }
        if (CollectionUtils.isEmpty(vo.getIcons())) {
            return new JsonResult(-1, "图片不能为空");
        }
        List<Map> list = null;
        try {
            List<String> delList = new ArrayList<>();
            list = new ArrayList<>();
            for (String icon : vo.getIcons()) {
                icon = icon.replaceFirst("data:image/jpeg;base64,", "");
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] bytes = decoder.decodeBuffer(icon);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {
                        bytes[i] += 256;
                    }
                }
                String path = request.getSession().getServletContext().getRealPath("/") + UUIDUtil.getUUID() + ".jpg";
                OutputStream out = new FileOutputStream(path);
                out.write(bytes);
                out.flush();
                out.close();
                delList.add(path);
                File file = new File(path);
                //            ByteArrayInputStream bio = new ByteArrayInputStream(bytes);
                //            QiNiuUtil.uploadStream(vo.getBucket(),null,bio);
                Map map = QiNiuUtil.uploadFile(vo.getBucket(), null, file);
                if (map != null) {
                    Map m = new HashMap();
                    m.put(map.get("key"), map.get("keyWithPrefix"));
                    list.add(m);
                }
            }
            for (String filePath : delList) {
                File f = new File(filePath);
                if (f.exists()) {
                    f.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("上传图片异常");
        }

        return new JsonResult(list);
    }



    @ResponseBody
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public JsonResult smsCode(HttpServletRequest request, @RequestBody PhoneVo vo) throws Exception {
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
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (!PasswordUtil.isAllNumeric(vo.getPhone()) || vo.getPhone().length() != 11) {
            return new JsonResult(1, "请输入合法手机号");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp = userService.getByCondition(condition);
        if (temp != null) {
            return new JsonResult(-1, "该手机号已被注册");
        }
        int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
        SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.COMMON, smsCode + "");
        Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
        return new JsonResult();
    }


    @ResponseBody
    @RequestMapping(value = "/registersms", method = RequestMethod.POST)
    public JsonResult registerSmsCode(HttpServletRequest request, @RequestBody PhoneVo vo) throws Exception {
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (!PasswordUtil.isAllNumeric(vo.getPhone()) || vo.getPhone().length() != 11) {
            return new JsonResult(1, "请输入合法手机号");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User temp = userService.getByCondition(condition);
        if (temp != null) {
            return new JsonResult(-1, "该手机号已被注册");
        }
        int smsCode = (int) ((Math.random() * 9 + 1) * 100000);
        SmsUtil.sendSmsCode(vo.getPhone(), SmsTemplate.COMMON, smsCode + "");
        Strings.setEx(RedisKey.SMS_CODE.getKey() + vo.getPhone(), RedisKey.SMS_CODE.getSeconds(), smsCode + "");
        return new JsonResult();
    }


    public static void main(String[] args) {
//        File file = new File("E:\\ideawork\\serveManage\\hongbao\\hongbao-api-web\\target\\api\\0028FB70FFA14A68ABF55EA2D48BEE09.jpg");
//        if (file.exists()) {
//            System.out.println(file.exists());
//            System.out.println("111");
//            file.delete();
//        }
//        System.out.println(file.exists());
//            QiNiuUtil.batchDelFile("doupai-test-goods11",new String[]{"FlM-OCUpiNope_7GQ-FngttsZF1S"});
    }


}
