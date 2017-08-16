package com.yanbao.service.impl;

import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StoreType;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.StoreDao;
import com.mall.model.Store;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;
import com.yanbao.redis.SortSet;
import com.yanbao.service.StoreService;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.DistanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreDao storeDao;

    private void setDefaultValue(Store model) {
        model.setId(UUIDUtil.getUUID());
        model.setCollectNum(0);
        model.setStatus(StoreType.PENDING.getCode());
        model.setCreateTime(new Date());
        model.setUpdateTime(model.getCreateTime());
    }

    @Override
    public Store getById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return storeDao.getById(id);
    }

    @Override
    public Integer add(Store model) throws Exception {
        if (model == null) {
            return 0;
        }
        setDefaultValue(model);
        return storeDao.add(model);
    }

    @Override
    public Integer update(String id, Store model) throws Exception {
        if (StringUtils.isBlank(id) || model == null) {
            return 0;
        }
        model.setUpdateTime(new Date());
        return storeDao.update(id, model);
    }

    @Override
    public Integer updateCollectNum(String id, Integer num) throws Exception {
        if (StringUtils.isBlank(id) || num == null || num == 0) {
            return 0;
        }
        return storeDao.updateCollectNum(id, num);
    }

    @Override
    public List<Store> getCollectList(List<String> storeIds) throws Exception {
        if (storeIds == null || storeIds.size() <= 0) {
            return null;
        }
        return storeDao.getCollectList(storeIds);
    }

    @Override
    public PageResult<Store> getPage(Page page, String location) throws Exception {
       PageResult<Store> pageResult = new PageResult<Store>();
            BeanUtils.copyProperties(pageResult, page);

            Integer count = storeDao.count(location);
            pageResult.setTotalSize(count);
            if (count != null && count > 0) {
                List<Store> list = storeDao.getList(page, location);
                pageResult.setRows(list);
        }
        return pageResult;
    }

    @Override
    public List<Store> getListByName(String searchWord, String location,Boolean isHasCoordinate) throws Exception {
        if (StringUtils.isBlank(searchWord)) {
            return null;
        }
        return storeDao.getListByName(searchWord, location,isHasCoordinate);
    }

    @Override
    public List<Store> getNewStore(Integer maxRow) throws Exception {
        return storeDao.getNewStore(maxRow);
    }

    @Override
    public Integer updateSumSaleCount(String id, Integer num) throws Exception {
        if (StringUtils.isEmpty(id)) {
            return 0;
        }
        if (num == null || num < 0) {
            return 0;
        }
        return storeDao.updateSumSaleCount(id, num);
    }

    @Override
    public List<DistanceVo> getSimpleList(Page page, DistanceVo distanceVo) throws Exception {
        List<DistanceVo> distanceVoList = new ArrayList<>();
        String location = distanceVo.getAddress();
        //加载一个地区所有商家的id和经纬度到此key
        String key = RedisKey.STORE_CODE_PREFIX.getKey() + location;
        Set<String> storeIds = null;
        storeIds = Sets.smembers(key);
        if (ToolUtil.isNotEmpty(storeIds)) {
            //如果有缓存商家数据计算距离：获取带有商家id 距离排序的list
            calculateDistanceByRedis(page, distanceVo, distanceVoList, storeIds);
        } else {
            Page temp =new Page();
            temp.setPageNo(1);
            temp.setPageSize(Page.bigPageSize);
            PageResult<Store> pageResult = new PageResult<Store>();
            BeanUtils.copyProperties(pageResult, page);
            //查询此区域所有商家的id 经纬度
            List<Store> list = storeDao.getSimpleList(temp, location);
            if (CollectionUtils.isEmpty(list)) {
                list = Collections.emptyList();
            } else {
                for (Store store : list) {
                    Map<String, String> map = new HashMap<>();
                    Sets.sadd(RedisKey.STORE_CODE_PREFIX.getKey() + location, store.getId());
                    map.put("0", store.getLongitude());
                    map.put("1", store.getLatitude());
                    Hash.hmset(RedisKey.STORE_CODE_PREFIX.getKey() + store.getId(), map);
                    storeIds.add(store.getId());
                }
                calculateDistanceByRedis(page, distanceVo, distanceVoList, storeIds);
            }
            pageResult.setRows(list);
        }
        return distanceVoList;
    }

    private void calculateDistanceByRedis(Page page, DistanceVo distanceVo, List<DistanceVo> distanceVoList, Set<String> storeIds) {
        for (String storeId : storeIds) {
            String longitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "0");
            String latitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "1");
            Double distance = ToolUtil.getDistance(distanceVo.getLongitude(), distanceVo.getLatitude(), Double.valueOf(longitude), Double.valueOf(latitude));
            //计算距离
            SortSet.zadd(RedisKey.STORE_CODE_PREFIX.getKey() + distanceVo.getSerialNumber()+distanceVo.getAddress(), distance, storeId);
        }
        //分页获取商家id
        Set<String> storeIdWithSort =  SortSet.zrange(RedisKey.STORE_CODE_PREFIX.getKey() + distanceVo.getSerialNumber()+distanceVo.getAddress(),page.getStartRow(),page.getStartRow()+page.getPageSize()-1);
        for (String storeId : storeIdWithSort) {
            Double finalDistance  = SortSet.zscore(RedisKey.STORE_CODE_PREFIX.getKey() + distanceVo.getSerialNumber()+distanceVo.getAddress(),storeId);
            DistanceVo dis=new DistanceVo();
            dis.setStoreId(storeId);
            dis.setDistance(finalDistance);
            distanceVoList.add(dis);
        }
    }

    @Override
    public PageResult<Store> getNewPage(Page page,  DistanceVo distanceVo) throws Exception {
        PageResult<Store> pageResult = new PageResult<Store>();
        BeanUtils.copyProperties(pageResult, page);
        List<DistanceVo> list=this.getSimpleList(page,distanceVo);
        List<String> storeIds=new ArrayList<>();
        Long size = SortSet.zcard(RedisKey.STORE_CODE_PREFIX.getKey() + distanceVo.getSerialNumber()+distanceVo.getAddress());
        Integer count = Integer.valueOf(size.toString());
        pageResult.setTotalSize(count);
        for (DistanceVo vo:list){
            storeIds.add(vo.getStoreId());
        }
        if (count != null && count > 0 && storeIds.size()>0) {
            List<Store> storeList = this.getListByIds(storeIds);
            pageResult.setRows(storeList);
        }
        return pageResult;
    }

    @Override
    public List<Store> getListByIds(List<String> list) throws Exception {
        if (ToolUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Store> l=storeDao.getListByIds(list);
        if (ToolUtil.isEmpty(l)){
            return Collections.emptyList();
        }
        return l;
    }

    public static void main(String[] args) {
        Double distance = ToolUtil.getDistance(116.32953, 39.998559,115.32953, 39.998559);
        System.out.println(distance);

//
//        if (flag==0){
//            for (int i = ii; i < jj; i++) {
//                Map<String, String> map = new HashMap<>();
//                Sets.sadd(RedisKey.STORE_CODE_PREFIX.getKey() + "aa", i + "");
//                map.put("0", (116.32953 + i) + "");
//                map.put("1", (39.998559 + i) + "");
//                Hash.hmset(RedisKey.STORE_CODE_PREFIX.getKey() + i, map);
//            }
//        }
//
//        List<DistanceVo> distanceVoList = new ArrayList<>();
//        Page page = new Page();
//        page.setPageNo(1);
//        page.setPageSize(10);
//        Set<String> storeIds = Sets.smembers(RedisKey.STORE_CODE_PREFIX.getKey() + "aa");
//        for (String storeId : storeIds) {
//            String longitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "0");
//            String latitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "1");
//            Double distance = ToolUtil.getDistance(116.32953, 39.998559, Double.valueOf(longitude), Double.valueOf(latitude));
//            SortSet.zadd(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", distance, storeId);
//        }
//        Set<String> storeIdWithSort = SortSet.zrange(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", page.getStartRow(), page.getPageSize());
//        for (String storeId : storeIdWithSort) {
//            Double finalDistance = SortSet.zscore(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", storeId);
//            DistanceVo dis = new DistanceVo();
//            dis.setStoreId(storeId);
//            dis.setDistance(finalDistance);
//            distanceVoList.add(dis);
//        }
//        for (DistanceVo vo : distanceVoList) {
//            System.out.println(vo.getDistance());
//        }
    }
}
