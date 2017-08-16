package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.Ad;

/**
 * Created by summer on 2016-12-08:13:39;
 */
@Repository
public interface ADDao extends CommonDao<Ad> {
	

	int readStartCount(@Param("model") Ad  ad);
	
	List<Ad> readStartList(@Param("model") Ad  ad,@Param("page") Page page);
	

}
