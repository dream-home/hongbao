package com.yanbao.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.EpRecord;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.EpRecordMapper;
import com.yanbao.service.EpRecordService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbEpRecordServiceImpl")
public class EpRecordServiceImpl extends CommonServiceImpl<EpRecord> implements EpRecordService  {
	
	@Autowired
	private EpRecordMapper mapper;
	
	@Override
	protected CommonDao<EpRecord> getDao() {
		return mapper;
	}

	@Override
	protected Class<EpRecord> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EpRecord> getByUserids(List<String> ids,Page page) {
		return mapper.getByUserids(ids,page);
	}

	@Override
	public List<EpRecord> readList(Page page) {
		return mapper.readList(page);
	}
	
}