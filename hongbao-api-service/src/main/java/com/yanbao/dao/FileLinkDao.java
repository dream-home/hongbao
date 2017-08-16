package com.yanbao.dao;

import java.util.List;

import com.mall.model.SysFileLink;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface FileLinkDao {

	Integer add(SysFileLink model) throws Exception;

	Integer del(String id) throws Exception;

	List<SysFileLink> getList(String linkId) throws Exception;

	Integer update(String id, SysFileLink model) throws Exception;
	
	Integer delPo(SysFileLink model) throws Exception;
	
	List<SysFileLink> getListByPo(SysFileLink model) throws Exception;
}
