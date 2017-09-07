package com.yanbao.mapper;

import com.mall.model.SecondCallBack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondCallBackMapper {
    SecondCallBack getById(@Param("id") String id);
}
