package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 文件系统表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class SysFile extends SimpleModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件类型：0：图片，1：文件
     */
    private Integer fileType;
    /**
     * 上传人
     */
    private String uploadUserId;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

}
