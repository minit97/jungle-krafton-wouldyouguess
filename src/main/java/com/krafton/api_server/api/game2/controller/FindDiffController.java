package com.krafton.api_server.api.game2.controller;

import com.krafton.api_server.api.game2.service.FindDiffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.*;


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
    public ResponseEntity<FindDiffAiGeneratedImageResponseDto> uploadImage(
            @RequestParam("originalImage") MultipartFile originalImage,
            @RequestParam("maskingImage") MultipartFile maskingImage,
            @RequestParam("userId") Long userId,
            @RequestParam("gameId") Long gameId,
            @RequestParam("maskX1") Long maskX1,
            @RequestParam("maskY1") Long maskY1,
            @RequestParam("maskX2") Long maskX2,
            @RequestParam("maskY2") Long maskY2) {

        FindDiffImageUploadRequestDto request = FindDiffImageUploadRequestDto.builder()
                .originalImage(originalImage)
                .maskingImage(maskingImage)
                .userId(userId)
                .gameId(gameId)
                .maskX1(maskX1)
                .maskX2(maskX2)
                .maskY1(maskY1)
                .maskY2(maskY2)
                .build();

        FindDiffAiGeneratedImageResponseDto responseDto = findDiffService.callUploadImage(request);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/images/{gameId}")
    public ResponseEntity<List<FindDiffGameImagesDto>> callFindDiffGameImages(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        List<FindDiffGameImagesDto> response = findDiffService.getFindDiffGameImages(gameId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result/{gameId}")
    public ResponseEntity<List<FindDiffResultDto>> getFindDiffResultList(@PathVariable("gameId") Long gameId) {
        List<FindDiffResultDto> response = findDiffService.callFindDiffResultList(gameId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/score")
    public void updateChance(@RequestBody FindDiffScoreRequestDto request) {
        findDiffService.updateChance(request);
    }

}