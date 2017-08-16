package com.yanbao.dao;

import java.util.List;

import com.mall.model.SysFile;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface FileDao {

	Integer add(SysFile model) throws Exception;

	SysFile getById(String id) throws Exception;

	Integer del(String id) throws Exception;

	Integer update(String id, SysFile model) throws Exception;
	
	List<SysFile> getSelectByIdIn(String ids) throws Exception;

    List<SysFile> getListByFileIds(List<String> fileList);
}
