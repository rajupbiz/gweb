package com.blob.controller.sagai;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.blob.controller.BaseController;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.account.Account;
import com.blob.model.ui.ProfileFilter;
import com.blob.model.user.User;
import com.blob.security.SessionService;
import com.blob.service.sagai.ProfileService;
import com.blob.service.sagai.UserService;
import com.blob.util.GConstants;
import com.blob.util.GResponse;

@Controller
public class SearchController extends BaseController {

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
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/profile/search", method=RequestMethod.GET)
	public ModelAndView searchProfile(ModelMap model){
		
		logger.debug(" \n search profile .............  ");
		Model m = new ExtendedModelMap();
		Account a = getLoggedInAccount();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_search.toString(), a);
		ProfileFilter profileFilter = new ProfileFilter();
		profileFilter.setDegreeOptions(masterDegreeDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
		m.addAttribute("profileFilter", profileFilter);
		return new ModelAndView("/sagai/search", m.asMap());
	}
	
	@RequestMapping(value="/profiles/get", method=RequestMethod.GET)
	public @ResponseBody GResponse getProfiles(@ModelAttribute ProfileFilter profileFilter){
		
		logger.debug(" \n search profile .............  ");
		GResponse resp = new GResponse();
		Context ctx = new Context();
		User c = getLoggedInUser();
		profileFilter.setLoggedInUserId(c.getId());
		ctx.setVariable("page", profileService.getProfiles(request, profileFilter));
		
		String html = templateEngine.process("sagai/fragments/f-search-result", ctx);
		resp.setSuccess(true);
		resp.setData(html);
		//sessionService.addToSession(request.getSession(), SessionParamEnum.PROFILE_SEARCH_CURRENT_PAGE_NO.toString(), profileFilter.getPageNo());
		return resp;
	}
	
	@RequestMapping(value="/profile/view", method=RequestMethod.GET)
	public @ResponseBody GResponse viewProfile(@RequestParam("id") Long id){
		
		logger.debug(" \n view profile .............  "+id);
		GResponse resp = new GResponse();
		Context ctx = new Context();
		ctx.setVariable("profile", profileService.getSagaiProfile(id, request.getContextPath()));
		String html = templateEngine.process("sagai/fragments/f-view-profile", ctx);
		resp.setSuccess(true);
		resp.setData(html);
		//sessionService.addToSession(request.getSession(), SessionParamEnum.PROFILE_SEARCH_CURRENT_PAGE_NO.toString(), profileFilter.getPageNo());
		return resp;
	}
}
