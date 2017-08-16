package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.UserAddress;

@Repository
public interface UserAddressMapper {

	List<UserAddress> getList(@Param("userId") String userId);

	Integer add(@Param("model") UserAddress model);

	Integer update(@Param("id") String id, @Param("model") UserAddress model);

	Integer updateDefaultAddr(@Param("userId") String userId);

}
