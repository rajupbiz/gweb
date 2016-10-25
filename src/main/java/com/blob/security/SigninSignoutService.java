package com.blob.security;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.blob.dao.common.UserDao;

@Service
public class SigninSignoutService {
	
	@Resource
	private UserDao userDao;
	
	@Resource
    private UserDetailsService userDetailService;
	
	@Resource
	private AuthenticationManager authenticationManager;

	public void autoSignin(String username, String password) throws Exception {
		System.out.println(" auto signin ");
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			//logger.debug(String.format("Auto login %s successfully!", username));
		}
	}
	
	public void logout(){
		System.out.println(" logout ... ");
	}
}
