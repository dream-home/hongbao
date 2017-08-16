package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;
import com.yanbao.redis.SortSet;
import com.yanbao.service.*;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.AdVo;
import com.yanbao.vo.DistanceVo;
import com.yanbao.vo.HotGoodsVo;
import com.yanbao.vo.TopVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private AdService adService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    private GoodsSortService goodsSortService;
    @Autowired
    private IndexAdService indexAdService;
    @Autowired
    private IndexBannerService indexBannerService;

    /**
     * 幻灯片广告
     */
    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult test(HttpServletRequest request ,Integer jj, Double lon, Double lat) throws Exception {
        for (int i = 0; i < jj; i++) {
            Map<String, String> map = new HashMap<>();
            Sets.sadd(RedisKey.STORE_CODE_PREFIX.getKey() + "aa", i + "");
            map.put("0", (116.32953 + lon + RandomUtil.randomDouble()) + "");
            map.put("1", (39.998559 + lat + RandomUtil.randomDouble()) + "");
            Hash.hmset(RedisKey.STORE_CODE_PREFIX.getKey() + i, map);
        }
        List<DistanceVo> distanceVoList = new ArrayList<>();
        Page page = new Page();
        page.setPageNo(1);
        page.setPageSize(10);
        Set<String> storeIds = Sets.smembers(RedisKey.STORE_CODE_PREFIX.getKey() + "aa");
        for (String storeId : storeIds) {
            String longitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "0");
            String latitude = Hash.hget(RedisKey.STORE_CODE_PREFIX.getKey() + storeId, "1");
            System.out.println("("+longitude+","+latitude+")");
            Double distance = ToolUtil.getDistance(116.32953, 39.998559, Double.valueOf(longitude), Double.valueOf(latitude));
            System.out.println("距离："+distance);
            SortSet.zadd(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", distance, storeId);
        }
        Set<String> storeIdWithSort = SortSet.zrange(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", page.getStartRow(), page.getPageSize());
        for (String storeId : storeIdWithSort) {
            Double finalDistance = SortSet.zscore(RedisKey.STORE_CODE_PREFIX.getKey() + "bbbbb", storeId);
            DistanceVo dis = new DistanceVo();
            dis.setStoreId(storeId);
            dis.setDistance(finalDistance);
            distanceVoList.add(dis);
        }
        for (DistanceVo vo : distanceVoList) {
            System.out.println(ToolUtil.getDistance(vo.getDistance()));
        }
        return new JsonResult(distanceVoList);
    }


    /**
     * 幻灯片广告
     */
    @ResponseBody
    @RequestMapping(value = "/ad", method = RequestMethod.GET)
    public JsonResult getAdList(HttpServletRequest request) throws Exception {

        Ad ad = new Ad();
        ad.setRemark(null);
        ad.setType(null);
        List<Ad> list = adService.getList(ad);
        return new JsonResult(list);
    }


    /**
     * 幻灯片广告 （新版，有引导图）
     */
    @ResponseBody
    @RequestMapping(value = "/guide", method = RequestMethod.GET)
    public JsonResult guide(HttpServletRequest request, AdVo vo) throws Exception {
        if (vo.getType() == null) {
            return new JsonResult(-1, "类型不能为空");
        }
        if (vo.getType() != 0 && StringUtils.isEmpty(vo.getVersion())) {
            return new JsonResult(-1, "版本号不能为空");
        }
        Ad ad = new Ad();
        ad.setRemark(vo.getVersion());
        ad.setType(vo.getType());
        List<Ad> list = adService.getList(ad);
        Map map = new HashMap();
        map.put("key", ParamUtil.getIstance().get(Parameter.GUIDEFLAG));
        map.put("list", list);
        return new JsonResult(map);
    }


    /**
     * 最佳人气
     */
    @ResponseBody
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public JsonResult getHotList(HttpServletRequest request) throws Exception {
        Map<String, String> sortMap = new HashMap<>();
        ;
        List<Goods> list = goodsService.getRecommendList(9);
        List<HotGoodsVo> hostList = new ArrayList<>();
        List<GoodsSort> sortList = goodsSortService.getList();
        if (!CollectionUtils.isEmpty(sortList)) {
            for (GoodsSort goodsSort : sortList) {
                sortMap.put(goodsSort.getId(), goodsSort.getName());
            }
        }
        for (Goods good : list) {
            HotGoodsVo vo = new HotGoodsVo();
            BeanUtils.copyProperties(vo, good);
            if (!StringUtils.isEmpty(good.getGoodsSortId())) {
                vo.setGoodsSortName(sortMap.get(good.getGoodsSortId()));
            } else {
                vo.setGoodsSortName("");
            }
            hostList.add(vo);
        }
        return new JsonResult(hostList);
    }

    /**
     * 最计参与人数
     */
    @ResponseBody
    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public JsonResult getPlayers(HttpServletRequest request) throws Exception {
        Map<String, Long> result = new HashMap<String, Long>();
        Long total = goodsIssueDetailService.players();
        result.put("total", total);
        return new JsonResult(result);
    }

    /**
     * 竞拍排行榜
     */
    @ResponseBody
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public JsonResult getTopList(HttpServletRequest request) throws Exception {
        Integer num = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.WINNERLISTNUM),10);
        List<TopVo> list = goodsWinService.getTopList(num);
        return new JsonResult(list);
    }


    /**
     * 六张广告图
     */
    @ResponseBody
    @RequestMapping(value = "/indexad", method = RequestMethod.GET)
    public JsonResult getIndexAdList(HttpServletRequest request) throws Exception {
        List<IndexAd> list = indexAdService.getIndexAdList();
        return new JsonResult(list);
    }

    /**
     * 功能banner
     */
    @ResponseBody
    @RequestMapping(value = "/banners", method = RequestMethod.GET)
    public JsonResult getIndexBannersList(HttpServletRequest request) throws Exception {
        List<IndexBanner> list = indexBannerService.getIndexBannerList();
        return new JsonResult(list);
    }
}
