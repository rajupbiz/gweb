package com.blob.controller.common;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.dao.common.ServicesDao;
import com.blob.dao.common.UserDao;
import com.blob.model.common.Services;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.security.SigninSignoutService;
import com.blob.util.GConstants;

@Controller
public class SigninSignoutController extends BaseController {

	@Resource
	private SigninSignoutService signinSignoutService;

	@Resource
	private SessionService sessionService;
	
	@Resource
	private ServicesDao servicesDao;
	
	@Resource
	private UserDao userDao;
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public ModelAndView signIn(Model model){
		
		model = new ExtendedModelMap();
		System.out.println(" signIn ............. ");
		String signinUserName = request.getParameter("signinUserName");
		String signinPassword = request.getParameter("signinPassword");
		User user = null;
		try{
			user = signinSignoutService.autoSignin(signinUserName, signinPassword, request);
			/*user = userDao.findByUsername(signinUserName);
			sessionService.setLoginSessionData(request.getSession(), user);
			user.setLastLoggedIn(DateUtils.now());
			user = userDao.save(user);*/
		}catch(UsernameNotFoundException ue){
			ue.printStackTrace();
			model.addAttribute("signinUserName", signinUserName);
			model.addAttribute("signinPassword", signinPassword);
			model.addAttribute("FailureMsg", "User name or password is incorrect.");
			return new ModelAndView("/index", model.asMap());
		}catch(Exception e){
			e.printStackTrace();
		}
		Services service = servicesDao.findByServiceNameAndStatus(GConstants.Service_IDENTITY, GConstants.Status_Active);
		sessionService.setServiceSwitchCommonAttribtesInSession(request.getSession(), service, user);
		//sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.home.toString(), user);
		return new ModelAndView("redirect:/id/home", model.asMap());
		//return "redirect:/home";
	}
	
	@RequestMapping("/signout")
	public void signout(){
		
		System.out.println(" signout ............. ");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	}
}
