package files.google.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.services.drive.model.File;

@Component
public class GoogleFileService {
	private static final Logger logger = LoggerFactory.getLogger(GoogleFileService.class);

	@Autowired
	GoogleDriverApi driverApi;

	public File uploadFileToGoogle(String filePath, String mediaType) throws Exception {
		return driverApi.uploadFile(filePath, mediaType, false);
	}

	public void downloadFileFromGoogle(String path) {

	}

}
