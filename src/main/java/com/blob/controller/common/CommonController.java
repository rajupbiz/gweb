package com.blob.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;


@Controller
public class CommonController extends BaseController {

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
}
