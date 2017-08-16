package com.yanbao.mapper;

import com.mall.model.PartnerBillDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
@Repository
public interface PartnerBillDetailMapper{

    List<PartnerBillDetail> readList(@Param("model") PartnerBillDetail model,@Param("startRow") Integer startRow,@Param("pageSize") Integer pageSize) throws Exception;

    Integer readCount(@Param("model") PartnerBillDetail model) throws Exception;

    Integer create(@Param("model") PartnerBillDetail model) throws Exception;

    Integer updateById(@Param("id") String id, @Param("model") PartnerBillDetail model) throws Exception;

    PartnerBillDetail readById(@Param("id") String id) throws Exception;

    Double deleteById(@Param("id") String id) throws Exception;
}
