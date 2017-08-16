package com.yanbao.service.impl;

import com.mall.model.Image;
import com.yanbao.constant.StatusType;
import com.yanbao.dao.ImageDao;
import com.yanbao.service.ImageService;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zzwei
 * @date 2017年06月28日
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Integer add(Image model) throws Exception {
        setDefaultValue(model);
        return imageDao.add(model);
    }

    @Override
    public Image getById(String id) throws Exception {
        return imageDao.getById(id);
    }

    @Override
    public Integer deleteById(String id) throws Exception {
        return  imageDao.deleteById(id);
    }

    @Override
    public Integer updateById(String id, Image model) throws Exception {
        return  imageDao.updateById(id,model);
    }

    @Override
    public List<Image> getByLinkId(String linkId,Integer type) throws Exception {
        return  imageDao.getByLinkId(linkId,type);
    }

    private void setDefaultValue(Image model) {
        model.setId(UUIDUtil.getUUID());
        model.setStatus(StatusType.FALSE.getCode());
        model.setCreateTime(new Date());
        model.setUpdateTime(model.getCreateTime());
    }


}
