package com.yanbao.vo;

import com.yanbao.core.model.CustomDateSerializer;
import com.yanbao.util.EmojiUtil;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商铺Vo
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class StoreVo implements Serializable {

    private static final long serialVersionUID = 5898754554769429761L;
    /**
     * id
     */
    private String id;
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
     * 一级分销比例
     */
    private Double firstReferrerScale = 0d;
    /**
     * 二级分销比例
     */
    private Double secondReferrerScale = 0d;
    /**
     * 三级分销比例
     */
    private Double thirdReferrerScale = 0d;
    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    protected Date createTime;
    /**
     * 图片列表
     */
    private List<ImageVo> icons;
    /**
     * 是否收藏：0：否，1：是
     */
    private Integer isCollect = 0;
    /**
     * 是否有邀请码：0：否，1：是
     */
    private Integer hasInviteCode = 0;
    /**
     * 店铺联系电话
     */
    private String phone;
    /**
     * 客服电话
     */
    private String servicePhone;
    /**
     * 营业执照图片列表
     */
    private List<ImageVo> licenseIcons;
    /**
     * 身份证图片列表
     */
    private List<ImageVo> idcardIcons;
    /**
     * 距离
     */
    private Double distance=0d;

    /**
     * 距离
     */
    private String distanceRank;
    /**
     * 累计销售商品数量
     */
    private String sumSaleCount;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    
    /**
     * 省
     */
    private String provinceCode;
    /**
     * 市
     */
    private String cityCode;
    /**
     * 县
     */
    private String countyCode;
    /** '二维码地址 */
    private String qrcodeUrl;
    /** 二维码内容地址 */
    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ImageVo> getIcons() {
        return icons;
    }

    public void setIcons(List<ImageVo> icons) {
        this.icons = icons;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getHasInviteCode() {
        return hasInviteCode;
    }

    public void setHasInviteCode(Integer hasInviteCode) {
        this.hasInviteCode = hasInviteCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public List<ImageVo> getLicenseIcons() {
        return licenseIcons;
    }

    public void setLicenseIcons(List<ImageVo> licenseIcons) {
        this.licenseIcons = licenseIcons;
    }

    public List<ImageVo> getIdcardIcons() {
        return idcardIcons;
    }

    public void setIdcardIcons(List<ImageVo> idcardIcons) {
        this.idcardIcons = idcardIcons;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getSumSaleCount() {
        return sumSaleCount;
    }

    public void setSumSaleCount(String sumSaleCount) {
        this.sumSaleCount = sumSaleCount;
    }

    public String getDistanceRank() {
        return distanceRank;
    }

    public void setDistanceRank(String distanceRank) {
        this.distanceRank = distanceRank;
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
    
    
}
