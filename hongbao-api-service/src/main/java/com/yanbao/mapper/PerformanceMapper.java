package com.yanbao.mapper;

import com.mall.model.PerformanceRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceMapper {
    Integer add(@Param("model") PerformanceRecord model);
}
