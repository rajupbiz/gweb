package com.blob.controller.common;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.enums.MenuTabEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.service.common.SignupService;
import com.blob.util.GResponse;

@Controller
public class SignupController extends BaseController {

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Resource
	private SignupService signupService;
	
	@Resource
	private SessionService sessionService;
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public ModelAndView signup(){

		ModelAndView mv = new ModelAndView();
		System.out.println(" \n\n\n signup ............. ");
		String userName = request.getParameter("signupUserName");
		String password = request.getParameter("signupPassword");
		String email = request.getParameter("signupEmail");
		User user = new User();
		user.setUsername(userName);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setEmail(email);
		user.setStatus(StatusEnum.Active.toString());
		user.setCreateOn(new Date());
		user.setUpdateOn(new Date());
		GResponse resp = signupService.signup(user);
		if(resp != null){
			if(resp.isSuccess()){
				sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.home.toString(), user);
				mv = new ModelAndView("/home");
			}else{
				Model m = new ExtendedModelMap();
				m.addAttribute("ErrorMsg", resp.getError().getErrorMsg());
				m.addAttribute("userName", userName);
				m.addAttribute("password", password);
				m.addAttribute("email", email);
				mv = new ModelAndView("/index", m.asMap());
			}
		}
		return mv;
	}
}
