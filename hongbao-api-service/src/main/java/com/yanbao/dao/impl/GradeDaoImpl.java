package com.yanbao.dao.impl;

import com.yanbao.dao.GradeDao;
import com.yanbao.mapper.GradeMapper;
import com.mall.model.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zcj
 * @date 2017年03月02日
 */
@Repository
public class GradeDaoImpl implements GradeDao{

    @Autowired
    private GradeMapper gradeMapper;
    @Override
    public Integer getMemberGrade(Double minCount, Double midCount) throws Exception {
        return gradeMapper.getMemberGrade(minCount,midCount);
    }

    @Override
    public Grade getGradeDetil(Integer grade) throws Exception {
        return gradeMapper.getGradeDetil(grade);
    }
}
