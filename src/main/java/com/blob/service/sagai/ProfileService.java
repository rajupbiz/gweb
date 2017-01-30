package com.blob.service.sagai;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.blob.dao.common.SystemPropertyDao;
import com.blob.dao.master.MasterDegreeDao;
import com.blob.dao.user.UserAddressDao;
import com.blob.dao.user.UserContactDao;
import com.blob.dao.user.UserDao;
import com.blob.dao.user.UserEducationDao;
import com.blob.dao.user.UserOccupationDao;
import com.blob.model.ui.ProfileFilter;
import com.blob.model.ui.ProfileSearchResult;
import com.blob.model.ui.ProfileSummary;
import com.blob.model.ui.SagaiProfile;
import com.blob.model.user.User;
import com.blob.model.user.UserAddress;
import com.blob.model.user.UserContact;
import com.blob.model.user.UserEducation;
import com.blob.model.user.UserOccupation;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;

@Service
public class ProfileService {
	
	@Resource
	private UserContactDao userContactDao;
	
	@Resource
	private UserAddressDao userAddressDao;
	
	@Resource
	private UserEducationDao userEducationDao;
	
	@Resource
	private UserOccupationDao userOccupationDao;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MasterDegreeDao masterDegreeDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	@Resource
	private UserUIService userUIService;

	public List<UserContact> saveUserContacts(List<UserContact> contacts, User c){

		List<UserContact> resp = new ArrayList<>();
		if(contacts != null && !contacts.isEmpty()){
			for (UserContact userContact : contacts) {
				userContact.setUser(c);
				UserContact savedContact = userContactDao.save(userContact);
				resp.add(savedContact);
			}
		}
		return resp;
	}
	
	public List<UserAddress> saveUserAddress(List<UserAddress> addresses, User c){
		
		List<UserAddress> resp = new ArrayList<>();
		if(addresses != null && !addresses.isEmpty()){
			for (UserAddress address : addresses) {
				address.setUser(c);
				UserAddress savedAddress = userAddressDao.save(address);
				resp.add(savedAddress);
			}
		}
		return resp;
	}
	
	public List<UserEducation> saveUserEducation(List<UserEducation> educations, User c){

		List<UserEducation> resp = new ArrayList<>();
		if(educations != null && !educations.isEmpty()){
			for (UserEducation userEducation : educations) {
				userEducation.setUser(c);
				UserEducation savedEducation = userEducationDao.save(userEducation);
				resp.add(savedEducation);
			}
		}
		return resp;
	}
	
	public List<UserOccupation> saveUserOccupation(List<UserOccupation> occupations, User c){

		List<UserOccupation> resp = new ArrayList<>();
		if(occupations != null && !occupations.isEmpty()){
			
			for (UserOccupation userOccupation : occupations) {
				userOccupation.setUser(c);
				UserOccupation savedOccupation = userOccupationDao.save(userOccupation);
				resp.add(savedOccupation);
			}
		}
		return resp;
	}
	
