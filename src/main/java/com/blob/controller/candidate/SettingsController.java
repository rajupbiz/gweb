package com.blob.controller.candidate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.enums.MenuTabEnum;
import com.blob.model.common.User;
import com.blob.security.SessionService;

@Controller
public class SettingsController extends BaseController {
	
	@Resource
	private SessionService sessionService;

	@RequestMapping(value="/profile-privacy-settings", method=RequestMethod.GET)
	public ModelAndView vPrivacySettings(){

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		//sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.privacy_profile.toString(), user);
		return new ModelAndView("/privacy-settings", m.asMap());
	}
	
	@RequestMapping(value="/profile-account-settings", method=RequestMethod.GET)
	public ModelAndView vAccountSettings(){

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.more.toString(), user);
		return new ModelAndView("/account-settings", m.asMap());
	}
}
