package com.yanbao.service.impl;

import com.mall.model.City;
import com.yanbao.dao.SysCityDao;
import com.yanbao.dao.SysSettingDao;
import com.yanbao.service.SysCityService;
import com.yanbao.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年7月14日
 */
@Service
public class SysCityServiceImpl implements SysCityService {

    @Autowired
    private SysCityDao sysCityDao;

    @Override
    public City readById(String id) throws Exception {
        if (ToolUtil.isEmpty(id)) {
            return null;
        }
        return sysCityDao.readById(id);
    }

    @Override
    public City readOne(City model) throws Exception {
        if (model == null) {
            return null;
        }
        return sysCityDao.readOne(model);
    }

    @Override
    public List<City> readList(City model, int startRow, int pageSize) throws Exception {
        return sysCityDao.readList(model, startRow, pageSize);
    }

    @Override
    public Integer readCount(City model) throws Exception {
        return sysCityDao.readCount(model);
    }

    @Override
    public List<City> getAllCity(String field) throws Exception {
        if(ToolUtil.isEmpty(field)){
            return null;
        }
        return sysCityDao.getAllCity(field);
    }
}
