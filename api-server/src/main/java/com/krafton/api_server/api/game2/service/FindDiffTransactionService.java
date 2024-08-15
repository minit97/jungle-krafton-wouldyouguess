package com.krafton.api_server.api.game2.service;

import com.krafton.api_server.api.external.vo.AwsS3;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindDiffTransactionService {
    @Transactional
    public void uploadS3RequestImageUrlUpdate(FindDiffUser user, AwsS3 original, AwsS3 masking) {
        user.updateUploadRequestImage(original, masking);
    }
}
