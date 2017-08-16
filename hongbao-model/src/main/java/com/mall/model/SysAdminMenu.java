package com.mall.model;

import com.yanbao.core.model.SimpleModel;


/**
 * @author zyc  2017-03-23 15:14
 */


public class SysAdminMenu extends SimpleModel {


    /**
     * 用户角色id;
     */
    private Integer roleId;


    /**
     * 菜单id;
     */
    private String menuId;


    /**
     * 父级菜单
     */
    private Integer lastNo;


    public Integer getLastNo() {
        return lastNo;
    }


    public void setLastNo(Integer lastNo) {
        this.lastNo = lastNo;
    }


    public Integer getRoleId() {
        return roleId;
    }


    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    public String getMenuId() {
        return menuId;
    }


    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }


}
