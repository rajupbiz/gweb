package com.blob.controller.sagai;

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
import com.blob.dao.candidate.CandidateAstroDetailDao;
import com.blob.dao.candidate.CandidateDao;
import com.blob.dao.candidate.CandidateFamilyDao;
import com.blob.dao.candidate.CandidatePersonalDetailDao;
import com.blob.dao.common.GPhotoDao;
import com.blob.enums.MenuTabEnum;
import com.blob.model.candidate.Candidate;
import com.blob.model.candidate.CandidateAddress;
import com.blob.model.candidate.CandidateAstroDetail;
import com.blob.model.candidate.CandidateContact;
import com.blob.model.candidate.CandidateEducation;
import com.blob.model.candidate.CandidateFamily;
import com.blob.model.candidate.CandidateOccupation;
import com.blob.model.candidate.CandidatePersonalDetail;
import com.blob.model.common.GPhoto;
import com.blob.model.common.User;
import com.blob.model.ui.ContactInfo;
import com.blob.model.ui.EduOccuInfo;
import com.blob.model.ui.FamilyInfo;
import com.blob.model.ui.PersonalInfo;
import com.blob.model.ui.PhotoInfo;
import com.blob.security.SessionService;
import com.blob.service.candidate.CandidatePhotoService;
import com.blob.service.candidate.CandidateService;
import com.blob.service.candidate.CandidateUIService;
import com.blob.service.candidate.ProfileService;
import com.blob.service.common.CommonService;

@Controller
public class ProfileController extends BaseController {

	@Resource
	private SessionService sessionService;
	
	@Resource
	private CandidateService candidateService;
	
	@Resource
	private CandidateUIService candidateUIService;
	
	@Resource
	private CandidateDao candidateDao;
	
	@Resource
	private GPhotoDao gPhotoDao;
	
	@Resource
	private CandidatePersonalDetailDao candidatePersonalDetailDao;
	
	@Resource
	private CandidateAstroDetailDao candidateAstroDetailDao;
	
	@Resource
	private CandidateFamilyDao candidateFamilyDao;
	
