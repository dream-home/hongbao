package com.yanbao.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.mall.model.SysFile;
import com.yanbao.service.FileService;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.UUIDUtil;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private static final String UPLOAD = "/upload/";

	private static Map<String, String> contentTypeMap = new HashMap<>();

	static {
		contentTypeMap.put(".jpg", "image/jpg");
		contentTypeMap.put(".png", "image/png");
		contentTypeMap.put(".jpeg", "image/jpeg");
		contentTypeMap.put(".bmp", "application/x-bmp");
	}

	@Autowired
	FileService fileService;
	@Value("${file_root_path}")
	private String FILE_ROOT_PATH;

	@RequestMapping(value = "/image", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult uploadImg(MultipartFile file, HttpServletRequest request) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		String fileName = file.getOriginalFilename();
		if (!isPicture(fileName)) {
			return new JsonResult(1, "请上传jpg/png/bmp格式图片 ");
		}
		String uploadPath = request.getSession().getServletContext().getRealPath(UPLOAD);
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String imgFileName = UUIDUtil.getUUID() + suffix;
		File tempFile = new File(uploadPath, imgFileName);

		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
			outputStream = new FileOutputStream(tempFile);
			int readBytes = 0;
			byte[] buffer = new byte[10240];
			while ((readBytes = inputStream.read(buffer, 0, 10240)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
			}
		}
		SysFile sysFile = fileService.save(tempFile, FILE_ROOT_PATH, token.getId());
		return new JsonResult(sysFile);
	}

	public static boolean isPicture(String name) {
		name = name.toLowerCase();
		for (String type : contentTypeMap.keySet()) {
			if (name.toLowerCase().endsWith(type))
				return true;
		}
		return false;
	}

}
