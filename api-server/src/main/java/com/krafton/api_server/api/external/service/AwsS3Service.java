package com.krafton.api_server.api.external.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krafton.api_server.api.external.vo.AwsS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public AwsS3 uploadImageToS3(MultipartFile image, String directoryName) {
        File file = convertMultipartFileToFile(image)
                .orElseThrow(() -> new IllegalArgumentException("multipartFile image convert fail"));
        String objectname = randomFileName(file, directoryName);

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, objectname, file).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new RuntimeException("aws properties error");
        } finally {
            file.delete();
        }


        String path = amazonS3.getUrl(bucket, objectname).toString();

        return AwsS3.builder()
                .objectname(objectname)
                .path(path)
                .build();
    }

    private static Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            return Optional.of(file);
        } catch (IOException e) {
            throw new RuntimeException("failed to convert multipartFile to file", e);
        }
    }

    private static String randomFileName(File file, String dirName) {
        return dirName + "/" + UUID.randomUUID() + file.getName();
    }

    public void removeImageFromS3(String key) {
        if (!amazonS3.doesObjectExist(bucket, key)) {
            throw new AmazonS3Exception("aws S3 object : " + key + " does not exist!");
        }
        amazonS3.deleteObject(bucket, key);
    }
}