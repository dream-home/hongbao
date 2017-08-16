package com.yanbao.mapper;

import com.mall.model.SysFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMapper {

    Integer add(@Param("model") SysFile model);

    SysFile getById(@Param("id") String id);

    Integer del(@Param("id") String id);

    Integer update(@Param("id") String id, @Param("model") SysFile model);

    List<SysFile> getByIdIn(@Param("ids") String ids);

    List<SysFile> getListByFileIds(@Param("fileList") List<String> fileList);
}
