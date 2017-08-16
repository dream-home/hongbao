package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.Image;
import com.yanbao.core.dao.CommonDao;

import java.util.List;

@Repository
public interface ImgageMapper extends CommonDao<Image>{
    List<Image> readByIds(@Param("list")List<String> list);
}
