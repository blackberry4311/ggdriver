/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express
 * or implied. See the License for the specific language governing permissions
 * and limitations under
 * the License.
 */

package files.google.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;

/**
 * The File Download Progress Listener.
 *
 */
public class FileDownloadProgressListener implements MediaHttpDownloaderProgressListener {
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadProgressListener.class);

	@Override
	public void progressChanged(MediaHttpDownloader downloader) {
		switch (downloader.getDownloadState()) {
			case MEDIA_IN_PROGRESS:
				logger.info("Download is in progress: " + downloader.getProgress());
				break;
			case MEDIA_COMPLETE:
				logger.info("Download is Complete!");
				break;
			default:
				logger.info("Default case, what the shit media type download is this ????");
				break;
		}
	}
}
