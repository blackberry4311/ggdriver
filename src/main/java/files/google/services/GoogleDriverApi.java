package files.google.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Component
public class GoogleDriverApi {
	private static final Logger logger = LoggerFactory.getLogger(GoogleDriverApi.class);
	@Value("${google.driver.application.name:test}")
	String APPLICATION_NAME;

	@Value("{google.driver.application.folder.download:/Users/blackberry/Work/ggdriver}")
	String DIR_FOR_DOWNLOADS;

	/** Directory to store user credentials. */
	@Value("${google.driver.credentical.store.dir:./}")
	String credenticalStoredDir;
	// new java.io.File(System.getProperty("user.temp"), ".store/drive_sample");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single
	 * globally shared instance across your application.
	 */
	private FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global Drive API client. */
	private Drive drive;

	/** Authorizes the installed application to access user's protected data. */
	private Credential authorize() throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(
				GoogleDriverApi.class.getResourceAsStream("/client_secrets.json")));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			logger.error("Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
					+ "into drive-cmdline-sample/src/main/resources/client_secrets.json");
			throw new Exception("Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
					+ "into drive-cmdline-sample/src/main/resources/client_secrets.json");
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
				.build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	@PostConstruct
	public void init() throws Exception {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(new java.io.File(credenticalStoredDir));
		Credential credential = authorize();
		drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) {

		/*
		 * try {
		 * httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		 * dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		 * // authorization
		 * Credential credential = authorize();
		 * // set up the global Drive instance
		 * drive = new Drive.Builder(httpTransport, JSON_FACTORY,
		 * credential).setApplicationName(APPLICATION_NAME)
		 * .build();
		 * 
		 * // run commands
		 * 
		 * View.header1("Starting Resumable Media Upload");
		 * File uploadedFile = uploadFile(false);
		 * 
		 * View.header1("Updating Uploaded File Name");
		 * File updatedFile = updateFileWithTestSuffix(uploadedFile.getId());
		 * 
		 * View.header1("Starting Resumable Media Download");
		 * downloadFile(false, updatedFile);
		 * 
		 * View.header1("Starting Simple Media Upload");
		 * uploadedFile = uploadFile(true);
		 * 
		 * View.header1("Starting Simple Media Download");
		 * downloadFile(true, uploadedFile);
		 * 
		 * View.header1("Success!");
		 * return;
		 * } catch (IOException e) {
		 * System.err.println(e.getMessage());
		 * } catch (Throwable t) {
		 * t.printStackTrace();
		 * }
		 * System.exit(1);
		 */
	}

	/** Uploads a file using either resumable or direct media upload. */
	public File uploadFile(String filePath, String mediaType, boolean useDirectUpload) throws IOException {
		final java.io.File uploadFile = new java.io.File(filePath);
		File fileMetadata = new File();
		fileMetadata.setTitle(uploadFile.getName());

		FileContent mediaContent = new FileContent(mediaType, uploadFile); // mediaType
																			// :"image/jpeg"

		Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		uploader.setProgressListener(new FileUploadProgressListener());
		return insert.execute();
	}

	/** Updates the name of the uploaded file to have a "drivetest-" prefix. */
	// private File updateFileWithTestSuffix(String id) throws IOException {
	// File fileMetadata = new File();
	// fileMetadata.setTitle("drivetest-" + UPLOAD_FILE.getName());
	//
	// Drive.Files.Update update = drive.files().update(id, fileMetadata);
	// return update.execute();
	// }

	/** Downloads a file using either resumable or direct media download. */
	// private static void downloadFile(boolean useDirectDownload, File
	// uploadedFile) throws IOException {
	// // create parent directory (if necessary)
	// java.io.File parentDir = new java.io.File(DIR_FOR_DOWNLOADS);
	// if (!parentDir.exists() && !parentDir.mkdirs()) {
	// throw new IOException("Unable to create parent directory");
	// }
	// OutputStream out = new FileOutputStream(new java.io.File(parentDir,
	// uploadedFile.getTitle()));
	//
	// MediaHttpDownloader downloader = new MediaHttpDownloader(httpTransport,
	// drive.getRequestFactory()
	// .getInitializer());
	// downloader.setDirectDownloadEnabled(useDirectDownload);
	// downloader.setProgressListener(new FileDownloadProgressListener());
	// downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
	// }
}
