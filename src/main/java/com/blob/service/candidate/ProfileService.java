package com.blob.service.candidate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.blob.dao.candidate.CandidateAddressDao;
import com.blob.dao.candidate.CandidateContactDao;
import com.blob.dao.candidate.CandidateDao;
import com.blob.dao.candidate.CandidateEducationDao;
import com.blob.dao.candidate.CandidateOccupationDao;
import com.blob.dao.common.SystemPropertyDao;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.model.candidate.Candidate;
import com.blob.model.candidate.CandidateAddress;
import com.blob.model.candidate.CandidateContact;
import com.blob.model.candidate.CandidateEducation;
import com.blob.model.candidate.CandidateOccupation;
import com.blob.model.common.SystemProperty;
import com.blob.model.ui.ProfileFilter;
import com.blob.model.ui.ProfileSearchResult;
import com.blob.model.ui.ProfileSummary;
import com.blob.util.GConstants;

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
	
	@Resource
	private CandidateService candidateService;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@Resource
	private CandidateDao candidateDao;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;

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
	
	public ProfileSearchResult getProfiles(ProfileFilter profileFilter){
		
		ProfileSearchResult result = new ProfileSearchResult();
		int pageSize = 5;
		//StringBuffer whereClause = new StringBuffer();
		//whereClause.append(" c.gid = 'HA2603' ");  // profileFilter.getGid(), "%"+profileFilter.getName()+"%"
		
		Long totalRecords = candidateDao.countResult(profileFilter.getGid(), "%"+profileFilter.getName()+"%");		//	
		result.setTotalRecords(totalRecords);
		result.setTotalPages(Math.toIntExact((result.getTotalRecords() + pageSize - 1) / pageSize));
		if(profileFilter.getPageNo() != null && profileFilter.getPageNo() > 1){
			result.setCurrentPageNo(profileFilter.getPageNo());
		}else{
			result.setCurrentPageNo(1);
		}
		System.out.println(" totalRecords : "+totalRecords+"  pages : "+result.getTotalPages());
		final PageRequest page2 = new PageRequest(
				  result.getCurrentPageNo()-1, pageSize, new Sort(
				    new Order(Direction.ASC, "id")
				  )
				);
		List<BigInteger> candidateIdsList = candidateDao.searchCandidates(profileFilter.getGid(), "%"+profileFilter.getName()+"%", page2);
		StringBuffer candidateIdsStr = new StringBuffer();
		for (BigInteger id : candidateIdsList) {
			candidateIdsStr.append(id+",");
			//System.out.println(" id "+id);
		}
		List<ProfileSummary> content = new ArrayList<>(pageSize);
		if(CollectionUtils.isNotEmpty(candidateIdsList)){
			String candidateIds = candidateIdsStr.substring(0, candidateIdsStr.length() - 1);
			System.out.println(" candidateIds "+candidateIds);
			List<Object[]> rows = candidateDao.getCandidatesSummary(candidateIdsList);
			SystemProperty systemProperty = systemPropertyDao.findByListNameAndListKeyAndStatus(GConstants.ListName_FILE_UPLOAD_PATH, GConstants.ListKey_SAGAI_PHOTO, GConstants.Status_Active);
			if(systemProperty != null && StringUtils.isNotBlank(systemProperty.getListValue())){
				String directory = systemProperty.getListValue();
				
				for (Object[] row : rows) {
					ProfileSummary p = new ProfileSummary();
					p.setId(row[0].toString());
					p.setGid(row[1].toString());
					p.setFullName(row[3].toString() + (row[5] != null ? " "+row[5].toString() : "") + (row[4] != null ? " "+row[4].toString() : ""));
					//p.setFathersFullName((row[5] != null ? " "+ row[5].toString() : "") + (row[6] != null ? " "+row[6].toString() : "") + (row[7] != null ? " "+row[7].toString() : ""));
					p.setCurrentLocation((row[10] != null ? " "+ row[10].toString() : "") + (row[11] != null ? " "+row[11].toString() : ""));
					p.setHomeTown(row[9] != null ? row[9].toString() : "");
					p.setMamasTown(row[8] != null ? row[8].toString() : "");
					//p.setAbout(row[16] != null ? row[16].toString() : "");
					//String filepath = Paths.get("load-image", row[18] != null ? row[18].toString() : "").toString();
					String filepath = "load-image/"+(row[18] != null ? row[18].toString() : "");
					p.setPrimaryPicPath(filepath);
					p.setGender(row[20] != null ? row[20].toString() : "");
					//p.setDob(row[17].toString());
					//p.setEducation(row[14].toString() + " " + row[15].toString());
					//p.setOccupation(row[12].toString() + " " + row[13].toString());
					
					System.out.println("  >>  "+p.toString());
					
					content.add(p);
				}
			}
			result.setProfiles(content);
		}
		return result;
	}
}
