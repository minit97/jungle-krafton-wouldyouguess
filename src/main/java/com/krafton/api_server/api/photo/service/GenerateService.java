package com.krafton.api_server.api.photo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateService {

    private final RestTemplate restTemplate;

    public MultipartFile processImage(MultipartFile image, MultipartFile mask, String prompt) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        body.add("mask", new ByteArrayResource(mask.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        body.add("prompt", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String flaskServerUrl = "http://3.38.98.172:5000/";
        ResponseEntity<byte[]> response = restTemplate.exchange(
                flaskServerUrl + "/inpaint",
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to process image: " + response.getStatusCode());
        }

        byte[] processedImageBytes = response.getBody();
        if (processedImageBytes == null) {
            throw new RuntimeException("No image data received from server");
        }

        return new MultipartFile() {
            @Override
            public String getName() {
                return "processedImage";
            }

            @Override
            public String getOriginalFilename() {
                return "processed_image.png";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return processedImageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return processedImageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return processedImageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(processedImageBytes);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                    fos.write(processedImageBytes);
                }
            }
        };
    }
}