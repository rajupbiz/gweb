package com.blob.controller.sagai;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blob.controller.BaseController;
import com.blob.dao.user.UserAstroDao;
import com.blob.dao.user.UserDao;
import com.blob.dao.user.UserFamilyDao;
import com.blob.dao.user.UserPersonalDao;
import com.blob.dao.user.UserPhotoDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.account.Account;
import com.blob.model.ui.ContactInfo;
import com.blob.model.ui.EduOccuInfo;
import com.blob.model.ui.FamilyInfo;
import com.blob.model.ui.PersonalInfo;
import com.blob.model.ui.PhotoInfo;
import com.blob.model.user.User;
import com.blob.model.user.UserAddress;
import com.blob.model.user.UserAstro;
import com.blob.model.user.UserContact;
import com.blob.model.user.UserEducation;
import com.blob.model.user.UserFamily;
import com.blob.model.user.UserOccupation;
import com.blob.model.user.UserPersonal;
import com.blob.model.user.UserPhoto;
import com.blob.security.SessionService;
import com.blob.service.common.CommonService;
import com.blob.service.sagai.ProfileService;
import com.blob.service.sagai.UserPhotoService;
import com.blob.service.sagai.UserService;
import com.blob.service.sagai.UserUIService;

@Controller
public class ProfileController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserUIService userUIService;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private UserPhotoDao userPhotoDao;
	
	@Resource
	private UserPersonalDao userPersonalDao;
	
	@Resource
	private UserAstroDao userAstroDao;
	
	@Resource
	private UserFamilyDao userFamilyDao;
	
	@Resource
	private ProfileService profileService;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private UserPhotoService userPhotoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/profile/update", method=RequestMethod.GET)
	public ModelAndView vUpdateProfile(){

		logger.debug(" \n update profile .............  ");
		Model m = new ExtendedModelMap();
		Boolean isNewUser = false;
		Account account = getLoggedInAccount();
		User c = userService.getUserByAccount(account);
		if(c == null){
			isNewUser = true;
			c = new User();
			c.setUserPersonal(new UserPersonal());
		}
		System.out.println(" update-profile is called 85 ");
		m.addAttribute("user", c);
		m.addAttribute("personalInfo", userUIService.getPersonalInfoSectionForUI(c, true));
		m.addAttribute("familyInfo", userUIService.getFamilyInfoSectionForUI(c));
		m.addAttribute("contactInfo", userUIService.getContactInfoSectionForUI(c, true));
		m.addAttribute("eduOccuInfo", userUIService.getEducationInfoSectionForUI(c, true));
		m.addAttribute("photoInfo", userUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		m.addAttribute("isNewUser", isNewUser);
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_update_profile.toString(), account);
		return new ModelAndView("/sagai/update-profile", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/edit", method=RequestMethod.GET)
	public ModelAndView ePersonalInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("personalInfo", userUIService.getPersonalInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/get", method=RequestMethod.GET)
	public ModelAndView vPersonalInfo(){

		Model m = new ExtendedModelMap();
		Boolean isNewUser = false;
		User c = getLoggedInUser();
		if(c == null){
			isNewUser = true;
			c = new User();
			c.setUserPersonal(new UserPersonal());
		}
		//m.addAttribute("user", c);
		m.addAttribute("personalInfo", userUIService.getPersonalInfoSectionForUI(c, true));
		m.addAttribute("isNewUser", isNewUser);
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/save", method=RequestMethod.POST)
	public ModelAndView sPersonalInfo(@ModelAttribute("personalInfo") PersonalInfo personalInfo, BindingResult result, Model model){

		UserPersonal personalDet = userUIService.getUserPersonalInfoFromUI(personalInfo);
		UserAstro astroDet = userUIService.getUserAstroFromUI(personalInfo);
		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		
		personalDet.setId(c.getUserPersonal().getId());
		personalDet.setUser(c);
		personalDet.setUpdateOn(new Date());
		personalDet = userPersonalDao.save(personalDet);
		c.setUserPersonal(personalDet);
		if(c.getUserAstro() != null){
			astroDet.setId(c.getUserAstro().getId());
		}
		astroDet.setUser(c);
		astroDet.setUpdateOn(new Date());
		astroDet = userAstroDao.save(astroDet);
		c.setUserAstro(astroDet);
		
		m.addAttribute("personalInfo", userUIService.getPersonalInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/edit", method=RequestMethod.GET)
	public ModelAndView eFamilyInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("familyInfo", userUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/get", method=RequestMethod.GET)
	public ModelAndView vFamilyInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("familyInfo", userUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/save", method=RequestMethod.POST)
	public ModelAndView sFamilyInfo(@ModelAttribute("familyInfo") FamilyInfo familyInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		UserFamily cf = userUIService.getFamilyInfoFromUI(familyInfo);
		if(c.getUserFamily() != null){
			cf.setId(c.getUserFamily().getId());
		}
		cf.setUser(c);
		cf = userFamilyDao.save(cf);
		c.setUserFamily(cf);
		m.addAttribute("familyInfo", userUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/edit", method=RequestMethod.GET)
	public ModelAndView eContactInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("contactInfo", userUIService.getContactInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/get", method=RequestMethod.GET)
	public ModelAndView vContactInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("contactInfo", userUIService.getContactInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/save", method=RequestMethod.POST)
	public ModelAndView sContactInfo(@ModelAttribute("contactInfo") ContactInfo contactInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		List<UserContact> contacts = userUIService.getContactsInfoFromUI(contactInfo);
		List<UserAddress> addresses = userUIService.getAddressesInfoFromUI(c, contactInfo);
		//UserPersonal pd = c.getUserPersonal();
		//pd.setNativePlace(contactInfo.getNativePlace());
		//pd = userPersonalDetailDao.save(pd);
		contacts = profileService.saveUserContacts(contacts, c);
		addresses = profileService.saveUserAddress(addresses, c);
		if(contacts != null && !contacts.isEmpty()){
			c.setUserContacts(contacts);
		}
		if(addresses != null && !addresses.isEmpty()){
			c.setUserAddresses(addresses);
		}
		//c.setUserPersonal(pd);
		m.addAttribute("contactInfo", userUIService.getContactInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/edit", method=RequestMethod.GET)
	public ModelAndView eEducationInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("eduOccuInfo", userUIService.getEducationInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/get", method=RequestMethod.GET)
	public ModelAndView vEducationInfo(){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		//User c = userService.getUserByUser(user);
		m.addAttribute("eduOccuInfo", userUIService.getEducationInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/save", method=RequestMethod.POST)
	public ModelAndView sEducationInfo(@ModelAttribute("eduOccuInfo") EduOccuInfo eduOccuInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		List<UserEducation> educations = userUIService.getEducationsInfoFromUI(eduOccuInfo);
		List<UserOccupation> occupations = userUIService.getOccupationsInfoFromUI(eduOccuInfo);
		educations = profileService.saveUserEducation(educations, c);
		occupations = profileService.saveUserOccupation(occupations, c);
		if(educations != null && !educations.isEmpty()){
			c.setUserEducations(educations);
		}
		if(occupations != null && !occupations.isEmpty()){
			c.setUserOccupations(occupations);
		}
		m.addAttribute("eduOccuInfo", userUIService.getEducationInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/preview", method=RequestMethod.GET)
	public ModelAndView vPreviewProfile(){

		Account account = getLoggedInAccount();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_preview_profile.toString(), account);
		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		m.addAttribute("profile", profileService.getSagaiProfile(c.getId(), request.getContextPath()));
		return new ModelAndView("/sagai/preview-profile", m.asMap());
	}
	
	@RequestMapping(value="/profile/shortlisted", method=RequestMethod.GET)
	public ModelAndView vShortlistedProfiles(){

		Account account = getLoggedInAccount();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_shortlisted_profiles.toString(), account);
		Model m = new ExtendedModelMap();
		return new ModelAndView("/sagai/shortlisted-profiles", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/edit", method=RequestMethod.GET)
	public ModelAndView ePhotoInfo() throws Exception{

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
//		String baseURL = commonService.getURLBase(request);
//		baseURL += request.getContextPath();
		m.addAttribute("photoInfo", userUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/get", method=RequestMethod.GET)
	public ModelAndView vPhotoInfo()  throws Exception{

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		//User c = userService.getUserByUser(user);
//		String baseURL = commonService.getURLBase(request);
		m.addAttribute("photoInfo", userUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/save", method=RequestMethod.POST)
	public ModelAndView sPhotoInfo(@ModelAttribute("photoInfo") PhotoInfo photoInfo, BindingResult result, Model model) throws Exception{

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		//User c = userService.getUserByUser(user);
		List<UserPhoto> pi = userUIService.getPhotosInfoFromUI(photoInfo, c);
		if(CollectionUtils.isNotEmpty(pi)){
			for (UserPhoto gPhoto : pi) {
				userPhotoDao.save(gPhoto);
			}
		}
//		String baseURL = commonService.getURLBase(request);
		m.addAttribute("photoInfo", userUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/make-primary", method=RequestMethod.GET)
	public ModelAndView makePrimary(@RequestParam("photo") Long photo) throws Exception{

		Model m = new ExtendedModelMap();
		User c = getLoggedInUser();
		UserPhoto gPhoto = userPhotoDao.findOne(photo);
		Boolean resp = userPhotoService.setPhotoAsSagaiPrimary(c, gPhoto);
		if(resp){
			//User c = userService.getUserByUser(user);
			m.addAttribute("photoInfo", userUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		}else{
			// TODO: handle error
		}
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoEdit", m.asMap());
	}
}
