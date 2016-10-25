package com.blob.service.common;

import java.util.List;

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
}
