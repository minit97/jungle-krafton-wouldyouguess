package com.krafton.api_server.api.external.service;

import com.krafton.api_server.api.external.vo.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeneratedAiService {

    @Value("${jungle.krafton.ai-server}")
    private String flaskServerUrl;

    private final RestTemplate restTemplate;

    public MultipartFile processAiImage(MultipartFile image, Long maskX1, Long maskY1, Long maskX2, Long maskY2) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", toByteArrayResource(image));
        body.add("maskX1", maskX1);
        body.add("maskY1", maskY1);
        body.add("maskX2", maskX2);
        body.add("maskY2", maskY2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                flaskServerUrl + "/inpaint",
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("failed to process image : " + response.getStatusCode());
        }

        byte[] processedImageBytes = response.getBody();
        if (processedImageBytes == null) {
            throw new RuntimeException("no image data received from server");
        }

        MultipartFile multipartFile = new CustomMultipartFile(processedImageBytes, "processed_image.png", "image/png");
        return multipartFile;
    }



    private ByteArrayResource toByteArrayResource(MultipartFile image) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };

            return byteArrayResource;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ByteArrayResource from image", e);
        }
    }
}