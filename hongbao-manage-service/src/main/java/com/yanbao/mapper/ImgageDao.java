package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;

@Repository
public interface ImgageDao extends CommonDao<Image>{
	
	
	List<Image> getPoList(@Param("model") Image image);

}
