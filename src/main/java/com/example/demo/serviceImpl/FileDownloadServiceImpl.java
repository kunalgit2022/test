package com.example.demo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.service.FileDownloadService;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {

	@Value("${file.upload-dir}")
	private String filelocation;

	@Override
	public void loadFileAsResource(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Path file = Paths.get(filelocation, fileName);
		if (Files.exists(file)) {
			//response.setContentType("application/pdf");
			//response.setContentType("application/vnd");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
