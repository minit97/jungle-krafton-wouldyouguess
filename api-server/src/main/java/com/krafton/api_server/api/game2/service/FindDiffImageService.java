package com.krafton.api_server.api.game2.service;

import com.krafton.api_server.api.external.service.AwsS3Service;
import com.krafton.api_server.api.external.service.GeneratedAiService;
import com.krafton.api_server.api.external.vo.AwsS3;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import com.krafton.api_server.api.game2.dto.FindDiffRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindDiffImageService {
    private final AwsS3Service awsS3Service;
    private final GeneratedAiService generateService;

    @Async
    @Transactional
    public void uploadS3RequestImage(FindDiffUser user, MultipartFile originalImage, MultipartFile maskingImage) {
        AwsS3 originalS3 = awsS3Service.uploadImageToS3(originalImage, "findDiff_original");
        AwsS3 maskingS3 = awsS3Service.uploadImageToS3(maskingImage, "findDiff_masking");

        user.updateUploadRequestImage(originalS3, maskingS3);
//        uploadS3RequestImageUrlUpdate(user, originalS3, maskingS3);
    }

//    public void uploadS3RequestImageUrlUpdate(FindDiffUser user, AwsS3 original, AwsS3 masking) {
//        user.updateUploadRequestImage(original, masking);
//    }

    public void saveGeneratedAiImage(FindDiffUser user, FindDiffRequest.FindDiffImageUploadRequestDto request) {
        MultipartFile processedAiImage = generateService.processAiImage(
                request.getOriginalImage(),
                request.getMaskX1(),
                request.getMaskY1(),
                request.getMaskX2(),
                request.getMaskY2()
        );

        AwsS3 generatedAiS3 = awsS3Service.uploadImageToS3(processedAiImage, "findDiff_generated_ai");

        user.uploadAiGeneratedImage(generatedAiS3, request.getMaskX1(), request.getMaskY1(),request.getMaskX2(), request.getMaskY2());
    }
}
