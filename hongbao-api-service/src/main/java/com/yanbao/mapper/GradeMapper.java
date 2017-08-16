package com.yanbao.mapper;

import com.mall.model.Grade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zcj
 * @date 2017年03月02日
 */
@Repository
public interface GradeMapper {
    /**
     * 根据三个销售业绩获取会员所属等级
     * */
    Integer getMemberGrade(@Param("minCount") Double minCount,@Param("midCount") Double midCount);
    /**
     * 根据会员所属等级获取详细信息
     * */
    Grade getGradeDetil(@Param("grade") Integer grade);
}
