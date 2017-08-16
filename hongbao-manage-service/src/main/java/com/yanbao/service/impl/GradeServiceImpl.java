package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GradeDao;

import com.mall.model.Grade;
import com.yanbao.service.GradeService;
/**
 * 
 * @author zhengyicai	2017-03-03 17:22
 *
 */

@Service
public class GradeServiceImpl extends CommonServiceImpl<Grade> implements GradeService  {
	 	@Autowired
	    GradeDao gradeDao;

	    @Override
	    protected CommonDao<Grade> getDao() {
	        return gradeDao;
	    }

	    @Override
	    protected Class<Grade> getModelClass() {
	        return Grade.class;
	    }
}
