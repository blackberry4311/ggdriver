package files.google.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.drive.model.File;

import files.google.services.GoogleFileService;

@RestController
public class GoogleDriverRest {
	@Autowired
	GoogleFileService ggService;

	@RequestMapping(path = "/google/rest/upload", method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public File searchICareMember(@RequestBody Map<String, String> jsonInput) throws Exception {
		return ggService.uploadFileToGoogle(jsonInput.get("file_path"), jsonInput.get("media_type"));
	}
}
