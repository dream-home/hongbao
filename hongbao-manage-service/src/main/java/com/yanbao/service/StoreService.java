package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.Store;
import com.yanbao.vo.StoreVo;

/**
 * Created by summer on 2017-01-11:14:56;
 */
public interface StoreService extends CommonService<Store> {

    List<StoreVo> readVoList(StoreVo storeVo, Page page);

    Integer readVoCount(StoreVo storeVo);
}
