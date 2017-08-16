package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.RechargeType;
import com.yanbao.dao.*;
import com.yanbao.service.IncomeStatisticsService;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author YIYIRUI
 * @date 2017年06月05日
 */
@Service
public class IncomeStatisticsServiceImpl implements IncomeStatisticsService {
	
	 public static Log log = LogFactory.getLog(IncomeStatisticsServiceImpl.class);

	@Autowired
	private StoreDao storeDao;
	@Autowired
    private WalletRechargeDao walletRechargeDao;
	@Autowired
	private GoodsWinDao goodsWinDao;
	@Autowired
	private WalletBillDao walletBillDao;
	@Autowired
	private WalletBillDetailDao walletBillDetailDao;
	
	public List<String> allStoreids=new ArrayList<String>();
	public final boolean iscomplete=false;
	
	@Override
	public List<WalletBill> getAllinfo() throws Exception {
		List<WalletBill> list=walletBillDao.getList(new WalletBill());
		Map<String,WalletBill> map=new HashMap<String,WalletBill>();
		for (WalletBill walletBill : list) {
			map.put(walletBill.getOrderNo(), walletBill);
		}
		List<WalletBillDetail> dlist=walletBillDetailDao.getList(new WalletBillDetail());
		for (WalletBillDetail walletBillDetail : dlist) {
			WalletBill walletBill=map.get(walletBillDetail.getOrderNo());
			List<WalletBillDetail> bdlist=walletBill.getDetail();
			if(null==bdlist){
				walletBill.setDetail(new ArrayList<WalletBillDetail>());
			}else{
				bdlist.add(walletBillDetail);
			}
		}
		return list;
	}
	
	
	//开始统计方法
	@Override
	public List<WalletBill> IncomeStatistics(Date starttime,Date endtime) throws Exception {
		//获取系统设置
		ParamUtil util = ParamUtil.getIstance();
		//调用所有统计
		List<Map<String, WalletBillDetail>>  allWalletBillDetail=getallWalletBill(starttime, endtime);
		//获取所有的有数据的商铺
		List<Store> allStore=new ArrayList<Store>();
		if(allStoreids.size()>0){
			allStore=storeDao.getCollectList(allStoreids);
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		List<WalletBillDetail> list=new ArrayList<WalletBillDetail>();
		List<WalletBill> resultList=new ArrayList<WalletBill>();
		double epScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.EPSCALE), 0d),100,4);
		double balanceScale = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.BALANCESCALE), 0d),100,4);
		if(allStore.size()>0){
			for (Store store : allStore) {
				WalletBill wb=new WalletBill();
				wb.setId(UUIDUtil.getUUID());
				wb.setUserId(store.getUserId());
				wb.setStoreId(store.getId());
				wb.setType("0");
				wb.setOrderNo(UUIDUtil.getUUID());
				wb.setEP(0d);
				wb.setEPScale(epScale);
				wb.setBalance(0d);
				wb.setBalanceScale(balanceScale);
				wb.setCreateTime(now);
				wb.setUpdateTime(now);
				wb.setRemark(sdf.format(starttime)+"_"+sdf.format(endtime));
				wb.setBillday(sdf2.format(endtime));
				for (Map<String, WalletBillDetail> map : allWalletBillDetail) {
					if(null!=map.get(store.getId())){
						WalletBillDetail wbd=map.get(store.getId());
						wbd.setUserId(store.getUserId());
						wbd.setStoreId(store.getId());
						wbd.setOrderNo(wb.getOrderNo());
						wbd.setEPScale(epScale);
						wbd.setBalanceScale(balanceScale);
						wbd.setCreateTime(now);
						wbd.setUpdateTime(now);
						wbd.setBillday(sdf2.format(endtime));
						wb.setEP(wb.getEP()+wbd.getEP());
						wb.setBalance(wb.getBalance()+wbd.getBalance());
						List<WalletBillDetail> details=wb.getDetail();
						if(null==details){
							wb.setDetail(new ArrayList<WalletBillDetail>());
						}else{
							details.add(wbd);
						}
						list.add(wbd);
					}
				}
				walletBillDao.add(wb);
				resultList.add(wb);
			}
		}
		//线程添加数据
		addInfo(walletBillDetailDao, list);
		//清除所有的ID
		allStoreids.clear();
		return resultList;
	}
	  
	//线程添加统计数据
	private void addInfo(final WalletBillDetailDao dao,final List<WalletBillDetail> list){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//记录错误的记录
				String errorIds="";
				for (WalletBillDetail walletBillDetail : list) {
					walletBillDetail.setId(UUIDUtil.getUUID());
					try {
						dao.add(walletBillDetail);
					} catch (Exception e) {
						e.printStackTrace();
						errorIds+=walletBillDetail.getStoreId()+"_"+walletBillDetail.getRemark()+",";
						continue;
					}
				}
				if(!"".equals(errorIds)){
					log.info("Statistics ErrorIDs:"+errorIds);
				}
			}
		}).start();
	}	
	//调用所有统计方法
	@SuppressWarnings("unchecked")
	public List<Map<String, WalletBillDetail>> getallWalletBill(Date starttime,Date endtime){
		//组装数据集合
		List<Map<String, WalletBillDetail>> resultList=new ArrayList<Map<String,WalletBillDetail>>();
		//调用统计查询
		Map<String, Object> h5Wechat_map=h5Wechat(starttime, endtime);
		if(null!=h5Wechat_map){
			Map<String, WalletBillDetail> h5_WalletMap=(Map<String, WalletBillDetail>)h5Wechat_map.get(WalletBillDetail);
			if(null!=h5_WalletMap){
				resultList.add(h5_WalletMap);
			}
		}
		
		Map<String, Object> appBalance_map=appBalance(starttime, endtime);
		if(null!=appBalance_map){
			Map<String, WalletBillDetail> appBalance_WalletMap=(Map<String, WalletBillDetail>)h5Wechat_map.get(WalletBillDetail);
			if(null!=appBalance_WalletMap){
				resultList.add(appBalance_WalletMap);
			}
		}
		
		Map<String, Object> appWechat_map=appWechat(starttime, endtime);
		if(null!=appWechat_map){
			Map<String, WalletBillDetail> appWechat_WalletMap=(Map<String, WalletBillDetail>)appWechat_map.get(WalletBillDetail);
			if(null!=appWechat_WalletMap){
				resultList.add(appWechat_WalletMap);
			}
		}
		
		Map<String, Object> appAliPay_map=appAliPay(starttime, endtime);
		if(null!=appAliPay_map){
			Map<String, WalletBillDetail> appAliPay_WalletMap=(Map<String, WalletBillDetail>)appAliPay_map.get(WalletBillDetail);
			if(null!=appAliPay_WalletMap){
				resultList.add(appAliPay_WalletMap);
			}
		}
		
		Map<String, Object> faceAppPay_map=faceAppPay(starttime, endtime);
		if(null!=faceAppPay_map){
			Map<String, WalletBillDetail> faceAppPay_WalletMap=(Map<String, WalletBillDetail>)faceAppPay_map.get(WalletBillDetail);
			if(null!=faceAppPay_WalletMap){
				resultList.add(faceAppPay_WalletMap);
			}
		}
		
		Map<String, Object> faceAppWechat_map=faceAppWechat(starttime, endtime);
		if(null!=faceAppWechat_map){
			Map<String, WalletBillDetail> faceAppWechat_WalletMap=(Map<String, WalletBillDetail>)faceAppWechat_map.get(WalletBillDetail);
			if(null!=faceAppWechat_WalletMap){
				resultList.add(faceAppWechat_WalletMap);
			}
		}
		
		Map<String, Object> faceAppAliPay_map=faceAppAliPay(starttime, endtime);
		if(null!=faceAppAliPay_map){
			Map<String, WalletBillDetail> faceAppAliPay_WalletMap=(Map<String, WalletBillDetail>)faceAppAliPay_map.get(WalletBillDetail);
			if(null!=faceAppAliPay_WalletMap){
				resultList.add(faceAppAliPay_WalletMap);
			}
		}
		
		Map<String, Object> storeAppPay_map=storeAppPay(starttime, endtime);
		if(null!=storeAppPay_map){
			Map<String, WalletBillDetail> storeAppPay_WalletMap=(Map<String, WalletBillDetail>)storeAppPay_map.get(WalletBillDetail);
			if(null!=storeAppPay_WalletMap){
				resultList.add(storeAppPay_WalletMap);
			}
		}
		
		Map<String, Object> storeAppWechat_map=storeAppWechat(starttime, endtime);
		if(null!=storeAppWechat_map){
			Map<String, WalletBillDetail> storeAppWechat_WalletMap=(Map<String, WalletBillDetail>)storeAppWechat_map.get(WalletBillDetail);
			if(null!=storeAppWechat_WalletMap){
				resultList.add(storeAppWechat_WalletMap);
			}
		}
		
		Map<String, Object> storeAppAliPay_map=storeAppAliPay(starttime, endtime);
		if(null!=storeAppAliPay_map){
			Map<String, WalletBillDetail> storeAppAliPay_WalletMap=(Map<String, WalletBillDetail>)storeAppAliPay_map.get(WalletBillDetail);
			if(null!=storeAppAliPay_WalletMap){
				resultList.add(storeAppAliPay_WalletMap);
			}
		}
		
		Map<String, Object> storeH5Wechat_map=storeH5Wechat(starttime, endtime);
		if(null!=storeH5Wechat_map){
			Map<String, WalletBillDetail> storeH5Wechat_WalletMap=(Map<String, WalletBillDetail>)storeH5Wechat_map.get(WalletBillDetail);
			if(null!=storeH5Wechat_WalletMap){
				resultList.add(storeH5Wechat_WalletMap);
			}
		}
		
		Map<String, Object> storeH5AliPay_map=storeH5AliPay(starttime, endtime);
		if(null!=storeH5AliPay_map){
			Map<String, WalletBillDetail> storeH5AliPay_WalletMap=(Map<String, WalletBillDetail>)storeH5AliPay_map.get(WalletBillDetail);
			if(null!=storeH5AliPay_WalletMap){
				resultList.add(storeH5AliPay_WalletMap);
			}
		}
		return resultList;
	}
	
	//直接购买:H5微信分享购买
	public Map<String,Object> h5Wechat(Date starttime,Date endtime){
		try {
			List<GoodsWin> list = goodsWinDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.SHARE_WEIXIN.getCode(),"NOT NULL");
			return getResult(list, "直接购买:H5微信分享购买");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//直接购买：APP余额支付
	public Map<String,Object> appBalance(Date starttime,Date endtime){
		try {
			List<GoodsWin> list = goodsWinDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.PURCHASE_BALANCE.getCode(),null);
			return getResult(list, "直接购买：APP余额支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//直接购买:APP微信支付
	public Map<String,Object> appWechat(Date starttime,Date endtime){
		try {
			List<GoodsWin> list = goodsWinDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.PURCHASE_WEIXIN.getCode(),null);
			return getResult(list, "直接购买:APP微信支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//直接购买:APP支付宝支付
	public Map<String,Object> appAliPay(Date starttime,Date endtime){
		try {
			List<GoodsWin> list = goodsWinDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.PURCHASE_ALIPAY.getCode(),null);
			return getResult(list, "直接购买:APP支付宝支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//面对面扫码支付：APP余额支付
	public Map<String, Object> faceAppPay(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.SCAN_CODE_BALANCE.getCode());
			return getResultRecharge(list, "面对面扫码支付：APP余额支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//面对面扫码支付:APP微信支付
	public Map<String, Object> faceAppWechat(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.SCAN_CODE_WEIXIN.getCode());
			return getResultRecharge(list, "面对面扫码支付:APP微信支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//面对面扫码支付:APP支付宝支付
	public Map<String, Object> faceAppAliPay(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.SCAN_CODE_ALIPAY.getCode());
			return getResultRecharge(list, "面对面扫码支付:APP支付宝支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//商家二维码扫码支付：APP余额支付
	public Map<String, Object> storeAppPay(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.STORE_SCAN_CODE_BALANCE.getCode());
			return getResultRecharge(list, "商家二维码扫码支付：APP余额支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//商家二维码扫码支付：APP微信支付
	public Map<String, Object> storeAppWechat(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.STORE_SCAN_APP_WEIXIN.getCode());
			return getResultRecharge(list, "商家二维码扫码支付：APP微信支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//商家二维码扫码支付：APP支付宝支付
	public Map<String, Object> storeAppAliPay(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.STORE_SCAN_APP_ALIPAY.getCode());
			return getResultRecharge(list, "商家二维码扫码支付：APP支付宝支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	//商家二维码扫码支付：H5微信直接扫码支付
	public Map<String, Object> storeH5Wechat(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode());
			return getResultRecharge(list, "商家二维码扫码支付：H5微信直接扫码支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}		
	//商家二维码扫码支付：H5微信直接扫码支付
	public Map<String, Object> storeH5AliPay(Date starttime,Date endtime){
		try {
			List<WalletRecharge> list = walletRechargeDao.getListByTime(starttime, endtime,RechargeType.TRANSFER_SUCCESS.getCode(),BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
			return getResultRecharge(list, "商家二维码扫码支付：H5微信直接扫码支付");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}		
	
	//获取组装GoodsWin表数据
	public Map<String,Object> getResult(List<GoodsWin> list,String remark)throws Exception{
		//返回的MAP声明
		Map<String,Object> resultMap=new HashMap<String,Object>();
		//分类好的GoodsWin集合声明
		Map<String,List<GoodsWin>> tempMap=new HashMap<String,List<GoodsWin>>();
		List<String> storeids =new ArrayList<String>();
		//分类好的WalletBillDetail声明
		Map<String,WalletBillDetail> tempbdMap=new HashMap<String,WalletBillDetail>();
		//循环查出来的数据 进行分类
		for (GoodsWin goodsWin : list) {
			//非直接购买和购物车购买舍弃
			if(goodsWin.getOrderType()!=3&&goodsWin.getOrderType()!=1){
				continue;
			}
			//当前分类的GoodsWin声明
			List<GoodsWin> listByStore=null;
			//当前分类的WalletBillDetail声明
			WalletBillDetail wbd=null;
			//从MAP中根据商铺ID取出一个GoodsWin 
			if(null==tempMap.get(goodsWin.getStoreId())){//取出为NULL时说明没创建过对象 则新建对象
				listByStore=new ArrayList<GoodsWin>();
				wbd=new WalletBillDetail();
				//设置WalletBillDetail
				wbd.setStoreId(goodsWin.getStoreId());
				wbd.setRemark(remark);
				wbd.setType(goodsWin.getPayWay()+"");
				if(goodsWin.getOrderType()==3){//购物车购买时Price为总价不需要乘以数量
					wbd.setEP(null==goodsWin.getDrawPrice()?0d:goodsWin.getDrawPrice());
					wbd.setBalance(null==goodsWin.getPrice()?0d:goodsWin.getPrice());
				}else if(goodsWin.getOrderType()==1){//直接购买时Price为单价 需要乘以购买数量
					wbd.setEP((null==goodsWin.getDrawPrice()?0d:goodsWin.getDrawPrice())*(null==goodsWin.getNum()?0d:goodsWin.getNum()));
					wbd.setBalance((null==goodsWin.getPrice()?0d:goodsWin.getPrice())*(null==goodsWin.getNum()?0d:goodsWin.getNum()));
				}
				wbd.setStatus(goodsWin.getStatus());
				//每次发现新的商铺ID加入商铺数组
				storeids.add(goodsWin.getStoreId()); 
				allStoreids.add(goodsWin.getStoreId());//所有的商铺ID
			}else{										//取出不为NULL时说明有创建过对象 则从MAP中取出对象
				listByStore=tempMap.get(goodsWin.getStoreId());//从MAP中取出对象
				wbd=tempbdMap.get(goodsWin.getStoreId());//从MAP中取出对象
				if(goodsWin.getOrderType()==3){//购物车购买时Price为总价不需要乘以数量
					wbd.setEP(wbd.getEP()+(null==goodsWin.getDrawPrice()?0d:goodsWin.getDrawPrice()));
					wbd.setBalance(wbd.getBalance()+(null==goodsWin.getPrice()?0d:goodsWin.getPrice()));
				}else if(goodsWin.getOrderType()==1){//直接购买时Price为单价 需要乘以购买数量
					wbd.setEP(wbd.getEP()+((null==goodsWin.getDrawPrice()?0d:goodsWin.getDrawPrice())*(null==goodsWin.getNum()?0d:goodsWin.getNum())));
					wbd.setBalance(wbd.getBalance()+((null==goodsWin.getPrice()?0d:goodsWin.getPrice())*(null==goodsWin.getNum()?0d:goodsWin.getNum())));
				}
			}
			listByStore.add(goodsWin);//添加当前对象进入分类
			tempbdMap.put(goodsWin.getStoreId(), wbd);//覆盖上一次的MAP记录
			tempMap.put(goodsWin.getStoreId(), listByStore);//覆盖上一次的MAP记录
		}
		resultMap.put(AllStoreids, storeids);//当前统计所有的商铺ID
		resultMap.put(WalletBillDetail, tempbdMap);//根据商铺ID分类好的WalletBillDetail
		resultMap.put(AllGoodsWin, tempMap);//根据商铺ID分类好的GoodsWin集合
		return resultMap;
	}
	//获取组装WalletRecharge表数据
	public Map<String,Object> getResultRecharge(List<WalletRecharge> list,String remark)throws Exception{
		//返回的MAP声明
		Map<String,Object> resultMap=new HashMap<String,Object>();
		//分类好的WalletRecharge集合声明
		Map<String,List<WalletRecharge>> tempMap=new HashMap<String,List<WalletRecharge>>();
		List<String> storeids =new ArrayList<String>();
		//分类好的WalletBillDetail声明
		Map<String,WalletBillDetail> tempbdMap=new HashMap<String,WalletBillDetail>();
		//循环查出来的数据 进行分类
		for (WalletRecharge walletRecharge : list) {
			//当前分类的WalletRecharge声明
			List<WalletRecharge> listByStore=null;
			//当前分类的WalletBillDetail声明
			WalletBillDetail wbd=null;
			//从MAP中根据商铺ID取出一个WalletRecharge  WalletRecharge没有商铺ID  所以使用USERID作为唯一标识
			if(null==tempMap.get(walletRecharge.getStoreUserId())){//取出为NULL时说明没创建过对象 则新建对象
				listByStore=new ArrayList<WalletRecharge>();
				wbd=new WalletBillDetail();
				//设置WalletBillDetail
				wbd.setRemark(remark);
				wbd.setType(walletRecharge.getSource()+"");
				wbd.setEP(null==walletRecharge.getScore()?0d:walletRecharge.getScore());
				wbd.setBalance(null==walletRecharge.getConfirmScore()?0d:walletRecharge.getConfirmScore());
				wbd.setStatus(walletRecharge.getStatus());
				//每次发现新的商铺ID加入商铺数组
				storeids.add(walletRecharge.getStoreUserId());
			}else{										//取出不为NULL时说明有创建过对象 则从MAP中取出对象
				listByStore=tempMap.get(walletRecharge.getStoreUserId());//从MAP中取出对象
				wbd=tempbdMap.get(walletRecharge.getStoreUserId());//从MAP中取出对象
				wbd.setEP(wbd.getEP()+walletRecharge.getScore());
				wbd.setBalance(wbd.getBalance()+walletRecharge.getConfirmScore());
			}
			listByStore.add(walletRecharge);//添加当前对象进入分类
			tempbdMap.put(walletRecharge.getStoreUserId(), wbd);//覆盖上一次的MAP记录
			tempMap.put(walletRecharge.getStoreUserId(), listByStore);//覆盖上一次的MAP记录
		}
		//查询出所有的用户设置商铺ID 
		List<Store> stores=new ArrayList<Store>();
		if(storeids.size()>0){
			stores=storeDao.getCollectListByUserId(storeids);
		}
		List<String> resultstoreids =new ArrayList<String>();
		//分类好的WalletBillDetail声明
		Map<String,WalletBillDetail> resulttempbdMap=new HashMap<String,WalletBillDetail>();
		Map<String,List<WalletRecharge>> resulttempMap=new HashMap<String,List<WalletRecharge>>();
		for (Store store : stores) {
			allStoreids.add(store.getId());
			resultstoreids.add(store.getId());
			if(null!=tempbdMap.get(store.getUserId())){
				resulttempbdMap.put(store.getId(), tempbdMap.get(store.getUserId()));
			}
			if(null!=tempMap.get(store.getUserId())){
				resulttempMap.put(store.getId(), tempMap.get(store.getUserId()));
			}
		}
		//吧所有的值设置进去
		resultMap.put(AllStoreids, resultstoreids);//当前统计所有的商铺ID
		resultMap.put(WalletBillDetail, resulttempbdMap);//根据商铺ID分类好的WalletBillDetail
		resultMap.put(AllWalletRecharge, resulttempMap);//根据商铺ID分类好的WalletRecharge集合
		return resultMap;
	}

}
