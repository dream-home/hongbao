package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 概率用户表
 *
 * @author zhuzh
 * @date 2017年2月13日
 */
public class UserVip extends SimpleModel {

    private static final long serialVersionUID = 4440654314398185244L;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * UID
     */
    private Integer uid;
    /**
     * 真实姓名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
