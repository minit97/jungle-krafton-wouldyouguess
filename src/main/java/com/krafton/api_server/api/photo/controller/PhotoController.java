package com.krafton.api_server.api.photo.controller;

import com.krafton.api_server.api.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PhotoController {
    private final PhotoService photoService;





}
