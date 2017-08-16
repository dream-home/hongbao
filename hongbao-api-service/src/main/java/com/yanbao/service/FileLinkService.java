package com.yanbao.service;

import com.mall.model.SysFileLink;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface FileLinkService {

	Integer add(SysFileLink model) throws Exception;

	Integer del(String id) throws Exception;

	List<SysFileLink> getList(String linkId) throws Exception;

	Integer update(String id, SysFileLink model) throws Exception;
	
	SysFileLink saveInfo(String sysFileID, String linkID,Integer isDefault) throws Exception;
	
	Integer delPo(SysFileLink model) throws Exception;
	
	List<SysFileLink> getListByPo(SysFileLink model) throws Exception;
}
