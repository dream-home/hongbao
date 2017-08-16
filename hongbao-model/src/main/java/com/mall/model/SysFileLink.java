package com.mall.model;

import com.yanbao.core.model.SimpleModel;


/**
 * 文件关联表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class SysFileLink extends SimpleModel {

    private static final long serialVersionUID = -7833218054468128226L;
    /**
     * 文件Id
     */
    private String fileId;
    /**
     * 关联Id
     */
    private String linkId;
    /**
     * 关联类型：0：店铺icon，1：商品icon
     */
    private Integer linkType;
    /**
     * 是否默认：0：否；1：是
     */
    private Integer isDefault = 0;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public Integer getLinkType() {
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

}
