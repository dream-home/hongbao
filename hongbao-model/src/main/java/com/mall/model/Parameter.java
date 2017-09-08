package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends SimpleModel implements Comparable<Parameter>{

    private static final long serialVersionUID = -76937973371276572L;
    /**
     * 最小中奖积分
     */
    public static final String DRAWMINSCORE = "drawMinScore";
    /**
     * 幸运中拍奖金比例上限
     */
    public static final String WINNERSCALEMAX = "winnerScaleMax";
    /**
     * 幸运中拍奖金比例下限
     */
    public static final String WINNERSCALEMIN = "winnerScaleMin";
    /**
     * 系统分销比例
     */
    public static final String SYSTEMPOUNDAGESCALE = "systemPoundageScale";
    /**
     * 斗拍一级分销
     */
    public static final String FIRSTREFERRERSCALE = "firstReferrerScale";
    /**
     * 斗拍二级分销
     */
    public static final String SECONDREFERRERSCALE = "secondReferrerScale";
    /**
     * 斗拍三级分销
     */
    public static final String THIRDREFERRERSCALE = "thirdReferrerScale";
    /**
     * 最大开奖人数
     */
    public static final String DRAWNUMMAX = "drawNumMax";
    /**
     * 最小开奖人数
     */
    public static final String DRAWNUMMIN = "drawNumMin";
    /**
     * 排行榜上榜数
     */
    public static final String WINNERLISTNUM = "winnerListNum";
    /**
     * 转账（捐赠）开关：0：禁用，1：启用
     **/
    public static final String DONATESWICH = "donateSwich";
    /**
     * 单次转账（捐赠）最大值
     */
    public static final String DONATEMAX = "donateMax";
    /**
     * 单次转账（捐赠）最小值
     */
    public static final String DONATEMIN = "donateMin";
    /**
     * 转账手续费
     */
    public static final String DONATEPOUNDAGESCALE = "donatePoundageScale";

    /**
     * 积分兑换（提现）开关：0：禁用，1：启用
     */
    public static final String EXCHANGESWITCH = "exchangeSwitch";
    /**
     * 每天提现次数限制
     */
    public static final String EXCHANGTIMES = "exchangeTimes";
    /**
     * 单次兑换（提现）最大值
     */
    public static final String EXCHANGEMAX = "exchangeMax";
    /**
     * 单次兑换（提现）最小值
     */
    public static final String EXCHANGEMIN = "exchangeMin";
    /**
     * 兑换手续费
     */
    public static final String EXCHANGEPOUNDAGESCALE = "exchangePoundageScale";
    /**
     * 开店条件：默认100积分
     */
    public static final String CREATESTORECONDITION = "createStoreCondition";
    /**
     * 商铺商品最大库存
     */
    public static final String STORESTOCKMAX = "storeStockMax";
    /**
     * 商铺发布商品数量限制
     */
    public static final String STOREGOODSMAX = "storeGoodsMax";
    /**
     * 邀请开关：0：禁用，1：启用
     */
    public static final String INVITESWICH = "inviteSwich";
    /**
     * VIP用户中奖概率
     */
    public static final String VIPWINSCALE = "vipWinScale";
    /**
     * 最新安卓客户端版本号
     */
    public static final String ANDROIDAPPVERSION = "androidAppVersion";
    /**
     * 最新安卓客户端下载地址
     */
    public static final String ANDROIDAPPURL = "androidAppUrl";
    /**
     * 最新安卓客户端更新说明
     */
    public static final String ANDROIDAPPDETAIL = "androidAppDetail";
    /**
     * 安卓客户端是否强制更新：0：否，1：是
     */
    public static final String ANDROIDAPPFORCEUPDATE = "androidAppForceUpdate";
    /**
     * 最新ios客户端版本号
     */
    public static final String IOSAPPVERSION = "iosAppVersion";
    /**
     * 最新ios客户端下载地址
     */
    public static final String IOSAPPURL = "iosAppUrl";
    /**
     * 最新ios客户端更新说明
     */
    public static final String IOSAPPDETAIL = "iosAppDetail";
    /**
     * ios客户端是否强制更新：0：否，1：是
     */
    public static final String IOSAPPFORCEUPDATE = "iosAppForceUpdate";
    /**
     * 系统版权
     */
    public static final String SYSTEMCOPYRIGHT = "systemCopyright";
    /**
     * 系统官方域名
     */
    public static final String SYSTEMDOMAIN = "systemDomain";
    /**
     * 查询层级
     */
    public static final String LEVELNO = "levelNo";
    /**
     * 注册赠送ep
     */
    public static final String REGISTEREP = "registerEP";
    /**
     * 邀请人赠送ep
     */
    public static final String INVITEREP = "inviterEP";
    /**
     * 绑定者赠送ep
     */
    public static final String BINDEP = "bindEP";
    /**
     * mail推送人
     */
    public static final String MAILPEOPLE = "mailPeople";
    /**
     * 分享标题
     */
    public static final String SHARETITLE = "shareTitle";
    /**
     * 区域抽佣比例
     */
    public static final String TRADERATE = "tradeRate";
    /**
     * 分享内容
     */
    public static final String SHAREMESSAGE = "shareMessage";
    /**
     * EP销售业绩比例
     */
    public static final String EPSCALE = "EPScale";
    /**
     * 销售业绩比例
     */
    public static final String BALANCESCALE = "balanceScale";
    /**
     * 代理结算日期
     */
    public static final String BILLDAY = "billDay";
    /**
     * 加入合伙人的金额
     */
    public static final String JOINEP = "joinEp";
    /**
     * 加入合伙人一级分销比例
     */
    public static final String JOINFIRSTREFERRERSCALE = "joinFirstReferrerScale";
    /**
     * 加入合伙人二级分销比例
     */
    public static final String JOINSECONDREFERRERSCALE = "joinSecondReferrerScale";
    /**
     * 加入合伙人三级分销比例
     */
    public static final String JOINTHIRDREFERRERSCALE = "joinThirdReferrerScale";
    /**
     * 引导页标致
     */
    public static final String GUIDEFLAG = "guideFlag";
    /**
     * ep兑换成斗斗比例
     */
    public static final String EPTODOUSCALE = "EPToDouScale";
    /**
     * ep兑换最小值
     */
    public static final String MINEPCONVERTNUM = "minEPConvertNum";
    /**
     * ep兑换最大值
     */
    public static final String MAXEPCONVERTNUM = "maxEPConvertNum";
    /**
     * 普通会员释放比例
     */
    public static final String COMMONRELEASESCALE = "commonReleaseScale";
    /**
     * VIP会员释放比例
     */
    public static final String VIPRELEASESCALE = "vipReleaseScale";
    /**
     * 斗斗签到最小值
     */
    public static final String MINSIGNDOUNUM = "minSignDouNum";
    /**
     * ep开关：on  off
     */
    public static final String EPSWITCH = "epSwitch";
    /**
     * 加入合伙人时EP兑换成斗斗比例
     */
    public static final String JOINTODOUSCALE = "joinToDouScale";
    /**
     * 公司客服电话，为空就不显示
     */
    public static final String SERVICEPHONE = "servicePhone";
    /**
     * 安卓是否弹窗
     */
    public static final String ISANDROIDPOPUP = "isAndroidPopup";
    /**
     * IOS是否弹窗
     */
    public static final String ISIOSPOPUP = "isIOSPopup";
    /**
     * 注册赠送斗斗
     */
    public static final String REGISTERDOUDOU = "registerDoudou";
    /**
     * 邀请人赠送斗斗
     */
    public static final String INVITERDOUDOU = "inviterDoudou";
    /**
     * 累计最多赠送斗斗
     */
    public static final String BINDDOUDOU = "bindDoudou";
    /**
     * 备注
     */
    public static final String REMARK = "remark";
    /**
     * 省代销售业绩提成比例
     */
    public static final String PROVINCEBALANCESCALE = "provinceBalanceScale";
    /**
     * 市代销售业绩提成比例
     */
    public static final String CITYBALANCESCALE = "cityBalanceScale";
    /**
     * 县代销售业绩提成比例
     */
    public static final String COUNTRYBALANCESCALE = "countryBalanceScale";
    /**
     * 省代EP业绩提成比例
     */
    public static final String PROVINCEEPSCALE = "provinceEPScale";
    /**
     * 市代EP业绩提成比例
     */
    public static final String CITYEPSCALE = "cityEPScale";
    /**
     * 县代EP业绩提成比例
     */
    public static final String COUNTRYEPSCALE = "countryEPScale";

    /**
     * EP转账 开关：0：禁用，1：启用
     **/
    public static final String DONATEEPSWICH = "donateEPSwich";
    /**
     * 单次EP转账 最大值
     */
    public static final String DONATEEPMAX = "donateEPMax";
    /**
     * 单次EP转账 最小值
     */
    public static final String DONATEEPMIN = "donateEPMin";
    /**
     * 转账EP手续费
     */
    public static final String DONATEEPPOUNDAGESCALE = "donateEPPoundageScale";
    /**
     * 加入合伙人60业绩累加值
     */
    public static final String JOINPERFORMANCE = "joinPerformance";
    /**
     * 加入合伙人最小现金比例（默认50%)
     */
    public static final String JOINRMBSCALE = "joinRmbScale";
    /**
     * 余额提现方式:1： 民生银行 2：德衫银行 3：微信
     */
    public static final String EXCHANGEWAY = "exchangeWay";
    /**
     *  是否要实名认证
     */
    public static final String ISWXCHECKUSERNAME = "isWxCheckUserName";



    private String name;
    private String value;
    private String kind;
    private String title;
    private String remark;
    private String groupType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }


    public int compareTo(Parameter arg0) {
        return this.getCreateTime().compareTo(arg0.getCreateTime());
    }


}
