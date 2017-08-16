package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.ConsumeEPRecord;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
public interface ConsumeEPRecordService {

    Integer add(ConsumeEPRecord model) throws Exception;
    
    PageResult<ConsumeEPRecord> getPage(String userId,Page page) throws Exception;

}
