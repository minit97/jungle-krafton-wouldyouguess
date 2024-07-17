package com.krafton.api_server.api.game2.controller;

import com.krafton.api_server.api.game2.service.FindDiffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.FindDiffGeneratedImageResponseDto;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/findDiff")
@RestController
public class FindDiffController {

    private final FindDiffService findDiffService;

    @PostMapping("/start")
    public ResponseEntity<?> startFindDiff(@RequestBody FindDiffRequestDto request) {
        log.debug("Received startFindDiff request: {}", request);
        Long gameId = findDiffService.startFindDiff(request);
        return ResponseEntity.ok(gameId);
    }

    @PostMapping(value = "/inpaint", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveGeneratedImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("roomId") Long roomId,
            @RequestParam("userId") Long userId,
            @RequestParam("maskX1") Long maskX1,
            @RequestParam("maskY1") Long maskY1,
            @RequestParam("maskX2") Long maskX2,
            @RequestParam("maskY2") Long maskY2) throws IOException {

        FindDiffGeneratedImageRequestDto request = new FindDiffGeneratedImageRequestDto();
        request.setImage(image);
        request.setRoomId(roomId);
        request.setUserId(userId);
        request.setMaskX1(maskX1);
        request.setMaskY1(maskY1);
        request.setMaskX2(maskX2);
        request.setMaskY2(maskY2);

        log.debug("Received saveGeneratedImage request: {}", request);
        findDiffService.saveGeneratedImage(request);
        return ResponseEntity.ok("OK");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("roomId") Long roomId,
            @RequestParam("userId") Long userId) throws IOException {

        FindDiffImageRequestDto request = new FindDiffImageRequestDto();
        request.setImage(image);
        request.setRoomId(roomId);
        request.setUserId(userId);

        log.debug("Received saveImage request: {}", request);
        findDiffService.saveImage(request);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/og/{gameId}/{userId}")
    public ResponseEntity<List<String>> getOriginalImages(@PathVariable("gameId") Long gameId, @PathVariable("userId") Long userId) {
        List<String> originalImages = findDiffService.getOriginalImages(gameId, userId);
        log.warn("OriginalImages : {}", originalImages);
        return ResponseEntity.ok(originalImages);
    }

    @GetMapping("/gen/{gameId}/{userId}")
    public ResponseEntity<List<FindDiffGeneratedImageResponseDto>> getGeneratedImages(@PathVariable("gameId") Long gameId, @PathVariable("userId") Long userId) {
        List<FindDiffGeneratedImageResponseDto> generatedImages = findDiffService.getGeneratedImages(gameId, userId);
        log.warn("GeneratedImages : {}", generatedImages);
        return ResponseEntity.ok(generatedImages);
    }

}