package com.blob.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.blob.dao.common.UserDao;
import com.blob.model.common.User;
import com.blob.util.DateUtils;

@Service
public class SigninSignoutService {
	
	@Resource
	private UserDao userDao;
	
	@Resource
    private UserDetailsService userDetailService;
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private AuthenticationManager authenticationManager;

	public User autoSignin(String username, String password, HttpServletRequest request) throws Exception {
		
		User user = null;
		System.out.println(" auto signin ");
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			user = userDao.findByUsername(username);
			sessionService.setLoginSessionData(request.getSession(), user);
			user.setLastLoggedIn(DateUtils.now());
			user = userDao.save(user);
			
			//logger.debug(String.format("Auto login %s successfully!", username));
		}
		return user;
	}
	
	public void logout(){
		System.out.println(" logout ... ");
	}
}
