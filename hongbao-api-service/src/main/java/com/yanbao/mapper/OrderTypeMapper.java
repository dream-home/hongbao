package com.yanbao.mapper;

import com.mall.model.OrderTypeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeMapper {
    OrderTypeModel getById(@Param("id") String id);

    void add(@Param("model") OrderTypeModel model);
}
