package com.yanbao.service;

import com.mall.model.City;

import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年7月14日
 */
public interface SysCityService {

    City readById(String id) throws Exception;

    City readOne(City model) throws Exception;

    List<City> readList(City model, int startRow, int pageSize) throws Exception;

    Integer readCount(City model) throws Exception;

    List<City> getAllCity(String field) throws Exception;
}
