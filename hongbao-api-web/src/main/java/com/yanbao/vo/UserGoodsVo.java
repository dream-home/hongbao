package com.yanbao.vo;

import com.yanbao.core.model.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商铺商品Vo
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class UserGoodsVo implements Serializable {

    private static final long serialVersionUID = 5898754554769429761L;
    /**
     * id
     */
    private String id;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String icon;
    /**
     * 商品介绍
     */
    private String detail;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 当前期数编号
     */
    private Integer curIssueNo;
    /**
     * 当前期数Id
     */
    private String curIssueId;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 竞拍价
     */
    private Double drawPrice;
    /**
     * 参与竞拍人数
     */
    private Integer drawNum;
    /**
     * 当前竞拍人数
     */
    private Integer curNum;
    /**
     * 已参加竞拍
     */
    private Integer isDraw = 0;
    /**
     * 是否委托出售：0：否，1：是
     */
    private Integer saleSwitch;
    /**
     * 状态：0：上架，1：下架
     */
    protected Integer status;
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
     * 参与记录
     */
    private List<DrawUserVo> drawUsers;
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
     * EP折扣比例
     */
    private Double discountEP = 0d;
    /**
     * 商家赠送Ep
     */
    private Double businessSendEp = 0d;
    /**
     * 商品分类类型id
     */
    private String goodsSortId;
    /**
     * 商品原价
     */
    private Double originalPrice;
    /**
     * 删除图片列表
     */
    private List<String> delIcons;
    /**
     * 命名空间
     */
    private String bucket;

    /**
     * 累计销售商品数量
     */
    private Integer sumSaleCount;

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

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public List<DrawUserVo> getDrawUsers() {
        return drawUsers;
    }

    public void setDrawUsers(List<DrawUserVo> drawUsers) {
        this.drawUsers = drawUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCurIssueNo() {
        return curIssueNo;
    }

    public void setCurIssueNo(Integer curIssueNo) {
        this.curIssueNo = curIssueNo;
    }

    public String getCurIssueId() {
        return curIssueId;
    }

    public void setCurIssueId(String curIssueId) {
        this.curIssueId = curIssueId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDrawPrice() {
        return drawPrice;
    }

    public void setDrawPrice(Double drawPrice) {
        this.drawPrice = drawPrice;
    }

    public Integer getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(Integer drawNum) {
        this.drawNum = drawNum;
    }

    public Integer getCurNum() {
        return curNum;
    }

    public void setCurNum(Integer curNum) {
        this.curNum = curNum;
    }

    public Integer getIsDraw() {
        return isDraw;
    }

    public void setIsDraw(Integer isDraw) {
        this.isDraw = isDraw;
    }

    public Integer getSaleSwitch() {
        return saleSwitch;
    }

    public void setSaleSwitch(Integer saleSwitch) {
        this.saleSwitch = saleSwitch;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Double getBusinessSendEp() {
        return businessSendEp;
    }

    public void setBusinessSendEp(Double businessSendEp) {
        this.businessSendEp = businessSendEp;
    }

    public String getGoodsSortId() {
        return goodsSortId;
    }

    public void setGoodsSortId(String goodsSortId) {
        this.goodsSortId = goodsSortId;
    }

    public List<ImageVo> getIcons() {
        return icons;
    }

    public void setIcons(List<ImageVo> icons) {
        this.icons = icons;
    }

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
    }

    public Integer getSumSaleCount() {
        return sumSaleCount;
    }

    public void setSumSaleCount(Integer sumSaleCount) {
        this.sumSaleCount = sumSaleCount;
    }
}
