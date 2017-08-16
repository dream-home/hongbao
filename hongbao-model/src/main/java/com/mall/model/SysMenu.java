package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * @author zyc  2017-03-23 15:03
 */
public class SysMenu extends SimpleModel {


    //菜单的url
    private String url;
    //菜单名称
    private String name;
    //父类的编号
    private Integer lastNo;
    //备注
    private String remark;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLastNo() {
        return lastNo;
    }

    public void setLastNo(Integer lastNo) {
        this.lastNo = lastNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
