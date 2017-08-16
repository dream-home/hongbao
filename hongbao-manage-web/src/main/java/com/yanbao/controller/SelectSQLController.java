package com.yanbao.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;


/**
 * Created by summer on 2016-12-08:13:38;
 * 
 * 幻灯片管理(广告管理)
 */
@Controller
@RequestMapping("/select")
public class SelectSQLController extends BaseController {
	
	private static List<String> staticsql=new ArrayList<String>();
	private String url;
	private String username;
	private String password;
    
	@RequestMapping("/selectPage")
    public String selectPage(){
		return "SelectPage";
	}
	
    @RequestMapping("/selectBySQL")
    public String selectBySQL(HttpServletResponse model,HttpServletRequest req,String sql,Integer pageNum,Integer pageSize){
    	try {
    		//页码默认值
    		if(null==pageNum){
    			pageNum=0;
    		}else if(pageNum>=1){
    			pageNum=pageNum-1;
    		}
    		if(null==pageSize){
    			pageSize=10;
    		}
    		req.setAttribute("sql", sql);
    		//验证是否为select 过滤其他
    		String upsql="";
    		if(null!=sql){//转大写
    			upsql=sql.toUpperCase();
    		}
    		if("".equals(upsql)
    				||upsql.contains("INSERT ")
    				||upsql.contains("DELETE ")
    				||upsql.contains("UPDATE ")
    				||upsql.contains("DROP ")
    				||upsql.contains("CREATE ")
    				||upsql.contains("CHANGE ")
    				||upsql.contains("MODIFY ")
    				||upsql.contains("RENAME ")
    				||upsql.contains("ALERT ")
    				||upsql.contains("TRUNCATE ")){
    			errorRetun(req);
    			return "SelectPage";
    		}
    		sql=sql.replace(";", "");
    		//记录历史sql
    		historySql(sql);
    		//统计sql
    		String countsql=sql;
    		//是否需要自动分页
    		boolean ispage=true;
    		//加入分页
    		if(sql.contains("${limit}")){//${limit}分页占位符
    			sql=sql.replace("${limit}", " limit "+(pageSize*pageNum)+","+pageSize+" ");
    			countsql=countsql.replace("${limit}", "");
    		}else{
    			if(!sql.contains("limit")&&!sql.contains("LIMIT")){
    				sql+=" limit "+(pageSize*pageNum)+","+pageSize;
    			}else{
    				ispage=false;
    			}
    		}
    		//获取链接
    		getConn(req);
    		//执行总条数SQL
    		int count=0;
    		if(ispage){
    			count=getSqlCount(countsql);
    		}
    		//总页数
    		int countpage=(count%pageSize)>0||((count/pageSize)==0)?(count/pageSize)+1:(count/pageSize);
    		//当前选择页数大于总页数,查询第一页
    		if(pageNum>countpage){
    			pageNum=0;
    			sql=sql.substring(0, sql.indexOf("limit"));
    			sql+=" limit 0,"+pageSize;
    		}
    		System.err.println(sql);
    		//查询记录
    		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
    		Connection conn = DriverManager.getConnection(url, username, password);
    		Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery(sql);
    		ResultSetMetaData md = rs.getMetaData();
    		int columnCount = md.getColumnCount();
    		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    		List<String> column= new ArrayList<String>();
    		for (int i = 1; i <= columnCount; i++) {  
    			column.add(md.getColumnName(i).toString());
			}
    		while(rs.next()){
    			Map<String,String> rowData = new HashMap<String,String>();  
    			for (int i = 1; i <= columnCount; i++) {  
    				rowData.put(md.getColumnName(i).toString(), null==rs.getObject(i)?"null":rs.getObject(i).toString());  
    			}
    			list.add(rowData);
    		}
    		//关闭
    		close(conn, st, rs);
    		//返回
    		req.setAttribute("msg", "success");
    		req.setAttribute("historysql", staticsql);
    		req.setAttribute("column", column);
    		req.setAttribute("info", list);
    		req.setAttribute("pageNum", pageNum+1);
    		req.setAttribute("pageSize", pageSize);
    		req.setAttribute("countinfo", count);
    		req.setAttribute("countpage", countpage);
            return "SelectPage";
		} catch (Exception e) {
			e.printStackTrace();
			errorRetun(req);
			return "SelectPage";
		}
    }
    
    //统计SQL
    public int getSqlCount(String sql)throws Exception{
    	//拼装总条数SQL
		String countSql=sql;
		if(countSql.contains("from")){
			countSql=countSql.substring(countSql.indexOf("from"),countSql.length());
		}else if(countSql.contains("FROM")){
			countSql=countSql.substring(countSql.indexOf("FROM"),countSql.length());
		}
		countSql="SELECT count(*) as countsize "+countSql;
		System.err.println(countSql);
    	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Connection conn = DriverManager.getConnection(url, username, password);
    	Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(countSql);
		int count=0;
		while(rs.next()){
			count=Integer.parseInt(rs.getObject("countsize").toString());
		}
		close(conn, st, rs);
		return count;
    }
    //加载链接数据
    public void getConn(HttpServletRequest req)throws Exception{
    	if(null==url||null==username||null==password){
	    	ServletContext servletContext =req.getSession().getServletContext();
	    	Properties config = new Properties();
			config.load(servletContext.getResourceAsStream("/WEB-INF/classes/config/jdbc.properties"));
			url=StringUtils.trimToEmpty(config.getProperty("jdbc_url"));
			username=StringUtils.trimToEmpty(config.getProperty("jdbc_username"));
			password=StringUtils.trimToEmpty(config.getProperty("jdbc_password"));
    	}
    }
    //关闭数据库链接
    public void close(Connection conn,Statement st,ResultSet rs)throws Exception{
    	rs.close();
    	st.close();
    	conn.close();
    }
    //错误耳朵返回方式
    public void errorRetun(HttpServletRequest req){
    	req.setAttribute("msg", "error");
		req.setAttribute("historysql", staticsql);
		req.setAttribute("countinfo", 0);
		req.setAttribute("countpage", 1);
		req.setAttribute("pageNum", 1);
    }
    //记录sql
    public void historySql(String sql){
    	//记录3条历史SQL
		boolean isRedo=true;
		for (String hissql : staticsql) {
			if(hissql.equals(sql)){
				isRedo=false;
			}
		}
		if(staticsql.size()>=3){//保持3条历史记录
			if(isRedo){
				staticsql.remove(0);
			}
		}
		//不重复则往里面加入
		if(staticsql.size()==0||isRedo){
			staticsql.add(sql);
		}
    }
}
