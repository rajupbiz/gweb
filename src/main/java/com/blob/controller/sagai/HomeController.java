package com.blob.controller.sagai;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.dao.user.UserDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.account.Account;
import com.blob.model.user.User;
import com.blob.security.SessionService;
import com.blob.security.SigninSignoutService;
import com.blob.service.sagai.UserService;
import com.blob.service.sagai.UserUIService;

@Controller
public class HomeController extends BaseController {

	@Resource
	private SigninSignoutService loginLogoutService;

	@Resource
	private SessionService sessionService;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private UserUIService userUIService;
	
	@Resource
	private UserService userService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/profile/home")
	public ModelAndView profileHome(){
		Model m = new ExtendedModelMap();
		log.debug("profile home ");
		Account account = getLoggedInAccount();
		User c = userService.getUserByAccount(account);
		m.addAttribute("dashboard", userUIService.getDashboardInfoForUI(c, request.getContextPath()));
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_home.toString(), account);
		return new ModelAndView("/sagai/home", m.asMap());
	}
}
