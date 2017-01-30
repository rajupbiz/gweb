package com.blob.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.blob.dao.account.AccountDao;
import com.blob.dao.account.AccountRoleDao;
import com.blob.model.account.Account;
import com.blob.model.account.AccountRole;
import com.blob.util.GConstants;

@Component
@Transactional
public class UserDetailService implements UserDetailsService {
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
	private AccountRoleDao accountRoleDao;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		System.out.println(" \n\n\n loadUserByUsername ... ");
		UserDetails userDetails = null;
		Account account = accountDao.findByUsername(userName);
		if(account == null){
			throw new UsernameNotFoundException("User name or password is incorrect.");
		}else{
			List<AccountRole> roles = accountRoleDao.findByAccountAndStatus(account, GConstants.Status_Active);
			userDetails = getAccountDetails(account.getUsername(), account.getPassword(), roles);
		}
		return userDetails;
	}
	
	private UserDetails getAccountDetails(String userName, String password, List<AccountRole> roles){
		
		System.out.println(" \n\n\n getUserDetails ... ");
		UserDetails userDetails = null;
		try{
			userDetails = new org.springframework.security.core.userdetails.User(userName, password, true, true, true, true, getAuthorities(roles));
		}catch(UsernameNotFoundException e){
			e.printStackTrace();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		return userDetails;
	}
	
	private Collection<GrantedAuthority> getAuthorities(List<AccountRole> roles){
		
		List<GrantedAuthority> lstAuth = null;
		if(roles != null && !roles.isEmpty()){
			lstAuth = new ArrayList<GrantedAuthority>(roles.size());
			for (AccountRole role : roles) {
				if(role != null){
					lstAuth.add(new SimpleGrantedAuthority(role.getRole().getId().toString()));
				}
			}
		}
		return lstAuth;
	}
}
