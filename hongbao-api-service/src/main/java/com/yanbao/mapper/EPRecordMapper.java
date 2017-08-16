package com.yanbao.mapper;

import com.yanbao.core.page.Page;
import com.mall.model.EpRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EPRecordMapper {

    Integer count(@Param("userId") String userId, @Param("recordTypes") String[] recordTypes);

    List<EpRecord> getList(@Param("userId") String userId, @Param("recordTypes") String[] recordTypes, @Param("page") Page page);

    Integer add(@Param("model") EpRecord model);

    Integer update(@Param("id") String id, @Param("model") EpRecord model);
}
