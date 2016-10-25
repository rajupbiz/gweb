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

import com.blob.dao.common.UserDao;
import com.blob.model.common.User;
import com.blob.model.common.UserRole;

@Component
@Transactional
public class UserDetailService implements UserDetailsService {
	
	@Resource
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		System.out.println(" \n\n\n loadUserByUsername ... ");
		UserDetails userDetails = null;
		User user = userDao.findByUsername(userName);
		if(user == null){
			throw new UsernameNotFoundException("User name or password is incorrect.");
		}else{
			userDetails = getUserDetails(user.getUsername(), user.getPassword(), user.getUserRoles());
		}
		return userDetails;
	}
	
	private UserDetails getUserDetails(String userName, String password, List<UserRole> roles){
		
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
	
	private Collection<GrantedAuthority> getAuthorities(List<UserRole> roles){
		
		List<GrantedAuthority> lstAuth = null;
		if(roles != null && !roles.isEmpty()){
			lstAuth = new ArrayList<GrantedAuthority>(roles.size());
			for (UserRole role : roles) {
				if(role != null){
					lstAuth.add(new SimpleGrantedAuthority(role.getRole().getId().toString()));
				}
			}
		}
		return lstAuth;
	}
}
