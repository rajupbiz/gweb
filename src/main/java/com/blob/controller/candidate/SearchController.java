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
public class SearchController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@RequestMapping(value="/vSearch", method=RequestMethod.GET)
	public ModelAndView vSearch(){

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.search.toString(), user);
		return new ModelAndView("/search", m.asMap());
	}
}
