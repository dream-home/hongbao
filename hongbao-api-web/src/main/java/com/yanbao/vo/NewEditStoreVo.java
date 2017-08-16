package com.yanbao.vo;

import com.yanbao.util.EmojiUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 商铺Vo
 *
 * @author zzwei
 * @date 2017年06月28日
 */
public class NewEditStoreVo implements Serializable {

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
    /**
     * 详情地址
     */
    private String addr;
    /**
     * 经营类别
     */
    private String businessScope;
    /**
     * 商铺介绍
     */
    private String detail;
    /**
     * 图片列表
     */
    private List<ImageVo> icons;
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
     * 命名空间
     */
    private String bucket;
    /**
     * 删除图片列表
     */
    private List<String> delIcons;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 代理id
     *
     */
    private String areaId;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public List<String> getDelIcons() {
        return delIcons;
    }

    public void setDelIcons(List<String> delIcons) {
        this.delIcons = delIcons;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
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

    public List<ImageVo> getIcons() {
        return icons;
    }

    public void setIcons(List<ImageVo> icons) {
        this.icons = icons;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
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
