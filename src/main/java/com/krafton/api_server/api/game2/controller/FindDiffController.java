package com.krafton.api_server.api.game2.controller;

import com.krafton.api_server.api.game2.dto.FindDiffResponse;
import com.krafton.api_server.api.game2.service.FindDiffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.FindDiffGeneratedImageResponseDto;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/findDiff")
@RestController
public class FindDiffController {

    private final FindDiffService findDiffService;

    @PostMapping("/start")
    public ResponseEntity<?> startFindDiff(@RequestBody FindDiffRequestDto request) {
        Long gameId = findDiffService.startFindDiff(request);
        return ResponseEntity.ok(gameId);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("originalImage") MultipartFile originalImage,
            @RequestParam("maskingImage") MultipartFile maskingImage,
            @RequestParam("userId") Long userId,
            @RequestParam("maskX1") Long maskX1,
            @RequestParam("maskY1") Long maskY1,
            @RequestParam("maskX2") Long maskX2,
            @RequestParam("maskY2") Long maskY2) throws IOException {

        FindDiffImageUploadRequestDto request = FindDiffImageUploadRequestDto.builder()
                .originalImage(originalImage)
                .maskingImage(maskingImage)
                .userId(userId)
                .maskX1(maskX1)
                .maskX2(maskX2)
                .maskY1(maskY1)
                .maskY2(maskY2)
                .build();

        findDiffService.callUploadImage(request);
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/original/image/{gameId}")
    public ResponseEntity<List<String>> getOriginalImages(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        List<String> originalImages = findDiffService.getOriginalImages(gameId, userId);
        return ResponseEntity.ok(originalImages);
    }

    @GetMapping("/generated/image/{gameId}")
    public ResponseEntity<List<FindDiffGeneratedImageResponseDto>> getGeneratedImages(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        List<FindDiffGeneratedImageResponseDto> generatedImages = findDiffService.getGeneratedImages(gameId, userId);
        return ResponseEntity.ok(generatedImages);
    }

    @GetMapping("/result/{gameId}")
    public ResponseEntity<List<FindDiffResultDto>> getFindDiffResultList(@PathVariable("gameId") Long gameId) {
        List<FindDiffResultDto> response = findDiffService.callFindDiffResultList(gameId);
        return ResponseEntity.ok(response);
    }

}