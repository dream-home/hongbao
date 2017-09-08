<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Table</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	 <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	
	<script type="text/javascript">
		//选中的sql
		function setval(obj){
			$("#inputsql").val($(obj).html());
		}
		//显示选中的数据
		function showtext(obj){
			var html=$(obj).html().trim();
			if(html.indexOf("src=")>0){
				html=$(obj).children().attr("src");
			}
			$("#selectText").html(html);
		}
		//去往某页
		function go(pagenum){
			$("#pageNum").val(pagenum);
			submitFrom();
		}
		//回车事件
		$(function(){
			$("body").bind('keypress', function (event) {
	            if (event.keyCode == "13") {
	                submitFrom();
	            }
	        });
		});
		//表单提交
		function submitFrom(){
			var sql = $("#inputsql").val();
			if(sql.indexOf("　")!=-1){
				alert("请检查语句中包含中文空格!");
				return false;
			}
			if(sql.indexOf("insert ")!=-1
			||sql.indexOf("delete ")!=-1
			||sql.indexOf("update ")!=-1
			||sql.indexOf("drop ")!=-1
			||sql.indexOf("create ")!=-1
			||sql.indexOf(" change ")!=-1
			||sql.indexOf("modify ")!=-1
			||sql.indexOf("rename ")!=-1
			||sql.indexOf("alert ")!=-1
			||sql.indexOf("truncate ")!=-1
			||sql.indexOf("INSERT ")!=-1
			||sql.indexOf("DELETE ")!=-1
			||sql.indexOf("UPDATE ")!=-1
			||sql.indexOf("DROP ")!=-1
			||sql.indexOf("CREATE ")!=-1
			||sql.indexOf(" CHANGE ")!=-1
			||sql.indexOf("MODIFY ")!=-1
			||sql.indexOf("RENAME ")!=-1
			||sql.indexOf("ALERT ")!=-1
			||sql.indexOf("TRUNCATE ")!=-1
			 ){
				alert("SQL只能是查询语句");
				return false;
			}
			$("#subfrom").submit();
		}
		//分页方法
	    function getPage(type){
	   		var countpage=parseInt("${countpage}");
	   		var pageNum=parseInt("${pageNum}");
	   		if(type=="up"){
	   			if((pageNum-1)<=0){
	   				alert("当前为最前页");
	   				return false;
	   			}else{
	   				$("#pageNum").val(pageNum-1);
	   			}
	   		}
	   		if(type=="down"){
	   			if((pageNum+1)>countpage){
	   				alert("当前为最后页");
	   				return false;
	   			}else{
	   				$("#pageNum").val(pageNum+1);
	   			}
	   		}
	   		if(type=="head"){
	   			$("#pageNum").val("1");
	   		}
	   		if(type=="last"){
	   			$("#pageNum").val(countpage);
	   		}
	   		submitFrom();
	    }
	</script>
	<style>
	table {
	table-layout: fixed;
	}
	th{
	table-layout: auto;
	white-space: nowrap;
	}
	td {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	}
	
	#pagination-flickr a { border:solid 1px #DDDDDD; margin-right:2px; }
	#pagination-flickr .next a,#pagination-flickr .previous a { font-weight:bold; border:solid 1px #FFFFFF; }	
	#pagination-flickr .active { color:#ff0084; font-weight:bold; display:block; float:left; padding:4px 6px; }
	#pagination-flickr a:link, 
	#pagination-flickr a:visited { color:#0063e3; display:block; float:left; padding:3px 6px; text-decoration:none; }
	#pagination-flickr a:hover { border:solid 1px #000; background:#0063DC; color:#FFF; }
	#pagination-flickr li { border:0; margin:0; padding:0; font-size:11px; list-style:none; /* savers */ float:left; }
	#pagination-flickr .previous-off,#pagination-flickr .next-off { color:#666666; display:block; float:left; font-weight:bold; padding:3px 4px; }
	body { font-family:Arial, Helvetica, sans-serif; font-size:12px; }
	h2{ clear:both; border:0; margin:0; padding-top:30px; font-size:13px; }
	p{ border:0; margin:0; padding:0; padding-bottom:20px; }
	ul{ border:0; margin:0; padding:0; }
	
	</style>
  </head>
  
  <body style="width: 1900px">
  <div style="position:fixed; top:6%; left:65%; width:700px; height:140px; margin:-25px 0 0 -50px;display:block;word-break: break-all;word-wrap: break-word;overflow: auto;">
  	<textarea style="width:688px; height:138px;min-width: 680px;min-height:138px;max-width: 680px;max-height:138px "   rows="" cols="" id="selectText"></textarea>
  </div>
  <div class="container" style="margin-right: 10px;margin-left: 10px;position:fixed; ">
		 <form  action="${pageContext.request.contextPath}/select/selectBySQL" method="post" id="subfrom">
		 	<input type="hidden" name="pageNum" value="${pageNum}" id="pageNum"/> 
		 	<input type="hidden" name="pageSize" value="${pageSize}" id="pageSize"/>
		    <div class="row" style="margin-top: 35px;">
			<div class="col-xs-11">  
			    <div class="form-group">
			    	<label for="inputsql"></label>
			    	<input name="sql" style="margin-top: -3px;" type="text" class="form-control" id="inputsql" placeholder="sql" value="${sql }">
			 		</div>
			</div>
			<div class="col-xs-1">  
			    	  <button type="button" class="btn btn-default" onclick="submitFrom()">查询</button>
			</div>
			</div>
			 
		 </form>
		 <div class="row">
		 
		 </div>
