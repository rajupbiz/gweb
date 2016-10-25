package com.blob.service.candidate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.candidate.CandidateDao;
import com.blob.enums.MessageCategoryEnum;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.common.GMessage;
import com.blob.model.common.GPhoto;
import com.blob.model.common.User;
import com.blob.util.DateUtils;
import com.blob.util.GUtils;

@Service
public class CandidateService {
	
	@Resource
	private GUtils gutils;
	
	@Resource
	private CandidateDao candidateDao;

	public Candidate getCandidateByUser(User user){
	
		Candidate c = null;
		List<Candidate> candidates = user.getCandidates();
		if(candidates != null && !candidates.isEmpty())
			c = candidates.get(0);
		return c;
	}
	
	public Boolean isCandidate(User user){
		
		Boolean c = false;
		List<Candidate> candidates = user.getCandidates();
		if(candidates != null && !candidates.isEmpty())
			c = true;
		return c;
	}
	
	public Candidate registerAsCandidate(User user){
		
		Candidate c = null;
		List<Candidate> candidates = user.getCandidates();
		if(candidates == null || candidates.isEmpty()){
			c = new Candidate();
			c.setUser(user);
			c.setGid(gutils.generateGid());
			c.setStatus(StatusEnum.Active.toString());
			c.setCreateOn(DateUtils.toDate(LocalDateTime.now()));
			c.setUpdateOn(DateUtils.toDate(LocalDateTime.now()));
			c = candidateDao.save(c);
		}
		return c;
	}
	
	public List<GMessage> getCandidateMessages(Candidate candidate){
		
		List<GMessage> messages = new ArrayList<>();
		List<GMessage> userMessages = candidate.getUser().getMessages();
		if(CollectionUtils.isNotEmpty(userMessages)){
			for (GMessage gMessage : userMessages) {
				if(gMessage != null 
						&& gMessage.getStatus().equalsIgnoreCase(StatusEnum.Active.toString())
						&& gMessage.getCategory().equalsIgnoreCase(MessageCategoryEnum.Candidate.toString())){
					messages.add(gMessage);
				}
			}
		}
		return messages;
	}
	
	public List<GPhoto> getCandidatePhotos(Candidate candidate){
		
		List<GPhoto> photos = null;
		List<GPhoto> userPhotos = candidate.getUser().getPhotos();
		if(CollectionUtils.isNotEmpty(userPhotos)){
			photos = new ArrayList<>();
			for (GPhoto gPhoto : userPhotos) {
				if(gPhoto != null 
						&& gPhoto.getStatus().equalsIgnoreCase(StatusEnum.Active.toString()) 
						&& gPhoto.getCategory().equalsIgnoreCase(PhotoCategoryEnum.Sagai.toString())){
					photos.add(gPhoto);
				}
			}
		}
		return photos;
	}
}
