package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.EpRecord;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;

@Repository
public interface EpRecordMapper extends CommonDao<EpRecord>{
	List<EpRecord> getByUserids(@Param("ids")List<String> ids,@Param("page")Page page);
	List<EpRecord> readList(@Param("page")Page page);
}
