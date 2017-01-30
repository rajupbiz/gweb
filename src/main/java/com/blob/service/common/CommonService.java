package com.blob.service.common;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.blob.model.account.Account;
import com.blob.model.user.User;

@Service
public class CommonService {

	public User getUserByAccount(Account account){
	
		User c = account.getUser();
		/*List<User> users = account.getUser();
		if(CollectionUtils.isNotEmpty(candidates))
			c = users.get(0);*/
		return c;
	}
	
	public String getURLBase(HttpServletRequest request) throws MalformedURLException {
	    URL requestURL = new URL(request.getRequestURL().toString());
	    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
	    return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
	}
}
                                                                                                                  