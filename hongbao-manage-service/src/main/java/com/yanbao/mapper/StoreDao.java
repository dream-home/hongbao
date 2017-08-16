package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.Store;
import com.yanbao.vo.StoreVo;

/**
 * Created by summer on 2017-01-11:15:11;
 */
public interface StoreDao extends CommonDao<Store>{

    List<StoreVo> readVoList(@Param("model") StoreVo storeVo,@Param("page") Page page);

    Integer readVoCount(@Param("model") StoreVo storeVo);

}
