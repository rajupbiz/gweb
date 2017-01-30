package com.blob.service.sagai;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blob.dao.common.SystemPropertyDao;
import com.blob.dao.user.UserPhotoDao;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.model.account.Account;
import com.blob.model.user.User;
import com.blob.model.user.UserPhoto;
import com.blob.security.SessionService;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;

@Service
public class UserPhotoService {
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private UserPhotoDao userPhotoDao;
	
	public void uploadPhoto(HttpServletRequest request, User user, MultipartFile uploadfile) throws Exception{
		
		String filePath = (String) request.getSession().getAttribute(GConstants.SAGAI_DEFAULT_FILE_UPLOAD_PATH);
		if(StringUtils.isNotBlank(filePath)){
			String origFileName = uploadfile.getOriginalFilename();
			String[] tokens = origFileName.split("\\.(?=[^\\.]+$)");
			if(tokens != null && tokens.length == 2){
				// upload photo
				Long photoCounter = userPhotoDao.countByUserAndCategory(user, PhotoCategoryEnum.Sagai.toString());
				photoCounter++;
				String fileName = user.getId()+"_"+PhotoCategoryEnum.Sagai.toString()+"_"+photoCounter+"."+tokens[1];
				String directory = filePath;
				String filepath = Paths.get(directory, fileName).toString();
				BufferedOutputStream stream = null;
				try{
					stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
					stream.write(uploadfile.getBytes());
				}finally {
					stream.close();
				}
				// save photo details
				UserPhoto gPhoto = new UserPhoto();
				gPhoto.setUser(user);
				gPhoto.setCategory(PhotoCategoryEnum.Sagai.toString());
				gPhoto.setCreateOn(DateUtils.now());
				gPhoto.setFileName(fileName);
				gPhoto.setOriginalFileName(origFileName);
				gPhoto.setStatus(GConstants.Status_Active);
				gPhoto.setUpdateOn(DateUtils.now());
				userPhotoDao.save(gPhoto);
			}
		}
	}
	
	public void removePhoto(Account account, UserPhoto gPhoto) throws Exception{
		if(gPhoto != null && StringUtils.isNotBlank(gPhoto.getStatus()) && gPhoto.getStatus().equalsIgnoreCase(GConstants.Status_Active)){
			gPhoto.setStatus(GConstants.Status_Inactive);
			gPhoto.setIsSagaiPrimary(false);
			gPhoto.setUpdateOn(DateUtils.now());
			userPhotoDao.save(gPhoto);
		}
	}
	
	public Boolean setPhotoAsSagaiPrimary(User user, UserPhoto gPhoto) {
		Boolean resp = false;
		try {
			if (gPhoto != null && StringUtils.isNotBlank(gPhoto.getStatus())
					&& gPhoto.getStatus().equalsIgnoreCase(GConstants.Status_Active)) {
				List<UserPhoto> allActivePhotos = userPhotoDao.findByUserAndCategoryAndStatus(user, PhotoCategoryEnum.Sagai.toString(), GConstants.Status_Active);
				if (CollectionUtils.isNotEmpty(allActivePhotos)) {
					for (UserPhoto gPhoto2 : allActivePhotos) {
						if (gPhoto2.getId().equals(gPhoto.getId())) {
							gPhoto2.setIsSagaiPrimary(true);
						} else {
							gPhoto2.setIsSagaiPrimary(false);
						}
						gPhoto2.setUpdateOn(DateUtils.now());
						userPhotoDao.save(gPhoto2);
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			resp = true;
		}
		return resp;
	}
}
