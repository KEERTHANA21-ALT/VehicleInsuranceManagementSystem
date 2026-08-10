package com.springboot.insurance.dto.response;

public record UploadResponseDto(

        long id,
        String path,
        String fileName,
        String message

) {
}
