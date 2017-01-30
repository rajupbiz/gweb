package com.blob.service.sagai;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.blob.dao.user.UserDao;
import com.blob.enums.PhotoCategoryEnum;
import com.blob.model.account.Account;
import com.blob.model.user.User;
import com.blob.model.user.UserPhoto;
import com.blob.util.GConstants;

@Service
public class UserService {
	
	/*@Resource
	private GUtils gutils;*/
	
	@Resource
	private UserDao userDao;

	public User getUserByAccount(Account account){
	
		User c = account.getUser();
		/*List<User> users = user.getUsers();
		if(users != null && !users.isEmpty())
			c = users.get(0);*/
		return c;
	}
	
	public Boolean isSagaiUser(Account account){
		
		Boolean c = account.getIsSagaiServiceActive();
		return c;
	}
	
	/*public Account registerAsAccount(User user){
		
		User c = null;
		List<User> users = user.getUsers();
		if(users == null || users.isEmpty()){
			c = new User();
			c.setUser(user);
			c.setGid(gutils.generateGid());
			c.setStatus(GConstants.Status_Active);
			c.setCreateOn(DateUtils.toDate(LocalDateTime.now()));
			c.setUpdateOn(DateUtils.toDate(LocalDateTime.now()));
			c = userDao.save(c);
		}
		return c;
	}*/
	
	/*public List<UserService> getUserMessages(User user){
		
		List<UserService> messages = new ArrayList<>();
		List<UserService> userMessages = user.getUser().getMessages();
		if(CollectionUtils.isNotEmpty(userMessages)){
			for (GMessage gMessage : userMessages) {
				if(gMessage != null 
						&& gMessage.getStatus().equalsIgnoreCase(GConstants.Status_Active)
						&& gMessage.getCategory().equalsIgnoreCase(MessageCategoryEnum.Candidate.toString())){
					messages.add(gMessage);
				}
			}
		}
		return messages;
	}*/
	
	public List<UserPhoto> getUserPhotos(User user){
		
		List<UserPhoto> photos = null;
		if(user != null){
			List<UserPhoto> userPhotos = user.getUserPhotos();
			if(CollectionUtils.isNotEmpty(userPhotos)){
				photos = new ArrayList<>();
				for (UserPhoto gPhoto : userPhotos) {
					if(gPhoto != null 
							&& gPhoto.getStatus().equalsIgnoreCase(GConstants.Status_Active) 
							&& gPhoto.getCategory().equalsIgnoreCase(PhotoCategoryEnum.Sagai.toString())){
						photos.add(gPhoto);
					}
				}
			}
		}
		return photos;
	}
	
	public List<User> searchUser(){
		
		//List<User> users = userDao.searchUsers();
		/*for (User user : users) {
			System.out.println("  Id : "+user.getId()+"  Gid : "+user.getGid());
		}*/
		return null;
	}
}
