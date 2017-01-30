package com.blob.controller.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.dao.master.MasterServiceDao;
import com.blob.model.master.MasterService;
import com.blob.security.SessionService;
import com.blob.util.GConstants;


@Controller
public class CommonController extends BaseController {
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private MasterServiceDao masterServiceDao;
	
	@RequestMapping("/register")
	public ModelAndView register(){

		Model model = new ExtendedModelMap();
		System.out.println("Goto register");
		model.addAttribute("MENU_TAB", "register");
		return new ModelAndView("/index", model.asMap());
	}
	
	@RequestMapping("/services")
	public ModelAndView services(){

		Model model = new ExtendedModelMap();
		System.out.println("Goto services");
		model.addAttribute("MENU_TAB", "services");
		return new ModelAndView("/services", model.asMap());
	}
	
	@RequestMapping("/features")
	public ModelAndView features(){

		Model model = new ExtendedModelMap();
		System.out.println("Goto features");
		model.addAttribute("MENU_TAB", "features");
		return new ModelAndView("/features", model.asMap());
	}
	
	@RequestMapping("/how-it-works")
	public ModelAndView howItWorks(){

		Model model = new ExtendedModelMap();
		System.out.println("Goto how-it-works");
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

		default:
			break;
		}
		System.out.println("serviceId >> "+serviceId+" view >> "+view);
		return new ModelAndView("redirect:/"+view, m.asMap());
	}
}
