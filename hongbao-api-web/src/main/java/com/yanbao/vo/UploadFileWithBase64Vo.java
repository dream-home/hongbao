package com.yanbao.vo;

import java.util.List;

/**
 * Created by zzwei on 2017/5/19 0019.
 */
public class UploadFileWithBase64Vo {
    private  String bucket;

    private List<Base64ImgVo> iconList;

    private List<String> icons;

    public List<String> getIcons() {

        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public List<Base64ImgVo> getIconList() {
        return iconList;
    }

    public void setIconList(List<Base64ImgVo> iconList) {
        this.iconList = iconList;
    }
}
