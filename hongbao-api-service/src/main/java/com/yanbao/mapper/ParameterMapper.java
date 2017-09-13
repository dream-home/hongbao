package com.yanbao.mapper;

import com.mall.model.Parameter;
import com.yanbao.core.dao.CommonDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家相关接口
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface ParameterMapper extends CommonDao<Parameter> {

    /**
     * 代理区域或搜索区域商家列表
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;

    int updateValue(@Param("name") String name, @Param("value") String value);

    Parameter readByKey(@Param("name") String name);
}