	public ProfileSearchResult getProfiles(HttpServletRequest request, ProfileFilter profileFilter){
		
		ProfileSearchResult result = new ProfileSearchResult();
		int pageSize = Integer.valueOf((String) request.getSession().getAttribute(GConstants.SAGAI_DEFAULT_PAGE_SIZE));
		Long totalRecords = userDao.countSagaiProfileResult(profileFilter.getLoggedInUserId().toString(), profileFilter.getGid(), "%"+profileFilter.getName()+"%");		//	
		result.setTotalRecords(totalRecords);
		result.setPageSize(pageSize);
		result.setTotalPages(Math.toIntExact((result.getTotalRecords() + pageSize - 1) / pageSize));
		if(profileFilter.getPageNo() != null && profileFilter.getPageNo() > 1){
			result.setCurrentPageNo(profileFilter.getPageNo());
		}else{
			result.setCurrentPageNo(1);
		}
		if(result.getCurrentPageNo() < result.getTotalPages()){
			result.setNextPageNo(result.getCurrentPageNo() + 1);
		}else{
			result.setNextPageNo(0);
		}
		if(result.getCurrentPageNo() > 1){
			result.setPreviousPageNo(result.getCurrentPageNo() - 1);
		}else{
			result.setPreviousPageNo(0);
		}
		System.out.println(" totalRecords : "+totalRecords+"  pages : "+result.getTotalPages());
		final PageRequest page2 = new PageRequest(
				  result.getCurrentPageNo()-1, pageSize, new Sort(
				    new Order(Direction.DESC, "id")
				  )
				);
		List<BigInteger> userIdsList = userDao.searchSagaiProfiles(profileFilter.getLoggedInUserId().toString(), profileFilter.getGid(), "%"+profileFilter.getName()+"%", page2);
		StringBuffer userIdsStr = new StringBuffer();
		for (BigInteger id : userIdsList) {
			userIdsStr.append(id+",");
			//System.out.println(" id "+id);
		}
		List<ProfileSummary> content = new ArrayList<>(pageSize);
		if(CollectionUtils.isNotEmpty(userIdsList)){
			String userIds = userIdsStr.substring(0, userIdsStr.length() - 1);
			System.out.println(" userIds "+userIds);
			List<Object[]> rows = userDao.getSagaiProfileSummary(userIdsList);
			for (Object[] row : rows) {
				ProfileSummary p = new ProfileSummary();
				p.setId(row[0].toString());
				p.setGid(row[1].toString());
				p.setGender(row[20] != null ? row[20].toString() : "");
				p.setFullName(row[3].toString() + (row[5] != null ? " "+row[5].toString() : "") + (row[4] != null ? " "+row[4].toString() : ""));
				//p.setFathersFullName((row[5] != null ? " "+ row[5].toString() : "") + (row[6] != null ? " "+row[6].toString() : "") + (row[7] != null ? " "+row[7].toString() : ""));
				p.setCurrentLocation((row[10] != null ? " "+ row[10].toString() : "") + (row[11] != null ? " "+row[11].toString() : ""));
				System.out.println("  row[9] " +row[9]);
				p.setHomeTown(row[9] != null ? row[9].toString() : "");
				p.setMamasTown(row[8] != null ? row[8].toString() : "");
				//p.setAbout(row[16] != null ? row[16].toString() : "");
				//String filepath = Paths.get("load-image", row[18] != null ? row[18].toString() : "").toString();
				String filepath = (row[18] != null ? row[18].toString() : "");
				if(filepath.equalsIgnoreCase("")){
					if(StringUtils.isNoneBlank(p.getGender()) && p.getGender().equalsIgnoreCase(GConstants.Gender_Female)){
						filepath = GConstants.FilePath_Default_Female_Symbol;
					}else{
						filepath = GConstants.FilePath_Default_Male_Symbol;
					}
				}
				String contextPath = request.getContextPath();
				p.setPrimaryPicPath(""+contextPath.substring(1, contextPath.length())+"/"+GConstants.Action_load_image+"/"+filepath);
				
				System.out.println("  dob "+row[17]);
				if(row[17] != null){
					try {
						DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
						String dob = DateUtils.toDDMMMYYYY(DateUtils.toLocalDate(fromFormat.parse(row[17].toString())));
						System.out.println("  dob "+dob);
						p.setDob(dob);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				//p.setEducation(row[14].toString() + " " + row[15].toString());
				//p.setOccupation(row[12].toString() + " " + row[13].toString());
				
				System.out.println("  >>  "+p.toString());
				
				content.add(p);
			}
			result.setProfiles(content);
		}
		return result;
	}
	
	public SagaiProfile getSagaiProfile(Long userId, String contextPath){
		SagaiProfile sagaiProfile = new SagaiProfile();
		User c = userDao.findOne(userId);
		sagaiProfile.setGid(c.getGid());
		sagaiProfile.setPersonalInfo(userUIService.getPersonalInfoSectionForUI(c, true));
		sagaiProfile.setFamilyInfo(userUIService.getFamilyInfoSectionForUI(c));
		sagaiProfile.setContactInfo(userUIService.getContactInfoSectionForUI(c, true));
		sagaiProfile.setEduOccInfo(userUIService.getEducationInfoSectionForUI(c, true));
		sagaiProfile.setPhotos(userUIService.getPhotoInfoSectionForUI(c, contextPath));
		return sagaiProfile;
	}
}
