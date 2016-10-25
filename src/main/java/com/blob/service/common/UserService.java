package com.blob.service.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blob.dao.common.UserDao;
import com.blob.model.common.User;

@Service
public class UserService {

	@Resource
	private UserDao userDao;
	
	public Boolean isUserExists(String username){

		User user = userDao.findByUsername(username);
		if(user != null && user.getId() > 0){
			return true;
		}
		return false;
	}
}
