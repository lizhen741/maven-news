<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>
 <head>
 	<meta charset="utf-8">
 	<link href="/news/css/1.css" rel="stylesheet" type="text/css">
 	<script type="text/javascript">
		function valName(){
			var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$","i");//创建模式对象
			var str1=document.getElementById("username").value;//获取文本框的内容
			
			if(document.getElementById("username").value==null || document.getElementById("username").value==""){
				document.getElementById("namespan").innerHTML="*不能为空";
				return false;
			}else
				return true;
		}
		
		function valPassword(){
			var str = document.getElementById("password").value;
			if(document.getElementById("password").value==null || document.getElementById("password").value==""){
				document.getElementById("passwordspan").innerHTML="*不能为空";
				return false;
			}else
				return true;
		}
		
		function changeCode(){
			var checkImgObj=document.getElementById("checkImg");
			//元素对象可以使用setAttribute方法来设置属性的值，或者getAttribute方法获取属性的值
			//网址后加如一个随机值rand，表示了不同的网址，防止缓存导致的图片内容不变				
			checkImgObj.setAttribute("src","/news/servlet/ImageCheckCodeServlet?rand="+Math.random());
		}			
				
		function submit1(){
			result1=valName();
			result1=valPassword() && result1;		
			if( result1)
				return true;//提交
			else 
				return false;//阻止提交
		}
		
	</script>
</head>

<body>

<form action="/news/servlet/UserServlet?type1=login" method="post" onsubmit="return submit1()">
	<div class="center" style="width:600px;margin-top:40px">
		<table  height="121" border="0" align="center">
			<tbody>
				<tr height="30">
					<td></td><td>登录</td>
				</tr>			
				<tr height="30">
					<td align="right">用户名：</td>
					<td align="left"><input type="text" name="name" id="name" onBlur="valName()"><span id="namespan"></span></td>
				</tr>
				<tr height="30">
					<td align="right">密码：</td>
					<td align="left"><input type="password" name="password" id="password" onBlur="valPassword()"><span id="passwordspan"></span></td>
				</tr>
			    <tr>
			      <td align="right">图形验证码：</td>
			      <td valign="middle"><input type="text" name="checkCode" id="checkCode" required="required">
			      		<img id="checkImg" onclick="changeCode();"  src="/news/servlet/ImageCheckCodeServlet?rand=-1" class="hand" /></td>
			    </tr>					
				<tr height="30">
					<td></td><td><input type="submit" value="    登  录    "/>
					&nbsp; &nbsp; &nbsp; &nbsp; <a href="findPassword.jsp">找回密码</a></td>
				</tr> 
			</tbody>
		</table>
	</div>
</form>

  </body>
</html>
