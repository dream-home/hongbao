package com.yanbao.service.impl;

import com.mall.model.SysFileLink;
import com.yanbao.constant.StatusType;
import com.yanbao.mapper.FileLinkMapper;
import com.yanbao.service.FileLinkService;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author zhuzh
 * @date 2017年1月11日
 */
@Service
public class FileLinkServiceImpl implements FileLinkService {

	@Autowired
	private FileLinkMapper fileLinkDao;

	@Override
	public Integer add(SysFileLink model) throws Exception {
		if (model == null) {
			return 0;
		}
		setDefaultValue(model);
		return fileLinkDao.add(model);
	}

	private void setDefaultValue(SysFileLink model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	public Integer del(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return 0;
		}
		return fileLinkDao.del(id);
	}

	@Override
	public List<SysFileLink> getList(String linkId) throws Exception {
		if (StringUtils.isBlank(linkId)) {
			return null;
		}
		return fileLinkDao.getList(linkId);
	}

	@Override
	public Integer update(String id, SysFileLink model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return fileLinkDao.update(id, model);
	}
	//添加图片数据 --V3.8
	@Override
	public SysFileLink saveInfo(String sysFileID, String linkID,Integer isDefault)
			throws Exception {
		SysFileLink fileLink = new SysFileLink();
		Date now=new Date();
		fileLink.setId(UUIDUtil.getUUID());
		fileLink.setFileId(sysFileID);
		fileLink.setLinkId(linkID);
		fileLink.setLinkType(0);
		fileLink.setCreateTime(now);
		fileLink.setUpdateTime(now);
		fileLink.setIsDefault(isDefault);
		fileLink.setStatus(StatusType.TRUE.getCode());
		int count=fileLinkDao.add(fileLink);
		return count>0?fileLink:null;
	}

	@Override
	public Integer delPo(SysFileLink model) throws Exception {
		return fileLinkDao.delPo(model);
	}

	@Override
	public List<SysFileLink> getListByPo(SysFileLink model) throws Exception {
		return fileLinkDao.getListByPo(model);
	}

}
