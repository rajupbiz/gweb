package com.blob.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.blob.dao.common.UserDao;
import com.blob.model.common.User;

@Controller
public class BaseController {

	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpServletResponse response;
	
	@Autowired
	protected UserDao userDao;
	
	public String getLoggedInUserName(){
		
		Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
		String userName = null;
		try {
			userName = authenticate.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}
	
	public Long getLoggedInUserId(){
		
		String userName = getLoggedInUserName();
		User user = userDao.findByUsername(userName);
		return user.getId();
	}
	
	public User getLoggedInUser(){
		
		String userName = getLoggedInUserName();
		return userDao.findByUsername(userName);
	}
	
	public String getCurrentService(){
		return (String) request.getSession().getAttribute("SERVICE");
	}
}
