package com.yanbao.service.impl;

import com.mall.model.SysFile;
import com.yanbao.constant.StatusType;
import com.yanbao.mapper.FileMapper;
import com.yanbao.service.FileService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileMapper fileDao;

	@Override
	public SysFile save(File file, String FILE_ROOT_PATH, String userId) throws Exception {
		String datePath = DateTimeUtil.formatDate(new Date(), "yyyy/MM");
		String filename = file.getName();
		String suffix = "other";
		if (filename.indexOf(".") > 0) {
			suffix = filename.substring(filename.indexOf(".") + 1).toLowerCase();
		}
		String fullPath = FILE_ROOT_PATH + datePath + "/" + suffix;
		this.checkAndCreateDirectory(fullPath);
		File realFile = new File(fullPath, filename);
		FileUtils.copyFile(file, realFile);

		SysFile sysFile = saveToDB(realFile, userId, FILE_ROOT_PATH);
		return sysFile;
	}

	private SysFile saveToDB(File file, String userId, String FILE_ROOT_PATH) throws Exception {
		SysFile model = new SysFile();
		model.setFilename(file.getName());
		if (file.getName().indexOf(".") != -1) {
			model.setSuffix(file.getName().substring(file.getName().indexOf(".") + 1));
		}
		model.setPath(file.getPath().substring(FILE_ROOT_PATH.length()));
		model.setFileType(0);
		model.setUploadUserId(userId);
		setDefaultValue(model);
		fileDao.add(model);
		return model;
	}

	@Override
	public SysFile saveForBase64(String base64, String userId) throws Exception {
		SysFile model = new SysFile();
		model.setPath(base64);
		model.setFileType(0);
		model.setUploadUserId(userId);
		setDefaultValue(model);
		fileDao.add(model);
		return model;
	}

	private void setDefaultValue(SysFile model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.FALSE.getCode());
		model.setCreateTime(new Date());
		model.setUpdateTime(model.getCreateTime());
	}

	private void checkAndCreateDirectory(String directoryPath) {
		final List<File> needToCreateDirectoryList = new ArrayList<File>();
		File file = new File(directoryPath);
		while (file != null && !file.exists()) {
			needToCreateDirectoryList.add(file);
			file = file.getParentFile();
		}
		if (needToCreateDirectoryList.isEmpty()) {
			return;
		}
		for (int i = needToCreateDirectoryList.size() - 1; i >= 0; i--) {
			File f = needToCreateDirectoryList.get(i);
			f.mkdir();
		}
	}

	@Override
	public SysFile getById(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return fileDao.getById(id);
	}

	@Override
	public Integer del(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return 0;
		}
		return fileDao.del(id);
	}

	@Override
	public Integer update(String id, SysFile model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return fileDao.update(id, model);
	}

	@Override
	public List<SysFile> getSelectByIdIn(String ids) throws Exception {
		return fileDao.getByIdIn(ids);
	}

	@Override
	public List<SysFile> getListByFileIds(List<String> fileList) throws Exception {
		return fileDao.getListByFileIds(fileList);
	}

}
