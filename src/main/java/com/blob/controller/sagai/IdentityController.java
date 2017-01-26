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
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.service.candidate.CandidateService;
import com.blob.service.candidate.ProfileService;

@Controller
public class IdentityController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@Resource
	private ProfileService profileService;
	
	@Resource
	private CandidateService candidateService;
	
	@Resource
	protected TemplateEngine templateEngine;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@RequestMapping("/id/home")
	public ModelAndView idHome(){

		Model m = new ExtendedModelMap();
		System.out.println("id home");
		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.id_home.toString(), user);
		return new ModelAndView("/id/id-home", m.asMap());
	}
}
