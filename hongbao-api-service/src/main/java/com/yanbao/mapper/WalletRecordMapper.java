package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.WalletRecord;

@Repository
public interface WalletRecordMapper {

	Integer count(@Param("userId") String userId, @Param("recordTypes") String[] recordTypes);

	List<WalletRecord> getList(@Param("userId") String userId, @Param("recordTypes") String[] recordTypes, @Param("page") Page page);

	Integer add(@Param("model") WalletRecord model);

	Integer update(@Param("id") String id, @Param("model") WalletRecord model);

	Double sumScore(@Param("userId") String userId, @Param("recordType") Integer recordType);

	List<WalletRecord> getLatestList(@Param("userId") String userId);
}
