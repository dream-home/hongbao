package com.mall.model;

import org.springframework.util.StringUtils;

import com.yanbao.core.model.SimpleModel;
import com.yanbao.util.EmojiUtil;

/**
 * 商铺表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Store extends SimpleModel {

    private static final long serialVersionUID = 5898754554769429761L;
    /**
     * 商品分类ID
     */
    private String userId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 默认图片
     */
    private String icon;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 详情地址
     */
    private String addr;
    /**
     * 经营类别
     */
    private String businessScope;
    /**
     * 商铺邀请码
     */
    private String inviteCode;
    /**
     * 商铺介绍
     */
    private String detail;
    /**
     * 是否推荐
     */
    private Integer isRecommend;
    /**
     * 收藏数量
     */
    private Integer collectNum;
    /**
     * 一级分销比例
     */
    private Double firstReferrerScale;
    /**
     * 二级分销比例
     */
    private Double secondReferrerScale;
    /**
     * 三级分销比例
     */
    private Double thirdReferrerScale;
    /**
     * '客服电话
     */
    private String servicePhone;
    /**
     * '商家营业执照
     */
    private String storeLicense;
    /**
     * '身份证照id
     */
    private String IDCardIcon;
    /**
     * '审核失败原因
     */
    private String message;
    /**
     * '所属地区ID
     */
    private String areaId;
    /**
     * 省区ID
     */
    private String provinceCode;
    /**
     * 市区ID
     */
    private String cityCode;
    /**
     * 县区ID
     */
    private String countyCode;
    /**
     * '经度
     */
    private String longitude;
    /**
     * '纬度
     */
    private String latitude;
    /**
     * 累计销售商品数量
     */
    private String sumSaleCount;
    /** '二维码地址 */
    private String qrcodeUrl;
    /** 二维码内容地址 */
    private String shareUrl;
    /** 微信店铺URL  */
    private String storeUrl;
    /** 微信我的URL  */
    private String imUrl;
    /** 微信店铺状态  */
    private Integer weixinStatus;
    /** 我的+店铺URL  */
    private String menuUrl;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getDetail() {
        if (!StringUtils.isEmpty(detail)) {
            try {
                detail = EmojiUtil.filterEmoji(detail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return detail;
    }

    public void setDetail(String detail) {
        if (!StringUtils.isEmpty(detail)) {
            try {
                detail = EmojiUtil.filterEmoji(detail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.detail = detail;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Double getFirstReferrerScale() {
        return firstReferrerScale;
    }

    public void setFirstReferrerScale(Double firstReferrerScale) {
        this.firstReferrerScale = firstReferrerScale;
    }

    public Double getSecondReferrerScale() {
        return secondReferrerScale;
    }

    public void setSecondReferrerScale(Double secondReferrerScale) {
        this.secondReferrerScale = secondReferrerScale;
    }

    public Double getThirdReferrerScale() {
        return thirdReferrerScale;
    }

    public void setThirdReferrerScale(Double thirdReferrerScale) {
        this.thirdReferrerScale = thirdReferrerScale;
    }


    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getStoreLicense() {
        return storeLicense;
    }

    public void setStoreLicense(String storeLicense) {
        this.storeLicense = storeLicense;
    }

    public String getIDCardIcon() {
        return IDCardIcon;
    }

    public void setIDCardIcon(String IDCardIcon) {
        this.IDCardIcon = IDCardIcon;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSumSaleCount() {
        return sumSaleCount;
    }

    public void setSumSaleCount(String sumSaleCount) {
        this.sumSaleCount = sumSaleCount;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
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
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public String getImUrl() {
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
    
}
