package com.yanbao.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mall.model.Ad;
import com.mall.model.Admin;
import com.mall.model.Goods;
import com.mall.model.User;
import com.yanbao.service.ADService;
import com.yanbao.service.AdminService;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.service.GoodsService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletDonateService;
import com.yanbao.service.WalletRecordService;
import com.yanbao.util.RandomUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.GoodsIssueDetailVo;

/**
 * Created by summer on 2016-12-08:11:04;
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/spring-*.xml")
public class Daotest {
    @Autowired
    AdminService adminService;
    @Autowired
    ADService adService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    WalletRecordService walletRecordService;
    @Autowired
    WalletDonateService walletDonateService;
    @Autowired
    GoodsIssueDetailService goodsIssueDetailService;

    @Test
    public void test1(){
        Admin admin=new Admin();
        admin.setPassword("123456");
        admin.setId(UUIDUtil.getUUID());
        adminService.create(admin);
        admin.setPassword("1234567");
        adminService.updateById(admin.getId(),admin);
        System.out.println(admin.getAccount());
    }
    @Test
    public void test2(){
        Ad ad=adService.readById("1");
        System.out.println(ad.getAdImg());
        adService.deleteById("2");
        ad.setId(UUIDUtil.getUUID());
        adService.create(ad);
    }
    @Test
    public void test3(){
        Goods goods=new Goods();
        goods.setIsRecommend(1);
        goods.setStatus(1);
        List<Goods> goodses=goodsService.readAll(goods);
        System.out.println(goodses.size());
    }
    @Test
    public void test4(){
//        User user=userService.readById("1");
//        System.out.println(user.getAccount());
        User user=new User();
        user.setAccount(UUIDUtil.getUUID(1)[0]);
        user.setId(UUIDUtil.getUUID());
        user.setUid(RandomUtil.randomInt(1000,9000000));
        userService.create(user);
    }

    @Test
    public void test5(){
        try {
            GoodsIssueDetailVo goodsIssueDetailVo=new GoodsIssueDetailVo();
            List<GoodsIssueDetailVo> goodsIssueDetailVos=goodsIssueDetailService.getGoodsIssueDetailVoList(goodsIssueDetailVo,0,10);
            System.out.println(goodsIssueDetailService.getGoodsIssueDetailVoCount(goodsIssueDetailVo));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
