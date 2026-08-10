package com.springboot.insurance.utility;


import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class UploadUtility {
    // This method checks if the image extension given is allowed or not
    public void validateImage(MultipartFile file) throws FileUploadException {

        if(file == null || file.isEmpty())
            throw new FileUploadException("Image not given");

        String imageName = file.getOriginalFilename();

        List<String> allowedList = List.of("png","jpg","jpeg");
        // image name : 836.jpg
        String[] arr = imageName.split("\\.");

        if(arr.length != 2)
            throw new FileUploadException("Invalid Image Name");

        String ext = arr[1]; // Extension: png

        if(!allowedList.contains(ext))
            throw new FileUploadException("Extension not allowed");

    }
}
