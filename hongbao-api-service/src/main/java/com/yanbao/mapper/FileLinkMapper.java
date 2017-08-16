package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.SysFileLink;

@Repository
public interface FileLinkMapper {

	Integer add(@Param("model") SysFileLink model);

	Integer del(@Param("id") String id);

	List<SysFileLink> getList(@Param("linkId") String linkId);

	Integer update(@Param("id") String id, @Param("model") SysFileLink model);
	
	Integer delPo(@Param("model") SysFileLink model);
	
	List<SysFileLink> getListByPo(@Param("model") SysFileLink model);
}
