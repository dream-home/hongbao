package com.yanbao.mapper;

import com.mall.model.City;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author jay.zheng 2017-07-14 10:23
 *
 */

@Repository
public interface SysCityMapper{

    City readById(@Param("id") String id);

    City readOne(@Param("model") City model);

    List<City> readList(@Param("model") City model, @Param("startRow") int startRow, @Param("pageSize") int pageSize);

    List<City> getAllCity(@Param("field") String field);

    Integer readCount(@Param("model") City model);
}
