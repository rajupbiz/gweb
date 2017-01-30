package com.blob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.blob.dao.account.AccountDao;
import com.blob.enums.SessionParamEnum;
import com.blob.model.account.Account;
import com.blob.model.user.User;
import com.blob.service.sagai.UserService;

@Controller
public class BaseController {

	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpServletResponse response;
	
	@Autowired
	protected AccountDao accountDao;
	
	@Resource
	private UserService userService;
	
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
		Account account = accountDao.findByUsername(userName);
		return account.getId();
	}
	
	public Account getLoggedInAccount(){
		
		String userName = getLoggedInUserName();
		return accountDao.findByUsername(userName);
	}
	
	public User getLoggedInUser(){
		
		User c = userService.getUserByAccount(getLoggedInAccount());
		return c;
	}
	
	public String getCurrentService(){
		return (String) request.getSession().getAttribute(SessionParamEnum.SERVICE.toString());
	}
}
