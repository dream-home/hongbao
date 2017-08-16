package com.yanbao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.util.ConnectionUtil;
@Controller
@RequestMapping("/Sql")
public class ConvertRegion {
	static Map<String, City2> nameByKey=new HashMap<String, City2>();
	static Map<String, City2> codeByKey=new HashMap<String, City2>();

	@ResponseBody
    @RequestMapping(value = "/setSql", method = RequestMethod.GET)
	public void setSql(){
		getAll();
		List<Store2> list=getUpdateInfo();
		update(list);
	}

	public static void main(String[] args) {
		getAll();
		List<Store2> list=getUpdateInfo();
		update(list);
	}

	public static void getAll(){
		List<Map<String, Object>> list=ConnectionUtil.getSelectBySql("SELECT * FROM `hb_sys_city`");
		for (Map<String, Object> map : list) {
			City2 c=new City2();
			c.setCode(map.get("code").toString());
			c.setName(map.get("name").toString());
			c.setParentCode(map.get("parentCode").toString());
			c.setShortName(map.get("shortName").toString());
			c.setLevel(map.get("level").toString());
			c.setZipCode(null==map.get("zipCode")?"":map.get("zipCode").toString());
			c.setZhName(map.get("zhName").toString());
			c.setAreaCode(null==map.get("areaCode")?"":map.get("areaCode").toString());
			codeByKey.put(c.getCode(), c);
			nameByKey.put(c.getName(), c);
		}
	}
	//�����Ҫ�޸ĵ����
	public static List<Store2> getUpdateInfo(){
		List<Store2> result=new ArrayList<Store2>();
		List<Map<String, Object>> list=ConnectionUtil.getSelectBySql("SELECT * FROM `hb_mall_user_store`");
		for (Map<String, Object> map : list) {
			Store2 s=new Store2();
			s.setId(map.get("id").toString());
			s.setProvince(map.get("province").toString());
			s.setCity(map.get("city").toString());
			s.setCounty(map.get("county").toString());
			result.add(s);
		}
		return result;
	}

	//��ʼ�޸�
	public static void update(List<Store2> list){
		String sql="UPDATE `hb_mall_user_store` SET `provinceCode` = '${province}',`cityCode` = '${city}',`areaId` = '${areaId}', `countyCode` = '${county}' WHERE `id` = '${id}'";
		for (Store2 store : list) {
			String code=nameByKey.get(store.getProvince()).getCode();
			String city=nameByKey.get(store.getCity()).getCode();
			String county=nameByKey.get(store.getCounty()).getCode();
			String upsql=sql.replace("${province}",code ).replace("${city}",city).replace("${areaId}", county).replace("${county}", county).replace("${id}", store.getId());
			System.out.println(upsql);
			ConnectionUtil.updateBySql(upsql);
		}
	}
}


class City2{
    private String code;
    private String name;
    private String parentCode;
    private String shortName;
    private String level;
    private String areaCode;
    private String zipCode;
    private String zhName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


}

class Store2{
    private String id;
    private String province;
    private String city;
    private String county;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}

}