package com.yanbao.service;

import com.mall.model.Parameter;

import java.util.List;

/**
 * 商家相关接口
 * Created by Administrator on 2017/6/20.
 */
public interface ParameterService {

    /**
     * 代理区域商家列表
     *
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;
}
