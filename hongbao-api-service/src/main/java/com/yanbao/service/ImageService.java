package com.yanbao.service;

import com.mall.model.Image;

import java.util.List;

/**
 * @author zzwei
 * @date 2017年06月28日
 */
public interface ImageService {

    Integer add(Image model) throws Exception;

    Image getById(String id) throws Exception;

    Integer deleteById(String id) throws Exception;

    Integer updateById(String id, Image model) throws Exception;

    List<Image> getByLinkId(String linkId,Integer type)throws Exception;

}
