package com.yanbao.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yanbao.core.page.JsonResult;
import com.yanbao.service.impl.UploadServiceImpl;

/**
 * Created by summer on 2016-12-12:12:15;
 */
@Controller
public class UploadController extends BaseController {

    private static Map<String, String> contentTypeMap = new HashMap<>();

    static {
        contentTypeMap.put(".jpg", "image/jpg");
        contentTypeMap.put(".gif", "image/gif");
        contentTypeMap.put(".png", "image/png");
        contentTypeMap.put(".jpeg", "image/jpeg");
        contentTypeMap.put(".bmp", "application/x-bmp");
    }

    @Autowired
    UploadServiceImpl uploadService;
    @Value("${baseDirPath}")
    String baseDirPath;
    @Value("${upLoadDomain}")
    String upLoadDomain;


    @RequestMapping(value = "/image/upload", method ={RequestMethod.POST,RequestMethod.OPTIONS})
    @ResponseBody
    public JsonResult uploadImg( MultipartFile file, HttpServletRequest request){
        if (request.getMethod().toString().equals("OPTIONS")){
            return success();
        }
        String fileName=file.getOriginalFilename();
        if (!isPicture(fileName)){
            return fail("只允许上传图片");
        }
        final String imageType =getFileType(fileName);
        String realPictureName = UUID.randomUUID().toString().replace("-", "").toUpperCase() + imageType;
        // 文件夹命名规则为年月的字符串形式,如:201410
        final String folder = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM")+"/jpg";
        // 上传到本地
        String fullPath=uploadService.uploadPictureToLocal(file, baseDirPath, "/" + folder, realPictureName);
        String path=folder+"/"+realPictureName;
        return success(path);
    }

    public static boolean isPicture(String name) {
        name = name.toLowerCase();
        for (String type : contentTypeMap.keySet()) {
            if (name.toLowerCase().endsWith(type))
                return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/uploadDomain")
    public JsonResult getUploadDomain(){
        return success(upLoadDomain);
    }
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getImageContentType(String name) {
        String fileType = getFileType(name).toLowerCase();
        return contentTypeMap.get(fileType);
    }


}
