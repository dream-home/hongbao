package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2016-12-14:11:30; 系统设置
 */
@Controller
@RequestMapping("/sys")
public class SysController extends BaseController {

	@Autowired
	SysSettingService sysSettingService;
	@Autowired
	SysFileService sysFileService;
	@Autowired
	SysFileLinkService sysFileLinkService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	StoreService storeService;
	@Autowired
	UserService userService;
	@Autowired
	HbImgageService imageService;

	@ResponseBody
	@RequestMapping("/detail")
	public JsonResult detail() {
		try {
			SysSetting sysSetting = new SysSetting();
			sysSetting.setStatus(1);
			sysSetting = sysSettingService.readOne(sysSetting);
			if (sysSetting == null) {
				SysSetting sysSetting1 = new SysSetting();
				sysSetting1.setId(UUIDUtil.getUUID());
				sysSetting1.setDrawNumMin(3);
				sysSetting1.setDrawNumMax(20);
				sysSettingService.create(sysSetting1);
				sysSetting1 = sysSettingService.readOne(sysSetting1);
				return success(sysSetting1);
			}
			return success(sysSetting);
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
		return fail("数据库出现错误");
	}

	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(SysSetting sysSetting) {
		// sysSettingService.updateById(sysSetting.getId(), sysSetting);

		sysSettingService.updateById(sysSetting.getId(), sysSetting);
		return success();
	}

	/**
	 * 商品图片审查
	 *
	 * @return
	 */
	@RequestMapping("/passPictures")
	@ResponseBody
	public JsonResult passPictures(String fileIds) {
		String[] ids = fileIds.split(",");
		SysFile sysFile = new SysFile();
		sysFile.setStatus(1);
		for (String id : ids) {
			sysFileService.updateById(id, sysFile);
		}
		return success();
	}
	
	
	/**
	 * 
	 * 商品图片审查通过（新）
	 * 2017-07-18 10:59
	 */
	@RequestMapping("/passPicturesNew")
	@ResponseBody
	public JsonResult passPicturesNew(String fileIds){
		String[] ids  = fileIds.split(",");
		for(String id:ids){
			Image image = new Image();
			image.setStatus(2);
			imageService.updateById(id, image);
		}
		
		
		
		return success();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 商品图片审查不通过(新)
	 *2017-07-17 18：00
	 * @return
	 */
	@RequestMapping("/unpassPicturesNew")
	@ResponseBody
	public JsonResult unpassPicturesNew(String fileIds) {
		String[] ids = fileIds.split(",");
		
		
		for(String id:ids){
			Image image1  = imageService.readById(id);
			Goods goods = new Goods();
			goods.setStatus(0);
			goods.setVerify(3);
			goodsService.updateById(image1.getImageLinkId(), goods);
			
			Image image = new Image();
			image.setStatus(1);
			imageService.updateById(id, image);
			
		}
		return success();
	}
	
	

	/**
	 * 图片审查不通过 关闭店铺 冻结账户 通过文件 找到店铺 通过店铺找到用户
	 *
	 * @param id
	 *            文件表id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unPassPicture")
	public JsonResult unPassPicture(String id) {
		SysFile sysFile = sysFileService.readById(id);
		if (sysFile == null) {
			return fail("图片不存在");
		}
		sysFile.setStatus(2);
		sysFileService.updateById(id, sysFile);
		SysFileLink sysFileLink = new SysFileLink();
		sysFileLink.setFileId(id);
		sysFileLink = sysFileLinkService.readOne(sysFileLink);
		if (sysFileLink == null) {
			return fail("图片关联失效");
		}
		String storeId = "";
		if (sysFileLink.getLinkType() == 0) {
			storeId = sysFileLink.getLinkId();
		}
		if (sysFileLink.getLinkType() == 1) {
			storeId = goodsService.readById(sysFileLink.getLinkId()).getStoreId();
		}
		Map<String, String> store = new HashedMap();
		store.put("storeId", storeId);
		// 关闭店铺 冻结账户
		Store store1 = storeService.readById(storeId);
		User user = null;
		if (store1 != null) {
			store1.setStatus(3);
			storeService.updateById(storeId, store1);
			user = userService.readById(store1.getUserId());
		}
		if (user != null) {

			user.setStatus(0);
			Strings.del("token_api_" + user.getId());
			userService.updateById(user.getId(), user);
		}
		return success(store);
	}

	/**
	 * 图片审查列表
	 * 
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pictureList")
	public JsonResult pictureList(Page page) {
		SysFile file = new SysFile();
		file.setStatus(0);
		file.setFileType(0);
		int count = sysFileService.readCount(file);
		if (count == 0) {
			return fail("没有需要审查的图片");
		}
		List<SysFile> sysFiles = sysFileService.readList(file, page.getPageNo(), page.getPageSize(), count);
		return success(getPageResult(page, count, sysFiles));
	}

	/**
	 * 图片审查列表
	 * 
	 * @param page 2017-07-17 12:00:00 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/imageList")
	public JsonResult imageList(Page page) {
		Image image = new Image();
		image.setStatus(0);
		image.setType(4);

		int count = imageService.readCount(image);
		if (count == 0) {
			return fail("没有需要审查的图片");
		}
		List<Image> images = imageService.readList(image, page.getPageNo(), page.getPageSize(), count);
		// List<SysFile> sysFiles = sysFileService.readList(file,
		// page.getPageNo(), page.getPageSize(), count);
		return success(getPageResult(page, count, images));
	}

}
