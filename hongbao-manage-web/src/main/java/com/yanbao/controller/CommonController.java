package com.yanbao.controller;

import com.yanbao.core.page.JsonResult;
import com.yanbao.service.UserService;
import com.yanbao.util.PropertiesUtil;
import com.yanbao.util.QiNiuUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.DelImgVo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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

    /***
     * 根据文件名删除上传图片
     */
    @RequestMapping(value = "/delupimg", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delUpImg(HttpServletRequest request, HttpServletResponse response, @RequestBody DelImgVo vo) {
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
        } finally {
            throw new RuntimeException("删除异常");
        }
    }


}
