package com.yanbao.service;

import com.mall.model.SysFile;

import java.io.File;
import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface FileService {

    SysFile save(File file, String FILE_ROOT_PATH, String userId) throws Exception;

    SysFile saveForBase64(String base64, String userId) throws Exception;

    SysFile getById(String id) throws Exception;

    Integer del(String id) throws Exception;

    Integer update(String id, SysFile model) throws Exception;

    List<SysFile> getSelectByIdIn(String ids) throws Exception;

    List<SysFile> getListByFileIds(List<String> fileList) throws Exception;

}
