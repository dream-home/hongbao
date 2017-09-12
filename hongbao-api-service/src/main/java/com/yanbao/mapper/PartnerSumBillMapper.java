package com.yanbao.mapper;

import com.mall.model.PartnerBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerSumBillMapper {
	Integer create(@Param("model") PartnerBill model);

	PartnerBill readById(@Param("id") String id);

	List<PartnerBill> readList(@Param("model") PartnerBill model, @Param("startRow") Integer startRow, @Param("pageSize") Integer pageSize);

	PartnerBill readOne(@Param("model") PartnerBill model);

	Integer readCount(@Param("model") PartnerBill model);

	Integer updateById(@Param("model") PartnerBill model, @Param("id") String id);

	Integer deleteById(@Param("id") String id);

	List<PartnerBill> getAllPartners(@Param("startTime") String startTime, @Param("endTime") String endTime);

	List<PartnerBill> getPartners(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
