package com.blob.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import com.blob.dao.common.UserDao;

@Component
public class GSessionListener extends HttpSessionEventPublisher {

	@Resource
	private UserDao userDao;
	
	@Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		super.sessionCreated(httpSessionEvent);
    	System.out.println("  session created  ");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //HttpSession httpSession = httpSessionEvent.getSession();
        //SecurityContext securityContext = (SecurityContextImpl) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
    	//httpSessionEvent.getSession().invalidate();
    	/*SessionRegistry sessionRegistry = getSessionRegistry();
	    SessionInformation sessionInfo = (sessionRegistry != null ? sessionRegistry.getSessionInformation(httpSessionEvent.getSession().getId()) : null);
	    UserDetails ud = null;
	    if (sessionInfo != null) {
	        ud = (UserDetails) sessionInfo.getPrincipal();
	    }
	    if (ud != null) {
	               // Do my stuff
	    }*/
    	super.sessionDestroyed(httpSessionEvent);
    	System.out.println("  session destroyed  ");
    }
    
}