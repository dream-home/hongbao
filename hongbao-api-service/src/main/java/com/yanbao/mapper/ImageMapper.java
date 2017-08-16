package com.yanbao.mapper;

import com.mall.model.Image;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMapper {

    Integer add(@Param("model") Image model);

    Image getById(@Param("id") String id);

    List<Image> getByLinkId(@Param("linkId") String linkId,@Param("type") Integer type);

    Integer deleteById(@Param("id") String id);

    Integer updateById(@Param("id") String id, @Param("model") Image model);

}
