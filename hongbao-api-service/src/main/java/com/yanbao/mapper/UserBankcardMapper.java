package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.UserBankcard;

@Repository
public interface UserBankcardMapper {

	List<UserBankcard> getList(@Param("userId") String userId);

	Integer add(@Param("model") UserBankcard model);

	Integer update(@Param("id") String id, @Param("model") UserBankcard model);

}
