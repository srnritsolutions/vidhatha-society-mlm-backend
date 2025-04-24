package com.srnr.vidhatasocietymlm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import lombok.Getter;

@Component
@Getter
public class FileStorageProperties {
	
	@Value("{file.storage.images}")
	private String imageStoragePath;
	
	
	@Value("{spring.servlet.multipart.max-file-size}")
	private Long getMaxFileSize;
	
}


