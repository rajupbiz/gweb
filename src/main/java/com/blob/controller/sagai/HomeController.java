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
import com.blob.dao.common.UserDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.security.SigninSignoutService;
import com.blob.service.candidate.CandidateService;
import com.blob.service.candidate.CandidateUIService;

@Controller
public class HomeController extends BaseController {

	@Resource
	private SigninSignoutService loginLogoutService;

	@Resource
	private SessionService sessionService;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private CandidateUIService candidateUIService;
	
	@Resource
	private CandidateService candidateService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/profile/home")
	public ModelAndView profileHome(){
		Model m = new ExtendedModelMap();
		logger.debug(" \n profile home .............  ");
		System.out.println(" \n profile home ............. ");
		
		User user = getLoggedInUser();
		Candidate c = candidateService.getCandidateByUser(user);
		m.addAttribute("dashboard", candidateUIService.getDashboardInfoForUI(c, request.getContextPath()));
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_home.toString(), user);
		return new ModelAndView("/sagai/home", m.asMap());
	}
}