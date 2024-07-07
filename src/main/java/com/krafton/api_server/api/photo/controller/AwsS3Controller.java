package com.krafton.api_server.api.photo.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krafton.api_server.api.photo.domain.AwsS3;
import com.krafton.api_server.api.photo.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/s3")
@RestController
public class AwsS3Controller {

    private final AmazonS3 amazonS3;
    private final AwsS3Service awsS3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping("/resource")
    public ResponseEntity<AwsS3> upload(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String objectKey = "upload/" + file.getOriginalFilename(); // 업로드할 파일의 경로와 파일명

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                objectKey,
                file.getInputStream(),
                objectMetadata
        );

        amazonS3.putObject(putObjectRequest);

        String fileUrl = amazonS3.getUrl(bucketName, objectKey).toString();
        AwsS3 awsS3 = AwsS3.builder()
                .key(objectKey)
                .path(fileUrl)
                .build();

        return ResponseEntity.ok(awsS3);
    }

    @DeleteMapping("/resource/{key}")
    public ResponseEntity<Void> delete(@PathVariable("key") String key) {
        try {
            awsS3Service.delete(key);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AmazonS3Exception e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
