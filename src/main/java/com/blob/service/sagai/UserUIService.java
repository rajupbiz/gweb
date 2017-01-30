package com.blob.service.sagai;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.master.MasterBloodGroupDao;
import com.blob.dao.master.MasterDayOfWeekDao;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.dao.master.MasterDegreeSpecializationDao;
import com.blob.dao.master.MasterDesignationDao;
import com.blob.dao.master.MasterMaritalStatusDao;
import com.blob.dao.master.MasterOccupationDao;
import com.blob.dao.master.MasterRelationshipDao;
import com.blob.dao.user.UserAddressDao;
import com.blob.dao.user.UserContactDao;
import com.blob.dao.user.UserEducationDao;
import com.blob.dao.user.UserOccupationDao;
import com.blob.dao.user.UserPhotoDao;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.model.sagai.SagaiShortlistedProfile;
import com.blob.model.ui.ContactInfo;
import com.blob.model.ui.DashboardInfo;
import com.blob.model.ui.EduOccuInfo;
import com.blob.model.ui.FamilyInfo;
import com.blob.model.ui.PersonalInfo;
import com.blob.model.ui.Photo;
import com.blob.model.ui.PhotoInfo;
import com.blob.model.ui.ShortlistedProfile;
import com.blob.model.user.User;
import com.blob.model.user.UserAddress;
import com.blob.model.user.UserAstro;
import com.blob.model.user.UserContact;
import com.blob.model.user.UserEducation;
import com.blob.model.user.UserFamily;
import com.blob.model.user.UserOccupation;
import com.blob.model.user.UserPersonal;
import com.blob.model.user.UserPhoto;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;
import com.blob.util.GConverter;
import com.blob.util.UiUtils;

@Service
public class UserUIService {
	
	@Resource
	private MasterMaritalStatusDao masterMaritalStatusDao;
	
	@Resource
	private MasterBloodGroupDao masterBloodGroupDao;
	
	@Resource
	private MasterDayOfWeekDao masterDayOfWeekDao;
	
	@Resource
	private MasterRelationshipDao masterRelationshipDao;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@Resource
	private MasterDegreeSpecializationDao masterDegreeSpecializationDao;
	
	@Resource
	private MasterDesignationDao masterDesignationDao;
	
	@Resource
	private MasterOccupationDao masterOccupationDao;
	
	/*@Resource
	private MasterStateDao masterStateDao;
	
	@Resource
	private MasterCountryDao masterCountryDao;
	
	@Resource
	private MasterYearlyIncomeDao masterYearlyIncomeDao;*/
	
	@Resource
	private UserContactDao userContactDao;
	
	@Resource
	private UserAddressDao userAddressDao;
	
	@Resource
	private UserEducationDao userEducationDao;
	
	@Resource
	private UserOccupationDao userOccupationDao;
	
	@Resource
	private UiUtils uiUtils;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserPhotoDao userPhotoDao;
	
