package com.yanbao.vo;

/**
 * Created by zzwei on 2017/7/12 0012.
 */
public class DistanceVo {
    private  String storeId;
    private  Double longitude;
    private  Double latitude;
    /**
     * 地址id
     */
    private String addressId;
    /**
     * 地址
     */
    private String address;
    /**
     * 未登录用户标识
     */
    private String serialNumber;
    /**
     * 距离
     */
    private Double distance;
    /**
     * 搜索关键字
     */
    private String searchWord;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
}
