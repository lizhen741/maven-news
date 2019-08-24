<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
 <head>
 	<meta charset="utf-8">
 	<link href="/news/css/1.css" rel="stylesheet" type="text/css">
	<script type="text/javascript">	
		//判断两次密码是否相同
		function isSame(){
			var password=document.getElementById("password").value;
			var password1=document.getElementById("password1").value;
			
			if(password===password1)
				return true;
			else{
				document.getElementById("password1Span").innerHTML="两次密码不一致！";
				return false;
			}
		}
	</script>
  </head>
  
  <body>
  	<div class="center" style="width:600px;margin-top:40px">
	 	<form id="form1" name="form1" method="post"  onsubmit="isSame()"
	 		action="/news/servlet/UserServlet?type1=newPassword">
		  <table width="400" align="center">
		    <tr><td colspan="2" align="center">输入新密码</td></tr>
		    <tr>
		      <td align="right">新密码：</td>
		      <td><input type="password" id="password" name="password" pattern="^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$" title="*密码至少需要8个字符，必须以字母开头，以字母或数字结尾，可以有-和_"></td>
		    </tr>	            
		    <tr>
		      <td align="right">重复一遍新密码：</td>
		      <td>
		      	<input type="password" id="password1" pattern="^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$" title="*密码至少需要8个字符，必须以字母开头，以字母或数字结尾，可以有-和_"/>
		        <span id="password1Span"></span>
		      </td>
		    </tr>		    
		    <tr>
		      <td colspan="2" align="center"><input type="submit" value="修改密码"/></td>
	        </tr>	        
		  </table>
		  <input type="hidden" name="rand" id="rand" value="${param.rand}">
	</form>  
	 </div>
  </body>
</html>
