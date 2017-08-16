package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.FileLinkDao;
import com.yanbao.mapper.FileLinkMapper;
import com.mall.model.SysFileLink;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class FileLinkDaoImpl implements FileLinkDao {

	@Autowired
	private FileLinkMapper fileLinkMapper;

	@Override
	public Integer add(SysFileLink model) throws Exception {
		return fileLinkMapper.add(model);
	}

	@Override
	public Integer del(String id) throws Exception {
		return fileLinkMapper.del(id);
	}

	@Override
	public List<SysFileLink> getList(String linkId) throws Exception {
		return fileLinkMapper.getList(linkId);
	}

	@Override
	public Integer update(String id, SysFileLink model) throws Exception {
		return fileLinkMapper.update(id, model);
	}

	@Override
	public Integer delPo(SysFileLink model) throws Exception {
		return fileLinkMapper.delPo(model);
	}

	@Override
	public List<SysFileLink> getListByPo(SysFileLink model) throws Exception {
		return fileLinkMapper.getListByPo(model);
	}

}