</div>
 <div class="container" style="width: 100%;">
 	<div class="table-responsive" style="margin-top: -15px;">
 		<div  style="position:fixed;margin-top: 105px;">	
 			<div align="left" style="margin-left: 10px">历史记录:<br>
		   		<c:forEach items="${historysql}" var="indexsql">  
					<span onclick="setval(this)"><c:out value="${indexsql}"></c:out></span><br>
				</c:forEach>
			</div>
 			<ul id="pagination-flickr">
		   		<li class="next"><a href="#">总共${countinfo}条</a></li>
				<li class="next"><a href="#" onclick="getPage('head')">首页</a></li>
			    <li class="next"><a href="#" onclick="getPage('up')">上一页</a></li>
			    	<%-- <c:set var="mincount" value="1"/>
			    	<c:set var="maxcount" value="10"/>
			    	<c:if test="${countpage<10}">
			    		<c:set var="maxcount" value="${countpage}"/>
			    	</c:if>
			    	<c:if test="${countpage>10}">
			    		<c:set var="mincount" value="${countpage-10}"/>
			    	</c:if> --%>
			   		<c:forEach var="x" begin="1" end="${countpage}">
			   			<c:if test="${pageNum==x}">
			   				<li class="active">${x}</li>
			   			</c:if>
			   			<c:if test="${pageNum!=x}">
					   		<li><a href="#" onclick="go('${x}')">${x}</a></li>
			   			</c:if>
					</c:forEach>
			      <li class="next"><a href="#" onclick="getPage('down')">下一页</a></li>
			      <li class="next"><a href="#" onclick="getPage('last')">尾页</a></li>
			</ul>
		</div>
			<div style="margin-top: 205px;height:740px;overflow:auto;">
			 <table class="table  table-condensed" >
		   		<c:if test="${msg=='success'}">
			   		<thead>
			   			<tr>
			   				<c:forEach items="${column}" var="columnname">  
							   <th class="success " style="min-width: 120px" width="120px"><c:out value="${columnname}"></c:out></th>
							</c:forEach>
			   			</tr>
			   		</thead>
			   		<tbody>
			   			<c:forEach items="${info}" var="map">  
			   				<tr>
			   				<c:forEach items="${column}" var="columnname">  
							   <td ondblclick="showtext(this)" style="min-width: 100px" width="100px">
							   <c:if test="${fn:contains(map[columnname], 'data:image')||fn:contains(map[columnname], 'http://')||fn:contains(map[columnname], 'https://')}" >
							   <img style="height: 50px;" src="${map[columnname]}"  alt="${map[columnname]}" />
							   </c:if>
							   <c:if test="${!fn:contains(map[columnname], 'data:image')&&!fn:contains(map[columnname], 'http://')&&!fn:contains(map[columnname], 'https://')}">
							   <c:out value="${map[columnname]}"></c:out> 
							   </c:if>
							   </td>
							</c:forEach>
							</tr>
						</c:forEach> 
			   		</tbody>
		   		</c:if>
		   		<c:if test="${empty msg}">
		   			<tbody>
		   			<tr>
		   			<td>请输入sql并提交</td>
		   			</tr>
		   			</tbody>
		   		</c:if>
		   		<c:if test="${msg=='error'}">
		   			<tbody>
		   			<tr>
		   			<td>sql出现错误</td>
		   			</tr>
		   			</tbody>
		   		</c:if>
		   	</table>
		</div>
	</div>
 </div>
</body>
</html>
