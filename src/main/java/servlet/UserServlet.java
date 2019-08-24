package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.UserService;
import tools.Message;
import tools.PageInformation;
import tools.SearchTool;
import tools.Tool;
import bean.User;
import bean.Userinformation;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type=request.getParameter("type1");
		UserService userService=new UserService();
		Message message=new Message();
		if(type.equals("register")){
			User user=new User();
			user.setType(request.getParameter("type"));
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));
			user.setEmail(request.getParameter("email"));
			if(user.getType().equals("user"))
				user.setEnable("use");
			else
				user.setEnable("stop");		
			
			String checkCode = request.getParameter("checkCode");
			HttpSession session=request.getSession();
			String severCheckCode=(String)session.getAttribute("checkCode");//获取session中的验证码
			
			int result;
			if(severCheckCode==null ){//服务器端验证图片验证码不存在
				message.setResult(-3);
				message.setMessage("注册失败！服务器端验证图片验证码不存在，请重新注册！");
				message.setRedirectUrl("/news/user/free/login.jsp");	
			}else if(!severCheckCode.equals(checkCode)){//服务器端验证图片验证码验证失败
				message.setResult(-4);
				message.setMessage("注册失败！服务器端验证图片验证码验证失败，请重新注册！");
				message.setRedirectUrl("/news/user/free/login.jsp");	
			}else{//验证码验证正确	
				result=userService.register(user);
				message.setResult(result);
				if(result==1){
					message.setMessage("注册成功！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				}else if(result==0){
					message.setMessage("同名用户已存在，请改名重新注册！");
					message.setRedirectUrl("/news/user/free/register.jsp");
				}else if(result==-1){
					message.setMessage("电子邮箱已被使用，请换一个电子邮箱重新注册！");
					message.setRedirectUrl("/news/user/free/register.jsp");
				}else{
					message.setMessage("注册失败！请重新注册！");
					message.setRedirectUrl("/news/user/free/register.jsp");
				}
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request,response);
		}else if(type.equals("login")){
			String checkCode = request.getParameter("checkCode");
			HttpSession session=request.getSession();
			String severCheckCode=(String)session.getAttribute("checkCode");//获取session中的验证码
			
			int result;
			if(severCheckCode==null ){//服务器端验证图片验证码不存在
				message.setResult(-4);
				message.setMessage("登录失败！服务器端验证图片验证码不存在，请重新登录！");
				message.setRedirectUrl("/news/user/free/login.jsp");	
			}else if(!severCheckCode.equals(checkCode)){//服务器端验证图片验证码验证失败
				message.setResult(-5);
				message.setMessage("登录失败！服务器端验证图片验证码验证失败，请重新登录！");
				message.setRedirectUrl("/news/user/free/login.jsp");	
			}else{//验证码验证正确	
				User user=new User();
				user.setName(request.getParameter("name"));
				user.setPassword(request.getParameter("password"));
				user.setEnable(null);
				result=userService.login(user);
				message.setResult(result);
				if(result==1){//可以登录
					user.setPassword(null);//防止密码泄露
					request.getSession().setAttribute("user", user);
					String originalUrl=(String)request.getSession().getAttribute("originalUrl");
					
					if(originalUrl==null)
						response.sendRedirect("/news/user/manageUIMain/manageMain.jsp");
					else
						response.sendRedirect(originalUrl);
					
					return;//跳转到之前要访问的网页
				}else if(result==0){
					message.setMessage("密码错误！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				}else if(result==-1){
					message.setMessage("用户不存在！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				}else if(result==-2){
					message.setMessage("用户被停用！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				}else if(result==-3){
					message.setMessage("操作失败！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				}
			}	

			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request,response);			
		}else if(type.equals("exit")){
			request.getSession().removeAttribute("user");
			response.sendRedirect("/news/index.jsp");
		}else if(type.equals("showPage")){
			PageInformation pageInformation=new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			List<User> users=userService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userShow.jsp").forward(request,response);
		}else if(type.equals("search")){
			PageInformation pageInformation=new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(SearchTool.user(request));
			List<User> users=userService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userShow.jsp").forward(request,response);
		}else if(type.equals("check")){
			PageInformation pageInformation=new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			String id=pageInformation.getIds();
			pageInformation.setIds(null);
			List<User> users=userService.check(pageInformation, id);			
			if(users==null){
				message.setMessage("切换可用性失败，请联系管理员！");
				message.setRedirectUrl("/news/servlet/UserServlet?type1=check&page=1&pageSize=2");
			}else{
				request.setAttribute("pageInformation", pageInformation);
				request.setAttribute("users", users);
				getServletContext().getRequestDispatcher("/manager/userCheck.jsp").forward(request,response);
			}
		}else if(type.equals("delete")){
			PageInformation pageInformation=new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(" (type='user' or type='newsAuthor')");
			List<User> users=userService.deletes(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userDelete.jsp").forward(request,response);
		}else if(type.equals("showPrivate")){//显示普通用户个人信息
			User user=(User)request.getSession().getAttribute("user");
			if("user".equals(user.getType())){
				Userinformation userinformation=userService.getByUserId(user.getUserId());
				request.setAttribute("userinformation", userinformation);
			}			
			getServletContext().getRequestDispatcher("/user/manage/showPrivate.jsp").forward(request,response);			
		}else if(type.equals("changePrivate1")){//修改普通用户个人信息的第一步：显示可修改信息
			User user=(User)request.getSession().getAttribute("user");
			if("user".equals(user.getType())){
				Userinformation userinformation=userService.getByUserId(user.getUserId());
				request.setAttribute("userinformation", userinformation);
			}			
			getServletContext().getRequestDispatcher("/user/manage/changePrivate.jsp").forward(request,response);			
		}else if(type.equals("changePrivate2")){//修改普通用户个人信息的第二步：修改信息
			User user=(User)request.getSession().getAttribute("user");
			if("user".equals(user.getType())){
				Userinformation userinformation=new Userinformation();
				userinformation.setUserId(user.getUserId());
				userinformation.setSex(request.getParameter("sex"));
				userinformation.setHobby(request.getParameter("hobby"));
			}
			Integer result=userService.updatePrivate(user, request);
			message.setResult(result);
			if(result==5){
				message.setMessage("修改个人信息成功！");	
				message.setRedirectUrl("/news/servlet/UserServlet?type1=showPrivate");
			}else if(result==0){
				message.setMessage("修改个人信息失败，请联系管理员！");
				message.setRedirectUrl("/news/servlet/UserServlet?type1=showPrivate");
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request,response);							
		}else if(type.equals("byEmail")){//找回密码
			String email=request.getParameter("email");
			Integer rand=Tool.getRandomInRangeInteger(10, 100000);//随机数作为验证修改密码用
			int result=userService.findPasswordByEmail(email,rand);
			message.setResult(result);
			if(result==1){//发送邮件成功
				HttpSession session=request.getSession();
				session.setAttribute("email",email);
				session.setAttribute("rand", rand);
				session.setAttribute("time", new Date());
				message.setMessage("发送邮件成功！请登录邮箱确认。");	
				message.setRedirectUrl("/news/user/free/login.jsp");				
			}else if(result==-1){
				message.setMessage("发送邮件失败！请重新尝试。");	
				message.setRedirectUrl("/news/user/free/findPassword.jsp");					
			}else if(result==-2){
				message.setMessage("该邮件地址未被注册过！请重新尝试。");	
				message.setRedirectUrl("/news/user/free/findPassword.jsp");					
			}else if(result==0){
				message.setMessage("数据库操作失败！请重新尝试。");	
				message.setRedirectUrl("/news/user/free/findPassword.jsp");					
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request,response);				
		}else if(type.equals("newPassword")){//设置新密码			
			String rand=(String)request.getParameter("rand");			
			HttpSession session=request.getSession();
			Integer trueRand=(Integer)session.getAttribute("rand");	
			
			User user=new User();
			user.setEmail((String)session.getAttribute("email"));
			user.setPassword(request.getParameter("password"));
			Date old=(Date)session.getAttribute("time");
			
			if(!rand.equals(trueRand.toString())){//rand值不对，无权限修改密码
				message.setMessage("rand值不对，无权限修改密码！请重新尝试。");	
				message.setRedirectUrl("/news/user/free/findPassword.jsp");	
			}else if(old==null || Tool.getSecondFromNow(old)>300 ){
				message.setMessage("超时！请重新尝试。");	
				message.setRedirectUrl("/news/user/free/findPassword.jsp");	
			}else {			
				if(userService.updatePassword(user)==1){
					message.setMessage("修改密码成功！");	
					message.setRedirectUrl("/news/user/free/login.jsp");
				}else{
					message.setMessage("修改密码失败！请重新尝试。");	
					message.setRedirectUrl("/news/user/free/findPassword.jsp");
				}	
			}
			
			session.removeAttribute("email");//删除session数据
			session.removeAttribute("rand");
			session.removeAttribute("time");	
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request,response);	
		}
	}
}
