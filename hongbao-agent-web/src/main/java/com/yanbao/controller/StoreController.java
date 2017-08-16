package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.*;
import com.yanbao.util.Md5Util;
import com.yanbao.util.StaticVariable;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.ImageVo;
import com.yanbao.vo.StoreVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yanbao.util.StaticVariable.getCitySetlower;
import static com.yanbao.util.StaticVariable.setAll;

/***
 * 商家相关
 */
@Controller
@RequestMapping("/store")
public class StoreController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private AgentInfoService agentInfoService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileLinkService fileLinkService;
    @Autowired
    private FileService fileService;
    @Autowired
    private SysCityService sysCityService;
    @Autowired 
    private HbImgageService imgageService;
    //当前类权限id
    private static String classPermissionId="5";
    //商铺审核权限  有审核权限可以允许不用店铺权限
    private static String storeCheck="3";
    
    /**
     * 代理区域或地区区域查询商家列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getStoreList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getStoreList(HttpServletRequest request, HttpServletResponse response, String location,String status, Page page) throws Exception{
    	if(!ispermission(storeCheck, request)){
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    	}
    	Token token = TokenUtil.getSessionUser(request);
        AgentInfo agentInfo = null;
        //判断是代理区域查找还是地区区域查找
        List<Integer> statusList = new ArrayList<>();

        if(!ToolUtil.isEmpty(status)){
            String[] strs = status.split(",");
            for(int i=0;i<strs.length;i++){
                statusList.add(Integer.parseInt(strs[i]));
            }
        }else{
            statusList.add(1);
        }

        //查询所有location下属的地区
        setAll(sysCityService);
        City city = null;
        List<City> cityList = new ArrayList<>();
        if (ToolUtil.isEmpty(location)) {
            location = "";
            //获取当前代理
            agentInfo = getAgent(token);
            if(agentInfo == null){
                return new JsonResult(1,"代理不存在");
            }
            //city.setCode(agentInfo.getAgentAreaId());
            city = StaticVariable.ALL_CITY_MAP.get(agentInfo.getAgentAreaId());
        }else{
            city = StaticVariable.ALL_CITY_MAP.get(location);
        }

        Map<String,Object> maps = getCitySetlower(city);
        List<String> ids = (List<String>) maps.get("IDS");

        //获取商家数量
        List<Store> storeList = storeService.getStoreList(ids,statusList,page);
        //统计商家数量
        int count = storeService.countStoreList(ids,statusList);

        List<StoreVo> rows = new ArrayList<>();
        int length = storeList.size();
        if(count ==0 || storeList.size() <= 0){
            return new JsonResult(new PageResult<StoreVo>());
        }
        StoreVo vo = null;
        for(int i=0;i<length;i++){
            vo = new StoreVo();
            BeanUtils.copyProperties(vo,storeList.get(i));

            //根据userid查询商家信息
            User user = userService.readById(vo.getUserId());
            if(user != null){
                vo.setPhone(user.getPhone());
                vo.setUserName(vo.getUserName());
            }

            rows.add(vo);
        }

        PageResult<StoreVo> voPage = new PageResult<StoreVo>();
        voPage.setRows(rows);
        voPage.setPageNo(page.getPageNo());
        voPage.setPageSize(page.getPageSize());
        voPage.setTotalSize(count);

        return new JsonResult(voPage);
    }

    /**
     * 查看商家信息
     * @param request
     * @param response
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/getStoreInfo", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getStoreInfoById(HttpServletRequest request, HttpServletResponse response, String storeId) throws Exception{
    	if(!ispermission(storeCheck, request)){
    		if(!ispermission(classPermissionId, request)){
        		return new JsonResult(2,"无权限");
        	}
    	}
        Token token = TokenUtil.getSessionUser(request);
        if(ToolUtil.isEmpty(storeId)){
            return new JsonResult(3,"商家id不能为空!");
        }
        //根据商家id查询商家信息
        Store store = storeService.readById(storeId);

        //判断商家是否存在
        if(store == null){
            return new JsonResult(3,"商家不存在");
        }
        String pathString="assets/images/shopDefault.png";
        //获取店铺相册
        List<ImageVo> storeIcons = new ArrayList<ImageVo>();
        List<SysFileLink> storeLinks = fileLinkService.getList(storeId);
        if (storeLinks != null && storeLinks.size() > 0) {
            for (SysFileLink link : storeLinks) {
                SysFile file = fileService.getById(link.getFileId());
                if (file != null) {
                	if(file.getPath().contains(pathString)){
                		continue;
                	}
                    ImageVo icon = new ImageVo();
                    icon.setId(file.getId());
                    icon.setPath(file.getPath());
                    icon.setIsDefault(link.getIsDefault());
                    storeIcons.add(icon);
                }
            }
        }

        //获取商铺营业执照
        List<ImageVo> licenseIcons = new ArrayList<ImageVo>();
        List<SysFileLink> licenseLinks = fileLinkService.getList(store.getStoreLicense());
        if (licenseLinks != null && licenseLinks.size() > 0) {
            for (SysFileLink link : licenseLinks) {
                SysFile file = fileService.getById(link.getFileId());
                if (file != null) {
                	if(file.getPath().contains(pathString)){
                		continue;
                	}
                    ImageVo icon = new ImageVo();
                    icon.setId(file.getId());
                    icon.setPath(file.getPath());
                    icon.setIsDefault(link.getIsDefault());
                    licenseIcons.add(icon);
                }
            }
        }

        //获取商铺营业执照
        List<ImageVo> IdCardIcons = new ArrayList<ImageVo>();
        List<SysFileLink> IdCardLinks = fileLinkService.getList(store.getIDCardIcon());
        if (IdCardLinks != null && IdCardLinks.size() > 0) {
            for (SysFileLink link : IdCardLinks) {
                SysFile file = fileService.getById(link.getFileId());
                if (file != null) {
                	if(file.getPath().contains(pathString)){
                		continue;
                	}
                    ImageVo icon = new ImageVo();
                    icon.setId(file.getId());
                    icon.setPath(file.getPath());
                    icon.setIsDefault(link.getIsDefault());
                    IdCardIcons.add(icon);
                }
            }
        }
        //查询商品详情图img表
        Image imgfind=new Image();
		imgfind.setImageLinkId(store.getId());
		List<Image> imglist=imgageService.readAll(imgfind);
        for (Image image : imglist) {
        	if(image.getPath().contains(pathString)){
        		continue;
        	}
        	ImageVo icon = new ImageVo();
            icon.setId(image.getId());
            icon.setPath(image.getPath());
            icon.setIsDefault(0);
            if(image.getType()==1){//身份证证件照
            	IdCardIcons.add(icon);
            }else if(image.getType()==2){//2：商家资质
            	licenseIcons.add(icon);
            }else if(image.getType()==3){//3：店铺图
            	 storeIcons.add(icon);
            }
		}


        StoreVo vo = new StoreVo();
        BeanUtils.copyProperties(vo, store);

        //商铺相册
        vo.setIcons(storeIcons);
        //商铺营业执照
        vo.setLicenseIcons(licenseIcons);
        //商铺用户身份证
        vo.setIDCardIcons(IdCardIcons);

        //根据userid查询商家信息
        User user = userService.readById(vo.getUserId());
        if(user != null){
            vo.setPhone(user.getPhone());
            vo.setUserName(user.getUserName());
            vo.setUid(user.getUid());
        }

        return new JsonResult(vo);
    }

    public static void main(String[] args) {
//        String[] locations = "1".split(",");
//        System.out.print(locations.length);

        System.out.println(Md5Util.MD5Encode("as12345","20170601"));
    }

}
