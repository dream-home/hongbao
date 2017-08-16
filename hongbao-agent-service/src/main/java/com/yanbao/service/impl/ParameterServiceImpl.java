package com.yanbao.service.impl;

import com.mall.model.Parameter;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.ParameterMapper;
import com.yanbao.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class ParameterServiceImpl extends CommonServiceImpl<Parameter> implements ParameterService {

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    protected CommonDao<Parameter> getDao() {
        return parameterMapper;
    }
    @Override
    protected Class<Parameter> getModelClass() {
        return Parameter.class;
    }

    @Override
    public List<Parameter> getList() throws Exception {
        return parameterMapper.getList();
    }
}
