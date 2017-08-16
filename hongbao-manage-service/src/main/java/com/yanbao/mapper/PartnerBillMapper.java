package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.PartnerBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;

/**
 * @author Pay - 1091945691@qq.com
 */
@Repository
public interface PartnerBillMapper  extends CommonDao<PartnerBill> {
	List<PartnerBill> getBySearch(@Param("page")Page page,@Param("model")PartnerBill pb)throws Exception;
}
