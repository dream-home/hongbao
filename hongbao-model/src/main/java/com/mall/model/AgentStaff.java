package com.mall.model;

import com.yanbao.core.model.SimpleModel;



/**
 * @author Pay - 1091945691@qq.com
 */
public class AgentStaff extends SimpleModel implements java.io.Serializable {
	
	/**类的版本号*/
	private static final long serialVersionUID = 2224420628531200L;

	private java.lang.String id;	/** UID */	private java.lang.Integer uid;	/** 代理商家编号 */	private java.lang.String agentId;	/** 昵称 */	private java.lang.String nickName;	/** 性别：0：男，1：女，2：保密 */	private java.lang.Integer sex;	/** 头像地址 */	private java.lang.String headImgUrl;	/** 帐号 */	private java.lang.String loginName;	/** 密码md5+salt */	private java.lang.String password;	/** 支付密码 */	private java.lang.String payPassWord;	/** 盐 */	private java.lang.String salt;	/** 手机号 */	private java.lang.String phone;	/** 邮箱 */	private java.lang.String email;	/** 真实姓名 */	private java.lang.String userName;	/** 菜单权限 */	private java.lang.String menuPermissions;	/** 地址 */	private java.lang.String address;	/** 身份证照片id */	private java.lang.String cardIconId;	/** 登录时间 */	private java.util.Date loginTime;	/** 错误次数 */	private java.lang.Integer errorCount;	/** 创建时间 */	private java.util.Date createTime;	/** 修改时间 */	private java.util.Date updateTime;	/** 状态：0：审核中，1：审核通过，2：审核不通过，3：关闭 */	private java.lang.Integer status;	/** 备注 */	private java.lang.String remark;	public java.lang.String getId() {	    return this.id;	}	public void setId(java.lang.String id) {	    this.id=id;	}	public java.lang.Integer getUid() {	    return this.uid;	}	public void setUid(java.lang.Integer uid) {	    this.uid=uid;	}	public java.lang.String getAgentId() {	    return this.agentId;	}	public void setAgentId(java.lang.String agentId) {	    this.agentId=agentId;	}	public java.lang.String getNickName() {	    return this.nickName;	}	public void setNickName(java.lang.String nickName) {	    this.nickName=nickName;	}	public java.lang.Integer getSex() {	    return this.sex;	}	public void setSex(java.lang.Integer sex) {	    this.sex=sex;	}	public java.lang.String getHeadImgUrl() {	    return this.headImgUrl;	}	public void setHeadImgUrl(java.lang.String headImgUrl) {	    this.headImgUrl=headImgUrl;	}	public java.lang.String getLoginName() {	    return this.loginName;	}	public void setLoginName(java.lang.String loginName) {	    this.loginName=loginName;	}	public java.lang.String getPassword() {	    return this.password;	}	public void setPassword(java.lang.String password) {	    this.password=password;	}	public java.lang.String getPayPassWord() {	    return this.payPassWord;	}	public void setPayPassWord(java.lang.String payPassWord) {	    this.payPassWord=payPassWord;	}	public java.lang.String getSalt() {	    return this.salt;	}	public void setSalt(java.lang.String salt) {	    this.salt=salt;	}	public java.lang.String getPhone() {	    return this.phone;	}	public void setPhone(java.lang.String phone) {	    this.phone=phone;	}	public java.lang.String getEmail() {	    return this.email;	}	public void setEmail(java.lang.String email) {	    this.email=email;	}	public java.lang.String getUserName() {	    return this.userName;	}	public void setUserName(java.lang.String userName) {	    this.userName=userName;	}	public java.lang.String getMenuPermissions() {	    return this.menuPermissions;	}	public void setMenuPermissions(java.lang.String menuPermissions) {	    this.menuPermissions=menuPermissions;	}	public java.lang.String getAddress() {	    return this.address;	}	public void setAddress(java.lang.String address) {	    this.address=address;	}	public java.lang.String getCardIconId() {	    return this.cardIconId;	}	public void setCardIconId(java.lang.String cardIconId) {	    this.cardIconId=cardIconId;	}	public java.util.Date getLoginTime() {	    return this.loginTime;	}	public void setLoginTime(java.util.Date loginTime) {	    this.loginTime=loginTime;	}	public java.lang.Integer getErrorCount() {	    return this.errorCount;	}	public void setErrorCount(java.lang.Integer errorCount) {	    this.errorCount=errorCount;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public java.lang.Integer getStatus() {	    return this.status;	}	public void setStatus(java.lang.Integer status) {	    this.status=status;	}	public java.lang.String getRemark() {	    return this.remark;	}	public void setRemark(java.lang.String remark) {	    this.remark=remark;	}	public String toString() {	    return "[" + "id:" + getId() +"," + "uid:" + getUid() +"," + "agentId:" + getAgentId() +"," + "nickName:" + getNickName() +"," + "sex:" + getSex() +"," + "headImgUrl:" + getHeadImgUrl() +"," + "loginName:" + getLoginName() +"," + "password:" + getPassword() +"," + "payPassWord:" + getPayPassWord() +"," + "salt:" + getSalt() +"," + "phone:" + getPhone() +"," + "email:" + getEmail() +"," + "userName:" + getUserName() +"," + "menuPermissions:" + getMenuPermissions() +"," + "address:" + getAddress() +"," + "cardIconId:" + getCardIconId() +"," + "loginTime:" + getLoginTime() +"," + "errorCount:" + getErrorCount() +"," + "createTime:" + getCreateTime() +"," + "updateTime:" + getUpdateTime() +"," + "status:" + getStatus() +"," + "remark:" + getRemark() +"]";	}
}