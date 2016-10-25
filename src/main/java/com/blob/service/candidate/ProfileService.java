package com.blob.service.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blob.dao.candidate.CandidateAddressDao;
import com.blob.dao.candidate.CandidateContactDao;
import com.blob.dao.candidate.CandidateEducationDao;
import com.blob.dao.candidate.CandidateOccupationDao;
import com.blob.model.candidate.Candidate;
import com.blob.model.candidate.CandidateAddress;
import com.blob.model.candidate.CandidateContact;
import com.blob.model.candidate.CandidateEducation;
import com.blob.model.candidate.CandidateOccupation;

@Service
public class ProfileService {
	
	@Resource
	private CandidateContactDao candidateContactDao;
	
	@Resource
	private CandidateAddressDao candidateAddressDao;
	
	@Resource
	private CandidateEducationDao candidateEducationDao;
	
	@Resource
	private CandidateOccupationDao candidateOccupationDao;

	public List<CandidateContact> saveCandidateContacts(List<CandidateContact> contacts, Candidate c){

		List<CandidateContact> resp = new ArrayList<>();
		if(contacts != null && !contacts.isEmpty()){
			for (CandidateContact candidateContact : contacts) {
				candidateContact.setCandidate(c);
				CandidateContact savedContact = candidateContactDao.save(candidateContact);
				resp.add(savedContact);
			}
		}
		return resp;
	}
	
	public List<CandidateAddress> saveCandidateAddress(List<CandidateAddress> addresses, Candidate c){
		
		List<CandidateAddress> resp = new ArrayList<>();
		if(addresses != null && !addresses.isEmpty()){
			for (CandidateAddress address : addresses) {
				address.setCandidate(c);
				CandidateAddress savedAddress = candidateAddressDao.save(address);
				resp.add(savedAddress);
			}
		}
		return resp;
	}
	
	public List<CandidateEducation> saveCandidateEducation(List<CandidateEducation> educations, Candidate c){

		List<CandidateEducation> resp = new ArrayList<>();
		if(educations != null && !educations.isEmpty()){
			for (CandidateEducation candidateEducation : educations) {
				candidateEducation.setCandidate(c);
				CandidateEducation savedEducation = candidateEducationDao.save(candidateEducation);
				resp.add(savedEducation);
			}
		}
		return resp;
	}
	
	public List<CandidateOccupation> saveCandidateOccupation(List<CandidateOccupation> occupations, Candidate c){

		List<CandidateOccupation> resp = new ArrayList<>();
		if(occupations != null && !occupations.isEmpty()){
			for (CandidateOccupation candidateOccupation : occupations) {
				candidateOccupation.setCandidate(c);
				CandidateOccupation savedOccupation = candidateOccupationDao.save(candidateOccupation);
				resp.add(savedOccupation);
			}
		}
		return resp;
	}
	
}
