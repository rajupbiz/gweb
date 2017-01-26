package com.blob.security;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.common.SystemPropertyDao;
import com.blob.dao.common.UserServicesDao;
import com.blob.enums.SessionParamEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.common.GMessage;
import com.blob.model.common.Services;
import com.blob.model.common.SystemProperty;
import com.blob.model.common.User;
import com.blob.model.common.UserServices;
import com.blob.service.candidate.CandidateService;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;

@Service
public class SessionService {
	
	@Resource
	private CommonService commonService;

	@Resource
	private CandidateService candidateService;
	
	@Resource
	private UserServicesDao userServicesDao;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	public void setLoginSessionData(HttpSession session, User user) throws Exception {
		System.out.println(" set session data ");
		String currentService = GConstants.Service_SAGAI;
		List<UserServices> userServices = userServicesDao.findByUserAndStatus(user, GConstants.Status_Active);
		if(CollectionUtils.isNotEmpty(userServices)){
			for (UserServices userService : userServices) {
				if(userService != null && userService.getServices() != null && StringUtils.isNotBlank(userService.getServices().getServiceName())){
					currentService = userService.getServices().getServiceName();
				}
			}
		}
		String lastLoggedIn = null;
		session.setAttribute(SessionParamEnum.USER.toString(), user);
		session.setAttribute(SessionParamEnum.SERVICES.toString(), userServices);
		session.setAttribute(SessionParamEnum.SERVICE.toString(), currentService);
		if(DateUtils.toLocalDate(DateUtils.now()).equals(DateUtils.toLocalDate(user.getLastLoggedIn()))){
			lastLoggedIn = "today";
		}else{
			lastLoggedIn = DateUtils.getYearMonthDayBetweenDates(DateUtils.toLocalDate(user.getLastLoggedIn()), DateUtils.toLocalDate(DateUtils.now()))+" before";
		}
		session.setAttribute(SessionParamEnum.LAST_LOGGED_IN.toString(), lastLoggedIn);
		
		if(StringUtils.equalsIgnoreCase(currentService, GConstants.Service_SAGAI)){
			List<SystemProperty> sagaiDefaultProperties = systemPropertyDao.findByListNameAndStatusOrderByListOrder(GConstants.ListName_SAGAI_DEFAULT, GConstants.Status_Active);
			for (SystemProperty systemProperty : sagaiDefaultProperties) {
				session.setAttribute(systemProperty.getListName()+"_"+systemProperty.getListKey(), systemProperty.getListValue());
			}
		}
	}
	
	public void setMenuChangeCommonAttribtesInSession(HttpSession session, String tab, User user){
		Candidate candidate = null;
		Integer noOfUnreadMessages = 0, noOfShortlistedProfiles = 0;
		candidate = commonService.getCandidateByUser(user);
		if(candidate != null){
			List<GMessage> messages = candidateService.getCandidateMessages(candidate);
			if(CollectionUtils.isNotEmpty(messages)){
				noOfUnreadMessages = messages.size();
			}
			if(CollectionUtils.isNotEmpty(candidate.getShortlistedCandidates())){
				noOfShortlistedProfiles = candidate.getShortlistedCandidates().size();
			}
		}
		session.setAttribute(SessionParamEnum.NO_OF_UNREAD_MESSAGES.toString(), noOfUnreadMessages);
		session.setAttribute(SessionParamEnum.NO_OF_SHORTLISTED_PROFILES.toString(), noOfShortlistedProfiles);
		session.setAttribute(SessionParamEnum.TAB.toString(), tab);
	}
	
	public void setServiceSwitchCommonAttribtesInSession(HttpSession session, Services service, User user){
		session.setAttribute(SessionParamEnum.SERVICE.toString(), service.getServiceName());
	}
	
	public void addToSession(HttpSession session, String key, Object value){
		session.setAttribute(key, value);
	}
}
