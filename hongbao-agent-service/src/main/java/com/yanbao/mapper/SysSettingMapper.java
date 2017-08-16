package com.yanbao.mapper;

import com.mall.model.SysSetting;
import com.yanbao.core.dao.CommonDao;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSettingMapper  extends CommonDao<SysSetting> {

	SysSetting get();

}
