package com.yanbao.dao;

import com.mall.model.Grade;

/**
 *
 * @author zcj
 * @date 2017年03月02日
 */
public interface GradeDao {
    /**
     * 根据三个销售业绩获取会员所属等级
     * */
    Integer getMemberGrade(Double minCount, Double midCount) throws Exception;
    /**
     * 根据会员所属等级获取详细信息
     * */
    Grade getGradeDetil(Integer grade) throws Exception;
}
