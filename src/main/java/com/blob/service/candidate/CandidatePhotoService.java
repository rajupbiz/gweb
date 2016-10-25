package com.blob.service.candidate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blob.dao.common.GPhotoDao;
import com.blob.dao.common.SystemPropertyDao;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.enums.StatusEnum;
import com.blob.model.common.GPhoto;
import com.blob.model.common.SystemProperty;
import com.blob.model.common.User;
import com.blob.security.SessionService;
import com.blob.service.common.CommonService;
import com.blob.util.DateUtils;
import com.blob.util.GConstants;

@Service
public class CandidatePhotoService {
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	@Resource
	private SessionService sessionService;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private GPhotoDao gPhotoDao;
	
	public void uploadPhoto(User user, MultipartFile uploadfile) throws Exception{
		
		SystemProperty systemProperty = systemPropertyDao.findByListNameAndListKeyAndStatus(GConstants.ListName_FILE_UPLOAD_PATH, GConstants.ListKey_SAGAI_PHOTO, StatusEnum.Active.toString());
		if(systemProperty != null && StringUtils.isNotBlank(systemProperty.getListValue())){
			
			String origFileName = uploadfile.getOriginalFilename();
			String[] tokens = origFileName.split("\\.(?=[^\\.]+$)");
			if(tokens != null && tokens.length == 2){
				// upload photo
				Long photoCounter = gPhotoDao.countByUserAndCategory(user, PhotoCategoryEnum.Sagai.toString());
				photoCounter++;
				String fileName = user.getId()+"_"+PhotoCategoryEnum.Sagai.toString()+"_"+photoCounter+"."+tokens[1];
				String directory = systemProperty.getListValue();
				String filepath = Paths.get(directory, fileName).toString();
				BufferedOutputStream stream = null;
				try{
					stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
					stream.write(uploadfile.getBytes());
				}finally {
					stream.close();
				}
				// save photo details
				GPhoto gPhoto = new GPhoto();
				gPhoto.setUser(user);
				gPhoto.setCategory(PhotoCategoryEnum.Sagai.toString());
				gPhoto.setCreateOn(DateUtils.now());
				gPhoto.setFileName(fileName);
				gPhoto.setOriginalFileName(origFileName);
				gPhoto.setStatus(StatusEnum.Active.toString());
				gPhoto.setUpdateOn(DateUtils.now());
				gPhotoDao.save(gPhoto);
			}
		}
	}
	
	public void removePhoto(User user, GPhoto gPhoto) throws Exception{
		if(gPhoto != null && StringUtils.isNotBlank(gPhoto.getStatus()) && gPhoto.getStatus().equalsIgnoreCase(StatusEnum.Active.toString())){
			gPhoto.setStatus(StatusEnum.Inactive.toString());
			gPhoto.setIsSagaiPrimary(false);
			gPhoto.setUpdateOn(DateUtils.now());
			gPhotoDao.save(gPhoto);
		}
	}
	
	public void setPhotoAsSagaiPrimary(User user, GPhoto gPhoto) throws Exception{
		if(gPhoto != null && StringUtils.isNotBlank(gPhoto.getStatus()) && gPhoto.getStatus().equalsIgnoreCase(StatusEnum.Active.toString())){
			List<GPhoto> allActivePhotos = gPhotoDao.findByUserAndCategoryAndStatus(user, PhotoCategoryEnum.Sagai.toString(), StatusEnum.Active.toString());
			if(CollectionUtils.isNotEmpty(allActivePhotos)){
				for (GPhoto gPhoto2 : allActivePhotos) {
					if(gPhoto2.getId().equals(gPhoto.getId())){
						gPhoto2.setIsSagaiPrimary(true);
					}else{
						gPhoto2.setIsSagaiPrimary(false);
					}
					gPhoto2.setUpdateOn(DateUtils.now());
					gPhotoDao.save(gPhoto2);
				}
			}
		}
	}
}