	public PersonalInfo getPersonalInfoSectionForUI(User user, boolean isViewOnly){
		PersonalInfo pi = new PersonalInfo();
		if(user != null){
			UserPersonal pd = user.getUserPersonal();
			UserAstro ad = user.getUserAstro();
			if(pd != null){
				pi.setFirstName(pd.getFirstName());
				pi.setMiddleName(pd.getMiddleName());
				pi.setLastName(pd.getLastName());
				pi.setGender(pd.getGender());
				if(pd.getMaritalStatus() != null){
					pi.setMaritalStatus(pd.getMaritalStatus());
				}
				pi.setHeightCms(pd.getHeight());
				if(pd.getHeight() != null && pd.getHeight() > 0)
				pi.setHeightFoot("("+GConverter.convertCmsToFoot(pd.getHeight())+")");
				pi.setWeight(pd.getWeight());
				if(pd.getBloodGroup() != null)
				pi.setBloodGroup(pd.getBloodGroup());
				pi.setHobby(pd.getHobby());
				pi.setAboutMe(pd.getAboutMe());
			}
			if(ad != null){
				pi.setBirthName(ad.getBirthName());
				pi.setBirthPlace(ad.getBirthPlace());
				pi.setBirthDate(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(ad.getBirthDate())));
				pi.setBirthTime(DateUtils.toHHMM_AMPM(DateUtils.toLocalTime(ad.getBirthTime())));
				if(ad.getBirthDayOfWeek() != null){
					pi.setBirthDay(ad.getBirthDayOfWeek());
				}
				if(ad.isMangal() != null)
				pi.setIsMangal(ad.isMangal() ? GConstants.Mangal_Yes : GConstants.Mangal_No);
			}
		}
		if(!isViewOnly){
			pi.setMaritalStatusOptions(masterMaritalStatusDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			pi.setBirthDayOptions(masterDayOfWeekDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			pi.setBloodGroupOptions(masterBloodGroupDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
		}
		return pi;
	}
	
	public UserPersonal getUserPersonalInfoFromUI(PersonalInfo pi){
		UserPersonal pd = null;
		if(pi != null){
			pd = new UserPersonal();
			pd.setFirstName(pi.getFirstName());
			pd.setLastName(pi.getLastName());
			pd.setMiddleName(pi.getMiddleName());
			pd.setGender(pi.getGender());
			if(StringUtils.isNoneBlank(pi.getMaritalStatus()))
				pd.setMaritalStatus(pi.getMaritalStatus());
			pd.setHeight(pi.getHeightCms());
			pd.setWeight(pi.getWeight());
			if(StringUtils.isNoneBlank(pi.getBloodGroup()))
				pd.setBloodGroup(pi.getBloodGroup());
			pd.setHobby(pi.getHobby());
			pd.setAboutMe(pi.getAboutMe());
		}
		return pd;
	}
	
	public UserAstro getUserAstroFromUI(PersonalInfo pi){
		UserAstro ad = null;
		if(pi != null){
			ad = new UserAstro();
			ad.setBirthName(pi.getBirthName());
			ad.setBirthPlace(pi.getBirthPlace());
			ad.setBirthDate(DateUtils.toDate(DateUtils.toLocalDate(pi.getBirthDate())));
			ad.setBirthTime(DateUtils.toDate(DateUtils.toLocalTime(pi.getBirthTime())));
			if(StringUtils.isNoneBlank(pi.getBirthDay()))
				ad.setBirthDayOfWeek(pi.getBirthDay());
			if(StringUtils.isNotBlank(pi.getIsMangal()))
				ad.setMangal(pi.getIsMangal().equalsIgnoreCase(GConstants.Mangal_No)? false : true);
		}
		return ad;
	}
	
	public FamilyInfo getFamilyInfoSectionForUI(User user){
		FamilyInfo fi = new FamilyInfo();
		if(user != null){
			UserFamily f = user.getUserFamily();
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
	
	public UserFamily getFamilyInfoFromUI(FamilyInfo fi){
		UserFamily f = null;
		if(fi != null){
			f = new UserFamily();
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
	
	public ContactInfo getContactInfoSectionForUI(User user, boolean isViewOnly){
		ContactInfo contactInfo = new ContactInfo();
		if(user != null){
			List<UserContact> contacts = user.getUserContacts();
			if(contacts != null && !contacts.isEmpty()){
				if(contacts.size() >= 2){
					//	additional validation to only show 2 contacts
					List<UserContact> contactList = new ArrayList<>(2);
					UserContact cc = contacts.get(0);
					/*if(StringUtils.isNotBlank(cc.getRelationship()))
						cc.setRelationship(cc.getRelationship());*/
					contactList.add(cc);
					cc = contacts.get(1);
					/*if(StringUtils.isNotBlank(cc.getRelationship()))
						cc.setRelationship(cc.getRelationship().getId());*/
					contactList.add(cc);
					contacts = contactList;				
				}else if(contacts.size() == 1){
					List<UserContact> contactList = new ArrayList<>(2);
					UserContact cc = contacts.get(0);
					/*if(cc.getRelationship() != null && cc.getRelationship().getId() != null && cc.getRelationship().getId() > 0)
						cc.setRelationshipId(cc.getRelationship().getId());*/
					contactList.add(cc);
					contactList.add(new UserContact());
					contacts = contactList;
				}
			}else if(!isViewOnly){
				contacts = new ArrayList<>(2);
				contacts.add(new UserContact());
				contacts.add(new UserContact());
			}
			contactInfo.setContacts(contacts);
			
			UserAddress existingNativeAddress = userAddressDao.findFirstByUserAndAddressTypeAndStatusOrderByUpdateOnDesc(user, GConstants.AddressType_Native, GConstants.Status_Active);
			UserAddress existingCurrentAddress = userAddressDao.findFirstByUserAndAddressTypeAndStatusOrderByUpdateOnDesc(user, GConstants.AddressType_Current, GConstants.Status_Active);
			if(existingNativeAddress != null){
				contactInfo.setNativePlace(existingNativeAddress.getCityOrTown());
			}
			if(existingCurrentAddress != null){
				contactInfo.setCurrentLocation(existingCurrentAddress.getCityOrTown());
			}
		}
		if(!isViewOnly){
			contactInfo.setRelationshipOptions(masterRelationshipDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			/*contactInfo.setStateOptions(masterStateDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			contactInfo.setCountryOptions(masterCountryDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));*/
		}
		return contactInfo;
	}

	public List<UserContact> getContactsInfoFromUI(ContactInfo contactInfo){
		List<UserContact> contacts = new ArrayList<>(2);
		if(contactInfo != null){
			for (UserContact cc : contactInfo.getContacts()) {
				if(cc.getId() != null){
					UserContact existingCC = userContactDao.findOne(cc.getId());
					existingCC.setFullName(cc.getFullName());
					existingCC.setRelationship(cc.getRelationship());
//					//existingCC.setRelationship(masterRelationshipDao.findOne(cc.getRelationshipId()));
					existingCC.setMobile(cc.getMobile());
					existingCC.setUpdateOn(DateUtils.now());
					contacts.add(existingCC);
				}else{
					if(StringUtils.isNotBlank(cc.getFullName())){
						cc.setCreateOn(DateUtils.now());
						cc.setRelationship(cc.getRelationship());
						cc.setStatus(GConstants.Status_Active);
						cc.setUpdateOn(DateUtils.now());
						contacts.add(cc);
					}
				}
			}
		}
		return contacts;
	}
	
	public List<UserAddress> getAddressesInfoFromUI(User c, ContactInfo contactInfo){
		
		List<UserAddress> addresses = new ArrayList<>(2);
		if(contactInfo != null){
			UserAddress existingNativeAddress = userAddressDao.findFirstByUserAndAddressTypeAndStatusOrderByUpdateOnDesc(c, GConstants.AddressType_Native, GConstants.Status_Active);
			if(existingNativeAddress != null){
				existingNativeAddress.setCityOrTown(contactInfo.getNativePlace());
				existingNativeAddress.setUpdateOn(DateUtils.now());
				addresses.add(existingNativeAddress);
			}else{
				if(StringUtils.isNoneBlank(contactInfo.getNativePlace())){
					UserAddress nativeAddress = new UserAddress();
					nativeAddress.setCityOrTown(contactInfo.getNativePlace());
					nativeAddress.setAddressType(GConstants.AddressType_Native);
					nativeAddress.setStatus(GConstants.Status_Active);
					nativeAddress.setUpdateOn(DateUtils.now());
					addresses.add(nativeAddress);
				}
			}
			UserAddress existingCurrentAddress = userAddressDao.findFirstByUserAndAddressTypeAndStatusOrderByUpdateOnDesc(c, GConstants.AddressType_Current, GConstants.Status_Active);
			if(existingCurrentAddress != null){
				existingCurrentAddress.setCityOrTown(contactInfo.getCurrentLocation());
				existingCurrentAddress.setUpdateOn(DateUtils.now());
				addresses.add(existingCurrentAddress);
			}else{
				if(StringUtils.isNoneBlank(contactInfo.getNativePlace())){
					UserAddress address = new UserAddress();
					address.setCityOrTown(contactInfo.getCurrentLocation());
					address.setAddressType(GConstants.AddressType_Current);
					address.setStatus(GConstants.Status_Active);
					address.setUpdateOn(DateUtils.now());
					addresses.add(address);
				}
			}
			if(CollectionUtils.isNotEmpty(addresses)){
				for (UserAddress UserAddress : addresses) {
					if(UserAddress != null){
						userAddressDao.save(UserAddress);
					}
				}
			}
		}
		return addresses;
	}
	
	public DashboardInfo getDashboardInfoForUI(User user, String contextPath){
		DashboardInfo dashboard = new DashboardInfo();
		if(user != null){
			UserPersonal personalDetail = user.getUserPersonal();
			UserAstro astroDetail = user.getUserAstro();
			List<UserEducation> educations = user.getUserEducations();
			List<UserOccupation> occupations = user.getUserOccupations();
			List<UserAddress> addresses = user.getUserAddresses();
			List<UserContact> contacts = user.getUserContacts();
			dashboard.setPhotoInfo(getPhotoInfoSectionForUI(user, contextPath));
			dashboard.setGid(user.getGid());
			if(DateUtils.toLocalDate(DateUtils.now()).equals(getLastProfileUpdatedDate(user))){
				dashboard.setLastProfileUpdated("today");
			}else{
				dashboard.setLastProfileUpdated(DateUtils.getYearMonthDayBetweenDates(getLastProfileUpdatedDate(user), DateUtils.toLocalDate(DateUtils.now()))+" before");
			}
			if(personalDetail != null){
				// activities
				// profile overview
				dashboard.setFirstName(personalDetail.getFirstName());
				dashboard.setFullName(personalDetail.getFirstName()+" "+(personalDetail.getMiddleName() != null ? personalDetail.getMiddleName() +" " : "")+personalDetail.getLastName());
				dashboard.setHeight(personalDetail.getHeight() != null? personalDetail.getHeight().toString():"");
				dashboard.setHeightFoot(GConverter.convertCmsToFoot(personalDetail.getHeight()));
				dashboard.setWeight(personalDetail.getWeight() != null? personalDetail.getWeight().toString():"");
				if(astroDetail != null){
					dashboard.setDateOfBirth(DateUtils.toDDMMMYYYY(DateUtils.toLocalDate(astroDetail.getBirthDate())));
				}
				dashboard.setOccupation(uiUtils.getOccupationTxt(occupations));
				dashboard.setEducation(uiUtils.getEducationTxt(educations));
				if(CollectionUtils.isNotEmpty(addresses)){
					for (UserAddress userAddr : addresses) {
						if(StringUtils.equalsIgnoreCase(userAddr.getAddressType(), GConstants.AddressType_Native)){
							dashboard.setNativePlace(userAddr.getCityOrTown());
						}else if(StringUtils.equalsIgnoreCase(userAddr.getAddressType(), GConstants.AddressType_Current)){
							dashboard.setCurrentPlace(userAddr.getCityOrTown());
						}
					}
				}
				dashboard.setContact(uiUtils.getContactTxt(contacts));
			}
			//dashboard.setMessages(getMessagesForUI(user));
			dashboard.setShortlistedProfiles(getShortlistedProfilesForUI(user));
		}
		return dashboard;
	}
	
	private LocalDate getLastProfileUpdatedDate(User c){
		LocalDate resp = DateUtils.toLocalDate(DateUtils.now());
		if(c != null && c.getUpdateOn() != null){
			resp = DateUtils.toLocalDate(c.getUpdateOn());
		}
		if(c.getUserPersonal() != null && c.getUserPersonal().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getUserPersonal().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getUserPersonal().getUpdateOn());
			}
		}
		if(c.getUserFamily() != null && c.getUserFamily().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getUserFamily().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getUserFamily().getUpdateOn());
			}
		}
		/*if(c.getUserExpectation() != null && c.getUserExpectation().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getUserExpectation().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getUserExpectation().getUpdateOn());
			}
		}*/
		if(c.getUserAstro() != null && c.getUserAstro().getUpdateOn() != null){
			if(resp.isBefore(DateUtils.toLocalDate(c.getUserAstro().getUpdateOn()))){
				resp = DateUtils.toLocalDate(c.getUserAstro().getUpdateOn());
			}
		}
		List<UserAddress> addresses = c.getUserAddresses();
		if(CollectionUtils.isNotEmpty(addresses)){
			for (UserAddress ca : addresses) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<UserContact> contacts = c.getUserContacts();
		if(CollectionUtils.isNotEmpty(contacts)){
			for (UserContact ca : contacts) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<UserEducation> educations = c.getUserEducations();
		if(CollectionUtils.isNotEmpty(educations)){
			for (UserEducation ca : educations) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		List<UserOccupation> occupations = c.getUserOccupations();
		if(CollectionUtils.isNotEmpty(occupations)){
			for (UserOccupation ca : occupations) {
				if(ca != null && ca.getUpdateOn() != null){
					if(resp.isBefore(DateUtils.toLocalDate(ca.getUpdateOn()))){
						resp = DateUtils.toLocalDate(ca.getUpdateOn());
					}
				}
			}
		}
		return resp;
	}
	
	public EduOccuInfo getEducationInfoSectionForUI(User user, boolean isViewOnly){
		EduOccuInfo eduOccuInfo = new EduOccuInfo();
		if(user != null){
			List<UserEducation> educations = user.getUserEducations();
			if(educations != null && !educations.isEmpty()){
				if(educations.size() >= 2){
					//	additional validation to only show 2 educations
					List<UserEducation> educationList = new ArrayList<>(2);
					
					UserEducation cc = educations.get(0);
					if(cc.getDegree() != null && cc.getDegreeId() != null && cc.getDegreeId() > 0){
						cc.setDegreeId(cc.getDegree().getId());
						if(StringUtils.equalsIgnoreCase(cc.getDegree().getDegree(), GConstants.SpecifyOther) && StringUtils.isNoneBlank(cc.getOtherDegree())){
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
					List<UserEducation> educationList = new ArrayList<>(2);
					UserEducation cc = educations.get(0);
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
					educationList.add(new UserEducation());
					educations = educationList;
				}
			}else if(!isViewOnly){
				educations = new ArrayList<>(2);
				educations.add(new UserEducation());
				educations.add(new UserEducation());
			}
			eduOccuInfo.setEducations(educations);
			List<UserOccupation> occupations = user.getUserOccupations();
			if(occupations != null && !occupations.isEmpty()){
				if(occupations.size() >= 2){
					//	additional validation to only show 2 occupations
					List<UserOccupation> occupationList = new ArrayList<>(2);
					UserOccupation cc = occupations.get(0);
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
					List<UserOccupation> occupationList = new ArrayList<>(2);
					UserOccupation cc = occupations.get(0);
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
					occupationList.add(new UserOccupation());
					occupations = occupationList;
				}
			}else if(!isViewOnly){
				occupations = new ArrayList<>(2);
				occupations.add(new UserOccupation());
				occupations.add(new UserOccupation());
			}
			eduOccuInfo.setOccupations(occupations);
			
			/*if(CollectionUtils.isNotEmpty(occupations) && occupations.size() > 0){
				if(user.getYearlyIncome() != null){
					eduOccuInfo.setYearlyIncomeId(user.getYearlyIncome().getId());
					eduOccuInfo.setYearlyIncomeStr(user.getYearlyIncome().getYearlyIncome());
				}
			}*/
		}
		if(!isViewOnly){
			eduOccuInfo.setOccupationOptions(masterOccupationDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			eduOccuInfo.setDegreeOptions(masterDegreeDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			eduOccuInfo.setDesignationOptions(masterDesignationDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			eduOccuInfo.setSpecializationOptions(masterDegreeSpecializationDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
			//eduOccuInfo.setYearlyIncomeOptions(masterYearlyIncomeDao.findByStatusOrderBySequenceNumber(GConstants.Status_Active));
		}
		return eduOccuInfo;
	}
	
	public List<UserEducation> getEducationsInfoFromUI(EduOccuInfo eduOccuInfo){
		List<UserEducation> educations = new ArrayList<>(2);
		if(eduOccuInfo != null){
			int eduSequenceNum = 1;
			for (UserEducation cc : eduOccuInfo.getEducations()) {
				if(cc.getId() != null){
					UserEducation existingCE = userEducationDao.findOne(cc.getId());
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
						cc.setStatus(GConstants.Status_Active);
						cc.setUpdateOn(DateUtils.now());
						cc.setSequenceNumber(eduSequenceNum);
						educations.add(cc);
						eduSequenceNum++;
					}
				}
			}
		}
		return educations;
	}
	
	public List<UserOccupation> getOccupationsInfoFromUI(EduOccuInfo eduOccuInfo){
		List<UserOccupation> occupations = new ArrayList<>(2);
		if(eduOccuInfo != null){
			int occSequenceNum = 1;
			for (UserOccupation cc : eduOccuInfo.getOccupations()) {
				if(cc.getId() != null){
					UserOccupation existingCO = userOccupationDao.findOne(cc.getId());
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
						/*if(cc.getYearlyIncomeId() != null && cc.getYearlyIncomeId() > 0)
							cc.setYearlyIncome(masterYearlyIncomeDao.findOne(cc.getYearlyIncomeId()));
						else
							cc.setYearlyIncome(null);*/
						cc.setSequenceNumber(occSequenceNum);
						cc.setCreateOn(DateUtils.now());
						cc.setStatus(GConstants.Status_Active);
						cc.setUpdateOn(DateUtils.now());
						occupations.add(cc);
						occSequenceNum++;
					}
				}
			}
		}
		return occupations;
	}
	
	public List<ShortlistedProfile> getShortlistedProfilesForUI(User user){
		List<ShortlistedProfile> shortlistedProfiles = null;
		List<SagaiShortlistedProfile> candidateProfiles = user.getSagaiShortlistedProfiles();
		if(CollectionUtils.isNotEmpty(candidateProfiles)){
			shortlistedProfiles = new ArrayList<>();
			ShortlistedProfile p = null;
			for (SagaiShortlistedProfile profile : candidateProfiles) {
				if(profile != null){
					p = new ShortlistedProfile();
					p.setFullName(user.getUserPersonal().getFirstName() + " " + user.getUserPersonal().getLastName());
					p.setDateOfBirth(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(user.getUserAstro().getBirthDate())));
					
					if(CollectionUtils.isNotEmpty(user.getUserAddresses())){
						String nativePlace = null, currentPlace = null;
						for (UserAddress userAddr : user.getUserAddresses()) {
							if(StringUtils.equalsIgnoreCase(userAddr.getAddressType(), GConstants.AddressType_Native)){
								nativePlace = userAddr.getCityOrTown();
							}else if(StringUtils.equalsIgnoreCase(userAddr.getAddressType(), GConstants.AddressType_Current)){
								currentPlace = userAddr.getCityOrTown();
							}
						}
						StringBuffer address = new StringBuffer();
						if(StringUtils.isNotBlank(nativePlace)){
							address.append(nativePlace);
							if(StringUtils.isNotBlank(currentPlace)){
								address.append(" ("+currentPlace+")");
							}
						}else if(StringUtils.isNotBlank(currentPlace)){
							address.append(currentPlace);
						}
						p.setAddress(address.toString());
					}
					p.setOccupation(uiUtils.getPrimaryOccupation(user.getUserOccupations()));
					shortlistedProfiles.add(p);
				}
			}
		}
		return shortlistedProfiles;
	}
	
	/*public List<Message> getMessagesForUI(User fromUser){
		List<Message> messages = null;
		List<GMessage> gmessages = candidateService.getUserMessages(fromUser);
		if(CollectionUtils.isNotEmpty(gmessages)){
			messages = new ArrayList<>();
			Message m = null;
			for (GMessage message : gmessages) {
				if(message != null){
					m = new Message();
					m.setMessageId(message.getId());
					m.setFrom(fromuser.getUserPersonal().getFirstName() + " " + fromuser.getUserPersonal().getLastName());
					m.setSubject(message.getSubject());
					m.setBody(message.getBody());
					m.setDateReceived(DateUtils.toDDMMYYYY(DateUtils.toLocalDate(message.getCreateOn())));
					messages.add(m);
				}
			}
		}
		return messages;
	}*/
	
	public PhotoInfo getPhotoInfoSectionForUI(User user, String contextPath){
		PhotoInfo pi = new PhotoInfo();
		if(user != null){
			List<UserPhoto> gPhotos = userService.getUserPhotos(user);
			if(CollectionUtils.isNotEmpty(gPhotos)){
				List<Photo> photos = new ArrayList<>();
				for (UserPhoto gPhoto : gPhotos) {
					if(gPhoto != null){
						Photo p = new Photo();
						p.setPhotoId(gPhoto.getId());
						p.setIsActive(true);
						p.setIsPrimary(gPhoto.getIsSagaiPrimary());
						/*String fileName = "";
						if (gPhoto.getFileName().indexOf(".") > 0)
							fileName = gPhoto.getFileName().substring(0, gPhoto.getFileName().lastIndexOf("."));*/
						p.setFileName(gPhoto.getFileName());
						p.setTitle(gPhoto.getTitle());
						photos.add(p);
						
						if(p.getIsPrimary() != null && p.getIsPrimary()){
							pi.setPrimaryPhoto(p);
							// set primary pic path
							String filepath = p.getFileName();
							if(StringUtils.isBlank(filepath)){
								UserPersonal pd = user.getUserPersonal();
								if(StringUtils.equalsIgnoreCase(pd.getGender(), GConstants.Gender_Female)){
									filepath = GConstants.FilePath_Default_Female_Symbol;
								}else{
									filepath = GConstants.FilePath_Default_Male_Symbol;
								}
							}
							pi.setPrimaryPicPath(""+contextPath.substring(1, contextPath.length())+"/"+GConstants.Action_load_image+"/"+filepath);
							/*pi.setPrimaryPicPath(GConstants.Action_load_image+"/"+filepath);*/
						}
					}
				}
				pi.setPhotos(photos);
				pi.setIsUploadAllowed(photos.size() < 2? true:false);
			}
		}
		return pi;
	}
	
	public List<UserPhoto> getPhotosInfoFromUI(PhotoInfo photoInfo, User user){
		List<UserPhoto> gPhotos = new ArrayList<>();
		if(photoInfo != null){
			for (Photo p : photoInfo.getPhotos()) {
				UserPhoto gp = null;
				if(p.getPhotoId() != null){
					gp = userPhotoDao.findOne(p.getPhotoId());
					gp.setIsSagaiPrimary(p.getIsPrimary());
					if(!p.getIsActive()){
						gp.setStatus(GConstants.Status_Inactive);
					}
					gp.setUpdateOn(DateUtils.now());
					gPhotos.add(gp);
				}else{
					gp = new UserPhoto();
					gp.setTitle(user.getUserPersonal().getFirstName()+" "+user.getUserPersonal().getLastName());
					gp.setUser(user);
					gp.setCategory(PhotoCategoryEnum.Sagai.toString());
					gp.setIsSagaiPrimary(p.getIsPrimary());
					gp.setStatus(GConstants.Status_Active);
					gp.setPath(p.getPath());
					gp.setUpdateOn(DateUtils.now());
					gPhotos.add(gp);
				}
			}
		}
		return gPhotos;
	}
}
