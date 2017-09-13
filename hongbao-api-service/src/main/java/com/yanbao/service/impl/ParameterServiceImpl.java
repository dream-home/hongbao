package com.yanbao.service.impl;

import com.mall.model.Parameter;
import com.yanbao.mapper.ParameterMapper;
import com.yanbao.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterMapper parameterMapper;


    @Override
    public List<Parameter> getList() throws Exception {
        return parameterMapper.getList();
    }

    @Override
    public Parameter readByKey(String key) throws Exception {
        return parameterMapper.readByKey(key);
    }

    @Override
    public int updateValue(String key, String value) {
         return parameterMapper.updateValue(key,value);
    }
}
