package com.yanbao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mall.model.City;
import com.yanbao.service.SysCityService;

public class StaticVariable {
	public static Map<String,City> ALL_CITY_MAP=new HashMap<String,City>();//以CODE为KEY
	public static Map<String,List<City>> ALL_CITY_PARENT_MAP=new HashMap<String,List<City>>();//以PARENTCODE为KEY
	public static List<City> ALL_CITY_LIST=new ArrayList<City>();
	
	public static void setAll(SysCityService sysCityService){
		if(ALL_CITY_MAP.size()==0||ALL_CITY_LIST.size()==0||ALL_CITY_PARENT_MAP.size()==0){
			ALL_CITY_LIST=sysCityService.readList(new City(), 0, 9999, 9999);
			for (City city : ALL_CITY_LIST) {
				ALL_CITY_MAP.put(city.getCode(), city);
				List<City> cityList=ALL_CITY_PARENT_MAP.get(city.getParentCode());
				if(null==cityList||cityList.size()==0){
					cityList=new ArrayList<City>();
				}
				cityList.add(city);
				ALL_CITY_PARENT_MAP.put(city.getParentCode(), cityList);
			}
		}
	}
	
	public static Map<String,Object> getCitySetParent(City city){//查询上级
		Map<String,Object> result=new HashMap<String, Object>();
		List<City> list=new ArrayList<City>();
		List<String> ids=new ArrayList<String>();
		Map<String,City> map=new HashMap<String, City>();
		ids.add(city.getCode());
		list.add(city);
		map.put(city.getCode(), city);
		if(ALL_CITY_MAP.size()==0||ALL_CITY_LIST.size()==0||ALL_CITY_PARENT_MAP.size()==0){
			return null;
		}else{
			String code = city.getParentCode();
			//最高查询10级
			for (int i = 0; i<5; i++) {
				City citycode=ALL_CITY_MAP.get(code);
				if(null==citycode){
					break;
				}else{
					code=citycode.getParentCode();
					map.put(citycode.getCode(), citycode);
					list.add(citycode);
					ids.add(citycode.getCode());
				}
			}
			result.put("MAP", map);
			result.put("LIST", list);
			result.put("IDS", ids);
			return result;
		}
	}
	
	public static Map<String,Object> getCitySetlower(City city){//查询下级
		Map<String,Object> result=new HashMap<String, Object>();
		List<City> list=new ArrayList<City>();
		List<String> ids=new ArrayList<String>();
		Map<String,City> map=new HashMap<String, City>();
		ids.add(city.getCode());
		list.add(city);
		map.put(city.getCode(), city);
		if(ALL_CITY_PARENT_MAP.size()==0||ALL_CITY_LIST.size()==0||ALL_CITY_PARENT_MAP.size()==0){
			return null;
		}else{
			String code = city.getCode();
			setLowLevel(map, list, ids, code);
			result.put("MAP", map);
			result.put("LIST", list);
			result.put("IDS", ids);
			return result;
		}
	}
	
	//递归下级
	public static void setLowLevel(Map<String,City> map,List<City> list,List<String> ids,String code){
		List<City> listcitycode=ALL_CITY_PARENT_MAP.get(code);
		if(null==listcitycode||listcitycode.size()==0){
			return;
		}else{
			for (City city2 : listcitycode) {
				code=city2.getCode();
				map.put(city2.getCode(), city2);
				list.add(city2);
				ids.add(city2.getCode());
				setLowLevel(map, list, ids, code);
			}
		}	
	}

	public static void main(String[] args) {
		/*Hash.hset("area", "singleArea", "");
		Hash.hget("area","singleArea");*/
		City city= new City();
		city.setId("yyr1");
		ALL_CITY_MAP.put("123", city);
		String valueString=JSONObject.fromObject(ALL_CITY_MAP).toString();
		System.out.println(valueString);
		Map<String, Object> map=(Map<String, Object>)JSONObject.fromObject(valueString);
		City city2=(City) JSONObject.toBean(JSONObject.fromObject(map.get("123")), City.class);
		System.out.println(city2.getId());
		
		ALL_CITY_LIST.add(city);
		valueString=JSONArray.fromObject(ALL_CITY_LIST).toString();
		System.out.println(valueString);
		List<JSONObject> listObj=(List<JSONObject>)JSONArray.fromObject(valueString);
		List<City> cityList=new ArrayList<City>();
		for (JSONObject jsonObject : listObj) {
			City cityo=(City)JSONObject.toBean(JSONObject.fromObject(jsonObject), City.class);
			cityList.add(cityo);
		}
		System.out.println(cityList.get(0).getId());
	}
}
