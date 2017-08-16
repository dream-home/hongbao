package com.yanbao.service.impl;

import com.yanbao.constant.EPRecordType;
import com.yanbao.constant.StatusType;
import com.yanbao.constant.UserType;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.EPRecordDao;
import com.mall.model.EpRecord;
import com.mall.model.User;
import com.yanbao.service.EPRecordService;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author zzwei
 * @date 2017年06月17日
 */
@Service
public class EPRecordServiceImpl implements EPRecordService {

	@Autowired
	private EPRecordDao ePRecordDao;

	@Override
	public PageResult<EpRecord> getPage(String userId, String[] recordTypes, Page page) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		PageResult<EpRecord> pageResult = new PageResult<EpRecord>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = ePRecordDao.count(userId, recordTypes);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<EpRecord> list = ePRecordDao.getList(userId, recordTypes, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(EpRecord model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return ePRecordDao.add(model);
	}

	@Override
	public Integer update(String id, EpRecord model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return ePRecordDao.update(id, model);
	}

	private void setDefaultValue(EpRecord model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

	public void consumeEpRecord(User user, Double ep, String orderNo, EPRecordType recordType,String sendUserId,String receiveUserId,String remark) throws Exception {
		EpRecord record = new EpRecord();
		record.setId(UUIDUtil.getUUID());
		record.setCreateTime(new Date());
		record.setSendUserId(sendUserId);
		record.setReceiveUserId(receiveUserId);
		record.setOrderNo(orderNo);
		record.setEp(ep);
		record.setRecordType(recordType.getCode());
        if (ToolUtil.isEmpty(remark)){
            record.setRemark(remark);
        }else {
            record.setRemark(recordType.getMsg());
        }
		record.setRecordTypeDesc(recordType.getMsg());
		record.setStatus(1);
		record.setRemark(recordType.getMsg());
		if (ToolUtil.isEmpty(user.getStoreId())) {
			record.setUserType(UserType.COMMON.getCode());
		}else if(!ToolUtil.isEmpty(user.getStoreId())){
			record.setUserType(UserType.STORE.getCode());
		}
		ePRecordDao.add(record);
	}
}
