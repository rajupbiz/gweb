package com.blob.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blob.dao.account.AccountDao;
import com.blob.dao.account.AccountServicesDao;
import com.blob.dao.master.MasterServiceDao;
import com.blob.model.account.Account;
import com.blob.model.account.AccountServices;
import com.blob.model.master.MasterService;
import com.blob.util.GConstants;

@Service
public class AccountService {

	@Resource
	private AccountDao accountDao;
	
	@Resource
	private MasterServiceDao masterServiceDao;
	
	@Resource
	private AccountServicesDao accountServicesDao;
	
	public Boolean isAccountExists(String username){

		Account account = accountDao.findByUsername(username);
		if(account != null && account.getId() > 0){
			return true;
		}
		return false;
	}
	
	public List<AccountServices> saveAccountServices(Account a, List<String> services){
	
		List<AccountServices> accountServices = new ArrayList<>();
		for (String service : services) {
			MasterService masterService = masterServiceDao.findByServiceNameAndStatus(service, GConstants.Status_Active);
			AccountServices accService = new AccountServices();
			accService.setAccount(a);
			accService.setService(masterService);
			accService.setStatus(GConstants.Status_Active);
			accService.setCreateOn(new Date());
			accService.setUpdateOn(new Date());
			accountServices.add(accountServicesDao.save(accService));
		}
		return accountServices;
	}
}
