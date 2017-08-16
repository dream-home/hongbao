package com.mall.model;

import java.util.Date;

import com.yanbao.core.model.SimpleModel;

/**
 * 用户反馈表
 *
 * @author zzwei
 * @date 201&年06月27日
 */
public class FeedBack extends SimpleModel {

    private static final long serialVersionUID = -6966234808156525194L;
    /**
     * 反馈用户Id
     */
    private String userId;
    /**
     * 反馈用户Id
     */
    private String uid;
    /**
     * 反馈用户Id
     */
    private String nickName;
    /**
     * 反馈用户Id
     */
    private String phone;
    /**
     * 分类标题
     */
    private String title;
    /**
     * 分类类型
     */
    private Integer type;
    /**
     * 反馈描述
     */
    private String detail;
    /**
     * 反馈图片
     */
    private String icons;
    
    private Date fromTime;
    
    private Date stopTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
    
}
