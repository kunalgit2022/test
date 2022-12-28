package com.example.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.response.UploadFileResponse;
import com.example.demo.service.FileDownloadService;
import com.example.demo.service.FileUploadService;

@RestController
@RequestMapping("/api/file")
public class FileUpload {
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private FileDownloadService fileDownloadService;

	@PostMapping("/fileUpload")
	public UploadFileResponse fileUpload(@RequestParam(name = "file") MultipartFile file) 
	{
		String fileName = fileUploadService.uploadFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request,HttpServletResponse response) {
        fileDownloadService.loadFileAsResource(request, response, fileName);
        return ResponseEntity.ok("file downloaded successfully");
    }


}
