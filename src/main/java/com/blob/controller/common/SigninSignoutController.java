package com.blob.controller.common;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.blob.dao.master.MasterServiceDao;
import com.blob.dao.user.UserDao;
import com.blob.model.error.InactiveAccountException;
import com.blob.model.master.MasterService;
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
	private MasterServiceDao masterServiceDao;
	
	@Resource
	private UserDao userDao;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public ModelAndView signIn(Model model){
		
		model = new ExtendedModelMap();
		log.debug(" signIn ");
		String signinUserName = request.getParameter("signinUserName");
		String signinPassword = request.getParameter("signinPassword");
		try{
			signinSignoutService.autoSignin(signinUserName, signinPassword, request);
			/*user = userDao.findByUsername(signinUserName);
			sessionService.setLoginSessionData(request.getSession(), user);
			user.setLastLoggedIn(DateUtils.now());
			user = userDao.save(user);*/
		}catch(UsernameNotFoundException | BadCredentialsException ube){
			ube.printStackTrace();
			model.addAttribute("signinUserName", signinUserName);
			model.addAttribute("signinPassword", signinPassword);
			model.addAttribute("FailureMsg", "User name or password is incorrect.");
			log.error(" Error occured: User name or password is incorrect. ", ube);
			return new ModelAndView("/index", model.asMap());
		}catch(InactiveAccountException iae){
			iae.printStackTrace();
			model.addAttribute("signinUserName", signinUserName);
			model.addAttribute("signinPassword", signinPassword);
			model.addAttribute("FailureMsg", "User is inactive, please contact administrator.");
			log.error(" Error occured: User is inactive, please contact administrator. "+signinUserName);
			return new ModelAndView("/index", model.asMap());
		}catch(Exception e){
			log.error(" Error occured: ", e);
			e.printStackTrace();
		}
		MasterService service = masterServiceDao.findByServiceNameAndStatus(GConstants.Service_IDENTITY, GConstants.Status_Active);
		sessionService.setServiceSwitchCommonAttribtesInSession(request.getSession(), service);
		//sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.home.toString());
		return new ModelAndView("redirect:/id/home", model.asMap());
		//return "redirect:/home";
	}
	
	@RequestMapping("/signout")
	public void signout(){
		
		log.debug(" signout ");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	}
}
