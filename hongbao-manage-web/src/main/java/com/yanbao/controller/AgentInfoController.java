package com.yanbao.controller;


import com.mall.model.AgentInfo;
import com.mall.model.AgentStaff;
import com.mall.model.Image;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.AgentInfoService;
import com.yanbao.service.AgentStaffService;
import com.yanbao.service.HbImgageService;
import com.yanbao.service.SysCityService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.Md5Util;
import com.yanbao.util.RandomUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.AgentInfoVo;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * 
 * @author zyc	 2017-06-19 15:50
 *	代理商信息表
 *
 */

@Controller
@RequestMapping("/agentInfo")
public class AgentInfoController extends BaseController {
	
	@Autowired
	AgentInfoService agentInfoService;
	
	@Autowired
	HbImgageService imgageService;
	@Value("${systemSalt}")
    String salt;
	
	 @Autowired
	 SysCityService sysCityService;
	 
	 
	 @Autowired
	private AgentStaffService agentStaffService;
	
	
	//添加
	 @RequestMapping("/create")
	 @ResponseBody
	 public JsonResult create(AgentInfo agentInfo,String imageVos){
		 //UUIDUtil.getUUID()
		 
		 //验证是否代理出现重复
		 AgentInfo agentInfoOne = new AgentInfo();
		 agentInfoOne.setAgentAreaId(agentInfo.getAgentAreaId());
		 agentInfoOne.setStatus(1);
		 
		 if(agentInfoService.readCount(agentInfoOne)>0){
			 return fail("区域代理只能添加一个");
		 }
		 
		 
		 
	
		String aId = UUIDUtil.getUUID(); 
		//保存image的Id
		String cardIconsId = "";
		String licenseIconsId  = "";
		 
		String[] images =  imageVos.split(",");
		
		for( int i = 0; i<  images.length; i++){
			
			
			Image imgage = new Image();
			
			//添加代理图片到hb_image表
			imgage.setId(UUIDUtil.getUUID());
			imgage.setPath(images[i]);
			imgage.setStatus(0);
			imgage.setType(5);
			imgage.setRemark("代理资质");
			imgage.setImageLinkId(aId);
			imgage.setCreateTime(new Date());
			
			if(i<=2){
				imgage.setType(1);
				imgage.setRank(i+1);
				cardIconsId += imgage.getId()+",";
				
				
			}else{
				imgage.setRank(i-2);
				imgage.setType(2);
				if(!images[i].equals("")){
					licenseIconsId += imgage.getId()+",";
				}else{
					licenseIconsId +=null+",";
				}
				
			}
			
			
			
			if(!images[i].equals("")){
				imgageService.create(imgage);
			}
			
		}
		 
		
		 
		 
		 agentInfo.setId(aId);
		 agentInfo.setUid(null);
		 agentInfo.setAgentId(aId);
		 agentInfo.setStatus(1);
		 agentInfo.setLoginName("DP"+agentInfo.getAgentAreaId());
		 agentInfo.setPassword(Md5Util.MD5Encode(agentInfo.getLoginName(),salt));
		 agentInfo.setPayPassWord(Md5Util.MD5Encode(agentInfo.getLoginName(),salt));
		 
		 agentInfo.setSalt(salt);
		 if(cardIconsId.length()!=0){
			 agentInfo.setCardIconId(cardIconsId.substring(0, cardIconsId.length()-1));
		 }
		 if(licenseIconsId.length()!=0){
			 agentInfo.setLicenseIconId(licenseIconsId.substring(0, licenseIconsId.length()-1));	
		 }
		 SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		 
		 try {
			agentInfo.setStatisticsTime(simple.parse(simple.format(DateTimeUtil.getDayThirty())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 agentInfo.setCreateTime(new Date());
		 agentInfoService.create(agentInfo);
		 return success("添加成功");
		 
 
	 }
	 
	 

	//查询
	 @RequestMapping("/list")
	 @ResponseBody
	 public JsonResult list(AgentInfo agentInfo,Page page){
		
		System.out.println(agentInfo.getFromTime());
		 int count =   agentInfoService.readCount(agentInfo);
		 
		 if (count == 0) {
	            return fail("没有推荐商品");
	     }
		 
		 
		 List<AgentInfo> list =   agentInfoService.readList(agentInfo, page.getPageNo(),page.getPageSize(), count);
		for(int i = 0 ; i<list.size();i++){
			
			list.get(i).setPassword("");
			list.get(i).setPayPassWord("");
		}
		 return  success(new PageResult(page.getPageNo(), page.getPageSize(), count, list));
	 }
	 
	 
	 	//下载
	 	@RequestMapping("/extAgentInfo")
	    public void extScoreRecords(AgentInfo agentInfo, Page page, HttpServletResponse response) {
	        String fileName = "代理记录统计" + RandomUtil.randomString(5) + ".csv";
	        response.setContentType("application/CSV");
	        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
	        response.addHeader("Cache-Control", "must-revalidate");
	        response.addHeader("Pragma", "must-revalidate");
	        OutputStream outputStream = null;
	        try {
	            outputStream = response.getOutputStream();
	            outputStream.write("id,代理账户,代理公司名称,负责人姓名,手机号,客服电话,邮箱,代理等级,代理区域id,省,市,区,公司详情地址,代理合作日期,状态\r\n".getBytes());
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            int count =  agentInfoService.readCount(agentInfo);
	          /*  if (count == 0) {
	                return;
	            }*/
	            List<AgentInfo> list =   agentInfoService.readList(agentInfo, page.getPageNo(),page.getPageSize(), count);
	    		for(int i = 0 ; i<list.size();i++){
	    			
	    			list.get(i).setPassword("");
	    			list.get(i).setPayPassWord("");
	    		}
	            
	            
	           
	            String type = "";
	            for (AgentInfo vo : list) {
	            	String agentLevel = "";
	            	String status = "";
	            	if(vo.getAgentLevel() == 1){
	            		agentLevel = "省级代理";
	            	}else if(vo.getAgentLevel() == 2){
	            		agentLevel = "市级代理";
	            	}else if(vo.getAgentLevel() == 3){
	            		agentLevel = "区级代理";
	            	}
	            	
	            	if(vo.getStatus() == 1){
	            		status = "审核通过";
	            	}else if(vo.getStatus() == 2){
	            		status = "审核不通过";
	            	}else if(vo.getStatus() == 3){
	            		status = "关闭";
	            	}else if(vo.getStatus() == 0){
	            		status = "审核中";
	            	}
	            	
	            	
	            	
	                outputStream.write((vo.getId() + ",\t" + vo.getLoginName().toString() +"," + vo.getCompany() + "," +vo.getUserName()+","+vo.getPhone()
	                        +"," +vo.getServicePhone()+","+vo.getEmail()+","+agentLevel+","+vo.getAgentAreaId()+","+vo.getAgentProvinceName()+","+vo.getAgentCityName()+","+vo.getAgentCountryName()+","+vo.getAddress()+","+DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG)+","+status+"\r\n").getBytes());
	                outputStream.flush();
	            }

	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 //删除
	 @RequestMapping("/delete")
	 @ResponseBody
	 public JsonResult delete(String id,AgentInfo agentInfo){
		 agentInfoService.updateById(id, agentInfo);
		 agentInfoService.deleteById(id);
		 /*status ==3 代理取消*/
	 	int  status = 3;
	 	AgentStaff ast= new AgentStaff();
    	ast.setAgentId(id);
    	ast.setStatus(status);
    	if(status==3){
    		ast.setRemark("代理账号停用!");
    	}else{
    		ast.setRemark("");
    	}
    	agentStaffService.modPoNotNullByAgent(ast);
	 
		 
		 
		 return success("修改成功");
	 }
	 //@RequestParam(required=false,value="imageVoList[]") 
	 
	 
	 //修改
	 
	 @RequestMapping(value="/updateAgent")
	 @ResponseBody
	 public JsonResult updateAgent(AgentInfo agentInfoVo,String imageVos){
		 JSONArray json = JSONArray.fromObject(imageVos);
		 List<Image> imageVo = (List<Image>) JSONArray.toCollection(json,Image.class);
		 String[] str = agentInfoVo.getLicenseIconId().split(",");
		 String licenseIconsId = "";
		 String strs = "";
		 for(int i = 0 ; i<imageVo.size();i++){
			
			 if(!(imageVo.get(i).getPath().equals(""))){
				 if(imageVo.get(i).getId().equals("")){
					 Image imgage = new Image();
						
						//添加代理图片到hb_image表
						imgage.setId(UUIDUtil.getUUID());
						imgage.setPath(imageVo.get(i).getPath());
						imgage.setStatus(1);
						imgage.setImageLinkId(agentInfoVo.getId());
						imgage.setCreateTime(new Date());
						imgage.setRank(imageVo.get(i).getRank());
					 
					 	
						imgageService.create(imgage);
						licenseIconsId+=imgage.getId();
				 }else{
					 imgageService.updateById(imageVo.get(i).getId(),imageVo.get(i));
				 }
				 	
			 }
			 
		 }
		 
		 
		
		 agentInfoService.updateById(agentInfoVo.getId(), agentInfoVo);
		 

		 return success("修改成功");
	 }
	 
	 
	//修改
	 @RequestMapping("/update")
	 @ResponseBody
	 public JsonResult update(AgentInfo agentInfo,String imageVos){
		 //判断image是否为新增， 如果是， 则为images[i].equals("")
		 
		 String[] images = agentInfo.getRemark().split(",");
		 String[] imageVosList = imageVos.split(",");
		 
		 String  aId = agentInfo.getId();
		 //id添加到licenseIconId
		 String licenseIconIds = "";
		//id添加到cardIconIds
		 String cardIconIds ="";
		 
		 
		 //新增尾部图片
		 
		 String lastlicenseIconId = "";
		
		 
		 for(int i =images.length;i<imageVosList.length;i++){
			 	
			 	if(!imageVosList[i].equals("1")){
			 		Image imgage = new Image();
					
					//添加代理图片到hb_image表
					imgage.setId(UUIDUtil.getUUID());
					imgage.setPath(imageVosList[i]);
					imgage.setStatus(1);
					imgage.setImageLinkId(aId);
					imgage.setCreateTime(new Date());
					
					imgage.setRank(3);
					imgage.setType(2);
					imgageService.create(imgage);
					lastlicenseIconId += imgage.getId()+",";
			 	}
			 	
			 
			 
		 }
		 if(lastlicenseIconId!=""){
			 lastlicenseIconId = lastlicenseIconId.substring(0, lastlicenseIconId.length()-1);
		 }
		 


		 		

		 
		 
		 
		 //添加新图片
		 for(int i  = 0 ; i<images.length;i++){
			 if(images[i].equals("1")){
				 
				 Image imgage = new Image();
					
					//添加代理图片到hb_image表
					imgage.setId(UUIDUtil.getUUID());
					imgage.setPath(images[i]);
					imgage.setStatus(1);
					imgage.setImageLinkId(aId);
					imgage.setCreateTime(new Date());
					
					if(i<=2){
						imgage.setType(0);
						imgage.setRank(i+1);
					}else{
						imgage.setRank(i-2);
						imgage.setType(1);
					}
					imgageService.create(imgage);
					images[i] = imgage.getId(); 
				 
				 
			 }else{
				 Image imgage = new Image();
				 imgage.setPath(imageVosList[i]);
				 
				 imgageService.updateById(images[i],imgage);
			 }
			 
			 if(i>2){
				 licenseIconIds += images[i]+",";
			 }else{
				 cardIconIds += images[i]+",";
			 }
			 
			 
		 }
		 
		 licenseIconIds+=lastlicenseIconId;
		 
		 agentInfo.setLicenseIconId(licenseIconIds.substring(0, licenseIconIds.length()-1));
		 agentInfo.setCardIconId(cardIconIds.substring(0, cardIconIds.length()));
		 
		 agentInfoService.updateById(aId, agentInfo);
		 
		 
		 
		 System.out.println(agentInfo.getCardIconId());
		 System.out.println(imageVos);
		 
		 return success("修改成功");
	 }
	 
	 
	 
	 
	 
	 
	 //恢复初始密码(代理初始密码和操作密码一样)
	 //删除
	 @RequestMapping("/updatePassWord")
	 @ResponseBody
	 public JsonResult updatePassWord(String id){
		 
		AgentInfo agentInfo =  agentInfoService.readById(id);
		
		agentInfo.setPassword(Md5Util.MD5Encode(agentInfo.getLoginName(),salt));
		 agentInfo.setPayPassWord(Md5Util.MD5Encode(agentInfo.getLoginName(),salt));
		agentInfoService.updateById(id, agentInfo);
		
		 return success("重置初始密码成功");
	 }
	 //@RequestParam(required=false,value="imageVoList[]") 
	 
	 
	 
	 //查看詳情
	 @RequestMapping("/detail")
	 @ResponseBody
	 public JsonResult detail(String id) throws Exception{
	 
		AgentInfo agentInfo = agentInfoService.readById(id);
		
		List<Image> idcardImg=new ArrayList<Image>();
    	List<Image> licensesImg=new ArrayList<Image>();
		
		if(agentInfo.getCardIconId()!=null){
			String[] idCards =  agentInfo.getCardIconId().split(",");

			for(String imgageId:idCards){
				idcardImg.add(imgageService.readById(imgageId));
			}
			
		}
		
		
		if(agentInfo.getLicenseIconId()!=null){
			String[] licenses =  agentInfo.getLicenseIconId().split(",");
			
			
	    	
			
			for(String imgageId:licenses){
				licensesImg.add(imgageService.readById(imgageId));
			}
		}
		AgentInfoVo agentInfoVo  = new AgentInfoVo();
		
		//复制类对象
		BeanUtils.copyProperties(agentInfoVo, agentInfo);
		agentInfoVo.setIdCard(idcardImg);
		agentInfoVo.setLicense(licensesImg);
		 
		 return success(agentInfoVo);
		 
		 	
	 }
	 
	 
	 
	 

	 
	 
	 
	 
	 
	 
	 
	
	
	
	
	
	
	
	
	
	
	
	
	

}
