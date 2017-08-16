package com.yanbao.mapper;

import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImgageMapper extends CommonDao<Image>{
	List<Image> readByIds(@Param("list")List<String> list);
	List<Image> getPoList(@Param("model") Image image);
}
