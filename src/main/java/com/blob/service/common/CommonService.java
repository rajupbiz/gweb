package com.blob.service.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.blob.model.candidate.Candidate;
import com.blob.model.common.User;

@Service
public class CommonService {

	public Candidate getCandidateByUser(User user){
	
		Candidate c = null;
		List<Candidate> candidates = user.getCandidates();
		if(CollectionUtils.isNotEmpty(candidates))
			c = candidates.get(0);
		return c;
	}
	
	public String getURLBase(HttpServletRequest request) throws MalformedURLException {
	    URL requestURL = new URL(request.getRequestURL().toString());
	    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
	    return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
	}
}
