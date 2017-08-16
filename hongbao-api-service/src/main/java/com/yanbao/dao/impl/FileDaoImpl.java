package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.FileDao;
import com.yanbao.mapper.FileMapper;
import com.mall.model.SysFile;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class FileDaoImpl implements FileDao {

	@Autowired
	private FileMapper fileMapper;

	@Override
	public Integer add(SysFile model) throws Exception {
		return fileMapper.add(model);
	}

	@Override
	public SysFile getById(String id) throws Exception {
		return fileMapper.getById(id);
	}

	@Override
	public Integer del(String id) throws Exception {
		return fileMapper.del(id);
	}

	@Override
	public Integer update(String id, SysFile model) throws Exception {
		return fileMapper.update(id, model);
	}

	@Override
	public List<SysFile> getSelectByIdIn(String ids) throws Exception {
		return fileMapper.getByIdIn(ids);
	}

	@Override
	public List<SysFile> getListByFileIds(List<String> fileList) {
		return  fileMapper.getListByFileIds(fileList);
	}
}
