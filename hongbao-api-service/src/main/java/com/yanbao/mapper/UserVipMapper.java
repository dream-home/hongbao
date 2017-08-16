package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVipMapper {

	List<String> getVipList(@Param("ids") List<String> ids);
}
