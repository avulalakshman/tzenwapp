package com.heraizen.dhi.tzen.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUPloadService {

	private static Logger LOGGER = LoggerFactory.getLogger(FileUPloadService.class);

	public Optional<String> uploadFile(MultipartFile file) {
		String name = file.getOriginalFilename();
		LOGGER.info("File name: {} and size of the file (bytes): {}", name, file.getSize());
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String dirName = System.getProperty("java.io.tmpdir");
				String path = dirName + "/" + name;
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
				stream.write(bytes);
				stream.close();
				LOGGER.info("File :{} is uploaded successfully", path);
				return Optional.of(path);
			} catch (Exception e) {
				return Optional.of(null);
			}
		} else {
			return Optional.of(null);
		}
	}

}
