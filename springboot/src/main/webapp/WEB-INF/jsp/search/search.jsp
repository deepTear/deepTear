<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'userAdd.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="/js/jquery.js"></script>
  </head>
  
  <body>
  	<input type="search" id="search"/>
  	<div id="item">
  	
  	</div>
  </body>
  <script>
  	$("#search").on("input",function(){
  		var key = $(this).val();
  		$.ajax({
  			url:"${pageContext.request.contextPath}/search-item/?",
  			dataType:"json",
  			data:{key:key},
  			type:"GET",
  			success:function(data){
  				$("#item").empty();
  				var html = "<ul>";
  				if(data){
  					for(var i = 0 ; i < data.length ; i++){
  						html += "<li>" + data[i].name + "</li>";
  					}
  				}
  				html += "</ul>";
				$("#item").append(html);
  			}
  		});
  	})
  </script>
</html>
