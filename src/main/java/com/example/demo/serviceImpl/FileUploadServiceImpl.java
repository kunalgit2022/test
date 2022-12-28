package com.example.demo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.customException.FileStorageException;
import com.example.demo.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {

	@Value("${file.upload-dir}")
	private String filelocation;

	@Override
	public String uploadFile(MultipartFile file) {
		Path fileStorageLocation = Paths.get(filelocation).toAbsolutePath().normalize();
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("issue with creating file directory");
		}
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path filePath = Paths.get(fileStorageLocation + "\\" + fileName);
		try {
			// Check if the file's name contains invalid characters
			if (fileName.isEmpty() || fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence or file is empty" + fileName);
			} else {
				// Copy file to the target location (Replacing existing file with the same name)

				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				return fileName;
			}
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}

	}

}
