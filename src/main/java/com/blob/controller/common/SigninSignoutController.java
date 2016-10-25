package com.blob.controller.common;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blob.controller.BaseController;
import com.blob.dao.common.UserDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.security.SigninSignoutService;
import com.blob.util.DateUtils;

@Controller
public class SigninSignoutController extends BaseController {

	@Resource
	private SigninSignoutService signinSignoutService;

	@Resource
	private SessionService sessionService;
	
	@Resource
	private UserDao userDao;
	
	@RequestMapping("/register")
	public String register(){

		System.out.println(" \n\n\n register ............. ");
		
		
		
		return "index";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public String signIn(){
		
		System.out.println(" \n\n\n signIn ............. ");
		String signinUserName = request.getParameter("signinUserName");
		String signinPassword = request.getParameter("signinPassword");
		User user = null;
		try{
			signinSignoutService.autoSignin(signinUserName, signinPassword);
			user = userDao.findByUsername(signinUserName);
			sessionService.setLoginSessionData(request.getSession(), user);
			user.setLastLoggedIn(DateUtils.now());
			user = userDao.save(user);
		}catch(UsernameNotFoundException ue){
			ue.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(" \n\n\n signIn 11 ............. ");
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.home.toString(), user);
		return "redirect:/vHome";
	}
	
	@RequestMapping("/signout")
	public void signout(){
		System.out.println(" \n\n\n signout ............. ");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
		
	}
}
