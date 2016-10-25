package com.blob.service.candidate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.candidate.CandidateAddressDao;
import com.blob.dao.candidate.CandidateContactDao;
import com.blob.dao.candidate.CandidateEducationDao;
import com.blob.dao.candidate.CandidateOccupationDao;
import com.blob.dao.common.GPhotoDao;
import com.blob.dao.master.MasterBloodGroupDao;
import com.blob.dao.master.MasterCountryDao;
import com.blob.dao.master.MasterDayOfWeekDao;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.dao.master.MasterDegreeSpecializationDao;
import com.blob.dao.master.MasterDesignationDao;
import com.blob.dao.master.MasterMaritalStatusDao;
import com.blob.dao.master.MasterOccupationDao;
import com.blob.dao.master.MasterRelationshipDao;
import com.blob.dao.master.MasterStateDao;
import com.blob.dao.master.MasterYearlyIncomeDao;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.candidate.CandidateAddress;
import com.blob.model.candidate.CandidateAstroDetail;
import com.blob.model.candidate.CandidateContact;
import com.blob.model.candidate.CandidateEducation;
import com.blob.model.candidate.CandidateFamily;
import com.blob.model.candidate.CandidateOccupation;
import com.blob.model.candidate.CandidatePersonalDetail;
import com.blob.model.candidate.CandidateShortlistedProfile;
import com.blob.model.common.GMessage;
import com.blob.model.common.GPhoto;
import com.blob.model.common.User;
import com.blob.model.ui.ContactInfo;
import com.blob.model.ui.DashboardInfo;
import com.blob.model.ui.EduOccuInfo;
import com.blob.model.ui.FamilyInfo;
import com.blob.model.ui.Message;
import com.blob.model.ui.PersonalInfo;
import com.blob.model.ui.Photo;
import com.blob.model.ui.PhotoInfo;
import com.blob.model.ui.ShortlistedProfile;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;
import com.blob.util.GConverter;
import com.blob.util.UiUtils;

@Service
public class CandidateUIService {
	
	@Resource
	private MasterBloodGroupDao masterBloodGroupDao;
	
	@Resource
	private MasterMaritalStatusDao masterMaritalStatusDao;
	
	@Resource
	private MasterDayOfWeekDao masterDayOfWeekDao;
	
	@Resource
	private MasterRelationshipDao masterRelationshipDao;
	
	@Resource
	private MasterStateDao masterStateDao;
	
	@Resource
	private MasterCountryDao masterCountryDao;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@Resource
	private MasterDegreeSpecializationDao masterDegreeSpecializationDao;
	
	@Resource
	private MasterDesignationDao masterDesignationDao;
	
	@Resource
	private MasterOccupationDao masterOccupationDao;
	
	@Resource
	private MasterYearlyIncomeDao masterYearlyIncomeDao;
	
	@Resource
	private CandidateContactDao candidateContactDao;
	
	@Resource
	private CandidateAddressDao candidateAddressDao;
	
	@Resource
	private CandidateEducationDao candidateEducationDao;
	
	@Resource
	private CandidateOccupationDao candidateOccupationDao;
	
	@Resource
	private UiUtils uiUtils;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private CandidateService candidateService;
	
	@Resource
	private GPhotoDao gPhotoDao;
	
	public PersonalInfo getPersonalInfoSectionForUI(Candidate candidate){
		PersonalInfo pi = new PersonalInfo();
		if(candidate != null){
			CandidatePersonalDetail pd = candidate.getCandidatePersonalDetail();
			CandidateAstroDetail ad = candidate.getCandidateAstroDetail();
			if(pd != null){
				pi.setFirstName(pd.getFirstName());
				pi.setMiddleName(pd.getMiddleName());
				pi.setLastName(pd.getLastName());
				pi.setGender(pd.getGender());
				if(pd.getMaritalStatus() != null){
					pi.setMaritalStatus(pd.getMaritalStatus().getMaritalStatus());
					pi.setMaritalStatusId(pd.getMaritalStatus().getId());
				}
				pi.setHeightCms(pd.getHeight());
				if(pd.getHeight() != null && pd.getHeight() > 0)
				pi.setHeightFoot("("+GConverter.convertCmsToFoot(pd.getHeight())+")");
				pi.setWeight(pd.getWeight());
				if(pd.getBloodGroup() != null)
				pi.setBloodGroup(pd.getBloodGroup().getBloodGroupName());
				pi.setHobby(pd.getHobby());
				pi.setAboutMe(pd.getAboutMe());
			}
			if(ad != null){
				pi.setBirthName(ad.getBirthName());
				pi.setBirthPlace(ad.getBirthPlace());
				pi.setBirthDate(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(ad.getBirthDate())));
				pi.setBirthTime(DateUtils.toHHMM_AMPM(DateUtils.toLocalTime(ad.getBirthTime())));
				if(ad.getBirthDayOfWeek() != null){
					pi.setBirthDay(ad.getBirthDayOfWeek().getDayOfWeek());
				}
				if(ad.isMangal() != null)
				pi.setIsMangal(ad.isMangal() ? GConstants.Mangal_Yes : GConstants.Mangal_No);
			}
		}
		
