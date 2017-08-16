package com.yanbao.mapper;

import com.mall.model.Store;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家相关接口
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface StoreMapper extends CommonDao<Store> {

    /**
     * 代理区域或搜索区域商家列表
     * @param ids
     * @return
     * @throws Exception
     */
    List<Store> getStoreList(@Param("ids") List<String> ids,@Param("statusList") List<Integer> statusList,@Param("page") Page page) throws Exception;

    /**
     * 统计代理区域或搜索区域商家数量
     * @param ids
     * @return
     * @throws Exception
     */
    Integer countStoreList(@Param("ids") List<String> ids,@Param("statusList") List<Integer> statusList) throws Exception;
    
    
    /**
     * 查出所有地区集合的商家
     * @param areaIds
     * @return
     * @throws Exception
     */
    List<Store> getListByAreaId(@Param("areaIds") List<String> areaIds,@Param("statusList") List<Integer> statusList,@Param("startRow") Integer startRow,@Param("pageSize") Integer pageSize,@Param("searh")String searh) throws Exception;

}
