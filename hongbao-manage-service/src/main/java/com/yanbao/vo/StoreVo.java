package com.yanbao.vo;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2017-01-11:16:05;
 */
public class StoreVo extends BaseVo {
    /** 商品分类ID */
    private String userId;
    /** 商铺名称 */
    private String storeName;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 县 */
    private String county;
    /** 详情地址 */
    private String addr;
    /** 经营类别 */
    private String businessScope;
    /** 商铺邀请码 */
    private String inviteCode;
    
    /** Uid */
    private Integer uid;
    
    private String phone;

    private String userName;

    private Integer collectNum;

    private Integer isRecommend;
    
    private String qrcodeUrl;
    
    private String shareUrl;
    
    private String storeUrl;
    
    private String imUrl;
    
    private Integer weixinStatus;

    private String menuUrl;
    /** 默认图片 */
    private String icon;

    private String detail;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getStoreUrl() {
		if(null==storeUrl||"".equals(storeUrl)){
			if(null!=menuUrl&&menuUrl.contains("Store")){
				Map<String,String> map=(Map<String,String>)com.alibaba.fastjson.JSONObject.parse(menuUrl);
				storeUrl=map.get("Store");
			}
		}
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public String getImUrl() {
		if(null==imUrl||"".equals(imUrl)){
			if(null!=menuUrl&&menuUrl.contains("Im")){
				Map<String,String> map=(Map<String,String>)com.alibaba.fastjson.JSONObject.parse(menuUrl);
				imUrl=map.get("Im");
			}
		}
		return imUrl;
	}

	public void setImUrl(String imUrl) {
		this.imUrl = imUrl;
	}

	public Integer getWeixinStatus() {
		return weixinStatus;
	}

	public void setWeixinStatus(Integer weixinStatus) {
		this.weixinStatus = weixinStatus;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
    
}
