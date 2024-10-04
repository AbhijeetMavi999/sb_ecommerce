package com.sb_ecom.service.impl;

import com.sb_ecom.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File name of current / original file
        String originalFileName = file.getOriginalFilename();
        // Generate a unique file name to avoid naming conflicts
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        // Check path if exist and create
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();
        // upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // returning file name
        return fileName;
    }
}
