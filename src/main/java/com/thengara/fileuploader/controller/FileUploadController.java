package com.thengara.fileuploader.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.thengara.fileuploader.service.FileuploadService;

@Controller
public class FileUploadController {

	private final FileuploadService fileuploadService;

	@Autowired
	FileUploadController(FileuploadService fileuploadService) {
		this.fileuploadService = fileuploadService;
	}

	@PostMapping("upload_xlsx")
	@ResponseBody
	public String handleXLSXFileUpload(@RequestParam("file") MultipartFile file)
			throws IOException {
		long startTime = System.currentTimeMillis();

		fileuploadService.uploExcelFile(file);
		long endtime = System.currentTimeMillis();

		return "You successfully uploaded " + file.getOriginalFilename()
				+ " in " + (endtime - startTime) / 1000 + " Seconds.";

	}

	@PostMapping("upload_1")
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file)
			throws IOException {
		fileuploadService.uploadFile(file);
		return "You successfully uploaded " + file.getOriginalFilename() + "!";

	}

	@RequestMapping(value = "/upload_2", method = RequestMethod.POST)
	public @ResponseBody Response<String> upload(HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				// Inform user about invalid request
				Response<String> responseObject = new Response<String>(false,
						"Not a multipart request.", "");
				return responseObject;
			}

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();

			// Parse the request
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				if (!item.isFormField()) {
					fileuploadService.uploadFiles(item);
				}
			}
		} catch (FileUploadException e) {
			return new Response<String>(false, "File upload error",
					e.toString());
		} catch (IOException e) {
			return new Response<String>(false, "Internal server IO error",
					e.toString());
		}
		long endtime = System.currentTimeMillis();

		return new Response<String>(true, "Success",
				(endtime - startTime) / 1000 + " Seconds");
	}

	@RequestMapping(value = "/uploader", method = RequestMethod.GET)
	public ModelAndView uploaderPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("uploader");
		return model;
	}

	private void isMultipartRequest() {

	}
}
