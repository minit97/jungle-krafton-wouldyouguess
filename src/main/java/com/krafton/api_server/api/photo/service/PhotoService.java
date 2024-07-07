package com.krafton.api_server.api.photo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoService {

    private final AwsS3Service awsS3Service;

}