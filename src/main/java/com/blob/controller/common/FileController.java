package com.blob.controller.common;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
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
import com.blob.enums.ServicesEnum;
import com.blob.model.common.User;
import com.blob.service.candidate.CandidatePhotoService;

@Controller
public class FileController extends BaseController {

	@Resource
	private CandidatePhotoService candidatePhotoService;
	
	@Resource
	private SystemPropertyDao systemPropertyDao;
	
	@Autowired
    private ResourceLoader resourceLoader;
	
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
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {

		try {
			User user = getLoggedInUser();
			String service = getCurrentService();
			if(StringUtils.isNotBlank(service) && service.equalsIgnoreCase(ServicesEnum.SAGAI.toString())){
				candidatePhotoService.uploadPhoto(user, uploadfile);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
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
	@RequestMapping(value = "/removeFile", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "loadImage/{fileName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> loadImage(@PathVariable String fileName) {
		
		System.out.println(" filename  "+fileName);
		
		resourceLoader.getResource("file://C:/Users/RPATEL/Google Drive/00_biz/gomaie/pics/sagai/1_Sagai_3.jpg");
		
		
		return new ResponseEntity<>(HttpStatus.OK);
		
		/*SystemProperty systemProperty = systemPropertyDao.findByListNameAndListKeyAndStatus(GConstants.ListName_FILE_UPLOAD_PATH, GConstants.ListKey_SAGAI_PHOTO, StatusEnum.Active.toString());
		if(systemProperty != null && StringUtils.isNotBlank(systemProperty.getListValue())){
			String directory = systemProperty.getListValue();
			String filepath = Paths.get(directory, fileName).toString();
		}
		InputStream in = servletContext.getResourceAsStream("/images"+image);
		
		
	    return ResponseEntity.ok()
	            .contentLength(gridFsFile.getLength())
	            .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
	            .body(new InputStreamResource(gridFsFile.getInputStream()));*/
	}
}