	@Resource
	private ProfileService profileService;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private CandidatePhotoService candidatePhotoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/profile/update", method=RequestMethod.GET)
	public ModelAndView vUpdateProfile(){

		logger.debug(" \n update profile .............  ");
		Model m = new ExtendedModelMap();
		Boolean isNewCandidate = false;
		User user = getLoggedInUser();
		Candidate c = candidateService.getCandidateByUser(user);
		if(c == null){
			isNewCandidate = true;
			c = new Candidate();
			c.setCandidatePersonalDetail(new CandidatePersonalDetail());
		}
		System.out.println(" update-profile is called 85 ");
		m.addAttribute("candidate", c);
		m.addAttribute("personalInfo", candidateUIService.getPersonalInfoSectionForUI(c, true));
		m.addAttribute("familyInfo", candidateUIService.getFamilyInfoSectionForUI(c));
		m.addAttribute("contactInfo", candidateUIService.getContactInfoSectionForUI(c, true));
		m.addAttribute("eduOccuInfo", candidateUIService.getEducationInfoSectionForUI(c, true));
		m.addAttribute("photoInfo", candidateUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		m.addAttribute("isNewCandidate", isNewCandidate);
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_update_profile.toString(), user);
		return new ModelAndView("/sagai/update-profile", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/edit", method=RequestMethod.GET)
	public ModelAndView ePersonalInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("personalInfo", candidateUIService.getPersonalInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/get", method=RequestMethod.GET)
	public ModelAndView vPersonalInfo(){

		Model m = new ExtendedModelMap();
		Boolean isNewCandidate = false;
		Candidate c = getLoggedInSagaiCandidate();
		if(c == null){
			isNewCandidate = true;
			c = new Candidate();
			c.setCandidatePersonalDetail(new CandidatePersonalDetail());
		}
		//m.addAttribute("candidate", c);
		m.addAttribute("personalInfo", candidateUIService.getPersonalInfoSectionForUI(c, true));
		m.addAttribute("isNewCandidate", isNewCandidate);
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/personal/save", method=RequestMethod.POST)
	public ModelAndView sPersonalInfo(@ModelAttribute("personalInfo") PersonalInfo personalInfo, BindingResult result, Model model){

		CandidatePersonalDetail personalDet = candidateUIService.getCandidatePersonalInfoFromUI(personalInfo);
		CandidateAstroDetail astroDet = candidateUIService.getCandidateAstroDetailFromUI(personalInfo);
		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		Candidate c = null;
		if(!candidateService.isCandidate(user)){
			// new candidate
			c = candidateService.registerAsCandidate(user);
		}else{
			c = candidateService.getCandidateByUser(user);
			personalDet.setId(c.getCandidatePersonalDetail().getId());
			astroDet.setId(c.getCandidateAstroDetail().getId());
		}
		astroDet.setCandidate(c);
		personalDet.setCandidate(c);
		personalDet = candidatePersonalDetailDao.save(personalDet);
		astroDet = candidateAstroDetailDao.save(astroDet);
		c.setCandidatePersonalDetail(personalDet);
		c.setCandidateAstroDetail(astroDet);
		m.addAttribute("personalInfo", candidateUIService.getPersonalInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-personal-info :: personalInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/edit", method=RequestMethod.GET)
	public ModelAndView eFamilyInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("familyInfo", candidateUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/get", method=RequestMethod.GET)
	public ModelAndView vFamilyInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("familyInfo", candidateUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/family/save", method=RequestMethod.POST)
	public ModelAndView sFamilyInfo(@ModelAttribute("familyInfo") FamilyInfo familyInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		CandidateFamily cf = candidateUIService.getFamilyInfoFromUI(familyInfo);
		if(c.getCandidateFamily() != null){
			cf.setId(c.getCandidateFamily().getId());
		}
		cf.setCandidate(c);
		cf = candidateFamilyDao.save(cf);
		c.setCandidateFamily(cf);
		m.addAttribute("familyInfo", candidateUIService.getFamilyInfoSectionForUI(c));
		return new ModelAndView("sagai/fragments/f-family-info :: familyInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/edit", method=RequestMethod.GET)
	public ModelAndView eContactInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("contactInfo", candidateUIService.getContactInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/get", method=RequestMethod.GET)
	public ModelAndView vContactInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("contactInfo", candidateUIService.getContactInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/contact/save", method=RequestMethod.POST)
	public ModelAndView sContactInfo(@ModelAttribute("contactInfo") ContactInfo contactInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		List<CandidateContact> contacts = candidateUIService.getContactsInfoFromUI(contactInfo);
		List<CandidateAddress> addresses = candidateUIService.getAddressesInfoFromUI(c, contactInfo);
		//CandidatePersonalDetail pd = c.getCandidatePersonalDetail();
		//pd.setNativePlace(contactInfo.getNativePlace());
		//pd = candidatePersonalDetailDao.save(pd);
		contacts = profileService.saveCandidateContacts(contacts, c);
		addresses = profileService.saveCandidateAddress(addresses, c);
		if(contacts != null && !contacts.isEmpty()){
			c.setCandidateContacts(contacts);
		}
		if(addresses != null && !addresses.isEmpty()){
			c.setCandidateAddresses(addresses);
		}
		//c.setCandidatePersonalDetail(pd);
		m.addAttribute("contactInfo", candidateUIService.getContactInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-contact-info :: contactInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/edit", method=RequestMethod.GET)
	public ModelAndView eEducationInfo(){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("eduOccuInfo", candidateUIService.getEducationInfoSectionForUI(c, false));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/get", method=RequestMethod.GET)
	public ModelAndView vEducationInfo(){

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		Candidate c = candidateService.getCandidateByUser(user);
		m.addAttribute("eduOccuInfo", candidateUIService.getEducationInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/education/save", method=RequestMethod.POST)
	public ModelAndView sEducationInfo(@ModelAttribute("eduOccuInfo") EduOccuInfo eduOccuInfo, BindingResult result, Model model){

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		List<CandidateEducation> educations = candidateUIService.getEducationsInfoFromUI(eduOccuInfo);
		List<CandidateOccupation> occupations = candidateUIService.getOccupationsInfoFromUI(eduOccuInfo);
		educations = profileService.saveCandidateEducation(educations, c);
		occupations = profileService.saveCandidateOccupation(occupations, c);
		if(educations != null && !educations.isEmpty()){
			c.setCandidateEducations(educations);
		}
		if(occupations != null && !occupations.isEmpty()){
			c.setCandidateOccupations(occupations);
		}
		m.addAttribute("eduOccuInfo", candidateUIService.getEducationInfoSectionForUI(c, true));
		return new ModelAndView("sagai/fragments/f-education-info :: educationInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/preview", method=RequestMethod.GET)
	public ModelAndView vPreviewProfile(){

		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_preview_profile.toString(), user);
		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
		m.addAttribute("profile", profileService.getSagaiProfile(c.getId(), request.getContextPath()));
		return new ModelAndView("/sagai/preview-profile", m.asMap());
	}
	
	@RequestMapping(value="/profile/shortlisted", method=RequestMethod.GET)
	public ModelAndView vShortlistedProfiles(){

		User user = getLoggedInUser();
		sessionService.setMenuChangeCommonAttribtesInSession(request.getSession(), MenuTabEnum.sagai_shortlisted_profiles.toString(), user);
		Model m = new ExtendedModelMap();
		return new ModelAndView("/sagai/shortlisted-profiles", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/edit", method=RequestMethod.GET)
	public ModelAndView ePhotoInfo() throws Exception{

		Model m = new ExtendedModelMap();
		Candidate c = getLoggedInSagaiCandidate();
//		String baseURL = commonService.getURLBase(request);
//		baseURL += request.getContextPath();
		m.addAttribute("photoInfo", candidateUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoEdit", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/get", method=RequestMethod.GET)
	public ModelAndView vPhotoInfo()  throws Exception{

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		Candidate c = candidateService.getCandidateByUser(user);
//		String baseURL = commonService.getURLBase(request);
		m.addAttribute("photoInfo", candidateUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/photo/save", method=RequestMethod.POST)
	public ModelAndView sPhotoInfo(@ModelAttribute("photoInfo") PhotoInfo photoInfo, BindingResult result, Model model) throws Exception{

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		Candidate c = candidateService.getCandidateByUser(user);
		List<GPhoto> pi = candidateUIService.getPhotosInfoFromUI(photoInfo, user);
		if(CollectionUtils.isNotEmpty(pi)){
			for (GPhoto gPhoto : pi) {
				gPhotoDao.save(gPhoto);
			}
		}
//		String baseURL = commonService.getURLBase(request);
		m.addAttribute("photoInfo", candidateUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoView", m.asMap());
	}
	
	@RequestMapping(value="/profile/make-primary", method=RequestMethod.GET)
	public ModelAndView makePrimary(@RequestParam("photo") Long photo) throws Exception{

		Model m = new ExtendedModelMap();
		User user = getLoggedInUser();
		GPhoto gPhoto = gPhotoDao.findOne(photo);
		Boolean resp = candidatePhotoService.setPhotoAsSagaiPrimary(user, gPhoto);
		if(resp){
			Candidate c = candidateService.getCandidateByUser(user);
			m.addAttribute("photoInfo", candidateUIService.getPhotoInfoSectionForUI(c, request.getContextPath()));
		}else{
			// TODO: handle error
		}
		return new ModelAndView("sagai/fragments/f-photo-info :: photoInfoEdit", m.asMap());
	}
}