		pi.setMaritalStatusOptions(masterMaritalStatusDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		pi.setBirthDayOptions(masterDayOfWeekDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		pi.setBloodGroupOptions(masterBloodGroupDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		return pi;
	}
	
	public CandidatePersonalDetail getCandidatePersonalInfoFromUI(PersonalInfo pi){
		CandidatePersonalDetail pd = null;
		if(pi != null){
			pd = new CandidatePersonalDetail();
			pd.setFirstName(pi.getFirstName());
			pd.setLastName(pi.getLastName());
			pd.setMiddleName(pi.getMiddleName());
			pd.setGender(pi.getGender());
			if(pi.getMaritalStatusId() != null && pi.getMaritalStatusId() > 0)
				pd.setMaritalStatus(masterMaritalStatusDao.findOne(pi.getMaritalStatusId()));
			pd.setHeight(pi.getHeightCms());
			pd.setWeight(pi.getWeight());
			if(pi.getBloodGroupId() != null && pi.getBloodGroupId() > 0)
				pd.setBloodGroup(masterBloodGroupDao.findOne(pi.getBloodGroupId()));
			pd.setHobby(pi.getHobby());
			pd.setAboutMe(pi.getAboutMe());
		}
		return pd;
	}
	
	public CandidateAstroDetail getCandidateAstroDetailFromUI(PersonalInfo pi){
		CandidateAstroDetail ad = null;
		if(pi != null){
			ad = new CandidateAstroDetail();
			ad.setBirthName(pi.getBirthName());
			ad.setBirthPlace(pi.getBirthPlace());
			ad.setBirthDate(DateUtils.toDate(DateUtils.toLocalDate(pi.getBirthDate())));
			ad.setBirthTime(DateUtils.toDate(DateUtils.toLocalTime(pi.getBirthTime())));
			if(StringUtils.isNoneBlank(pi.getBirthDay()))
				ad.setBirthDayOfWeek(masterDayOfWeekDao.findByDayOfWeek(pi.getBirthDay()));
			if(StringUtils.isNotBlank(pi.getIsMangal()))
				ad.setMangal(pi.getIsMangal().equalsIgnoreCase(GConstants.Mangal_No)? false : true);
		}
		return ad;
	}
	
	public FamilyInfo getFamilyInfoSectionForUI(Candidate candidate){
		FamilyInfo fi = new FamilyInfo();
		if(candidate != null){
			CandidateFamily f = candidate.getCandidateFamily();
			if(f != null){
				fi.setFatherFirstName(f.getFatherFirstName());
				fi.setFatherMiddleName(f.getFatherMiddleName());
				fi.setFatherLastName(f.getFatherLastName());
				fi.setMotherFirstName(f.getMotherFirstName());
				if(f.getNoOfBrother() != null && f.getNoOfBrother() > 0){
					fi.setNoOfBrother(f.getNoOfBrother());
				}
				if(f.getNoOfMarriedBrother() != null && f.getNoOfMarriedBrother() > 0){
					fi.setNoOfMarriedBrother(f.getNoOfMarriedBrother());
				}
				if(f.getNoOfSister() != null && f.getNoOfSister() > 0){
					fi.setNoOfSister(f.getNoOfSister());
				}
				if(f.getNoOfMarriedSister() != null && f.getNoOfMarriedSister() > 0){
					fi.setNoOfMarriedSister(f.getNoOfMarriedSister());
				}
				fi.setFamilyOccupation(f.getFamilyOccupation());
				fi.setFamilyWealth(f.getFamilyWealth());
				fi.setMamasFirstName(f.getMamasFirstName());
				fi.setMamasMiddleName(f.getMamasMiddleName());
				fi.setMamasLastName(f.getMamasLastName());
				fi.setMamasNativePlace(f.getMamasNativePlace());
				fi.setMamasCurrentPlace(f.getMamasCurrentPlace());
				fi.setAboutFamily(f.getAboutFamily());
			}
		}
		return fi;
	}
	
	public CandidateFamily getFamilyInfoFromUI(FamilyInfo fi){
		CandidateFamily f = null;
		if(fi != null){
			f = new CandidateFamily();
			f.setFatherFirstName(fi.getFatherFirstName());
			f.setFatherMiddleName(fi.getFatherMiddleName());
			f.setFatherLastName(fi.getFatherLastName());
			f.setMotherFirstName(fi.getMotherFirstName());
			f.setNoOfBrother(fi.getNoOfBrother());
			f.setNoOfMarriedBrother(fi.getNoOfMarriedBrother());
			f.setNoOfSister(fi.getNoOfSister());
			f.setNoOfMarriedSister(fi.getNoOfMarriedSister());
			f.setFamilyOccupation(fi.getFamilyOccupation());
			f.setFamilyWealth(fi.getFamilyWealth());
			f.setMamasFirstName(fi.getMamasFirstName());
			f.setMamasMiddleName(fi.getMamasMiddleName());
			f.setMamasLastName(fi.getMamasLastName());
			f.setMamasNativePlace(fi.getMamasNativePlace());
			f.setMamasCurrentPlace(fi.getMamasCurrentPlace());
			f.setAboutFamily(fi.getAboutFamily());
		}
		return f;
	}
	
	public ContactInfo getContactInfoSectionForUI(Candidate candidate){
		ContactInfo contactInfo = new ContactInfo();
		if(candidate != null){
			List<CandidateContact> contacts = candidate.getCandidateContacts();
			if(contacts != null && !contacts.isEmpty()){
				if(contacts.size() >= 2){
					//	additional validation to only show 2 contacts
					List<CandidateContact> contactList = new ArrayList<>(2);
					CandidateContact cc = contacts.get(0);
					if(cc.getRelationship() != null && cc.getRelationship().getId() != null && cc.getRelationship().getId() > 0)
						cc.setRelationshipId(cc.getRelationship().getId());
					contactList.add(cc);
					cc = contacts.get(1);
					if(cc.getRelationship() != null && cc.getRelationship().getId() != null && cc.getRelationship().getId() > 0)
						cc.setRelationshipId(cc.getRelationship().getId());
					contactList.add(cc);
					contacts = contactList;
				}else if(contacts.size() == 1){
					List<CandidateContact> contactList = new ArrayList<>(2);
					CandidateContact cc = contacts.get(0);
					if(cc.getRelationship() != null && cc.getRelationship().getId() != null && cc.getRelationship().getId() > 0)
						cc.setRelationshipId(cc.getRelationship().getId());
					contactList.add(cc);
					contactList.add(new CandidateContact());
					contacts = contactList;
				}
			}else{
				contacts = new ArrayList<>(2);
				contacts.add(new CandidateContact());
				contacts.add(new CandidateContact());
			}
			contactInfo.setContacts(contacts);
			List<CandidateAddress> addresses = candidate.getCandidateAddresses();
			CandidateAddress address = null;
			if(addresses != null && !addresses.isEmpty()){
				CandidateAddress ca = addresses.get(0);
				if(ca.getState() != null && ca.getState().getId() != null && ca.getState().getId() > 0)
					ca.setStateId(ca.getState().getId());
				if(ca.getCountry() != null && ca.getCountry().getId() != null && ca.getCountry().getId() > 0)
					ca.setCountryId(ca.getCountry().getId());
				ca.setAddressStr(uiUtils.getAddressTxt(addresses));
				address = ca;
			}
			contactInfo.setAddress(address);
			contactInfo.setNativePlace(candidate.getCandidatePersonalDetail().getNativePlace());
		}
		contactInfo.setRelationshipOptions(masterRelationshipDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		contactInfo.setStateOptions(masterStateDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		contactInfo.setCountryOptions(masterCountryDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		return contactInfo;
	}

	public List<CandidateContact> getContactsInfoFromUI(ContactInfo contactInfo){
		List<CandidateContact> contacts = new ArrayList<>(2);
		if(contactInfo != null){
			for (CandidateContact cc : contactInfo.getContacts()) {
				if(cc.getId() != null){
					CandidateContact existingCC = candidateContactDao.findOne(cc.getId());
					existingCC.setFullName(cc.getFullName());
					existingCC.setRelationshipId(cc.getRelationshipId());
					existingCC.setRelationship(masterRelationshipDao.findOne(cc.getRelationshipId()));
					existingCC.setMobile(cc.getMobile());
					existingCC.setUpdateOn(DateUtils.now());
					contacts.add(existingCC);
				}else{
					if(StringUtils.isNotBlank(cc.getFullName())){
						cc.setCreateOn(DateUtils.now());
						cc.setRelationship(masterRelationshipDao.findOne(cc.getRelationshipId()));
						cc.setStatus(StatusEnum.Active.toString());
						cc.setUpdateOn(DateUtils.now());
						contacts.add(cc);
					}
				}
			}
		}
		return contacts;
	}
	
	public List<CandidateAddress> getAddressesInfoFromUI(ContactInfo contactInfo){
		List<CandidateAddress> addresses = null;
		if(contactInfo != null && contactInfo.getAddress() != null){
			addresses = new ArrayList<>(1);
			CandidateAddress address = contactInfo.getAddress();
			if(address != null){
				if(address.getId() != null && address.getId() > 0){
					CandidateAddress existingAddress = candidateAddressDao.findOne(address.getId());
					existingAddress.setAddressLine(address.getAddressLine());
					existingAddress.setCityOrTown(address.getCityOrTown());
					existingAddress.setTahsil(address.getTahsil());
					existingAddress.setDistrict(address.getDistrict());
					if(address.getStateId() != null && address.getStateId() > 0)
						existingAddress.setState(masterStateDao.findOne(address.getStateId()));
					else
						existingAddress.setState(null);
					if(address.getCountryId() != null && address.getCountryId() > 0)
						existingAddress.setCountry(masterCountryDao.findOne(address.getCountryId()));
					else
						existingAddress.setCountry(null);
					existingAddress.setOtherCountry(address.getOtherCountry());
					existingAddress.setOtherState(address.getOtherState());
					existingAddress.setUpdateOn(DateUtils.now());
					addresses.add(existingAddress);
				}else{
					if(StringUtils.isNotBlank(address.getCityOrTown())){
						if(address.getStateId() != null && address.getStateId() > 0)
							address.setState(masterStateDao.findOne(address.getStateId()));
						else
							address.setState(null);
						if(address.getCountryId() != null && address.getCountryId() > 0)
							address.setCountry(masterCountryDao.findOne(address.getCountryId()));
						else
							address.setCountry(null);
						address.setUpdateOn(DateUtils.now());
						addresses.add(address);
					}
				}
			}
		}
		return addresses;
	}
	
	public DashboardInfo getDashboardInfoForUI(Candidate candidate){
		DashboardInfo dashboard = new DashboardInfo();
		if(candidate != null){
			CandidatePersonalDetail personalDetail = candidate.getCandidatePersonalDetail();
			CandidateAstroDetail astroDetail = candidate.getCandidateAstroDetail();
			List<CandidateEducation> educations = candidate.getCandidateEducations();
			List<CandidateOccupation> occupations = candidate.getCandidateOccupations();
			List<CandidateAddress> addresses = candidate.getCandidateAddresses();
			List<CandidateContact> contacts = candidate.getCandidateContacts();
			dashboard.setGid(candidate.getGid());
			if(DateUtils.toLocalDate(DateUtils.now()).equals(getLastProfileUpdatedDate(candidate))){
				dashboard.setLastProfileUpdated("today");
			}else{
				dashboard.setLastProfileUpdated(DateUtils.getYearMonthDayBetweenDates(getLastProfileUpdatedDate(candidate), DateUtils.toLocalDate(DateUtils.now()))+" before");
			}
			if(personalDetail != null){
				// activities
				// profile overview
				dashboard.setFirstName(personalDetail.getFirstName());
				dashboard.setFullName(personalDetail.getFirstName()+" "+personalDetail.getMiddleName()+" "+personalDetail.getLastName());
				dashboard.setHeight(personalDetail.getHeight() != null? personalDetail.getHeight().toString():"");
				dashboard.setHeightFoot(GConverter.convertCmsToFoot(personalDetail.getHeight()));
				dashboard.setWeight(personalDetail.getWeight() != null? personalDetail.getWeight().toString():"");
				if(astroDetail != null){
					dashboard.setDateOfBirth(DateUtils.toDDMMMYYYY(DateUtils.toLocalDate(astroDetail.getBirthDate())));
				}
				dashboard.setOccupation(uiUtils.getOccupationTxt(occupations));
				dashboard.setEducation(uiUtils.getEducationTxt(educations));
				dashboard.setCurrentLocation(uiUtils.getAddressCityOrTown(addresses));
				dashboard.setContact(uiUtils.getContactTxt(contacts));
			}
			dashboard.setMessages(getMessagesForUI(candidate));
			dashboard.setShortlistedProfiles(getShortlistedProfilesForUI(candidate));
		}
		return dashboard;
	}
	
	private LocalDate getLastProfileUpdatedDate(Candidate c){
		LocalDate resp = DateUtils.toLocalDate(DateUtils.now());
		if(c != null && c.getUpdateOn() != null){
			resp = DateUtils.toLocalDate(c.getUpdateOn());
		}
		if(c.getCandidatePersonalDetail() != null && c.getCandidatePersonalDetail().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getCandidatePersonalDetail().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getCandidatePersonalDetail().getUpdateOn());
			}
		}
		if(c.getCandidateFamily() != null && c.getCandidateFamily().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getCandidateFamily().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getCandidateFamily().getUpdateOn());
			}
		}
		if(c.getCandidateExpectation() != null && c.getCandidateExpectation().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getCandidateExpectation().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getCandidateExpectation().getUpdateOn());
			}
		}
		if(c.getCandidateAstroDetail() != null && c.getCandidateAstroDetail().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getCandidateAstroDetail().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getCandidateAstroDetail().getUpdateOn());
			}
		}
		List<CandidateAddress> addresses = c.getCandidateAddresses();
		if(CollectionUtils.isNotEmpty(addresses)){
			for (CandidateAddress ca : addresses) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<CandidateContact> contacts = c.getCandidateContacts();
		if(CollectionUtils.isNotEmpty(contacts)){
			for (CandidateContact ca : contacts) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<CandidateEducation> educations = c.getCandidateEducations();
		if(CollectionUtils.isNotEmpty(educations)){
			for (CandidateEducation ca : educations) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<CandidateOccupation> occupations = c.getCandidateOccupations();
		if(CollectionUtils.isNotEmpty(occupations)){
			for (CandidateOccupation ca : occupations) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		return resp;
	}
	
	public EduOccuInfo getEducationInfoSectionForUI(Candidate candidate){
		EduOccuInfo eduOccuInfo = new EduOccuInfo();
		if(candidate != null){
			List<CandidateEducation> educations = candidate.getCandidateEducations();
			if(educations != null && !educations.isEmpty()){
				if(educations.size() >= 2){
					//	additional validation to only show 2 educations
					List<CandidateEducation> educationList = new ArrayList<>(2);
					
					CandidateEducation cc = educations.get(0);
					if(cc.getDegree() != null && cc.getDegree().getId() != null && cc.getDegree().getId() > 0){
						cc.setDegreeId(cc.getDegree().getId());
						if(cc.getDegree().getDegree().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDegree())){
							cc.setDegreeStr(cc.getOtherDegree());
						}else{
							cc.setDegreeStr(cc.getDegree().getDegree());
						}
					}
					if(cc.getSpecialization() != null && cc.getSpecialization().getId() != null && cc.getSpecialization().getId() > 0){
						cc.setSpecializationId(cc.getSpecialization().getId());
						if(StringUtils.isNotBlank(cc.getDegreeStr())){
							if(cc.getSpecialization().getSpecialization().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNotBlank(cc.getOtherSpecialization())){
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getOtherSpecialization()+")"); 
							}else {
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getSpecialization().getSpecialization()+")");
							}
						}
					}
					educationList.add(cc);
					
					cc = educations.get(1);
					if(cc.getDegree() != null && cc.getDegree().getId() != null && cc.getDegree().getId() > 0){
						cc.setDegreeId(cc.getDegree().getId());
						if(cc.getDegree().getDegree().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDegree())){
							cc.setDegreeStr(cc.getOtherDegree());
						}else{
							cc.setDegreeStr(cc.getDegree().getDegree());
						}
					}
					if(cc.getSpecialization() != null && cc.getSpecialization().getId() != null && cc.getSpecialization().getId() > 0){
						cc.setSpecializationId(cc.getSpecialization().getId());
						if(StringUtils.isNotBlank(cc.getDegreeStr())){
							if(cc.getSpecialization().getSpecialization().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNotBlank(cc.getOtherSpecialization())){
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getOtherSpecialization()+")"); 
							}else {
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getSpecialization().getSpecialization()+")");
							}
						}
					}

					educationList.add(cc);
					educations = educationList;
				}else if(educations.size() == 1){
					List<CandidateEducation> educationList = new ArrayList<>(2);
					CandidateEducation cc = educations.get(0);
					if(cc.getDegree() != null && cc.getDegree().getId() != null && cc.getDegree().getId() > 0){
						cc.setDegreeId(cc.getDegree().getId());
						if(cc.getDegree().getDegree().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDegree())){
							cc.setDegreeStr(cc.getOtherDegree());
						}else{
							cc.setDegreeStr(cc.getDegree().getDegree());
						}
					}
					if(cc.getSpecialization() != null && cc.getSpecialization().getId() != null && cc.getSpecialization().getId() > 0){
						cc.setSpecializationId(cc.getSpecialization().getId());
						if(StringUtils.isNotBlank(cc.getDegreeStr())){
							if(cc.getSpecialization().getSpecialization().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNotBlank(cc.getOtherSpecialization())){
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getOtherSpecialization()+")"); 
							}else {
								cc.setDegreeStr(cc.getDegreeStr()+" ("+cc.getSpecialization().getSpecialization()+")");
							}
						}
					}
					
					educationList.add(cc);
					educationList.add(new CandidateEducation());
					educations = educationList;
				}
			}else{
				educations = new ArrayList<>(2);
				educations.add(new CandidateEducation());
				educations.add(new CandidateEducation());
			}
			eduOccuInfo.setEducations(educations);
			List<CandidateOccupation> occupations = candidate.getCandidateOccupations();
			if(occupations != null && !occupations.isEmpty()){
				if(occupations.size() >= 2){
					//	additional validation to only show 2 occupations
					List<CandidateOccupation> occupationList = new ArrayList<>(2);
					CandidateOccupation cc = occupations.get(0);
					if(cc.getOccupation() != null && cc.getOccupation().getId() != null && cc.getOccupation().getId() > 0){
						cc.setOccupationId(cc.getOccupation().getId());
						if(cc.getOccupation().getOccupation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherOccupation())){
							cc.setOccupationStr(cc.getOtherOccupation());
						}else{
							cc.setOccupationStr(cc.getOccupation().getOccupation());
						}
					}
					if(cc.getDesignation() != null && cc.getDesignation().getId() != null && cc.getDesignation().getId() > 0){
						cc.setDesignationId(cc.getDesignation().getId());
						if(cc.getDesignation().getDesignation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDesignation())){
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getOtherDesignation()+")");
						}else{
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getDesignation().getDesignation()+")");
						}
					}
					occupationList.add(cc);
					
					cc = occupations.get(1);
					if(cc.getOccupation() != null && cc.getOccupation().getId() != null && cc.getOccupation().getId() > 0){
						cc.setOccupationId(cc.getOccupation().getId());
						if(cc.getOccupation().getOccupation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherOccupation())){
							cc.setOccupationStr(cc.getOtherOccupation());
						}else{
							cc.setOccupationStr(cc.getOccupation().getOccupation());
						}
					}
					if(cc.getDesignation() != null && cc.getDesignation().getId() != null && cc.getDesignation().getId() > 0){
						cc.setDesignationId(cc.getDesignation().getId());
						if(cc.getDesignation().getDesignation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDesignation())){
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getOtherDesignation()+")");
						}else{
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getDesignation().getDesignation()+")");
						}
					}
					occupationList.add(cc);
					occupations = occupationList;
				}else if(occupations.size() == 1){
					List<CandidateOccupation> occupationList = new ArrayList<>(2);
					CandidateOccupation cc = occupations.get(0);
					if(cc.getOccupation() != null && cc.getOccupation().getId() != null && cc.getOccupation().getId() > 0){
						cc.setOccupationId(cc.getOccupation().getId());
						if(cc.getOccupation().getOccupation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherOccupation())){
							cc.setOccupationStr(cc.getOtherOccupation());
						}else{
							cc.setOccupationStr(cc.getOccupation().getOccupation());
						}
					}
					if(cc.getDesignation() != null && cc.getDesignation().getId() != null && cc.getDesignation().getId() > 0){
						cc.setDesignationId(cc.getDesignation().getId());
						if(cc.getDesignation().getDesignation().equalsIgnoreCase(GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDesignation())){
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getOtherDesignation()+")");
						}else{
							cc.setOccupationStr(cc.getOccupationStr()+" ("+cc.getDesignation().getDesignation()+")");
						}
					}
					occupationList.add(cc);
					occupationList.add(new CandidateOccupation());
					occupations = occupationList;
				}
			}else{
				occupations = new ArrayList<>(2);
				occupations.add(new CandidateOccupation());
				occupations.add(new CandidateOccupation());
			}
			eduOccuInfo.setOccupations(occupations);
			
			if(CollectionUtils.isNotEmpty(occupations) && occupations.size() > 0){
				if(candidate.getYearlyIncome() != null){
					eduOccuInfo.setYearlyIncomeId(candidate.getYearlyIncome().getId());
					eduOccuInfo.setYearlyIncomeStr(candidate.getYearlyIncome().getYearlyIncome());
				}
			}
		}
		eduOccuInfo.setOccupationOptions(masterOccupationDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		eduOccuInfo.setDegreeOptions(masterDegreeDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		eduOccuInfo.setDesignationOptions(masterDesignationDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		eduOccuInfo.setSpecializationOptions(masterDegreeSpecializationDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		eduOccuInfo.setYearlyIncomeOptions(masterYearlyIncomeDao.findByStatusOrderBySequenceNumber(StatusEnum.Active.toString()));
		return eduOccuInfo;
	}
	
	public List<CandidateEducation> getEducationsInfoFromUI(EduOccuInfo eduOccuInfo){
		List<CandidateEducation> educations = new ArrayList<>(2);
		if(eduOccuInfo != null){
			for (CandidateEducation cc : eduOccuInfo.getEducations()) {
				if(cc.getId() != null){
					CandidateEducation existingCE = candidateEducationDao.findOne(cc.getId());
					if(cc.getDegreeId() != null && cc.getDegreeId() > 0)
						existingCE.setDegree(masterDegreeDao.findOne(cc.getDegreeId()));
					else
						existingCE.setDegree(null);
					//existingCE.setDegreeId(cc.getDegreeId());
					if(cc.getSpecializationId() != null && cc.getSpecializationId() > 0)
						existingCE.setSpecialization(masterDegreeSpecializationDao.findOne(cc.getSpecializationId()));
					else
						existingCE.setSpecialization(null);
					//existingCE.setSpecializationId(cc.getSpecializationId());
					//existingCE.setOtherDegree(cc.getOtherDegree());
					//existingCE.setOtherSpecialization(cc.getOtherSpecialization());
					educations.add(existingCE);
				}else{
					if(cc.getDegreeId() != null && cc.getDegreeId() > 0){
						cc.setDegree(masterDegreeDao.findOne(cc.getDegreeId()));
						if(cc.getSpecializationId() != null && cc.getSpecializationId() > 0)
							cc.setSpecialization(masterDegreeSpecializationDao.findOne(cc.getSpecializationId()));
						else
							cc.setSpecialization(null);
						cc.setCreateOn(DateUtils.now());
						cc.setStatus(StatusEnum.Active.toString());
						cc.setUpdateOn(DateUtils.now());
						educations.add(cc);
					}
				}
			}
		}
		return educations;
	}
	
	public List<CandidateOccupation> getOccupationsInfoFromUI(EduOccuInfo eduOccuInfo){
		List<CandidateOccupation> occupations = new ArrayList<>(2);
		if(eduOccuInfo != null){
			for (CandidateOccupation cc : eduOccuInfo.getOccupations()) {
				if(cc.getId() != null){
					CandidateOccupation existingCO = candidateOccupationDao.findOne(cc.getId());
					if(cc.getOccupationId() != null && cc.getOccupationId() > 0)
						existingCO.setOccupation(masterOccupationDao.findOne(cc.getOccupationId()));
					else
						existingCO.setOccupation(null);
					//existingCO.setOccupationId(cc.getOccupationId());
					if(cc.getDesignationId() != null && cc.getDesignationId() > 0)
						existingCO.setDesignation(masterDesignationDao.findOne(cc.getDesignationId()));
					else
						existingCO.setDesignation(null);
					//existingCO.setDesignationId(cc.getDesignationId());
					//existingCO.setYearlyIncomeId(cc.getYearlyIncomeId());
					//existingCO.setOtherOccupation(cc.getOtherOccupation());
					//existingCO.setOtherDesignation(cc.getOtherDesignation());
					//existingCO.setDescription(cc.getDescription());
					occupations.add(existingCO);
				}else{
					if(cc.getOccupationId() != null && cc.getOccupationId() > 0){
						cc.setOccupation(masterOccupationDao.findOne(cc.getOccupationId()));
						if(cc.getDesignationId() != null && cc.getDesignationId() > 0)
							cc.setDesignation(masterDesignationDao.findOne(cc.getDesignationId()));
						else
							cc.setDesignation(null);
						if(cc.getYearlyIncomeId() != null && cc.getYearlyIncomeId() > 0)
							cc.setYearlyIncome(masterYearlyIncomeDao.findOne(cc.getYearlyIncomeId()));
						else
							cc.setYearlyIncome(null);
						cc.setCreateOn(DateUtils.now());
						cc.setStatus(StatusEnum.Active.toString());
						cc.setUpdateOn(DateUtils.now());
						occupations.add(cc);
					}
				}
			}
		}
		return occupations;
	}
	
	public List<ShortlistedProfile> getShortlistedProfilesForUI(Candidate candidate){
		List<ShortlistedProfile> shortlistedProfiles = null;
		List<CandidateShortlistedProfile> candidateProfiles = candidate.getShortlistedCandidates();
		if(CollectionUtils.isNotEmpty(candidateProfiles)){
			shortlistedProfiles = new ArrayList<>();
			ShortlistedProfile p = null;
			for (CandidateShortlistedProfile profile : candidateProfiles) {
				if(profile != null){
					p = new ShortlistedProfile();
					p.setFullName(candidate.getCandidatePersonalDetail().getFirstName() + " " + candidate.getCandidatePersonalDetail().getLastName());
					p.setDateOfBirth(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(candidate.getCandidateAstroDetail().getBirthDate())));
					p.setAddress(uiUtils.getAddressCityOrTown(candidate.getCandidateAddresses()));
					p.setOccupation(uiUtils.getPrimaryOccupation(candidate.getCandidateOccupations()));
					shortlistedProfiles.add(p);
				}
			}
		}
		return shortlistedProfiles;
	}
	
	public List<Message> getMessagesForUI(Candidate fromCandidate){
		List<Message> messages = null;
		List<GMessage> gmessages = candidateService.getCandidateMessages(fromCandidate);
		if(CollectionUtils.isNotEmpty(gmessages)){
			messages = new ArrayList<>();
			Message m = null;
			for (GMessage message : gmessages) {
				if(message != null){
					m = new Message();
					m.setMessageId(message.getId());
					m.setFrom(fromCandidate.getCandidatePersonalDetail().getFirstName() + " " + fromCandidate.getCandidatePersonalDetail().getLastName());
					m.setSubject(message.getSubject());
					m.setBody(message.getBody());
					m.setDateReceived(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(message.getCreateOn())));
					messages.add(m);
				}
			}
		}
		return messages;
	}
	
	public PhotoInfo getPhotoInfoSectionForUI(Candidate candidate){
		PhotoInfo pi = new PhotoInfo();
		if(candidate != null){
			List<GPhoto> gPhotos = candidateService.getCandidatePhotos(candidate);
			if(CollectionUtils.isNotEmpty(gPhotos)){
				List<Photo> photos = new ArrayList<>();
				for (GPhoto gPhoto : gPhotos) {
					if(gPhoto != null){
						Photo p = new Photo();
						p.setPhotoId(gPhoto.getId());
						p.setIsActive(true);
						p.setIsPrimary(gPhoto.getIsSagaiPrimary());
						p.setPath(gPhoto.getPath());
						photos.add(p);
					}
				}
				pi.setPhotos(photos);
				pi.setIsUploadAllowed(photos.size() < 2? true:false);
			}
		}
		return pi;
	}
	
	public List<GPhoto> getPhotosInfoFromUI(PhotoInfo photoInfo, User user){
		List<GPhoto> gPhotos = new ArrayList<>();
		if(photoInfo != null){
			for (Photo p : photoInfo.getPhotos()) {
				GPhoto gp = null;
				if(p.getPhotoId() != null){
					gp = gPhotoDao.findOne(p.getPhotoId());
					gp.setIsSagaiPrimary(p.getIsPrimary());
					if(!p.getIsActive()){
						gp.setStatus(StatusEnum.Inactive.toString());
					}
					gp.setUpdateOn(DateUtils.now());
					gPhotos.add(gp);
				}else{
					gp = new GPhoto();
					gp.setUser(user);
					gp.setCategory(PhotoCategoryEnum.Sagai.toString());
					gp.setIsSagaiPrimary(p.getIsPrimary());
					gp.setStatus(StatusEnum.Active.toString());
					gp.setPath(p.getPath());
					gp.setUpdateOn(DateUtils.now());
					gPhotos.add(gp);
				}
			}
		}
		return gPhotos;
	}
}
