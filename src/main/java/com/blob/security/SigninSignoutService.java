package com.blob.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.blob.dao.account.AccountDao;
import com.blob.model.account.Account;
import com.blob.util.DateUtils;

@Service
public class SigninSignoutService {
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
    private UserDetailsService userDetailService;
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private AuthenticationManager authenticationManager;

	public Account autoSignin(String username, String password, HttpServletRequest request) throws Exception {
		
		Account account = null;
		System.out.println(" auto signin ");
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			account = accountDao.findByUsername(username);
			sessionService.setLoginSessionData(request.getSession(), account);
			account.setLastLoggedIn(DateUtils.now());
			account = accountDao.save(account);
			
			//logger.debug(String.format("Auto login %s successfully!", username));
		}
		return account;
	}
	
	public void logout(){
		System.out.println(" logout ... ");
	}
}
