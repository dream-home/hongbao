package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * 后台管理员
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Admin extends SimpleModel {

    private static final long serialVersionUID = -907855895577266412L;
    /**
     * 帐号
     */
    private String account;
    /**
     * 密码md5+salt
     */
    private String password;
    /**
     * 微信openId
     */
    private String weixin;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 真实姓名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 头像地址
     */
    private String headImgUrl;
    /**
     * 盐
     */
    private String salt;

    private Integer flag;
    //角色id;
    private Integer role;
    //交易密码
    private String payPassWord;


    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPayPassWord() {
        return payPassWord;
    }

    public void setPayPassWord(String payPassWord) {
        this.payPassWord = payPassWord;
    }
}
