package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * 
 * @author zhuzh
 * @date 2016年12月12日
 */
public class DrawUserVo implements Serializable {

	private static final long serialVersionUID = -8261886280598614119L;

	/** 昵称 */
	private String nickName;
	/** 头像地址 */
	private String headImgUrl;
	/** 创建时间 */
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date createTime;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
