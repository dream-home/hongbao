package com.yanbao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.UserAddressDao;
import com.mall.model.UserAddress;
import com.yanbao.service.UserAddressService;
import com.yanbao.util.UUIDUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {

	@Autowired
	private UserAddressDao userAddressDao;

	@Override
	public List<UserAddress> getList(String userId) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		return userAddressDao.getList(userId);
	}

	@Override
	public Integer add(UserAddress model) throws Exception {
		if (model == null || StringUtils.isBlank(model.getUserId())) {
			return 0;
		}
		setDefaultValue(model);
		return userAddressDao.add(model);
	}

	@Override
	public Integer update(String id, UserAddress model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return userAddressDao.update(id, model);
	}

	private void setDefaultValue(UserAddress model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setIsDefault(StatusType.FALSE.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	@Transactional
	public void updateDefaultAddr(String userId,String addrId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			return;
		}
		//根据userid将默认地址清空
		userAddressDao.updateDefaultAddr(userId);
		UserAddress model = new UserAddress();
		model.setId(addrId);
		model.setIsDefault(StatusType.TRUE.getCode());
		//最后再根据唯一地址id设置默认收货地址
		userAddressDao.update(model.getId(),model);
	}


	@Override
	@Transactional
	public void handleUserAddress(UserAddress vo) throws Exception{
		if(vo.getId()==null || "".equals(vo.getId())){
			this.add(vo);
		}else if(!StringUtils.isEmpty(vo.getId())){
			this.update(vo.getId(),vo);
		}
	}
}
