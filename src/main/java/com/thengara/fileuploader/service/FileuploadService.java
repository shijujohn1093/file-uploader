package com.thengara.fileuploader.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.monitorjbl.xlsx.StreamingReader;

@Service
public class FileuploadService {

	private final FileUtils fileUtils;

	@Autowired
	public FileuploadService(FileUtils fileUtils) {
		this.fileUtils = fileUtils;
	}

	public void uploadFile(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		IOUtils.copy(inputStream, outputStream);
		inputStream.close();
		outputStream.close();
	}

	public void uploadFile(MultipartFile file) throws IOException {
		InputStream inputStream = file.getInputStream();
		OutputStream outputStream = fileUtils
				.getTempDirectoryStream(file.getOriginalFilename());
		uploadFile(inputStream, outputStream);
	}

	public void uploadFiles(FileItemStream item) throws IOException {
		InputStream inputStream = item.openStream();
		OutputStream outputStream = fileUtils
				.getTempDirectoryStream(item.getName());
		uploadFile(inputStream, outputStream);
	}

	public void uploExcelFile(MultipartFile file) throws IOException {
		InputStream inputStream = file.getInputStream();
		Workbook workbook = StreamingReader.builder().rowCacheSize(100)
				.bufferSize(4096).open(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		long recordNumer = 1;
		for (Row r : sheet) {
			for (Cell c : r) {
				System.out.println(c.getStringCellValue() + "~" + recordNumer);
			}
			recordNumer++;
		}
		workbook.close();
		inputStream.close();
	}

}
