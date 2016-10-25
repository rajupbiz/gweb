package com.blob.security;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.common.UserServicesDao;
import com.blob.enums.ServicesEnum;
import com.blob.enums.SessionParamEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.common.GMessage;
import com.blob.model.common.User;
import com.blob.model.common.UserServices;
import com.blob.service.candidate.CandidateService;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;

@Service
public class SessionService {
	
	@Resource
	private CommonService commonService;

	@Resource
	private CandidateService candidateService;
	
	@Resource
	private UserServicesDao userServicesDao;
	
	public void setLoginSessionData(HttpSession session, User user) throws Exception {
		System.out.println(" set session data ");
		String currentService = ServicesEnum.IDENTITY.toString();
		List<UserServices> userServices = userServicesDao.findByUserAndStatus(user, StatusEnum.Active.toString());
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
	
}
