package com.thengara.fileuploader.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileUploadAsyncController {

	@RequestMapping(value = "/asyncupload", method = RequestMethod.POST)
	public @ResponseBody Response<String> upload(HttpServletRequest request) {
		AsyncContext asyncContext = request.getAsyncContext();

		long startTime = System.currentTimeMillis();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			// Inform user about invalid request
			Response<String> responseObject = new Response<String>(false, "Not a multipart request.", "");
			return responseObject;
		}

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload();

		asyncContext.start(() -> {
			FileItemIterator iter;
			try {
				iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (!item.isFormField()) {
						String filename = item.getName();
						// Process the input stream
						OutputStream out = new FileOutputStream("uploaded-" + filename);
						IOUtils.copy(stream, out);
						stream.close();
						out.close();
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
			asyncContext.complete();
		});

		long endtime = System.currentTimeMillis();

		return new Response<String>(true, "Success", (endtime - startTime) / 1000 + " Seconds");
	}

}
