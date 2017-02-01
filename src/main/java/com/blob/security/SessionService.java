package com.blob.security;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.account.AccountServicesDao;
import com.blob.dao.common.SystemPropertyDao;
import com.blob.enums.SessionParamEnum;
import com.blob.model.account.Account;
import com.blob.model.account.AccountServices;
import com.blob.model.common.SystemProperty;
import com.blob.model.master.MasterService;
import com.blob.model.user.User;
import com.blob.service.common.CommonService;
import com.blob.service.sagai.UserService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;

@Service
public class SessionService {
	
	@Resource
	private CommonService commonService;

	@Resource
	private UserService userService;
	
	@Resource
	private AccountServicesDao accountServiceDao;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	public void setLoginSessionData(HttpSession session, Account account) throws Exception {
		System.out.println(" set session data ");
		String currentService = GConstants.Service_IDENTITY;
		List<AccountServices> accountServices = accountServiceDao.findByAccountAndStatus(account, GConstants.Status_Active);
		if(CollectionUtils.isNotEmpty(accountServices)){
			for (AccountServices accountService : accountServices) {
				if(accountService != null && accountService.getService() != null && StringUtils.isNotBlank(accountService.getService().getServiceName())){
					currentService = accountService.getService().getServiceName();
				}
			}
		}
		
		User user = userService.getUserByAccount(account);
		
		String lastLoggedIn = null;
		session.setAttribute(SessionParamEnum.ACCOUNT.toString(), account);
		session.setAttribute(SessionParamEnum.USER.toString(), user);
		session.setAttribute(SessionParamEnum.SERVICES.toString(), accountServices);
		session.setAttribute(SessionParamEnum.SERVICE.toString(), currentService);
		if(account.getLastLoggedIn() == null || DateUtils.toLocalDate(DateUtils.now()).equals(DateUtils.toLocalDate(account.getLastLoggedIn()))){
			lastLoggedIn = "today";
		}else{
			lastLoggedIn = DateUtils.getYearMonthDayBetweenDates(DateUtils.toLocalDate(account.getLastLoggedIn()), DateUtils.toLocalDate(DateUtils.now()))+" before";
		}
		session.setAttribute(SessionParamEnum.LAST_LOGGED_IN.toString(), lastLoggedIn);
	}
	
	public void setMenuChangeCommonAttribtesInSession(HttpSession session, String tab, Account account){
		User user = commonService.getUserByAccount(account);
		if(user != null){
			/*List<UserMessage> messages = userService.getUserMessages(user);
			if(CollectionUtils.isNotEmpty(messages)){
				noOfUnreadMessages = messages.size();
			}*/
			if(StringUtils.startsWith(tab, "sagai")){
				Integer noOfShortlistedProfiles = 0;
				if(CollectionUtils.isNotEmpty(user.getSagaiShortlistedProfiles())){
					noOfShortlistedProfiles = user.getSagaiShortlistedProfiles().size();
					session.setAttribute(SessionParamEnum.NO_OF_SHORTLISTED_PROFILES.toString(), noOfShortlistedProfiles);
				}
			}
		}
		//session.setAttribute(SessionParamEnum.NO_OF_UNREAD_MESSAGES.toString(), noOfUnreadMessages);
		session.setAttribute(SessionParamEnum.TAB.toString(), tab);
	}
	
	public void setServiceSwitchCommonAttribtesInSession(HttpSession session, MasterService service){
		session.setAttribute(SessionParamEnum.SERVICE.toString(), service.getServiceName());
		
		switch (service.getServiceName()) {
		case GConstants.Service_IDENTITY:
			
			break;
			
		case GConstants.Service_JOB:
					
			break;
			
		case GConstants.Service_SAGAI:
			List<SystemProperty> sagaiDefaultProperties = systemPropertyDao.findByListNameAndStatusOrderByListOrder(GConstants.ListName_SAGAI_DEFAULT, GConstants.Status_Active);
			for (SystemProperty systemProperty : sagaiDefaultProperties) {
				session.setAttribute(systemProperty.getListName()+"_"+systemProperty.getListKey(), systemProperty.getListValue());
			}
			break;

		default:
			break;
		}
		
	}
	
	public void addToSession(HttpSession session, String key, Object value){
		session.setAttribute(key, value);
	}
}
