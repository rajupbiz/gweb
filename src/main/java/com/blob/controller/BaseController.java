package com.blob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.blob.dao.common.UserDao;
import com.blob.enums.SessionParamEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.common.User;
import com.blob.service.sagai.CandidateService;

@Controller
public class BaseController {

	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpServletResponse response;
	
	@Autowired
	protected UserDao userDao;
	
	@Resource
	private CandidateService candidateService;
	
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
	
	public Candidate getLoggedInSagaiCandidate(){
		
		Candidate c = candidateService.getCandidateByUser(getLoggedInUser());
		return c;
	}
	
	public String getCurrentService(){
		return (String) request.getSession().getAttribute(SessionParamEnum.SERVICE.toString());
	}
}
