package com.example.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;

public interface FileDownloadService {

	void loadFileAsResource(HttpServletRequest request, HttpServletResponse response,String fileName);

}
