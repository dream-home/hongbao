package com.yanbao.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.MenuPermission;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.MenuPermissionMapper;
import com.yanbao.service.HbMenuPermissionService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbMenuPermissionServiceImpl")
public class HbMenuPermissionServiceImpl extends CommonServiceImpl<MenuPermission> implements HbMenuPermissionService  {
	/**注入员工权限表DAO接口类*/
	@Autowired
	private MenuPermissionMapper mapper;
	
	
	//讲权限数据存着内存中   如果有的话就不再查询数据库
	public static List<MenuPermission> staticPermission=null;
	public static Map<String,MenuPermission> staticPermissionMap=null;
	
	
	@Override
	public List<MenuPermission> getPoList(MenuPermission model) {
		return mapper.readList(new MenuPermission(), 0, 9999);
	}

	@Override
	public Integer addPo(MenuPermission model) {
		mapper.create(model);
		return 1;
	}

	@Override
	public List<MenuPermission> getAll() {
		if(null==staticPermission){
			staticPermission=mapper.readList(new MenuPermission(), 0, 9999);
		}
		return staticPermission;
	}
	
	@Override
	public Map<String,MenuPermission> getAllByMap() {
		if(null==staticPermission||staticPermission.size()==0){
			staticPermission=mapper.readList(new MenuPermission(), 0, 9999);
		}
		if(staticPermission.size()>0){
			staticPermissionMap=new HashMap<String, MenuPermission>();
			for (MenuPermission mp : staticPermission) {
				staticPermissionMap.put(mp.getId(), mp);
			}
		}
		return staticPermissionMap;
	}

	@Override
	protected CommonDao<MenuPermission> getDao() {
		return mapper;
	}

	@Override
	protected Class<MenuPermission> getModelClass() {
		return null;
	}
    
}