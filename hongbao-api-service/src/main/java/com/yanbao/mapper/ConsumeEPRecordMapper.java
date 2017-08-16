package com.yanbao.mapper;

import com.yanbao.core.page.Page;
import com.mall.model.ConsumeEPRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
@Repository
public interface ConsumeEPRecordMapper {

    Integer add(@Param("model") ConsumeEPRecord model);
    
    Integer count(@Param("userId") String userId);

	List<ConsumeEPRecord> getList(@Param("userId") String userId, @Param("page") Page page);

}
