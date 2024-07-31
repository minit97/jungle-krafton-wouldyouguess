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
    public ResponseEntity<?> callStartFindDiff(@RequestBody FindDiffRequestDto request) {
        Long gameId = findDiffService.startFindDiff(request);
        return ResponseEntity.ok(gameId);
    }

    @PostMapping("/upload")
    public ResponseEntity<FindDiffAiGeneratedImageResponseDto> callUploadImage(FindDiffImageUploadRequestDto request) {
        FindDiffAiGeneratedImageResponseDto response = findDiffService.callUploadImage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/images/{gameId}")
    public ResponseEntity<List<FindDiffGameImagesDto>> callFindDiffGameImages(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        List<FindDiffGameImagesDto> response = findDiffService.findDiffGameImages(gameId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result/{gameId}")
    public ResponseEntity<List<FindDiffResultDto>> callFindDiffResultList(@PathVariable("gameId") Long gameId) {
        List<FindDiffResultDto> response = findDiffService.findDiffResultList(gameId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/score")
    public void callUpdateChance(@RequestBody FindDiffScoreRequestDto request) {
        findDiffService.updateChance(request);
    }

}