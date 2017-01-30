package com.blob.service.common;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.blob.dao.account.AccountDao;
import com.blob.dao.account.AccountRoleDao;
import com.blob.dao.master.MasterRoleDao;
import com.blob.dao.master.MasterServiceDao;
import com.blob.dao.user.UserDao;
import com.blob.dao.user.UserPersonalDao;
import com.blob.enums.RoleEnum;
import com.blob.model.account.Account;
import com.blob.model.account.AccountRole;
import com.blob.model.user.User;
import com.blob.model.user.UserPersonal;
import com.blob.util.GConstants;
import com.blob.util.GError;
import com.blob.util.GResponse;

@Service
public class SignupService {

	@Resource
	private AccountDao accountDao;
	
	@Resource
	private MasterRoleDao masterRoleDao;
	
	@Resource
	private AccountRoleDao accountRoleDao;
	
	@Resource
	private AccountService accountService;
	
	@Resource
	private MasterServiceDao masterServiceDao;
	
	@Resource
	private UserPersonalDao userPersonalDao;
	
	@Resource
	private UserDao userDao;
	
	@Transactional(value=TxType.REQUIRES_NEW, rollbackOn=Exception.class)
	public GResponse signup(Account account, String fname, String lname){
		
		GResponse resp = new GResponse();
		if(account != null){
			if(!accountService.isAccountExists(account.getUsername())){
				account = accountDao.save(account);
				if(account != null && account.getId() > 0){
					AccountRole accountRole = new AccountRole();
					accountRole.setAccount(account);
					accountRole.setRole(masterRoleDao.findByRoleName(RoleEnum.User.toString()));
					accountRole.setStatus(GConstants.Status_Active);
					accountRole.setCreateOn(new Date());
					accountRoleDao.save(accountRole);
					
					User user = new User();
					user.setAccount(account);
					user.setCreateOn(new Date());
					user.setStatus(GConstants.Status_Active);
					user.setGid(account.getGid());
					user.setUpdateOn(new Date());
					user = userDao.save(user);
					
					UserPersonal userPersonal = new UserPersonal();
					userPersonal.setUser(user);
					userPersonal.setFirstName(fname);
					userPersonal.setLastName(lname);
					userPersonal.setCreateOn(new Date());
					userPersonal.setUpdateOn(new Date());
					userPersonalDao.save(userPersonal);
					
					List<String> services = new ArrayList<>();
					services.add(GConstants.Service_IDENTITY);
					accountService.saveAccountServices(account, services);
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
