package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 系统设置表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class SysSetting extends SimpleModel {

    private static final long serialVersionUID = -2718823113713989591L;
    /**
     * 最小中奖积分
     */
    private Double drawMinScore;
    /**
     * 幸运中拍奖金比例上限
     */
    private Double winnerScaleMax;
    /**
     * 幸运中拍奖金比例下限
     */
    private Double winnerScaleMin;
    /**
     * 系统分销比例
     */
    private Double systemPoundageScale;
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
     * 最大开奖人数
     */
    private Integer drawNumMax;
    /**
     * 最小开奖人数
     */
    private Integer drawNumMin;
    /**
     * 排行榜上榜数
     */
    private Integer winnerListNum;
    /**
     * 转账（捐赠）开关：0：禁用，1：启用
     */
    private Integer donateSwich;
    /**
     * 单次转账（捐赠）最大值
     */
    private Double donateMax;
    /**
     * 单次转账（捐赠）最小值
     */
    private Double donateMin;
    /**
     * 转账（捐赠）手续费比例
     */
    private Double donatePoundageScale;
    /**
     * 积分兑换（提现）开关：0：禁用，1：启用
     */
    private Integer exchangeSwitch;
    /**
     * 单次兑换（提现）最大值
     */
    private Double exchangeMax;
    /**
     * 单次兑换（提现）最小值
     */
    private Double exchangeMin;
    /**
     * 单次兑换（提现）手续费比例
     */
    private Double exchangePoundageScale;
    /**
     * 开店条件：默认100积分
     */
    private Double createStoreCondition;
    /**
     * 商铺商品最大库存
     */
    private Integer storeStockMax;
    /**
     * 商铺发布商品数量限制
     */
    private Integer storeGoodsMax;
    /**
     * 邀请开关：0：禁用，1：启用
     */
    private Integer inviteSwich;
    /**
     * VIP用户中奖概率
     */
    private Double vipWinScale;
    /**
     * 最新安卓客户端版本号
     */
    private String androidAppVersion;
    /**
     * 最新安卓客户端下载地址
     */
    private String androidAppUrl;
    /**
     * 最新安卓客户端更新说明
     */
    private String androidAppDetail;
    /**
     * 安卓客户端是否强制更新：0：否，1：是
     */
    private Integer androidAppForceUpdate;
    /**
     * 最新ios客户端版本号
     */
    private String iosAppVersion;
    /**
     * 最新ios客户端下载地址
     */
    private String iosAppUrl;
    /**
     * 最新ios客户端更新说明
     */
    private String iosAppDetail;
    /**
     * ios客户端是否强制更新：0：否，1：是
     */
    private Integer iosAppForceUpdate;
    /**
     * 系统版权
     */
    private String systemCopyright;
    /**
     * 系统官方域名
     */
    private String systemDomain;
    /**
     * 层级
     */
    private Integer levelNo;
    /**
     * 注册赠送ep
     */
    private Double registerEP;
    /**
     * 邀请人赠送ep
     */
    private Double inviterEP;
    /**
     * 累计最大赠送EP
     */
    private Double bindEP;

    /**
     * 邮箱推送人
     */
    private String mailPeople;
    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享内容
     */
    private String shareMessage;
    /**
     * 交易手续费
     */
    private Double tradeRate;
    /**
     * EP分销比例
     */
    private Double EPScale;
    /**
     * 营业额分销比例
     */
    private Double balanceScale;
    /**
     * 每月结算日
     */
    private Integer billDay;
    /**
     * 加入合伙人支付金额
     */
    private Double joinEp;
    /**
     * 一级分销
     */
    private Double joinFirstReferrerScale;
    /**
     * 二级分销
     */
    private Double joinSecondReferrerScale;
    /**
     * 三级分销
     */
    private Double joinThirdReferrerScale;

    /**
     * 引导标识
     */
    private String guideFlag;

    /**
     *省代销售业绩提成比例
     */
    private Double provinceBalanceScale;
    /**
     *市代销售业绩提成比例
     */
    private Double cityBalanceScale;
    /**
     *县代销售业绩提成比例
     */
    private Double countryBalanceScale;
    /**
     *省代EP业绩提成比例
     */
    private Double provinceEPScale;
    /**
     *市代EP业绩提成比例
     */
    private Double cityEPScale;
    /**
     *县代EP业绩提成比例
     */
    private Double countryEPScale;

    /**
     * 加入合伙人ep兑换成斗斗比例
     */
    private Double joinToDouScale;
    /**
     * ep兑换成斗斗比例
     */
    private Double EPToDouScale;
    /**
     * ep兑换最小值
     */
    private Double minEPConvertNum;
    /**
     * ep兑换最大值
     */
    private Double maxEPConvertNum;
    /**
     * 普通会员释放比例
     */
    private Double commonReleaseScale;
    /**
     * VIP会员释放比例
     */
    private Double vipReleaseScale;
    /**
     * 斗斗签到最小值
     */
    private Double minSignDouNum;
    /**
     * EP兑换开关
     */
    private String epSwitch;
    /**
     * 客服电话
     */
    private String servicePhone;
    /**
     * ios是否弹窗：0：否，1：是
     */
    private Integer isIOSPopup;
    /**
     * 安卓是否弹窗：0：否，1：是
     */
    private Integer isAndroidPopup;
    /**
     * 注册赠送斗斗
     */
    private Double registerDoudou;
    /**
     * 邀请人赠送斗斗
     */
    private Double inviterDoudou;
    /**
     * 累计最大赠送斗斗
     */
    private Double bindDoudou;

    public String getGuideFlag() {
        return guideFlag;
    }

    public void setGuideFlag(String guideFlag) {
        this.guideFlag = guideFlag;
    }

    public Double getEPScale() {
        return EPScale;
    }

    public void setEPScale(Double EPScale) {
        this.EPScale = EPScale;
    }

    public Double getBalanceScale() {
        return balanceScale;
    }

    public void setBalanceScale(Double balanceScale) {
        this.balanceScale = balanceScale;
    }

    public Integer getBillDay() {
        return billDay;
    }

    public void setBillDay(Integer billDay) {
        this.billDay = billDay;
    }

    public Double getJoinEp() {
        return joinEp;
    }

    public void setJoinEp(Double joinEp) {
        this.joinEp = joinEp;
    }

    public Double getJoinFirstReferrerScale() {
        return joinFirstReferrerScale;
    }

    public void setJoinFirstReferrerScale(Double joinFirstReferrerScale) {
        this.joinFirstReferrerScale = joinFirstReferrerScale;
    }

    public Double getJoinSecondReferrerScale() {
        return joinSecondReferrerScale;
    }

    public void setJoinSecondReferrerScale(Double joinSecondReferrerScale) {
        this.joinSecondReferrerScale = joinSecondReferrerScale;
    }

    public Double getJoinThirdReferrerScale() {
        return joinThirdReferrerScale;
    }

    public void setJoinThirdReferrerScale(Double joinThirdReferrerScale) {
        this.joinThirdReferrerScale = joinThirdReferrerScale;
    }

    public Double getTradeRate() {
        return tradeRate;
    }

    public void setTradeRate(Double tradeRate) {
        this.tradeRate = tradeRate;
    }

    public String getMailPeople() {
        return mailPeople;
    }

    public void setMailPeople(String mailPeople) {
        this.mailPeople = mailPeople;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }

    public Double getDrawMinScore() {
        return drawMinScore;
    }

    public void setDrawMinScore(Double drawMinScore) {
        this.drawMinScore = drawMinScore;
    }

    public Double getWinnerScaleMax() {
        return winnerScaleMax;
    }

    public void setWinnerScaleMax(Double winnerScaleMax) {
        this.winnerScaleMax = winnerScaleMax;
    }

    public Double getWinnerScaleMin() {
        return winnerScaleMin;
    }

    public void setWinnerScaleMin(Double winnerScaleMin) {
        this.winnerScaleMin = winnerScaleMin;
    }

    public Double getSystemPoundageScale() {
        return systemPoundageScale;
    }

    public void setSystemPoundageScale(Double systemPoundageScale) {
        this.systemPoundageScale = systemPoundageScale;
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

    public Integer getDrawNumMax() {
        return drawNumMax;
    }

    public void setDrawNumMax(Integer drawNumMax) {
        this.drawNumMax = drawNumMax;
    }

    public Integer getDrawNumMin() {
        return drawNumMin;
    }

    public void setDrawNumMin(Integer drawNumMin) {
        this.drawNumMin = drawNumMin;
    }

    public Integer getWinnerListNum() {
        return winnerListNum;
    }

    public void setWinnerListNum(Integer winnerListNum) {
        this.winnerListNum = winnerListNum;
    }

    public Integer getDonateSwich() {
        return donateSwich;
    }

    public void setDonateSwich(Integer donateSwich) {
        this.donateSwich = donateSwich;
    }

    public Double getDonateMax() {
        return donateMax;
    }

    public void setDonateMax(Double donateMax) {
        this.donateMax = donateMax;
    }

    public Double getDonateMin() {
        return donateMin;
    }

    public void setDonateMin(Double donateMin) {
        this.donateMin = donateMin;
    }

    public Double getDonatePoundageScale() {
        return donatePoundageScale;
    }

    public void setDonatePoundageScale(Double donatePoundageScale) {
        this.donatePoundageScale = donatePoundageScale;
    }

    public Integer getExchangeSwitch() {
        return exchangeSwitch;
    }

    public void setExchangeSwitch(Integer exchangeSwitch) {
        this.exchangeSwitch = exchangeSwitch;
    }

    public Double getExchangeMax() {
        return exchangeMax;
    }

    public void setExchangeMax(Double exchangeMax) {
        this.exchangeMax = exchangeMax;
    }

    public Double getExchangeMin() {
        return exchangeMin;
    }

    public void setExchangeMin(Double exchangeMin) {
        this.exchangeMin = exchangeMin;
    }

    public Double getExchangePoundageScale() {
        return exchangePoundageScale;
    }

    public void setExchangePoundageScale(Double exchangePoundageScale) {
        this.exchangePoundageScale = exchangePoundageScale;
    }

    public Double getCreateStoreCondition() {
        return createStoreCondition;
    }

    public void setCreateStoreCondition(Double createStoreCondition) {
        this.createStoreCondition = createStoreCondition;
    }

    public Integer getStoreStockMax() {
        return storeStockMax;
    }

    public void setStoreStockMax(Integer storeStockMax) {
        this.storeStockMax = storeStockMax;
    }

    public Integer getStoreGoodsMax() {
        return storeGoodsMax;
    }

    public void setStoreGoodsMax(Integer storeGoodsMax) {
        this.storeGoodsMax = storeGoodsMax;
    }

    public Integer getInviteSwich() {
        return inviteSwich;
    }

    public void setInviteSwich(Integer inviteSwich) {
        this.inviteSwich = inviteSwich;
    }

    public Double getVipWinScale() {
        return vipWinScale;
    }

    public void setVipWinScale(Double vipWinScale) {
        this.vipWinScale = vipWinScale;
    }

    public String getAndroidAppVersion() {
        return androidAppVersion;
    }

    public void setAndroidAppVersion(String androidAppVersion) {
        this.androidAppVersion = androidAppVersion;
    }

    public String getAndroidAppUrl() {
        return androidAppUrl;
    }

    public void setAndroidAppUrl(String androidAppUrl) {
        this.androidAppUrl = androidAppUrl;
    }

    public String getAndroidAppDetail() {
        return androidAppDetail;
    }

    public void setAndroidAppDetail(String androidAppDetail) {
        this.androidAppDetail = androidAppDetail;
    }

    public String getIosAppVersion() {
        return iosAppVersion;
    }

    public void setIosAppVersion(String iosAppVersion) {
        this.iosAppVersion = iosAppVersion;
    }

    public String getIosAppUrl() {
        return iosAppUrl;
    }

    public void setIosAppUrl(String iosAppUrl) {
        this.iosAppUrl = iosAppUrl;
    }

    public String getIosAppDetail() {
        return iosAppDetail;
    }

    public void setIosAppDetail(String iosAppDetail) {
        this.iosAppDetail = iosAppDetail;
    }

    public String getSystemCopyright() {
        return systemCopyright;
    }

    public void setSystemCopyright(String systemCopyright) {
        this.systemCopyright = systemCopyright;
    }

    public String getSystemDomain() {
        return systemDomain;
    }

    public void setSystemDomain(String systemDomain) {
        this.systemDomain = systemDomain;
    }

    public Integer getAndroidAppForceUpdate() {
        return androidAppForceUpdate;
    }

    public void setAndroidAppForceUpdate(Integer androidAppForceUpdate) {
        this.androidAppForceUpdate = androidAppForceUpdate;
    }

    public Integer getIosAppForceUpdate() {
        return iosAppForceUpdate;
    }

    public void setIosAppForceUpdate(Integer iosAppForceUpdate) {
        this.iosAppForceUpdate = iosAppForceUpdate;
    }

    public Integer getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }

    public Double getRegisterEP() {
        return registerEP;
    }

    public void setRegisterEP(Double registerEP) {
        this.registerEP = registerEP;
    }

    public Double getInviterEP() {
        return inviterEP;
    }

    public void setInviterEP(Double inviterEP) {
        this.inviterEP = inviterEP;
    }

    public Double getBindEP() {
        return bindEP;
    }

    public void setBindEP(Double bindEP) {
        this.bindEP = bindEP;
    }

    public Double getProvinceBalanceScale() {
        return provinceBalanceScale;
    }

    public void setProvinceBalanceScale(Double provinceBalanceScale) {
        this.provinceBalanceScale = provinceBalanceScale;
    }

    public Double getCityBalanceScale() {
        return cityBalanceScale;
    }

    public void setCityBalanceScale(Double cityBalanceScale) {
        this.cityBalanceScale = cityBalanceScale;
    }

    public Double getCountryBalanceScale() {
        return countryBalanceScale;
    }

    public void setCountryBalanceScale(Double countryBalanceScale) {
        this.countryBalanceScale = countryBalanceScale;
    }

    public Double getProvinceEPScale() {
        return provinceEPScale;
    }

    public void setProvinceEPScale(Double provinceEPScale) {
        this.provinceEPScale = provinceEPScale;
    }

    public Double getCityEPScale() {
        return cityEPScale;
    }

    public void setCityEPScale(Double cityEPScale) {
        this.cityEPScale = cityEPScale;
    }

    public Double getCountryEPScale() {
        return countryEPScale;
    }

    public void setCountryEPScale(Double countryEPScale) {
        this.countryEPScale = countryEPScale;
    }

    public Double getJoinToDouScale() {
        return joinToDouScale;
    }

    public void setJoinToDouScale(Double joinToDouScale) {
        this.joinToDouScale = joinToDouScale;
    }

    public Double getEPToDouScale() {
        return EPToDouScale;
    }

    public void setEPToDouScale(Double EPToDouScale) {
        this.EPToDouScale = EPToDouScale;
    }

    public Double getMinEPConvertNum() {
        return minEPConvertNum;
    }

    public void setMinEPConvertNum(Double minEPConvertNum) {
        this.minEPConvertNum = minEPConvertNum;
    }

    public Double getMaxEPConvertNum() {
        return maxEPConvertNum;
    }

    public void setMaxEPConvertNum(Double maxEPConvertNum) {
        this.maxEPConvertNum = maxEPConvertNum;
    }

    public Double getCommonReleaseScale() {
        return commonReleaseScale;
    }

    public void setCommonReleaseScale(Double commonReleaseScale) {
        this.commonReleaseScale = commonReleaseScale;
    }

    public Double getVipReleaseScale() {
        return vipReleaseScale;
    }

    public void setVipReleaseScale(Double vipReleaseScale) {
        this.vipReleaseScale = vipReleaseScale;
    }

    public Double getMinSignDouNum() {
        return minSignDouNum;
    }

    public void setMinSignDouNum(Double minSignDouNum) {
        this.minSignDouNum = minSignDouNum;
    }

    public String getEpSwitch() {
        return epSwitch;
    }

    public void setEpSwitch(String epSwitch) {
        this.epSwitch = epSwitch;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public Integer getIsIOSPopup() {
        return isIOSPopup;
    }

    public void setIsIOSPopup(Integer isIOSPopup) {
        this.isIOSPopup = isIOSPopup;
    }

    public Integer getIsAndroidPopup() {
        return isAndroidPopup;
    }

    public void setIsAndroidPopup(Integer isAndroidPopup) {
        this.isAndroidPopup = isAndroidPopup;
    }

    public Double getRegisterDoudou() {
        return registerDoudou;
    }

    public void setRegisterDoudou(Double registerDoudou) {
        this.registerDoudou = registerDoudou;
    }

    public Double getInviterDoudou() {
        return inviterDoudou;
    }

    public void setInviterDoudou(Double inviterDoudou) {
        this.inviterDoudou = inviterDoudou;
    }

    public Double getBindDoudou() {
        return bindDoudou;
    }

    public void setBindDoudou(Double bindDoudou) {
        this.bindDoudou = bindDoudou;
    }
}
