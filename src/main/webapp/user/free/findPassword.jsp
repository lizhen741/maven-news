<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head><link href="/news/css/1.css" rel="stylesheet" type="text/css"></head>  
  <body>
  	<div class="center" style="width:600px;margin-top:40px">
	 	<form method="post" action="/news/servlet/UserServlet?type1=byEmail">
		  <table width="400" align="center">
		    <tr><td colspan="2" align="center">找回密码</td></tr>            
		    <tr>
		      <td align="right">电子邮箱：</td>
		      <td><input name="email" type="email"  size="30"/></td>
		    </tr>		    
		    <tr>
		      <td colspan="2" align="center"><input type="submit" value="找回密码"/></td>
	        </tr>	        
		  </table>
		</form>
	</div>  
  </body>
</html>
