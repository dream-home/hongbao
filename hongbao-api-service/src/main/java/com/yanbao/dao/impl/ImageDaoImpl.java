package com.yanbao.dao.impl;

import com.mall.model.Image;
import com.yanbao.dao.ImageDao;
import com.yanbao.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zzwei
 * @date 2017年06月28日
 */
@Repository
public class ImageDaoImpl implements ImageDao {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public Integer add(Image model) throws Exception {
        return imageMapper.add(model);
    }

    @Override
    public Image getById(String id) throws Exception {
        return imageMapper.getById(id);
    }

    @Override
    public Integer deleteById(String id) throws Exception {
        return imageMapper.deleteById(id);
    }

    @Override
    public Integer updateById(String id, Image model) throws Exception {
        return imageMapper.updateById(id, model);
    }

    @Override
    public List<Image> getByLinkId(String linkId,Integer type) throws Exception {
        return imageMapper.getByLinkId(linkId,type);
    }
}
