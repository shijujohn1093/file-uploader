package com.thengara.fileuploader.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileUtils {

	Logger logger = LoggerFactory.getLogger(FileUtils.class);

	@Value("${path.directory.temp}")
	private String tempDirectory;

	public final OutputStream getTempDirectoryStream(String fileName)
			throws FileNotFoundException {
		File directory = new File(tempDirectory);
		directory.mkdir();
		logger.info("Temp direcotry at " + directory.getAbsolutePath());
		OutputStream outputStream = new FileOutputStream(new File(directory,
				System.currentTimeMillis() + "-" + fileName));
		return outputStream;
	}
}
