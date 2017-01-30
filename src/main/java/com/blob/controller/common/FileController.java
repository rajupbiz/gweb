package com.blob.controller.common;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.blob.controller.BaseController;
import com.blob.dao.common.SystemPropertyDao;
import com.blob.dao.user.UserPhotoDao;
import com.blob.model.error.FileUploadError;
import com.blob.model.user.User;
import com.blob.model.user.UserPhoto;
import com.blob.service.sagai.UserPhotoService;
import com.blob.util.GConstants;

@Controller
public class FileController extends BaseController {

	@Resource
	private UserPhotoService userPhotoService;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	@Resource
	private UserPhotoDao userPhotoDao;
	
	/**
	 * POST /uploadFile -> receive and locally save a file.
	 * 
	 * @param uploadfile
	 *            The uploaded file as Multipart file parameter in the HTTP
	 *            request. The RequestParam name must be the same of the
	 *            attribute "name" in the input tag with type file.
	 * 
	 * @return An http OK status in case of success, an http 4xx status in case of errors.
	 */
	@RequestMapping(value = "/upload-file", method = RequestMethod.POST)
	public @ResponseBody FileUploadError uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		FileUploadError error = new FileUploadError();
		try {
			User user = getLoggedInUser();
			String service = getCurrentService();
			if(StringUtils.isNotBlank(service) && service.equalsIgnoreCase(GConstants.Service_SAGAI)){
				userPhotoService.uploadPhoto(request, user, uploadfile);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			/*String[] keys = null;
			keys[0] = "";
			*/
			error.setError("Unable to upload file. Please try again later.");
			//error.setErrorkeys(errorkeys);
		}
		return error;
	}
	
	/**
	 * POST /uploadFile -> receive and locally save a file.
	 * 
	 * @param uploadfile
	 *            The uploaded file as Multipart file parameter in the HTTP
	 *            request. The RequestParam name must be the same of the
	 *            attribute "name" in the input tag with type file.
	 * 
	 * @return An http OK status in case of success, an http 4xx status in case of errors.
	 */
	@RequestMapping(value = "/remove-file", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> removeFile(@RequestParam("key") String key) {

		try {
			System.out.println("  remove file  "+key);
			/*User user = getLoggedInUser();
			String service = getCurrentService();
			if(StringUtils.isNotBlank(service) && service.equalsIgnoreCase(ServicesEnum.SAGAI.toString())){
				candidatePhotoService.uploadPhoto(user, uploadfile);
			}*/
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "load-image/{fileName}", method = RequestMethod.GET)
	@ResponseBody
	public void loadImage(@PathVariable String fileName) throws IOException {
		String filePath = (String) request.getSession().getAttribute(GConstants.SAGAI_DEFAULT_FILE_UPLOAD_PATH);
		System.out.println(" filename  "+fileName+"  filePath  "+filePath);
		
		UserPhoto gPhoto = userPhotoDao.findByFileNameLikeAndStatus(fileName+"%", GConstants.Status_Active);
		if(gPhoto != null){
			//filePath.replaceAll("\\", "\\\\");
			org.springframework.core.io.Resource r = resourceLoader.getResource("file:"+filePath+"\\"+gPhoto.getFileName());		// +".jpg"	file:C:\\Users\\RPATEL\\Google Drive\\00_biz\\gomaie\\pics\\sagai\\
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		    IOUtils.copy(r.getInputStream(), response.getOutputStream());
		}
	}
}
