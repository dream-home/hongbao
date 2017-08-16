package com.yanbao.service;

import com.mall.model.Store;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家相关接口
 * Created by Administrator on 2017/6/20.
 */
public interface StoreService extends CommonService<Store> {

    /**
     * 代理区域商家列表
     * @param ids
     * @return
     * @throws Exception
     */
    List<Store> getStoreList(List<String> ids,List<Integer> statusList,Page page) throws Exception;

    /**
     * 统计代理区域或搜索区域商家数量
     * @param ids
     * @return
     * @throws Exception
     */
    Integer countStoreList(List<String> ids,List<Integer> statusList) throws Exception;
    /**
     * 查出所有地区集合的商家
     * @param areaIds
     * @return
     * @throws Exception
     */
    List<Store> getListByAreaId(List<String> areaIds,List<Integer> statusList,Integer startRow,Integer pageSize) throws Exception;
    /**
     * 查出所有地区集合的商家
     * @param areaIds
     * @return
     * @throws Exception
     */
    List<Store> getListByAreaId(List<String> areaIds,List<Integer> statusList,Integer startRow,Integer pageSize,String searh) throws Exception;
}
