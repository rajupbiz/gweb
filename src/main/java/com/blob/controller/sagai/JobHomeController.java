package com.blob.controller.sagai;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import com.blob.controller.BaseController;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.account.Account;
import com.blob.security.SessionService;
import com.blob.service.sagai.ProfileService;
import com.blob.service.sagai.UserService;

@Controller
public class JobHomeController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@Resource
	private ProfileService profileService;
	
	@Resource
	private UserService userService;
	
	@Resource
	protected TemplateEngine templateEngine;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@RequestMapping("/job/home")
	public ModelAndView jobHome(){

		Model m = new ExtendedModelMap();
		System.out.println("job home");
		Account account = getLoggedInAccount();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.job_home.toString(), account);
		return new ModelAndView("/job/job-home", m.asMap());
	}
}
