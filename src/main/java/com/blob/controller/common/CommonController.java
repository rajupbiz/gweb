package com.blob.controller.common;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.dao.master.MasterServiceDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.account.Account;
import com.blob.model.master.MasterService;
import com.blob.model.user.User;
import com.blob.security.SessionService;
import com.blob.service.sagai.UserService;
import com.blob.util.GConstants;


@Controller
public class CommonController extends BaseController {
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MasterServiceDao masterServiceDao;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/register")
	public ModelAndView register(){

		Model model = new ExtendedModelMap();
		log.debug("Goto register");
		model.addAttribute("MENU_TAB", "register");
		
		
		return new ModelAndView("/index", model.asMap());
	}
	
	@RequestMapping("/services")
	public ModelAndView services(){

		Model model = new ExtendedModelMap();
		log.debug("Goto services");
		model.addAttribute("MENU_TAB", "services");
		return new ModelAndView("/services", model.asMap());
	}
	
	@RequestMapping("/features")
	public ModelAndView features(){

		Model model = new ExtendedModelMap();
		log.debug("Goto features");
		model.addAttribute("MENU_TAB", "features");
		return new ModelAndView("/features", model.asMap());
	}
	
	@RequestMapping("/how-it-works")
	public ModelAndView howItWorks(){

		Model model = new ExtendedModelMap();
		log.debug("Goto how-it-works");
		model.addAttribute("MENU_TAB", "how-it-works");
		return new ModelAndView("/how-it-works", model.asMap());
	}
	
	@RequestMapping("/service/switch/{serviceId}")
	public ModelAndView serviceSwitch(@PathVariable Long serviceId){
		Model m = new ExtendedModelMap();
		//User user = getLoggedInUser();
		MasterService service = masterServiceDao.findOne(serviceId);
		sessionService.setServiceSwitchCommonAttribtesInSession(request.getSession(), service);
		
		String view = null;
		switch (service.getServiceName()) {
		case GConstants.Service_IDENTITY:
			view = "id/home";
			break;
			
		case GConstants.Service_SAGAI:
			view = "profile/home";
			break;
			
		case GConstants.Service_JOB:
			view = "job/home";
			break;

		case GConstants.Service_NA:
			view = "account/settings";
			break;
		default:
			break;
		}
		System.out.println("serviceId >> "+serviceId+" view >> "+view);
		return new ModelAndView("redirect:/"+view, m.asMap());
	}
	
	@RequestMapping("/account/settings")
	public ModelAndView accountSettings(){

		Model model = new ExtendedModelMap();
		log.debug("account settings ");
		Account account = getLoggedInAccount();
		User c = userService.getUserByAccount(account);
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.common_account_settings.toString(), account);
		return new ModelAndView("account-settings", model.asMap());
	}
}
