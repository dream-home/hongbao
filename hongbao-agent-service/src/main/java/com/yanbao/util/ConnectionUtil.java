package com.yanbao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionUtil {
	
	
	public static List<Map<String,Object>> getSelectBySql(String sql){
		try {
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection conn = DriverManager.getConnection(ConfigStringModel.url, ConfigStringModel.username, ConfigStringModel.password);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String,Object> rowData=new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {  
					rowData.put(md.getColumnName(i), null==rs.getObject(i)?rs.getObject(i):rs.getObject(i).toString());  
				}
				list.add(rowData);
			}
			rs.close();
	        st.close();
	        conn.close();
	        return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static boolean updateBySql(String sql){
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection conn = DriverManager.getConnection(ConfigStringModel.url, ConfigStringModel.username, ConfigStringModel.password);
			Statement st = conn.createStatement();
			boolean is_up = st.execute(sql);
	        st.close();
	        conn.close();
	        return is_up;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
