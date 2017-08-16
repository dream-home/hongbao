package com.mall.model;

import com.yanbao.core.model.SimpleModel;


/***
 *
 * @author zyc 2071-05-13  10:44
 * 三级联表（省市区） 
 *
 */


public class City extends SimpleModel {

    private String code;
    private String name;
    private String parentCode;
    private String shortName;
    private Integer level;
    private String areaCode;
    private String zipCode;
    private String zhName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }


}
