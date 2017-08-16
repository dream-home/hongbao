package com.yanbao.vo;

/**
 * Created by summer on 2016-12-15:18:17;
 */
public class GoodsIssueVo {

    private String id;
    /** 商品ID */
    private String goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 期数Id */
    private String issueId;
    /** 期数编号 */
    private Integer issueNo;
    /** 商品价格 */
    private Double price;
    /** 竞拍价 */
    private Double drawPrice;
    /** 参与竞拍人数 */
    private Integer drawNum;
    /** 当前竞拍人数 */
    private Integer curNum;

    private Integer userCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Integer getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(Integer issueNo) {
        this.issueNo = issueNo;
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

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
