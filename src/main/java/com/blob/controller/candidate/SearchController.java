package com.blob.controller.candidate;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.enums.MenuTabEnum;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.service.candidate.ProfileService;

@Controller
public class SearchController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@Resource
	private ProfileService profileService;
	
	@RequestMapping(value="/vSearch", method=RequestMethod.GET)
	public ModelAndView vSearch(ModelMap model, @SortDefault("cityOrTown") Pageable pageable){
		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.search.toString(), user);
		//Page<ProfileSummary> page = new PageImpl<>(new ArrayList<>());
		//Pageable pageable = new PageRequest(1, 1);
		m.addAttribute("page", profileService.getProfiles(pageable));
		return new ModelAndView("/search", m.asMap());
	}
	
	@RequestMapping(value="/profiles", method=RequestMethod.GET)
	public ModelAndView profiles(ModelMap model, @SortDefault("cityOrTown") Pageable pageable){
//		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		model.addAttribute("page", profileService.getProfiles(pageable));
		return new ModelAndView("/search", model);
	}
}
