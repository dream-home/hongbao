package com.yanbao.mapper;

import com.mall.model.Parameter;
import com.yanbao.core.dao.CommonDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统参数相关接口
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface ParameterMapper extends CommonDao<Parameter> {

    /**
     * 获取所有参数
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;
    /**
     * 批量更新系统参数设置
     * @return
     * @throws Exception
     */
    Integer batchUpdate(@Param("list") List<Parameter> list) throws Exception;

}
