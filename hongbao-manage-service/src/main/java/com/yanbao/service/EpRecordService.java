package com.yanbao.service;

import java.util.List;

import com.mall.model.EpRecord;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;

/**
 * @author Pay - 1091945691@qq.com
 */
public interface EpRecordService extends CommonService<EpRecord>{
	List<EpRecord> getByUserids(List<String> ids,Page page);
	List<EpRecord> readList(Page page);
}