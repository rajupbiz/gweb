package com.blob.service.common;


import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.blob.dao.common.UserDao;
import com.blob.dao.common.UserRoleDao;
import com.blob.dao.master.MasterRoleDao;
import com.blob.enums.RoleEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.common.User;
import com.blob.model.common.UserRole;
import com.blob.util.GError;
import com.blob.util.GResponse;

@Service
public class SignupService {

	@Resource
	private UserDao userDao;
	
	@Resource
	private MasterRoleDao masterRoleDao;
	
	@Resource
	private UserRoleDao userRoleDao;
	
	@Resource
	private UserService userService;
	
	@Transactional(value=TxType.REQUIRES_NEW, rollbackOn=Exception.class)
	public GResponse signup(User user){
		
		GResponse resp = new GResponse();
		if(user != null){
			if(!userService.isUserExists(user.getUsername())){
				user = userDao.save(user);
				if(user != null && user.getId() > 0){
					UserRole userRole = new UserRole();
					userRole.setUser(user);
					userRole.setRole(masterRoleDao.findByRoleName(RoleEnum.User.toString()));
					userRole.setStatus(StatusEnum.Active.toString());
					userRole.setCreateOn(new Date());
					userRoleDao.save(userRole);
				}
				resp.setSuccess(true);
			}else{
				resp.setSuccess(false);
				GError e = new GError();
				e.setErrorMsg("User already exist!");
				resp.setError(e);
			}
		}
		return resp;
	}
}
